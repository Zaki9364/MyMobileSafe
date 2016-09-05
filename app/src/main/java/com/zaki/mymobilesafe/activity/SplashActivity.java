package com.zaki.mymobilesafe.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.utils.ConstantValue;
import com.zaki.mymobilesafe.utils.SharedPreUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by zaki on 2016/8/26.
 */
public class SplashActivity extends AppCompatActivity {
    private TextView tv_version_name;
    //本地版本号
    private int mLocalVersionCode;
    //volley请求队列
    public static RequestQueue mRequestQueue;
    //版本名称
    private String versionName;
    //版本号
    private String versionCode;
    //版本描述
    private String versionDes;
    //下载apk地址
    private String downloadUrl;
    //请求数据开始的时间
    private long startTime;
    //请求数据结束的时间
    private long endTime;
    private String TAG = "SplashActivity";
    private String url = "http://bmob-cdn-5912.b0.upaiyun.com/2016/08/29/5847efba4080408480a36a0a8334fe70.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mRequestQueue = Volley.newRequestQueue(this);
        //初始化UI
        initUI();
        //初始化数据
        initData();
        //初始化数据库
        initDB();
    }
    private void initDB() {
        //1,归属地数据拷贝过程
        initAddressDB("address.db");
        Log.i("++++++++++++++++++","22222222222222222");
    }

    /**
     * 拷贝数据库值files文件夹下
     * @param dbName	数据库名称
     */
    private void initAddressDB(String dbName) {
        //1,在files文件夹下创建同名dbName数据库文件过程
        Log.i("++++++++++++++++++","3333333333333333333");
        File files = getFilesDir();
        File file = new File(files, dbName);
        if(file.exists()){
            Log.i("++++++++++++++++++",file.getPath());
            return;
        }
        Log.i("++++++++++++++++++","5555555555555555");
        InputStream stream = null;
        FileOutputStream fos = null;
        //2,输入流读取第三方资产目录下的文件
        try {
            stream = getAssets().open(dbName);
            //3,将读取的内容写入到指定文件夹的文件中去
            fos = new FileOutputStream(file);
            //4,每次的读取内容大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while( (temp = stream.read(bs))!=-1){
                fos.write(bs, 0, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(stream!=null && fos!=null){
                try {
                    stream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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
        Log.i(TAG,"mLocalVersionCode"+mLocalVersionCode);
        //获取服务器端版本号（客户端发请求，服务端给响应）
        if(SharedPreUtil.getBoolean(this, ConstantValue.UPDATE_OPEN,false)){
            checkVersion(url);
        }else {
            enterHome();
        }
    }

    /**
     * 检测服务端的版本号
     * @param url 请求地址
     */
    private void checkVersion(String url) {
        startTime = System.currentTimeMillis();
        Log.i(TAG,"startTime"+startTime);
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
                    }else {
                        enterHomeDelayed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG,"JSON解析出错");
                    enterHomeDelayed();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, volleyError.toString());
                enterHomeDelayed();
            }
        }) {//解决Json乱码
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
        endTime = System.currentTimeMillis();
        Log.i(TAG,"endTime"+endTime);
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
                enterHome();
            }
        });
        builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,"isWifiConnected");
                if(isWifiConnected(SplashActivity.this)){
                    downloadApk();
                    Log.i(TAG,"Sure downloadApk");
                } else{
                    showIsContinueDialog();
                    Log.i(TAG,"showIsContinueDialog");
                }

            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 下载新版本Apk
     */
    private void downloadApk() {
        //1判断sd卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //2获取sd卡路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "mymobilesafe.apk";
            //3发送请求，获取apk，放置到指定位置
            HttpUtils httpUtils = new HttpUtils();
            //发送请求传递参数(下载地址，放置的路径，回调方法)
            httpUtils.download("http://bmob-cdn-5912.b0.upaiyun.com/2016/08/27/1357d73c40ba99b1800da303eb3e0083.apk",
                    path, new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            //下载成功
                            Log.i(TAG, "下载完成");
                            File file = responseInfo.result;
                            installApk(file);
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            //下载失败
                            Log.i(TAG, "下载失败");
                            Toast.makeText(SplashActivity.this,"下载失败，请检查网络后重试。",Toast.LENGTH_SHORT).show();
                            enterHome();
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            //下载开始
                            Toast.makeText(SplashActivity.this,"下载开始",Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "下载开始");
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            super.onLoading(total, current, isUploading);
                            //下载过程中（总共大小，当前下载大小，是否在下载）
                            Log.i(TAG, "总共大小 = " + total);
                            Log.i(TAG, "当前下载 = " + current);
                            tv_version_name.setText("已经下载 "+100*current/total+"%");

                        }
                    });
        }

    }

    /**
     * 弹出是否继续下载对话框
     */
    private void showIsContinueDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("提醒");
        builder.setMessage("检测到当前没有wifi连接，继续下载可能消耗数据流量，是否继续？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 安装新版本Apk
     * @param file    需要安装的apk
     */
    private void installApk(File file) {
        //系统应用界面,源码,安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
		/*//文件作为数据源
		intent.setData(Uri.fromFile(file));
		//设置安装的类型
		intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//		startActivity(intent);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 延时进入主界面
     */
    private void enterHomeDelayed() {
        if (endTime - startTime > 3000) {
            enterHome();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterHome();
                }
            }, 3000-(endTime-startTime));
        }
    }

    /**
     * 直接进入主界面
     */
    private void enterHome(){
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
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

    /**
     * 判断wifi是否连接
     * @param context
     * @return true 代表连接上
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }

}
