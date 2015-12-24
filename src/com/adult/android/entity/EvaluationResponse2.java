package com.adult.android.entity;

import java.util.List;

public class EvaluationResponse2 extends BaseEntity {

	private List<Comment2> commentList;

	public List<Comment2> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment2> commentList) {
		this.commentList = commentList;
	}
}
