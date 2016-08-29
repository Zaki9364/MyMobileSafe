package com.zaki.mymobilesafe.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;

/**
 * Created by zaki on 2016/8/26.
 */
public class SplashActivity extends AppCompatActivity {
    private TextView tv_version_name;
    private int mLocalVersionCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //初始化UI
        initUI();
        //初始化数据
        initData();
    }

    /**
     * 初始化控件
     */
    private void initUI() {

        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //1.应用版本名称
        tv_version_name.setText("版本名称：" + getVersionName());
        //2.检测（本地版本号与服务器版本号进行比对）是否有更新，有就提示用户下载更新
        //获取本地版本号
        mLocalVersionCode = getVersionCode();
        //获取服务器端版本号（客户端发请求，服务端给响应）

    }

    /**
     * 获取版本号
     *
     * @return 非0代表获取成功，为0代表获取失败
     */
    private int getVersionCode() {
        //1.包管理者对象packageManager
        PackageManager pm = getPackageManager();
        try {
            //2.从包管理者对象中获取指定包名的基本信息（包括版本名称和版本号等）,传0
            PackageInfo mPackageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //3.获取版本号
            return mPackageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称，从清单文件中
     *
     * @return
     */
    private String getVersionName() {
        //1.包管理者对象packageManager
        PackageManager pm = getPackageManager();
        try {
            //2.从包管理者对象中获取指定包名的基本信息（包括版本名称和版本号等）,传0
            PackageInfo mPackageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //3.获取版本名称
            return mPackageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
