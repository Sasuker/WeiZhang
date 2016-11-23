package com.example.administrator.take_two.moneylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.take_two.AppConstanct;
import com.example.administrator.take_two.BaseActivity;
import com.example.administrator.take_two.MyApplication;
import com.example.administrator.take_two.R;
import com.example.administrator.take_two.Util.ToastUtil;
import com.example.administrator.take_two.adapter.MobeyDetailListAdapter;
import com.example.administrator.take_two.db.dao.MoneyDao;
import com.example.administrator.take_two.db.dao.MonthTotalDao;
import com.example.administrator.take_two.db.dao.OneDayTotalDao;
import com.example.administrator.take_two.model.MoneyModel;
import com.example.administrator.take_two.moneylist.widget.FrameLayout;
import com.example.administrator.take_two.moneylist.widget.OperateAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import lib.xlistview.XListView;

/**
 * Created by Administrator on 2016/9/20.
 */
public class MoneyDetailListActivity extends BaseActivity implements XListView.IXListViewListener{
    FrameLayout frameProgress;
    int pageIndex = 1;
    int pageSize = 15;
    String dateFmt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_detail_list);
        initData();
        initView();
        initListView();
        loadDatas();
    }

    private void initData() {
        Intent intent = getIntent();
        dateFmt = intent.getStringExtra(AppConstanct.DATEFMT);
        ((TextView) findViewById(R.id.txt_title)).setText(dateFmt);
    }

    ImageView img_bar_right;

    private void initView() {
        frameProgress = (FrameLayout) this.findViewById(R.id.frame_progress);
        frameProgress.initContainView();

        img_bar_right = (ImageView) this.findViewById(R.id.img_bar_right);
        img_bar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoneyDetailListActivity.this, MoneyDetailEditActivity.class);
                startActivityForResult(intent, AppConstanct.REQ_CODE_ADD);
            }
        });
        findViewById(R.id.img_bar_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    MobeyDetailListAdapter adapter;
    XListView lvData;

    private void initListView() {
        adapter = new MobeyDetailListAdapter(this);
        lvData = (XListView) findViewById(R.id.lv_data);
        lvData.setAdapter(adapter);

        lvData.setXListViewListener(this);
        lvData.setPullLoadEnable(false);
        lvData.setPullRefreshEnable(true);
        lvData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position < 1) {
                    return true;
                }
                final MoneyModel data = (MoneyModel) parent.getItemAtPosition(position);
                OperateAlertDialog.show(MoneyDetailListActivity.this, "确定删除？", new OperateAlertDialog.OnButtonClickLinstener() {
                    @Override
                    public void confirm() {
                        //删除具体一条
                        MoneyDao.deleteById(MoneyDetailListActivity.this, data.getId());
                        if("0".equals(data.getIsOut())) {
                            //更新一天的最大值(支出）
                            OneDayTotalDao.updateMaxMoney_out(MoneyDetailListActivity.this, data);
                            //更新一个月的最大值(支出）
                            MonthTotalDao.updateMaxMoney_out(MoneyDetailListActivity.this, data);
                        }else {
                            //更新一天的最大值(收入）
                            OneDayTotalDao.updateMaxMoney_in(MoneyDetailListActivity.this, data);
                            //更新一个月的最大值(收入）
                            MonthTotalDao.updateMaxMoney_in(MoneyDetailListActivity.this, data);
                        }

                        adapter.getData().remove(position - 1);
                        adapter.notifyDataSetChanged();
                        EventBus.getDefault().post(new MoneyModel());
                    }
                });
                return true;
            }
        });
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 1) {
                    return;
                }
                MoneyModel data = (MoneyModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(MoneyDetailListActivity.this, MoneyDetailEditActivity.class);
                intent.putExtra(AppConstanct.TYPE, AppConstanct.TYPE_EDIT);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstanct.DATE, data);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstanct.REQ_CODE_EDIT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            pageIndex = 1;
            loadDatas();
        }
    }

    public void loadDatas() {
        List<MoneyModel> datas = MoneyDao.getScrollData(this, MyApplication.getInstance().useId, dateFmt, pageIndex, pageSize);
        loadDataSuccess(datas);
    }

    public void loadDataSuccess(Object object) {
        onLoad();
        if (object == null) {
            lvData.setPullLoadEnable(false);
            return;
        }
        List<MoneyModel> datas = (List<MoneyModel>) object;
//        for (int i = 0; i < 15; i++) {
//            datas.add(new BaseModel());
//        }
        if (datas.size() < pageSize) {
            lvData.setPullLoadEnable(false);
        } else {
            lvData.setPullLoadEnable(true);
        }
        if (pageIndex == 1) {
            adapter.setData(datas);
        } else {
//            //测试
//            if (pageIndex == 4) {
//                lvData.setPullLoadEnable(false);
//            }
            adapter.addData(datas);
        }
        pageIndex++;
    }

    public void loadDataFail(String errmsg) {
        onLoad();
        ToastUtil.show(errmsg);
    }

    private void onLoad() {
        frameProgress.destoryProgressView();

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss", Locale.CHINA);
        String date = sDateFormat.format(new Date());
        lvData.stopRefresh();
        lvData.stopLoadMore();
        lvData.setRefreshTime(date);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        loadDatas();
    }

    @Override
    public void onLoadMore() {
        loadDatas();
    }
}
