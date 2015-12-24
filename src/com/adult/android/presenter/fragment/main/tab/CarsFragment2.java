package com.adult.android.presenter.fragment.main.tab;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.adult.android.entity.CartProductListResponse;
import com.adult.android.entity.CartProductListResponse2;
import com.adult.android.entity.GetCartAmountResponse;
import com.adult.android.entity.ProductForCart;
import com.adult.android.entity.ProductRule;
import com.adult.android.entity.SkuForCart;
import com.adult.android.model.CartModel;
import com.adult.android.model.CartModel.OnCalculateCartCompletedListener;
import com.adult.android.model.CartModel.OnGetCartAmountCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.presenter.activity.MainActivity;
import com.adult.android.presenter.activity.OrderCommitActivity;
import com.adult.android.presenter.activity.ProductDetailsActivity2;
import com.adult.android.presenter.fragment.main.BaseTabFragment;
import com.adult.android.presenter.fragment.main.tab.adapter.CartListAdapter;
import com.adult.android.presenter.fragment.main.tab.adapter.CartListAdapter.OnDataChangeListener;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.CartPromotionPopupWindow;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CarsFragment2 extends BaseTabFragment implements OnDataChangeListener, OnClickListener {

	public static final String INTENT_ACTION_REFRESH_CART = "INTENT_ACTION_refresh_cart";

	private View mainView;

	private PullToRefreshListView listView;

	private CartListAdapter adapter;

	private CartProductListResponse2 cartDto;

	private List<ProductForCart> productList = new ArrayList<ProductForCart>();

	private LoadingDialog loadingDialog;

	private ImageView imgCheckAll;

	private TextView txtAllAmount;

	private LinearLayout llytNoGoods;

	private Button btnSum, btnGoBuy;

	private int currentPage = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.cars_fragment2, null);
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
				if (0 < productList.get((int) id).getSkuList().size()) {
					Intent intent = new Intent();
					intent.putExtra(ProductDetailsActivity2.EXTRA_PRODUCT_ID, productList.get((int) id).getSkuList()
							.get(0));
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
		List<SkuForCart> skuList = productList.get(productPosition).getSkuList();
		skuList.get(skuPosition).setChecked(isChecked);
		productList.get(productPosition).setSkuList(skuList);
		adapter.notifyDataSetChanged();
		getCartAmount();
	}

	@Override
	public void onSkuDelete(final int productPosition, final int skuPosition) {
		CartModel.getInstance().deleteCartProduct(AgentApplication.get().getUserId(),
				productList.get(productPosition).getSkuList().get(skuPosition).getSkuId(),
				productList.get(productPosition).getSkuList().get(skuPosition).getQty() + "",
				new CartModel.OnAddToCartCompletedListener() {

					@Override
					public void onSuccess(AddToCartResponse info) {
						// List<SkuForCart> skuList = productList.get(
						// productPosition).getSkuList();
						// skuList.remove(skuPosition);
						// if (0 == skuList.size()) {
						// productList.remove(productPosition);
						// }
						// productList.get(productPosition).setSkuList(skuList);
						// adapter = new CartListAdapter(getActivity(),
						// productList, CarsFragment2.this);
						// listView.setAdapter(adapter);
						getDateList(0);
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailed(ResponseException e) {
						// TODO Auto-generated method stub

					}
				});
	}

	/** 计算金额 */
	private void getCartAmount() {
		CartModel.getInstance().getCartAmount(AgentApplication.get().getUserId(), getCartSkus(),
				new OnGetCartAmountCompletedListener() {

					@Override
					public void onSuccess(GetCartAmountResponse info) {
						if (null != info.getData().getTotalAmount()) {
							txtAllAmount.setText(getResources().getString(R.string.rmb)
									+ info.getData().getTotalAmount());
						} else {
							txtAllAmount.setText(getResources().getString(R.string.rmb) + "0.00");
						}
						isAllChecked();
					}

					@Override
					public void onFailed(ResponseException e) {
						txtAllAmount.setText(getResources().getString(R.string.rmb) + "0.00");
						isAllChecked();
					}

					@Override
					public void onHttpException(HttpResponseException e) {

					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {

					}
				});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.cart_fragment_all_check:
			checkAll(!isAllCheced);
			break;
		case R.id.cart_fragment_btn_sum:
			sumCars();
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

	private boolean isAllCheced = true;

	/** 全选 */
	private void checkAll(boolean checked) {
		isAllCheced = checked;
		if (null == productList) {
			return;
		}
		for (int i = 0; i < productList.size(); i++) {
			if (null == productList.get(i)) {
				return;
			}
			for (int j = 0; j < productList.get(i).getSkuList().size(); j++) {
				productList.get(i).getSkuList().get(j).setChecked(isAllCheced);
			}
		}
		if (isAllCheced) {
			imgCheckAll.setImageResource(R.drawable.cart_product_select_on);
		} else {
			imgCheckAll.setImageResource(R.drawable.cart_product_select_off);
		}
		adapter.notifyDataSetChanged();
	}

	/** 检测是否是全部选中。是：全选按钮置为true */
	private void isAllChecked() {
		for (ProductForCart product : productList) {
			for (SkuForCart sku : product.getSkuList()) {
				if (!sku.isChecked()) {
					if (isAllCheced) {
						imgCheckAll.setImageResource(R.drawable.cart_product_select_off);
					}
					return;
				}
			}
		}
		imgCheckAll.setImageResource(R.drawable.cart_product_select_on);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// checkAll(true);
			initActivityTitle();
		}
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

	/** 获取数据 */
	protected void getDateList(final int flag) {
		if (GeneralTool.isEmpty(AgentApplication.get().getUserId())) {
			llytNoGoods.setVisibility(View.VISIBLE);
			ToastUtil.showToastShort(getActivity(), "请先登陆");
			return;
		}
		loadingDialog.show();
		CartModel.getInstance().getCartList(AgentApplication.get().getUserId(), currentPage + "",
				new CartModel.OnGetCartListCompletedListener() {

					@Override
					public void onSuccess(CartProductListResponse info) {
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
						llytNoGoods.setVisibility(View.VISIBLE);
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
	protected void refreshDate(int flag, CartProductListResponse info) {
		cartDto = info.getData();
		if (null == cartDto || null == cartDto.getProductList() || 0 == cartDto.getProductList().size()) {
			llytNoGoods.setVisibility(View.VISIBLE);
			return;
		}
		if (0 == flag) {
			productList = new ArrayList<ProductForCart>();
			adapter = new CartListAdapter(getActivity(), productList, CarsFragment2.this);
			listView.setAdapter(adapter);
		}
		productList.addAll(cartDto.getProductList());
		if (null == adapter) {
			adapter = new CartListAdapter(getActivity(), productList, CarsFragment2.this);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		txtAllAmount.setText(cartDto.getTotalAmount());
	}

	/**
	 * 更新数量
	 * 
	 * @param skuPosition
	 * @param productPosition
	 */
	private void updateCart(final int productPosition, final int skuPosition, String skuId, final int count) {
		loadingDialog.show();
		CartModel.getInstance().addToCart(AgentApplication.get().getUserId(), skuId, "" + count,
				new CartModel.OnAddToCartCompletedListener() {

					@Override
					public void onSuccess(AddToCartResponse info) {
						productList.get(productPosition).getSkuList().get(skuPosition).setQty(count);
						adapter.notifyDataSetChanged();
						loadingDialog.dismiss();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
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

	/**
	 * 结算
	 * 
	 * @param selectedList
	 */
	private void calculateCarsAmount(String skuIds, final List<SkuForCart> selectedList) {
		loadingDialog.show();
		String quickBuy = "0", skuIdQuickBuy = "", qtyQuickBuy = "";
		CartModel.getInstance().calculateCart(AgentApplication.get().getUserId(), skuIds, quickBuy, skuIdQuickBuy,
				qtyQuickBuy, new OnCalculateCartCompletedListener() {

					@Override
					public void onSuccess(CalculateCartResponse info) {
						loadingDialog.dismiss();
						if (null != info.getData()) {
							Intent intent = new Intent();
							intent.setClass(getActivity(), OrderCommitActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
							intent.putExtra("skuList", (Serializable) selectedList);
							intent.putExtra("data", info.getData());
							startActivity(intent);
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailed(ResponseException e) {
						// TODO Auto-generated method stub

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
		showCartPopupWindow(rule);
	}

	private CartPromotionPopupWindow skuPopupWindow;

	private void showCartPopupWindow(ProductRule rule) {
		// skuPopupWindow = new CartPromotionPopupWindow(getActivity(),
		// ((BaseActivity) getActivity()).pop_layout, rule,
		// new CartPromotionPopupWindowListener() {
		//
		// @Override
		// public void onPromotionSelected(ProductRule rule) {
		// ToastUtil.showToastShort(getActivity(),
		// rule.getRuleName());
		// }
		//
		// @Override
		// public void onPopupWindowDismiss() {
		//
		// }
		// });
		// skuPopupWindow.show(((BaseActivity) getActivity())
		// .findViewById(R.id.base_view));
	}

	/** 结算 */
	private void sumCars() {
		StringBuffer sb = new StringBuffer();
		List<SkuForCart> selectedList = new ArrayList<SkuForCart>();
		for (ProductForCart product : productList) {
			for (SkuForCart sku : product.getSkuList()) {
				if (sku.isChecked()) {
					selectedList.add(sku);
					sb.append(sku.getSkuId());
					sb.append("-");
				}
			}
		}
		if (0 == selectedList.size()) {
			ToastUtil.showToastShort(getActivity(), "请至少选择一件商品");
			return;
		}
		calculateCarsAmount(sb.substring(0, sb.length() - 1), selectedList);
	}

	private String getCartSkus() {
		StringBuffer sb = new StringBuffer();
		List<SkuForCart> selectedList = new ArrayList<SkuForCart>();
		for (ProductForCart product : productList) {
			for (SkuForCart sku : product.getSkuList()) {
				if (sku.isChecked()) {
					selectedList.add(sku);
					sb.append(sku.getSkuId());
					sb.append("-");
				}
			}
		}
		if (0 == sb.length()) {
			return "";
		}
		return sb.substring(0, sb.length() - 1);
	}
}
