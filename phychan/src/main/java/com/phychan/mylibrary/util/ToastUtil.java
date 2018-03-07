package com.phychan.mylibrary.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by wangxl on 2016/11/12.
 */
public class ToastUtil {
    static Toast toast;
    static Handler handler;
    public static void init(Context context) {
        toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        handler = new Handler(Looper.getMainLooper());
    }

    public static void toast(int msgId){
        toast.setText(msgId);
        show();
    }
    public static void toast(CharSequence msg){
        toast.setText(msg);
        show();
    }

    public static void toastError(){
        toast.setText("操作失败");
        show();
    }

    public static void toastFail(){
        toast.setText("网络异常");
        show();
    }

    private static void show() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }

    public static void toastEmpty() {
        toast.setText("数据为空");
        show();
    }
}
