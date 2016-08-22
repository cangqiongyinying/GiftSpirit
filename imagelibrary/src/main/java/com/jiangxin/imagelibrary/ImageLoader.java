package com.jiangxin.imagelibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jiangxin on 2016/8/13.
 */
public class ImageLoader {
    private static ImageLoader imageLoader;
    private static ExecutorService executorService;
    public static ImageLoader init(Context context) {
        if(executorService==null){
            executorService = Executors.newFixedThreadPool(3);
        }
        DiskLruCacheTool.init(context);
        if (imageLoader == null) {
            imageLoader = new ImageLoader();
        }
        return imageLoader;
    }
    //入口:开始加载图片
    public void load(String url,ImageView imageView) {
        //加载内存缓存
        Bitmap memoryCahche = MemoryCacheTool.readMemoryCahche(url);
        if (memoryCahche != null) {
            imageView.setImageBitmap(memoryCahche);
        } else {
            //开启线程
            executorService.execute(new ImageTask(url,imageView));
        }
        DiskLruCacheTool.flush();
    }
}
