package com.tianer.ch.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tianer.ch.R;
import com.tianer.ch.glide.GlideRoundTransform;
import com.tianer.ch.utils.ActivityManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends FragmentActivity {
    public Context context;
    private ProgressDialog dialog;
    private View view;
    private ImageView iv_loading;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        ActivityManager.getInstance().pushOneActivity(this);
//        //隐藏导航栏和状态栏
//        View decorView = getWindow().getDecorView();
//        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(option);

//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
    }

    //    /**
//     * 沉浸式模式
//     *
//     * @param hasFocus
//     */
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }


    /**
     * 打开Activity
     *
     * @param cls
     */
    public void startA(Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().popOneActivity(this);
    }

    /**
     * @param s
     */
    public void showtoast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast
     *
     * @param msg
     */
    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示加载动画
     */
    public void showLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        }
        if (iv_loading == null) {
            iv_loading = (ImageView) view.findViewById(R.id.iv_loading);
        }
        Glide.with(context).load(R.mipmap.num47).transform(new GlideRoundTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_loading);
        if (dialog == null) {
            dialog = new ProgressDialog(context, R.style.dialog);
        }
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);

    }

    /**
     * 隐藏加载动画
     */
    public void closeLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 通过资源res获得view
     *
     * @param res
     * @return
     */
    public View getViewByRes(int res) {
        return LayoutInflater.from(context).inflate(res, null);
    }

    /**
     * 获得TextView 的文本
     *
     * @param tv
     * @return
     */
    public String getTV(TextView tv) {
        return tv == null ? "" : tv.getText().toString();
    }


    /**
     * 取值
     *
     * @param key
     * @return
     */
    public String getValueByKey(String key) {
        if(preferences==null){
            preferences = context.getSharedPreferences("bainianegou", Context.MODE_PRIVATE);
        }
        return preferences.getString(key, "");
    }

    /**
     * 存值
     *
     * @param key
     * @param value
     */
    public void putValueByKey(String key, String value) {
        if(preferences==null){
            preferences = context.getSharedPreferences("bainianegou", Context.MODE_PRIVATE);
        }
        preferences.edit().putString(key, value).commit();
    }

    /**
     * 判断是否为手机号码
     */
    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }


}
