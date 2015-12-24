package com.adult.android.presenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.adult.android.view.LoadingDialog;

public class UserProfile extends BaseActivity implements OnClickListener {

	private LinearLayout llytAvator, llytNick, llytAcouSafty, llytGender, llytLove, llytMarrage, llytLocation,
			llytLevel;

	private TextView txtNickName, txtAge, txtSex, txtLove, txtMarriage, txtLocation, txtLevel;

	private LoadingDialog loaddingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver(broadCastReceiver, new IntentFilter(LoginActivity2.ACTION_REFRESH_USER));
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("个人资料");
		loaddingDialog = new LoadingDialog(this);
		llytAvator = (LinearLayout) findViewById(R.id.user_profile_avator);
		llytNick = (LinearLayout) findViewById(R.id.user_profile_nick);
		llytAcouSafty = (LinearLayout) findViewById(R.id.user_profile_acount_safe);
		llytGender = (LinearLayout) findViewById(R.id.user_profile_sexy);
		llytLove = (LinearLayout) findViewById(R.id.user_profile_love);
		llytMarrage = (LinearLayout) findViewById(R.id.user_profile_marry);
		llytLocation = (LinearLayout) findViewById(R.id.user_profile_location);
		llytLevel = (LinearLayout) findViewById(R.id.user_profile_level);

		txtNickName = (TextView) findViewById(R.id.user_profile_nick_txt);
		txtAge = (TextView) findViewById(R.id.user_profile_age_txt);
		txtSex = (TextView) findViewById(R.id.user_profile_sexy_txt);
		txtLove = (TextView) findViewById(R.id.user_profile_love_txt);
		txtMarriage = (TextView) findViewById(R.id.user_profile_marry_txt);
		txtLocation = (TextView) findViewById(R.id.user_profile_location_txt);
		txtLevel = (TextView) findViewById(R.id.user_profile_level_txt);

		llytAvator.setOnClickListener(this);
		llytNick.setOnClickListener(this);
		llytAcouSafty.setOnClickListener(this);
		llytGender.setOnClickListener(this);
		llytLove.setOnClickListener(this);
		llytMarrage.setOnClickListener(this);
		llytLocation.setOnClickListener(this);
		llytLevel.setOnClickListener(this);

		getUserInfo();
	}

	/** 获取用户信息 */
	private void getUserInfo() {
		loaddingDialog.show();
		UserModel.getInstance().getUserInfo(new OnLoginCompletedListener() {

			@Override
			public void onSuccess(UserResponse info) {
				loaddingDialog.dismiss();
				UserDto user = info.getData();
				user.setPassword(AgentApplication.get().getUserInfo().getPassword());
				UserLogic.getInsatnace().setUserInfo(user);
				txtNickName.setText(user.getLoginName());
				txtAge.setText(GeneralTool.isEmpty(user.getBirthday()) ? "待完善" : user.getBirthday());
				txtSex.setText(GeneralTool.isEmpty(user.getSex()) ? "待完善" : ("1".equals(user.getSex()) ? "男" : "女"));
				txtLove.setText(GeneralTool.isEmpty(user.getSexualOrientation()) ? "待完善" : user.getSexualOrientation());
				txtMarriage.setText(GeneralTool.isEmpty(user.getMarriageStatus()) ? "待完善" : user.getMarriageStatus());
				txtLocation.setText(GeneralTool.isEmpty(user.getProvince()) ? "待完善" : user.getProvince());
				txtLevel.setText(GeneralTool.isEmpty(user.getvLevel()) ? "待完善" : user.getvLevel() + "级");

				// sendBroadcast(new
				// Intent(LoginActivity2.ACTION_REFRESH_USER));
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onHttpException(HttpResponseException e) {
				loaddingDialog.dismiss();
			}

			@Override
			public void onFinish() {
				loaddingDialog.dismiss();
			}

			@Override
			public void onFailed(ResponseException e) {
				loaddingDialog.dismiss();
				ToastUtil.showToastShort(UserProfile.this, e.getResultMsg());
			}
		});

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.user_profile_avator:

			break;
		case R.id.user_profile_nick:
			Intent intentNick = new Intent(this, EditNickName.class);
			intentNick.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentNick);
			break;
		case R.id.user_profile_acount_safe:
			Intent intentAccountSafe = new Intent(this, AccountSafeActivity.class);
			intentAccountSafe.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentAccountSafe);
			break;
		case R.id.user_profile_sexy:
			Intent intentGender = new Intent(this, EditNickName.class);
			intentGender.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentGender);
			break;
		case R.id.user_profile_love:
			Intent intentLove = new Intent(this, SexLoveActivity.class);
			intentLove.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentLove);
			break;
		case R.id.user_profile_marry:
			Intent intentMarrage = new Intent(this, MarrageStatus.class);
			intentMarrage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentMarrage);
			break;
		case R.id.user_profile_location:
			Intent intentLocation = new Intent(this, EditNickName.class);
			intentLocation.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentLocation);
			break;
		case R.id.user_profile_level:
			Intent intentLevel = new Intent(this, EditNickName.class);
			intentLevel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentLevel);
			break;
		default:
			break;
		}
	}

	BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

		}
	};
}
