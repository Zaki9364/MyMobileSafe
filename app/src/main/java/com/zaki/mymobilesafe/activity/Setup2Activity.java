package com.zaki.mymobilesafe.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.utils.ActivityCollector;
import com.zaki.mymobilesafe.utils.ConstantValue;
import com.zaki.mymobilesafe.utils.SharedPreUtil;
import com.zaki.mymobilesafe.view.SettingItem;

/**
 * Created by zaki on 2016/8/31.
 */
public class Setup2Activity extends BaseAppCompatActivity implements View.OnClickListener{
    private SettingItem siv_sim_bind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("2.手机卡绑定");
        initUI();
        ActivityCollector.addActivity(this);
    }
    private void initUI() {
        Button bt_next = (Button) findViewById(R.id.bt_next);
        if (bt_next != null) {
            bt_next.setOnClickListener(this);
        }
        siv_sim_bind  = (SettingItem)findViewById(R.id.siv_sim_bind);
        //回显（读取已有的绑定状态用于显示）
        String sim_number = SharedPreUtil.getString(this, ConstantValue.SIM_NUMBER,"");
        if(TextUtils.isEmpty(sim_number)){
            siv_sim_bind.setCheck(false);
        } else {
            siv_sim_bind.setCheck(true);
        }
        siv_sim_bind.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setup_2;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                nextStep();
                break;
            case R.id.siv_sim_bind:
                //获取原有的状态
                boolean isCheck = siv_sim_bind.isCheck();
                //点击之后将原有的状态取反设置给当前条目
                siv_sim_bind.setCheck(!isCheck);
                if(!isCheck){
                    //存储序列卡号
                    try {
                        //1.获取TelephonyManager对象
                        TelephonyManager manager = (TelephonyManager)
                                Setup2Activity.this.getSystemService(Context.TELEPHONY_SERVICE);
                        //2.获取SIM卡序列号
                        String serialNumber = manager.getSimSerialNumber();
                        //3.存储序列号
                        SharedPreUtil.putString(getApplicationContext(),ConstantValue.SIM_NUMBER,serialNumber);
                    }catch (Exception e){
                        Log.i("Setup2Activity",e.toString());
                    }
                } else {
                    //将存储SIM序列号的节点从sp中删除
                    SharedPreUtil.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }
                break;
        }
    }

    /**
     * （下一步）
     * 只有绑定了SIM卡才能进行下一步
     */
    private void nextStep() {
        String serialNumber = SharedPreUtil.getString(getApplicationContext(),ConstantValue.SIM_NUMBER,"");
        if(TextUtils.isEmpty(serialNumber)){
            Toast.makeText(Setup2Activity.this,"亲，绑定SIM卡才能进行下一步哦",Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(Setup2Activity.this,Setup3Activity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
