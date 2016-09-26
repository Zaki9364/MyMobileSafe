package com.zaki.mymobilesafe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaki on 2016/9/1.
 */
public class ContactListActivity extends BaseAppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    private ListView lv_contact;
    private List<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
    private ContactAdapter mAdapter;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //8,填充数据适配器
            mAdapter = new ContactAdapter(ContactListActivity.this, contactList);
            lv_contact.setAdapter(mAdapter);
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("选择联系人");
        initUI();
        checkMyPermission();
        initData();
    }

    /**
     * 检测手机联系人读取权限
     * 没有就申请权限
     */
    private void checkMyPermission() {
        if(ContextCompat.checkSelfPermission(ContactListActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ContactListActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    /**
     * 回调的权限请求结果
     * @param requestCode 请求码
     * @param permissions 权限
     * @param grantResults 请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initData();
                    // permission was granted, yay! Do the contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.

                }
            }

            // other 'case' lines to check for other permissions this app might request
        }
    }

    /**
     * 获取手机联系人数据
     */
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Uri contactUri =ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    Cursor cursor = ContactListActivity.this.getContentResolver().query(contactUri,
                            new String[]{"display_name", "sort_key", "contact_id","data1"},
                            null, null, "sort_key");
                    String contactName;
                    String contactNumber;
                    while (cursor.moveToNext()) {
                        HashMap<String,String> hashMap = new HashMap<>();
                        contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        hashMap.put("name", contactName);
                        hashMap.put("phone", contactNumber);
                        contactList.add(hashMap);
                        hashMap = null;
                    }
                    cursor.close();//使用完后一定要将cursor关闭，不然会造成内存泄露等问题

                }catch (Exception e){
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        //点击相应条目，获取该条目的电话号码，并将其返回给Setup3Activity
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String safe_number = contactList.get(position).get("phone");
                Intent intent = new Intent();
                intent.putExtra("safe_number",safe_number);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact;
    }
}
