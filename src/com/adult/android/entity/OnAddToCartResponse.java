package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class OnAddToCartResponse extends StatusInfo {

	private OnAddToCartResponse2 data;

	public OnAddToCartResponse2 getData() {
		return data;
	}

	public void setData(OnAddToCartResponse2 data) {
		this.data = data;
	}
}
