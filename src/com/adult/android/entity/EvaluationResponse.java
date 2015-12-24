package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class EvaluationResponse extends StatusInfo {

	private EvaluationResponse2 data;

	public EvaluationResponse2 getData() {
		return data;
	}

	public void setData(EvaluationResponse2 data) {
		this.data = data;
	}
}
