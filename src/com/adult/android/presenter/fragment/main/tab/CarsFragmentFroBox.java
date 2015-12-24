package com.adult.android.presenter.fragment.main.tab;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.AddToCartResponse;
import com.adult.android.entity.CalculateCartResponse;
import com.adult.android.entity.CartDTO;
import com.adult.android.entity.OnGetCartListResponse;
import com.adult.android.entity.ProductRule;
import com.adult.android.entity.SkuForCart;
import com.adult.android.model.CartModel;
import com.adult.android.model.CartModel.OnCalculateCartCompletedListener;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnGetCartListCompletedListener;
import com.adult.android.model.UserModel.OnUpdateCartCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.presenter.activity.MainActivity;
import com.adult.android.presenter.activity.OrderCommitActivity;
import com.adult.android.presenter.activity.ProductDetailsActivity2;
import com.adult.android.presenter.fragment.main.BaseTabFragment;
import com.adult.android.presenter.fragment.main.tab.adapter.CartListAdapterForBox;
import com.adult.android.presenter.fragment.main.tab.adapter.CartListAdapterForBox.OnDataChangeListener;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.adult.android.view.SampleDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CarsFragmentFroBox extends BaseTabFragment implements OnDataChangeListener, OnClickListener {

	public static final String INTENT_ACTION_REFRESH_CART = "INTENT_ACTION_refresh_cart";

	private View mainView;

	private PullToRefreshListView listView;

	private CartListAdapterForBox adapter;

	private CartDTO cartDto;

	private List<CartDTO> productList = new ArrayList<CartDTO>();

	private LoadingDialog loadingDialog;

	private ImageView imgCheckAll;

	private TextView txtAllAmount;

	private LinearLayout llytNoGoods;

	private Button btnSum, btnGoBuy;

	private int currentPage = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.cars_fragment_for_box, null);
		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	private void initViews() {
		initActivityTitle();
		loadingDialog = new LoadingDialog(getActivity());
		imgCheckAll = (ImageView) mainView.findViewById(R.id.cart_fragment_all_check);
		txtAllAmount = (TextView) mainView.findViewById(R.id.cart_fragment_txt_all_amount);
		btnSum = (Button) mainView.findViewById(R.id.cart_fragment_btn_sum);
		llytNoGoods = (LinearLayout) mainView.findViewById(R.id.cart_fragment_no_goods);
		btnGoBuy = (Button) mainView.findViewById(R.id.btn_go_buy);
		imgCheckAll.setOnClickListener(this);
		btnSum.setOnClickListener(this);
		btnGoBuy.setOnClickListener(this);
		listView = (PullToRefreshListView) mainView.findViewById(R.id.cart_fragment_listView);
		listView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				getDateList(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				getDateList(1);
			}
		});
		listView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				if (0 < productList.get((int) id).getCartSkuDTOList().size()) {
					Intent intent = new Intent();
					intent.putExtra(ProductDetailsActivity2.EXTRA_PRODUCT_ID, productList.get((int) id)
							.getCartSkuDTOList().get(0));
					intent.setClass(getActivity(), ProductDetailsActivity2.class);
					startActivity(intent);
				}
			}
		});
		getDateList(0);
	}

	private void initActivityTitle() {
		if (MainActivity.class == getActivity().getClass()) {
			((MainActivity) getActivity()).setActivityTitle("购物车");
			((MainActivity) getActivity()).setShowMiddleView();
		}
	}

	@Override
	public void onSkuCountChange(int productPosition, int skuPosition, String skuId, int count) {
		updateCart(productPosition, skuPosition, skuId, count);
	}

	@Override
	public void onChecedChange(int productPosition, int skuPosition, boolean isChecked) {
		// List<CartSkuDTO> skuList =
		// productList.get(productPosition).getCartSkuDTOList();
		// productList.get(productPosition).setSkuList(skuList);
		// adapter.notifyDataSetChanged();
		// getCartAmount();
	}

	@Override
	public void onSkuDelete(final int productPosition, final int skuPosition) {
		final SampleDialog dialog = new SampleDialog(getActivity()) {

			@Override
			public View getContentView() {
				return null;
			}
		};
		dialog.setTitleText("提醒", R.color.black);
		dialog.setContentText("确定要删除该商品吗？", R.color.gray);
		dialog.setTwoButton(R.string.cancel, R.color.black, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				dialog.dismiss();
			}
		}, R.string.ok, R.color.red, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				dialog.dismiss();
				UserModel.getInstance().updateCart(
						cartDto.getCartSkuDTOList().get(skuPosition).getMerchandise().getMerchandiseNumber(), "0",
						new OnUpdateCartCompletedListener() {

							@Override
							public void onSuccess(OnGetCartListResponse info) {
								refreshDate(0, info);
							}

							@Override
							public void onStart() {

							}

							@Override
							public void onHttpException(HttpResponseException e) {
								loadingDialog.dismiss();
							}

							@Override
							public void onFinish() {
								loadingDialog.dismiss();
							}

							@Override
							public void onFailed(ResponseException e) {
								loadingDialog.dismiss();
							}
						});
			}
		});
		dialog.show();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.cart_fragment_all_check:
			// checkAll(!isAllCheced);
			break;
		case R.id.cart_fragment_btn_sum:

			break;
		case R.id.btn_go_buy:
			if (getActivity().getClass() == MainActivity.class) {
				((MainActivity) getActivity()).getTabSwitcherFragment().updateTab(1);
			} else {
				Intent intent = new Intent(getActivity(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(MainActivity.SWITCH_INDEX, 1);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			initActivityTitle();
		}
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		if (GeneralTool.isEmpty(AgentApplication.get().getUserId())) {
			llytNoGoods.setVisibility(View.VISIBLE);
			ToastUtil.showToastShort(getActivity(), "请先登陆");
			return;
		}
		loadingDialog.show();
		UserModel.getInstance().getcartList(new OnGetCartListCompletedListener() {

			@Override
			public void onSuccess(OnGetCartListResponse info) {
				loadingDialog.dismiss();
				listView.onRefreshComplete();
				refreshDate(flag, info);
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onHttpException(HttpResponseException e) {
				loadingDialog.dismiss();
				listView.onRefreshComplete();
			}

			@Override
			public void onFinish() {
				loadingDialog.dismiss();
				listView.onRefreshComplete();
				llytNoGoods.setVisibility(View.VISIBLE);
			}

			@Override
			public void onFailed(ResponseException e) {
				loadingDialog.dismiss();
				listView.onRefreshComplete();
				llytNoGoods.setVisibility(View.VISIBLE);
			}
		});
	}

	/** 处理数据 */
	protected void refreshDate(int flag, OnGetCartListResponse info) {
		if (null == info.getData() || null == info.getData().getCartDTO()
				|| 0 == info.getData().getCartDTO().getCartSkuDTOList().size()) {
			llytNoGoods.setVisibility(View.VISIBLE);
			return;
		}
		cartDto = info.getData().getCartDTO();
		if (0 == flag) {
			productList = new ArrayList<CartDTO>();
			adapter = new CartListAdapterForBox(getActivity(), productList, CarsFragmentFroBox.this);
			listView.setAdapter(adapter);
		}
		productList.add(cartDto);
		if (null == adapter) {
			adapter = new CartListAdapterForBox(getActivity(), productList, CarsFragmentFroBox.this);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		txtAllAmount.setText(getResources().getString(R.string.rmb) + cartDto.getItemAmount());
	}

	/**
	 * 更新数量
	 * 
	 * @param skuPosition
	 * @param productPosition
	 */
	private void updateCart(final int productPosition, final int skuPosition, String skuId, final int count) {
		loadingDialog.show();
		UserModel.getInstance().updateCart(skuId, count + "", new OnUpdateCartCompletedListener() {

			@Override
			public void onSuccess(OnGetCartListResponse info) {
				refreshDate(0, info);
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onHttpException(HttpResponseException e) {
				loadingDialog.dismiss();
			}

			@Override
			public void onFinish() {
				loadingDialog.dismiss();
			}

			@Override
			public void onFailed(ResponseException e) {
				loadingDialog.dismiss();
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter(INTENT_ACTION_REFRESH_CART);
		getActivity().registerReceiver(broadcastReceiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	@Override
	public void onPromotionClick(ProductRule rule) {
		// showCartPopupWindow(rule);
	}

	/** 刷新购物车数量 */
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null)
				return;
			String action = intent.getAction();
			if (INTENT_ACTION_REFRESH_CART.equals(action)) {
				ToastUtil.showToastShort(getActivity(), "刷新");
			}
		}
	};
}
