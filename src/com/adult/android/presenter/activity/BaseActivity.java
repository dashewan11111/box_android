package com.adult.android.presenter.activity;

import java.util.Locale;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.presenter.AgentApplication;
import com.baidu.mobstat.StatService;

public class BaseActivity extends FragmentActivity {

	private LinearLayout layoutHeader;

	private ImageView btnLeft, btnRight;

	private TextView txtMessage, txtActivityTitle, txtLeft, txtRight;

	private FrameLayout container, middleSearch, middleSearchFull;

	private View exceptionView;

	public LinearLayout pop_layout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseactivity);
		pop_layout = (LinearLayout) findViewById(R.id.popup_layout);

		// 初始化
		initTitleBar();
		setShowTitleBar(false);
		initActivityView(null);
		AgentApplication.get().addActivity(this);
	}

	protected boolean isFullScreen() {
		return false;
	}

	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}

	protected void retry() {

	}

	public void showExceptionView() {
		exceptionView.setVisibility(View.VISIBLE);
	}

	public void hideExceptionView() {
		exceptionView.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
		// JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
		// JPushInterface.onPause(this);
	}

	// 自定义标题栏 初始化
	private void initTitleBar() {
		layoutHeader = (LinearLayout) findViewById(R.id.common_header_main);
		btnLeft = (ImageView) findViewById(R.id.common_header_left_button);
		btnRight = (ImageView) findViewById(R.id.common_header_right_button);
		txtLeft = (TextView) findViewById(R.id.common_header_left_text);
		txtRight = (TextView) findViewById(R.id.common_header_right_text);
		txtMessage = (TextView) findViewById(R.id.common_header_left_message_count);
		txtActivityTitle = (TextView) findViewById(R.id.common_header_middle_title);
		middleSearch = (FrameLayout) findViewById(R.id.common_header_middle_search);
		middleSearchFull = (FrameLayout) findViewById(R.id.common_header_middle_search_full);
		btnRight.setVisibility(View.INVISIBLE);
		txtLeft.setVisibility(View.GONE);
		txtRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});
		middleSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(BaseActivity.this, SearchActivity.class);
				startActivity(intent);
			}
		});
		middleSearchFull.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(BaseActivity.this, SearchActivity.class);
				startActivity(intent);
			}
		});
	}

	public void setShowTitleBar(boolean show) {
		if (show) {
			layoutHeader.setVisibility(View.VISIBLE);
		} else {
			layoutHeader.setVisibility(View.GONE);
		}
	}

	public void setActivityTitle(String title) {
		txtActivityTitle.setText(title);
	}

	public void setActivityTitle(int id) {
		txtActivityTitle.setText(id);
	}

	public void setActivityTitleColor(int colorId) {
		txtActivityTitle.setTextColor(colorId);
	}

	public void setShowLeftButton(boolean show) {
		if (show) {
			btnLeft.setVisibility(View.VISIBLE);
		} else {
			btnLeft.setVisibility(View.INVISIBLE);
		}
	}

	public void setShowLeftText(String text) {
		txtLeft.setVisibility(View.VISIBLE);
		txtLeft.setText(text);
		btnLeft.setVisibility(View.GONE);
	}

	public void setShowRightText(String text) {
		txtRight.setVisibility(View.VISIBLE);
		txtRight.setText(text);
		btnRight.setVisibility(View.GONE);
	}

	public void setShowRightButton(boolean show) {
		if (show) {
			btnRight.setVisibility(View.VISIBLE);
		} else {
			btnRight.setVisibility(View.INVISIBLE);
		}
	}

	public void setTitleBackgroundColor(int color) {
		layoutHeader.setBackgroundColor(color);
	}

	public void setTitleBackground(int drawableId) {
		layoutHeader.setBackgroundResource(drawableId);
	}

	public void setLeftButtonBackGround(int drawResId) {
		btnLeft.setImageResource(drawResId);
	}

	public void setRighButtonBackGround(int drawResId) {
		btnRight.setVisibility(View.VISIBLE);
		btnRight.setImageResource(drawResId);
	}

	public void setLeftButtonOnClickListener(OnClickListener l) {
		btnLeft.setOnClickListener(l);
	}

	public void setRightButtonOnClickListener(OnClickListener l) {
		btnRight.setOnClickListener(l);
	}

	public void setLeftTextOnClickListener(OnClickListener l) {
		txtLeft.setOnClickListener(l);
	}

	public void setRightTextOnClickListener(OnClickListener l) {
		txtRight.setOnClickListener(l);
	}

	public void setShowMiddleView() {
		middleSearch.setVisibility(View.GONE);
		middleSearchFull.setVisibility(View.GONE);
		findViewById(R.id.common_header_middle_layout).setVisibility(
				View.VISIBLE);
		btnRight.setVisibility(View.GONE);
	}

	public void setShowSearchViewHome() {
		middleSearch.setVisibility(View.VISIBLE);
		middleSearchFull.setVisibility(View.GONE);
		findViewById(R.id.common_header_middle_layout).setVisibility(View.GONE);
		btnLeft.setVisibility(View.GONE);
	}

	public void setShowSearchViewCategory() {
		middleSearch.setVisibility(View.GONE);
		middleSearchFull.setVisibility(View.VISIBLE);
		findViewById(R.id.common_header_middle_layout).setVisibility(View.GONE);
		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
	}

	/**
	 * 用于mainContent初始化
	 * 
	 * @param view
	 *            View 新的view | null 根据activity.class自动获取
	 * @return view 返回新的view
	 */
	private View initActivityView(View view) {
		container = (FrameLayout) findViewById(R.id.contentFrame);
		// setSlidingFinish(true);
		// slidingFinishLayout
		// .setOnSildingFinishListener(new OnSildingFinishListener() {
		//
		// @Override
		// public void onSildingFinish() {
		// finish();
		// overridePendingTransition(0, R.anim.slide_out_to_right);
		// }
		// });
		// 根据activity.class选择默认View
		if (view == null) {
			try {
				view = LayoutInflater.from(this).inflate(
						R.layout.class.getField(
								this.getClass().getSimpleName()
										.toLowerCase(Locale.getDefault()))
								.getInt(R.layout.class), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (view != null) {
			container.addView(view);
		}
		exceptionView = LayoutInflater.from(this).inflate(
				R.layout.error_loading_layout, null);
		exceptionView.findViewById(R.id.retry).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						retry();
					}
				});
		container.addView(exceptionView, new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		hideExceptionView();
		return view;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AgentApplication.get().removeActivity(this);
	}

	/**
	 * 获取正在运行的activity名称
	 * 
	 * @return
	 */
	private String getRunningActivityName() {
		String contextString = this.toString();
		String className = contextString.substring(
				contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
		return className;
	}

	public void btnTopTitleBack(View view) {
		finish();
	}

	// public void setSlidingFinish(boolean slidingFinish) {
	// if (slidingFinish) {
	// slidingFinishLayout.setTouchView(container);
	// } else {
	// slidingFinishLayout.setTouchView(null);
	// }
	// }
}
