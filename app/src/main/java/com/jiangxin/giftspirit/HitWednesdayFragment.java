package com.jiangxin.giftspirit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiangxin.giftspirit.JSONresource.FeatureMessage;
import com.jiangxin.httplibrary.HttpCallback;
import com.jiangxin.httplibrary.HttpUtils;
import com.jiangxin.imagelibrary.ImageLoader;

import java.util.List;

/**
 * Created by my on 2016/8/18.
 */
public class HitWednesdayFragment extends Fragment implements HttpCallback{
    private static final String mGiftUrlHead = "http://www.1688wan.com";
    private static final String mGiftJsonContextUrl = "/majax.action?method=bdxqs&pageNo=0";
    private Context mContext;
    private LayoutInflater mInflater;
    private ListView mListView;
    private FeatureMessage featureMessage;
    private List<FeatureMessage.ListBean> list;
    private MyListAdapter myListAdapter;
    private boolean mIsBottom=false;

    public static HitWednesdayFragment newInstance(){
        return new HitWednesdayFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
        HttpUtils.load(mGiftUrlHead+mGiftJsonContextUrl).callback(this,4);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater=inflater;
        View view=inflater.inflate(R.layout.hit_wednesday_view,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView= (ListView) view.findViewById(R.id.hit_wednesday_list);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if(mIsBottom && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    Toast.makeText(mContext,"没有更多内容了",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount){
                    mIsBottom = true;
                }
                else{
                    mIsBottom = false;
                }
            }
        });
    }

    @Override
    public void success(String result, int requestCode) {
        Gson gson=new Gson();
        featureMessage=gson.fromJson(result,FeatureMessage.class);
        list=featureMessage.getList();
        myListAdapter=new MyListAdapter();
        mListView.setAdapter(myListAdapter);
    }

    class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list==null?0:list.size();
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

            ViewHolder viewHolder;
            if (view==null){
                view= mInflater.inflate(R.layout.hit_wednesday_simple_item,viewGroup,false);
                viewHolder = new ViewHolder(view);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            final FeatureMessage.ListBean hitMessage= list.get(i);
            viewHolder.name_tv.setText(hitMessage.getName());
            viewHolder.start_time_tv.setText(hitMessage.getAddtime());
            ImageLoader.init(mContext).load(mGiftUrlHead + hitMessage.getIconurl(),viewHolder.pic_iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,FeatureInfoBDXQSActivity.class);
                    intent.putExtra("id",hitMessage.getSid());
                    intent.putExtra("name",hitMessage.getName());
                    intent.putExtra("iconurl",mGiftUrlHead + hitMessage.getIconurl());
                    intent.putExtra("descs",hitMessage.getDescs());
                    intent.putExtra("addtime",hitMessage.getAddtime());
                    startActivity(intent);
                }
            });
            return view;
        }

        class ViewHolder {
            public ImageView pic_iv;
            public TextView name_tv;
            public TextView start_time_tv;

            public ViewHolder(View view) {
                view.setTag(this);
                pic_iv = (ImageView) view.findViewById(R.id.hit_wednesday_item_image);
                name_tv = (TextView) view.findViewById(R.id.hit_wednesday_item_name);
                start_time_tv = (TextView) view.findViewById(R.id.hit_wednesday_item_addtime);
            }
        }
    }
}
