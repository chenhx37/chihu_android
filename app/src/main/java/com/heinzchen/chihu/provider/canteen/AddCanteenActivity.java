package com.heinzchen.chihu.provider.canteen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.heinzchen.chihu.R;
import com.heinzchen.chihu.utils.EventBusMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by chen on 2016/4/18.
 */
public class AddCanteenActivity extends Activity implements View.OnClickListener {

    private EditText mName;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_add_canteen);
        findViewById(R.id.add_canteen_ok).setOnClickListener(this);

        mName = (EditText) findViewById(R.id.name);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public static void jumpToMe(Context context) {
        Intent intent = new Intent(context, AddCanteenActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_canteen_ok: {
                String name = mName.getText().toString();
                CanteenPresenter.getInstance().addCanteen(name);
                break;
            }
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(AddCanteenResult result) {
        if (result.result == EventBusMessage.SUCCEED) {
            finish();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddCanteenActivity.this, "注册餐厅失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
