package com.peipao.framework.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {
	
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年
	
	public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmssSSS";
    public final static String YYYYMMDDHHMMSS2 = "yyyyMMddHHmmss";

    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_AND_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE_AND_TIME_MILLISECOND = "yyyy-MM-dd HH:mm:ss SSS";

	
    /**
     * @return
     * @author neo
     * @date 2015-5-21
     */
    public static String getDateSequence() {
        return new SimpleDateFormat(YYYYMMDDHHMMSS).format(new Date());
    }

    public static Date parseDate(String dateStr) {
        Date returnDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS2);
        try {
            returnDate = sdf.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return returnDate;
    }

    public static String getDateString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String long2Date(Long d, String format) {
        return new SimpleDateFormat(format).format(d);
    }

    public static String getDateStringDefault(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStampStr2DateStr(String seconds,String format) {
        if(StringUtil.isEmpty(seconds) || seconds.equals("null")){
            return "";
        }
        if(StringUtil.isEmpty(format)) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    public static Date parseDate(String dateStr, String format) {
        Date returnDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            returnDate = sdf.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return returnDate;
    }


	/**
	 * @author neo
	 * @date 2016年8月10日
	 * @return
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}
	
	
    public static String getTimeFormatText(Long date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date;
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 将时间戳转换成当天0点
     * @param timestamp
     * @return
     */
    public static long getDayBegin(long timestamp) {
        String format = "yyyy-MM-DD";
        String toDayString = new SimpleDateFormat(format).format(new Date(timestamp));
        Date toDay = null;
        try {
            toDay = org.apache.commons.lang3.time.DateUtils.parseDate(toDayString, new String[]{format});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toDay.getTime();
    }

    /**
     * 获取一个月之前的时间戳
     * @return
     */
    public static long getLastMonthTime() {
        return getDayBegin(getCurrentTime())-86400000l*30;
    }

    /*
    * 检查一个字符串是否是有效的年月日期
    * @return Boolean
    **/
    public static boolean checkYYYYMMDD(String dateString) {
        boolean b = false;
        Date date = null;
        SimpleDateFormat  df  =  new  SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);//这个的功能是不把1996-13-3 转换为1997-1-3
        try {
            date = df.parse(dateString);
            b = true;
        } catch(Exception e) {
            b = false;
        }
        return b;
    }

    /**
     * 校验时间格式（仅格式）
     */
    public static boolean checkHHMM(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        try {
            Date t = dateFormat.parse(time);
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * 校验时间格式HH:MM（精确）
     */
    public static boolean checkTimeHHMM(String time) {
        if (checkHHMM(time)) {
            String[] temp = time.split(":");
            if ((temp[0].length() == 2 || temp[0].length() == 1) && temp[1].length() == 2) {
                int h,m;
                try {
                    h = Integer.parseInt(temp[0]);
                    m = Integer.parseInt(temp[1]);
                } catch (NumberFormatException e) {
                    return false;
                }
                if (h >= 0 && h <= 24 && m <= 60 && m >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            String aaa = new SimpleDateFormat("MMddHHmmssSSS").format(new Date());
            System.out.println(aaa);
        }

	}
}
