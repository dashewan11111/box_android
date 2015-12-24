package com.adult.android.presenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.adult.android.R;
import com.adult.android.entity.UserResponse;
import com.adult.android.logic.UserLogic;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnLoginCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.view.LoadingDialog;

public class CommitUserProfile extends BaseActivity implements OnClickListener {

	private boolean sexyMale = true, avatorMale;

	private ImageView imgAvator;

	private EditText edtxtNick, edtxtPassword;

	private LoadingDialog lodingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		lodingDialog = new LoadingDialog(this);
		imgAvator = (ImageView) findViewById(R.id.commit_user_profile_avator);
		findViewById(R.id.commit_user_profile_llyt_change).setOnClickListener(
				this);
		findViewById(R.id.commit_user_profile_male).setOnClickListener(this);
		findViewById(R.id.commit_user_profile_female).setOnClickListener(this);
		edtxtNick = (EditText) findViewById(R.id.commit_user_profile_nick);
		edtxtPassword = (EditText) findViewById(R.id.commit_user_profile_password);
		findViewById(R.id.commit_user_profile_btn_commit).setOnClickListener(
				this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.commit_user_profile_llyt_change:
			if (avatorMale) {
				imgAvator.setImageResource(R.drawable.default_user_avator_f);
			} else {
				imgAvator.setImageResource(R.drawable.default_user_avator_m);
			}
			avatorMale = !avatorMale;
			break;
		case R.id.commit_user_profile_male:
			((ImageView) findViewById(R.id.commit_user_profile_male_img))
					.setImageResource(R.drawable.sexy_selected);
			((ImageView) findViewById(R.id.commit_user_profile_female_img))
					.setImageResource(R.drawable.sexy_unselected);
			sexyMale = true;
			break;
		case R.id.commit_user_profile_female:
			((ImageView) findViewById(R.id.commit_user_profile_male_img))
					.setImageResource(R.drawable.sexy_unselected);
			((ImageView) findViewById(R.id.commit_user_profile_female_img))
					.setImageResource(R.drawable.sexy_selected);
			sexyMale = false;
			break;
		case R.id.commit_user_profile_btn_commit:
			regist();
			break;
		default:
			break;
		}
	}

	/** 注册 */
	private void regist() {
		lodingDialog.show();
		UserModel.getInstance().regist(getIntent().getStringExtra("mobile"),
				edtxtPassword.getText().toString(),
				getIntent().getStringExtra("verifyCode"), sexyMale ? "1" : "2",
				edtxtNick.getText().toString(), new OnLoginCompletedListener() {

					@Override
					public void onSuccess(UserResponse info) {
						lodingDialog.dismiss();
						UserLogic.getInsatnace().setUserInfo(info.getData());
						sendBroadcast(new Intent(
								LoginActivity2.ACTION_REFRESH_USER));
						finish();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onHttpException(HttpResponseException e) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onFailed(ResponseException e) {
						// TODO Auto-generated method stub
					}
				});
	}

}
