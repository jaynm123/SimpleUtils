package com.tianer.ch.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * Created by ch on 2017/3/1 0001.
 */

public abstract class MyBaseAdapter<T extends BaseViewHolder> extends BaseAdapter {
    private int count;

    public MyBaseAdapter( int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        T t = null;
        if (convertView == null) {
            t = onCreateViewHolder();
            convertView = t.createView();
            if (convertView != null) {
                convertView.setTag(t);
            }
        } else {
            t = (T) convertView.getTag();
        }
        onHolder(t, position);
        return convertView;
    }

    /**
     * 填充数据
     *
     * @param holder
     * @param position
     */
    public abstract void onHolder(T holder, int position);

    /**
     * 生成ViewHolder
     *
     * @return
     */
    public abstract T onCreateViewHolder();

    /**
     * 通知更新
     *
     * @param count
     */
    public void notifyDataSetChanged(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

}
