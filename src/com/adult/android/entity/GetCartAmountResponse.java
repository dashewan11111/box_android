package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class GetCartAmountResponse extends StatusInfo {

	private GetCartAmountResponse2 data;

	public GetCartAmountResponse2 getData() {
		return data;
	}

	public void setData(GetCartAmountResponse2 data) {
		this.data = data;
	}

}
