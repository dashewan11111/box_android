package com.adult.android.presenter.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.AddressDto;
import com.adult.android.entity.CalculateCartResponse2;
import com.adult.android.entity.CommitOrderResponse;
import com.adult.android.entity.CouponDto;
import com.adult.android.entity.SkuForCart;
import com.adult.android.model.CartModel;
import com.adult.android.model.CartModel.OnOrderCommitCompletedListener;
import com.adult.android.model.OrderModel2;
import com.adult.android.model.OrderModel2.OnDeleteOrderCompletedListener;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.internet.bean.StatusInfo;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.Misc;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.CartPromotionPopupWindow;
import com.adult.android.view.CartPromotionPopupWindow.CartPromotionPopupWindowListener;
import com.adult.android.view.LoadingDialog;
import com.lidroid.xutils.BitmapUtils;

public class OrderCommitActivity extends BaseActivity implements OnClickListener {

	private CalculateCartResponse2 data;

	private List<SkuForCart> productList;

	private AddressDto address;

	private String skuIds;

	private boolean isQuickBuy;

	private TextView txtReceiverName, txtReceiverNum, txtAddress, txtPromotionName, txtProductAmount, txtTransFee,
			txtCoupon, txtShoudPay;

	private EditText edTxtMessage;

	private ImageView imageAfterPay, imageAliPay, imageWechatPay;

	private LinearLayout llytProductContainer, llytAddress;

	private RelativeLayout llytNewAddress;

	private BitmapUtils mBitmapUtils;

	private LoadingDialog loadingDialog;

	private int payWay = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("订单提交");
		loadingDialog = new LoadingDialog(this);
		mBitmapUtils = new BitmapUtils(this);
		initView();
	}

	private void initView() {
		txtReceiverName = (TextView) findViewById(R.id.txt_receiver_name);
		txtReceiverNum = (TextView) findViewById(R.id.txt_receiver_num);
		txtAddress = (TextView) findViewById(R.id.txt_receiver_address);
		llytProductContainer = (LinearLayout) findViewById(R.id.order_commit_prodcut_container);
		imageAfterPay = (ImageView) findViewById(R.id.pay_method_after_check);
		imageAfterPay.setImageResource(R.drawable.cart_product_select_on);
		imageAfterPay.setOnClickListener(this);
		imageAliPay = (ImageView) findViewById(R.id.pay_method_alipay_check);
		imageAliPay.setOnClickListener(this);
		imageWechatPay = (ImageView) findViewById(R.id.pay_method_wechat_check);
		imageWechatPay.setOnClickListener(this);
		txtPromotionName = (TextView) findViewById(R.id.sum_details_promotion_name);
		txtProductAmount = (TextView) findViewById(R.id.sum_details_product_amount);
		txtTransFee = (TextView) findViewById(R.id.sum_details_trans_fee);
		txtCoupon = (TextView) findViewById(R.id.sum_details_coupon);
		txtShoudPay = (TextView) findViewById(R.id.order_commit_shoud_pay);

		edTxtMessage = (EditText) findViewById(R.id.order_commit_message);

		findViewById(R.id.sum_details_llyt_coupon).setOnClickListener(this);
		findViewById(R.id.order_commit_btn_commit).setOnClickListener(this);
		llytAddress = (LinearLayout) findViewById(R.id.order_commit_layout_address);
		llytAddress.setOnClickListener(this);
		llytNewAddress = (RelativeLayout) findViewById(R.id.order_commit_layout_address_new);
		llytNewAddress.setOnClickListener(this);
		if (null == getIntent().getSerializableExtra("data")) {
			finish();
		}
		isQuickBuy = getIntent().getBooleanExtra("isQuickBuy`", false);
		data = (CalculateCartResponse2) getIntent().getSerializableExtra("data");
		txtProductAmount.setText(getResources().getString(R.string.rmb) + data.getProductAmount());
		double transFee = Double.parseDouble(data.getTransAmount());
		if (0 < transFee) {
			txtTransFee.setText(getResources().getString(R.string.rmb) + Misc.scale(transFee, 2) + "");
			findViewById(R.id.order_commit_trans_fee_included).setVisibility(View.GONE);
		} else {
			findViewById(R.id.order_commit_trans_fee_included).setVisibility(View.VISIBLE);
			txtTransFee.setText("包邮");
		}
		txtShoudPay.setText(getResources().getString(R.string.rmb)
				+ Misc.scale(Double.parseDouble(data.getTotalAmount()), 2));
		productList = (List<SkuForCart>) getIntent().getSerializableExtra("skuList");
		skuIds = data.getSkuIds();
		address = data.getDefaultAddress();
		if (null == address || GeneralTool.isEmpty(address.getAddressId())
				|| GeneralTool.isEmpty(address.getReceiverName()) || GeneralTool.isEmpty(address.getReceiveMb())) {
			llytNewAddress.setVisibility(View.VISIBLE);
			llytAddress.setVisibility(View.GONE);
		} else {
			llytAddress.setVisibility(View.VISIBLE);
			llytNewAddress.setVisibility(View.GONE);

			txtReceiverName.setText(address.getReceiverName());
			txtReceiverNum.setText(address.getReceiveMb());
			txtAddress.setText("收货地址：" + address.getProvinceName() + " " + address.getCityName() + " "
					+ address.getAreaName() + " " + address.getDetailAddress());
		}
		if (null == data.getEnableUse() || 0 == data.getEnableUse().size()) {
			txtCoupon.setText(data.getAvailableCouponCount() + "张可用优惠券");
		}
		if (null == productList) {
			finish();
		}
		if (2 >= productList.size()) {
			findViewById(R.id.order_commit_prodcut_more).setVisibility(View.GONE);
		} else {
			findViewById(R.id.order_commit_prodcut_more).setOnClickListener(this);
		}
		addProduct();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.pay_method_after_check:
			payWay = 0;
			imageAfterPay.setImageResource(R.drawable.cart_product_select_on);
			imageAliPay.setImageResource(R.drawable.cart_product_select_off);
			imageWechatPay.setImageResource(R.drawable.cart_product_select_off);
			break;
		case R.id.pay_method_alipay_check:
			payWay = 1;
			imageAfterPay.setImageResource(R.drawable.cart_product_select_off);
			imageAliPay.setImageResource(R.drawable.cart_product_select_on);
			imageWechatPay.setImageResource(R.drawable.cart_product_select_off);
			break;
		case R.id.pay_method_wechat_check:
			payWay = 2;
			imageAfterPay.setImageResource(R.drawable.cart_product_select_off);
			imageAliPay.setImageResource(R.drawable.cart_product_select_off);
			imageWechatPay.setImageResource(R.drawable.cart_product_select_on);
			break;
		case R.id.order_commit_prodcut_more:
			addAllProduct(true);
			view.setVisibility(View.GONE);
			break;
		case R.id.order_commit_btn_commit:
			if (View.VISIBLE == llytNewAddress.getVisibility()) {
				ToastUtil.showToastShort(this, "请填写收货地址");
				return;
			}
			commitOrder();
			break;
		case R.id.order_commit_layout_address:
			Intent intentEditAddress = new Intent(this, AddressEditActivity.class);
			intentEditAddress.putExtra("address", address);
			intentEditAddress.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivityForResult(intentEditAddress, 10);
			break;
		case R.id.order_commit_layout_address_new:
			Intent intentNewAddress = new Intent(this, AddressEditActivity.class);
			intentNewAddress.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivityForResult(intentNewAddress, 10);
			break;
		case R.id.sum_details_llyt_coupon:
			if (null == data.getEnableUse() || 0 == data.getEnableUse().size()) {
				ToastUtil.showToastShort(this, "无可用优惠券");
				return;
			}
			showAddressPopupWindow();
			break;
		default:
			break;
		}
	}

	private CartPromotionPopupWindow skuPopupWindow;

	private CouponDto currentCoupon;

	/**
	 * 显示地址选择弹框
	 */
	private void showAddressPopupWindow() {
		skuPopupWindow = new CartPromotionPopupWindow(this, ((BaseActivity) this).pop_layout, data.getEnableUse(),
				new CartPromotionPopupWindowListener() {

					@Override
					public void onPromotionSelected(CouponDto coupon) {
						ToastUtil.showToastShort(OrderCommitActivity.this, coupon.getCoupon_id());
						currentCoupon = coupon;
						txtCoupon.setText(getResources().getString(R.string.rmb) + coupon.getCoupon_amount());
					}

					@Override
					public void onPopupWindowDismiss() {

					}
				});
		skuPopupWindow.show(((BaseActivity) this).findViewById(R.id.base_view));
	}

	/**
	 * 提交订单
	 */
	private void commitOrder() {
		loadingDialog.show();
		String quickBuy = "0";
		String skuIdQuickBuy = skuIds;
		String qtyQuickyBuy = "";
		if (isQuickBuy) {
			quickBuy = "1";
			qtyQuickyBuy = productList.get(0).getQty() + "";
		}
		CartModel.getInstance().commitOrder(AgentApplication.get().getUserId(), skuIds,
				null == currentCoupon ? "" : currentCoupon.getCoupon_id(), edTxtMessage.getText().toString(), "0",
				address.getAddressId(), "0", quickBuy, skuIdQuickBuy, qtyQuickyBuy,
				new OnOrderCommitCompletedListener() {

					@Override
					public void onSuccess(CommitOrderResponse info) {
						loadingDialog.dismiss();
						if (null != info.getData()) {
							if (0 == payWay) {
								Intent intent = new Intent(OrderCommitActivity.this, OrderListActivity2.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
								startActivity(intent);
								finish();
							} else {
								startPay();
							}
						}
						finish();
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

	protected void startPay() {
		// CartModel.getInstance().
		ToastUtil.showToastShort(OrderCommitActivity.this, "去支付");
	}

	/** 添加商品列表 */
	private void addProduct() {
		if (2 < productList.size()) {
			// 只显示两个
			addAllProduct(false);
		} else {
			addAllProduct(true);
		}
	}

	private void addAllProduct(boolean isShowAll) {
		if (null != llytProductContainer) {
			llytProductContainer.removeAllViewsInLayout();
		}
		for (SkuForCart product : productList) {
			if (!isShowAll && 2 == llytProductContainer.getChildCount()) {
				findViewById(R.id.order_commit_prodcut_more).setVisibility(View.VISIBLE);
				return;
			}
			View view = getLayoutInflater().inflate(R.layout.item_product_list_order_commit, null);
			ImageView imageProduct = (ImageView) view.findViewById(R.id.item_product_order_commit_image);
			TextView txtProductName = (TextView) view.findViewById(R.id.item_product_order_commit_name);
			TextView txtProductPrice = (TextView) view.findViewById(R.id.item_product_order_commit_price);
			TextView txtProductFormat = (TextView) view.findViewById(R.id.item_product_order_commit_format);
			TextView txtProductCount = (TextView) view.findViewById(R.id.item_product_order_commit_count);
			mBitmapUtils.display(imageProduct, ServiceUrlConstants.getImageHost() + product.getImgUrl());
			txtProductName.setText(product.getItemName());
			txtProductPrice.setText(getResources().getString(R.string.rmb) + product.getPromotionPrice());
			txtProductFormat.setText(product.getSkuName());
			txtProductCount.setText("数量：" + product.getQty());

			llytProductContainer.addView(view);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// ToastUtil.showToastShort(this, requestCode + ", resultCode:"
		// + resultCode);
		switch (requestCode) {
		case 10:
			updateAddress(intent);
			break;

		default:
			break;
		}
	}

	/** 更新收货地址 */
	private void updateAddress(Intent intent) {
		if (null != intent) {
			llytNewAddress.setVisibility(View.GONE);
			llytAddress.setVisibility(View.VISIBLE);
			address = (AddressDto) intent.getSerializableExtra("address");
			txtReceiverName.setText(address.getReceiverName());
			txtReceiverNum.setText(address.getReceiveMb());
			txtAddress.setText("收货地址：" + address.getProvinceName() + " " + address.getCityName() + " "
					+ address.getAreaName() + " " + address.getDetailAddress());
			getLogisticPrice(address.getProvinceName());
		}
	}

	/** 获取运费 */
	private void getLogisticPrice(String province) {
		loadingDialog.show();
		OrderModel2.getInstance().getLogisticsPrice(province, new OnDeleteOrderCompletedListener() {

			@Override
			public void onSuccess(StatusInfo info) {
				loadingDialog.dismiss();
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

			}

			@Override
			public void onFailed(ResponseException e) {
				loadingDialog.dismiss();
				ToastUtil.showToastShort(OrderCommitActivity.this, e.getResultMsg());
			}
		});
	}

}
