package com.jiangxin.giftspirit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiangxin.giftspirit.JSONresource.GiftMessage;
import com.jiangxin.httplibrary.HttpCallback;
import com.jiangxin.httplibrary.HttpUtils;
import com.jiangxin.imagelibrary.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by my on 2016/8/15.
 */
public class GiftFragment extends Fragment implements HttpCallback{
    private static final String mGiftUrlHead = "http://www.1688wan.com";
    private static final String mGiftJsonContextUrl = "/majax.action?method=getGiftList";
    private LayoutInflater mInflater;
    private Context mContext;
    private GiftMessage mGiftMessage;
    private List<GiftMessage.ListBean> mGiftMessageList=new ArrayList<>();
    private List<GiftMessage.AdBean> mHeaderList;
    private PullToRefreshListView mGiftListView;
    private ListView listView;
    private Map<String,Object> map=new HashMap<>();
    private View giftHeaderView;
    private ViewPager header;
    private GiftAdapter mGiftAdapter;
    private int page=1;
    private String number;
    private String caDate;
    private LinearLayout mIndicatorLayout;
    private boolean isAutoScroll = true;
    private int index=10000;
    private int count;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            header.setCurrentItem(index);
            index ++;
            if (isAutoScroll) {
                sendEmptyMessageDelayed(1,2000);
            }
        }
    };

    public static GiftFragment newInstance(){
        return new GiftFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = getContext();
        super.onCreate(savedInstanceState);
        loadHttps("pageno",page);
    }

    private void loadHttps(String key,int value){
        map.put("key",value);
        HttpUtils.loadPost(mGiftUrlHead+mGiftJsonContextUrl,map).callback(this,page);
        map.clear();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater=inflater;
        View view =inflater.inflate(R.layout.gift_fragment_view,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        giftHeaderView=mInflater.inflate(R.layout.gift_header_layout,null);
        mGiftListView= (PullToRefreshListView) view.findViewById(R.id.gift_listview);
        mGiftListView.setMode(PullToRefreshBase.Mode.BOTH);
        listView = mGiftListView.getRefreshableView();
    }
    @Override
    public void success(String result, int requestCode) {
        Gson gson=new Gson();
        mGiftMessage = gson.fromJson(result, GiftMessage.class);
        mGiftMessageList.addAll(mGiftMessage.getList());
        mHeaderList=mGiftMessage.getAd();
        listView.addHeaderView(giftHeaderView);
        updateGiftListHeader();
        mGiftAdapter=new GiftAdapter();
        mGiftListView.setAdapter(mGiftAdapter);
        mGiftListView.onRefreshComplete();
        if (caDate!=null){
        mGiftListView.setLastUpdatedLabel("上次刷新时间"+caDate);
        }
        mGiftListView.setOnRefreshListener(new MyPullfreshListener());
//        setupRefreshControl();
    }

    class GiftAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mGiftMessageList == null ? 0 : mGiftMessageList.size();
        }

        @Override
        public Object getItem(int i) {
            return mGiftMessageList.get(i);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            if (view==null){
                view= mInflater.inflate(R.layout.gift_list_simple_item,viewGroup,false);
                viewHolder = new ViewHolder(view);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            final GiftMessage.ListBean gift = mGiftMessageList.get(position);
//            Log.d("giftAdapter",mGiftUrlHead + gift.getIconurl());
            viewHolder.title_tv.setText(gift.getGname());
            viewHolder.gift_tv.setText(gift.getGiftname());
            number="剩余："+gift.getNumber();
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
            ImageLoader.init(mContext).load(mGiftUrlHead + gift.getIconurl(),viewHolder.pic_iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,GiftInfoActivity.class);
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

    class MyPullfreshListener implements PullToRefreshBase.OnRefreshListener2<ListView>,HttpCallback{
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            Calendar caTime = Calendar.getInstance();
            caDate=caTime.get(Calendar.YEAR)+"."+caTime.get(Calendar.MONTH)+"."+caTime.get(Calendar.DAY_OF_MONTH)+"  "+caTime.get(Calendar.HOUR_OF_DAY)
                    +":"+caTime.get(Calendar.MINUTE);
            mGiftMessageList.clear();
            page=1;
            map.put("pageno",page);
            HttpUtils.loadPost(mGiftUrlHead+mGiftJsonContextUrl,map).callback(this,page);
            map.clear();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            page +=2;
            map.put("pageno",page);
            HttpUtils.loadPost(mGiftUrlHead+mGiftJsonContextUrl,map).callback(this,page);
            map.clear();
        }

        @Override
        public void success(String result, int requestCode) {
            Gson gson=new Gson();
            mGiftMessage = gson.fromJson(result, GiftMessage.class);
            mGiftMessageList.addAll(mGiftMessage.getList());
            mGiftAdapter.notifyDataSetChanged();
            mGiftListView.onRefreshComplete();
            mGiftListView.setLastUpdatedLabel("上次刷新时间"+caDate);
        }
    }


    private void updateGiftListHeader(){
        header = (ViewPager)giftHeaderView.findViewById(R.id.head_view_pager);
        mIndicatorLayout= (LinearLayout) giftHeaderView.findViewById(R.id.indicator_layout);
        GiftHeaderAdapter giftHeaderAdapter = new GiftHeaderAdapter();
        header.setAdapter(giftHeaderAdapter);
        header.setCurrentItem(mHeaderList.size()*2000);
        count=mIndicatorLayout.getChildCount();
        controlIndicator(0);
        handler.sendEmptyMessageDelayed(1,2000);
        header.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                controlIndicator(position%mHeaderList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        header.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isAutoScroll = false;
                        //移除所有的处于等待状态的Message
                        handler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        isAutoScroll = true;
                        handler.sendEmptyMessageDelayed(1,2000);
                        break;
                }
                return false;
            }
        });
    }

    class GiftHeaderAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 根据缓存或网络请求图片来设置头部视图
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position%mHeaderList.size();
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.init(mContext).load(mGiftUrlHead + mHeaderList.get(position).getIconurl(),imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /*
    * 设置广告页选择圆点
    * @param 传入对应圆点
    * */
    private void controlIndicator(int index) {
        ImageView view = (ImageView) mIndicatorLayout.getChildAt(index);
        //初始化所有的ImageView的enable属性为false
        for (int i = 0; i < count; i++) {
            ImageView childView = (ImageView) mIndicatorLayout.getChildAt(i);
            childView.setEnabled(false);
        }
        view.setEnabled(true);
    }
}
