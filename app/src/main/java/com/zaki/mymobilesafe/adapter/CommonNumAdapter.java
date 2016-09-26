package com.zaki.mymobilesafe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.engine.CommonnumDao;

import java.util.List;

/**
 * Created by zaki on 2016/9/17.
 */
public class CommonNumAdapter extends BaseExpandableListAdapter {
    private List<CommonnumDao.Group> mGroup;
    private Context context;
    public CommonNumAdapter(Context context,List<CommonnumDao.Group> mGroup){
        this.mGroup = mGroup;
        this.context = context;
    }
    @Override
    public int getGroupCount() {
        return mGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroup.get(groupPosition).childList.size();
    }

    @Override
    public CommonnumDao.Group getGroup(int groupPosition) {
        return mGroup.get(groupPosition);
    }

    @Override
    public CommonnumDao.Child getChild(int groupPosition, int childPosition) {
        return mGroup.get(groupPosition).childList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setText("			 "+getGroup(groupPosition).name);
        textView.setTextColor(Color.RED);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.elv_child_item, null);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_number = (TextView) view.findViewById(R.id.tv_number);

        tv_name.setText(getChild(groupPosition, childPosition).name);
        tv_number.setText(getChild(groupPosition, childPosition).number);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
