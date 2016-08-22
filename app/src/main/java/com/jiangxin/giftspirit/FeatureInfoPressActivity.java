package com.jiangxin.giftspirit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiangxin.giftspirit.JSONresource.XYZKChildMessage;
import com.jiangxin.httplibrary.HttpCallback;
import com.jiangxin.httplibrary.HttpUtils;
import com.jiangxin.imagelibrary.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by my on 2016/8/18.
 */
public class FeatureInfoPressActivity extends BaseActivity implements HttpCallback{

    private static final String mGiftUrlHead = "http://www.1688wan.com";
    private static final String mGiftJsonContextUrl = "/majax.action?method=getWeekllChid";
    private int gameId;
    private Map<String,Object> map=new HashMap<>();
    private String iconurl;
    private String descs;
    private View listHeaderView;
    private ImageView mImageView;
    private TextView addTimeTextView;
    private TextView esdcTextView;
    private ListView mListView;
    private XYZKChildMessage xyzkChildMessage;
    private List<XYZKChildMessage.ListBean> list;
    private MyListAdapter myListAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_info_xyzx);
        getIntentMessage();
        initView();
        loadHttps("id",gameId);
    }

    private void initView() {
        mListView= (ListView) findViewById(R.id.xyzx_list);
        listHeaderView= LayoutInflater.from(FeatureInfoPressActivity.this).inflate(R.layout.xyzx_header_view,null);
        updateHeaderView();
        mListView.addHeaderView(listHeaderView);
    }

    private void updateHeaderView(){
        mImageView= (ImageView)listHeaderView.findViewById(R.id.xyzx_top_image);
        esdcTextView= (TextView) listHeaderView.findViewById(R.id.xyzx_top_esdc);
        ImageLoader.init(this).load(iconurl,mImageView);
        esdcTextView.setText(descs);
    }
    public void getIntentMessage() {
        Intent intent=this.getIntent();
        gameId=intent.getIntExtra("id",0);
        iconurl=intent.getStringExtra("iconurl");
        descs=intent.getStringExtra("descs");
        initBar(intent.getStringExtra("name"));
    }

    private void loadHttps(String key,int value){
        map.put(key,value);
        HttpUtils.loadPost(mGiftUrlHead+mGiftJsonContextUrl,map).callback(this,1);
        map.clear();
    }

    @Override
    public void success(String result, int requestCode) {
        Gson gson=new Gson();
        xyzkChildMessage=gson.fromJson(result,XYZKChildMessage.class);
        list=xyzkChildMessage.getList();
        myListAdapter=new MyListAdapter();
        mListView.setAdapter(myListAdapter);

    }

    class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list==null? 0:list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolderHot viewHolder;
            if (view==null){
                view= LayoutInflater.from(FeatureInfoPressActivity.this).inflate(R.layout.xyzk_list_simple_item,viewGroup,false);
                viewHolder = new ViewHolderHot(view);
            }else {
                viewHolder= (ViewHolderHot) view.getTag();
            }
            final XYZKChildMessage.ListBean game= list.get(i);
            viewHolder.gname_tv.setText(game.getAppname());
            String gType="类型："+game.getTypename();
            viewHolder.gtype_tv.setText(gType);
            String gSize="大小："+game.getAppsize();
            viewHolder.gsize_tv.setText(gSize);
            viewHolder.gesdc_tv.setText(game.getDescs());
            viewHolder.pic_iv.setTag(mGiftUrlHead + game.getIconurl());
            ImageLoader.init(FeatureInfoPressActivity.this).load(mGiftUrlHead + game.getIconurl(),viewHolder.pic_iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(FeatureInfoPressActivity.this,GameInfoActivity.class);
                    intent.putExtra("id",game.getAppid());
                    startActivity(intent);
                }
            });
            return view;
        }

        class ViewHolderHot {
            public ImageView pic_iv;
            public TextView gname_tv;
            public TextView gtype_tv;
            public TextView gsize_tv;
            public TextView gesdc_tv;


            public ViewHolderHot(View view) {
                view.setTag(this);
                pic_iv = (ImageView) view.findViewById(R.id.xyzk_simple_item_image);
                gname_tv = (TextView) view.findViewById(R.id.xyzk_simple_item_title);
                gtype_tv= (TextView) view.findViewById(R.id.xyzk_simple_item_gameType);
                gsize_tv= (TextView) view.findViewById(R.id.xyzk_simple_item_size);
                gesdc_tv= (TextView) view.findViewById(R.id.xyzk_simple_item_esdc);
            }
        }
    }
}
