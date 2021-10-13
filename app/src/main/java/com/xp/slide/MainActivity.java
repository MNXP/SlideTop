package com.xp.slide;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.xp.slide.adapter.PhotoAdapter;
import com.xp.slide.weight.MyBehavior;
import com.xp.slide.weight.refreshlayout.PullRefreshLayout;
import com.xp.slide.weight.refreshlayout.house.StoreHouseHeader;

public class MainActivity extends AppCompatActivity {
    private RecyclerView topRv;
    private RecyclerView bottomRv;
    private AppBarLayout appBarLayout;
    private PullRefreshLayout homeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBarLayout = findViewById(R.id.app_bar_layout);
        homeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        topRv = findViewById(R.id.top_img_rv);
        bottomRv = findViewById(R.id.bottom_img_rv);
        topRv.setLayoutManager(new LinearLayoutManager(this));
        bottomRv.setLayoutManager(new LinearLayoutManager(this));
        initView();
        initData();
    }
    private void initView() {
        StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, 20, 0, 20);
        header.initWithString("XIANGPAN");
        header.setTextColor(0xFF222222);

        homeRefreshLayout.setHeaderView(header);

        homeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListenerAdapter() {
            @Override
            public void onRefresh() {
                initData();
                checkHandler.sendEmptyMessageDelayed(0,2000);
            }
        });
        // 记录AppBar滚动距离
        appBarLayout.addOnOffsetChangedListener(View::setTag);

        homeRefreshLayout.setOnTargetScrollCheckListener(new PullRefreshLayout.OnTargetScrollCheckListener() {
            @Override
            public boolean onScrollUpAbleCheck() {
                // 根据AppBar滚动的距离来设置RefreshLayout是否可以下拉刷新
                int appbarOffset = ((appBarLayout.getTag() instanceof Integer)) ? (int) appBarLayout.getTag() : 0;
                return appbarOffset != 0;
            }

            @Override
            public boolean onScrollDownAbleCheck() {
                return true;
            }
        });

    }
    private void initData() {
        initTop();
        initBottom();
        if (!isFirstData) {
            initAppbar(-1);
        }

        appBarLayout.setExpanded(true, false);
    }

    private void initBottom() {
        PhotoAdapter bottomAdapter = new PhotoAdapter();
        bottomRv.setAdapter(bottomAdapter);
        bottomAdapter.setDataList(10);
        bottomRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    if (bottomRv != null && bottomRv.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) bottomRv.getLayoutManager();
                        if (layoutManager != null) {
                            int firstCompletelyVisible = layoutManager.findFirstCompletelyVisibleItemPosition();
                            initAppbar(firstCompletelyVisible);
                        }
                    }
                }
            }
        });
    }

    private void initTop() {
        PhotoAdapter topAdapter = new PhotoAdapter();
        topRv.setAdapter(topAdapter);
        topAdapter.setDataList(4);


    }
    private boolean isFirstData;
    private int oldPosition = -2;
    public void initAppbar(int position) {
        if (oldPosition == position){
            return;
        }
        oldPosition = position;
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        MyBehavior behavior = (MyBehavior) params.getBehavior();
        try {
            if (behavior!=null){
                if (position == -1){
                    behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                        @Override
                        public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                            isFirstData = true;
                            //为了启用折叠工具栏的滚动
                            return true;
                        }
                    });
                }else {
                    // 置顶后，如果recyclerview不是第一个item，禁止工具栏滑动
                    behavior.setCanMove(position<1);
                }
            }

        } catch (Exception e) {

        }

    }
    public Handler checkHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //模拟网络请求结束，去除刷新效果
            if (homeRefreshLayout != null) {
                homeRefreshLayout.refreshComplete();
            }
        }
    };
}