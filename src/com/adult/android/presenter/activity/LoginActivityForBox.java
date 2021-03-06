package com.adult.android.presenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.adult.android.R;
import com.adult.android.entity.UserResponse2;
import com.adult.android.logic.UserLogic;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnLoginCompletedListenerForBox;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.utils.CheckCode;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;

public class LoginActivityForBox extends BaseActivity implements
		OnClickListener {

	public static final int FROM_RRODUCT_DETATIL = 11;

	public static final String ACTION_REFRESH_USER = "refresh_user_info";

	private EditText edTxtUserName, edTxtPassword;

	private LoadingDialog loaddingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		loaddingDialog = new LoadingDialog(this);
		setShowTitleBar(false);
		setLeftButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		setActivityTitle(R.string.string_title_login);

		findViewById(R.id.login_loginBtn).setOnClickListener(this);
		edTxtUserName = (EditText) findViewById(R.id.login_usernameEdt);
		edTxtPassword = (EditText) findViewById(R.id.login_psdEdt);

		if (!GeneralTool.isEmpty(UserLogic.getInsatnace().getUserName())) {
			edTxtUserName.setText((UserLogic.getInsatnace().getUserName()));
			edTxtUserName.setSelection((UserLogic.getInsatnace().getUserName()
					.length()));
		}
	}

	// 处理点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 登录按钮
		case R.id.login_loginBtn:
			login();
			break;
		default:
			break;
		}
	}

	private void login() {
		final String userName = edTxtUserName.getText().toString().trim();
		final String password = edTxtPassword.getText().toString().trim();
		if (GeneralTool.isEmpty(userName)) {
			ToastUtil.showToastShort(LoginActivityForBox.this,
					R.string.login_nick_name_empty);
			return;
		}
		if (GeneralTool.isEmpty(password)) {
			ToastUtil.showToastShort(LoginActivityForBox.this,
					R.string.string_hint_login_pas);
			return;
		}
		if (password.length() > 20 || password.length() < 6) {
			ToastUtil.showToastShort(LoginActivityForBox.this,
					R.string.pwd_lengh_limit);
			return;
		}
		if (CheckCode.isChinese(password)) {
			ToastUtil.showToastShort(LoginActivityForBox.this,
					R.string.pwd_chinese_error);
			return;
		}

		loaddingDialog.show();
		UserModel.getInstance().loginForBox(userName, password,
				new OnLoginCompletedListenerForBox() {

					@Override
					public void onSuccess(UserResponse2 info) {
						loaddingDialog.dismiss();
						// UserDto user = info.getData();
						// user.setPassword(password);
						// UserLogic.getInsatnace().setUserInfo(user);
						if (null != info.getData()) {
							AgentApplication.get().setShopperId(
									info.getData().getShopperId());
							AgentApplication.get().setSessionId(
									info.getData().getSessionId());
							AgentApplication.get().setShopId(
									info.getData().getShopId());
							sendBroadcast(new Intent(ACTION_REFRESH_USER));
							UserLogic.getInsatnace().saveUserName(userName);
							UserLogic.getInsatnace().savePassword(password);
							Intent intent = new Intent(
									LoginActivityForBox.this,
									CarsActivityFroBox.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivity(intent);
							finish();
						} else {
							ToastUtil.showToastShort(LoginActivityForBox.this,
									"没有返回值");
						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						loaddingDialog.dismiss();
						ToastUtil.showToastShort(LoginActivityForBox.this,
								e.getResultMsg());
					}

					@Override
					public void onFinish() {
						loaddingDialog.dismiss();
					}

					@Override
					public void onFailed(ResponseException e) {
						loaddingDialog.dismiss();
						ToastUtil.showToastShort(LoginActivityForBox.this,
								e.getResultMsg());
						// finish();
					}
				});
	}

}
