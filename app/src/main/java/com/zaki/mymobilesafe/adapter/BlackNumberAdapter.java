package com.zaki.mymobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.db.dao.BlackNumberDao;
import com.zaki.mymobilesafe.db.javabean.BlackNumberInfo;

import java.util.List;

/**
 * Created by zaki on 2016/9/12.
 */
public class BlackNumberAdapter extends BaseAdapter {
    private List<BlackNumberInfo> blackNumberInfoList;
    private Context context;
    private BlackNumberDao mDao;

    public BlackNumberAdapter(Context context,List<BlackNumberInfo> blackNumberInfoList){
        this.blackNumberInfoList = blackNumberInfoList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return blackNumberInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return blackNumberInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mDao = BlackNumberDao.getInstance(context);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.listview_blacknumber_item,null);
            viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
            viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_phone.setText(blackNumberInfoList.get(position).getPhone());
        int mode = Integer.parseInt(blackNumberInfoList.get(position).getMode());
        switch (mode){
            case 1:
                viewHolder.tv_mode.setText("拦截短信");
                break;
            case 2:
                viewHolder.tv_mode.setText("拦截电话");
                break;
            case 3:
                viewHolder.tv_mode.setText("拦截所有");
                break;
        }
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1,数据库删除
                mDao.delete(blackNumberInfoList.get(position).getPhone());
                //2,集合中的删除
                blackNumberInfoList.remove(position);
                //3,通知数据适配器刷新
                notifyDataSetChanged();

            }
        });
        return convertView;
    }

    static class ViewHolder{
        public TextView tv_phone;
        public TextView tv_mode;
        public ImageView iv_delete;
    }
}
