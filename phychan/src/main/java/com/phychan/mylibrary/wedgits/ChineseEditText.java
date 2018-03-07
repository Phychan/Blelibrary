package com.phychan.mylibrary.wedgits;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * Created by 陈晖 on 2017/5/2.
 */

public class ChineseEditText extends android.support.v7.widget.AppCompatEditText {

    public ChineseEditText(Context context) {
        super(context);
    }

    public ChineseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChineseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 输入法
     *
     * @param outAttrs
     * @return
     */
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MyInputConnecttion(super.onCreateInputConnection(outAttrs),
                false);
    }


}

class MyInputConnecttion extends InputConnectionWrapper implements
        InputConnection {

    public MyInputConnecttion(InputConnection target, boolean mutable) {
        super(target, mutable);
    }

    /**
     * 对输入的内容进行拦截
     *
     * @param text
     * @param newCursorPosition
     * @return
     */
    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        // 只能输入汉字
        if (!text.toString().matches("[\u4e00-\u9fa5]+")) {
            return false;
        }
        return super.commitText(text, newCursorPosition);
    }

    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        return super.sendKeyEvent(event);
    }

    @Override
    public boolean setSelection(int start, int end) {
        return super.setSelection(start, end);
    }
}