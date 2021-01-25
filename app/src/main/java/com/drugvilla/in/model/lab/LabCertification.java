package com.drugvilla.in.model.lab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabCertification {

    @SerializedName("certification_name")
    @Expose
    private String certification_name;

    @SerializedName("certification_image")
    @Expose
    private String certification_image;

    public String getCertification_name() {
        return certification_name;
    }

    public void setCertification_name(String certification_name) {
        this.certification_name = certification_name;
    }

    public String getCertification_image() {
        return certification_image;
    }

    public void setCertification_image(String certification_image) {
        this.certification_image = certification_image;
    }
}
