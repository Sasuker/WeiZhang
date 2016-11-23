package com.example.administrator.take_two;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.take_two.Util.DensityUtil;
import com.example.administrator.take_two.Util.ScreenUtil;
import com.example.administrator.take_two.charge.ChargeListActivity;
import com.example.administrator.take_two.login.LoginActivity;
import com.example.administrator.take_two.moneylist.MoneyListActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    int startAnim = 1;
    int startAnim_2 = 2;
    TextView tx2;
    ImageView roundImageView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        roundImageView = (ImageView) headerView.findViewById(R.id.round);
        roundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
            }
        });
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");//从Sd中找头像，转换成Bitmap
        if(bt!=null){
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);//转换成drawable
            roundImageView.setImageDrawable(drawable);
        }else{
            /**
             *  如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }
        tx2 = (TextView) headerView.findViewById(R.id.xin_2);
        tx2.setText(MyApplication.getInstance().useId);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this , drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        initAnimParam();
    }
    // 再按一次退出
    private long firstTime;
    private long secondTime;
    private long spaceTime;
    public void onBackPressed() {
        firstTime = System.currentTimeMillis();
        spaceTime = firstTime - secondTime;
        secondTime = firstTime;
        if (spaceTime > 2000) {
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_LONG).show();
        } else {
            finish();
            System.exit(0);
        }
    }
    private Bitmap head;//头像Bitmap
    private static String path= "/sdcard/myHead/";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());
                }
                break;
            case 2:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if(head!=null){
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);//保存在SD卡中
                        roundImageView.setImageBitmap(head);//用ImageView显示出来
                    }
                }
            default:
                break;
        }
    }
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName =path + "head.jpg";//图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void goSelf(View view) {
        startActivity(new Intent(MainActivity.this, MoneyListActivity.class));
    }

    public void goOther(View view) {
        startActivity(new Intent(MainActivity.this,ChargeListActivity.class));
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == startAnim){
                operateMenu1();
                handler.sendEmptyMessageDelayed(startAnim_2, 600);
            }
            if(msg.what == startAnim_2){
                operateMenu2();
            }
        }
    };
    //动画部分
    int rect_w = 0,rect_w_2 = 0;
    int screenW;
    boolean menuOpen_1  = false,menuOpen_2  = false;;
    LinearLayout lay_rect_1,lay_rect_2;
    ImageView img_menu_1,img_menu_2;
    public void initAnimParam(){
        img_menu_1 = (ImageView)this.findViewById(R.id.img_1);
        img_menu_2 = (ImageView)this.findViewById(R.id.img_2);
        lay_rect_1 = (LinearLayout)this.findViewById(R.id.lay_rect_1);
        lay_rect_2 = (LinearLayout)this.findViewById(R.id.lay_rect_2);
        img_menu_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateMenu1();
            }
        });
        img_menu_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateMenu2();
            }
        });
        lay_rect_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSelf(v);
            }
        });
        lay_rect_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goOther(v);
            }
        });
        screenW = ScreenUtil.getScreenSize(this,null)[0];
        //中心长方形最大宽度：屏幕宽度 - 两边的padding值2*10dp-两个半圆2*41dp
        rect_w_2 = rect_w = screenW - DensityUtil.dip2px(this,40) - DensityUtil.dip2px(this,82);

        lay_rect_2.getLayoutParams().width = lay_rect_1.getLayoutParams().width = 0;
        lay_rect_1.requestLayout();
        lay_rect_2.requestLayout();
        handler.sendEmptyMessageDelayed(startAnim, 200);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.history){
            Intent intent = new Intent(MainActivity.this, MoneyListActivity.class);
            startActivity(intent);
        }else if(id == R.id.shezhi){
            Toast.makeText(this , "没啥可设置的！！" ,Toast.LENGTH_SHORT).show();
        }else if(id == R.id.shouye){
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }else if(id == R.id.change){
            Intent intent =new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("changeUser",true);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void contentExpandAnimate(final View target, final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                // 获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer) animator.getAnimatedValue();
                target.getLayoutParams().width = currentValue;
                target.requestLayout();
            }
        });
        valueAnimator.setDuration(600).start();
    }

    public void openMenu(View menu,boolean isOpen){
        if(isOpen){
            contentExpandAnimate(menu,0,rect_w);
        }else{
            contentExpandAnimate(menu,rect_w,0);
        }
    }
    public void operateMenu1(){
        menuOpen_1 = !menuOpen_1;
        openMenu(lay_rect_1,menuOpen_1);
    }

    public void operateMenu2(){
        menuOpen_2 = !menuOpen_2;
        openMenu(lay_rect_2,menuOpen_2);
    }

}
