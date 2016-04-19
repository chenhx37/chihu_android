package com.heinzchen.chihu.customer.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heinzchen.chihu.R;
import com.heinzchen.chihu.account.ProfileManager;
import com.heinzchen.chihu.customer.cart.DishAndNumberPair;
import com.heinzchen.chihu.customer.cart.ShoppingCart;

import java.util.List;

import protocol.Chihu;

/**
 * Created by chen on 2016/3/19.
 */
public class MakeOrderActivity extends Activity implements View.OnClickListener{
    public static final String TAG = MakeOrderActivity.class.getSimpleName();
    private EditText mReceiverName;
    private EditText mPhoneNumber;
    private EditText mAddress;
    private ViewGroup mDishContainer;
    private TextView mTotalPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

        mReceiverName = (EditText) findViewById(R.id.order_receiver_name);
        mPhoneNumber = (EditText) findViewById(R.id.order_phone_number);
        mAddress = (EditText) findViewById(R.id.order_address);
        mDishContainer = (ViewGroup) findViewById(R.id.order_dish_container);
        mTotalPrice = (TextView) findViewById(R.id.order_total_price);

        ((TextView) findViewById(R.id.order_canteen_name)).setText(ShoppingCart.getInstance().getCanteenName());
        List<DishAndNumberPair> list = ShoppingCart.getInstance().getDishes();
        float totalPrice = 0;
        for (DishAndNumberPair pair : list) {
            String name = pair.meal.getName();
            int number = pair.number;
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            tv.setLayoutParams(params);
            tv.setText(name + "*" + number);
            float price = Float.parseFloat(pair.meal.getPrice());
            totalPrice += (price*number);
            mDishContainer.addView(tv);
        }
        mTotalPrice.setText(String.valueOf(totalPrice));

        initData();
    }

    private void initData() {
        Chihu.Profile profile = ProfileManager.getInstance().getCachedProfile();
        if (null != profile) {
            setData(profile);
        }
    }

    private void setData(final Chihu.Profile profile) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String receiver = profile.getReceiver();
                String address = profile.getAddress();
                String phone = profile.getPhone();

                mReceiverName.setText(receiver);
                mAddress.setText(address);
                mPhoneNumber.setText(phone);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_btn_ok: {

                break;
            }
            default:
                break;
        }
    }

    public static void jumpToMe(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MakeOrderActivity.class);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

}
