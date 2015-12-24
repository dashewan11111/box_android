package com.adult.android.presenter.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.UserResponse;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnLoginCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.view.LoadingDialog;
import com.adult.android.view.SampleDialog;

public class BindMobileActivity extends BaseActivity implements OnClickListener {

	private EditText edTxtCode;

	private TextView txtMobile;

	private Button btnReSend, btnCommit;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	/** 初始化 */
	private void initView() {
		setShowTitleBar(true);
		setActivityTitle("绑定手机");
		loadingDialog = new LoadingDialog(this);
		edTxtCode = (EditText) findViewById(R.id.bindmobile_code);
		txtMobile = (TextView) findViewById(R.id.bindmobile_mobile);

		btnReSend = (Button) findViewById(R.id.bindmobile_btn_resend);
		btnCommit = (Button) findViewById(R.id.bindmobile_btn_commit);
		btnReSend.setEnabled(false);
		btnCommit.setEnabled(false);
		btnReSend.setOnClickListener(this);
		btnCommit.setOnClickListener(this);
		edTxtCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
				if (null != str && 0 < str.length()) {
					btnCommit.setTextColor(getResources().getColor(R.color.white));
					btnCommit.setEnabled(true);
				} else {
					btnCommit.setTextColor(getResources().getColor(R.color.gray));
					btnCommit.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		txtMobile.setText(AgentApplication.get().getUserInfo().getMobile());
		startTime();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bindmobile_btn_resend:// 重新获取验证码按钮
			startTime();
			break;
		case R.id.bindmobile_btn_commit:// 提交
			bindMobile(AgentApplication.get().getUserInfo().getMobile(), edTxtCode.getText().toString());
			break;
		default:
			break;
		}
	}

	/** 绑定手机号 */
	private void bindMobile(String mobile, String code) {
		loadingDialog.show();
		UserModel.getInstance().bindMobile(mobile, code, new OnLoginCompletedListener() {

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

			}

			@Override
			public void onFailed(ResponseException e) {
				loadingDialog.dismiss();
			}
		});
	}

	Timer timer;
	int currentTime = 60;
	MyHandler mHadHandler = new MyHandler();

	private void startTime() {
		btnReSend.setEnabled(false);
		currentTime = 60;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				mHadHandler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}

	private void stopTime() {
		if (null != timer) {
			timer.cancel();
		}
		btnReSend.setEnabled(true);
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			currentTime--;
			if (0 < currentTime) {
				btnReSend.setEnabled(false);
				btnReSend.setText("重新发送(" + currentTime + ")");
			} else {
				btnReSend.setEnabled(true);
				btnReSend.setText("重新发送");
			}
		}
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
		dialog.setContentText("不绑定手机可能会对您的账号造成安全隐患，确定要退出吗？", R.color.gray_1);
		dialog.setTwoButton(R.string.ok, R.color.red, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
				dialog.dismiss();
			}
		}, R.string.ok, R.color.red, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				dialog.dismiss();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopTime();
	}
}
