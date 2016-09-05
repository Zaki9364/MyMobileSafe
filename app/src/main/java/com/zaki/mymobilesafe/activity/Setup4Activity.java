package com.zaki.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.utils.ActivityCollector;
import com.zaki.mymobilesafe.utils.ConstantValue;
import com.zaki.mymobilesafe.utils.SharedPreUtil;

/**
 * Created by zaki on 2016/9/1.
 */
public class Setup4Activity extends BaseAppCompatActivity implements View.OnClickListener{
    private Button btn_setup_over;
    private CheckBox cb_setup_4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("4.恭喜您设置完成");
        initUI();
        ActivityCollector.addActivity(this);
    }

    private void initUI() {
        btn_setup_over = (Button)findViewById(R.id.bt_setup_over);
        cb_setup_4 = (CheckBox) findViewById(R.id.cb_setup_4);
        if (btn_setup_over != null) {
            btn_setup_over.setOnClickListener(this);
        }
        boolean open_security = SharedPreUtil.getBoolean(getApplicationContext(),ConstantValue.OPEN_SECURITY,false);
        if(open_security){
            cb_setup_4.setText("您已开启防盗保护");
            cb_setup_4.setChecked(true);
        }else {
            cb_setup_4.setText("您未开启防盗保护");
            cb_setup_4.setChecked(false);
        }
        cb_setup_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreUtil.putBoolean(getApplicationContext(),ConstantValue.OPEN_SECURITY,isChecked);
                if(isChecked){
                    cb_setup_4.setText("您已开启防盗保护");
                }else {
                    cb_setup_4.setText("您未开启防盗保护");
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setup_4;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_setup_over:
                setupOver();
        }
    }

    private void setupOver() {
        boolean open_security = SharedPreUtil.getBoolean(getApplicationContext(),ConstantValue.OPEN_SECURITY,false);
        if (open_security){
            Intent intent = new Intent(Setup4Activity.this,SetupOverActivity.class);
            startActivity(intent);
            ActivityCollector.finishAll();
            SharedPreUtil.putBoolean(this, ConstantValue.SETUP_OVER,true);
        }else {
            Toast.makeText(Setup4Activity.this,"亲，请开启手机防盗",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
