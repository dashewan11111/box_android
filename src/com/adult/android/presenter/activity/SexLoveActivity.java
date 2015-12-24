package com.adult.android.presenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.adult.android.R;

public class SexLoveActivity extends BaseActivity implements OnClickListener {

	public static final int SEX_LOVE = 110;

	private String sexLove = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("性取向");
		setShowRightText("保存");
		setRightTextOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
				Intent intent = new Intent(LoginActivity2.ACTION_REFRESH_USER);
				intent.putExtra("sex_love", sexLove);
				sendBroadcast(intent);
			}
		});
		findViewById(R.id.sex_love_man).setOnClickListener(this);
		findViewById(R.id.sex_love_femal).setOnClickListener(this);
		findViewById(R.id.sex_love_double).setOnClickListener(this);
		findViewById(R.id.sex_love_no).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		findViewById(R.id.sex_love_man).setBackgroundColor(
				getResources().getColor(R.color.white));
		findViewById(R.id.sex_love_femal).setBackgroundColor(
				getResources().getColor(R.color.white));
		findViewById(R.id.sex_love_double).setBackgroundColor(
				getResources().getColor(R.color.white));
		findViewById(R.id.sex_love_no).setBackgroundColor(
				getResources().getColor(R.color.white));
		switch (view.getId()) {
		case R.id.sex_love_man:
			sexLove = "1";
			findViewById(R.id.sex_love_man).setBackgroundColor(
					getResources().getColor(R.color.gold));
			break;
		case R.id.sex_love_femal:
			sexLove = "2";
			findViewById(R.id.sex_love_femal).setBackgroundColor(
					getResources().getColor(R.color.gold));
			break;
		case R.id.sex_love_double:
			sexLove = "3";
			findViewById(R.id.sex_love_double).setBackgroundColor(
					getResources().getColor(R.color.gold));
			break;
		case R.id.sex_love_no:
			sexLove = "4";
			findViewById(R.id.sex_love_no).setBackgroundColor(
					getResources().getColor(R.color.gold));
			break;
		default:
			break;
		}

	}
}
