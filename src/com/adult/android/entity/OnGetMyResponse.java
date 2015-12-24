package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class OnGetMyResponse extends StatusInfo {

	private OnGetMyResponse2 data;

	public OnGetMyResponse2 getData() {
		return data;
	}

	public void setData(OnGetMyResponse2 data) {
		this.data = data;
	}
}
