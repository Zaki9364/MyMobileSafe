package com.zaki.mymobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.db.dao.AppLockDao;
import com.zaki.mymobilesafe.db.javabean.AppInfo;

import java.util.List;

/**
 * Created by zaki on 2016/9/18.
 */
public class IsLockAdapter extends BaseAdapter {
    private Context context;
    private List<AppInfo> mLockList,mUnLockList;
    private boolean isLock;
    private TextView tv_unlock,tv_lock;
    private TranslateAnimation mTranslateAnimation;
    private AppLockDao mDao;
    public IsLockAdapter(Context context, List<AppInfo> mLockList,List<AppInfo> mUnLockList,boolean isLock,TextView tv_lock,TextView tv_unlock) {
        this.context = context;
        this.mLockList = mLockList;
        this.mUnLockList = mUnLockList;
        this.isLock = isLock;
        this.tv_lock = tv_lock;
        this.tv_unlock = tv_unlock;
        mDao =AppLockDao.getInstance(context);
        initAnimation();
    }
    /**
     * 初始化平移动画的方法(平移自身的一个宽度大小)
     */
    private void initAnimation() {
        mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        mTranslateAnimation.setDuration(500);
    }

    @Override
    public int getCount() {
        if(isLock){
            tv_lock.setText("已加锁应用:"+mLockList.size());
            return mLockList.size();
        }else {
            tv_unlock.setText("未加锁应用:"+mUnLockList.size());
            return mUnLockList.size();
        }
    }

    @Override
    public AppInfo getItem(int position) {
        if(isLock){
            return mLockList.get(position);
        }else {
            return mUnLockList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.listview_islock_item,null);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_lock);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AppInfo appInfo = getItem(position);
        final View animationView = convertView;

        holder.iv_icon.setBackgroundDrawable(appInfo.getIcon());
        holder.tv_name.setText(appInfo.getName());
        if(isLock){
            holder.iv_lock.setBackgroundResource(R.drawable.lock);
        }else{
            holder.iv_lock.setBackgroundResource(R.drawable.unlock);
        }

        holder.iv_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加动画效果,动画默认是非阻塞的,所以执行动画的同时,动画以下的代码也会执行
                animationView.startAnimation(mTranslateAnimation);//500毫秒
                //对动画执行过程做事件监听,监听到动画执行完成后,再去移除集合中的数据,操作数据库,刷新界面
                mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //动画开始的是调用方法
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        //动画重复时候调用方法
                    }
                    //动画执行结束后调用方法
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(isLock){
                            //已加锁------>未加锁过程
                            //1.已加锁集合删除一个,未加锁集合添加一个,对象就是getItem方法获取的对象
                            mLockList.remove(appInfo);
                            mUnLockList.add(0,appInfo);
                            //2.从已加锁的数据库中删除一条数据
                            mDao.delete(appInfo.getPackageName());
                            //3.刷新数据适配器
                            notifyDataSetChanged();
                        }else{
                            //未加锁------>已加锁过程
                            //1.已加锁集合添加一个,未加锁集合移除一个,对象就是getItem方法获取的对象
                            mLockList.add(0,appInfo);
                            mUnLockList.remove(appInfo);
                            //2.从已加锁的数据库中插入一条数据
                            mDao.insert(appInfo.getPackageName());
                            //3.刷新数据适配器
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        return convertView;
    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_lock;
    }
}
