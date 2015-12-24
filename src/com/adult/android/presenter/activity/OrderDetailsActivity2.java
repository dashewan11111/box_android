package com.adult.android.presenter.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.AddressDto;
import com.adult.android.entity.NewOrderDetailResponse;
import com.adult.android.entity.OrderDtoForList;
import com.adult.android.entity.Sku2;
import com.adult.android.model.OrderModel2;
import com.adult.android.model.OrderModel2.OnDeleteOrderCompletedListener;
import com.adult.android.model.OrderModel2.OnGetOrderDetailCompletedListener;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.internet.bean.StatusInfo;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.utils.Misc;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.adult.android.view.SampleDialog;
import com.lidroid.xutils.BitmapUtils;

public class OrderDetailsActivity2 extends BaseActivity implements OnClickListener {

	public static final String ACTION_ORDER_CHANGED = "action_order_changed";

	private OrderDtoForList orderDetailsDto;

	private AddressDto addressInfo;

	private LinearLayout llytProductContainer;

	private LoadingDialog LoadingDialog;

	private BitmapUtils mBitmapUtils;

	private Button btnDelete;

	private String orderId;

	private TextView txtReceiver, txtReceiverMobile, txtAddress, txtOrderAmount, txtTransFee, txtCouponFee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("订单详情");
		orderId = getIntent().getStringExtra("orderId");
		findViewById(R.id.order_commit_layout_address_new).setVisibility(View.GONE);
		txtReceiver = (TextView) findViewById(R.id.txt_receiver_name);
		txtReceiverMobile = (TextView) findViewById(R.id.txt_receiver_num);
		txtAddress = (TextView) findViewById(R.id.txt_receiver_address);

		txtOrderAmount = (TextView) findViewById(R.id.order_details_order_amount);
		txtTransFee = (TextView) findViewById(R.id.order_details_trans_fee);
		txtCouponFee = (TextView) findViewById(R.id.order_details_coupon_fee);

		LoadingDialog = new LoadingDialog(this);
		mBitmapUtils = new BitmapUtils(this);
		btnDelete = (Button) findViewById(R.id.order_details_btn_delete);
		llytProductContainer = (LinearLayout) findViewById(R.id.order_details_prodcut_container);
		btnDelete.setOnClickListener(this);
		getDateList();
	}

	private void addAllProduct() {
		if (null != llytProductContainer) {
			llytProductContainer.removeAllViewsInLayout();
		}
		if (null == orderDetailsDto) {
			return;
		}
		((TextView) findViewById(R.id.order_details_prodcut_count)).setText("共" + orderDetailsDto.getSkuList().size()
				+ "件");
		for (Sku2 sku : orderDetailsDto.getSkuList()) {
			View view = getLayoutInflater().inflate(R.layout.item_product_list_order_commit, null);
			ImageView imageProduct = (ImageView) view.findViewById(R.id.item_product_order_commit_image);
			TextView txtProductName = (TextView) view.findViewById(R.id.item_product_order_commit_name);
			TextView txtProductPrice = (TextView) view.findViewById(R.id.item_product_order_commit_price);
			TextView txtProductFormat = (TextView) view.findViewById(R.id.item_product_order_commit_format);
			TextView txtProductCount = (TextView) view.findViewById(R.id.item_product_order_commit_count);
			final Button btnAction = (Button) view.findViewById(R.id.item_product_order_action);
			String action = "";
			switch (orderDetailsDto.getStatus()) {
			case 1: // UN_PAY
				action = "去付款";
				btnAction.setVisibility(View.VISIBLE);
				break;
			case 2:
				action = "待发货";
				btnAction.setVisibility(View.GONE);
				break;
			case 3:
				// action = "待发货";
				btnAction.setVisibility(View.GONE);
				break;
			case 4:
				action = "确认收货";
				btnAction.setVisibility(View.VISIBLE);
				break;
			case 5:
				action = "去评价";
				btnAction.setVisibility(View.VISIBLE);
				break;
			default:
				btnAction.setVisibility(View.GONE);
				break;
			}
			btnAction.setText(action);
			btnAction.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					switch (orderDetailsDto.getStatus()) {
					case 1:
						Intent intent = new Intent(OrderDetailsActivity2.this, EvaluationListActivity.class);
						intent.putExtra(EvaluationListActivity.ORDER_DETAILS, orderDetailsDto);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);
						break;
					case 2:

						break;
					case 3:

						break;
					case 4:

						break;
					case 5:

						break;
					default:
						break;
					}
				}
			});
			mBitmapUtils.display(imageProduct, ServiceUrlConstants.getImageHost() + sku.getImgUrl());
			txtProductName.setText(sku.getName());
			txtProductPrice.setText(getResources().getString(R.string.rmb) + sku.getCurPrice());
			txtProductFormat.setText(sku.getName());
			txtProductCount.setText("数量：" + orderDetailsDto.getProductCount());

			llytProductContainer.addView(view);
		}
	}

	/** 获取数据 */
	protected void getDateList() {
		LoadingDialog.show();
		OrderModel2.getInstance().getOrderDetail(orderId, new OnGetOrderDetailCompletedListener() {

			@Override
			public void onSuccess(NewOrderDetailResponse info) {
				LoadingDialog.dismiss();
				orderDetailsDto = info.getData().getOrderDetail();
				if (null == orderDetailsDto) {
					LoadingDialog.dismiss();
					finish();
				}
				addressInfo = info.getData().getAddress();
				initOrder();
				addAddressInfo();
				addAllProduct();
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onHttpException(HttpResponseException e) {
				LoadingDialog.dismiss();
			}

			@Override
			public void onFinish() {
				LoadingDialog.dismiss();

			}

			@Override
			public void onFailed(ResponseException e) {
				LoadingDialog.dismiss();
				ToastUtil.showToastShort(OrderDetailsActivity2.this, e.getResultMsg());
				Misc.finishDelay(OrderDetailsActivity2.this);
			}
		});
		addAllProduct();
	}

	protected void initOrder() {
		txtOrderAmount.setText(getResources().getString(R.string.rmb) + orderDetailsDto.getOrderAmount());
		txtTransFee.setText(getResources().getString(R.string.rmb) + orderDetailsDto.getTransferAmount());
		txtCouponFee.setText(getResources().getString(R.string.rmb) + orderDetailsDto.getCouponAmount());
	}

	protected void addAddressInfo() {
		txtReceiver.setText(addressInfo.getReceiverName());
		txtReceiverMobile.setText(addressInfo.getReceiveMb());
		txtAddress.setText(addressInfo.getProvinceName() + " " + addressInfo.getCityName() + " "
				+ addressInfo.getAreaName() + " " + addressInfo.getDetailAddress());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.order_details_btn_delete:
			final SampleDialog dialog = new SampleDialog(this) {

				@Override
				public View getContentView() {
					return null;
				}
			};
			dialog.setTitleText("提醒", R.color.black);
			dialog.setContentText("确定要删除订单吗？", R.color.gray);
			dialog.setTwoButton(R.string.cancel, R.color.black, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					dialog.dismiss();
				}
			}, R.string.ok, R.color.red, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					LoadingDialog.show();
					OrderModel2.getInstance().deleteOrder(orderId, new OnDeleteOrderCompletedListener() {

						@Override
						public void onSuccess(StatusInfo info) {
							LoadingDialog.dismiss();
							dialog.dismiss();
							ToastUtil.showToastShort(OrderDetailsActivity2.this, "订单已删除");
							finish();
							Intent intent = new Intent(ACTION_ORDER_CHANGED);
							sendBroadcast(intent);
						}

						@Override
						public void onStart() {

						}

						@Override
						public void onHttpException(HttpResponseException e) {
							LoadingDialog.dismiss();
						}

						@Override
						public void onFinish() {
							LoadingDialog.dismiss();
						}

						@Override
						public void onFailed(ResponseException e) {
							LoadingDialog.dismiss();
						}
					});

				}
			});
			dialog.show();
			break;

		default:
			break;
		}
	}
}
