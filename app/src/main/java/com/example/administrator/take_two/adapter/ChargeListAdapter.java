package com.example.administrator.take_two.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.take_two.R;
import com.example.administrator.take_two.model.ChargeModel;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ChargeListAdapter extends BaseMyAdapter<ChargeModel>{
    public ChargeListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.layout_charge_list_item;
    }
    @Override
    public int getCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public Object initView(int position, View convertView) {
        Holder holder = new Holder();
        holder.txt_item_name = (TextView) convertView.findViewById(R.id.txt_item_name);
        holder.txt_total_money_total = (TextView) convertView.findViewById(R.id.txt_item_money_total);
        holder.txt_total_money_balance = (TextView) convertView.findViewById(R.id.txt_total_money_balance);
        holder.txt_item_remark = (TextView) convertView.findViewById(R.id.txt_item_remark);
        holder.txt_serial_num = (TextView) convertView.findViewById(R.id.txt_serial_num);
        return holder;
    }

    @Override
    public void fillView(final int position,final View convertView, Object mholder) {
        Holder holder = (Holder) mholder;
        ChargeModel data = datas.get(position);
        holder.txt_total_money_total.setText(data.getTotalMoney()+"");
        holder.txt_item_name.setText(data.getName());
        holder.txt_total_money_balance.setText(data.getBalanceMoney()+"");
        holder.txt_item_remark.setText("备注:  "+data.getRemark() + "");
        if(TextUtils.isEmpty(data.getRemark())){
            holder.txt_item_remark.setVisibility(View.GONE);
        }
        holder.txt_serial_num.setText((position+1)+"");
        double pie = data.getBalanceMoney()/data.getTotalMoney();
        if(pie > 0.5){
            holder.txt_serial_num.setBackgroundResource(R.drawable.shape_circle_green_a);
        }else{
            holder.txt_serial_num.setBackgroundResource(R.drawable.shape_circle_orange_a);
        }
    }

    private class Holder {
        TextView txt_item_name, txt_total_money_balance,txt_total_money_total,txt_item_remark,txt_serial_num;
    }
}
