package com.zaki.mymobilesafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zaki.mymobilesafe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by zaki on 2016/8/26.
 */
public class SplashActivity extends AppCompatActivity {
    private TextView tv_version_name;
    private int mLocalVersionCode;
    public static RequestQueue mRequestQueue;
    private String versionName;
    private String versionCode;
    private String versionDes;
    private String downloadUrl;
    private String TAG = "SplashActivity";
    private String url = "http://bmob-cdn-5912.b0.upaiyun.com/2016/08/29/5847efba4080408480a36a0a8334fe70.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mRequestQueue = Volley.newRequestQueue(this);
        //初始化UI
        initUI();
        //初始化数据
        initData();

    }

    /**
     * 初始化控件
     */
    private void initUI() {

        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //1.应用版本名称
        tv_version_name.setText("版本名称：" + getVersionName());
        //2.检测（本地版本号与服务器版本号进行比对）是否有更新，有就提示用户下载更新
        //获取本地版本号
        mLocalVersionCode = getVersionCode();
        //获取服务器端版本号（客户端发请求，服务端给响应）
        checkVersion(url);
    }

    /**
     * 检测服务端的版本号
     * @param url 请求地址
     */
    private void checkVersion(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    versionName = jsonObject.getString("versionName");
                    versionDes = jsonObject.getString("versionDes");
                    versionCode = jsonObject.getString("versionCode");
                    downloadUrl = jsonObject.getString("downloadUrl");
                    Log.i(TAG, versionName);
                    Log.i(TAG, versionDes);
                    Log.i(TAG, versionCode);
                    Log.i(TAG, downloadUrl);
                    if(mLocalVersionCode<Integer.parseInt(versionCode)){
                        showUpdateDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, volleyError.toString());
            }
        }) {
            protected Response<JSONObject> parseNetworkResponse(
                    NetworkResponse response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(response.data,
                            "UTF-8"));
                    return Response.success(jsonObject,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return Response.error(new ParseError(e));
                }
            }
        };
        mRequestQueue.add(jsonObjectRequest);
    }

    /**
     * 弹出对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("版本更新");
        builder.setMessage(versionDes);
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        });
        builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 获取版本号
     *
     * @return 非0代表获取成功，为0代表获取失败
     */
    private int getVersionCode() {
        //1.包管理者对象packageManager
        PackageManager pm = getPackageManager();
        try {
            //2.从包管理者对象中获取指定包名的基本信息（包括版本名称和版本号等）,传0
            PackageInfo mPackageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //3.获取版本号
            return mPackageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称，从清单文件中
     *
     * @return
     */
    private String getVersionName() {
        //1.包管理者对象packageManager
        PackageManager pm = getPackageManager();
        try {
            //2.从包管理者对象中获取指定包名的基本信息（包括版本名称和版本号等）,传0
            PackageInfo mPackageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //3.获取版本名称
            return mPackageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
