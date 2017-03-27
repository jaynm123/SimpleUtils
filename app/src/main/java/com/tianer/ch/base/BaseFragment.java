package com.tianer.ch.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tianer.ch.R;
import com.tianer.ch.glide.GlideRoundTransform;

/**
 * @author ch
 */
public class BaseFragment extends Fragment {

    public Context context;
    private ProgressDialog dialog;
    private View view;
    private ImageView iv_loading;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * 打开指定的activity
     *
     * @param cls
     */
    public void startA(Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }


    /**
     * toast
     *
     * @param msg
     */
    public void showtoast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
        if (null == dialog) {
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
        SharedPreferences preferences = context.getSharedPreferences("bainianegou", Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    /**
     * 存值
     *
     * @param key
     * @param value
     */
    public void putValueByKey(String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("bainianegou", Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).commit();
    }


}
