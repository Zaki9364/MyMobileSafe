package com.zaki.mymobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.zaki.mymobilesafe.utils.ConstantValue;
import com.zaki.mymobilesafe.utils.SharedPreUtil;

/**
 * Created by zaki on 2016/9/3.
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            Log .i(TAG,"重启开机++++++++");
            //1,获取开机后手机的sim卡的序列号
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = tm.getSimSerialNumber();
            //2,sp中存储的序列卡号
            String sim_number = SharedPreUtil.getString(context, ConstantValue.SIM_NUMBER, "");
            //3,比对不一致
            if(!simSerialNumber.equals(sim_number+"a")){
                //4,发送短信给选中联系人号码
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage("18328504774", null, "sim change!!!", null, null);
            }
        }else  if("android.intent.action.MEDIA_MOUNTED".equals(intent.getAction())){
            Log .i(TAG,"SD卡挂载成功++++++++");
        }else if("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())){
            Log .i(TAG,"SD卡被弹出++++++++");
        }else if("android.intent.action.MEDIA_REMOVED".equals(intent.getAction())){
            Log .i(TAG,"SD卡被拔出++++++++");
        }

    }
}
