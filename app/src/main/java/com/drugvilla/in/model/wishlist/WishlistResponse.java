package com.drugvilla.in.model.wishlist;

import com.drugvilla.in.model.product.productdetail.ProductData;
import com.drugvilla.in.model.reminder.ReminderData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WishlistResponse extends BaseResponse {
    @SerializedName("wishlist_data")
    @Expose
    private List<ProductData> wishlistDataList = null;

    public List<ProductData> getWishlistDataList() {
        return wishlistDataList;
    }

    public void setWishlistDataList(List<ProductData> wishlistDataList) {
        this.wishlistDataList = wishlistDataList;
    }
}
