package com.zaki.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.utils.ActivityCollector;

/**
 * Created by zaki on 2016/8/31.
 */
public class Setup1Activity extends BaseAppCompatActivity implements View.OnClickListener{
    private Button bt_next;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("1.欢迎使用手机防盗");
        bt_next = (Button)findViewById(R.id.bt_next);

        if (bt_next != null) {
            bt_next.setOnClickListener(this);
        }
        ActivityCollector.addActivity(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setup_1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                nextStep();
                break;
        }

    }

    /**
     * 下一步
     */
    private void nextStep() {
        Intent intent = new Intent(Setup1Activity.this,Setup2Activity.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
