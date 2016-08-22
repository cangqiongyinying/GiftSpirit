package com.jiangxin.giftspirit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2016/8/16.
 */
public class OpenServiceFragment extends Fragment{
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Context mContext;
    private MyPagerAdapter mPagerAdapter;
    private List<Fragment> fragments=new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    public static OpenServiceFragment newInstance(){
        return new OpenServiceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.openservice_fragment_view,container,false);
        addFragments();
        initView(view);
        return view;
    }

    private void addFragments(){
        fragments.add(OpenChildServiceFragment.newInstance());
        fragments.add(OpenTextFragment.newInstance());
        titles.add("开服");
        titles.add("开测");
    }
    private void initView(View view) {
        mTabLayout=(TabLayout)view.findViewById(R.id.open_service_tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.open_service_viewpager);
        mPagerAdapter = new MyPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments==null?0:fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}