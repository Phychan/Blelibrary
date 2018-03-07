package com.phychan.mylibrary.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by 陈晖 on 2017/8/29.
 */

public class MathUtil {

    /**
     * 保留两位小数
     */
    public static final String m2(double num) {
        BigDecimal bg = new BigDecimal(num).setScale(2, RoundingMode.UP);
//        DecimalFormat df = new DecimalFormat("#.00");
//        return df.format(num);
        return bg.doubleValue() + "";
    }

    /**
     * double转String解决数过大转科学计数法的问题
     */
    public static final String doubleToString(String num) {
        if (num == null || "null".equals(num) || "".equals(num)) {
            return "0";
        }
        double d = Double.parseDouble(num);
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(d);
    }

    /**
     * 四舍五入保留两位小数
     *
     * @return
     */
    public static final String round(String d) {
        return String.format("%.2f", Double.parseDouble(d));
    }
}
