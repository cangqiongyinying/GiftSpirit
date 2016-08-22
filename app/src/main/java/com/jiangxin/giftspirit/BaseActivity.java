package com.jiangxin.giftspirit;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by my on 2016/8/15.
 */
public class BaseActivity extends AppCompatActivity {
    private ActionBar mSupportActionBar;
    public void initBar(String title){
        mSupportActionBar=getSupportActionBar();
        View view=getLayoutInflater().inflate(R.layout.custom_action_bar_two,null);
        mSupportActionBar.setDisplayShowCustomEnabled(true);
        TextView titleTv= (TextView) view.findViewById(R.id.action_bar_title_two);
        titleTv.setText(title);
        ImageView backIcon= (ImageView) view.findViewById(R.id.back_icon_two);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSupportActionBar.setCustomView(view);
    }
}
