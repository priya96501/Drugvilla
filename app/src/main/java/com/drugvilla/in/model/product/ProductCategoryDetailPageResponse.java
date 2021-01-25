package com.drugvilla.in.model.product;

import com.drugvilla.in.model.product.productdetail.ProductDetail;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCategoryDetailPageResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ProductCategoryDetailPageData productData;

    public ProductCategoryDetailPageData getProductData() {
        return productData;
    }

    public void setProductData(ProductCategoryDetailPageData productData) {
        this.productData = productData;
    }
}
