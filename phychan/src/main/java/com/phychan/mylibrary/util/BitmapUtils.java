package com.phychan.mylibrary.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wuq on 2015/10/20.
 */
public class BitmapUtils {

    public static Bitmap getBitmapFormPath(String path, int reqWidth, int reqHeight) {
        if (path == null || path.equals("")) {
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inSampleSize = calculateInSampleSize(options, 200, 200);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapFormPath(String path) {
        return getBitmapFormPath(path, 300, 300);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static byte[] FilePath2Bytes(String path) {
        // 对图片进行压缩
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的高和宽
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int be = (int) (options.outHeight / (float) 200);
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片
        bitmap = BitmapFactory.decodeFile(path, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bitmap.recycle();
        byte[] bitmapByte = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmapByte;
    }

    /**
     * 更具图片网址返回bitmap
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }
}