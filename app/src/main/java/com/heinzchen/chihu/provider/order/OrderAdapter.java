package com.heinzchen.chihu.provider.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinzchen.chihu.R;

import java.util.List;

import protocol.Chihu;

/**
 * Created by chen on 2016/4/18.
 */
public class OrderAdapter extends BaseAdapter {

    private List<Chihu.Order> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    public OrderAdapter(Context context,List<Chihu.Order> data) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = data;
    }

    public void setData(List<Chihu.Order> data) {
        mData = data;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null == mData) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderViewHolder holder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_order_provider, parent, false);
            holder = new OrderViewHolder();
            holder.startTime = (TextView) convertView.findViewById(R.id.start_time);
            holder.endTime = (TextView) convertView.findViewById(R.id.end_time);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.phone = (TextView) convertView.findViewById(R.id.phone);
            holder.receiver = (TextView) convertView.findViewById(R.id.receiver);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.dishesViewGroup = (LinearLayout) convertView.findViewById(R.id.order_dish_container);

            convertView.setTag(holder);
        } else {
            holder = (OrderViewHolder) convertView.getTag();
        }

        Chihu.Order order = mData.get(position);

        holder.startTime.setText(order.getStartTime());
        holder.endTime.setText(order.getEndTime());
        holder.status.setText(order.getStatus());
        holder.phone.setText(order.getPhone());
        holder.receiver.setText(order.getReceiver());
        holder.address.setText(order.getAddress());
        holder.dishesViewGroup.removeAllViews();
        for (Chihu.OrderItem item : order.getItemListList()) {
            String str = item.getDishName() + " * " + item.getNumber();
            TextView tv = new TextView(mContext);
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setText(str);
            holder.dishesViewGroup.addView(tv);
        }

        return null;
    }

    @Override
    public int getCount() {
        if (null == mData) {
            return 0;

        }
        return mData.size();
    }
}
