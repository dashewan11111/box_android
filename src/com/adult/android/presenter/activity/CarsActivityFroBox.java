package com.adult.android.presenter.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.adult.android.R;
import com.adult.android.presenter.fragment.main.tab.CarsFragmentFroBox;

/**
 * Created by Administrator on 2015/6/29.
 */
public class CarsActivityFroBox extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setShowTitleBar(false);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("购物车");
		FragmentManager fragmentManager = getSupportFragmentManager();
		CarsFragmentFroBox carsFragment = new CarsFragmentFroBox();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.carsactvity_box_fragment_container, carsFragment);
		transaction.commit();
	}
}
