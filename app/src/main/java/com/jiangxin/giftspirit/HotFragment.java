package com.jiangxin.giftspirit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiangxin.giftspirit.JSONresource.HotGameMessage;
import com.jiangxin.httplibrary.HttpCallback;
import com.jiangxin.httplibrary.HttpUtils;
import com.jiangxin.imagelibrary.ImageLoader;

import java.util.List;

/**
 * Created by my on 2016/8/16.
 */
public class HotFragment extends Fragment implements HttpCallback{
    private static final String mGiftUrlHead = "http://www.1688wan.com";
    private static final String mGiftJsonContextUrl = "/majax.action?method=hotpushForAndroid";
    private ListView mListView;
    private GridView mGridView;
    private Context mContext;
    private LayoutInflater mInflater;
    private HotGameMessage hotGameMessage;
    private List<HotGameMessage.InfoBean.Push1Bean> boutiqueList;
    private List<HotGameMessage.InfoBean.Push2Bean> hotList;
    private MyListAdapter mListAdapter;
    private MyGridAdapter mGridAdpter;
    public static HotFragment newInstance(){
        return new HotFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
        HttpUtils.load(mGiftUrlHead+mGiftJsonContextUrl).callback(this,3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater=inflater;
        View view=inflater.inflate(R.layout.hot_fragment_view,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView= (ListView) view.findViewById(R.id.boutique);
        mGridView= (GridView) view.findViewById(R.id.hot_grid_view);
    }

    @Override
    public void success(String result, int requestCode) {
        Gson gson=new Gson();
        hotGameMessage=gson.fromJson(result,HotGameMessage.class);
        boutiqueList=hotGameMessage.getInfo().getPush1();
        hotList=hotGameMessage.getInfo().getPush2();
        mListAdapter=new MyListAdapter();
        mListView.setAdapter(mListAdapter);
        mGridAdpter=new MyGridAdapter();
        mGridView.setAdapter(mGridAdpter);
    }

    class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return boutiqueList==null?0:boutiqueList.size();
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
                view= mInflater.inflate(R.layout.boutique_simple_item,viewGroup,false);
                viewHolder = new ViewHolder(view);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            final HotGameMessage.InfoBean.Push1Bean boutiqueGame= boutiqueList.get(i);
            viewHolder.gname_tv.setText(boutiqueGame.getName());
            String typeName="类型："+boutiqueGame.getTypename();
            viewHolder.gtype_tv.setText(typeName);
            String size="大小：";
            if (boutiqueGame.getSize()==null){
                size +="未知";
            }else {
                size+=boutiqueGame.getSize();
            }
            viewHolder.size_tv.setText(size);
            String playerNum=boutiqueGame.getClicks()+"人在玩";
            viewHolder.player_num_tv.setText(playerNum);
            viewHolder.pic_iv.setTag(mGiftUrlHead + boutiqueGame.getLogo());
            ImageLoader.init(mContext).load(mGiftUrlHead + boutiqueGame.getLogo(),viewHolder.pic_iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,GameInfoActivity.class);
                    intent.putExtra("id",boutiqueGame.getAppid());
                    startActivity(intent);
                }
            });
            return view;
        }

        class ViewHolder {
            public ImageView pic_iv;
            public TextView gname_tv;
            public TextView gtype_tv;
            public TextView size_tv;
            public TextView player_num_tv;

            public ViewHolder(View view) {
                view.setTag(this);
                pic_iv = (ImageView) view.findViewById(R.id.boutique_simple_item_image);
                gname_tv = (TextView) view.findViewById(R.id.boutique_simple_item_gname);
                gtype_tv = (TextView) view.findViewById(R.id.boutique_simple_item_game_type);
                size_tv = (TextView) view.findViewById(R.id.boutique_simple_item_size);
                player_num_tv= (TextView) view.findViewById(R.id.boutique_simple_item_player_num);
            }
        }
    }

    class MyGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return hotList==null? 0:hotList.size();
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
                view= mInflater.inflate(R.layout.hot_simple_item,viewGroup,false);
                viewHolder = new ViewHolderHot(view);
            }else {
                viewHolder= (ViewHolderHot) view.getTag();
            }
            final HotGameMessage.InfoBean.Push2Bean hotGame= hotList.get(i);
            viewHolder.gname_tv.setText(hotGame.getName());
            viewHolder.pic_iv.setTag(mGiftUrlHead + hotGame.getLogo());
            ImageLoader.init(mContext).load(mGiftUrlHead + hotGame.getLogo(),viewHolder.pic_iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,GameInfoActivity.class);
                    intent.putExtra("id",hotGame.getAppid());
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
                pic_iv = (ImageView) view.findViewById(R.id.hot_game_image);
                gname_tv = (TextView) view.findViewById(R.id.hot_game_name);
            }
        }
    }
}
