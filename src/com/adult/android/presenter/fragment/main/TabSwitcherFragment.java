package com.adult.android.presenter.fragment.main;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.model.CartModel;
import com.adult.android.model.constants.SharedPreferencesConstants;
import com.adult.android.utils.SharedPreferencesUtil;

public class TabSwitcherFragment extends BaseFragment implements
		View.OnClickListener {
	public static final String INTENT_ACTION_REFRESH_CART_COUNT = "INTENT_ACTION_refresh_cart_count";
	public static final String INTENT_EXTRA_CART_COUNT = "INTENT_EXTRA_refresh_cart_count";
	private View mView;
	private TextView txtCarsCount;
	private View tabHome, tabCategory, tabCart, tabMy, tabCountry;
	// private AnimationDrawable frameAnimation;
	private List<OnTabSelectListener> onTabSelectListeners = new ArrayList<OnTabSelectListener>();
	private int currentIndex = -1;

	public void addOnTabSelectListener(int index,
			OnTabSelectListener mOnTabSelectListener) {
		onTabSelectListeners.add(index, mOnTabSelectListener);
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null)
				return;
			String action = intent.getAction();
			if (INTENT_ACTION_REFRESH_CART_COUNT.equals(action)) {
				String number = CartModel.formatCartCount(intent
						.getStringExtra(INTENT_EXTRA_CART_COUNT));
				if (!TextUtils.isEmpty(number)) {
					txtCarsCount.setVisibility(View.VISIBLE);
					txtCarsCount.setText(number);
				} else {
					txtCarsCount.setVisibility(View.GONE);
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter(
				INTENT_ACTION_REFRESH_CART_COUNT);
		getActivity().registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.main_tab_layout, container, false);
		tabHome = mView.findViewById(R.id.main_tab_home);
		tabCategory = mView.findViewById(R.id.main_tab_category);
		tabCountry = mView.findViewById(R.id.main_tab_country);

		// frameAnimation = (AnimationDrawable) tabCountry.getBackground();

		tabCart = mView.findViewById(R.id.main_tab_cart);
		tabMy = mView.findViewById(R.id.main_tab_my);
		txtCarsCount = (TextView) mView.findViewById(R.id.txt_cars_count);
		int count = SharedPreferencesUtil.getSharedPreferences(
				SharedPreferencesConstants.FILES.FILE_SHOPPING_CART,
				SharedPreferencesConstants.PARAMS.CART_COUNT, 0);
		String number = CartModel.formatCartCount(count + "");
		if (!TextUtils.isEmpty(number)) {
			txtCarsCount.setVisibility(View.VISIBLE);
			txtCarsCount.setText(number);
		} else {
			txtCarsCount.setVisibility(View.GONE);
		}

		tabHome.setOnClickListener(this);
		tabCategory.setOnClickListener(this);
		tabCountry.setOnClickListener(this);
		tabCart.setOnClickListener(this);
		tabMy.setOnClickListener(this);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		updateTab(0);
	}

	public void updateTab(int selectedIndex) {
		if (currentIndex == selectedIndex) {
			onTabSelectListeners.get(selectedIndex).onTabReselected();
		} else {
			if (currentIndex >= 0) {
				onTabSelectListeners.get(currentIndex).onTabUnselected();
				switch (currentIndex) {
				case 0:
					tabHome.setSelected(false);
					break;
				case 1:
					tabCategory.setSelected(false);
					break;
				case 2:
					// frameAnimation.stop();
					tabCountry.setSelected(false);
					break;
				case 3:
					tabCart.setSelected(false);
					break;
				case 4:
					tabMy.setSelected(false);
					break;
				}
			}
			currentIndex = selectedIndex;
			onTabSelectListeners.get(selectedIndex).onTabSelected();
			switch (currentIndex) {
			case 0:
				tabHome.setSelected(true);
				break;
			case 1:
				tabCategory.setSelected(true);
				break;
			case 2:
				// frameAnimation.start();
				tabCountry.setSelected(true);
				break;
			case 3:
				tabCart.setSelected(true);
				break;
			case 4:
				tabMy.setSelected(true);
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (id) {
		case R.id.main_tab_home:
			updateTab(0);
			break;
		case R.id.main_tab_category:
			updateTab(1);
			break;
		case R.id.main_tab_country:
			updateTab(2);
			break;
		case R.id.main_tab_cart:
			updateTab(3);
			break;
		case R.id.main_tab_my:
			updateTab(4);
			break;
		}
	}

	public interface OnTabSelectListener {
		void onTabSelected();

		void onTabUnselected();

		void onTabReselected();
	}

}
