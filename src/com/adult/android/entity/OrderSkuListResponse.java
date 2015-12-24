package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class OrderSkuListResponse extends StatusInfo {

	private OrderSkuListResponse2 data;

	public OrderSkuListResponse2 getData() {
		return data;
	}

	public void setData(OrderSkuListResponse2 data) {
		this.data = data;
	}
}
