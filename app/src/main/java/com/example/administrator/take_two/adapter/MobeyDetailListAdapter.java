package com.example.administrator.take_two.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.take_two.R;
import com.example.administrator.take_two.model.MoneyModel;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MobeyDetailListAdapter extends  BaseMyAdapter<MoneyModel>{


    public MobeyDetailListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.layout_money_list_detail_list_item;
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
        holder.txt_item_money = (TextView) convertView.findViewById(R.id.txt_item_money);
        holder.txt_item_name_word = (TextView) convertView.findViewById(R.id.txt_item_name_word);
        holder.txt_item_remark = (TextView) convertView.findViewById(R.id.txt_item_remark);
        holder.img_in_out = (ImageView) convertView.findViewById(R.id.img_in_out);
        return holder;
    }

    @Override
    public void fillView(final int position, final View convertView, Object mholder) {
        Holder holder = (Holder) mholder;
        MoneyModel data = datas.get(position);

        double money = data.getMoney();
        String word = "花费了";
        if("1".equals(data.getIsOut())){
            holder.img_in_out.setBackgroundResource(R.drawable.icon_money_in);
            if(money < 20){
                word = "也才赚了";
            }else if(money > 100){
                word = "居然赚了";
            }else{
                word = "赚了";
            }
        }else{
            if(money < 20){
                word = "也才花费了";
            }else if(money > 100){
                word = "居然花费了";
            }else{
                word = "花费了";
            }
            holder.img_in_out.setBackgroundResource(R.drawable.icon_money_out);
        }
        holder.txt_item_name_word.setText(word+":");
        holder.txt_item_name.setText(data.getItemName());
        holder.txt_item_money.setText(data.getMoney() + "");
        holder.txt_item_remark.setText("备注:  "+data.getRemark() + "");
        if(TextUtils.isEmpty(data.getRemark())){
            holder.txt_item_remark.setVisibility(View.GONE);
        }
    }

    private class Holder {
        TextView txt_item_name, txt_item_money,txt_item_remark,txt_item_name_word;
        ImageView img_in_out;
    }
}
