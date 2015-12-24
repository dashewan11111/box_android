package com.adult.android.presenter.fragment.main.tab.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.OrderDtoForList;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.presenter.activity.EvaluationActivity;
import com.adult.android.presenter.activity.OrderDetailsActivity2;
import com.adult.android.utils.ToastUtil;
import com.lidroid.xutils.BitmapUtils;

public class OrderListAdapter extends BaseAdapter {

	private Context context;

	private List<OrderDtoForList> productList;

	private BitmapUtils bitmapUtils;

	public OrderListAdapter(Context context, List<OrderDtoForList> productList) {
		this.context = context;
		this.productList = productList;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.img_default_114);
		bitmapUtils.configDefaultLoadingImage(R.drawable.img_default_114);
	}

	@Override
	public int getCount() {
		return productList == null ? 0 : productList.size();
	}

	@Override
	public Object getItem(int position) {
		return productList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_order_list, null);
			holder.image = (ImageView) convertView.findViewById(R.id.item_order_list_img);
			holder.txtOrderNum = (TextView) convertView.findViewById(R.id.item_order_list_id);
			holder.txtOrderStatus = (TextView) convertView.findViewById(R.id.item_order_list_status);
			holder.txtCreatTime = (TextView) convertView.findViewById(R.id.item_order_list_time);
			holder.txtCount = (TextView) convertView.findViewById(R.id.item_order_list_count);
			holder.txtOrderAmount = (TextView) convertView.findViewById(R.id.item_order_list_amount);
			holder.txtPayway = (TextView) convertView.findViewById(R.id.item_order_list_pay_way);
			holder.btnPay = (Button) convertView.findViewById(R.id.item_order_list_btn_pay);
			// holder.btnPay.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final OrderDtoForList order = (OrderDtoForList) getItem(position);

		holder.txtOrderNum.setText(order.getOrderId());
		String status = "";
		switch (order.getStatus()) {
		case 1:
			status = "已完成";
			break;
		case 2:
			status = "待付款";
			holder.btnPay.setVisibility(View.VISIBLE);
			break;
		case 3:
			status = "待发货";
			break;
		case 4:
			status = "待收货";
			break;
		case 5:
			status = "待评价";
			break;
		default:
			break;
		}
		holder.txtOrderStatus.setText(status);
		holder.txtCreatTime.setText(order.getCreateTime());
		holder.txtCount.setText(order.getSkuCount() + "件");
		holder.txtOrderAmount.setText(context.getResources().getString(R.string.rmb) + order.getOrderAmount());
		if ("1".equals(order.getPayWay())) {

		} else if ("2".equals(order.getPayWay())) {

		} else {

		}
		holder.txtPayway.setText("");
		holder.btnPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				switch (order.getStatus()) {
				case 1:
					holder.btnPay.setText("去付款");
					// Intent intent2 = new Intent(context,
					// EvaluationActivity.class);
					// intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
					// Intent.FLAG_ACTIVITY_SINGLE_TOP);
					// intent2.putExtra("order", order);
					// context.startActivity(intent2);
					ToastUtil.showToastShort(context, "去付款");
					break;
				case 2:
					holder.btnPay.setText("去付款");
					// Intent intentPay = new Intent(context,
					// EvaluationActivity.class);
					// intentPay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					// intentPay.putExtra("order", order);
					// context.startActivity(intentPay);
					ToastUtil.showToastShort(context, "去付款");
					break;
				case 3:
					holder.btnPay.setVisibility(View.INVISIBLE);
					break;
				case 4:
					holder.btnPay.setText("确认收货");
					// Intent intentRe = new Intent(context,
					// EvaluationActivity.class);
					// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					// intent.putExtra("order", order);
					// context.startActivity(intent);
					ToastUtil.showToastShort(context, "确认收货");
					break;
				case 5:
					holder.btnPay.setText("去评价");
					Intent intent = new Intent(context, EvaluationActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.putExtra("order", order);
					context.startActivity(intent);
					break;
				default:
					break;
				}

			}
		});
		bitmapUtils.display(holder.image, ServiceUrlConstants.getImageHost() + order.getImgUrl());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(context, OrderDetailsActivity2.class);
				intent.putExtra("orderId", order.getOrderId());
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	private class ViewHolder {

		private ImageView image;

		private TextView txtOrderNum;

		private TextView txtOrderStatus;

		private TextView txtCreatTime;

		private TextView txtCount;

		private TextView txtOrderAmount;

		private TextView txtPayway;

		private Button btnPay;

	}
}
