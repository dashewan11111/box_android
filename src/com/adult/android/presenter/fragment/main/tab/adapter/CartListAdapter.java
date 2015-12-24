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
import com.adult.android.entity.ProductForCart;
import com.adult.android.entity.ProductRule;
import com.adult.android.entity.SkuForCart;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.MyEditText2;
import com.adult.android.view.MyEditText2.OnEditNumberListener;
import com.lidroid.xutils.BitmapUtils;

public class CartListAdapter extends BaseAdapter implements OnEditNumberListener {

	private Context context;

	private List<ProductForCart> items;

	private BitmapUtils bitmapUtils;

	private OnDataChangeListener onDataChangeListener;

	private int currentPosition = 0;

	public CartListAdapter(Context context, List<ProductForCart> items, OnDataChangeListener onDateChangeListener) {
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
	public ProductForCart getItem(int position) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_cart_list, null);
			holder.llytPromotionContainer = (LinearLayout) convertView.findViewById(R.id.item_cart_list_promotion);
			holder.txtType = (TextView) convertView.findViewById(R.id.item_cart_list_promotion_type);
			holder.txtDesc = (TextView) convertView.findViewById(R.id.item_cart_list_promotion_desc);

			holder.llytSkuContainer = (LinearLayout) convertView.findViewById(R.id.item_cart_list_sku_container);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ProductForCart procut = (ProductForCart) getItem(position);
		addSkuList(position, holder.llytSkuContainer);
		final ProductRule rule = procut.getProRule();
		if (null != rule) {
			if (null == rule.getType()) {
				holder.llytPromotionContainer.setVisibility(View.GONE);
			} else {
				holder.llytPromotionContainer.setVisibility(View.VISIBLE);
				if ("RULE_FULL_CUT".equals(rule.getType())) {
					holder.txtType.setText("满减");
				} else {
					holder.txtType.setText("直降");
				}
				holder.txtDesc.setText(rule.getRuleName());
			}

			holder.llytPromotionContainer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					onDataChangeListener.onPromotionClick(rule);
				}
			});
		} else {
			holder.llytPromotionContainer.setVisibility(View.GONE);
		}
		return convertView;
	}

	private void addSkuList(int position, LinearLayout llytSkuContainer) {
		if (0 < llytSkuContainer.getChildCount()) {
			llytSkuContainer.removeAllViewsInLayout();
		}
		List<SkuForCart> skuList = getItem(position).getSkuList();
		for (int i = 0; i < skuList.size(); i++) {
			View skuView = LayoutInflater.from(context).inflate(R.layout.item_cart_sku, null);
			ImageView imgCheck = (ImageView) skuView.findViewById(R.id.item_cart_check);
			ImageView imgProduct = (ImageView) skuView.findViewById(R.id.item_cart_img_product);
			TextView txtProductName = (TextView) skuView.findViewById(R.id.item_cart_product_name);
			TextView txtProductPrice = (TextView) skuView.findViewById(R.id.item_cart_product_price);
			TextView txtProductFormart = (TextView) skuView.findViewById(R.id.item_cart_product_formart);
			MyEditText2 editCount = (MyEditText2) skuView.findViewById(R.id.item_cart_edit_count);
			ImageView imgDelete = (ImageView) skuView.findViewById(R.id.item_cart_delete);
			LinearLayout llytDisable = (LinearLayout) skuView.findViewById(R.id.item_cart_llyt_disable);
			SkuForCart sku = skuList.get(i);
			if (sku.isChecked()) {
				imgCheck.setImageResource(R.drawable.cart_product_select_on);
			} else {
				imgCheck.setImageResource(R.drawable.cart_product_select_off);
			}
			txtProductName.setText(sku.getItemName());
			txtProductPrice.setText(context.getResources().getString(R.string.rmb) + sku.getPromotionPrice());
			txtProductFormart.setText(sku.getSkuName());
			imgCheck.setOnClickListener(new ItemCartClickListner(position, i));
			imgDelete.setOnClickListener(new ItemCartClickListner(position, i));

			bitmapUtils.display(imgProduct, ServiceUrlConstants.getImageHost() + sku.getImgUrl());
			editCount.setOnAddReduceClickListener(this, position, i);
			editCount.setTag(sku);
			editCount.setNums(sku.getQty());
			editCount.setMaxNumber(sku.getStockQty());
			// addImages(holder.llytImageContainer, topic.getTopicImageList());
			if (0 > sku.getIsSoldOut()) {
				llytDisable.setVisibility(View.VISIBLE);
			} else {
				llytDisable.setVisibility(View.GONE);
			}
			llytDisable.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
				}
			});
			llytSkuContainer.addView(skuView);
			if (i < skuList.size() - 1) {
				View line = LayoutInflater.from(context).inflate(R.layout.line_gray_full, null);
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
			case R.id.item_cart_check:
				if (getItem(productPosition).getSkuList().get(skuPosition).isChecked()) {
					onDataChangeListener.onChecedChange(productPosition, skuPosition, false);
				} else {
					onDataChangeListener.onChecedChange(productPosition, skuPosition, true);
				}
				break;
			case R.id.item_cart_delete:
				onDataChangeListener.onSkuDelete(productPosition, skuPosition);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onAddClick(int position1, int position2, View v, int previousNum, int currentNum, Object tag) {
		onDataChangeListener.onSkuCountChange(position1, position2, ((SkuForCart) v.getTag()).getSkuId(), currentNum);
	}

	@Override
	public void onReduceClick(int position1, int position2, View v, int previousNum, int currentNum, Object tag) {
		onDataChangeListener.onSkuCountChange(position1, position2, ((SkuForCart) v.getTag()).getSkuId(), currentNum);
	}

	@Override
	public void onMaxQty(int position1, int position2, View v, int max) {
		ToastUtil.showToastLong(context, "已超出最大库存数");
	}

	@Override
	public void onInputNumberDone(int position1, int position2, int number, int max, Object tag) {
		// ToastUtil.showToastLong(context, position2 + "：onInputNumberDone");
	}

	public interface OnDataChangeListener {

		void onSkuCountChange(int productPosition, int skuPositions, String skuId, int count);

		void onChecedChange(int productPosition, int skuPosition, boolean isChecked);

		void onSkuDelete(int productPosition, int skuPosition);

		void onPromotionClick(ProductRule rule);
	}

	private class ViewHolder {

		private TextView txtType, txtDesc;

		private LinearLayout llytPromotionContainer, llytSkuContainer;

	}

}
