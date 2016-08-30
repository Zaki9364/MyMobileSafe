package com.zaki.mymobilesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;

/**
 * Created by zaki on 2016/8/30.
 */
public class SettingItem extends RelativeLayout {
    private String open;
    private String close;
    private TextView tv_title;
    private TextView tv_des;
    private CheckBox cb_box;
    public SettingItem(Context context) {
        this(context,null);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_item,this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.SettingItemAttrs);
        String title = ta.getString(R.styleable.SettingItemAttrs_myTitle);
        open = ta.getString(R.styleable.SettingItemAttrs_open);
        close = ta.getString(R.styleable.SettingItemAttrs_close);
        /*String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","myTitle");
        open = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","open");
        close = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","close");*/

        tv_title.setText(title);
        ta.recycle();
    }

    /**
     * 判断是否开启的方法
     * @return	返回当前SettingItemView是否选中状态	true开启(checkBox返回true)	false关闭(checkBox返回false)
     */
    public boolean isCheck(){
        //由checkBox的选中结果,决定当前条目是否开启
        return cb_box.isChecked();
    }

    /**
     * @param isCheck	作为是否开启的变量,由点击过程中去做传递
     */
    public void setCheck(boolean isCheck){
        //当前条目在选择的过程中,cb_box选中状态也在跟随(isCheck)变化
        cb_box.setChecked(isCheck);
        if(isCheck){
            //开启
            tv_des.setText(open);
        }else{
            //关闭
            tv_des.setText(close);
        }
    }
}
