package com.heinzchen.chihu.customer.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinzchen.chihu.R;
import protocol.Chihu;

/**
 * Created by chen on 2016/3/13.
 */
public class CanteenLayout extends RelativeLayout {
    private TextView mName;
    private ImageView mImage;
    private Chihu.Canteen mCanteen;

    public CanteenLayout(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.canteen_layout, this, true);
        mName = (TextView) findViewById(R.id.canteen_name);
        mImage = (ImageView) findViewById(R.id.canteen_image);
    }

    public void setCanteen(Chihu.Canteen canteen) {
        mCanteen = canteen;
        mName.setText(mCanteen.getName());
//        mImage.setImageResource(R.drawable.juncheng);
        setTag(mCanteen);
    }

}
