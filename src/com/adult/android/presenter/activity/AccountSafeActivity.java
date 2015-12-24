package com.adult.android.presenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adult.android.R;

public class AccountSafeActivity extends BaseActivity implements OnClickListener {

	private TextView txtMobileStatus;

	private LinearLayout llytBandMobile, llytChangePassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("账号安全");

		// String nickName =
		// AgentApplication.get().getUserInfo().getLoginName();
		txtMobileStatus = (TextView) findViewById(R.id.account_safe_mobile_status);

		llytBandMobile = (LinearLayout) findViewById(R.id.account_safe_mobile);
		llytChangePassword = (LinearLayout) findViewById(R.id.account_safe_password);

		llytBandMobile.setOnClickListener(this);
		llytChangePassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.account_safe_mobile:
			Intent intent = new Intent(this, BindMobileActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			break;
		case R.id.account_safe_password:

			break;
		default:
			break;
		}

	}
}
