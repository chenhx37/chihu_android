package com.heinzchen.chihu.customer.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinzchen.chihu.R;

/**
 * Created by chen on 2016/3/13.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private MainFragment mMainFragment = null;
    private Fragment mOrderFragment = null;
    private Fragment mProfileFragment = null;
    private FragmentManager mManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.menu_main_page).setOnClickListener(this);
        findViewById(R.id.menu_order_page).setOnClickListener(this);
        findViewById(R.id.menu_profile_page).setOnClickListener(this);

        if (null == mMainFragment) {
            mMainFragment = new MainFragment();
        }

        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.replace(R.id.fragment_placeholder, mMainFragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_main_page: {
                if (null == mMainFragment) {
                    mMainFragment = new MainFragment();
                }
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.replace(R.id.fragment_placeholder, mMainFragment);
                transaction.commit();
                break;
            }
            case R.id.menu_order_page: {
                if (null == mOrderFragment) {
                    mOrderFragment = new OrderFragment();
                }
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.replace(R.id.fragment_placeholder, mOrderFragment);
                transaction.commit();
                break;
            }
            case R.id.menu_profile_page: {
                if (null == mProfileFragment) {
                    mProfileFragment = new ProfileFragment();
                }
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.replace(R.id.fragment_placeholder, mProfileFragment);
                transaction.commit();
                break;
            }
            default:
                break;
        }
    }

    public static void jumpToMe(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
