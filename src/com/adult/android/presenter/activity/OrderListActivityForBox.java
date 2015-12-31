package com.adult.android.presenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.adult.android.R;
import com.adult.android.presenter.fragment.OrderListFragmenForBox;

public class OrderListActivityForBox extends BaseActivity implements
		OnClickListener {

	public static final int REQUEST_CODE = 11;

	public static final String Tag = "OrderListActivity2";

	public static final String ALL = "FragmentAll";

	public static final String UN_PAY = "FragmentUnPay";

	public static final String UN_POST = "FragmentUnPost";

	public static final String UN_RECEIVE = "FragmentUnReceive";

	public static final String UN_COMMENT = "FragmentUnComment";

	public static final String EXTRA_COMMUNITY_ID = "extra_community_id";

	// private View viewAll, viewUnPay, viewUnPost, viewUnReceive,
	// viewUnComment;

	private FragmentManager mFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setShowTitleBar(true);
		setActivityTitle("待支付订单");
		initView();
	}

	/** 初始化 */
	private void initView() {
		// findViewById(R.id.order_list_tab_all).setOnClickListener(this);
		// findViewById(R.id.order_list_tab_unpay).setOnClickListener(this);
		// findViewById(R.id.order_list_tab_unpost).setOnClickListener(this);
		// findViewById(R.id.order_list_tab_unreceive).setOnClickListener(this);
		// findViewById(R.id.order_list_tab_uncomment).setOnClickListener(this);
		// viewAll = (View) findViewById(R.id.order_list_tab_view_all);
		// viewUnPay = (View) findViewById(R.id.order_list_tab_view_unpay);
		// viewUnPost = (View) findViewById(R.id.order_list_tab_view_unpost);
		// viewUnReceive = (View)
		// findViewById(R.id.order_list_tab_view_unreceive);
		// viewUnComment = (View)
		// findViewById(R.id.order_list_tab_view_uncomment);
		mFragmentManager = getSupportFragmentManager();
		switchTab(ALL);
	}

	/** 切换页面 */
	private void switchTab(String tag) {
		switchView(tag);
		Fragment fragment = mFragmentManager.findFragmentByTag(tag);
		FragmentTransaction mFragmentTransaction = mFragmentManager
				.beginTransaction();
		if (null == fragment) {
			fragment = new OrderListFragmenForBox();
			Bundle data = new Bundle();
			if (ALL.equals(tag)) {
				data.putString("type", "-1");
			} else if (UN_PAY.equals(tag)) {
				data.putString("type", "0");
			} else if (UN_POST.equals(tag)) {
				data.putString("type", "2");
			} else if (UN_RECEIVE.equals(tag)) {
				data.putString("type", "4");
			} else if (UN_COMMENT.equals(tag)) {
				data.putString("type", "5");
			} else {
				data.putString("type", "-1");
			}
			fragment.setArguments(data);
			mFragmentTransaction
					.add(R.id.order_list_content_box, fragment, tag);
		} else {
			mFragmentTransaction.show(fragment);
		}
		mFragmentTransaction.commit();
	}

	/** 显示/隐藏背景条 */
	private void switchView(String tag) {

	}

	/** 隐藏Fragment */
	private void hideFragmet(String tag) {
		Fragment fragment = mFragmentManager.findFragmentByTag(tag);
		FragmentTransaction mFragmentTransaction = mFragmentManager
				.beginTransaction();
		if (null != fragment && !fragment.isHidden()) {
			mFragmentTransaction.hide(fragment);
			mFragmentTransaction.commit();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		setResult(REQUEST_CODE);
	}
}
