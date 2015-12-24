package com.adult.android.entity;

import java.util.Date;

public class Shop extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6034646116752685852L;

	private String shopId;

	private int shopNumber;
	
    private String shopNameCn;

    private String shopNameEn;

    private String shopTel;

    private String shopEmail;

    private String shopDesc;

    private String shopAddress;

    private String shopIcon;

    private Date shopCtime;

    private String cityId;

    private String shopBrandId;

    private String shopCategoryId;

    /**
	 * @return the shopNumber
	 */
	public int getShopNumber() {
		return shopNumber;
	}

	/**
	 * @param shopNumber the shopNumber to set
	 */
	public void setShopNumber(int shopNumber) {
		this.shopNumber = shopNumber;
	}

	public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getShopNameCn() {
        return shopNameCn;
    }

    public void setShopNameCn(String shopNameCn) {
        this.shopNameCn = shopNameCn == null ? null : shopNameCn.trim();
    }

    public String getShopNameEn() {
        return shopNameEn;
    }

    public void setShopNameEn(String shopNameEn) {
        this.shopNameEn = shopNameEn == null ? null : shopNameEn.trim();
    }

    public String getShopTel() {
        return shopTel;
    }

    public void setShopTel(String shopTel) {
        this.shopTel = shopTel == null ? null : shopTel.trim();
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail == null ? null : shopEmail.trim();
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc == null ? null : shopDesc.trim();
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress == null ? null : shopAddress.trim();
    }

    public String getShopIcon() {
        return shopIcon;
    }

    public void setShopIcon(String shopIcon) {
        this.shopIcon = shopIcon == null ? null : shopIcon.trim();
    }

    public Date getShopCtime() {
        return shopCtime;
    }

    public void setShopCtime(Date shopCtime) {
        this.shopCtime = shopCtime;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId == null ? null : cityId.trim();
    }

    public String getShopBrandId() {
        return shopBrandId;
    }

    public void setShopBrandId(String shopBrandId) {
        this.shopBrandId = shopBrandId == null ? null : shopBrandId.trim();
    }

    public String getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(String shopCategoryId) {
        this.shopCategoryId = shopCategoryId == null ? null : shopCategoryId.trim();
    }
}