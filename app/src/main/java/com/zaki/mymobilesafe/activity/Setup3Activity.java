package com.zaki.mymobilesafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zaki.mymobilesafe.R;

/**
 * Created by zaki on 2016/8/31.
 */
public class Setup3Activity extends BaseAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("设置安全号码");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setup_3;
    }
}
