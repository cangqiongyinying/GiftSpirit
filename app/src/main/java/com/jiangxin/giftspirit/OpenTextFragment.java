package com.jiangxin.giftspirit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiangxin.giftspirit.JSONresource.OpenTextMessage;
import com.jiangxin.httplibrary.HttpCallback;
import com.jiangxin.httplibrary.HttpUtils;
import com.jiangxin.imagelibrary.ImageLoader;

import java.util.Calendar;
import java.util.List;

/**
 * Created by my on 2016/8/18.
 */
public class OpenTextFragment extends Fragment implements HttpCallback{
    private static final String mGiftUrlHead = "http://www.1688wan.com";
    private static final String mGameJsonContextUrl = "/majax.action?method=getWebfutureTest";
    private Context mContext;
    private LayoutInflater mInflater;
    private PullToRefreshListView mGameListView;
    private OpenTextMessage openTextMessage;
    private List<OpenTextMessage.InfoBean> openTextMessageInfo;
    private TextGameAdapter textGameAdapter;
    private String caDate;

    public static OpenTextFragment newInstance(){
        return new OpenTextFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
        HttpUtils.load(mGiftUrlHead+mGameJsonContextUrl).callback(this,2);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater=inflater;
        View view=inflater.inflate(R.layout.opentext_fragment_view,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mGameListView= (PullToRefreshListView) view.findViewById(R.id.open_text_list);
        mGameListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void success(String result, int requestCode) {
        Gson gson=new Gson();
        openTextMessage=gson.fromJson(result, OpenTextMessage.class);
        openTextMessageInfo=openTextMessage.getInfo();
        textGameAdapter=new TextGameAdapter();
        mGameListView.setAdapter(textGameAdapter);
        mGameListView.onRefreshComplete();
        mGameListView.setOnRefreshListener(new MyPullfreshListener());
    }

    class TextGameAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return openTextMessageInfo==null?0:openTextMessageInfo.size();
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
            final ViewHolder viewHolder;
            if (view==null){
                view= mInflater.inflate(R.layout.open_text_simple_item,viewGroup,false);
                viewHolder = new ViewHolder(view);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            final OpenTextMessage.InfoBean textGame = openTextMessageInfo.get(i);
            viewHolder.gname_tv.setText(textGame.getGname());
            String operators="运营商"+textGame.getOperators();
            viewHolder.gservice_tv.setText(operators);
            viewHolder.gtime_tv.setText(textGame.getTeststarttime());
            viewHolder.pic_iv.setTag(mGiftUrlHead + textGame.getIconurl());
            ImageLoader.init(mContext).load(mGiftUrlHead + textGame.getIconurl(),viewHolder.pic_iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,GameInfoActivity.class);
                    intent.putExtra("id",textGame.getGid());
                    startActivity(intent);
                }
            });
            return view;
        }

        class ViewHolder{
            public ImageView pic_iv;
            public TextView gname_tv;
            public TextView gservice_tv;
            public TextView gtime_tv;

            public ViewHolder(View view){
                view.setTag(this);
                pic_iv= (ImageView) view.findViewById(R.id.open_text_item_iamge);
                gname_tv= (TextView) view.findViewById(R.id.open_text_item_gname);
                gservice_tv= (TextView) view.findViewById(R.id.open_text_item_service);
                gtime_tv= (TextView) view.findViewById(R.id.open_text_item_texttime);
            }
        }
    }

    class MyPullfreshListener implements PullToRefreshBase.OnRefreshListener2<ListView>,HttpCallback{
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mGameListView.onRefreshComplete();
            }
        };
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            Calendar caTime = Calendar.getInstance();
            caDate=caTime.get(Calendar.YEAR)+"."+caTime.get(Calendar.MONTH)+"."+caTime.get(Calendar.DAY_OF_MONTH)+"  "+caTime.get(Calendar.HOUR_OF_DAY)
                    +":"+caTime.get(Calendar.MINUTE);
            openTextMessageInfo.clear();
            HttpUtils.load(mGiftUrlHead+mGameJsonContextUrl).callback(this,2);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            Toast.makeText(mContext,"没有更多的了",Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(1,1000);
        }

        @Override
        public void success(String result, int requestCode) {
            Gson gson=new Gson();
            openTextMessage=gson.fromJson(result, OpenTextMessage.class);
            openTextMessageInfo=openTextMessage.getInfo();
            textGameAdapter.notifyDataSetChanged();
            mGameListView.onRefreshComplete();
            mGameListView.setLastUpdatedLabel("上次刷新时间"+caDate);
        }
    }
}
