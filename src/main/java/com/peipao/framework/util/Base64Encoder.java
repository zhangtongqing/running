package com.peipao.framework.util;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/6/21 11:04
 * 修订记录：
 */
public class Base64Encoder {
    public static String getBASE64(String s) {
        if (s == null)
            return null;
        return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
    }
    // 将 BASE64 编码的字符串 s 进行解码   解密
    public static String getFromBASE64(String s) {
        if (s == null)
            return null;
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
    public static String mTOa(Object ming){
        return Base64Encoder.getBASE64(Base64Encoder.getBASE64(Base64Encoder.getBASE64((String)ming)));
    }
    public static String aTOm(String an){
        return Base64Encoder.getFromBASE64(Base64Encoder.getFromBASE64(Base64Encoder.getFromBASE64(an)));
    }
    public static void main(String[] args) {

        String a = mTOa("dev".toString());
        System.out.println(a);//加密
        System.out.println(aTOm(a));//解密
    }
}
