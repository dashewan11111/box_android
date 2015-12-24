package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

/**
 * 
 * @author LIC
 *
 */
public class NewOrderDetailResponse extends StatusInfo {

	private NewOrderDetailResponse2 data;

	public NewOrderDetailResponse2 getData() {
		return data;
	}

	public void setData(NewOrderDetailResponse2 data) {
		this.data = data;
	}
}
