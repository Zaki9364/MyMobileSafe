package com.zaki.mymobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaki on 2016/9/1.
 */
public class ContactAdapter extends BaseAdapter {
    private List<HashMap<String, String>> contactList = new ArrayList<>();
    private Context context;

    public ContactAdapter(Context context, List<HashMap<String, String>> contactList) {
        this.contactList = contactList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.contact_item, null);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_phone_num = (TextView) convertView.findViewById(R.id.tv_phone_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(getItem(position).get("name"));
        viewHolder.tv_phone_num.setText(getItem(position).get("phone"));
        return convertView;
    }

    class ViewHolder {
        public TextView tv_name;
        public TextView tv_phone_num;
    }
}
