package com.drugvilla.in.model.cart;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private CartData cartData;

    public CartData getCartData() {
        return cartData;
    }

    public void setCartData(CartData cartData) {
        this.cartData = cartData;
    }
}
