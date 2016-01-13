package com.adult.android.presenter.fragment.main.tab;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.CartDTO;
import com.adult.android.entity.OnConnectUserResponse;
import com.adult.android.entity.OnGetCartListResponse;
import com.adult.android.entity.ProductRule;
import com.adult.android.entity.UserForBox;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnAddToCartCompletedListener;
import com.adult.android.model.UserModel.OnConnectUserCompletedListener;
import com.adult.android.model.UserModel.OnGetCartListCompletedListener;
import com.adult.android.model.UserModel.OnUpdateCartCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.presenter.activity.CarsActivityFroBox;
import com.adult.android.presenter.activity.MipcaActivityCapture;
import com.adult.android.presenter.activity.OrderListActivityForBox;
import com.adult.android.presenter.fragment.main.BaseTabFragment;
import com.adult.android.presenter.fragment.main.tab.adapter.CartListAdapterForBox;
import com.adult.android.presenter.fragment.main.tab.adapter.CartListAdapterForBox.OnDataChangeListener;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.Misc;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.adult.android.view.SampleDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CarsFragmentFroBox extends BaseTabFragment implements
		OnDataChangeListener, OnClickListener {

	private final static int LOADING_FOR_PRODUCT = 29;

	private final static int SCANNIN_GREQUEST_CODE = 1;

	public static final String INTENT_ACTION_REFRESH_CART = "INTENT_ACTION_refresh_cart";

	private View mainView;

	private PullToRefreshListView listView;

	private CartListAdapterForBox adapter;

	private CartDTO cartDto;

	private UserForBox user;

	private List<CartDTO> productList = new ArrayList<CartDTO>();

	private LoadingDialog loadingDialog;

	private TextView txtAllAmount, txtDutyFree, txtItemsPrice, txtSave,
			txtShippingFee, txtCheckOut;

	private LinearLayout llytNoGoods, llytScan;

	private Button btnGoBuy;

	private int currentPage = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.cars_fragment_for_box, null);
		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		broadcastReceiver = new MyBroadCastReceive();
		IntentFilter filter = new IntentFilter("com.box.android");
		// 注册广播接收器
		getActivity().registerReceiver(broadcastReceiver, filter);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initViews() {
		initActivityTitle();
		loadingDialog = new LoadingDialog(getActivity());

		txtAllAmount = (TextView) mainView
				.findViewById(R.id.cart_fragment_txt_all_amount);

		llytNoGoods = (LinearLayout) mainView
				.findViewById(R.id.cart_fragment_no_goods);
		btnGoBuy = (Button) mainView.findViewById(R.id.btn_go_buy);
		mainView.findViewById(R.id.btn_band).setOnClickListener(this);

		txtDutyFree = (TextView) mainView.findViewById(R.id.cart_box_duty_free);
		txtItemsPrice = (TextView) mainView
				.findViewById(R.id.cart_box_items_price);
		txtSave = (TextView) mainView.findViewById(R.id.cart_box_save);
		txtShippingFee = (TextView) mainView
				.findViewById(R.id.cart_box_shipping_fee);
		txtCheckOut = (TextView) mainView
				.findViewById(R.id.cart_box_txt_checkout);
		llytScan = (LinearLayout) mainView
				.findViewById(R.id.cars_fragment_box_scan);

		btnGoBuy.setOnClickListener(this);
		txtCheckOut.setOnClickListener(this);
		llytScan.setOnClickListener(this);
		listView = (PullToRefreshListView) mainView
				.findViewById(R.id.cart_fragment_listView);
		listView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getDateList(0);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getDateList(1);
			}
		});
		getDateList(0);
	}

	private void initActivityTitle() {

	}

	@Override
	public void onSkuCountChange(int productPosition, int skuPosition,
			String skuId, int count) {
		updateCart(productPosition, skuPosition, skuId, count);
	}

	@Override
	public void onChecedChange(int productPosition, int skuPosition,
			boolean isChecked) {
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
		dialog.setTwoButton(R.string.cancel, R.color.black,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						dialog.dismiss();
					}
				}, R.string.ok, R.color.red,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						dialog.dismiss();
						UserModel.getInstance().updateCart(
								cartDto.getCartSkuDTOList().get(skuPosition)
										.getMerchandise()
										.getMerchandiseNumber(), "0",
								new OnUpdateCartCompletedListener() {

									@Override
									public void onSuccess(
											OnGetCartListResponse info) {
										if (null == info.getData()
												|| null == info.getData()
														.getCartDTO()
												|| 0 == info.getData()
														.getCartDTO()
														.getCartSkuDTOList()
														.size()) {
											llytNoGoods
													.setVisibility(View.VISIBLE);
											return;
										}
										cartDto = info.getData().getCartDTO();
										refreshDate(0);
									}

									@Override
									public void onStart() {

									}

									@Override
									public void onHttpException(
											HttpResponseException e) {
										loadingDialog.dismiss();
										ToastUtil.showToastShort(getActivity(),
												e.getResultMsg());
									}

									@Override
									public void onFinish() {
										loadingDialog.dismiss();
									}

									@Override
									public void onFailed(ResponseException e) {
										loadingDialog.dismiss();
										ToastUtil.showToastShort(getActivity(),
												e.getResultMsg());
										refreshDate(0);
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
		case R.id.cars_fragment_box_scan:
			if (View.VISIBLE == llytNoGoods.getVisibility()) {
				return;
			}
			// 扫描商品
			Intent intent = new Intent();
			intent.setClass(getActivity(), MipcaActivityCapture.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			break;
		case R.id.btn_go_buy:
			// addToCart();
			getDateList(0);
			break;
		case R.id.btn_band:
			loadingDialog.show();
			UserModel.getInstance().bandUser(
					"db143c39-83f4-4bf1-8993-a84decc42f7b",
					new OnConnectUserCompletedListener() {

						@Override
						public void onSuccess(OnConnectUserResponse info) {
							loadingDialog.dismiss();
							ToastUtil.showToastShort(getActivity(), "绑定成功");
							getDateList(0);
						}

						@Override
						public void onFailed(ResponseException e) {

						}

						@Override
						public void onHttpException(HttpResponseException e) {
							loadingDialog.dismiss();
						}

						@Override
						public void onStart() {

						}

						@Override
						public void onFinish() {
							loadingDialog.dismiss();
						}

					});
			break;
		case R.id.cart_box_txt_checkout:
			if (View.VISIBLE == llytNoGoods.getVisibility()) {
				return;
			}
			Intent intentOrder = new Intent(getActivity(),
					OrderListActivityForBox.class);
			intentOrder.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			getActivity().startActivityForResult(intentOrder,
					OrderListActivityForBox.REQUEST_CODE);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case OrderListActivityForBox.REQUEST_CODE:
			getDateList(0);// 刷新
			break;
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == android.app.Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				ToastUtil.showToastShort(getActivity(),
						bundle.getString("result"));
				loadingDialog.show();
				Message msg2 = new Message();
				msg2.obj = bundle.getString("result");
				msg2.what = LOADING_FOR_PRODUCT;
				mHandler.sendMessageDelayed(msg2, 500);
			}
			break;
		default:
			break;
		}
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		if (GeneralTool.isEmpty(AgentApplication.get().getShopperId())) {
			llytNoGoods.setVisibility(View.VISIBLE);
			ToastUtil.showToastShort(getActivity(), "请先登陆");
			return;
		}
		loadingDialog.show();
		UserModel.getInstance().getcartList(
				new OnGetCartListCompletedListener() {

					@Override
					public void onSuccess(OnGetCartListResponse info) {
						loadingDialog.dismiss();
						listView.onRefreshComplete();
						if (null == info.getData()) {
							return;
						}
						user = info.getData().getUser();
						setUserTitle();
						if (null == info.getData().getCartDTO()
								|| 0 == info.getData().getCartDTO()
										.getCartSkuDTOList().size()) {
							llytNoGoods.setVisibility(View.VISIBLE);
							return;
						}
						AgentApplication.get().setCustomerId(
								null == user ? "" : user.getUserId());
						cartDto = info.getData().getCartDTO();
						refreshDate(flag);

					}

					@Override
					public void onStart() {

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						loadingDialog.dismiss();
						listView.onRefreshComplete();
						ToastUtil.showToastShort(getActivity(),
								e.getResultMsg());
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
						ToastUtil.showToastShort(getActivity(),
								e.getResultMsg());
						refreshDate(0);
					}
				});
	}

	/** 添加购物车 */
	private void addToCart() {
		UserModel.getInstance().addToCart(new OnAddToCartCompletedListener() {

			@Override
			public void onSuccess(OnGetCartListResponse info) {
				loadingDialog.dismiss();
				getDateList(0);
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onHttpException(HttpResponseException e) {
				loadingDialog.dismiss();
				ToastUtil.showToastShort(getActivity(), e.getResultMsg());
			}

			@Override
			public void onFinish() {
				loadingDialog.dismiss();

			}

			@Override
			public void onFailed(ResponseException e) {
				loadingDialog.dismiss();
				ToastUtil.showToastShort(getActivity(), e.getResultMsg());
			}
		});
	}

	/** 处理数据 */
	protected void refreshDate(int flag) {
		if (null == cartDto) {
			return;
		}
		// setUserTitle();
		llytNoGoods.setVisibility(View.GONE);
		if (0 == flag) {
			productList = new ArrayList<CartDTO>();
			adapter = new CartListAdapterForBox(getActivity(), productList,
					CarsFragmentFroBox.this);
			listView.setAdapter(adapter);
		}
		productList.add(cartDto);
		if (null == adapter) {
			adapter = new CartListAdapterForBox(getActivity(), productList,
					CarsFragmentFroBox.this);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		// txtAllAmount.setText(getResources().getString(R.string.rmb)
		// + cartDto.getItemAmount());
		txtDutyFree
				.setText(getResources().getString(R.string.euro)
						+ Misc.scale(
								Double.parseDouble(cartDto.getPayAmount() + ""),
								2) + "");
		txtItemsPrice.setText(getResources().getString(R.string.euro)
				+ Misc.scale(Double.parseDouble(cartDto.getItemAmount() + ""),
						2));
		txtSave.setText(getResources().getString(R.string.euro)
				+ Misc.scale(Double.parseDouble(cartDto.getSaveAmount() + ""),
						2));
		txtShippingFee.setText(getResources().getString(R.string.euro)
				+ Misc.scale(10d, 2));
	}

	private void setUserTitle() {
		((CarsActivityFroBox) getActivity()).setActivityTitle(user
				.getUserName() + " 的购物车");
		((CarsActivityFroBox) getActivity())
				.setTitleOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intentOrder = new Intent(getActivity(),
								OrderListActivityForBox.class);
						intentOrder.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						getActivity().startActivityForResult(intentOrder,
								OrderListActivityForBox.REQUEST_CODE);
					}
				});
	}

	/**
	 * 更新数量
	 * 
	 * @param skuPosition
	 * @param productPosition
	 */
	private void updateCart(final int productPosition, final int skuPosition,
			String skuId, final int count) {
		loadingDialog.show();
		UserModel.getInstance().updateCart(skuId, count + "",
				new OnUpdateCartCompletedListener() {

					@Override
					public void onSuccess(OnGetCartListResponse info) {
						loadingDialog.dismiss();
						if (null == info.getData()
								|| null == info.getData().getCartDTO()
								|| 0 == info.getData().getCartDTO()
										.getCartSkuDTOList().size()) {
							llytNoGoods.setVisibility(View.VISIBLE);
							return;
						}
						cartDto = info.getData().getCartDTO();
						refreshDate(0);
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(getActivity(),
								e.getResultMsg());
					}

					@Override
					public void onFinish() {
						loadingDialog.dismiss();
					}

					@Override
					public void onFailed(ResponseException e) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(getActivity(),
								e.getResultMsg());
						// refreshDate(0);
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
	private MyHandler mHandler = new MyHandler();

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			loadingDialog.dismiss();
			addToCart();
			ToastUtil.showToastShort(getActivity(), (String) msg.obj);
		}
	}

	class MyBroadCastReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (null == intent) {
				return;
			}
			loadingDialog.show();
			Message msg2 = new Message();
			msg2.obj = intent.getStringExtra("scan_result");
			msg2.what = LOADING_FOR_PRODUCT;
			mHandler.sendMessageDelayed(msg2, 500);
			return;
		}
	}
}
