package com.adult.android.presenter.fragment.main.tab.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.HomeFinalItem;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.lidroid.xutils.BitmapUtils;

public class HomeListViewAdapter extends BaseAdapter {

	private Context context;

	private List<HomeFinalItem> dataList;

	private BitmapUtils bitmapUtils;

	public HomeListViewAdapter(Context context, List<HomeFinalItem> dataList, BitmapUtils bitmapUtils, ListView listView) {
		this.context = context;
		this.dataList = dataList;
		this.bitmapUtils = bitmapUtils;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HomePageHodler holder = null;
		if (null == convertView) {
			holder = new HomePageHodler();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_home_page_list, null);
			holder.img = (ImageView) convertView.findViewById(R.id.item_home_page_img);
			holder.txt = (TextView) convertView.findViewById(R.id.item_home_page_txt);
			convertView.setTag(holder);
		} else {
			holder = (HomePageHodler) convertView.getTag();
		}
		// LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
		// holder.img
		// .getLayoutParams();
		// int width = Misc.getScreenDisplay((Activity) context)[0]
		// - Misc.dip2px(context, 20);
		// int height = width * 9 / 16;
		// params.width = width;
		// params.height = height;
		// holder.img.setLayoutParams(params);
		HomeFinalItem dto = dataList.get(position);
		holder.txt.setText("类型：" + dto.getType());
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.img_default_720_360);
		bitmapUtils.display(holder.img, ServiceUrlConstants.getImageHost() + dto.getImgUrl());
		return convertView;
	}

	class HomePageHodler {

		ImageView img;

		TextView txt;
	}
}
