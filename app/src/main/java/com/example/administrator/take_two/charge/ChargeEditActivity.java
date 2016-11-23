package com.example.administrator.take_two.charge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.take_two.AppConstanct;
import com.example.administrator.take_two.BaseActivity;
import com.example.administrator.take_two.MyApplication;
import com.example.administrator.take_two.R;
import com.example.administrator.take_two.Util.NumberUtil;
import com.example.administrator.take_two.db.dao.ChargeChildDao;
import com.example.administrator.take_two.db.dao.ChargeDao;
import com.example.administrator.take_two.model.ChargeModel;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ChargeEditActivity extends BaseActivity{
    ChargeEditActivity activity;
    TextView txt_title, txt_commit;
    EditText edit_item_name, edit_money, edit_remark;
    private String type = "";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AppConstanct.HANDLER_CODE_BACK) {
                dismissLoadingDialog();
                activity.finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_charge_edit);
        initView();
        initData();
    }
    ChargeModel chargeModel = new ChargeModel();
    private void initData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        type = intent.getStringExtra(AppConstanct.TYPE);
        if (AppConstanct.TYPE_EDIT.equals(type)) {
            chargeModel = (ChargeModel) bundle.getSerializable(AppConstanct.DATE);
            if (chargeModel != null) {
                edit_item_name.setText(chargeModel.getName());
                edit_money.setText(chargeModel.getTotalMoney()+"");
            }
            txt_title.setText("编辑项目");
            edit_money.setEnabled(false);
            edit_money.setTextColor(0xffb3b3b3);
        } else {
            //新建
            edit_money.setTextColor(0xff5c5c5c);
            edit_money.setEnabled(true);
            txt_title.setText("开启新的项目");
        }
    }
    public void goBack(View view) {
        finish();
    }
    private void initView() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_commit = (TextView) findViewById(R.id.txt_commit);
        txt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        edit_item_name = (EditText) findViewById(R.id.edit_item_name);
        edit_money = (EditText) findViewById(R.id.edit_money);
        edit_remark = (EditText) findViewById(R.id.edit_remark);
    }
    public void insert() {
        String remark = edit_remark.getText().toString();
        String moneyStr = edit_money.getText().toString();
        String itemName = edit_item_name.getText().toString();
        if (TextUtils.isEmpty(moneyStr)) {
            Toast.makeText(this,"请填写金额",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(itemName)) {
            Toast.makeText(this,"请填写名称",Toast.LENGTH_LONG).show();
            return;
        }
        double moneyN = 0;
        if (!NumberUtil.isDouble(edit_money.getText().toString())) {
            Toast.makeText(this,"请输入正确的金额",Toast.LENGTH_LONG).show();
            return;
        } else {
            //转换金额
            moneyN = Double.parseDouble(edit_money.getText().toString());
            moneyN = NumberUtil.KeepAfew(moneyN, 2);//保留两位
        }
        showLoadingDialog("请稍后");
        chargeModel.setTotalMoney(moneyN);
        chargeModel.setUserId(MyApplication.getInstance().useId);
        chargeModel.setName(itemName);
        chargeModel.setRemark(remark);

        if (!AppConstanct.TYPE_EDIT.equals(type)) {
            //余额为总费用
            chargeModel.setBalanceMoney(moneyN);
            //直接调用插入
            ChargeDao.insert(this, chargeModel);
        } else {
            //先算出余额=总额+子项收入-子项输出 根据父类id
            double inMoney = ChargeChildDao.getTotal(this,MyApplication.getInstance().useId,chargeModel.getId(),"1");
            double outMoney = ChargeChildDao.getTotal(this, MyApplication.getInstance().useId,chargeModel.getId(),"0");
            double balanceMoney = moneyN + inMoney - outMoney;
            chargeModel.setBalanceMoney(balanceMoney);
            //根据id，修改charge
            ChargeDao.update(this,chargeModel);
        }
//        setResult(Activity.RESULT_OK);
        EventBus.getDefault().post(chargeModel);
        handler.sendEmptyMessageDelayed(AppConstanct.HANDLER_CODE_BACK, 1000);
    }
}
