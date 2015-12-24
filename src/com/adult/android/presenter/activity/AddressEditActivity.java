package com.adult.android.presenter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.AddressDto;
import com.adult.android.entity.UpdateAddressResponse;
import com.adult.android.model.CartModel;
import com.adult.android.model.CartModel.OnUpdateAddressCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.utils.CheckCode;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.AddressPopupWindow;
import com.adult.android.view.LoadingDialog;

public class AddressEditActivity extends BaseActivity {

	private TextView txtArea;

	private boolean isChanged;

	private AddressDto addressTrans;

	private LoadingDialog loadingDialog;

	private EditText edtxtReceiver, edtxtReceiverNum, edtxtAddress;

	private String provinceName, cityName, streetName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	/** 初始化控件 */
	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("收货地址");
		setShowRightText("保存");
		loadingDialog = new LoadingDialog(this);
		edtxtReceiver = (EditText) findViewById(R.id.address_edit_receiver_name);
		edtxtReceiverNum = (EditText) findViewById(R.id.address_edit_receiver_mobile);
		txtArea = (TextView) findViewById(R.id.address_edit_receiver_area);
		edtxtAddress = (EditText) findViewById(R.id.address_edit_receiver_detail);
		initData();
		findViewById(R.id.address_llyt_receiver_area).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
						showAddressPopupWindow();
					}
				});
		setRightTextOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ToastUtil.showToastShort(AddressEditActivity.this, "保存");
				String receiver = edtxtReceiver.getText().toString().trim();
				String mobile = edtxtReceiverNum.getText().toString().trim();
				String area = txtArea.getText().toString();
				String address = edtxtAddress.getText().toString().trim();

				if (GeneralTool.isEmpty(receiver)) {
					ToastUtil.showToastLong(AddressEditActivity.this, "请输入收货人");
					return;
				}

				if (GeneralTool.isEmpty(mobile)) {
					ToastUtil.showToastLong(AddressEditActivity.this, "请输入手机号");
					return;
				}

				if (GeneralTool.isEmpty(area)) {
					ToastUtil.showToastLong(AddressEditActivity.this, "请选择区域");
					return;
				}

				if (GeneralTool.isEmpty(address)) {
					ToastUtil
							.showToastLong(AddressEditActivity.this, "请输入详细地址");
					return;
				}

				if (GeneralTool.isEmpty(receiver)) {
					ToastUtil.showToastLong(AddressEditActivity.this, "请输入收货人");
					return;
				}

				if (11 != mobile.length()) {
					ToastUtil.showToastShort(AddressEditActivity.this,
							"请输入正确的手机号");
					return;
				}

				if (mobile.length() == 11 && !CheckCode.checkPhone(mobile)) {
					ToastUtil.showToastShort(AddressEditActivity.this,
							"请输入有效的手机号");
					return;
				}
				if (null == addressTrans
						&& (!GeneralTool.isEmpty(receiver)
								|| !GeneralTool.isEmpty(mobile)
								|| !GeneralTool.isEmpty(area) || !GeneralTool
									.isEmpty(address))) {
					isChanged = true;
				} else if (null != addressTrans
						&& (!addressTrans.getReceiverName().equals(receiver)
								|| !addressTrans.getReceiveMb().equals(mobile)
								|| !addressTrans.getAreaName().equals(area) || !addressTrans
								.getDetailAddress().equals(address))) {
					isChanged = true;
				} else {
					isChanged = false;
				}

				if (isChanged) {
					saveAddress(receiver, mobile, address);
				} else {
					finish();
				}
			}
		});
	}

	/**
	 * 初始化
	 */
	private void initData() {
		if (null != getIntent().getSerializableExtra("address")) {
			addressTrans = (AddressDto) getIntent().getSerializableExtra(
					"address");
			provinceName = addressTrans.getProvinceName();
			cityName = addressTrans.getCityName();
			streetName = addressTrans.getAreaName();
			edtxtReceiver.setText(addressTrans.getReceiverName());
			edtxtReceiverNum.setText(addressTrans.getReceiveMb());
			txtArea.setText(provinceName + " " + cityName + " " + streetName);
			edtxtAddress.setText(addressTrans.getDetailAddress());
		}
	}

	/**
	 * 修改收货地址
	 * 
	 * @param address
	 * @param area
	 * @param mobile
	 * @param receiver
	 */
	protected void saveAddress(final String receiver, final String mobile,
			final String detailAddress) {
		loadingDialog.show();
		CartModel.getInstance().updateAddress(
				null == addressTrans ? "" : addressTrans.getAddressId(),
				AgentApplication.get().getUserId(), provinceName, cityName,
				streetName, detailAddress, receiver, mobile,
				new OnUpdateAddressCompletedListener() {

					@Override
					public void onSuccess(UpdateAddressResponse info) {
						loadingDialog.dismiss();
						if (null == addressTrans) {
							addressTrans = new AddressDto();
						}
						addressTrans.setProvinceName(provinceName);
						addressTrans.setCityName(cityName);
						addressTrans.setAreaName(streetName);
						addressTrans.setReceiverName(receiver);
						addressTrans.setReceiveMb(mobile);
						addressTrans.setDetailAddress(detailAddress);

						Intent intent = new Intent(AddressEditActivity.this,
								OrderCommitActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						intent.putExtra("address", addressTrans);
						setResult(10, intent);
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
						ToastUtil.showToastShort(AddressEditActivity.this,
								e.getResultMsg());
						loadingDialog.dismiss();
					}
				});
	}

	/**
	 * 显示地址选择弹框
	 */
	private void showAddressPopupWindow() {
		new AddressPopupWindow(this, pop_layout,
				new AddressPopupWindow.onConfirmCompletedListener() {

					@Override
					public void onConfirmCompleted(String provinceId,
							String cityId, String areaId, String provinceName,
							String cityName, String streetName) {
						AddressEditActivity.this.provinceName = provinceName;
						AddressEditActivity.this.cityName = cityName;
						AddressEditActivity.this.streetName = streetName;
						txtArea.setText(provinceName + " " + cityName + " "
								+ streetName);
					}
				});
	}

}
