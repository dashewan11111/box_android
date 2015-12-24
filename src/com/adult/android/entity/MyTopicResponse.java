package com.adult.android.entity;

import java.util.List;

import com.adult.android.model.internet.bean.StatusInfo;

public class MyTopicResponse extends StatusInfo {

	private List<TopicDTO> data;

	public List<TopicDTO> getData() {
		return data;
	}

	public void setData(List<TopicDTO> data) {
		this.data = data;
	}
}
