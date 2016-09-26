package com.zaki.mymobilesafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.adapter.BlackNumberAdapter;
import com.zaki.mymobilesafe.db.dao.BlackNumberDao;
import com.zaki.mymobilesafe.db.javabean.BlackNumberInfo;

import java.util.List;

/**
 * Created by zaki on 2016/9/12.
 */
public class BlackNumberActivity extends BaseAppCompatActivity {
    private ListView mListView;
    private BlackNumberDao mDao;
    private List<BlackNumberInfo> mBlackNumberList;
    private int mCount;
    private BlackNumberAdapter mBlackNumberAdapter;
    private int mode = 1;
    private boolean mIsLoad = false;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (mBlackNumberAdapter == null) {
                mBlackNumberAdapter = new BlackNumberAdapter(BlackNumberActivity.this, mBlackNumberList);
                mListView.setAdapter(mBlackNumberAdapter);
            } else {
                mBlackNumberAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("黑名单管理");
        getSubTitle().setText("添加");
        initUI();
        initData();
    }

    private void initData() {
        //获取数据库中所有电话号码
        new Thread() {
            @Override
            public void run() {
                //1,获取操作黑名单数据库的对象
                mDao = BlackNumberDao.getInstance(getApplicationContext());
                //2,查询部分数据
                mBlackNumberList = mDao.find(0);
                mCount = mDao.getCount();
                //3,通过消息机制告知主线程可以去使用包含数据的集合
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        getSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mListView = (ListView) findViewById(R.id.lv_blacknumber);
        //监听其滚动状态
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            //滚动过程中,状态发生改变调用方法()
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_FLING	飞速滚动
                //OnScrollListener.SCROLL_STATE_IDLE	 空闲状态
                //OnScrollListener.SCROLL_STATE_TOUCH_SCROLL	拿手触摸着去滚动状态
                if(mBlackNumberList!=null){
                    //条件一:滚动到停止状态
                    //条件二:最后一个条目可见(最后一个条目的索引值>=数据适配器中集合的总条目个数-1)
                    if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && mListView.getLastVisiblePosition()>=mBlackNumberList.size()-1
                            && !mIsLoad){
						/*mIsLoad防止重复加载的变量
						如果当前正在加载mIsLoad就会为true,本次加载完毕后,再将mIsLoad改为false
						如果下一次加载需要去做执行的时候,会判断上诉mIsLoad变量,是否为false,如果为true,就需要等待上一次加载完成,将其值
						改为false后再去加载*/

                        //如果条目总数大于集合大小的时,才可以去继续加载更多
                        if(mCount>mBlackNumberList.size()){
                            //加载下一页数据
                            new Thread(){
                                public void run() {
                                    //1,获取操作黑名单数据库的对象
                                    mDao = BlackNumberDao.getInstance(getApplicationContext());
                                    //2,查询部分数据
                                    List<BlackNumberInfo> moreData = mDao.find(mBlackNumberList.size());
                                    //3,添加下一页数据的过程
                                    mBlackNumberList.addAll(moreData);
                                    //4,通知数据适配器刷新
                                    mHandler.sendEmptyMessage(0);
                                }
                            }.start();
                        }
                    }
                }
            }
            //滚动过程中调用方法
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 弹出添加黑名单对话框
     */
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_add_blacknumber, null);
        dialog.setView(view);
        dialog.show();
        dialog.setCancelable(true);
        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        //监听其选中条目的切换过程
        if (rg_group != null) {
            rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rb_sms:
                            //拦截短信
                            mode = 1;
                            break;
                        case R.id.rb_phone:
                            //拦截电话
                            mode = 2;
                            break;
                        case R.id.rb_all:
                            //拦截所有
                            mode = 3;
                            break;
                    }
                }
            });
        }

        if (bt_submit != null) {
            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1,获取输入框中的电话号码
                    String phone = et_phone.getText().toString();
                    if (!TextUtils.isEmpty(phone)) {
                        //2,数据库插入当前输入的拦截电话号码
                        mDao.insert(phone, mode + "");
                        //3,让数据库和集合保持同步(1.数据库中数据重新读一遍,2.手动向集合中添加一个对象(插入数据构建的对象))
                        BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                        blackNumberInfo.setPhone(phone);
                        blackNumberInfo.setMode(mode + "");
                        //4,将对象插入到集合的最顶部
                        mBlackNumberList.add(0, blackNumberInfo);
                        //5,通知数据适配器刷新(数据适配器中的数据有改变了)
                        if (mBlackNumberAdapter != null) {
                            mBlackNumberAdapter.notifyDataSetChanged();
                        }
                        //6,隐藏对话框
                        dialog.dismiss();
                    } else {
                        Toast.makeText(BlackNumberActivity.this, "请输入拦截号码", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (bt_cancel != null) {
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        dialog.show();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_blacknumber;
    }
}
