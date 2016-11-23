package com.example.administrator.take_two.charge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.take_two.AppConstanct;
import com.example.administrator.take_two.BaseActivity;
import com.example.administrator.take_two.MyApplication;
import com.example.administrator.take_two.R;
import com.example.administrator.take_two.Util.NumberUtil;
import com.example.administrator.take_two.db.dao.ChargeChildDao;
import com.example.administrator.take_two.db.dao.ChargeDao;
import com.example.administrator.take_two.model.ChargeChildModel;
import com.example.administrator.take_two.model.ChargeModel;
import com.example.administrator.take_two.model.MoneyModel;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ChargeChildEditActivity extends BaseActivity{
    ChargeChildEditActivity activity;
    TextView txt_title, txt_commit;
    EditText edit_item_name, edit_money, edit_remark;
    private String type = "";
    ImageView rdio_is_out,rdio_is_in;
    boolean isOut = true;
    MoneyModel oldMoneyModel = null;
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
        setContentView(R.layout.activity_charge_child_edit);
        initView();
        initData();
    }
    ChargeChildModel chargeChildModel = new ChargeChildModel();
    ChargeModel parentChargeModel = new ChargeModel();

    private void initData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        type = intent.getStringExtra(AppConstanct.TYPE);
        parentChargeModel =  (ChargeModel) bundle.getSerializable(AppConstanct.DATE_2);//正常一般都要有

        if (AppConstanct.TYPE_EDIT.equals(type)) {
            chargeChildModel = (ChargeChildModel) bundle.getSerializable(AppConstanct.DATE);
            if (chargeChildModel != null) {
                edit_item_name.setText(chargeChildModel.getName());
                edit_money.setText(chargeChildModel.getMoney() + "");
                edit_remark.setText(chargeChildModel.getRemark());
                if ("0".equals(chargeChildModel.getIsOut())) {
                    rdio_is_in.setImageResource(R.drawable.icon_check_out);
                    rdio_is_out.setBackgroundResource(R.drawable.icon_check_in);
                    isOut = true;
                } else {
                    isOut = false;
                    rdio_is_in.setImageResource(R.drawable.icon_check_in);
                    rdio_is_out.setBackgroundResource(R.drawable.icon_check_out);
                }
            }
            txt_title.setText("编辑");
        } else {
            //新建
            txt_title.setText("添加一项");
        }
    }
    public void goBack(View view) {
        finish();
    }
    private void initView() {
        rdio_is_out = (ImageView) findViewById(R.id.rdio_is_out);
        rdio_is_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOut = true;
                rdio_is_in.setImageResource(R.drawable.icon_check_out);
                rdio_is_out.setBackgroundResource(R.drawable.icon_check_in);
            }
        });
        rdio_is_in = (ImageView) findViewById(R.id.rdio_is_in);
        rdio_is_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOut = false;
                rdio_is_in.setImageResource(R.drawable.icon_check_in);
                rdio_is_out.setBackgroundResource(R.drawable.icon_check_out);
            }
        });
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
        if(!NumberUtil.isDouble(edit_money.getText().toString())){
            Toast.makeText(this,"请输入正确的金额",Toast.LENGTH_LONG).show();
            return;
        }else{
            //转换金额
            moneyN = Double.parseDouble(edit_money.getText().toString());
            moneyN = NumberUtil.KeepAfew(moneyN,2);//保留两位
        }


        showLoadingDialog("请稍后");
        if (isOut) {
            //说明是支出
            chargeChildModel.setIsOut("0");
        } else {
            chargeChildModel.setIsOut("1");
        }
        chargeChildModel.setMoney(moneyN);
        chargeChildModel.setUserId(MyApplication.getInstance().useId);
        chargeChildModel.setName(edit_item_name.getText().toString());
        chargeChildModel.setChargeParentId(parentChargeModel.getId());
        chargeChildModel.setRemark(edit_remark.getText().toString());
        if (!AppConstanct.TYPE_EDIT.equals(type)) {
            //直接调用插入语句
            ChargeChildDao.insert(this,chargeChildModel);
        } else {
            //直接调用更新语句
            ChargeChildDao.update(this,chargeChildModel);
        }
        //重新计算父类的数值余额
        //先算出余额=总额+子项收入-子项输出 根据父类id
        double inMoney = ChargeChildDao.getTotal(this,MyApplication.getInstance().useId,parentChargeModel.getId(),"1");
        double outMoney = ChargeChildDao.getTotal(this, MyApplication.getInstance().useId,parentChargeModel.getId(),"0");
        double balanceMoney = parentChargeModel.getTotalMoney() + inMoney - outMoney;
        parentChargeModel.setBalanceMoney(balanceMoney);
        //同事需要修改总额=之前的+收入的
        parentChargeModel.setTotalMoney(parentChargeModel.getTotalMoney() + inMoney);
        //根据id，修改charge
        ChargeDao.update(this, parentChargeModel);

        EventBus.getDefault().post(parentChargeModel);//传回去
        handler.sendEmptyMessageDelayed(AppConstanct.HANDLER_CODE_BACK, 1000);
    }
}
