package com.heinzchen.chihu.customer.dishes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.heinzchen.chihu.CApplication;
import com.heinzchen.chihu.R;
import com.heinzchen.chihu.customer.cart.CartAdapter;
import com.heinzchen.chihu.customer.cart.DishAndNumberPair;
import com.heinzchen.chihu.customer.cart.ShoppingCart;
import com.heinzchen.chihu.customer.main.MainProcessor;
import com.heinzchen.chihu.customer.order.MakeOrderActivity;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import protocol.Chihu;

/**
 * Created by chen on 2016/3/16.
 */
public class DishesActivity extends Activity implements View.OnClickListener{
    public static final String TAG = DishesActivity.class.getSimpleName();
    public static String CANTEEN_ID = "CanteenId";
    public static String CANTEEN_NAME = "CanteenName";
    public static String DISH_AND_NUMBER_PAIR_LIST = "DishAndNumberPairList";

    private int mCanteenId;
    private String mCanteenName;
    private ListView mListView;
    private ListAdapter mAdapter;

    private View mCartDetail;
    private ListView mCartListView;
    private ListAdapter mCartAdapter;

    private TextView mCartWording;

    private List<DishAndNumberPair> mSelectedList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MLog.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null == bundle || !bundle.containsKey(CANTEEN_ID)) {
            finish();
            return;
        }
        mCanteenId = bundle.getInt(CANTEEN_ID);
        mCanteenName = bundle.getString(CANTEEN_NAME);

        setContentView(R.layout.activity_meals);
        mListView = (ListView) findViewById(R.id.dishes_lv);
        EventBus.getDefault().register(this);
        findViewById(R.id.cart_img).setOnClickListener(this);
        mCartDetail = findViewById(R.id.cart_detail_ll);
        ((TextView) findViewById(R.id.dishes_title)).setText(mCanteenName);

        findViewById(R.id.btn_make_order).setOnClickListener(this);

        mCartWording = (TextView) findViewById(R.id.cart_wording);

        MainProcessor.getInstance().viewMeals(mCanteenId);
        mCartListView = (ListView) findViewById(R.id.cart_lv);
        mCartAdapter = new CartAdapter(this, mSelectedList);
        ((CartAdapter) mCartAdapter).notifyDataSetChanged();
        mCartListView.setAdapter(mCartAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cart_img: {
                if (mCartDetail.getVisibility() == LinearLayout.VISIBLE) {
                    mCartDetail.setVisibility(LinearLayout.GONE);
                } else {
                    mCartDetail.setVisibility(LinearLayout.VISIBLE);
                }
                break;
            }
            case R.id.btn_make_order: {
                ShoppingCart.getInstance().setParams(mCanteenId, mCanteenName, mSelectedList);
                MakeOrderActivity.jumpToMe(this, null);
                break;
            }

            default:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public static void jumpToMe(Context context, Bundle bundle) {
        MLog.i(TAG, "jumpToMe");
        Intent intent = new Intent(context, DishesActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void addMeal(Chihu.Meal meal) {
        MLog.i(TAG, "addMeal");
        for (DishAndNumberPair pair : mSelectedList) {
            if (pair.meal.equals(meal)) {
                ++pair.number;
                ((CartAdapter) mCartAdapter).notifyDataSetChanged();
                MLog.i(TAG, "contains,name|number=" + pair.meal.getName() + "|" + pair.number);
                return;
            }
        }

        //not exists
        DishAndNumberPair pair = new DishAndNumberPair();
        pair.meal = meal;
        pair.number = 1;
        mSelectedList.add(pair);
        ((CartAdapter) mCartAdapter).notifyDataSetChanged();

        updateUI();
    }

    private void updateUI() {
        if (mSelectedList.isEmpty()) {
            mCartWording.setText(R.string.cart_is_empty);
        } else {
            int size = mSelectedList.size();
            mCartWording.setText("已选择"+size+"种菜品");
        }
    }

    @Subscribe
    public void onEvent(final ViewMealsMessage msg) {
        switch (msg.state) {
            case EventBusMessage.FAIL: {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CApplication.GLOBAL_CONTEXT, "获取美食列表失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case EventBusMessage.SUCCEED: {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CApplication.GLOBAL_CONTEXT, "获取美食列表成功", Toast.LENGTH_SHORT).show();
                        mAdapter = new DishesAdapter(DishesActivity.this, msg.meals);
                        mListView.setAdapter(mAdapter);

                    }
                });
                break;

            }
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(AddDishMessage msg) {
        MLog.i(TAG, "onEvent(AddDishMessage msg)");
        addMeal(msg.meal);
    }
}
