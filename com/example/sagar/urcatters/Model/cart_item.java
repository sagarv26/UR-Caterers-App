package com.example.sagar.urcatters.Model;

public class cart_item {
    private String cartOrderName;
    private String cartShiftName;

    public cart_item(String cartOrderName, String cartShiftName) {
        this.cartOrderName = cartOrderName;
        this.cartShiftName = cartShiftName;
    }

    public String getCartOrderName() {
        return this.cartOrderName;
    }

    public void setCartOrderName(String cartOrderName) {
        this.cartOrderName = cartOrderName;
    }

    public String getCartShiftName() {
        return this.cartShiftName;
    }

    public void setCartShiftName(String cartShiftName) {
        this.cartShiftName = cartShiftName;
    }
}
