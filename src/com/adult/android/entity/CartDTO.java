/**
 * 
 */
package com.adult.android.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengqiang.shi 2015年11月22日 上午12:47:35
 */
public class CartDTO implements Serializable {

	private static final long serialVersionUID = 4507716023509111305L;

	// 商品信息集合
	private List<CartSkuDTO> cartSkuDTOList = new ArrayList<CartSkuDTO>();

	// 店铺信息
	private Shop shop;

	// 店铺促销信息集合
	private List<ShopPromotion> shopPromotionList = new ArrayList<ShopPromotion>();

	// 支付金额
	private BigDecimal payAmount;

	// 商品总金额
	private BigDecimal itemAmount;

	// 节省金额
	private BigDecimal saveAmount;

	// 总重量
	private BigDecimal totalWeight;

	// 总数量
	private int totalNumber;

	// 总运费
	private BigDecimal totalFreight;

	/**
	 * 
	 */
	public CartDTO() {
		super();
	}

	/**
	 * @return the totalFreight
	 */
	public BigDecimal getTotalFreight() {
		return totalFreight;
	}

	/**
	 * @param totalFreight
	 *            the totalFreight to set
	 */
	public void setTotalFreight(BigDecimal totalFreight) {
		this.totalFreight = totalFreight;
	}

	public List<CartSkuDTO> getCartSkuDTOList() {
		return cartSkuDTOList;
	}

	public void setCartSkuDTOList(List<CartSkuDTO> cartSkuDTOList) {
		this.cartSkuDTOList = cartSkuDTOList;
	}

	/**
	 * @return the shop
	 */
	public Shop getShop() {
		return shop;
	}

	/**
	 * @param shop
	 *            the shop to set
	 */
	public void setShop(Shop shop) {
		this.shop = shop;
	}

	/**
	 * @return the shopPromotionList
	 */
	public List<ShopPromotion> getShopPromotionList() {
		return shopPromotionList;
	}

	/**
	 * @param shopPromotionList
	 *            the shopPromotionList to set
	 */
	public void setShopPromotionList(List<ShopPromotion> shopPromotionList) {
		this.shopPromotionList = shopPromotionList;
	}

	/**
	 * @return the saveAmount
	 */
	public BigDecimal getSaveAmount() {
		return saveAmount;
	}

	/**
	 * @param saveAmount
	 *            the saveAmount to set
	 */
	public void setSaveAmount(BigDecimal saveAmount) {
		this.saveAmount = saveAmount;
	}

	/**
	 * @return the totalWeight
	 */
	public BigDecimal getTotalWeight() {
		return totalWeight;
	}

	/**
	 * @param totalWeight
	 *            the totalWeight to set
	 */
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}

	/**
	 * @return the totalNumber
	 */
	public int getTotalNumber() {
		return totalNumber;
	}

	/**
	 * @param totalNumber
	 *            the totalNumber to set
	 */
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	/**
	 * @return the payAmount
	 */
	public BigDecimal getPayAmount() {
		return payAmount;
	}

	/**
	 * @param payAmount
	 *            the payAmount to set
	 */
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	/**
	 * @return the itemAmount
	 */
	public BigDecimal getItemAmount() {
		return itemAmount;
	}

	/**
	 * @param itemAmount
	 *            the itemAmount to set
	 */
	public void setItemAmount(BigDecimal itemAmount) {
		this.itemAmount = itemAmount;
	}

}
