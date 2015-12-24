package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class VerifyResponse extends StatusInfo {

	private VerifyResponse2 data;

	public VerifyResponse2 getData() {
		return data;
	}

	public void setData(VerifyResponse2 data) {
		this.data = data;
	}
}
