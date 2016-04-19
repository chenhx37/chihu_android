package com.heinzchen.chihu.provider.canteen;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.heinzchen.chihu.R;
import com.heinzchen.chihu.customer.main.CanteenLayout;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import protocol.Chihu;

/**
 * Created by chen on 2016/4/16.
 */
public class CanteenFragment extends Fragment implements View.OnClickListener{
    public static final String TAG = CanteenFragment.class.getSimpleName();

    public static final String BUNDLE_CANTEEN = "canteen";

    private LinearLayout mContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        CanteenPresenter.getInstance().getCanteens();
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_canteens, container, false);
        view.findViewById(R.id.add_canteen).setOnClickListener(this);
        mContainer = (LinearLayout) view.findViewById(R.id.canteens_container);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_canteen: {
                AddCanteenActivity.jumpToMe(getActivity());
                break;
            }
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(final GetCanteenResult result) {
        MLog.i(TAG, String.valueOf(result.canteens.size()));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mContainer.removeAllViews();
                for (final Chihu.Canteen canteen : result.canteens) {
                    CanteenLayout layout = new CanteenLayout(getActivity());
                    layout.setCanteen(canteen);
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putByteArray(BUNDLE_CANTEEN, canteen.toByteArray());
                            CanteenDetailActivity.jumpToMe(getActivity(), bundle);
                        }
                    });
                    mContainer.addView(layout);
                }
            }
        });

    }

}
