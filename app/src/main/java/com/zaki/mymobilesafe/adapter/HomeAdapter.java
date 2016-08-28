package com.zaki.mymobilesafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;

/**
 * Created by zaki on 2016/8/28.
 */
public class HomeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    public HomeAdapter(Context context) {
        mInflater=LayoutInflater.from(context);
    }

    private String[] mTitleStr = new String[]{
            "手机防盗", "通信卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"
    };
    private int[] mDrawableIds = new int[]{
                R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
                R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings
        };

    @Override
    public int getCount() {
        return mTitleStr.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitleStr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.gridview_item,null);
        ImageView iv_icon = (ImageView)view.findViewById(R.id.iv_icon);
        TextView tv_function = (TextView)view.findViewById(R.id.tv_function);
        iv_icon.setImageResource(mDrawableIds[position]);
        tv_function.setText(mTitleStr[position]);
        return view;
    }
}
