package com.heinzchen.chihu.dishes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.heinzchen.chihu.CApplication;
import com.heinzchen.chihu.R;
import com.heinzchen.chihu.main.MainProcessor;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by chen on 2016/3/16.
 */
public class DishesActivity extends Activity {
    public static final String TAG = DishesActivity.class.getSimpleName();
    public static String canteenId = "CanteenId";

    private int mCanteenId;
    private ListView mListView;
    private ListAdapter mAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        MLog.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null == bundle || !bundle.containsKey(canteenId)) {
            finish();
            return;
        }
        mCanteenId = bundle.getInt(canteenId);

        setContentView(R.layout.activity_meals);
        mListView = (ListView) findViewById(R.id.dishes_lv);
        EventBus.getDefault().register(this);

        MainProcessor.getInstance().viewMeals(mCanteenId);
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
                // TODO: 2016/3/16 将dishes放到listview中
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
}
