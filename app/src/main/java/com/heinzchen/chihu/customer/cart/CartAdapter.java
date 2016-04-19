package com.heinzchen.chihu.customer.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heinzchen.chihu.R;

import java.util.List;

/**
 * Created by chen on 2016/3/18.
 */
public class CartAdapter extends BaseAdapter {

    private List<DishAndNumberPair> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    public CartAdapter(Context context,List<DishAndNumberPair> data) {
        mContext = context;
        mData = data;
        notifyDataSetChanged();

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartViewHolder holder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_cart, null);

            holder = new CartViewHolder();
            holder.dishName = (TextView) convertView.findViewById(R.id.cart_name);
            holder.dishPrice = (TextView) convertView.findViewById(R.id.cart_price);
            holder.addBtn = (TextView) convertView.findViewById(R.id.btn_add);
            holder.subBtn = (TextView) convertView.findViewById(R.id.btn_sub);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            convertView.setTag(holder);
        } else {
            holder = (CartViewHolder) convertView.getTag();
        }

        DishAndNumberPair data = mData.get(position);
        holder.dishName.setText(data.meal.getName());
        holder.dishPrice.setText(data.meal.getPrice());
        holder.number.setText(Integer.toString(data.number));

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // TODO: 2016/3/18
            }
        });
        holder.subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/3/18
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

}
