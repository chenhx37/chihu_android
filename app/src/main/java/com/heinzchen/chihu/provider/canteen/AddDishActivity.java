package com.heinzchen.chihu.provider.canteen;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.heinzchen.chihu.CApplication;
import com.heinzchen.chihu.R;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by chen on 2016/4/18.
 */
public class AddDishActivity extends Activity implements View.OnClickListener {
    public static final String TAG = AddDishActivity.class.getSimpleName();
    private EditText mName;
    private EditText mPrice;

    private int mCanteenId;
    public static final String BUNDLE_CANTEEN_ID = "canteenId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            finish();
            return;
        }
        int id = bundle.getInt(BUNDLE_CANTEEN_ID, -1);
        if (-1 == id) {
            finish();
            return;
        }
        mCanteenId = id;

        setContentView(R.layout.activity_add_dish);
        mName = (EditText) findViewById(R.id.name);
        mPrice = (EditText) findViewById(R.id.price);
        findViewById(R.id.add_dish_ok).setOnClickListener(this);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_dish_ok: {
                String name = mName.getText().toString();
                String price = mPrice.getText().toString();
                CanteenPresenter.getInstance().addDish(name, price, mCanteenId);
                break;
            }
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(AddDishResult result) {
        MLog.i(TAG,String.valueOf(result.result));
        if (result.result == EventBusMessage.SUCCEED) {
            MLog.i(TAG, "SUCCEED");
            AddDishActivity.this.finish();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CApplication.GLOBAL_CONTEXT, "添加失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void jumpToMe(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddDishActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}

