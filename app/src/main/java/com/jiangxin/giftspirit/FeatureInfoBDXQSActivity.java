package com.jiangxin.giftspirit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiangxin.giftspirit.JSONresource.BDXQSMessage;
import com.jiangxin.httplibrary.HttpCallback;
import com.jiangxin.httplibrary.HttpUtils;
import com.jiangxin.imagelibrary.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by my on 2016/8/18.
 */
public class FeatureInfoBDXQSActivity extends BaseActivity implements HttpCallback{
    private static final String mGiftUrlHead = "http://www.1688wan.com";
    private static final String mGiftJsonContextUrl = "/majax.action?method=bdxqschild";
    private int gameId;
    private Map<String,Object> map=new HashMap<>();
    private String iconurl;
    private String descs;
    private String addtime;
    private ImageView mImageView;
    private TextView addTimeTextView;
    private TextView esdcTextView;
    private GridView mGridView;
    private BDXQSMessage bdxqsMessage;
    private List<BDXQSMessage.ListBean> list;
    private MyGridAdapter myGridAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_info_bdxqs);
        getIntentMessage();
        initView();
        loadHttps("id",gameId);
    }

    private void initView() {
        mImageView= (ImageView) findViewById(R.id.bdxqs_child_image);
        addTimeTextView= (TextView) findViewById(R.id.bdxqs_child_addtime);
        esdcTextView= (TextView) findViewById(R.id.bdxqs_child_esdc);
        mGridView= (GridView) findViewById(R.id.bdxqs_child_gridView);
        ImageLoader.init(this).load(iconurl,mImageView);
        addTimeTextView.setText(addtime);
        esdcTextView.setText(descs);
    }

    private void loadHttps(String key,int value){
        map.put(key,value);
        HttpUtils.loadPost(mGiftUrlHead+mGiftJsonContextUrl,map).callback(this,1);
        map.clear();
    }
    private void getIntentMessage(){
        Intent intent=this.getIntent();
        gameId=intent.getIntExtra("id",0);
        iconurl=intent.getStringExtra("iconurl");
        descs=intent.getStringExtra("descs");
        addtime=intent.getStringExtra("addtime");
        initBar(intent.getStringExtra("name"));
    }
    @Override
    public void success(String result, int requestCode) {
        Gson gson=new Gson();
        bdxqsMessage=gson.fromJson(result,BDXQSMessage.class);
        list=bdxqsMessage.getList();
        myGridAdapter=new MyGridAdapter();
        mGridView.setAdapter(myGridAdapter);
    }

    class MyGridAdapter extends BaseAdapter {

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
                view= LayoutInflater.from(FeatureInfoBDXQSActivity.this).inflate(R.layout.baxqs_gridview_simple_item,viewGroup,false);
                viewHolder = new ViewHolderHot(view);
            }else {
                viewHolder= (ViewHolderHot) view.getTag();
            }
            final BDXQSMessage.ListBean game= list.get(i);
            viewHolder.gname_tv.setText(game.getAppname());
            viewHolder.pic_iv.setTag(mGiftUrlHead + game.getAppicon());
            ImageLoader.init(FeatureInfoBDXQSActivity.this).load(mGiftUrlHead + game.getAppicon(),viewHolder.pic_iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(FeatureInfoBDXQSActivity.this,GameInfoActivity.class);
                    intent.putExtra("id",game.getAppid());
                    startActivity(intent);
                }
            });
            return view;
        }

        class ViewHolderHot {
            public ImageView pic_iv;
            public TextView gname_tv;


            public ViewHolderHot(View view) {
                view.setTag(this);
                pic_iv = (ImageView) view.findViewById(R.id.bdxqs_child_gridView_image);
                gname_tv = (TextView) view.findViewById(R.id.bdxqs_child_gridView_gname);
            }
        }
    }
}
