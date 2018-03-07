package com.phychan.mylibrary.util;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by 陈晖 on 2015/12/25.
 */
public class MyCount extends CountDownTimer {

    private Button securityButton;

    public MyCount(long millisInFuture, long countDownInterval, Button securityButton) {
        super(millisInFuture, countDownInterval);
        this.securityButton = securityButton;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        securityButton.setClickable(false);
        securityButton.setSelected(false);
        securityButton.setText(millisUntilFinished / 1000 + "s");
    }

    @Override
    public void onFinish() {
        securityButton.setClickable(true);
        securityButton.setSelected(true);
        securityButton.setText("发送验证码");
    }

}
