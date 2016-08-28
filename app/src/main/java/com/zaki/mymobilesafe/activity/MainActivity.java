package com.zaki.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.adapter.HomeAdapter;

public class MainActivity extends BaseAppCompatActivity {

    private GridView gv_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("功能列表");
        initUI();
        gv_home.setAdapter(new HomeAdapter(this));
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        startActivity(new Intent(MainActivity.this,SettingActivity.class));
                        break;
                }
            }
        });
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_functions);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isShowBacking() {
        return false;
    }
}
