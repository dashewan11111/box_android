package com.adult.android.entity;

import java.util.Date;

public class Merchandise extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4089533503346781168L;

	private String merchandiseId;

	private String merchandiseNumber;

	private String merchandiseNameCn;

	private String merchandiseNameEn;

	private String merchandiseDesc;

	private String merchandiseSpec;

	private Double merchandiseWeight;

	private Double merchandiseVolume;

	private Double merchandisePrice;

	private String merchandiseCurrency;

	private String merchandiseIcon;

	private String merchandiseBarcode;

	private String merchandiseQrcode;

	private Double merchandiseDomesticPrice;

	private String shopId;

	private Date ctime;

	public String getMerchandiseId() {
		return merchandiseId;
	}

	public void setMerchandiseId(String merchandiseId) {
		this.merchandiseId = merchandiseId == null ? null : merchandiseId.trim();
	}

	public String getMerchandiseNumber() {
		return merchandiseNumber;
	}

	public void setMerchandiseNumber(String merchandiseNumber) {
		this.merchandiseNumber = merchandiseNumber == null ? null : merchandiseNumber.trim();
	}

	public String getMerchandiseNameCn() {
		return merchandiseNameCn;
	}

	public void setMerchandiseNameCn(String merchandiseNameCn) {
		this.merchandiseNameCn = merchandiseNameCn == null ? null : merchandiseNameCn.trim();
	}

	public String getMerchandiseNameEn() {
		return merchandiseNameEn;
	}

	public void setMerchandiseNameEn(String merchandiseNameEn) {
		this.merchandiseNameEn = merchandiseNameEn == null ? null : merchandiseNameEn.trim();
	}

	public String getMerchandiseDesc() {
		return merchandiseDesc;
	}

	public void setMerchandiseDesc(String merchandiseDesc) {
		this.merchandiseDesc = merchandiseDesc == null ? null : merchandiseDesc.trim();
	}

	public String getMerchandiseSpec() {
		return merchandiseSpec;
	}

	public void setMerchandiseSpec(String merchandiseSpec) {
		this.merchandiseSpec = merchandiseSpec == null ? null : merchandiseSpec.trim();
	}

	public Double getMerchandiseWeight() {
		return merchandiseWeight;
	}

	public void setMerchandiseWeight(Double merchandiseWeight) {
		this.merchandiseWeight = merchandiseWeight;
	}

	public Double getMerchandiseVolume() {
		return merchandiseVolume;
	}

	public void setMerchandiseVolume(Double merchandiseVolume) {
		this.merchandiseVolume = merchandiseVolume;
	}

	public String getMerchandiseCurrency() {
		return merchandiseCurrency;
	}

	public void setMerchandiseCurrency(String merchandiseCurrency) {
		this.merchandiseCurrency = merchandiseCurrency == null ? null : merchandiseCurrency.trim();
	}

	public String getMerchandiseIcon() {
		return merchandiseIcon;
	}

	public void setMerchandiseIcon(String merchandiseIcon) {
		this.merchandiseIcon = merchandiseIcon == null ? null : merchandiseIcon.trim();
	}

	public String getMerchandiseBarcode() {
		return merchandiseBarcode;
	}

	public void setMerchandiseBarcode(String merchandiseBarcode) {
		this.merchandiseBarcode = merchandiseBarcode == null ? null : merchandiseBarcode.trim();
	}

	public String getMerchandiseQrcode() {
		return merchandiseQrcode;
	}

	public void setMerchandiseQrcode(String merchandiseQrcode) {
		this.merchandiseQrcode = merchandiseQrcode == null ? null : merchandiseQrcode.trim();
	}

	public Double getMerchandiseDomesticPrice() {
		return merchandiseDomesticPrice;
	}

	public void setMerchandiseDomesticPrice(Double merchandiseDomesticPrice) {
		this.merchandiseDomesticPrice = merchandiseDomesticPrice;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId == null ? null : shopId.trim();
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Double getMerchandisePrice() {
		return merchandisePrice;
	}

	public void setMerchandisePrice(Double merchandisePrice) {
		this.merchandisePrice = merchandisePrice;
	}
}