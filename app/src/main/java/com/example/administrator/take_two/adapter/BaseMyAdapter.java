package com.example.administrator.take_two.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public abstract class BaseMyAdapter<T> extends BaseAdapter{
    protected Context context;
    protected List<T> datas;
    protected LayoutInflater mInflater;

    public BaseMyAdapter(Context context){
        this.context = context;
        datas = new ArrayList<T>();
        mInflater = LayoutInflater.from(context);
    }

    public BaseMyAdapter(Context context, List<T> datas){
        this.context = context;
        if (datas == null)
            this.datas = new ArrayList<T>();
        else
            this.datas = datas;

        mInflater = LayoutInflater.from(context);
    }

    /**
     * @return resource
     */
    public abstract int getLayoutResourceId();

    /**
     * 初始化view
     * @param position
     * @param convertView
     * @return holder
     */
    public abstract Object initView(int position, View convertView);

    /**
     * 填充view
     * @param position
     * @param convertView
     * @param holder
     */
    public abstract void fillView(int position, View convertView , Object holder);

    @Override
    public int getCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object holder;
        if(convertView == null){
            convertView = getConvertView(getLayoutResourceId());
            holder = initView(position, convertView);
            convertView.setTag(holder);
        }else{
            holder = convertView.getTag();
        }

        fillView(position, convertView , holder);
        return convertView;
    }

    protected View getConvertView(int resourceId) {
        return mInflater.inflate(resourceId, null);
    }

    /**
     * 设置数据
     * @param datas
     */
    public void setData(List<T> datas) {
        if (datas == null){
            this.datas.clear();
            notifyDataSetChanged();
            return;
        }
        this.datas.clear();
        for (T t : datas) {
            this.datas.add(t);
        }
        notifyDataSetChanged();
    }

    /**
     * 增加数据
     * @param datas
     */
    public void addData(List<T> datas){
        if(datas == null)
            return;
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 获取数据
     * @return
     */
    public List<T> getData() {
        return this.datas;
    }

    /**
     * 清除数据
     */
    public void clearData(){
        datas.clear();
        notifyDataSetChanged();
    }
}
