package com.dkkm.marketsim.model.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Data {
    private Map<String,Object> data;

    private ArrayList<Map<String,Object>> dataList;

    private Map<String,Object> dataListRow;

    public Map<String, Object> getDataListRow() {
        return dataListRow;
    }

    public void setDataListRow(Map<String, Object> dataListRow) {
        this.dataListRow = dataListRow;
    }

    public ArrayList<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
