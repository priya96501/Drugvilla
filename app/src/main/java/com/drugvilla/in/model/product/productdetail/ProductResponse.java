package com.drugvilla.in.model.product.productdetail;

import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductResponse extends BaseResponse {

    @SerializedName("product_data")
    @Expose
    private ProductDetail productData;

    public ProductDetail getProductData() {
        return productData;
    }

    public void setProductData(ProductDetail productData) {
        this.productData = productData;
    }
}
