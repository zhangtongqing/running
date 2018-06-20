package com.peipao.framework.util;


import com.google.common.base.Joiner;
import com.peipao.framework.constant.WebConstants;

import java.util.*;

/**
 * @author: dongll
 * @date: 2015-5-18上午10:59:25
 * @version: @description：
 */
public class StringUtil {

    /**
     * 判断字符串是空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return "".equals(str) || str == null;
    }

    /**
     * 判断字符串不是空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !"".equals(str) && str != null;
    }

    /**
     * 判断某一个字符串数组中是否含有某一字符串
     *
     * @param str
     * @param strArr
     * @return
     */
    public static boolean existStrArr(String str, String[] strArr) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    //密码明文转MD5
    public static String getMD5(String instr) {
        String s = null;
        // 用来将字节转换成 16 进制表示的字
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(instr.getBytes());
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str); //.toUpperCase()
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 将终端传来经过混合加密的md5解密
     *
     * @param p
     * @return
     */
    public static String decode(String p) {
        StringBuffer sb = new StringBuffer();
        if (p.length() == 32) {
            sb.append(p.charAt(p.length() - 1));
            sb.append(p.substring(1, 4));
            sb.append(p.charAt(p.length() - 5));
            sb.append(p.substring(5, p.length() - 5));
            sb.append(p.charAt(4));
            sb.append(p.substring(p.length() - 4, p.length() - 1));
            sb.append(p.charAt(0));
        }
        return sb.toString();
    }

    public static String toString(String[] s) {
        return Joiner.on(WebConstants.DELIMITER_COMMA).join(s);
    }

    public static List<String> toList(String s) {
        if (s == null || s.isEmpty())
            return new ArrayList<String>();
        List<String> ids = new ArrayList<String>(Arrays.asList(s.split(WebConstants.DELIMITER_COMMA)));
        return ids;
    }

    public static List<String> toList(String s, String splitStr) {
        if (s == null || s.isEmpty())
            return new ArrayList<String>();
        List<String> ids = new ArrayList<String>(Arrays.asList(s.split(splitStr)));
        return ids;
    }

    public static String toLikeString(String s) {

        return "%" + s + "%";
    }

    public static String toSerialNumber(int number) {
        return String.format("%03d", number);
    }

    public static String toString(List<String> list) {
        return Joiner.on(WebConstants.DELIMITER_COMMA).join(list);
    }

    public static Set<String> toSet(String strs) {
        Set<String> set = new HashSet<String>();
        set.addAll(toList(strs));
        return set;
    }

    public static String collectionToStr(Iterable<String> parts) {
        return Joiner.on(WebConstants.DELIMITER_COMMA).join(parts);
    }

    public static boolean hasRepeat(List<? extends Object> list) {
        if (null == list)
            return false;
        return list.size() != new HashSet<Object>(list).size();
    }

    public static boolean allSame(List<? extends Object> list) {
        if (null == list)
            return false;
        return 1 == new HashSet<Object>(list).size();
    }

    public static boolean hasSame(Collection<?> c1, Collection<?> c2) {
        return !Collections.disjoint(c1, c2);
    }

    public static void main(String args[]) {
//        String formatted = Integer.toHexString((int) System.currentTimeMillis());
//        System.out.println(formatted.toUpperCase());
    }


    public static String formatDistrict(String district) throws Exception {
        if (district == null) {
            return null;
        }
        if (district.substring(2, 6).equals("0000")) {//传入省,查市
            if(district.substring(0, 2).equals("11")
                    || district.substring(0, 2).equals("12")
                    || district.substring(0, 2).equals("31")
                    || district.substring(0, 2).equals("50")
            )
            return district.substring(0,2) + "01__";
            return district.substring(0, 2) + "__00";
        }
        if (district.substring(4, 6).equals("00")) {//传入市,查区
            return district.substring(0, 4) + "__";
        }
        return district;
    }

    /**
     * 依据系统时间产生8位数
     * @return
     */
    public static String getTimeNum(){
        String formatted = Integer.toHexString((int) System.currentTimeMillis());
        return formatted.toLowerCase();
    }

}
