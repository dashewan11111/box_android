package com.adult.android.entity;


/**
 * 
 * @author LIC
 *
 */
public class NewOrderDetailResponse2 extends BaseEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6276784714366577643L;

	private AddressDto address;

	private OrderDtoForList orderDetail;

	public AddressDto getAddress() {
		return address;
	}

	public void setAddress(AddressDto address) {
		this.address = address;
	}

	public OrderDtoForList getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDtoForList orderDetail) {
		this.orderDetail = orderDetail;
	}

}
