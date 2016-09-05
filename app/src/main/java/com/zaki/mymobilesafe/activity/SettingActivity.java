package com.zaki.mymobilesafe.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.utils.ConstantValue;
import com.zaki.mymobilesafe.utils.SharedPreUtil;
import com.zaki.mymobilesafe.view.SettingItem;

/**
 * Created by zaki on 2016/8/28.
 */
public class SettingActivity extends BaseAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("设置中心");
        initUpdate();
    }

    /**
     * 版本更新开关
     */
    private void initUpdate() {
        final SettingItem siv_update = (SettingItem) findViewById(R.id.siv_update);
        //获取已有的开关状态,用作显示
        boolean open_update = SharedPreUtil.getBoolean(this, ConstantValue.UPDATE_OPEN, false);
        //是否选中,根据上一次存储的结果去做决定
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前是选中的,点击过后,变成未选中
                //如果之前是未选中的,点击过后,变成选中

                //获取之前的选中状态
                boolean isCheck = siv_update.isCheck();
                //将原有状态取反,等同上诉的两部操作
                siv_update.setCheck(!isCheck);
                //将取反后的状态存储到相应sp中
                SharedPreUtil.putBoolean(getApplicationContext(), ConstantValue.UPDATE_OPEN,!isCheck);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }
}
