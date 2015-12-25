package com.adult.android.presenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.adult.android.R;
import com.adult.android.presenter.fragment.main.tab.CarsFragmentFroBox;

/**
 * Created by Administrator on 2015/6/29.
 */
public class CarsActivityFroBox extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("购物车");
		setLeftButtonBackGround(R.drawable.boy_bcg);
		setLeftButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CarsActivityFroBox.this,
						AsistantActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
		});

		FragmentManager fragmentManager = getSupportFragmentManager();
		CarsFragmentFroBox carsFragment = new CarsFragmentFroBox();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.carsactvity_box_fragment_container,
				carsFragment);
		transaction.commit();
	}
}
