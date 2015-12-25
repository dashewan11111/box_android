package com.adult.android.presenter.fragment.main.tab.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.CartDTO;
import com.adult.android.entity.ProductRule;
import com.adult.android.entity.CartSkuDTO;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.MyEditText2;
import com.adult.android.view.MyEditText2.OnEditNumberListener;
import com.lidroid.xutils.BitmapUtils;

public class CartListAdapterForBox extends BaseAdapter implements
		OnEditNumberListener {

	private Context context;

	private List<CartDTO> items;

	private BitmapUtils bitmapUtils;

	private OnDataChangeListener onDataChangeListener;

	private int currentPosition = 0;

	public CartListAdapterForBox(Context context, List<CartDTO> items,
			OnDataChangeListener onDateChangeListener) {
		this.context = context;
		this.items = items;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.img_default_114);
		bitmapUtils.configDefaultLoadingImage(R.drawable.img_default_114);
		this.onDataChangeListener = onDateChangeListener;
	}

	@Override
	public int getCount() {
		return items == null ? 0 : items.size();
	}

	@Override
	public CartDTO getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_cart_list_for_box, null);
			holder.llytSkuContainer = (LinearLayout) convertView
					.findViewById(R.id.item_cart_box_list_sku_container);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		addSkuList(position, holder.llytSkuContainer);

		return convertView;
	}

	private void addSkuList(int position, LinearLayout llytSkuContainer) {
		if (0 < llytSkuContainer.getChildCount()) {
			llytSkuContainer.removeAllViewsInLayout();
		}
		List<CartSkuDTO> skuList = getItem(position).getCartSkuDTOList();
		for (int i = 0; i < skuList.size(); i++) {
			View skuView = LayoutInflater.from(context).inflate(
					R.layout.item_cart_sku_for_box, null);
			ImageView imgCheck = (ImageView) skuView
					.findViewById(R.id.item_cart_box_check);
			ImageView imgProduct = (ImageView) skuView
					.findViewById(R.id.item_cart_box_img_product);
			TextView txtProductName = (TextView) skuView
					.findViewById(R.id.item_cart_box_product_name);
			TextView txtProductPrice = (TextView) skuView
					.findViewById(R.id.item_cart_box_product_price);
			TextView txtProductFormart = (TextView) skuView
					.findViewById(R.id.item_cart_box_product_formart);
			MyEditText2 editCount = (MyEditText2) skuView
					.findViewById(R.id.item_cart_box_edit_count);
			ImageView imgDelete = (ImageView) skuView
					.findViewById(R.id.item_cart_box_delete);
			LinearLayout llytDisable = (LinearLayout) skuView
					.findViewById(R.id.item_cart_box_llyt_disable);
			CartSkuDTO sku = skuList.get(i);

			txtProductName.setText(sku.getMerchandise().getMerchandiseNameCn());
			txtProductPrice.setText(context.getResources().getString(
					R.string.euro)
					+ sku.getSubTotal());
			// txtProductFormart.setText(sku.getSkuName());
			imgCheck.setOnClickListener(new ItemCartClickListner(position, i));
			imgDelete.setOnClickListener(new ItemCartClickListner(position, i));

			bitmapUtils.display(imgProduct, ServiceUrlConstants.getImageHost()
					+ sku.getMerchandise().getMerchandiseIcon());
			editCount.setOnAddReduceClickListener(this, position, i);
			editCount.setTag(sku);
			editCount.setNums(sku.getQty());
			editCount.setMaxNumber(1000);
			// addImages(holder.llytImageContainer, topic.getTopicImageList());
			// if (0 > sku.getIsSoldOut()) {
			// llytDisable.setVisibility(View.VISIBLE);
			// } else {
			// llytDisable.setVisibility(View.GONE);
			// }
			llytDisable.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
				}
			});
			llytSkuContainer.addView(skuView);
			if (i < skuList.size() - 1) {
				View line = LayoutInflater.from(context).inflate(
						R.layout.line_gray_full, null);
				llytSkuContainer.addView(line);
			}
		}
	}

	class ItemCartClickListner implements OnClickListener {

		private int productPosition, skuPosition;

		public ItemCartClickListner(int productPosition, int skuPosition) {
			this.productPosition = productPosition;
			this.skuPosition = skuPosition;
		}

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.item_cart_box_check:
				// if
				// (getItem(productPosition).getCartSkuDTOList().get(skuPosition).isChecked())
				// {
				// onDataChangeListener.onChecedChange(productPosition,
				// skuPosition, false);
				// } else {
				// onDataChangeListener.onChecedChange(productPosition,
				// skuPosition, true);
				// }
				break;
			case R.id.item_cart_box_delete:
				onDataChangeListener.onSkuDelete(productPosition, skuPosition);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onAddClick(int position1, int position2, View v,
			int previousNum, int currentNum, Object tag) {
		onDataChangeListener.onSkuCountChange(position1, position2,
				((CartSkuDTO) v.getTag()).getMerchandise()
						.getMerchandiseNumber(), currentNum);
	}

	@Override
	public void onReduceClick(int position1, int position2, View v,
			int previousNum, int currentNum, Object tag) {
		onDataChangeListener.onSkuCountChange(position1, position2,
				((CartSkuDTO) v.getTag()).getMerchandise()
						.getMerchandiseNumber(), currentNum);
	}

	@Override
	public void onMaxQty(int position1, int position2, View v, int max) {
		ToastUtil.showToastLong(context, "已超出最大库存数");
	}

	@Override
	public void onInputNumberDone(int position1, int position2, int number,
			int max, Object tag) {
		// ToastUtil.showToastLong(context, position2 + "：onInputNumberDone");
	}

	public interface OnDataChangeListener {

		void onSkuCountChange(int productPosition, int skuPositions,
				String skuId, int count);

		void onChecedChange(int productPosition, int skuPosition,
				boolean isChecked);

		void onSkuDelete(int productPosition, int skuPosition);

		void onPromotionClick(ProductRule rule);
	}

	private class ViewHolder {

		private TextView txtType, txtDesc;

		private LinearLayout llytSkuContainer;

	}

}
