package com.example.sagar.urcatters.Model;

public class DetailItem {
    private String orderName;
    private String orderOption;
    private int orderThumbnail;

    public DetailItem(String orderName, int orderThumbnail, String orderOption) {
        this.orderName = orderName;
        this.orderOption = orderOption;
        this.orderThumbnail = orderThumbnail;
    }

    public String getOrderName() {
        return this.orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getOrderThumbnail() {
        return this.orderThumbnail;
    }

    public void setOrderThumbnail(int orderThumbnail) {
        this.orderThumbnail = orderThumbnail;
    }

    public String getOrderOption() {
        return this.orderOption;
    }

    public void setOrderOption(String orderOption) {
        this.orderOption = orderOption;
    }
}
