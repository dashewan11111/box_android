package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class CommitOrderResponse extends StatusInfo {

	private CommitOrderResponse2 data;

	public CommitOrderResponse2 getData() {
		return data;
	}

	public void setData(CommitOrderResponse2 data) {
		this.data = data;
	}

}
