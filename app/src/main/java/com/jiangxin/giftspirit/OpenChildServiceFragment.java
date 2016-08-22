package com.jiangxin.giftspirit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiangxin.giftspirit.JSONresource.OpenServiceMessage;
import com.jiangxin.httplibrary.HttpCallback;
import com.jiangxin.httplibrary.HttpUtils;
import com.jiangxin.imagelibrary.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangxin on 2016/8/17.
 */
public class OpenChildServiceFragment extends Fragment implements HttpCallback{
    private static final String mGiftUrlHead = "http://www.1688wan.com";
    private static final String mGameJsonContextUrl = "/majax.action?method=getJtkaifu";
    private Context mContext;
    private LayoutInflater mInflater;
    private ExpandableListView mExpandListView;
    private MyExpandableAdapter mExpandableAdapter;
    private OpenServiceMessage openServiceMessage;
    private List<String> openServiceTime=new ArrayList<>();

    public static OpenChildServiceFragment newInstance(){
        return new OpenChildServiceFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
        HttpUtils.load(mGiftUrlHead+mGameJsonContextUrl).callback(this,1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater=inflater;
        View view =inflater.inflate(R.layout.open_service_child_list,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mExpandListView= (ExpandableListView) view.findViewById(R.id.expand_list_view);
    }
    @Override
    public void success(String result, int requestCode) {
        Gson gson=new Gson();
        openServiceMessage=gson.fromJson(result,OpenServiceMessage.class);
        List<String> temp=new ArrayList<>();
        for (OpenServiceMessage.InfoBean i:openServiceMessage.getInfo()){
            temp.add(i.getStarttime());
        }
        for (String time:temp){
            boolean flag=judge(openServiceTime,time);
            if (!flag){
                openServiceTime.add(time);
            }
        }
        mExpandableAdapter=new MyExpandableAdapter();
        mExpandListView.setAdapter(mExpandableAdapter);
        for (int i = 0; i < openServiceTime.size(); i++) {
            mExpandListView.expandGroup(i);
        }
    }

    private boolean judge(List<String> list,String member){
        boolean flag=false;
        for (String i:list){
            if (i.equals(member)){
                flag=true;
                break;
            }
        }
        return flag;
    }

    class MyExpandableAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return openServiceTime==null? 0:openServiceTime.size();
        }

        @Override
        public int getChildrenCount(int i) {
            String startTime=openServiceTime.get(i);
            List<OpenServiceMessage.InfoBean> child=new ArrayList<>();
            for (OpenServiceMessage.InfoBean info:openServiceMessage.getInfo()){
                if (info.getStarttime().equals(startTime)){
                    child.add(info);
                }
            }
            for (OpenServiceMessage.InfoBean infoBean:child){
            }
            return child.size();
        }

        @Override
        public Object getGroup(int i) {
            return openServiceTime.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            String startTime=openServiceTime.get(i);
            List<OpenServiceMessage.InfoBean> child=new ArrayList<>();
            for (OpenServiceMessage.InfoBean info:openServiceMessage.getInfo()){
                if (info.getStarttime().equals(startTime)){
                    child.add(info);
                }
            }
            return child.get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
//            TextView groupView= (TextView) view;
            View groupView =view;
            if(groupView ==null){

                groupView=mInflater.inflate(R.layout.open_service_group_item,viewGroup,false);
                TextView textView= (TextView) groupView.findViewById(R.id.open_group_item_tv);
                if (i==0){
                    textView.setText(openServiceTime.get(i)+"  今日开服");
                }
                textView.setText(openServiceTime.get(i));
            }
            return groupView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            View childView=view;
            if (childView==null){
                childView=LayoutInflater.from(mContext).inflate(R.layout.open_service_child_item,viewGroup,false);
                viewHolder = new ViewHolder(childView);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            String startTime=openServiceTime.get(i);
            List<OpenServiceMessage.InfoBean> child=new ArrayList<>();
            for (OpenServiceMessage.InfoBean info:openServiceMessage.getInfo()){
                if (info.getStarttime().equals(startTime)){
                    child.add(info);
                }
            }
            final OpenServiceMessage.InfoBean openMessage=child.get(i1);
            viewHolder.title_tv.setText(openMessage.getGname());
            String operators="运营商："+openMessage.getOperators();
            viewHolder.message_tv.setText(operators);
            viewHolder.start_time_tv.setText(openMessage.getStarttime());
            viewHolder.service_tv.setText(openMessage.getArea());
            ImageLoader.init(mContext).load(mGiftUrlHead+openMessage.getIconurl(),viewHolder.pic_iv);
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,GameInfoActivity.class);
                    intent.putExtra("id",openMessage.getGid());
                    startActivity(intent);
                }
            });
            return childView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
    class ViewHolder{
        public ImageView pic_iv;
        public TextView title_tv;
        public TextView message_tv;
        public TextView start_time_tv;
        public TextView service_tv;

        public ViewHolder(View view){
            view.setTag(this);
            pic_iv= (ImageView) view.findViewById(R.id.open_child_item_image);
            title_tv= (TextView) view.findViewById(R.id.open_child_item_title);
            message_tv= (TextView) view.findViewById(R.id.open_child_item_giftType);
            start_time_tv= (TextView) view.findViewById(R.id.open_child_item_start_time);
            service_tv= (TextView) view.findViewById(R.id.open_child_item_service);
        }
    }
}
