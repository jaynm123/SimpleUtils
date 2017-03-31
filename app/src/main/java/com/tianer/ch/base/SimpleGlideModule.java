package com.tianer.ch.base;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * 作者： ch 时间： 2016/7/27.14:07 描述：glide 配置类 来源：
 */
public class SimpleGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        glideBuilder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        String path = context.getExternalCacheDir().getPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        int cacheSize100MegaBytes = 104857600;
        glideBuilder.setDiskCache(new DiskLruCacheFactory(path, cacheSize100MegaBytes));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
