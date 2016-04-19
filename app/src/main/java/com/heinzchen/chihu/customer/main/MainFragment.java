package com.heinzchen.chihu.customer.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.heinzchen.chihu.CApplication;
import com.heinzchen.chihu.R;
import protocol.Chihu;

import com.heinzchen.chihu.customer.dishes.DishesActivity;
import com.heinzchen.chihu.utils.EventBusMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by chen on 2016/3/13.
 */
public class MainFragment extends Fragment {
    private ViewGroup mCanteensContainer;
    private View.OnClickListener mCanteenOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Chihu.Canteen canteen = (Chihu.Canteen) v.getTag();
            int canteenId = canteen.getCanteenId();
            String canteenName = canteen.getName();
            Bundle bundle = new Bundle();
            bundle.putInt(DishesActivity.CANTEEN_ID, canteenId);
            bundle.putString(DishesActivity.CANTEEN_NAME, canteenName);
            DishesActivity.jumpToMe(getActivity(), bundle);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainProcessor.getInstance().viewCanteens();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mCanteensContainer = (ViewGroup) view.findViewById(R.id.canteens_container);
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Subscribe
    public void onEvent(ViewCanteenMessage msg) {
        switch (msg.state) {
            case EventBusMessage.SUCCEED: {
                for (final Chihu.Canteen canteen : msg.canteens) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CanteenLayout layout = new CanteenLayout(getActivity());
                            layout.setCanteen(canteen);
                            layout.setOnClickListener(mCanteenOnClickListener);
                            mCanteensContainer.addView(layout);
                        }
                    });
                }

                break;
            }
            case EventBusMessage.FAIL: {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CApplication.GLOBAL_CONTEXT, "获取餐厅失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            default:
                break;
        }
    }
}
