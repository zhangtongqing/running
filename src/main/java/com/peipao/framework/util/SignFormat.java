package com.peipao.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Description: 签名规则
 * Copyright  : Copyright (c) 2016
 * Company    : Xi'an accompany running network technology Co., Ltd.
 * Author     : leaf
 * Date       : 2017/7/13
 */
public class SignFormat {

    protected static String privateKey="ABCDEFG$";

    private static final  String DEFAULT_URL_ENCODING =  "UTF-8" ;

    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String urlEncode(String part) {
        try  {
            String encode = URLEncoder.encode(part, DEFAULT_URL_ENCODING);
            String msg = encode
                                //.replace( "+",  " ")
                                .replace("%3D","=")
                                .replace("%2F", "/")
                                .replace("%2C", ",")
                                .replace("%3A", ":")
                                .replace("%27", "'")
                                //.replace("%22" ,"\"")
                                //.replace("%25", "%")
                                .replace("%26", "&")
                                .replace("%7E", "~")
                                .replace("%28", "(")
                                .replace("%24", "$")
                                .replace("%29", ")")
                                .replace("%27", "'")
                                .replace("%21", "!")
                                .replace("%23", "#")
                                .replace("%3B", ";")
                                .replace("%40", "@")
                                .replace("%2B", "+")
                                .replace("+", "%20")
                                .replace("%3F", "?");

            return msg;
        }  catch  (UnsupportedEncodingException e) {
            return "urlEncode编码出错";
        }
    }

    /**
     * 签名规则
     * @param url    方位的URL
     * @param body   参数
     * @return
     */
    public static String getSignFormat(String url,String body){
        String s = urlEncode(body);
        StringBuilder sign = new StringBuilder();
        sign.append(url).append(s).append(privateKey);
//        System.out.println(sign.toString());
        String md5String = MD5Util.getMD5(sign.toString());
//        System.out.println(md5String);
        return md5String;
    }

    public static void main(String[] args) {

//        String url = "http://211.159.153.228:8080/api/luckdraw/app/luckRun?userId=17&token=d42feb1b4bd24d4899d858377b520fbd&sign=";
//        String body = "{\"runningRecordId\":\"1720171225121644668\"}";
//        getSignFormat(url, body);


    }

}
