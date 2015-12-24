package com.adult.android.presenter.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.adult.android.R;
import com.adult.android.entity.UserDto;
import com.adult.android.entity.UserResponse;
import com.adult.android.entity.VersionInfo;
import com.adult.android.logic.UserLogic;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnLoginCompletedListener;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.constants.ServiceUrlConstants.HOST;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.model.setting.AppUpgradeModel;
import com.adult.android.model.setting.AppUpgradeModel.OnCheckVersionListener;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.presenter.fragment.main.BaseTabFragment;
import com.adult.android.presenter.fragment.main.TabSwitcherFragment;
import com.adult.android.presenter.fragment.main.tab.CarsFragment2;
import com.adult.android.presenter.fragment.main.tab.CategoryFragment;
import com.adult.android.presenter.fragment.main.tab.CommunityFragment;
import com.adult.android.presenter.fragment.main.tab.HomePageFragment2;
import com.adult.android.presenter.fragment.main.tab.MyCcigFragment2;
import com.adult.android.service.DaemonService;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.Misc;
import com.adult.android.view.LoadingDialog;
import com.adult.android.view.SampleDialog;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnClickListener, OnCheckVersionListener {
	/** bottom tab switcher fragment */
	private TabSwitcherFragment tabSwitcherFragment;
	public static final String SWITCH_INDEX = "switch_index";
	private AppUpgradeModel appUpdateModel;
	// 是否强制升级
	private String apkUrl;

	private LoadingDialog loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setSlidingFinish(false);
		tabSwitcherFragment = (TabSwitcherFragment) Fragment.instantiate(this, TabSwitcherFragment.class.getName(),
				null);
		getSupportFragmentManager().beginTransaction().add(R.id.tab_switcher, tabSwitcherFragment, null).commit();

		tabSwitcherFragment
				.addOnTabSelectListener(0, new TabListener(this, "AFragment", HomePageFragment2.class, null));
		tabSwitcherFragment.addOnTabSelectListener(1, new TabListener(this, "BFragment", CategoryFragment.class, null));
		tabSwitcherFragment
				.addOnTabSelectListener(2, new TabListener(this, "CFragment", CommunityFragment.class, null));
		tabSwitcherFragment.addOnTabSelectListener(3, new TabListener(this, "DFragment", CarsFragment2.class, null));
		tabSwitcherFragment.addOnTabSelectListener(4, new TabListener(this, "EFragment", MyCcigFragment2.class, null));
		loadingDialog = new LoadingDialog(this);
		startService(new Intent(this, DaemonService.class));
		PackageManager pm = this.getPackageManager();

		/** 检查版本更新 */
		appUpdateModel = new AppUpgradeModel(this);
		if (HOST.TEST.getApiHost().equals(ServiceUrlConstants.getApiHost())) {
			appUpdateModel.checkUpgrade(this);
		}
		login();
		// handleIntent(getIntent());
	}

	private void login() {
		String userName = UserLogic.getInsatnace().getUserName();
		final String password = UserLogic.getInsatnace().getPassword();
		if (GeneralTool.isEmpty(userName) || GeneralTool.isEmpty(password)) {
			return;
		}
		loadingDialog.show();

		UserModel.getInstance().login(userName, password, new OnLoginCompletedListener() {

			@Override
			public void onSuccess(UserResponse info) {
				loadingDialog.dismiss();
				UserDto user = info.getData();
				user.setPassword(password);
				UserLogic.getInsatnace().setUserInfo(user);
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
				loadingDialog.dismiss();
			}

			@Override
			public void onFailed(ResponseException e) {
				loadingDialog.dismiss();
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (intent == null)
			return;
		final int switchIndex = intent.getIntExtra(SWITCH_INDEX, -1);
		if (switchIndex > -1) {
			tabSwitcherFragment.updateTab(switchIndex);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		item.getItemId();
		// if (id == R.id.action_check_version) {
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(new Intent(this, DaemonService.class));
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
			// if ((System.currentTimeMillis() - lastBackPressTime) < 3 * 1000)
			// {
			// lastBackPressTime = 0;
			// finish();
			// } else {
			// lastBackPressTime = System.currentTimeMillis();
			// ToastUtil.showToastShort(this, R.string.press_exit_app);
			// }
			// return true;

			final SampleDialog dialog = new SampleDialog(this) {

				@Override
				public View getContentView() {
					return null;
				}
			};
			dialog.setTitleText("提醒", R.color.black);
			dialog.setContentText("确定要退出成人秀吗？", R.color.gray);
			dialog.setTwoButton(R.string.cancel, R.color.black, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					dialog.dismiss();
				}
			}, R.string.ok, R.color.red, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
					AgentApplication.get().exit();
					dialog.dismiss();
				}
			});
			dialog.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View arg0) {
		final int id = arg0.getId();
		switch (id) {
		// case R.id.upgrade:
		// /** 用户点击了检查更新按钮，进行一次版本更新检测 */
		// appUpdateModel.checkUpgrade(this);
		// break;

		default:
			break;
		}

	}

	public static class TabListener<T extends Fragment> implements TabSwitcherFragment.OnTabSelectListener {
		private final MainActivity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private BaseTabFragment mFragment;

		public TabListener(MainActivity activity, String tag, Class<T> clz) {
			this(activity, tag, clz, null);
		}

		public TabListener(MainActivity activity, String tag, Class<T> clz, Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			mFragment = (BaseTabFragment) mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
				ft.detach(mFragment);
				ft.commit();
			}
		}

		@Override
		public void onTabSelected() {
			FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
			if (mFragment == null) {
				mFragment = (BaseTabFragment) Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				ft.add(R.id.tab_container, mFragment, mTag);
			} else {
				ft.show(mFragment);
			}
			ft.commit();
			mFragment.onSelected();
		}

		@Override
		public void onTabUnselected() {
			if (mFragment != null) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
				ft.hide(mFragment);
				ft.commit();
				mFragment.onUnSelected();
			}
		}

		@Override
		public void onTabReselected() {

		}
	}

	public TabSwitcherFragment getTabSwitcherFragment() {
		return tabSwitcherFragment;
	}

	public void setTabSwitcherFragment(TabSwitcherFragment tabSwitcherFragment) {
		this.tabSwitcherFragment = tabSwitcherFragment;
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
		// dialog.setContentText(releaseNotes, 0);
		dialog.setContentText(Misc.ToDBC(releaseNotes), 0);
		dialog.setContentTextGravity(Gravity.LEFT);
		if (res == 1) {
			dialog.setCancelable(false);
			dialog.setSingleButton(R.string.upgrade, 0, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Misc.startSystemBrowser(MainActivity.this, apkUrl);
					new Handler().postDelayed(new Runnable() {

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
					Misc.startSystemBrowser(MainActivity.this, apkUrl);
					dialog.dismiss();
				}
			});
		}
		dialog.show();
	}

	@Override
	public void onNotNeedUpdateVersion(boolean same) {
	}

	@Override
	public void onCheckVersionFail(ResponseException e) {
	}

	@Override
	public void onRequestStart() {

	}

	@Override
	public void onRequestFinish() {

	}

}
