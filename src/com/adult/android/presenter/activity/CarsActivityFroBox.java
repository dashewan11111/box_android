package com.adult.android.presenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.adult.android.R;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.presenter.fragment.main.tab.CarsFragmentFroBox;
import com.adult.android.utils.ToastUtil;

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

	/**
	 * 三秒内双击退出
	 */
	private long lastBackPressTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// moveTaskToBack(true);
			if ((System.currentTimeMillis() - lastBackPressTime) < 3 * 1000) {
				lastBackPressTime = 0;
				AgentApplication.exit();
			} else {
				lastBackPressTime = System.currentTimeMillis();
				ToastUtil.showToastShort(this, R.string.press_exit_app);
			}
			return true;

			// final SampleDialog dialog = new SampleDialog(this) {
			//
			// @Override
			// public View getContentView() {
			// return null;
			// }
			// };
			// dialog.setTitleText("提醒", R.color.black);
			// dialog.setContentText("确定要退出成人秀吗？", R.color.gray);
			// dialog.setTwoButton(R.string.cancel, R.color.black,
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface arg0, int arg1) {
			// dialog.dismiss();
			// }
			// }, R.string.ok, R.color.red,
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface arg0, int arg1) {
			// finish();
			// AgentApplication.get().exit();
			// dialog.dismiss();
			// }
			// });
			// dialog.show();
		}
		return super.onKeyDown(keyCode, event);
	}
}
