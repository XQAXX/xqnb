package com.dream.common.util.encryption;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

public class MD5Utils {
    /**
     * 通用加密方法
     *
     * @param str     需要加密的字符串
     * @param isUpper 字母大小写(false为小写，true为大写)
     * @param bit     加密的位数（16,32,64）
     * @return
     */
    public static String getMD5(String str, boolean isUpper, Integer bit) {
        String md5 = null;
        try {
            // 创建加密对象
            MessageDigest md = MessageDigest.getInstance("md5");
            if (bit == 64) {
                BASE64Encoder bw = new BASE64Encoder();
                md5 = bw.encode(md.digest(str.getBytes("utf-8")));
            } else {
                // 计算MD5函数
                md.update(str.getBytes());
                byte b[] = md.digest();
                int i;
                StringBuilder sb = new StringBuilder();
                for (byte aB : b) {
                    i = aB;
                    if (i < 0) {
                        i += 256;
                    }
                    if (i < 16) {
                        sb.append("0");
                    }
                    sb.append(Integer.toHexString(i));
                }
                md5 = sb.toString();
                if (bit == 16) {
                    //截取32位md5为16位
                    md5 = md5.substring(8, 24);
                    if (isUpper) {
                        md5 = md5.toUpperCase();
                    }
                    return md5;
                }
            }
            //转换成大写
            if (isUpper) {
                md5 = md5.toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }
}
