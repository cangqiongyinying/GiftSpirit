package com.jiangxin.giftspirit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;


public class MainActivity extends BaseActivity {

    private ActionBar mSupportActionBar;
    private GiftFragment giftFragment;
    private OpenServiceFragment openServiceFragment;
    private HotFragment hotFragment;
    private FeatureFragment featureFragment;
    private FragmentManager mSupportFragmentManager;
    private RadioGroup pageChangeBtn;
    private Fragment mCurrentShowFragment;
    private TextView titleTv;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBar("礼包精灵");
        initView();
    }


    public void initBar(String title) {
        mSupportActionBar=getSupportActionBar();
        View view=getLayoutInflater().inflate(R.layout.custom_action_bar,null);
        mSupportActionBar.setDisplayShowCustomEnabled(true);
        titleTv= (TextView) view.findViewById(R.id.action_bar_title);
        titleTv.setText(title);
        ImageView backIcon= (ImageView) view.findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        Button searchBtn= (Button) view.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        mSupportActionBar.setCustomView(view);
    }

    private void initView() {
        mDrawerLayout= (DrawerLayout) findViewById(R.id.menu_drawerLayout);
        mSupportFragmentManager= getSupportFragmentManager();
        giftFragment=GiftFragment.newInstance();
        openServiceFragment=OpenServiceFragment.newInstance();
        hotFragment=HotFragment.newInstance();
        featureFragment=FeatureFragment.newInstance();
        ctrlFragment(giftFragment);
        pageChangeBtn= (RadioGroup) findViewById(R.id.change_fragment_btnGroup);
        pageChangeBtn.check(R.id.gift_btn);
        pageChanged();
    }




    private void pageChanged(){
        pageChangeBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.gift_btn :
                        ctrlFragment(giftFragment);
                        titleTv.setText("礼包精灵");
                        break;
                    case R.id.open_service_btn:
                        ctrlFragment(openServiceFragment);
                        titleTv.setText("开服");
                        break;
                    case R.id.hot_btn:
                        ctrlFragment(hotFragment);
                        titleTv.setText("热门游戏");
                        break;
                    case R.id.feature_btn:
                        ctrlFragment(featureFragment);
                        titleTv.setText("独家企划");
                        break;
                }
            }
        });
    }
    private void ctrlFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mSupportFragmentManager.beginTransaction();
        if (mCurrentShowFragment != null && mCurrentShowFragment.isAdded()) {
            fragmentTransaction.hide(mCurrentShowFragment);
        }
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.show_fragment_layout,fragment);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.commit();

        mCurrentShowFragment = fragment;
    }
}
