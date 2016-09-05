package com.zaki.mymobilesafe.service;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by zaki on 2016/9/4.
 */
public class LocationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("++++++++++++++++", "22222222222222222");
        //获取手机的经纬度坐标
        //1,获取位置管理者对象
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2,以最优的方式获取经纬度坐标()
        Criteria criteria = new Criteria();
        //允许花费
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//指定获取经纬度的精确度
        String bestProvider = lm.getBestProvider(criteria, true);

        Log.i("++++++++++++++++", "3333333333333");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    101);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //3,在一定时间间隔,移动一定距离后获取经纬度坐标
        lm.requestLocationUpdates(bestProvider, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("++++++++++++++++", "555555555555555");
                //经度
                double longitude = location.getLongitude();
                Log.i("++++++++++++++++", "666666666666666");
                //纬度
                double latitude = location.getLatitude();
                Log.i("++++++++++++++++", "7777777777777777");

                //4,发送短信(添加权限)
                SmsManager sms = SmsManager.getDefault();
                Log.i("++++++++++++++++", "8888888888888888");

                sms.sendTextMessage("18328504774", null, "longitude = " + longitude + ",latitude = " + latitude, null, null);
                Log.i("++++++++++++++++", "longitude = " + longitude + ",latitude = " + latitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        Log.i("++++++++++++++++", "44444444444444444");
    }
    /*class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.i("++++++++++++++++","555555555555555");
            //经度
            double longitude = location.getLongitude();
            Log.i("++++++++++++++++","666666666666666");
            //纬度
            double latitude = location.getLatitude();
            Log.i("++++++++++++++++","7777777777777777");

            //4,发送短信(添加权限)
            SmsManager sms = SmsManager.getDefault();
            Log.i("++++++++++++++++","8888888888888888");

            sms.sendTextMessage("18328504774", null, "longitude = "+longitude+",latitude = "+latitude, null, null);
            Log.i("++++++++++++++++","longitude = "+longitude+",latitude = "+latitude);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
