package com.adult.android.model;

import com.adult.android.model.internet.bean.StatusInfo;

public class OnUnPayOrderListResponse extends StatusInfo {

	private OnUnPayOrderListResponse2 data;

	public OnUnPayOrderListResponse2 getData() {
		return data;
	}

	public void setData(OnUnPayOrderListResponse2 data) {
		this.data = data;
	}

}
