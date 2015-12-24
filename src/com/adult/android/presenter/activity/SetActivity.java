package com.adult.android.presenter.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.VersionInfo;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.constants.ServiceUrlConstants.HOST;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.model.setting.AppUpgradeModel;
import com.adult.android.model.setting.AppUpgradeModel.OnCheckVersionListener;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.utils.Misc;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.adult.android.view.SampleDialog;
import com.lidroid.xutils.BitmapUtils;

/**
 * 
 * @author GongXun
 * 
 * @2015年3月25日
 * 
 * @descripte
 * 
 *            设置界面
 */
public class SetActivity extends BaseActivity implements OnClickListener, OnCheckVersionListener {
	private RelativeLayout rl_checkVersion, rl_clear;
	private AppUpgradeModel appUpdateModel;
	private TextView tv_setting_current_version;
	private LoadingDialog loadingDialog;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setShowTitleBar(true);
		setActivityTitle(R.string.activity_title_setting);
		loadingDialog = new LoadingDialog(this);
		appUpdateModel = new AppUpgradeModel(this);
		rl_checkVersion = (RelativeLayout) findViewById(R.id.rl_checkVersion);
		rl_checkVersion.setOnClickListener(this);
		tv_setting_current_version = (TextView) findViewById(R.id.tv_setting_current_version);
		tv_setting_current_version.setText("当前版本：" + Misc.getVersionName(this));
		rl_clear = (RelativeLayout) findViewById(R.id.rl_clear);
		rl_clear.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rl_checkVersion:
			if (HOST.TEST.getApiHost().equals(ServiceUrlConstants.getApiHost())) {
				appUpdateModel.checkUpgrade(this);
			}
			break;

		case R.id.rl_clear:
			// DataCleanManager.cleanApplicationData(this);
			BitmapUtils bitmapUtils = new BitmapUtils(this);
			bitmapUtils.clearCache();
			ToastUtil.showToastShort(this, "缓存清理完成");
			break;
		}
	}

	@Override
	public void onNeedUpdateVerion(VersionInfo info) {
		String versionName = info.getVersionName();
		String releaseNotes = info.getReleaseNotes();
		String strategy = info.getStrategy();
		int res = Integer.parseInt(strategy);
		final String apkUrl = info.getUrl();
		SampleDialog dialog = new SampleDialog(this) {

			@Override
			public View getContentView() {
				return null;
			}
		};
		dialog.setTitleText(String.format(getResources().getString(R.string.new_version), versionName), 0);
		dialog.setContentText(Misc.ToDBC(releaseNotes), 0);
		dialog.setContentTextGravity(Gravity.LEFT);

		// dialog.setContentText(releaseNotes, 0);
		if (res == 1) {
			dialog.setCancelable(false);
			dialog.setSingleButton(R.string.upgrade, 0, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Misc.startSystemBrowser(SetActivity.this, apkUrl);
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							AgentApplication.get().exit();
						}
					}, 2000);
				}
			});
		} else {
			dialog.setTwoButton(R.string.cancel, 0, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}, R.string.upgrade, 0, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Misc.startSystemBrowser(SetActivity.this, apkUrl);
					dialog.dismiss();
				}
			});
		}
		dialog.show();
	}

	@Override
	public void onNotNeedUpdateVersion(boolean same) {
		ToastUtil.showToastShort(SetActivity.this, "您当前已经是最新版本");
	}

	@Override
	public void onCheckVersionFail(ResponseException e) {
		ToastUtil.showToastShort(SetActivity.this, "请求失败");
		showExceptionView();
	}

	@Override
	public void onRequestStart() {
		loadingDialog.show();
	}

	@Override
	public void onRequestFinish() {
		loadingDialog.dismiss();
	}
}
