package com.tianer.ch.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tianer.ch.R;
import com.tianer.ch.glide.GlideRoundTransform;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 作者： ch
 * 时间： 2016/11/16.17:30
 * 描述：联网处理类 处理显示/关闭dialog 和error
 * 来源：
 */

public abstract class BaseCallback extends Callback<String> {
    private Context context;
    private ProgressDialog dialog;
    public static final String ERROR_MSG = "连接超时";

    public BaseCallback(Context context) {
        this.context = context;
    }


    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        return response.body().string();
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        closeLoadingDialog();
        showtoast(ERROR_MSG);
    }


    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        showLoadingDialog();

    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        closeLoadingDialog();
    }

    /**
     * 显示加载动画
     */
    public void showLoadingDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        ImageView iv_loading = (ImageView) view.findViewById(R.id.iv_loading);
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
     * @param s
     */
    public void showtoast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
