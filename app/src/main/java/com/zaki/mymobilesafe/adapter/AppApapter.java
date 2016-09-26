package com.zaki.mymobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.db.javabean.AppInfo;

import java.util.List;

/**
 * Created by zaki on 2016/9/14.
 */
public class AppApapter extends BaseAdapter {

    private Context context;
    private List<AppInfo> mSystemList;
    private List<AppInfo> mCustomerList;

    public AppApapter(Context context, List<AppInfo> mSystemList, List<AppInfo> mCustomerList) {
        this.context = context;
        this.mSystemList = mSystemList;
        this.mCustomerList = mCustomerList;
    }
    //获取数据适配器中条目类型的总数,修改成两种(纯文本,图片+文字)
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
        return mCustomerList.size()+mSystemList.size()+2;
    }

    @Override
    public AppInfo getItem(int position) {
        if(position == 0 || position == mCustomerList.size()+1){
            return null;
        }else{
            if(position<mCustomerList.size()+1){
                return mCustomerList.get(position-1);
            }else{
                //返回系统应用对应条目的对象
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
                holder.tv_title.setText("用户应用("+mCustomerList.size()+")");
            }else{
                holder.tv_title.setText("系统应用("+mSystemList.size()+")");
            }
            return convertView;
        } else {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.listview_app_item, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_path = (TextView) convertView.findViewById(R.id.tv_path);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_icon.setBackgroundDrawable(getItem(position).getIcon());
            holder.tv_name.setText(getItem(position).getName());
            if (getItem(position).isSdCard()) {
                holder.tv_path.setText("sd卡应用");
            } else {
                holder.tv_path.setText("手机应用");
            }
            return convertView;
        }

    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_path;
    }
    static class ViewTitleHolder{
        TextView tv_title;
    }
}
