package com.zaki.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zaki.mymobilesafe.R;

/**
 * Created by zaki on 2016/8/31.
 */
public class Setup2Activity extends BaseAppCompatActivity implements View.OnClickListener{
    private Button bt_next;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("2.手机卡绑定");
        bt_next = (Button)findViewById(R.id.bt_next);
        if (bt_next != null) {
            bt_next.setOnClickListener(this);
        }
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

        }
    }

    private void nextStep() {
        Intent intent = new Intent(Setup2Activity.this,Setup3Activity.class);
        startActivity(intent);
    }
}
