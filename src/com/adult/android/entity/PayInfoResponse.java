package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class PayInfoResponse extends StatusInfo {

	private PayInfoResponse2 data;

	public PayInfoResponse2 getData() {
		return data;
	}

	public void setData(PayInfoResponse2 data) {
		this.data = data;
	}
}
