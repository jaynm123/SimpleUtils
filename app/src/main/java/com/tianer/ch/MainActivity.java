package com.tianer.ch;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianer.ch.adapter.BaseViewHolder;
import com.tianer.ch.adapter.MyBaseAdapter;
import com.tianer.ch.base.BaseActivity;
import com.tianer.ch.utils.SimVation;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("cheng", "待加密文字=" + "18702953620");
        String x = SimVation.encrypt("18702953620", "chenghuan");
        Log.e("cheng", "加密后=" + x);
        String y = SimVation.decrypt(x, "chenghuan");
        Log.e("cheng", "解密后=" + y);
        MyBaseAdapter adapter = new MyBaseAdapter<ViewHolder>(100) {
            @Override
            public void onHolder(ViewHolder holder, int position) {

            }

            @Override
            public ViewHolder onCreateViewHolder() {
                return new ViewHolder(getViewByRes(R.layout.item_get_house));
            }
        };
    }

    public class ViewHolder extends BaseViewHolder {
        public View rootView;
        public TextView tv_gethouse_type;
        public TextView tv_gethouse_trends;
        public ImageView iv_get_house;
        public TextView tv_gethouse_name;
        public TextView tv_gethouse_area;
        public TextView tv_gethouse_acreage;
        public TextView tv_gethouse_price;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_gethouse_type = (TextView) rootView.findViewById(R.id.tv_gethouse_type);
            this.tv_gethouse_trends = (TextView) rootView.findViewById(R.id.tv_gethouse_trends);
            this.iv_get_house = (ImageView) rootView.findViewById(R.id.iv_get_house);
            this.tv_gethouse_name = (TextView) rootView.findViewById(R.id.tv_gethouse_name);
            this.tv_gethouse_area = (TextView) rootView.findViewById(R.id.tv_gethouse_area);
            this.tv_gethouse_acreage = (TextView) rootView.findViewById(R.id.tv_gethouse_acreage);
            this.tv_gethouse_price = (TextView) rootView.findViewById(R.id.tv_gethouse_price);
        }

        @Override
        public View createView() {
            return rootView;
        }

    }
}
