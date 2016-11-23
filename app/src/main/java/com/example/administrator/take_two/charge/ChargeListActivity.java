package com.example.administrator.take_two.charge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.administrator.take_two.AppConstanct;
import com.example.administrator.take_two.BaseActivity;
import com.example.administrator.take_two.MyApplication;
import com.example.administrator.take_two.R;
import com.example.administrator.take_two.Util.ToastUtil;
import com.example.administrator.take_two.adapter.ChargeListAdapter;
import com.example.administrator.take_two.db.dao.ChargeChildDao;
import com.example.administrator.take_two.db.dao.ChargeDao;
import com.example.administrator.take_two.model.ChargeModel;
import com.example.administrator.take_two.moneylist.widget.FrameLayout;
import com.example.administrator.take_two.moneylist.widget.OperateAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lib.xlistview.XListView;

/**
 * Created by Administrator on 2016/9/11.
 */
public class ChargeListActivity extends BaseActivity implements XListView.IXListViewListener {
    FrameLayout frameProgress;
    int pageIndex = 1;
    int pageSize = 15;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_list);
        registerEventBus();
        initView();
        initListView();
        loadDatas();
    }

    ImageView img_bar_right;
    private void initView() {
        frameProgress = (FrameLayout) this.findViewById(R.id.frame_progress);
        frameProgress.initContainView();
        img_bar_right = (ImageView) this.findViewById(R.id.img_bar_right);
        img_bar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargeListActivity.this, ChargeEditActivity.class);
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
    ChargeListAdapter adapter;
    XListView lvData;
    private void initListView(){
        adapter = new ChargeListAdapter(this);
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
                final ChargeModel data = (ChargeModel) parent.getItemAtPosition(position);
                OperateAlertDialog.show(ChargeListActivity.this, "确定删除？", new OperateAlertDialog.OnButtonClickLinstener() {
                    @Override
                    public void confirm() {
                        ChargeDao.deleteById(ChargeListActivity.this,data.getId());
                        //删除子类
                        ChargeChildDao.deleteById(ChargeListActivity.this, data.getId());

                        adapter.getData().remove(position - 1);
                        adapter.notifyDataSetChanged();
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
                ChargeModel data = (ChargeModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(ChargeListActivity.this, ChargeChildListActivity.class);
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
        List<ChargeModel> datas = ChargeDao.getScrollData(this, MyApplication.getInstance().useId, pageIndex, pageSize);
        loadDataSuccess(datas);
    }

    public void loadDataSuccess(Object object) {
        onLoad();
        if (object == null) {
            lvData.setPullLoadEnable(false);
            return;
        }
        List<ChargeModel> datas = (List<ChargeModel>) object;
        if (datas.size() < pageSize) {
            lvData.setPullLoadEnable(false);
        } else {
            lvData.setPullLoadEnable(true);
        }
        if (pageIndex == 1) {
            adapter.setData(datas);
        } else {
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

    public void onRefresh() {
        pageIndex = 1;
        loadDatas();
    }

    public void onLoadMore() {
        loadDatas();
    }

    public void onEventMainThread(ChargeModel chargeModel){
        pageIndex = 1;
        loadDatas();
    }
}
