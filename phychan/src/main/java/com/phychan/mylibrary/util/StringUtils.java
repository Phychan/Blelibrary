package com.phychan.mylibrary.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 陈晖 on 2016/8/19.
 */
public class StringUtils {
    public static boolean isNotEmpty(String value) {
        if (null != value && !TextUtils.isEmpty(value.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String value) {
        if (value == null || TextUtils.isEmpty(value.trim())) {
            return true;
        }
        return false;
    }


    /**
     * 验证手机号码
     *
     * @param phone 要验证的手机号
     * @return 验证结果
     */
    public static boolean isPhoneNumber(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        Pattern p = Pattern.compile("^[1][34578][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 验证是否为简单密码
     *
     * @param password
     * @return
     */
    public static boolean isNotEasyPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        Pattern p = Pattern.compile("(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,20}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        String str = obj.toString();
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasLength(String value) {
        if (null != value && !"".equals(value.trim())) {
            return true;
        }
        return false;
    }

    public static String trimSpaces(String IP) {// 去掉IP字符串前后所有的空格
        while (IP.startsWith(" ")) {
            IP = IP.substring(1, IP.length()).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    public static boolean isIp(String IP) {// 判断是否是一个IP
        boolean b = false;
        if (IP == null || "".equals(IP.trim())) {
            return b;
        }
        IP = trimSpaces(IP);
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255)
                if (Integer.parseInt(s[1]) < 255)
                    if (Integer.parseInt(s[2]) < 255)
                        if (Integer.parseInt(s[3]) < 255)
                            b = true;
        }
        return b;
    }

    /**
     * @param @param  values
     * @param @return
     * @return boolean 返回类型
     * @throws
     * @Title: areNotEmpty
     * @Description: 数组是否为空
     */
    public static boolean areNotEmpty(String... values) {

        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= isNotEmpty(value);
            }
        }
        return result;
    }

    /**
     * @param @param  unicode
     * @param @return
     * @return String 返回类型
     * @throws
     * @Title: unicodeToChinese
     * @Description: TODO
     */
    public static String unicodeToChinese(String unicode) {
        StringBuilder out = new StringBuilder();
        if (isNotEmpty(unicode)) {
            for (int i = 0; i < unicode.length(); i++) {
                out.append(unicode.charAt(i));
            }
        }
        return out.toString();
    }

    public static String stripNonValidXMLCharacters(String input) {
        if (input == null || ("".equals(input)))
            return "";
        StringBuilder out = new StringBuilder();
        char current;
        for (int i = 0; i < input.length(); i++) {
            current = input.charAt(i);
            if ((current == 0x9) || (current == 0xA) || (current == 0xD)
                    || ((current >= 0x20) && (current <= 0xD7FF))
                    || ((current >= 0xE000) && (current <= 0xFFFD))
                    || ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String sha1(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toHexString(byte[] keyData) {
        if (keyData == null) {
            return null;
        }
        int expectedStringLen = keyData.length * 2;
        StringBuilder sb = new StringBuilder(expectedStringLen);
        for (int i = 0; i < keyData.length; i++) {
            String hexStr = Integer.toString(keyData[i] & 0x00FF, 16);
            if (hexStr.length() == 1) {
                hexStr = "0" + hexStr;
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }

    public static String wxSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes());

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    public static String displayValue(String org) {
        if (isEmpty(org)) {
            return "";
        } else {
            return org;
        }
    }

    /**
     * 是否为合法用户名(不可以纯字母)
     *
     * @param password
     * @return
     */
    public static boolean isUserName(String password) {
        if (isEmpty(password)) {
            return false;
        }
        Pattern p = Pattern.compile("(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{1,20}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 是否为合法用户名(可以纯字母)
     * @param username
     * @return
     */
    public static boolean isUserNameCanOnlyLetter(String username){
        if (isEmpty(username)) {
            return false;
        }
        Pattern p = Pattern.compile("/^[a-zA-Z\\d]*[a-zA-Z]+[a-zA-Z\\d]*$/");
        Matcher m = p.matcher(username);
        return m.matches();
    }
}
