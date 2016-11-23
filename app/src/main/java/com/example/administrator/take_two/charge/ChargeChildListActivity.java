package com.example.administrator.take_two.charge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.take_two.AppConstanct;
import com.example.administrator.take_two.BaseActivity;
import com.example.administrator.take_two.MyApplication;
import com.example.administrator.take_two.R;
import com.example.administrator.take_two.Util.ToastUtil;
import com.example.administrator.take_two.adapter.ChargeChildListAdapter;
import com.example.administrator.take_two.db.dao.ChargeChildDao;
import com.example.administrator.take_two.db.dao.ChargeDao;
import com.example.administrator.take_two.model.ChargeChildModel;
import com.example.administrator.take_two.model.ChargeModel;
import com.example.administrator.take_two.moneylist.widget.FrameLayout;
import com.example.administrator.take_two.moneylist.widget.OperateAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lib.xlistview.XListView;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ChargeChildListActivity extends BaseActivity implements XListView.IXListViewListener {
    FrameLayout frameProgress;
    int pageIndex = 1;
    int pageSize = 15;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_child_list);
        registerEventBus();
        initView();
        initData();
        initListView();
        loadDatas();
    }
    ChargeModel parentModel;
    double parentMoneyTotal = 0;
    Integer chargeParentId = 0;
    private void initData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        parentModel = (ChargeModel) bundle.getSerializable(AppConstanct.DATE);
        chargeParentId = parentModel.getId();
        parentMoneyTotal = parentModel.getTotalMoney();
        setParentChargeInfo();
        findViewById(R.id.lay_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargeChildListActivity.this, ChargeEditActivity.class);
                intent.putExtra(AppConstanct.TYPE,AppConstanct.TYPE_EDIT);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstanct.DATE, parentModel);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstanct.REQ_CODE_EDIT);
            }
        });
    }
    ImageView img_bar_right;
    private void initView(){
        frameProgress = (FrameLayout) findViewById(R.id.frame_progress);
        frameProgress.initContainView();
        img_bar_right = (ImageView) this.findViewById(R.id.img_bar_right);
        img_bar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargeChildListActivity.this, ChargeChildEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstanct.DATE_2, parentModel);
                intent.putExtras(bundle);
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
    ChargeChildListAdapter adapter;
    XListView lvData;
    private void initListView(){
        adapter = new ChargeChildListAdapter(this);
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
                final ChargeChildModel data = (ChargeChildModel) parent.getItemAtPosition(position);
                OperateAlertDialog.show(ChargeChildListActivity.this, "确定删除？", new OperateAlertDialog.OnButtonClickLinstener() {
                    @Override
                    public void confirm() {
                        //删除子类某一条
                        ChargeChildDao.deleteById(ChargeChildListActivity.this,data.getId());
                        //重新计算父类的数值余额
                        //先算出余额=总额+子项收入-子项输出 根据父类id
                        double inMoney = ChargeChildDao.getTotal(ChargeChildListActivity.this, MyApplication.getInstance().useId,chargeParentId,"1");
                        double outMoney = ChargeChildDao.getTotal(ChargeChildListActivity.this,MyApplication.getInstance().useId,chargeParentId,"0");
                        double balanceMoney = parentMoneyTotal + inMoney - outMoney;
                        parentModel.setBalanceMoney(balanceMoney);
                        //根据id，修改charge
                        ChargeDao.update(ChargeChildListActivity.this, parentModel);

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
                ChargeChildModel data = (ChargeChildModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(ChargeChildListActivity.this, ChargeChildEditActivity.class);
                intent.putExtra(AppConstanct.TYPE, AppConstanct.TYPE_EDIT);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstanct.DATE_2, parentModel);
                bundle.putSerializable(AppConstanct.DATE, data);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstanct.REQ_CODE_EDIT);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            pageIndex = 1;
            loadDatas();
        }
    }
    public void loadDatas() {
        List<ChargeChildModel> datas = ChargeChildDao.getScrollData(this, MyApplication.getInstance().useId,chargeParentId, pageIndex, pageSize);
        loadDataSuccess(datas);
    }

    public void loadDataSuccess(Object object) {
        onLoad();
        if (object == null) {
            lvData.setPullLoadEnable(false);
            return;
        }
        List<ChargeChildModel> datas = (List<ChargeChildModel>) object;
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




    private void setParentChargeInfo() {
        ((TextView)findViewById(R.id.txt_item_name)).setText(parentModel.getName());
        ((TextView)findViewById(R.id.txt_item_money_total)).setText(parentModel.getTotalMoney()+"");
        ((TextView)findViewById(R.id.txt_total_money_balance)).setText(parentModel.getBalanceMoney()+"");
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
    public void onEventMainThread(ChargeModel chargeModel){
        if(chargeModel != null){
            parentModel = chargeModel;
            parentMoneyTotal = parentModel.getTotalMoney();
            setParentChargeInfo();
        }
        pageIndex = 1;
        loadDatas();
    }

}
