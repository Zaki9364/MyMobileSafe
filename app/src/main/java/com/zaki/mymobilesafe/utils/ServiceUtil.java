package com.zaki.mymobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by zaki on 2016/9/6.
 */
public class ServiceUtil {
    public static boolean isRunning(Context context, String serviceName) {
        //1,获取activityMananger管理者对象,可以去获取当前手机正在运行的所有服务
        ActivityManager mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //2,获取手机中正在运行的服务集合(多少个服务)
        List<ActivityManager.RunningServiceInfo> runningServices = mAM.getRunningServices(1000);
        //3,遍历获取的所有的服务集合,拿到每一个服务的类的名称,和传递进来的类的名称作比对,如果一致,说明服务正在运行
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            //4,获取每一个真正运行服务的名称
            if (runningServiceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
