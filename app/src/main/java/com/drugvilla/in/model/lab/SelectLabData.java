package com.drugvilla.in.model.lab;

import com.drugvilla.in.model.lab.labs.LabData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SelectLabData {

    @SerializedName("test_id")
    @Expose
    private String testId;
    @SerializedName("test_name")
    @Expose
    private String testName;
    @SerializedName("lab_data")
    @Expose
    private List<LabData> labDataList = null;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public List<LabData> getLabDataList() {
        return labDataList;
    }

    public void setLabDataList(List<LabData> labDataList) {
        this.labDataList = labDataList;
    }
}
