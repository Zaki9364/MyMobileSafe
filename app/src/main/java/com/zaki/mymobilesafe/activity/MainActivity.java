package com.zaki.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.adapter.HomeAdapter;
import com.zaki.mymobilesafe.utils.ConstantValue;
import com.zaki.mymobilesafe.utils.SharedPreUtil;

public class MainActivity extends BaseAppCompatActivity {

    private GridView gv_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("功能列表");
        initUI();
    }

    /**
     * 弹出对话框
     */
    private void showMyDialog() {
        String psd = SharedPreUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(psd)) {
            //1.设置初始密码对话框
            showSetPsdDialog();
        } else {
            //2.设置确认密码对话框
            showConfirmPsdDialog();
        }
    }

    /**
     * 确认密码对话框
     */
    private void showConfirmPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_confirm_psd, null);
        dialog.setView(view);
        dialog.show();
        dialog.setCancelable(false);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        if (bt_cancel != null) {
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        if (bt_submit != null) {
            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);
                    String confirmPsd = et_confirm_psd.getText().toString();
                    if (!TextUtils.isEmpty(confirmPsd)) {
                        String psd = SharedPreUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, "");
                        if (psd.equals(confirmPsd)) {
                            //未完成导航界面设置，进入Setup1Activity
                            if (!SharedPreUtil.getBoolean(getApplicationContext(), ConstantValue.SETUP_OVER, false)) {
                                Intent intent = new Intent(MainActivity.this, Setup1Activity.class);
                                startActivity(intent);
                                dialog.dismiss();
                                //如果已经完成导航界面设置，则进入SetupOverActivity
                            } else {
                                Intent intent = new Intent(MainActivity.this, SetupOverActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                            et_confirm_psd.setText("");
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    /**
     * 设置初始密码对话框
     */
    private void showSetPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_psd, null);
        dialog.setView(view);
        dialog.show();
        dialog.setCancelable(false);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        if (bt_cancel != null) {
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        if (bt_submit != null) {
            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
                    EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);
                    String psd = et_set_psd.getText().toString();
                    String confirmPsd = et_confirm_psd.getText().toString();

                    if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)) {
                        if (psd.equals(confirmPsd)) {
                            Intent intent = new Intent(MainActivity.this, Setup1Activity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            SharedPreUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, psd);
                        } else {
                            Toast.makeText(MainActivity.this, "两次输入密码不一样！", Toast.LENGTH_SHORT).show();
                            et_set_psd.setText("");
                            et_confirm_psd.setText("");
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }


    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_functions);
        gv_home.setAdapter(new HomeAdapter(this));
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showMyDialog();
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, BlackNumberActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, AppManagerActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, ProcessManagerActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, TrafficActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, AnitVirusActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, CacheClearActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(MainActivity.this, AToolActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        break;
                }
            }
        });
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
