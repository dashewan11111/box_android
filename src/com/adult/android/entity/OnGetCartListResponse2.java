package com.adult.android.entity;

public class OnGetCartListResponse2 extends BaseEntity {

	private CartDTO CartDTO;

	private UserForBox User;

	public CartDTO getCartDTO() {
		return CartDTO;
	}

	public void setCartDTO(CartDTO cartDTO) {
		CartDTO = cartDTO;
	}

	public UserForBox getUser() {
		return User;
	}

	public void setUser(UserForBox user) {
		User = user;
	}
}
