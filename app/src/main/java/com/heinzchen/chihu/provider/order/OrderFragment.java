package com.heinzchen.chihu.provider.order;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.heinzchen.chihu.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by chen on 2016/4/16.
 */
public class OrderFragment extends Fragment {

    private ListView mListView;
    private OrderAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        OrderPresenter.getInstance().getProviderOrders();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_order, container, false);

        mListView = (ListView) view.findViewById(R.id.order_listview);
        mAdapter = new OrderAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Subscribe
    public void onEvent(GetOrderResult result) {
        if (null == mAdapter) {
            mAdapter = new OrderAdapter(getActivity(), result.orders);
        } else {
            mAdapter.setData(result.orders);
            mAdapter.notifyDataSetChanged();
        }
    }
}
