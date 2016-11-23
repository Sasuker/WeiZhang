package com.example.administrator.take_two.moneylist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.take_two.R;

/**
 * Created by Administrator on 2016/9/17.
 */
public class FrameLayout extends LinearLayout{
    private Context mContext;

    private TextView clickRefreshBt;
    private TextView fail;

    private ImageView img_load_fail;

    private ProgressBar mProgressBar;

    public FrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext= context;
    }

    public void initContainView()
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (ViewGroup) inflater.inflate(R.layout.layout_loading_progress, this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.load_progressBar_id);
        mProgressBar.setVisibility(View.VISIBLE);
        setVisibility(View.VISIBLE);

        clickRefreshBt = (TextView) view.findViewById(R.id.click_refresh_id);
        clickRefreshBt.setVisibility(View.GONE);

        fail = (TextView)view.findViewById(R.id.fail);
        fail.setVisibility(View.GONE);

        img_load_fail = (ImageView)view.findViewById(R.id.img_load_fail);
        img_load_fail.setVisibility(View.GONE);
    }

    public void loadFail(String msg){
        mProgressBar.setVisibility(View.GONE);
        clickRefreshBt.setVisibility(View.VISIBLE);
        clickRefreshBt.setText(msg);
        clickRefreshBt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onClickRefreshListener != null) {
                    destoryProgressView();
                    onClickRefreshListener.onClickRefresh();
                }
            }
        });
    }
    public void loadFail(){
        mProgressBar.setVisibility(View.GONE);
        clickRefreshBt.setVisibility(View.VISIBLE);
        clickRefreshBt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onClickRefreshListener != null) {
                    destoryProgressView();
                    onClickRefreshListener.onClickRefresh();
                }
            }
        });
    }



    public void noImage(){
        img_load_fail.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void noData(){
        fail.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void noData(String str){
        fail.setText(str);
        fail.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void destoryProgressView(){
        this.removeAllViews();
        setVisibility(View.GONE);
    }

    private OnClickRefreshListener onClickRefreshListener;

    public OnClickRefreshListener getOnClickRefreshListener()
    {
        return onClickRefreshListener;
    }

    public void setOnClickRefreshListener(OnClickRefreshListener onClickRefreshListener)
    {
        this.onClickRefreshListener = onClickRefreshListener;
    }

    public interface OnClickRefreshListener{
        public void onClickRefresh();
    }
}
