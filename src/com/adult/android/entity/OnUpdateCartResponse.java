package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class OnUpdateCartResponse extends StatusInfo {

	private OnUpdateCartResponse2 data;

	public OnUpdateCartResponse2 getData() {
		return data;
	}

	public void setData(OnUpdateCartResponse2 data) {
		this.data = data;
	}
}
