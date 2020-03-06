package com.example.boots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import RecyclerView.Fruit;
import RecyclerView.FruitAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SmartRefresh extends AppCompatActivity {


    static RefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_refresh);
        ButterKnife.bind(this);//注意位置 否则报错
        layoutManager = new LinearLayoutManager(this);

        mRefreshLayout= findViewById(R.id.refreshLayout);
//        //Android 智能刷新框架SmartRefreshLayout 第三种构造方式，覆盖前两种构造方法
//        final RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        //设置 Header 为 贝塞尔雷达 样式
//        mRefreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
//        //设置 Footer 为 球脉冲 样式
//        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        //通过set自定义一些属性
        //header.set*

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                List<Fruit> data=initFruits(1);
                Message message = new Message();
                message.what = 1 ;
                message.obj = data ;
                mHandler.sendMessageDelayed(message,2000);
            }
        });

        //上拉刷新
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                List<Fruit> data=initFruits(2);
                Message message = new Message();
                message.what = 2;
                message.obj = data ;
                mHandler.sendMessageDelayed(message,2000);
            }
        });



    }

    //点击返回按钮，结束此activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:         //刷新加载
                    List<Fruit> mList  = (List<Fruit>) msg.obj;
                    mRefreshLayout.finishRefresh(true);
                    recyclerView.setLayoutManager(layoutManager);
                    FruitAdapter adapter = new FruitAdapter(mList);
                    recyclerView.setAdapter(adapter);
                    break;
                case 2:         //加载更多
                    List<Fruit> mList1  = (List<Fruit>) msg.obj;
                    mRefreshLayout.finishLoadMore(true);
                    recyclerView.setLayoutManager(layoutManager);
                    FruitAdapter adapter1= new FruitAdapter(mList1);
                    recyclerView.setAdapter(adapter1);
                    break;
            }
            return false;
        }
    });

    private List<Fruit> initFruits(int s) {
        List<Fruit> fruitList =new ArrayList<Fruit>();
        if(s==1){
            Fruit apple = new Fruit("Apple", R.mipmap.admin1);
            fruitList.add(apple);
            Fruit banana = new Fruit("Banana", R.mipmap.admin2);
            fruitList.add(banana);

        }else{
            Fruit banana = new Fruit("Banana", R.mipmap.admin2);
            fruitList.add(banana);
            Fruit apple = new Fruit("Apple", R.mipmap.admin1);
            fruitList.add(apple);
        }
        return fruitList;
    }
}
