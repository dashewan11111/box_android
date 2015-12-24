package com.adult.android.presenter.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.Result;
import com.adult.android.entity.UserInfo;
import com.adult.android.logic.UserLogic;
import com.adult.android.model.UserActionModel;
import com.adult.android.model.constants.SharedPreferencesConstants;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.model.listener.SampleModelListener;
import com.adult.android.utils.CheckCode;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.SharedPreferencesUtil;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class ModifyLoginPwdActivity extends BaseActivity implements OnClickListener, SampleModelListener<Object> {
	private EditText editOldPsd, editNewPsd, editSurePsd, editVd;
	private TextView editPhone;
	private UserInfo userBean;
	private LoadingDialog loadingDialog;
	private Button psdMD_vdBtn;
	private LinearLayout llyt_time;
	/**
	 * 计时器
	 */
	private TextView timeTet;
	private String methodString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setShowTitleBar(true);
		setActivityTitle("修改登录密码");
		userBean = UserLogic.getInsatnace().getUserBean();
		psdMD_vdBtn = (Button) findViewById(R.id.psdMD_vdBtn);
		psdMD_vdBtn.setOnClickListener(this);
		findViewById(R.id.psdMD_tijiaoBtn).setOnClickListener(this);
		editNewPsd = (EditText) findViewById(R.id.psdMD_new1PsdEdt);
		editOldPsd = (EditText) findViewById(R.id.psdMD_oldPsdEdt);
		editSurePsd = (EditText) findViewById(R.id.psdMD_new2PsdEdt);
		editPhone = (TextView) findViewById(R.id.psdMD_phoneEdt);
		String savedName = SharedPreferencesUtil.getSharedPreferences(SharedPreferencesConstants.FILES.FILE_USER,
				SharedPreferencesConstants.PARAMS.USER_LOGIN_REGISTER_SUCCESS_INFO, "");
		if (!TextUtils.isEmpty(userBean.getMobile())) {
			editPhone.setText(userBean.getMobile().substring(0, 3) + "****"
					+ userBean.getMobile().substring(userBean.getMobile().length() - 4, userBean.getMobile().length()));

		}
		editVd = (EditText) findViewById(R.id.psdMD_vdEdit);
		loadingDialog = new LoadingDialog(this);
		timeTet = (TextView) findViewById(R.id.timeTet_modifypwd);
		llyt_time = (LinearLayout) findViewById(R.id.llyt_time_modifypwd);
	}

	@Override
	public void onClick(View v) {
		String phone = userBean.getMobile();
		if (GeneralTool.isEmpty(phone)) {
			ToastUtil.showToastShort(ModifyLoginPwdActivity.this, "手机号不能为空");
			return;
		}
		switch (v.getId()) {
		// 获取验证码
		case R.id.psdMD_vdBtn:
			methodString = "getCode";
			UserActionModel.getModifyPwdCode(userBean.getUserId(), phone, this);
			break;
		// 修改密码
		case R.id.psdMD_tijiaoBtn:
			String vd = editVd.getText().toString();
			String oldPsd = editOldPsd.getText().toString();
			String new1Psd = editNewPsd.getText().toString();
			String new2Psd = editSurePsd.getText().toString();
			if (GeneralTool.isEmpty(vd)) {
				ToastUtil.showToastShort(ModifyLoginPwdActivity.this, "验证码不能为空");
				return;
			}
			if (GeneralTool.isEmpty(oldPsd)) {
				ToastUtil.showToastShort(ModifyLoginPwdActivity.this, "旧密码不能为空");
				return;
			}
			if (oldPsd.length() < 6) {
				ToastUtil.showToastShort(ModifyLoginPwdActivity.this, "原密码错误");
				return;
			}

			if (GeneralTool.isEmpty(new1Psd)) {
				ToastUtil.showToastShort(ModifyLoginPwdActivity.this, "新密码不能为空");
				return;
			}

			if (GeneralTool.isEmpty(new2Psd)) {
				ToastUtil.showToastShort(ModifyLoginPwdActivity.this, "确认密码不能为空");
				return;
			}

			if (GeneralTool.isEmpty(new1Psd)) {
				ToastUtil.showToastShort(ModifyLoginPwdActivity.this, "密码不能为空");
				return;
			}
			if (!CheckCode.checkPsd(new1Psd)) {
				ToastUtil.showToastShort(ModifyLoginPwdActivity.this, "密码格式不正确，只允许使用数字和字母，长度为6-20位，且不可以为全部相同的数字或字母");
				return;
			}

			if (!new2Psd.equals(new1Psd)) {
				ToastUtil.showToastShort(ModifyLoginPwdActivity.this, "新密码与确认密码不一致");
				return;
			}
			if (UserLogic.getInsatnace().getUserBean().getUserName().equals(new1Psd)) {
				ToastUtil.showToastLong(ModifyLoginPwdActivity.this, "密码不能与用户名相同");
				return;
			}
			methodString = "modify";
			UserActionModel.modifyLoginPwd(userBean.getUserId(), phone, oldPsd, new1Psd, vd, this);
			break;
		default:
			break;
		}
	}

	/**
	 * 更新计时器
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String vaString = timeTet.getText().toString();
			int sec = Integer.parseInt(vaString);
			sec--;
			if (sec <= 0) {
				stopJishi();
				llyt_time.setVisibility(View.GONE);
				psdMD_vdBtn.setVisibility(View.VISIBLE);
			} else {
				timeTet.setText(String.valueOf(sec));

			}
		};
	};

	Timer timer;

	/**
	 * 开始计时
	 */
	private void startJishi() {
		timer = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		};
		timer.schedule(tt, 1000, 1000);
	}

	/**
	 * 结束计时
	 */
	public void stopJishi() {
		if (timer != null) {
			timer.cancel();
			timeTet.setText("120");
		}
		timer = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopJishi();
	}

	@Override
	public void onRequestStart() {
		loadingDialog.show();
	}

	@Override
	public void onRequestSuccess(Object t) {
		Result result = (Result) t;
		if ("getCode".equals(methodString)) {
			ToastUtil.showToastShort(this, result.getMessage());
			llyt_time.setVisibility(View.VISIBLE);
			psdMD_vdBtn.setVisibility(View.GONE);
			startJishi();
		}
		if ("modify".equals(methodString)) {
			methodString = "logOut";
			UserActionModel.userLogOut(this);

		}
		if ("logOut".equals(methodString)) {
			UserLogic.getInsatnace().setUserBean(null);
			setResult(11);
			finish();
		}
	}

	@Override
	public void onRequestFail(ResponseException e) {
		loadingDialog.dismiss();
		ToastUtil.showToastShort(this, "请求失败");
		showExceptionView();
	}

	@Override
	public void onRequestFinish() {
		loadingDialog.dismiss();
	}

}
