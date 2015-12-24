package com.adult.android.presenter.fragment.main.tab;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.HomeFinalItem;
import com.adult.android.entity.HomeGroup;
import com.adult.android.entity.HomePageResponse2;
import com.adult.android.model.HomePageModel;
import com.adult.android.model.HomePageModel.OnGetHomePageDataCompletedListener2;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.internet.exception.BusinessException;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.presenter.activity.MainActivity;
import com.adult.android.presenter.activity.ProductDetailsActivity2;
import com.adult.android.presenter.activity.ProductListActivity;
import com.adult.android.presenter.activity.PromotionActivity;
import com.adult.android.presenter.activity.SearchActivity;
import com.adult.android.presenter.activity.TopicListActivity;
import com.adult.android.presenter.fragment.main.BaseTabFragment;
import com.adult.android.utils.Misc;
import com.adult.android.utils.MyTimer;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.adult.android.view.MyGallery;
import com.adult.android.view.MyGallery.OnWindowAttachedChanged;
import com.adult.android.view.SampleDialog;
import com.adult.android.view.widget.adapter.GalleryAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.BitmapUtils;

/**
 * 首页
 * 
 * @author LiCheng
 * 
 */
public class HomePageFragment2 extends BaseTabFragment implements
		OnClickListener, OnRefreshListener2<ScrollView> {

	public static final String Tag = "HomePageFragment";

	private LinearLayout llytMainView, mainContainer;

	private ViewGroup galleryHeader;

	private MyGallery mGallery;

	private ViewGroup pointGroup;

	private GalleryAdapter galleryAdapter = null;
	// 三个功能数据列表
	private List<HomeFinalItem> listBanner, listGuid;

	private List<HomeGroup> listGroup;

	private BitmapUtils bitmapUtils;

	private PullToRefreshScrollView scrollView;

	// private TextView txtMessageCount;

	private LoadingDialog loadingDialog;

	private OnWindowAttachedChangedListener mOnWindowAttachedChanged;

	public HomePageFragment2() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		llytMainView = (LinearLayout) inflater.inflate(
				R.layout.fragment_home_page2, null);
		return llytMainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initFragment();
	}

	/** 初始化 */
	private void initFragment() {
		initActivityTitle();
		bitmapUtils = new BitmapUtils(getActivity());
		bitmapUtils
				.configDefaultLoadFailedImage(R.drawable.img_default_672_280);
		scrollView = (PullToRefreshScrollView) llytMainView
				.findViewById(R.id.homepage_listview);
		scrollView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		mainContainer = (LinearLayout) llytMainView
				.findViewById(R.id.homepage_content);
		scrollView.setOnRefreshListener(this);
		loadingDialog = new LoadingDialog(getActivity());

		getDataList(0);
		// 添加数据
		// addData();
	}

	/**
	 * 获取数据
	 * 
	 * @param i
	 */
	private void getDataList(final int flag) {
		mainContainer.removeAllViewsInLayout();
		loadingDialog.show();
		HomePageModel model = HomePageModel.getInsance();
		model.getHomePageData2(new OnGetHomePageDataCompletedListener2() {
			@Override
			public void onCompleted(HomePageResponse2 info) {
				loadingDialog.dismiss();
				scrollView.onRefreshComplete();
				listBanner = new ArrayList<HomeFinalItem>();
				listGuid = new ArrayList<HomeFinalItem>();
				listGroup = new ArrayList<HomeGroup>();
				listBanner.addAll(info.getData().getBanner());
				listGroup.addAll(info.getData().getGroup());
				listGuid.addAll(info.getData().getGuide());
				// 添加数据
				addData();

			}

			@Override
			public void onFailed(BusinessException e) {
				loadingDialog.dismiss();
				scrollView.onRefreshComplete();
				ToastUtil.showToastShort(getActivity(), e.getResultMsg());
			}

			@Override
			public void onHttpException(HttpResponseException e) {
				scrollView.onRefreshComplete();
				ToastUtil.showToastShort(getActivity(), "网络请求异常");
			}

			@Override
			public void onStart() {
			}

			@Override
			public void onFinish() {
				loadingDialog.dismiss();
				scrollView.onRefreshComplete();
			}
		});
	}

	/** 添加数据 */
	protected void addData() {
		// 添加广告页
		addBanner();
		onResume();// 开始滚动
		addGuidList();
		addGroupList();
	}

	/** 添加大模块 */
	protected void addGroupList() {
		for (int i = 0; i < listGroup.size(); i++) {
			if (0 == i) {
				addTopicLayout(listGroup.get(0));// “热门话题”
				continue;
			}
			addGroupItem(i);
		}
	}

	/**
	 * 热门话题
	 * 
	 * @param homeGroupDto
	 */
	private void addTopicLayout(final HomeGroup hotTopic) {
		View viewTopic = LayoutInflater.from(getActivity()).inflate(
				R.layout.layout_homepage_group_topic, null);
		viewTopic.findViewById(R.id.layout_homepage_group_topic_more)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						swicheIntent(hotTopic.getMoreType(), hotTopic.getMore());
					}
				});
		ImageView image = (ImageView) viewTopic
				.findViewById(R.id.homepage_group_topic_image);
		bitmapUtils.display(image, ServiceUrlConstants.getImageHost()
				+ hotTopic.getDataList().get(0).getImgUrl());
		image.setOnClickListener(new OnFinalItemClickListener(hotTopic
				.getDataList().get(0)));
		mainContainer.addView(viewTopic);
	}

	/** 添加Group列表 */
	private void addGroupItem(int i) {
		View groupView = LayoutInflater.from(getActivity()).inflate(
				R.layout.layout_homepage_group_base, null);
		final HomeGroup group = listGroup.get(i);
		if (null == group) {
			return;
		}
		TextView txtTitle = (TextView) groupView
				.findViewById(R.id.homepage_group_base_title);
		txtTitle.setText(group.getTitle());
		groupView.findViewById(R.id.homepage_group_base_more)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						swicheIntent(group.getMoreType(), group.getMore());
					}
				});
		addItemList(i, groupView);
		mainContainer.addView(groupView);
	}

	/** 添加最终子项 */
	private void addItemList(int i, View parent) {
		List<HomeFinalItem> dataList = listGroup.get(i).getDataList();
		LinearLayout middleContainer = (LinearLayout) parent
				.findViewById(R.id.homepage_group_base_middle_container);
		LinearLayout bottomContainer = (LinearLayout) parent
				.findViewById(R.id.homepage_group_base_bottom_container);
		if (null == dataList) {
			return;
		}
		if (0 == i) {
			return;
		}
		// 显示第一个子项（共通）
		bitmapUtils.display(
				parent.findViewById(R.id.homepage_group_base_top_image),
				ServiceUrlConstants.getImageHost()
						+ dataList.get(0).getImgUrl());
		// 添加中间的布局(这一区域是无规律定死的权重)
		switch (i) {
		case 1:// "情趣服饰" 布局 1:1;
			addMiddleItem(11, middleContainer, bottomContainer, dataList);
			break;
		case 2:// "套套天堂";
			addMiddleItem(111, middleContainer, bottomContainer, dataList);
			break;
		case 3:// "女性玩具";
			addMiddleItem(21, middleContainer, bottomContainer, dataList);
			break;
		case 4:// "男性玩具";
			addMiddleItem(111, middleContainer, bottomContainer, dataList);
			break;
		case 5:// "调情助兴";
			addMiddleItem(21, middleContainer, bottomContainer, dataList);
			break;
		default:
			break;
		}
	}

	private void addBottomItems(HomeFinalItem data1, HomeFinalItem data2,
			HomeFinalItem data3, LinearLayout bottomContainer) {
		if (null != data1) {
			View view = getActivity().getLayoutInflater().inflate(
					R.layout.item_homepage_group_weight_1, null);
			view.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f));
			addItem(bottomContainer, view, data1);
		}
		if (null != data2) {
			View view = getActivity().getLayoutInflater().inflate(
					R.layout.item_homepage_group_weight_1, null);
			view.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f));
			addItem(bottomContainer, view, data2);
		}
		if (null != data3) {
			View view = getActivity().getLayoutInflater().inflate(
					R.layout.item_homepage_group_weight_1, null);
			view.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f));
			addItem(bottomContainer, view, data3);
		}
	}

	private void addMiddleItem(int type, LinearLayout middleContainer,
			LinearLayout bottomContainer, List<HomeFinalItem> dataList) {
		HomeFinalItem item1 = dataList.size() >= 2 ? dataList.get(1) : null;
		HomeFinalItem item2 = dataList.size() >= 3 ? dataList.get(2) : null;
		HomeFinalItem item3 = dataList.size() >= 4 ? dataList.get(3) : null;
		HomeFinalItem item4 = dataList.size() >= 5 ? dataList.get(4) : null;
		HomeFinalItem item5 = dataList.size() >= 6 ? dataList.get(5) : null;
		HomeFinalItem item6 = dataList.size() >= 7 ? dataList.get(6) : null;
		switch (type) {
		case 11:
			if (null == item1) {
				return;
			}
			View view11_1 = getActivity().getLayoutInflater().inflate(
					R.layout.item_homepage_group_weight_1, null);
			view11_1.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f));
			addItem(middleContainer, view11_1, item1);
			if (null == item2) {
				return;
			}
			View view11_2 = getActivity().getLayoutInflater().inflate(
					R.layout.item_homepage_group_weight_1, null);
			view11_2.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f));
			addItem(middleContainer, view11_2, item2);
			addBottomItems(item3, item4, item5, bottomContainer);
			break;
		case 21:
			if (null == item1) {
				return;
			}
			View view21_1 = getActivity().getLayoutInflater().inflate(
					R.layout.item_homepage_group_weight_2, null);
			view21_1.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f));
			addItem(middleContainer, view21_1, item1);
			if (null == item2) {
				return;
			}
			View view21_2 = getActivity().getLayoutInflater().inflate(
					R.layout.item_homepage_group_weight_1, null);
			view21_2.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f));
			addItem(middleContainer, view21_2, item2);
			addBottomItems(item3, item4, item5, bottomContainer);
			break;
		case 111:
			if (null == item1) {
				return;
			}
			View view111_1 = getActivity().getLayoutInflater().inflate(
					R.layout.item_homepage_group_weight_1, null);
			view111_1.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f));
			addItem(middleContainer, view111_1, item1);
			if (null == item2) {
				return;
			}
			View view111_2 = getActivity().getLayoutInflater().inflate(
					R.layout.item_homepage_group_weight_1, null);
			view111_2.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f));
			addItem(middleContainer, view111_2, item2);
			if (null == item3) {
				return;
			}
			View view111_3 = getActivity().getLayoutInflater().inflate(
					R.layout.item_homepage_group_weight_1, null);
			view111_3.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f));
			addItem(middleContainer, view111_3, item3);
			addBottomItems(item4, item5, item6, bottomContainer);
			break;
		default:
			break;
		}

	}

	/** 添加最终子项 */
	private void addItem(LinearLayout container, View view, HomeFinalItem item) {
		bitmapUtils.display(view.findViewById(R.id.item_homepage_group_image),
				ServiceUrlConstants.getImageHost() + item.getImgUrl());
		view.setOnClickListener(new OnFinalItemClickListener(item));
		container.addView(view);
	}

	/** 最终子项点击事件 */
	class OnFinalItemClickListener implements OnClickListener {

		private HomeFinalItem item;

		public OnFinalItemClickListener(HomeFinalItem item) {
			this.item = item;
		}

		@Override
		public void onClick(View view) {
			swicheIntent(item.getType(), item.getJumpId());
		}
	}

	/** 添加四个导航 */
	protected void addGuidList() {
		if (null == listGuid || 0 == listGuid.size()) {
			return;
		}
		LinearLayout guidContainer = (LinearLayout) getActivity()
				.getLayoutInflater().inflate(R.layout.layout_homepage_guid,
						null);
		for (final HomeFinalItem item : listGuid) {
			if (null != item) {
				LinearLayout itemGuid = (LinearLayout) LayoutInflater.from(
						getActivity()).inflate(R.layout.item_homepage_guid,
						null);
				itemGuid.setLayoutParams(new LinearLayout.LayoutParams(0,
						LayoutParams.WRAP_CONTENT, 1.0f));
				ImageView image = (ImageView) itemGuid
						.findViewById(R.id.item_homepage_guid_image);
				TextView txt = (TextView) itemGuid
						.findViewById(R.id.item_homepage_guid_text);
				txt.setText(item.getName());
				bitmapUtils.display(image, ServiceUrlConstants.getImageHost()
						+ item.getImgUrl());
				itemGuid.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						swicheIntent(item.getType(), item.getJumpId());
					}
				});
				guidContainer.addView(itemGuid);
			}
		}
		mainContainer.addView(guidContainer);
	}

	/** 跳转到搜索画面 */
	public void goToSearch() {
		// animView.startAnim();
		Intent intent = new Intent();
		intent.setClass(getActivity(), SearchActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.search_view_root:
			goToSearch();
			break;
		// case R.id.homepage_guid_1:
		// partItemClick(0);
		// break;
		// case R.id.homepage_guid_2:
		// partItemClick(1);
		// break;
		// case R.id.homepage_guid_3:
		// partItemClick(2);
		// break;
		// case R.id.homepage_guid_4:
		// partItemClick(3);
		// break;
		default:
			break;
		}
	}

	private void partItemClick(int i) {
		if (i >= listGuid.size() || null == listGuid.get(i)) {
			return;
		}
		String promotionId = listGuid.get(i).getJumpId();
		Intent intent = new Intent();
		if (null == listGuid) {
			ToastUtil.showToastShort(getActivity(), "参数不足");
			return;
		}
		intent.putExtra(PromotionActivity.PROMOTION_ID, promotionId);
		intent.setClass(getActivity(), PromotionActivity.class);
		startActivity(intent);
	}

	/** 跳转 */
	public void swicheIntent(int type, String params) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		switch (type) {
		case 0:// 帖子列表
			intent.putExtra(TopicListActivity.EXTRA_COMMUNITY_ID, params);
			intent.setClass(getActivity(), TopicListActivity.class);
			break;
		case 2:// 商品详情
			intent.putExtra(ProductDetailsActivity2.EXTRA_PRODUCT_ID, params);
			intent.setClass(getActivity(), ProductDetailsActivity2.class);
			break;
		case 3:// 商品列表(类目id)
			intent.putExtra(ProductListActivity.CATEGORY_ID, params);
			intent.setClass(getActivity(), ProductListActivity.class);
			break;
		case 4: // 商品列表(关键字)
			intent.putExtra(ProductListActivity.KEWORD, params);
			intent.setClass(getActivity(), ProductListActivity.class);
			break;
		case 5: // 专题页
			intent.putExtra(PromotionActivity.PROMOTION_ID, params);
			intent.setClass(getActivity(), PromotionActivity.class);
			break;
		case 6: // 帖子列表
			intent.putExtra(TopicListActivity.EXTRA_COMMUNITY_ID, params);
			intent.setClass(getActivity(), TopicListActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent);
	}

	private class OnWindowAttachedChangedListener implements
			OnWindowAttachedChanged {
		private MyTimer saveTimer = null;

		@Override
		public void onDetachedFromWindow(MyGallery view) {
			if (saveTimer != null) {
				saveTimer.stop();
			}
		}

		@Override
		public void onAttachedToWindow(MyGallery view) {
			startScroll();
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN: {
				if (saveTimer != null) {
					saveTimer.stop();
				}
				break;
			}
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				startScroll();
				break;
			}
		}

		int[] location = new int[2];

		public void startScroll() {
			if (saveTimer != null) {
				saveTimer.stop();
			} else {
				saveTimer = new MyTimer(new MyTimer.Listener() {
					@SuppressWarnings("unused")
					@Override
					public void onTimer(MyTimer timer) {
						mGallery.getLocationOnScreen(location);
						if (location[0] < 0 || location[0] > 320
								|| location[1] < -100) {
							return;
						}
						int position = mGallery.getSelectedItemPosition();
						View item = null;
						boolean isScroll = true;
						if (item != null) {
							try {
								mGallery.setSelection(position + 2, false);
							} catch (OutOfMemoryError e) {
							}
							isScroll = false;
						}
						if (isScroll) {
							mGallery.scrollRight();
						}
					}
				});
			}
			saveTimer.start(3000);
		}

		public void stopScroll() {
			if (saveTimer != null) {
				saveTimer.stop();
			}
		}
	}

	/** 设置标题 */
	private void initActivityTitle() {
		((MainActivity) getActivity()).setShowTitleBar(true);
		((MainActivity) getActivity())
				.setTitleBackground(R.drawable.common_top_bg);
		((MainActivity) getActivity()).setActivityTitle("秀色成人");
		((MainActivity) getActivity()).setActivityTitleColor(getResources()
				.getColor(R.color.white));
		((MainActivity) getActivity()).setShowSearchViewHome();
		((MainActivity) getActivity())
				.setRighButtonBackGround(R.drawable.customer_service_phone);
		((MainActivity) getActivity())
				.setRightButtonOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						final SampleDialog dialog = new SampleDialog(
								getActivity()) {

							@Override
							public View getContentView() {
								return null;
							}
						};
						dialog.setTitleText("联系客服", R.color.black);
						dialog.setContentText("010-84460100", R.color.gray);
						dialog.setTwoButton(R.string.cancel, R.color.black,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										dialog.dismiss();
									}
								}, R.string.ok, R.color.red,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										Intent intent = new Intent(
												Intent.ACTION_CALL);
										intent.setData(Uri
												.parse("tel:010-84460100"));
										startActivity(intent);
										dialog.dismiss();
									}
								});
						dialog.show();
					}
				});
	}

	/** 添加游标 */
	public void addBanner() {
		if (galleryHeader != null) {
			mainContainer.removeView(galleryHeader);
		}
		galleryHeader = (ViewGroup) View.inflate(getActivity(),
				R.layout.recommend_gallery, null);
		mainContainer.addView(galleryHeader);
		mGallery = (MyGallery) galleryHeader.findViewById(R.id.recommend_adv);
		pointGroup = (ViewGroup) galleryHeader
				.findViewById(R.id.recommend_hint_panel);
		galleryAdapter = new GalleryAdapter(getActivity(), listBanner,
				bitmapUtils);
		mGallery.setAdapter(galleryAdapter);
		mOnWindowAttachedChanged = new OnWindowAttachedChangedListener();
		mGallery.setOnWindowAttachedChanged(mOnWindowAttachedChanged);
		final int picCount = listBanner.size();
		mGallery.setSelection(picCount * 100);
		// 设置布局参数
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int marginParam = Misc.dip2px(getActivity(), 10) / 2;

		pointGroup.removeAllViewsInLayout();
		for (int i = 0; i < picCount; i++) {
			// 设置按钮属性
			View item = new ImageView(getActivity());
			item.setBackgroundResource(R.drawable.recommend_gallery);
			item.setTag(String.valueOf(i));
			params.setMargins(marginParam, 0, marginParam, 0);
			params.weight = 1;
			pointGroup.addView(item, params);
		}
		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				for (int i = 0; i < picCount; i++) {
					if (pointGroup == null || pointGroup.getChildCount() <= i) {
						continue;
					}
					if (i == position % picCount) {
						pointGroup.getChildAt(i).setBackgroundResource(
								R.drawable.recommend_gallery_select);
					} else {
						pointGroup.getChildAt(i).setBackgroundResource(
								R.drawable.recommend_gallery);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long position) {
				swicheIntent(listBanner.get((int) position % picCount)
						.getType(), listBanner.get((int) position % picCount)
						.getJumpId());
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mOnWindowAttachedChanged != null) {
			mOnWindowAttachedChanged.startScroll();
		}
	}

	@Override
	public void onPause() {
		if (mOnWindowAttachedChanged != null) {
			mOnWindowAttachedChanged.stopScroll();
		}
		super.onPause();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		getDataList(0);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			initActivityTitle();
		}
	}
}
