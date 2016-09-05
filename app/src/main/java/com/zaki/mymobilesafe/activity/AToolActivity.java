package com.zaki.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;

/**
 * Created by zaki on 2016/9/5.
 */
public class AToolActivity extends BaseAppCompatActivity {
    private TextView tv_query_phone_address;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("高级工具");
        //电话归属地查询方法
        initPhoneAddress();
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
