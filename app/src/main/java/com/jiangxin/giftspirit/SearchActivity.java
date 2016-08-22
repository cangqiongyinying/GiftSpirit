package com.jiangxin.giftspirit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiangxin.giftspirit.JSONresource.SearchMessage;
import com.jiangxin.httplibrary.HttpCallback;
import com.jiangxin.httplibrary.HttpUtils;
import com.jiangxin.imagelibrary.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by my on 2016/8/19.
 */
public class SearchActivity extends BaseActivity implements HttpCallback{
    private static final String mGiftUrlHead = "http://www.1688wan.com";
    private static final String mGiftJsonContextUrl = "/majax.action?method=searchGift";
    private ActionBar mSupportActionBar;
    private ListView mListView;
    private Map<String,Object> map=new HashMap<>();
    private SearchMessage searchMessage;
    private List<SearchMessage.ListBean> list;
    private SearchAdapter searchAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        map.put("pageno",1);
        HttpUtils.loadPost(mGiftUrlHead+"/majax.action?method=getGiftList",map).callback(this,5);
        map.clear();
    }

    private void initView() {
        mSupportActionBar=getSupportActionBar();
        View view=getLayoutInflater().inflate(R.layout.custom_actionbar_search,null);
        mSupportActionBar.setDisplayShowCustomEnabled(true);
        ImageView backIcon= (ImageView) view.findViewById(R.id.back_icon_search);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SearchView searchView= (SearchView) view.findViewById(R.id.action_bar_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                loadHttps("key",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        mSupportActionBar.setCustomView(view);
        mListView= (ListView) findViewById(R.id.search_listView);
    }

    private void loadHttps(String key,String value){
        map.put(key,value);
        HttpUtils.loadPost(mGiftUrlHead+mGiftJsonContextUrl,map).callback(this,5);
        map.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void success(String result, int requestCode) {
        Gson gson=new Gson();
        searchMessage=gson.fromJson(result,SearchMessage.class);
        list=searchMessage.getList();
        searchAdapter=new SearchAdapter();
        mListView.setAdapter(searchAdapter);
    }

    class SearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            if (view==null){
                view= LayoutInflater.from(SearchActivity.this).inflate(R.layout.gift_list_simple_item,viewGroup,false);
                viewHolder = new ViewHolder(view);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            final SearchMessage.ListBean gift = list.get(position);
            viewHolder.title_tv.setText(gift.getGname());
            viewHolder.gift_tv.setText(gift.getGiftname());
            String number="剩余："+gift.getNumber();
            viewHolder.count_tv.setText(number);
            viewHolder.time_tv.setText(gift.getAddtime());
            if (gift.getNumber()==0){
                viewHolder.getgift_btn.setText("淘号");
                viewHolder.getgift_btn.setEnabled(false);
            }else {
                viewHolder.getgift_btn.setText("立即领取");
                viewHolder.getgift_btn.setEnabled(true);
            }
            viewHolder.pic_iv.setTag(mGiftUrlHead + gift.getIconurl());
            ImageLoader.init(SearchActivity.this).load(mGiftUrlHead + gift.getIconurl(),viewHolder.pic_iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(SearchActivity.this,GiftInfoActivity.class);
                    intent.putExtra("id",gift.getId());
                    startActivity(intent);
                }
            });
            return view;
        }

        class ViewHolder{
            public ImageView pic_iv;
            public TextView title_tv;
            public TextView gift_tv;
            public TextView count_tv;
            public TextView time_tv;
            public Button getgift_btn;

            public ViewHolder(View view){
                view.setTag(this);
                pic_iv= (ImageView) view.findViewById(R.id.simple_item_image);
                title_tv= (TextView) view.findViewById(R.id.simple_item_title);
                gift_tv= (TextView) view.findViewById(R.id.simple_item_giftType);
                count_tv= (TextView) view.findViewById(R.id.simple_item_count);
                time_tv= (TextView) view.findViewById(R.id.simple_item_time);
                getgift_btn= (Button) view.findViewById(R.id.get_gift_btn);
            }
        }
    }
}
