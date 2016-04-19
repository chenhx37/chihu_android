package com.heinzchen.chihu.customer.cart;

import java.util.List;

/**
 * Created by chen on 2016/3/20.
 */
public class ShoppingCart {
    private static ShoppingCart mInstance;

    private int mCanteenId;
    private String mCanteenName;
    private List<DishAndNumberPair> mDishes;

    private ShoppingCart() {}

    public static ShoppingCart getInstance() {
        if (null == mInstance) {
            synchronized (ShoppingCart.class) {
                if (null == mInstance) {
                    mInstance = new ShoppingCart();
                }
            }
        }
        return mInstance;
    }

    public int getCanteenId() {
        return mCanteenId;
    }

    public List<DishAndNumberPair> getDishes() {
        return mDishes;
    }

    public String getCanteenName() {
        return mCanteenName;
    }

    public void setParams(int canteenId, String canteenName, List<DishAndNumberPair> dishes) {
        mCanteenName = canteenName;
        mCanteenId = canteenId;
        mDishes = dishes;
    }



}
