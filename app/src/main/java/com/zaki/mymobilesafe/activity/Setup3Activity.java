package com.zaki.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.utils.ActivityCollector;
import com.zaki.mymobilesafe.utils.ConstantValue;
import com.zaki.mymobilesafe.utils.SharedPreUtil;

/**
 * Created by zaki on 2016/8/31.
 */
public class Setup3Activity extends BaseAppCompatActivity implements View.OnClickListener {
    private Button bt_next;
    private Button bt_choose_phone_num;
    private EditText et_phone_num;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("设置安全号码");
        initUI();
        ActivityCollector.addActivity(this);
    }

    private void initUI() {
        bt_next = (Button)findViewById(R.id.bt_next);
        bt_choose_phone_num = (Button) findViewById(R.id.bt_choose_phone_num);
        et_phone_num = (EditText) findViewById(R.id.et_phone_num);
        String phone = SharedPreUtil.getString(this,ConstantValue.CONTACT_PHONE,"");
        et_phone_num.setText(phone);
        bt_next.setOnClickListener(this);
        bt_choose_phone_num.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setup_3;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                nextStep();
                break;
            case R.id.bt_choose_phone_num:
                chooseContact();
                break;
        }

    }

    private void chooseContact() {
        Intent intent = new Intent(Setup3Activity.this,ContactListActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回到当前界面的时候，接收结果的方法
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    String safe_number = data.getStringExtra("safe_number");
                    et_phone_num.setText(safe_number);
                    //存储联系人
                    SharedPreUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE,safe_number);
                }
        }
    }

    private void nextStep() {
        if (TextUtils.isEmpty(et_phone_num.getText().toString())) {
            Toast.makeText(Setup3Activity.this, "亲，输入安全号码才能进行下一步哦", Toast.LENGTH_SHORT).show();
        } else {
            //判断是否是手机号码
            if (et_phone_num.getText().toString().matches(ConstantValue.TEL_REGEX)) {
                Intent intent = new Intent(Setup3Activity.this, Setup4Activity.class);
                startActivity(intent);
                SharedPreUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE,et_phone_num.getText().toString());
            } else {
                Toast.makeText(Setup3Activity.this, "亲，该号码不合法哦", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
