package com.drugvilla.in.model.product;

import com.drugvilla.in.model.product.productdetail.ProductData;
import com.drugvilla.in.service.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductListingResponse extends BaseResponse {
    @SerializedName("product_data")
    @Expose
    private List<ProductData> productDataList = null;

    public List<ProductData> getProductDataList() {
        return productDataList;
    }

    public void setProductDataList(List<ProductData> productDataList) {
        this.productDataList = productDataList;
    }
}
