package com.adult.android.presenter.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.adult.android.R;
import com.adult.android.entity.UserResponse;
import com.adult.android.entity.VerifyResponse;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnGetVerifyCodeCompletedListener;
import com.adult.android.model.UserModel.OnLoginCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.view.LoadingDialog;

@SuppressLint("NewApi")
public class FindPsdActivity2 extends BaseActivity implements OnClickListener {

	private EditText edtxtMobile, edtxtCode, edtxtPassword;

	private Button btnGetCode, btnCommit;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setShowTitleBar(true);
		setActivityTitle(R.string.string_find_psd);
		initViews();
	}

	private void initViews() {
		loadingDialog = new LoadingDialog(this);
		edtxtMobile = (EditText) findViewById(R.id.findpsd_mobile);
		edtxtCode = (EditText) findViewById(R.id.findpsd_code);
		edtxtPassword = (EditText) findViewById(R.id.findpsd_new_psd);
		btnGetCode = (Button) findViewById(R.id.findpsd_btn_code);
		btnGetCode.setOnClickListener(this);
		btnCommit = (Button) findViewById(R.id.findpsd_btn_commit);
		btnCommit.setOnClickListener(this);
		edtxtMobile.addTextChangedListener(new OnMyTextWacher(0));
		edtxtCode.addTextChangedListener(new OnMyTextWacher(1));
		edtxtPassword.addTextChangedListener(new OnMyTextWacher(2));
	}

	@Override
	public void onClick(View v) {
		String mobile = edtxtMobile.getText().toString();
		String code = edtxtCode.getText().toString();
		String password = edtxtPassword.getText().toString();
		switch (v.getId()) {
		case R.id.findpsd_btn_code:// 获取验证码
			getCode(mobile);
			break;
		case R.id.findpsd_btn_commit:// 提交
			// if (GeneralTool.isEmpty(mobile)) {
			// ToastUtil.showToastShort(FindPsdActivity2.this, "手机号不能为空");
			// return;
			// }
			// if (11 != mobile.length()) {
			// ToastUtil.showToastShort(FindPsdActivity2.this, "请输入正确的手机号");
			// return;
			// }
			// if (mobile.length() == 11 && !CheckCode.checkPhone(mobile)) {
			// ToastUtil.showToastShort(FindPsdActivity2.this, "请输入有效的手机号");
			// return;
			// }
			//
			// if (GeneralTool.isEmpty(code)) {
			// ToastUtil.showToastShort(FindPsdActivity2.this, "验证码不能为空");
			// return;
			// }
			// if (GeneralTool.isEmpty(password)) {
			// ToastUtil.showToastShort(FindPsdActivity2.this, "新密码不能为空");
			// return;
			// }
			// if (password.length() > 20 || password.length() < 6) {
			// ToastUtil.showToastShort(FindPsdActivity2.this,
			// R.string.pwd_lengh_limit);
			// return;
			// }
			commit(mobile, code, password);
			break;
		default:
			break;
		}
	}

	String verifyCode = "";// 验证码

	private void getCode(String mobile) {
		btnGetCode.setEnabled(false);
		loadingDialog.show();
		UserModel.getInstance().getVerifyCode(mobile,
				new OnGetVerifyCodeCompletedListener() {

					@Override
					public void onSuccess(VerifyResponse info) {
						loadingDialog.dismiss();
						edtxtMobile.setEnabled(false);
						verifyCode = info.getData().getVerifyCode();
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

	private void commit(String mobile, String code, String password) {
		btnGetCode.setEnabled(false);
		loadingDialog.show();
		UserModel.getInstance().findBackPassword(mobile, password, code, "",
				"", new OnLoginCompletedListener() {

					@Override
					public void onSuccess(UserResponse info) {
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
						loadingDialog.dismiss();

					}

					@Override
					public void onFailed(ResponseException e) {
						loadingDialog.dismiss();

					}
				});
	}

	boolean[] array = { false, false, false };

	class OnMyTextWacher implements TextWatcher {

		private int i;

		public OnMyTextWacher(int i) {
			this.i = i;
		}

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
			if (null != str && 0 < str.length()) {
				array[i] = true;
			} else {
				array[i] = false;
			}
			if (array[0] && array[1] && array[2]) {
				btnCommit.setEnabled(true);
				btnCommit.setTextColor(getResources().getColor(R.color.white));
			} else {
				btnCommit.setEnabled(false);
				btnCommit.setTextColor(getResources().getColor(R.color.gray));
			}
		}
	}
}
