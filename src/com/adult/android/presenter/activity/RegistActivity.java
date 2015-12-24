package com.adult.android.presenter.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.adult.android.R;
import com.adult.android.entity.VerifyResponse;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnGetVerifyCodeCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.utils.CheckCode;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.adult.android.view.SampleDialog;

public class RegistActivity extends BaseActivity implements OnClickListener {

	private EditText edTxtPhone;

	private Button btnGetCode;

	private LoadingDialog lodingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	/** 初始化 */
	private void initView() {
		setShowTitleBar(true);
		setActivityTitle(R.string.string_title_register);
		lodingDialog = new LoadingDialog(this);
		edTxtPhone = (EditText) findViewById(R.id.regist_phone);
		btnGetCode = (Button) findViewById(R.id.regist_btn_get_code);
		findViewById(R.id.regist_login).setOnClickListener(this);
		btnGetCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.regist_btn_get_code:// 获取验证码按钮
			String phoneNum = edTxtPhone.getText().toString();
			if (GeneralTool.isEmpty(phoneNum)) {
				ToastUtil.showToastShort(RegistActivity.this, "手机号不能为空");
				return;
			}

			if (edTxtPhone.getText().length() != 11) {
				ToastUtil.showToastShort(RegistActivity.this, "手机号不正确");
				return;
			}

			if (edTxtPhone.getText().length() == 11
					&& !CheckCode.checkPhone(phoneNum)) {
				ToastUtil.showToastShort(RegistActivity.this, "请输入有效的手机号");
				return;
			}
			getCode(phoneNum);
			break;
		case R.id.regist_login:// 登录
			Intent intent = new Intent(this, LoginActivity2.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	/** 获取验证码 */
	private void getCode(String phoneNum) {
		btnGetCode.setEnabled(false);
		lodingDialog.show();
		UserModel.getInstance().getVerifyCode(phoneNum,
				new OnGetVerifyCodeCompletedListener() {

					@Override
					public void onSuccess(VerifyResponse info) {
						lodingDialog.dismiss();
						goVerifyActivity(info.getData().getVerifyCode());
					}

					@Override
					public void onStart() {
						btnGetCode.setEnabled(false);
					}

					@Override
					public void onHttpException(HttpResponseException e) {
						btnGetCode.setEnabled(true);
					}

					@Override
					public void onFinish() {
						btnGetCode.setEnabled(true);
					}

					@Override
					public void onFailed(ResponseException e) {
						btnGetCode.setEnabled(true);
					}
				});
	}

	@Override
	public void onBackPressed() {
		final SampleDialog dialog = new SampleDialog(this) {

			@Override
			public View getContentView() {
				return null;
			}
		};
		dialog.setTitleText("提醒", R.color.gray_3);
		dialog.setContentText("确定要退出注册流程吗？", R.color.gray_1);
		dialog.setTwoButton(R.string.ok, R.color.red,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
						dialog.dismiss();
					}
				}, R.string.ok, R.color.red,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						dialog.dismiss();
					}
				});
	}

	protected void goVerifyActivity(String code) {
		Intent intent = new Intent(this, VerifyActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("verifyCode", code);
		intent.putExtra("mobile", edTxtPhone.getText().toString());
		startActivity(intent);
		finish();
	}
}
