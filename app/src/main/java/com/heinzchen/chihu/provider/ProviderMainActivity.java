package com.heinzchen.chihu.provider;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinzchen.chihu.R;
import com.heinzchen.chihu.customer.main.MainFragment;
import com.heinzchen.chihu.provider.canteen.CanteenFragment;
import com.heinzchen.chihu.provider.order.OrderFragment;
import com.heinzchen.chihu.provider.profile.ProfileFragment;

/**
 * Created by chen on 2016/4/16.
 */
public class ProviderMainActivity extends Activity implements View.OnClickListener {

    private OrderFragment mOrderFragment;
    private CanteenFragment mCanteenFragment;
    private ProfileFragment mProfileFragment;

    private FragmentManager mManager = getFragmentManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_main);

        findViewById(R.id.provider_menu_canteen).setOnClickListener(this);
        findViewById(R.id.provider_menu_order).setOnClickListener(this);
        findViewById(R.id.provider_menu_profile).setOnClickListener(this);

        if (null == mCanteenFragment) {
            mCanteenFragment = new CanteenFragment();
        }

        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.replace(R.id.fragment_placeholder, mCanteenFragment);
        transaction.commit();

    }

    public static void jumpToMe(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ProviderMainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.provider_menu_canteen: {
                if (null == mCanteenFragment) {
                    mCanteenFragment = new CanteenFragment();
                }
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.replace(R.id.fragment_placeholder, mCanteenFragment);
                transaction.commit();
                break;
            }
            case R.id.provider_menu_order: {
                if (null == mOrderFragment) {
                    mOrderFragment = new OrderFragment();
                }
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.replace(R.id.fragment_placeholder, mOrderFragment);
                transaction.commit();
                break;
            }
            case R.id.provider_menu_profile: {
//                if (null == mProfileFragment) {
//                    mProfileFragment = new ProfileFragment();
//                }
//                FragmentTransaction transaction = mManager.beginTransaction();
//                transaction.replace(R.id.fragment_placeholder, mProfileFragment);
//                transaction.commit();
                break;
            }
            default:
                break;
        }
    }
}
