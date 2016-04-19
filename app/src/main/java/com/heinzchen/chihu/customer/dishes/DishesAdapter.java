package com.heinzchen.chihu.customer.dishes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heinzchen.chihu.R;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import protocol.Chihu;

/**
 * Created by chen on 2016/3/17.
 */
public class DishesAdapter extends BaseAdapter {
    public static final String TAG = DishesAdapter.class.getSimpleName();
    public static final int BTN_ADD_POSITION = 1;
    private List<Chihu.Meal> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    public DishesAdapter(Context context,List<Chihu.Meal> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Chihu.Meal> data) {
        mData = data;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DishViewHolder holder = null;
        if (null == convertView) {
            holder = new DishViewHolder();
            convertView = mInflater.inflate(R.layout.item_dish, null);
            holder.dishName = (TextView) convertView.findViewById(R.id.dish_name);
            holder.dishPrice = (TextView) convertView.findViewById(R.id.dish_price);
            holder.btnAdd = convertView.findViewById(R.id.add_dish);
            convertView.setTag(holder);

        } else {
            holder = (DishViewHolder) convertView.getTag();
        }

        Chihu.Meal dish = (Chihu.Meal) getItem(position);

        holder.dishName.setText(dish.getName());
        holder.dishPrice.setText(dish.getPrice());
        holder.btnAdd.setTag(position);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                MLog.i(TAG, ((Chihu.Meal) getItem(position)).getName());
                AddDishMessage msg = new AddDishMessage();
                msg.meal = (Chihu.Meal) getItem(position);
                EventBus.getDefault().post(msg);
            }

        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

}
