package com.tianer.ch.adapter;

import android.view.View;


/**
 * Created by ch on 2017/3/2 0002.
 */

public abstract class BaseViewHolder {
    private View view;

    public BaseViewHolder() {

    }

    public BaseViewHolder(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * 用来返回 view
     *
     * @return
     */
    public abstract View createView();

}
