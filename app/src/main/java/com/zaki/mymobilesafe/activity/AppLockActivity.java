package com.zaki.mymobilesafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.adapter.IsLockAdapter;
import com.zaki.mymobilesafe.db.dao.AppLockDao;
import com.zaki.mymobilesafe.db.javabean.AppInfo;
import com.zaki.mymobilesafe.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 2016/9/17.
 */
public class AppLockActivity extends BaseAppCompatActivity {
    private Button bt_unlock, bt_lock;
    private LinearLayout ll_unlock, ll_lock;
    private TextView tv_unlock, tv_lock;
    private ListView lv_unlock, lv_lock;
    private List<AppInfo> mAppInfoList;
    private List<AppInfo> mLockList;
    private List<AppInfo> mUnLockList;
    private AppLockDao mDao;
    private IsLockAdapter mLockAdapter, mUnLockAdapter;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //6.接收到消息,填充已加锁和未加锁的数据适配器
            mLockAdapter = new IsLockAdapter(AppLockActivity.this, mLockList, mUnLockList, true, tv_lock, tv_unlock);
            lv_lock.setAdapter(mLockAdapter);
            mUnLockAdapter = new IsLockAdapter(AppLockActivity.this, mLockList, mUnLockList, false, tv_lock, tv_unlock);
            lv_unlock.setAdapter(mUnLockAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }

    /**
     * 区分已加锁和未加锁应用的集合
     */
    private void initData() {
        new Thread() {
            public void run() {
                //1.获取所有手机中的应用
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                //2.区分已加锁应用和未加锁应用
                mLockList = new ArrayList<>();
                mUnLockList = new ArrayList<>();

                //3.获取数据库中已加锁应用包名的的结合
                mDao = AppLockDao.getInstance(getApplicationContext());
                List<String> lockPackageList = mDao.findAll();
                for (AppInfo appInfo : mAppInfoList) {
                    //4,如果循环到的应用的包名,在数据库中,则说明是已加锁应用
                    if (lockPackageList.contains(appInfo.getPackageName())) {
                        mLockList.add(appInfo);
                    } else {
                        mUnLockList.add(appInfo);
                    }
                }
                //5.告知主线程,可以使用维护的数据
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        bt_unlock = (Button) findViewById(R.id.bt_unlock);
        bt_lock = (Button) findViewById(R.id.bt_lock);

        ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
        ll_lock = (LinearLayout) findViewById(R.id.ll_lock);

        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        tv_lock = (TextView) findViewById(R.id.tv_lock);

        lv_unlock = (ListView) findViewById(R.id.lv_unlock);
        lv_lock = (ListView) findViewById(R.id.lv_lock);

        bt_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.已加锁列表隐藏,未加锁列表显示
                ll_lock.setVisibility(View.GONE);
                ll_unlock.setVisibility(View.VISIBLE);
                //2.未加锁变成深色图片,已加锁变成浅色图片
                bt_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                bt_lock.setBackgroundResource(R.drawable.tab_right_default);
                mUnLockAdapter.notifyDataSetChanged();
            }
        });

        bt_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.已加锁列表显示,未加锁列表隐藏
                ll_lock.setVisibility(View.VISIBLE);
                ll_unlock.setVisibility(View.GONE);
                //2.未加锁变成浅色图片,已加锁变成深色图片
                bt_unlock.setBackgroundResource(R.drawable.tab_left_default);
                bt_lock.setBackgroundResource(R.drawable.tab_right_pressed);
                mLockAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_lock;
    }
}
