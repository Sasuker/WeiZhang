package com.example.administrator.take_two.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.take_two.R;
import com.example.administrator.take_two.model.ChargeChildModel;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ChargeChildListAdapter extends BaseMyAdapter<ChargeChildModel>{
    public ChargeChildListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.layout_charge_child_list_item;
    }
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
        holder.txt_item_money = (TextView) convertView.findViewById(R.id.txt_item_money);
        holder.txt_item_remark = (TextView) convertView.findViewById(R.id.txt_item_remark);
        holder.img_in_out = (ImageView) convertView.findViewById(R.id.img_in_out);
        holder.txt_item_title = (TextView) convertView.findViewById(R.id.txt_item_title);
        return holder;
    }

    @Override
    public void fillView(final int position,final View convertView, Object mholder) {
        Holder holder = (Holder) mholder;
        ChargeChildModel data = datas.get(position);

        if("1".equals(data.getIsOut())){
            holder.img_in_out.setBackgroundResource(R.drawable.icon_money_in);
            holder.txt_item_title.setText("收入项目");
        }else{
            holder.img_in_out.setBackgroundResource(R.drawable.icon_money_out);
            holder.txt_item_title.setText("支出项目");
        }
        holder.txt_item_name.setText(data.getName());
        holder.txt_item_money.setText(data.getMoney() + "");
        holder.txt_item_remark.setText("备注:  "+data.getRemark() + "");
        if(TextUtils.isEmpty(data.getRemark())){
            holder.txt_item_remark.setVisibility(View.GONE);
        }
    }
    private class Holder {
        TextView txt_item_name, txt_item_money,txt_item_remark,txt_item_title;
        ImageView img_in_out;
    }
}
