package com.zaki.mymobilesafe.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.db.javabean.ProcessInfo;
import com.zaki.mymobilesafe.utils.ConstantValue;
import com.zaki.mymobilesafe.utils.SharedPreUtil;

import java.util.List;

/**
 * Created by zaki on 2016/9/16.
 */
public class ProcessAdapter extends BaseAdapter {
    private List<ProcessInfo> mSystemList;
    private List<ProcessInfo> mCustomerList;
    private Context context;

    public ProcessAdapter(Context context,List<ProcessInfo> mSystemList,List<ProcessInfo> mCustomerList){
        this.context = context;
        this.mSystemList = mSystemList;
        this.mCustomerList = mCustomerList;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 || position == mCustomerList.size()+1){
            //返回0,代表纯文本条目的状态码
            return 0;
        }else{
            //返回1,代表图片+文本条目状态码
            return 1;
        }
    }

    @Override
    public int getCount() {
        if(SharedPreUtil.getBoolean(context, ConstantValue.SHOW_SYSTEM, false)){
            return mCustomerList.size()+mSystemList.size()+2;
        }else{
            return mCustomerList.size()+1;
        }
    }

    @Override
    public ProcessInfo getItem(int position) {
        if(position == 0 || position == mCustomerList.size()+1){
            return null;
        }else{
            if(position<mCustomerList.size()+1){
                return mCustomerList.get(position-1);
            }else{
                //返回系统进程对应条目的对象
                return mSystemList.get(position - mCustomerList.size()-2);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        if(type == 0){
            //展示灰色纯文本条目
            ViewTitleHolder holder;
            if(convertView == null){
                convertView = View.inflate(context, R.layout.listview_app_item_title, null);
                holder = new ViewTitleHolder();
                holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else{
                holder = (ViewTitleHolder) convertView.getTag();
            }
            if(position == 0){
                holder.tv_title.setText("用户进程("+mCustomerList.size()+")");
            }else{
                holder.tv_title.setText("系统进程("+mSystemList.size()+")");
            }
            return convertView;
        }else{
            //展示图片+文字条目
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(context, R.layout.listview_process_item, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
                holder.tv_memory_info = (TextView) convertView.findViewById(R.id.tv_memory_info);
                holder.cb_box = (CheckBox) convertView.findViewById(R.id.cb_box);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_icon.setBackgroundDrawable(getItem(position).icon);
            holder.tv_name.setText(getItem(position).name);
            String strSize = Formatter.formatFileSize(context, getItem(position).memSize);
            holder.tv_memory_info.setText(strSize);

            //本进程不能被选中,所以先将checkbox隐藏掉
            if(getItem(position).packageName.equals(context.getPackageName())){
                holder.cb_box.setVisibility(View.GONE);
            }else{
                holder.cb_box.setVisibility(View.VISIBLE);
            }

            holder.cb_box.setChecked(getItem(position).isCheck);

            return convertView;
        }
    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_memory_info;
        CheckBox cb_box;
    }

    static class ViewTitleHolder{
        TextView tv_title;
    }
}
