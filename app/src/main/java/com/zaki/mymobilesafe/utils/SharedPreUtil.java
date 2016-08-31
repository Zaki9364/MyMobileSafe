package com.zaki.mymobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zaki on 2016/8/30.
 */
public class SharedPreUtil {
    private static SharedPreferences pref;

    /**
     * @param context 上下文环境
     * @param key 存储节点
     * @param value 存储的boolean值
     */
    public static void putBoolean(Context context, String key, boolean value){
        if(pref == null){
            pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        }
        pref.edit().putBoolean(key,value).commit();
    }

    /**
     * 从sp中读取boolean标示
     * @param context  上下文环境
     * @param key  存储节点
     * @param defvalue 找不到此节点的默认值
     * @return 默认值或节点的值
     */
    public static boolean getBoolean(Context context, String key, boolean defvalue){
        if (pref == null){
            pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        }
        return pref.getBoolean(key,defvalue);
    }
    /**
     * @param context 上下文环境
     * @param key 存储节点
     * @param value 存储的String值
     */
    public static void putString(Context context, String key, String value){
        if(pref == null){
            pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        }
        pref.edit().putString(key,value).commit();
    }

    /**
     * 从sp中读取String
     * @param context  上下文环境
     * @param key  存储节点
     * @param defvalue 找不到此节点的默认值
     * @return 默认值或节点的值
     */
    public static String getString(Context context, String key, String defvalue){
        if (pref == null){
            pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        }
        return pref.getString(key,defvalue);
    }
}
