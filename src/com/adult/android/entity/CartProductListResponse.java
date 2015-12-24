package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class CartProductListResponse extends StatusInfo {

	private CartProductListResponse2 data;

	public CartProductListResponse2 getData() {
		return data;
	}

	public void setData(CartProductListResponse2 data) {
		this.data = data;
	}

}
