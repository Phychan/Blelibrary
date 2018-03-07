package com.phychan.mylibrary.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phychan.mylibrary.R;
import com.phychan.mylibrary.util.DialogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by 陈晖 on 2017/11/15.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Map<String, String> dataMap = new HashMap<>();

    public static final int CALLPHONE_REQUESTCODE = 0xCD;

    protected TextView toolbar_title;
    protected TextView cancel_button;
    protected ImageView back_button;
    protected LinearLayout ll_head_bg;
    protected View head_status_bar;
    protected TextView tv_title_right;
    protected ImageView iv_title_right;
    protected FrameLayout fl_title_content;

    protected FrameLayout content_view;

    public static int statusHeaigh;

    protected Handler handler = new Handler();

    protected Context context;

    private boolean isDialogExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (registerEventBus()) {
            EventBus.getDefault().register(this);
        }
        superSetContentView(R.layout.my_base_layout);
        context = this;
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        content_view = (FrameLayout) findViewById(R.id.content_view);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        cancel_button = (TextView) findViewById(R.id.cancel_button);
        back_button = (ImageView) findViewById(R.id.back_button);
        ll_head_bg = (LinearLayout) findViewById(R.id.ll_head_bg);
        head_status_bar = findViewById(R.id.head_status_bar);
        fl_title_content = (FrameLayout) findViewById(R.id.fl_title_content);

        if (isImmersiveMode()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            head_status_bar.getLayoutParams().height = BaseApplication.statusHeaigh;
        }

        if (getContentViewId() > 0) {
            setContentView(getContentViewId());
        }

        ButterKnife.bind(this);

        init();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public FrameLayout getContent_view() {
        return content_view;
    }

    /**
     * 获取布局文件
     *
     * @return 布局文件ID
     */
    protected abstract
    @LayoutRes
    int getContentViewId();

    /**
     * 初始化操作
     */
    protected abstract void init();

    /**
     * 是否是沉浸式
     *
     * @return
     */
    protected boolean isImmersiveMode() {
        return false;
    }

    /**
     * 是否注册到EventBus
     *
     * @return
     */
    protected boolean registerEventBus() {
        return false;
    }

    /**
     * 拨打电话
     *
     * @param phone 电话号码
     */
    protected void callPhone(String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    public void showSnackBar(String content) {
        Snackbar.make(content_view, content, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackBarWithFinish(String content) {
        Snackbar.make(content_view, content, Snackbar.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        content_view.removeAllViews();
        LayoutInflater.from(this).inflate(layoutResID, content_view, true);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        content_view.removeAllViews();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        content_view.addView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        content_view.addView(view, params);
    }

    protected void setTitleText(int titleRes) {
        setTitleText(getText(titleRes));
    }

    protected void setTitleText(CharSequence titleText) {
        toolbar_title.setText(titleText);
        fl_title_content.setVisibility(titleText == null ? View.GONE : View.VISIBLE);
    }

    protected void setTitleRightText(String right, View.OnClickListener listener) {
        iv_title_right.setVisibility(View.GONE);
        tv_title_right.setText(right);
        tv_title_right.setOnClickListener(listener);
    }

    protected void setTitleRightImage(int image, View.OnClickListener listener) {
        tv_title_right.setVisibility(View.GONE);
        iv_title_right.setImageResource(image);
        iv_title_right.setOnClickListener(listener);
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    OnBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(BaseActivity.OnBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    /**
     * 自动收键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideInput();
//                v.clearFocus();
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * @param v
     * @param event
     * @return
     * @author wangxl
     * @date 2016-6-7  上午11:14:39
     * @class BaseActivity.java
     * @description 判断是否该收键盘
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    protected InputMethodManager inputMethodManager;

    public void hideInput() {
        if (inputMethodManager == null) {
            inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        }
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showInput(View view) {
        view.requestFocus();
        if (inputMethodManager == null) {
            inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        }
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void showLoadingDialog(String showText) {
        if (!DialogUtil.isShowing()) {
            DialogUtil.showLoadingDialog(this, showText);
        }
//        isDialogExist = true;
    }

    public void dismissLoadingDialog() {
        DialogUtil.dismiss();
       // isDialogExist = false;
    }

    public void superSetContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void superSetContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onDestroy() {
        if (registerEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    /*---------------------------------------------------------------------------以下是android6.0动态授权的封装---------------------------------------------------------------------------*/
    private int mPermissionIdx = 0x10;//请求权限索引
    private SparseArray<GrantedResult> mPermissions = new SparseArray<>();//请求权限运行列表

    @SuppressLint("Override")
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        GrantedResult runnable = mPermissions.get(requestCode);
        if (runnable == null) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            runnable.mGranted = true;
        }
        runOnUiThread(runnable);
    }

    public void requestPermission(String[] permissions, String reason, GrantedResult runnable) {
        if (runnable == null) {
            return;
        }
        runnable.mGranted = false;
        if (Build.VERSION.SDK_INT < 23 || permissions == null || permissions.length == 0) {
            runnable.mGranted = true;//新添加
            runOnUiThread(runnable);
            return;
        }
        final int requestCode = mPermissionIdx++;
        mPermissions.put(requestCode, runnable);

		/*
            是否需要请求权限
		 */
        boolean granted = true;
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                granted = granted && checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            }
        }

        if (granted) {
            runnable.mGranted = true;
            runOnUiThread(runnable);
            return;
        }

		/*
            是否需要请求弹出窗
		 */
        boolean request = true;
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                request = request && !shouldShowRequestPermissionRationale(permission);
            }
        }

        if (!request) {
            final String[] permissionTemp = permissions;
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(reason)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(permissionTemp, requestCode);
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            GrantedResult runnable = mPermissions.get(requestCode);
                            if (runnable == null) {
                                return;
                            }
                            runnable.mGranted = false;
                            runOnUiThread(runnable);
                        }
                    }).create();
            dialog.show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, requestCode);
            }
        }
    }

    public static abstract class GrantedResult implements Runnable {
        private boolean mGranted;

        public abstract void onResult(boolean granted);

        @Override
        public void run() {
            onResult(mGranted);
        }
    }

}
