package com.zaki.mymobilesafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.engine.AddressDao;

/**
 * Created by zaki on 2016/9/5.
 */
public class QueryAddressActivity extends BaseAppCompatActivity {
    private EditText et_phone;
    private Button bt_query;
    private TextView tv_query_result;
    private String mAddress;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //4,控件使用查询结果
            tv_query_result.setText(mAddress);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("手机号码归属地查询");
        initUI();
    }

    private void initUI() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        bt_query = (Button) findViewById(R.id.bt_query);
        tv_query_result = (TextView) findViewById(R.id.tv_query_result);

        //1,点查询功能,注册按钮的点击事件
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    //2,查询是耗时操作,开启子线程
                    query(phone);
                } else {
                    //抖动
                    Animation shake = AnimationUtils.loadAnimation(
                            getApplicationContext(), R.anim.shake);
                    //手机震动
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);

                    //interpolator插补器,数学函数
                    //自定义插补器
//					shake.setInterpolator(new Interpolator() {
//						//y = 2x+1
//						@Override
//						public float getInterpolation(float arg0) {
//							return 0;
//						}
//					});

//					Interpolator
//					CycleInterpolator

                    et_phone.startAnimation(shake);
                }

            }
        });

        //5,实时查询(监听输入框文本变化)
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = et_phone.getText().toString();
                query(phone);
            }
        });
    }

    /**
     * 耗时操作
     * 获取电话号码归属地
     *
     * @param phone 查询电话号码
     */
    protected void query(final String phone) {
        new Thread() {
            public void run() {
                mAddress = AddressDao.getAddress(phone);
                //3,消息机制,告知主线程查询结束,可以去使用查询结果
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_query_address;
    }
}
