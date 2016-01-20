package com.adult.android.presenter.fragment.main.tab.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.UserOrdersDTO;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.view.LoadingDialog;
import com.lidroid.xutils.BitmapUtils;

public class OrderListAdapterForBox extends BaseAdapter {

	private Context context;

	private List<UserOrdersDTO> productList;

	private BitmapUtils bitmapUtils;

	private LoadingDialog loadingDialog;

	private OnPayOrderListener listener;

	public OrderListAdapterForBox(Context context, OnPayOrderListener listener,
			List<UserOrdersDTO> orderList) {
		this.context = context;
		this.listener = listener;
		this.productList = orderList;
		bitmapUtils = new BitmapUtils(context);
		loadingDialog = new LoadingDialog(context);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_order_list_box, null);
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_order_list_box_sku_img);
			holder.txtSkuName = (TextView) convertView
					.findViewById(R.id.item_order_list_box_sku_name);
			holder.txtOrderNum = (TextView) convertView
					.findViewById(R.id.item_order_list_box_order_num);
			holder.txtOrderStatus = (TextView) convertView
					.findViewById(R.id.item_order_list_box_order_status);
			holder.txtCreatTime = (TextView) convertView
					.findViewById(R.id.item_order_list_box_time);
			holder.txtBoxNum = (TextView) convertView
					.findViewById(R.id.item_order_list_box_box_num);
			holder.txtOrderAmount = (TextView) convertView
					.findViewById(R.id.item_order_list_box_amount);
			holder.btnPay = (Button) convertView
					.findViewById(R.id.item_order_list_box_btn_pay);
			holder.txtSaving = (TextView) convertView
					.findViewById(R.id.item_order_list_box_saving);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final UserOrdersDTO order = (UserOrdersDTO) getItem(position);

		holder.txtOrderNum.setText(order.getOrderNumber());
		holder.txtSkuName.setText(order.getShopName());
		// holder.txtSaving.setText(order.get);
		String status = "";
		switch (order.getOrderStatus()) {
		case 0:
			status = "待付款";
			holder.btnPay.setText("确认付款");
			holder.txtOrderStatus.setText(status);
			holder.btnPay.setEnabled(true);
			break;
		default:
			status = "已完成";
			holder.btnPay.setText(status);
			holder.txtOrderStatus.setText(status);
			holder.btnPay.setEnabled(false);
			break;
		}

		holder.txtCreatTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(order.getOrderDateTime()));
		holder.txtBoxNum.setText("共" + order.getBoxNum() + "箱");
		holder.txtOrderAmount.setText(context.getResources().getString(
				R.string.euro)
				+ order.getOrderPrice());

		holder.btnPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				listener.onPayOrder(position);
			}
		});
		bitmapUtils.display(holder.image, ServiceUrlConstants.getImageHost()
				+ order.getShopIcon());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Intent intent = new Intent();
				// intent.setClass(context, OrderDetailsActivity2.class);
				// intent.putExtra("orderId", order.getOrderId());
				// context.startActivity(intent);
			}
		});
		return convertView;
	}

	private class ViewHolder {

		private ImageView image;

		private TextView txtOrderNum, txtSkuName;

		private TextView txtOrderStatus;

		private TextView txtCreatTime;

		private TextView txtBoxNum;

		private TextView txtOrderAmount;

		private Button btnPay;

		private TextView txtSaving;
	}

	public interface OnPayOrderListener {
		void onPayOrder(int position);
	}
}
