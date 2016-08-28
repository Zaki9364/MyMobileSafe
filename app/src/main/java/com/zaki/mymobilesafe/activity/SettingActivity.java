package com.zaki.mymobilesafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zaki.mymobilesafe.R;

/**
 * Created by zaki on 2016/8/28.
 */
public class SettingActivity extends BaseAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("设置中心");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }
}
