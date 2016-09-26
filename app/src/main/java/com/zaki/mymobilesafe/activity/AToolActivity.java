package com.zaki.mymobilesafe.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.engine.SmsBackUp;

import java.io.File;

/**
 * Created by zaki on 2016/9/5.
 */
public class AToolActivity extends BaseAppCompatActivity {
    private TextView tv_query_phone_address,tv_sms_backup;
    private TextView tv_commonnumber_query;
    private TextView tv_app_lock;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("高级工具");
        //电话归属地查询方法
        initPhoneAddress();
        //短信备份方法
        initSmsBackUp();
        //常用号码查询
        initCommonNumberQuery();
        initAppLock();
    }
    private void initAppLock() {
        tv_app_lock = (TextView) findViewById(R.id.tv_app_lock);
        tv_app_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AppLockActivity.class));
            }
        });
    }

    private void initCommonNumberQuery() {
        tv_commonnumber_query = (TextView) findViewById(R.id.tv_commonnumber_query);
        tv_commonnumber_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CommonNumberQueryActivity.class));
            }
        });
    }

    /**
     * 短信备份
     */
    private void initSmsBackUp() {
        tv_sms_backup = (TextView) findViewById(R.id.tv_sms_backup);
        tv_sms_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSmsBackUpDialog();
            }
        });
    }

    /**
     * 显示备份进度条对话框
     */
    private void showSmsBackUpDialog() {
        //1,创建一个带进度条的对话框
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setTitle("短信备份");
        //2,指定进度条的样式为水平
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //3,展示进度条
        progressDialog.show();
        //4,直接调用备份短信方法即可
        new Thread(){
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"sms74.xml";
                SmsBackUp.backup(getApplicationContext(), path, new SmsBackUp.CallBack() {
                    @Override
                    public void setProgress(int index) {
                        //由开发者自己决定,使用对话框还是进度条
                        progressDialog.setProgress(index);
                    }

                    @Override
                    public void setMax(int max) {
                        //由开发者自己决定,使用对话框还是进度条
                        progressDialog.setMax(max);
                    }
                });

                progressDialog.dismiss();
            }
        }.start();
    }

    private void initPhoneAddress() {
        tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);
        tv_query_phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QueryAddressActivity.class));
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_atool;
    }
}
