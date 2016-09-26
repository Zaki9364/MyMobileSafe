package com.zaki.mymobilesafe.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.adapter.AppApapter;
import com.zaki.mymobilesafe.db.javabean.AppInfo;
import com.zaki.mymobilesafe.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 2016/9/13.
 */
public class AppManagerActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private List<AppInfo> mAppInfoList;
    private AppApapter mAppAdapter;
    private ListView lv_app_list;
    private TextView tv_des;
    private List<AppInfo> mSystemList;
    private List<AppInfo> mCustomerList;
    private AppInfo mAppInfo;
    private PopupWindow mPopupWindow;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mAppAdapter = new AppApapter(AppManagerActivity.this,mSystemList,mCustomerList);
            lv_app_list.setAdapter(mAppAdapter);

            if(tv_des!=null && mCustomerList!=null){
                tv_des.setText("用户应用("+mCustomerList.size()+")");
            }
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("软件管理");
        initTitle();
        initList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        new Thread(){
            @Override
            public void run() {
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                mSystemList = new ArrayList<>();
                mCustomerList = new ArrayList<>();
                for (AppInfo appInfo : mAppInfoList) {
                    if(appInfo.isSystem()){
                        //系统应用
                        mSystemList.add(appInfo);
                    }else{
                        //用户应用
                        mCustomerList.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initList() {
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        tv_des = (TextView) findViewById(R.id.tv_des);
        lv_app_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //滚动过程中调用方法
                //AbsListView中view就是listView对象
                //firstVisibleItem第一个可见条目索引值
                //visibleItemCount当前一个屏幕的可见条目数
                //总共条目总数
                if(mCustomerList!=null && mSystemList!=null){
                    if(firstVisibleItem>=mCustomerList.size()+1){
                        //滚动到了系统条目
                        tv_des.setText("系统应用("+mSystemList.size()+")");
                    }else{
                        //滚动到了用户应用条目
                        tv_des.setText("用户应用("+mCustomerList.size()+")");
                    }
                }
            }
        });
        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //view点中条目指向的view对象
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 || position == mCustomerList.size()+1){
                    return;
                }else{
                    if(position<mCustomerList.size()+1){
                        mAppInfo = mCustomerList.get(position-1);
                    }else{
                        //返回系统应用对应条目的对象
                        mAppInfo = mSystemList.get(position - mCustomerList.size()-2);
                    }
                    showPopupWindow(view);
                }
            }
        });
    }

    private void showPopupWindow(View view) {
        View popupView = View.inflate(this, R.layout.popupwindow_layout, null);

        TextView tv_uninstall = (TextView) popupView.findViewById(R.id.tv_uninstall);
        TextView tv_start = (TextView) popupView.findViewById(R.id.tv_start);
        TextView tv_share = (TextView) popupView.findViewById(R.id.tv_share);

        tv_uninstall.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_share.setOnClickListener(this);

        //透明动画(透明--->不透明)
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);

        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        //动画集合Set
        AnimationSet animationSet = new AnimationSet(true);
        //添加两个动画
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);

        //1,创建窗体对象,指定宽高

        mPopupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //2,设置一个透明背景(new ColorDrawable())
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        //3,指定窗体位置
        mPopupWindow.showAsDropDown(view, 400, -view.getHeight()+25);
        //4,popupView执行动画
        popupView.startAnimation(animationSet);
    }

    private void initTitle() {
        //1,获取磁盘(内存,区分于手机运行内存)可用大小,磁盘路径
        String path = Environment.getDataDirectory().getAbsolutePath();
        //2,获取sd卡可用大小,sd卡路径
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //3,获取以上两个路径下文件夹的可用大小
        String memoryAvailSpace = Formatter.formatFileSize(this, getAvailSpace(path));
        String sdMemoryAvailSpace = Formatter.formatFileSize(this,getAvailSpace(sdPath));

        TextView tv_memory = (TextView) findViewById(R.id.tv_memory);
        TextView tv_sd_memory = (TextView) findViewById(R.id.tv_sd_memory);

        tv_memory.setText("磁盘可用空间:"+memoryAvailSpace);
        tv_sd_memory.setText("sd卡可用空间:"+sdMemoryAvailSpace);
    }

    /**
     * @param path  指定手机磁盘或sd卡路径
     * @return  返回指定路径可用区域的byte类型值
     */
    private long getAvailSpace(String path) {
        //获取可用磁盘大小类
        StatFs statFs = new StatFs(path);
        //获取可用区块的个数
        long count = statFs.getAvailableBlocks();
        //获取区块的大小
        long size = statFs.getBlockSize();
        //区块大小*可用区块个数 == 可用空间大小
        return count*size;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_manager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_uninstall:
                if(mAppInfo.isSystem()){
                    Toast.makeText(getApplicationContext(), "此应用不能卸载",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent("android.intent.action.DELETE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:"+mAppInfo.getPackageName()));
                    startActivity(intent);
                }
                break;
            case R.id.tv_start:
                //通过桌面去启动指定包名应用
                PackageManager pm = getPackageManager();
                //通过Launch开启制定包名的意图,去开启应用
                Intent launchIntentForPackage = pm.getLaunchIntentForPackage(mAppInfo.getPackageName());
                if(launchIntentForPackage!=null){
                    startActivity(launchIntentForPackage);
                }else{
                    Toast.makeText(getApplicationContext(), "此应用不能被开启",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_share:
                //通过短信应用,向外发送短信
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"分享一个应用,应用名称为"+mAppInfo.getName());
                intent.setType("text/plain");
                startActivity(intent);
                break;
        }

        //点击了窗体后消失窗体
        if(mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }
}
