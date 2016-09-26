package com.zaki.mymobilesafe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ExpandableListView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.adapter.CommonNumAdapter;
import com.zaki.mymobilesafe.engine.CommonnumDao;

import java.util.List;

/**
 * Created by zaki on 2016/9/17.
 */
public class CommonNumberQueryActivity extends BaseAppCompatActivity {
    private ExpandableListView elv_common_number;
    private List<CommonnumDao.Group> mGroup;
    private CommonNumAdapter mAdapter;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("常用号码查询");
        initUI();
        //检测权限
        checkMyPermission();
        initData();
    }

    private void checkMyPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CommonNumberQueryActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }

    /**
     * 给可扩展ListView准备数据,并且填充
     */
    private void initData() {
        CommonnumDao commonnumDao = new CommonnumDao();
        mGroup = commonnumDao.getGroup();

        mAdapter = new CommonNumAdapter(CommonNumberQueryActivity.this, mGroup);
        elv_common_number.setAdapter(mAdapter);
        //给可扩展listview注册点击事件
        elv_common_number.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                startCall(mAdapter.getChild(groupPosition, childPosition).number);
                return false;
            }
        });
    }

    protected void startCall(String number) {
        //开启系统的打电话界面
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initData();
                    // permission was granted, yay! Do the contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.

                }
            }
        }
    }

    private void initUI() {
        elv_common_number = (ExpandableListView) findViewById(R.id.elv_common_number);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_number;
    }
}
