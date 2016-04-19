package com.heinzchen.chihu.provider.canteen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.protobuf.InvalidProtocolBufferException;
import com.heinzchen.chihu.R;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import protocol.Chihu;

/**
 * Created by chen on 2016/4/17.
 */
public class CanteenDetailActivity extends Activity implements View.OnClickListener {
    public static final String TAG = CanteenDetailActivity.class.getSimpleName();
    private Chihu.Canteen mCanteen;
    private ListView mListView;
    private DishesAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            finish();
            return;
        }
        Chihu.Canteen canteen = null;
        try {
            canteen = Chihu.Canteen.parseFrom(bundle.getByteArray(CanteenFragment.BUNDLE_CANTEEN));
        } catch (InvalidProtocolBufferException e) {
            MLog.e(TAG, e.getMessage());
        }
        if (null == canteen) {
            finish();
            return;
        }
        mCanteen = canteen;
        setContentView(R.layout.activity_canteen_detail);

        mListView = (ListView) findViewById(R.id.dishes_lv);
        ((TextView) findViewById(R.id.canteen_name)).setText(canteen.getName());
        findViewById(R.id.add_dish).setOnClickListener(this);

        MLog.i(TAG, "onCreate " + canteen.getName());
        EventBus.getDefault().register(this);

    }

    @Override
    public void onResume() {
        CanteenPresenter.getInstance().getDishes(mCanteen.getCanteenId());
        super.onResume();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public static void jumpToMe(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CanteenDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_dish: {
                Bundle bundle = new Bundle();
                bundle.putInt(AddDishActivity.BUNDLE_CANTEEN_ID, mCanteen.getCanteenId());
                AddDishActivity.jumpToMe(this, bundle);
                break;
            }
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(final GetDishesResult result) {
        MLog.i(TAG, "onEvent(GetDishesResult) " + result.dishes.size());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAdapter == null) {
                    mAdapter = new DishesAdapter(CanteenDetailActivity.this, result.dishes);
                    mListView.setAdapter(mAdapter);
                } else {
                    mAdapter.setData(result.dishes);
                    mAdapter.notifyDataSetChanged();

                }

            }
        });
    }
}
