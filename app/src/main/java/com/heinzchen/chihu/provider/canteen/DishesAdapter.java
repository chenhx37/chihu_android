package com.heinzchen.chihu.provider.canteen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heinzchen.chihu.R;

import java.util.List;

import protocol.Chihu;

/**
 * Created by chen on 2016/4/18.
 */
public class DishesAdapter extends BaseAdapter {
    private List<Chihu.Meal> mData;
    private LayoutInflater mInflater;

    public DishesAdapter(Context context, List<Chihu.Meal> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        notifyDataSetChanged();
    }

    public void setData(List<Chihu.Meal> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DishViewHolder holder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_provider_dish, null);
            holder = new DishViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.dish_name);
            holder.price = (TextView) convertView.findViewById(R.id.dish_price);
            convertView.setTag(holder);
        } else {
            holder = (DishViewHolder) convertView.getTag();
        }
        Chihu.Meal meal = mData.get(position);
        holder.name.setText(meal.getName());
        holder.price.setText(meal.getPrice());

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
