package com.adult.android.presenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.adult.android.R;
import com.adult.android.entity.UserDto;
import com.adult.android.entity.UserResponse;
import com.adult.android.logic.UserLogic;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnLoginCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.ClearEditText;
import com.adult.android.view.LoadingDialog;

public class EditNickName extends BaseActivity {

	private ClearEditText edTxtNickName;// 编辑昵称

	private String nickName;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("昵称");
		setShowRightText("保存");
		loadingDialog = new LoadingDialog(this);
		setRightTextOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String nick = edTxtNickName.getText().toString();
				if (GeneralTool.isEmpty(nick)) {
					ToastUtil.showToastShort(EditNickName.this, "昵称不能为空");
					return;
				}
				if (AgentApplication.get().getUserInfo().getLoginName().equals(nick)) {
					finish();
					return;
				}
				updateUserInfo(nick);
			}
		});
		// String nickName =
		// AgentApplication.get().getUserInfo().getLoginName();
		edTxtNickName = (ClearEditText) findViewById(R.id.edit_nick_name);
		edTxtNickName.setText(AgentApplication.get().getUserInfo().getLoginName());
		edTxtNickName.setSelection(AgentApplication.get().getUserInfo().getLoginName().length());
	}

	/** 保存用户信息 */
	protected void updateUserInfo(final String nick) {
		loadingDialog.show();
		UserModel.getInstance().updateUserInfo(nick, "", "", "", "", "", new OnLoginCompletedListener() {

			@Override
			public void onSuccess(UserResponse info) {
				loadingDialog.dismiss();
				UserDto user = AgentApplication.get().getUserInfo();
				user.setLoginName(nick);
				UserLogic.getInsatnace().setUserInfo(user);
				sendBroadcast(new Intent(LoginActivity2.ACTION_REFRESH_USER));
				finish();
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
				ToastUtil.showToastShort(EditNickName.this, e.getResultMsg());
				loadingDialog.dismiss();
			}
		});

	}
}
