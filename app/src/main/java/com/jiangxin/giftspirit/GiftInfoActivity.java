package com.jiangxin.giftspirit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiangxin.giftspirit.JSONresource.GiftInfoMessage;
import com.jiangxin.httplibrary.HttpCallback;
import com.jiangxin.httplibrary.HttpUtils;
import com.jiangxin.imagelibrary.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by my on 2016/8/16.
 */
public class GiftInfoActivity extends BaseActivity implements HttpCallback{
    private static final String mGiftUrlHead = "http://www.1688wan.com";
    private static final String mGiftJsonContextUrl = "/majax.action?method=getGiftInfo";
    private String giftId;
    private Map<String,Object> map=new HashMap<>();
    private TextView giftTimeTv;
    private TextView giftCountTv;
    private TextView giftInfoHasTv;
    private TextView giftInfoMethodTv;
    private Button getGiftBtn;
    private CircleImageView giftInfoImageView;
    private GiftInfoMessage giftInfoMessage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftinfo);
        getGiftId();
        loadHttps("id",giftId);
        initView();
    }

    private void initView() {
        giftTimeTv= (TextView) findViewById(R.id.giftinfo_time_tv);
        giftCountTv= (TextView) findViewById(R.id.giftinfo_count_tv);
        giftInfoHasTv= (TextView) findViewById(R.id.giftinfo_gifthas_tv);
        giftInfoMethodTv= (TextView) findViewById(R.id.giftinfo_info_getmethod_tv);
        getGiftBtn= (Button) findViewById(R.id.giftinfo_get_gift_btn);
        giftInfoImageView= (CircleImageView) findViewById(R.id.giftinfo_circle_image);
    }

    private void loadHttps(String key,String value){
        map.put(key,value);
        HttpUtils.loadPost(mGiftUrlHead+mGiftJsonContextUrl,map).callback(this,1);
        map.clear();
    }
    private void getGiftId(){
        Intent intent=this.getIntent();
        giftId=intent.getStringExtra("id");
    }

    @Override
    public void success(String result, int requestCode) {
        Gson gson=new Gson();
        giftInfoMessage=gson.fromJson(result,GiftInfoMessage.class);
        if (giftInfoMessage !=null){
        String overTime="有效期："+giftInfoMessage.getInfo().getOvertime();
        giftTimeTv.setText(overTime);
        String giftCount="礼包剩余："+giftInfoMessage.getInfo().getExchanges();
        giftCountTv.setText(giftCount);
        giftInfoHasTv.setText(giftInfoMessage.getInfo().getExplains());
        giftInfoMethodTv.setText(giftInfoMessage.getInfo().getDescs());
        if (giftInfoMessage.getInfo().getExchanges()==0){
            getGiftBtn.setText("马上淘号");
        }else {
            getGiftBtn.setText("立即领取");
        }

        initBar(giftInfoMessage.getInfo().getGname());
        ImageLoader.init(this).load(mGiftUrlHead +giftInfoMessage.getInfo().getIconurl(),giftInfoImageView);
        }
    }

    public void click(View view){
        Intent intent =new Intent(GiftInfoActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
