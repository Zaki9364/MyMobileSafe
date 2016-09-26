package com.zaki.mymobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaki.mymobilesafe.R;
import com.zaki.mymobilesafe.adapter.ProcessAdapter;
import com.zaki.mymobilesafe.db.javabean.ProcessInfo;
import com.zaki.mymobilesafe.engine.ProcessInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 2016/9/16.
 */
public class ProcessManagerActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private TextView tv_process_count,tv_memory_info,tv_des;
    private ListView lv_process_list;
    private Button bt_select_all,bt_select_reverse,bt_clear,bt_setting;
    private int mProcessCount;
    private List<ProcessInfo> mProcessInfoList;

    private ArrayList<ProcessInfo> mSystemList;
    private ArrayList<ProcessInfo> mCustomerList;
    private ProcessInfo mProcessInfo;
    private long mAvailSpace;
    private String mStrTotalSpace;
    private ProcessAdapter mAdapter;
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mAdapter = new ProcessAdapter(ProcessManagerActivity.this,mSystemList,mCustomerList);
            lv_process_list.setAdapter(mAdapter);

            if(tv_des!=null && mCustomerList!=null){
                tv_des.setText("用户应用("+mCustomerList.size()+")");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTitle().setText("进程管理");
        int a = ProcessInfoProvider.getProcessCount(this);
        Log.i("ProcessManagerActivity",a+"");
        initUI();
        initTitleData();
        initListData();

    }

    private void initListData() {
        getData();
    }

    private void getData() {
        new Thread(){
            public void run() {
                mProcessInfoList = ProcessInfoProvider.getProcessInfo(getApplicationContext());
                mSystemList = new ArrayList<>();
                mCustomerList = new ArrayList<>();

                for (ProcessInfo info : mProcessInfoList) {
                    if(info.isSystem){
                        //系统进程
                        mSystemList.add(info);
                    }else{
                        //用户进程
                        mCustomerList.add(info);
                    }
                }
                mHandler.sendEmptyMessage(0);
            };
        }.start();
    }

    private void initTitleData() {
        mProcessCount = ProcessInfoProvider.getProcessCount(this);
        tv_process_count.setText("进程总数:"+mProcessCount);

        //获取可用内存大小,并且格式化
        mAvailSpace = ProcessInfoProvider.getAvailSpace(this);
        String strAvailSpace = Formatter.formatFileSize(this, mAvailSpace);

        //总运行内存大小,并且格式化
        long totalSpace = ProcessInfoProvider.getTotalSpace(this);
        mStrTotalSpace = Formatter.formatFileSize(this, totalSpace);

        tv_memory_info.setText("剩余："+strAvailSpace+"/"+"总共："+ mStrTotalSpace);
    }

    private void initUI() {
        tv_process_count = (TextView) findViewById(R.id.tv_process_count);
        tv_memory_info = (TextView) findViewById(R.id.tv_memory_info);

        tv_des = (TextView) findViewById(R.id.tv_des);

        lv_process_list = (ListView) findViewById(R.id.lv_process_list);

        bt_select_all = (Button) findViewById(R.id.bt_select_all);
        bt_select_reverse = (Button) findViewById(R.id.bt_select_reverse);
        bt_clear = (Button)  findViewById(R.id.bt_clear);
        bt_setting = (Button) findViewById(R.id.bt_setting);

        bt_select_all.setOnClickListener(this);
        bt_select_reverse.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_setting.setOnClickListener(this);

        lv_process_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //滚动过程中调用方法
                //AbsListView中view就是listView对象
                //firstVisibleItem第一个可见条目索引值
                //visibleItemCount当前一个屏幕的可见条目数
                //总共条目总数
                if(mCustomerList!=null && mSystemList!=null){
                    if(firstVisibleItem>=mCustomerList.size()+1){
                        //滚动到了系统条目
                        tv_des.setText("系统进程("+mSystemList.size()+")");
                    }else{
                        //滚动到了用户应用条目
                        tv_des.setText("用户进程("+mCustomerList.size()+")");
                    }
                }

            }
        });

        lv_process_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //view选中条目指向的view对象
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 || position == mCustomerList.size()+1){
                    return;
                }else{
                    if(position<mCustomerList.size()+1){
                        mProcessInfo = mCustomerList.get(position-1);
                    }else{
                        //返回系统应用对应条目的对象
                        mProcessInfo = mSystemList.get(position - mCustomerList.size()-2);
                    }
                    if(mProcessInfo!=null){
                        if(!mProcessInfo.packageName.equals(getPackageName())){
                            //选中条目指向的对象和本应用的包名不一致,才需要去状态取反和设置单选框状态
                            //状态取反
                            mProcessInfo.isCheck = !mProcessInfo.isCheck;
                            //checkbox显示状态切换
                            //通过选中条目的view对象,findViewById找到此条目指向的cb_box,然后切换其状态
                            CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
                            cb_box.setChecked(mProcessInfo.isCheck);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_select_all:
                selectAll();
                break;
            case R.id.bt_select_reverse:
                selectReverse();
                break;
            case R.id.bt_clear:
                clearAll();
                break;
            case R.id.bt_setting:
                setting();
                break;
        }
    }

    private void setting() {
        Intent intent = new Intent(ProcessManagerActivity.this,ProcessSettingActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (mAdapter != null){
                    mAdapter.notifyDataSetChanged();
                }
        }
    }

    /**
     * 清理选中进程
     */
    private void clearAll() {
        //1,获取选中进程
        //2,创建一个记录需要杀死的进程的集合
        List<ProcessInfo> killProcessList = new ArrayList<>();
        for(ProcessInfo processInfo:mCustomerList){
            if(processInfo.getPackageName().equals(getPackageName())){
                continue;
            }
            if(processInfo.isCheck){
                //不能在集合循环过程中去移除集合中的对象
//				mCustomerList.remove(processInfo);
                //3,记录需要杀死的用户进程
                killProcessList.add(processInfo);
            }
        }

        for(ProcessInfo processInfo:mSystemList){
            if(processInfo.isCheck){
                //4,记录需要杀死的系统进程
                killProcessList.add(processInfo);
            }
        }
        //5,循环遍历killProcessList,然后去移除mCustomerList和mSystemList中的对象
        long totalReleaseSpace = 0;
        for (ProcessInfo processInfo : killProcessList) {
            //6,判断当前进程在那个集合中,从所在集合中移除
            if(mCustomerList.contains(processInfo)){
                mCustomerList.remove(processInfo);
            }

            if(mSystemList.contains(processInfo)){
                mSystemList.remove(processInfo);
            }
            //7,杀死记录在killProcessList中的进程
            ProcessInfoProvider.killProcess(this,processInfo);

            //记录释放空间的总大小
            totalReleaseSpace += processInfo.memSize;
        }
        //8,在集合改变后,需要通知数据适配器刷新
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
        //9,进程总数的更新
        mProcessCount -= killProcessList.size();
        //10,更新可用剩余空间(释放空间+原有剩余空间 == 当前剩余空间)
        mAvailSpace += totalReleaseSpace;
        //11,根据进程总数和剩余空间大小
        tv_process_count.setText("进程总数:"+mProcessCount);
        tv_memory_info.setText("剩余/总共"+Formatter.formatFileSize(this, mAvailSpace)+"/"+mStrTotalSpace);
        //12,通过吐司告知用户,释放了多少空间,杀死了几个进程,
        String totalRelease = Formatter.formatFileSize(this, totalReleaseSpace);
//		ToastUtil.show(getApplicationContext(), "杀死了"+killProcessList.size()+"个进程,释放了"+totalRelease+"空间");

//		jni  java--c   c---java
        //占位符指定数据%d代表整数占位符,%s代表字符串占位符
        Toast.makeText(ProcessManagerActivity.this, String.format("杀死了%d进程,释放了%s空间", killProcessList.size(),totalRelease),Toast.LENGTH_SHORT).show();
    }

    private void selectReverse() {
        //1,将所有的集合中的对象上isCheck字段取反
        for(ProcessInfo processInfo:mCustomerList){
            if(processInfo.getPackageName().equals(getPackageName())){
                continue;
            }
            processInfo.isCheck = !processInfo.isCheck;
        }
        for(ProcessInfo processInfo:mSystemList){
            processInfo.isCheck = !processInfo.isCheck;
        }
        //2,通知数据适配器刷新
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    private void selectAll() {
        //1,将所有的集合中的对象上isCheck字段设置为true,代表全选,排除当前应用
        for(ProcessInfo processInfo:mCustomerList){
            if(processInfo.getPackageName().equals(getPackageName())){
                continue;
            }
            processInfo.isCheck = true;
        }
        for(ProcessInfo processInfo:mSystemList){
            processInfo.isCheck = true;
        }
        //2,通知数据适配器刷新
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_process_manager;
    }
}
