package com.adult.android.entity;

import java.util.Date;

public class Shopper extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2730395471927325472L;

	private String shopperId;

	private String shopperFirstName;

	private String shopperLastName;

	private String shopperTel;

	private String shopperEmail;

	private String shopperPhoto;

	private String shopperBankNumber;

	private String shopId;

	private String shopperCommisionId;

	private Date shopperCtime;

	public String getShopperId() {
		return shopperId;
	}

	public void setShopperId(String shopperId) {
		this.shopperId = shopperId == null ? null : shopperId.trim();
	}

	public String getShopperFirstName() {
		return shopperFirstName;
	}

	public void setShopperFirstName(String shopperFirstName) {
		this.shopperFirstName = shopperFirstName == null ? null : shopperFirstName.trim();
	}

	public String getShopperLastName() {
		return shopperLastName;
	}

	public void setShopperLastName(String shopperLastName) {
		this.shopperLastName = shopperLastName == null ? null : shopperLastName.trim();
	}

	public String getShopperTel() {
		return shopperTel;
	}

	public void setShopperTel(String shopperTel) {
		this.shopperTel = shopperTel == null ? null : shopperTel.trim();
	}

	public String getShopperEmail() {
		return shopperEmail;
	}

	public void setShopperEmail(String shopperEmail) {
		this.shopperEmail = shopperEmail == null ? null : shopperEmail.trim();
	}

	public String getShopperPhoto() {
		return shopperPhoto;
	}

	public void setShopperPhoto(String shopperPhoto) {
		this.shopperPhoto = shopperPhoto == null ? null : shopperPhoto.trim();
	}

	public String getShopperBankNumber() {
		return shopperBankNumber;
	}

	public void setShopperBankNumber(String shopperBankNumber) {
		this.shopperBankNumber = shopperBankNumber == null ? null : shopperBankNumber.trim();
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId == null ? null : shopId.trim();
	}

	public String getShopperCommisionId() {
		return shopperCommisionId;
	}

	public void setShopperCommisionId(String shopperCommisionId) {
		this.shopperCommisionId = shopperCommisionId == null ? null : shopperCommisionId.trim();
	}

	public Date getShopperCtime() {
		return shopperCtime;
	}

	public void setShopperCtime(Date shopperCtime) {
		this.shopperCtime = shopperCtime;
	}
}