package com.adult.android.entity;

import java.io.Serializable;

public class HomeFinalItem extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2577355760807531621L;

	/**
	 * 目标地址信息
	 */
	private String imgUrl;

	/**
	 * 目标类型 1，url，2，商品Id；3，类目Id，4，关键字，5，专题Id，6，圈子ID
	 */
	private Integer type;

	private String jumpId;

	private String name;

	private String sort;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getJumpId() {
		return jumpId;
	}

	public void setJumpId(String jumpId) {
		this.jumpId = jumpId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
