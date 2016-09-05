package com.zaki.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.utils.ConstantValue;
import com.zaki.mymobilesafe.utils.SharedPreUtil;

/**
 * Created by zaki on 2016/8/31.
 */
public class SetupOverActivity extends BaseAppCompatActivity {
    private TextView tv_phone;
    private Button bt_reset_setup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("手机防盗");
        initUI();
    }

    private void initUI() {
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        bt_reset_setup = (Button) findViewById(R.id.bt_reset_setup);
        String safe_phone = SharedPreUtil.getString(this,ConstantValue.CONTACT_PHONE,"");
        tv_phone.setText(safe_phone);
        bt_reset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupOverActivity.this,Setup1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setup_over;
    }
}
