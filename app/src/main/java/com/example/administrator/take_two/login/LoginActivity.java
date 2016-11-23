package com.example.administrator.take_two.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.take_two.BaseActivity;
import com.example.administrator.take_two.MainActivity;
import com.example.administrator.take_two.MyApplication;
import com.example.administrator.take_two.R;
import com.example.administrator.take_two.Util.CacheUtil;
import com.example.administrator.take_two.Util.RegexUtil;

/**
 * Created by Administrator on 2016/9/8.
 */
public class LoginActivity extends BaseActivity{
    EditText et;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_main);
        initView();
    }

    private void initView() {
        et = (EditText) findViewById(R.id.ed_phone);
    }
    public void commit(View view){
        String userPhone = et.getText().toString();
        if (!RegexUtil.checkMobile(userPhone)) {
            Toast.makeText(this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        MyApplication.getInstance().useId = userPhone;
        CacheUtil.cacheStrValue(this, CacheUtil.PRE_NAME_USER, CacheUtil.KEY_USER_ID, userPhone);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }
    // 再按一次退出
    private long firstTime;
    private long secondTime;
    private long spaceTime;

    @Override
    public void onBackPressed() {
        if(!getIntent().getBooleanExtra("changeUser",false)){
            firstTime = System.currentTimeMillis();
            spaceTime = firstTime - secondTime;
            secondTime = firstTime;
            if (spaceTime > 2000) {
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
                System.exit(0);
            }
        }else{
            super.onBackPressed();
        }
    }
}
