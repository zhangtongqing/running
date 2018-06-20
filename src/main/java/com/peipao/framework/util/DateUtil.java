package com.peipao.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_AND_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE_AND_TIME_MILLISECOND = "yyyy-MM-dd HH:mm:ss SSS";

    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd
     */
    public static final int DEFAULT = 0;
    public static final int YM = 1;

    /**
     * 变量：日期格式化类型 - 格式:yyyy-MM-dd
     */
    public static final int YMR_SLASH = 11;

    /**
     * 变量：日期格式化类型 - 格式:yyyyMMdd
     */
    public static final int NO_SLASH = 2;

    /**
     * 变量：日期格式化类型 - 格式:yyyyMM
     */
    public static final int YM_NO_SLASH = 3;

    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd HH:mm:ss
     */
    public static final int DATE_TIME = 4;

    /**
     * 变量：日期格式化类型 - 格式:yyyyMMddHHmmss
     */
    public static final int DATE_TIME_NO_SLASH = 5;

    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd HH:mm
     */
    public static final int DATE_HM = 6;

    /**
     * 变量：日期格式化类型 - 格式:HH:mm:ss
     */
    public static final int TIME = 7;

    /**
     * 变量：日期格式化类型 - 格式:HH:mm
     */
    public static final int HM = 8;

    /**
     * 变量：日期格式化类型 - 格式:HHmmss
     */
    public static final int LONG_TIME = 9;
    /**
     * 变量：日期格式化类型 - 格式:HHmm
     */

    public static final int SHORT_TIME = 10;

    /**
     * 变量：日期格式化类型 - 格式:yyyy-MM-dd HH:mm:ss
     */
    public static final int DATE_TIME_LINE = 12;

    public static String dateToStr(Date date, int type) {
        switch (type) {
            case DEFAULT:
                return dateToStr(date);
            case YM:
                return dateToStr(date, "yyyy/MM");
            case NO_SLASH:
                return dateToStr(date, "yyyyMMdd");
            case YMR_SLASH:
                return dateToStr(date, "yyyy-MM-dd");
            case YM_NO_SLASH:
                return dateToStr(date, "yyyyMM");
            case DATE_TIME:
                return dateToStr(date, "yyyy/MM/dd HH:mm:ss");
            case DATE_TIME_NO_SLASH:
                return dateToStr(date, "yyyyMMddHHmmss");
            case DATE_HM:
                return dateToStr(date, "yyyy/MM/dd HH:mm");
            case TIME:
                return dateToStr(date, "HH:mm:ss");
            case HM:
                return dateToStr(date, "HH:mm");
            case LONG_TIME:
                return dateToStr(date, "HHmmss");
            case SHORT_TIME:
                return dateToStr(date, "HHmm");
            case DATE_TIME_LINE:
                return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
            default:
                throw new IllegalArgumentException("Type undefined : " + type);
        }
    }

    public static String dateToStr(Date date, String pattern) {
        if (date == null || date.equals(""))
            return null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static String dateToStr(Date date) {
        return dateToStr(date, "yyyy/MM/dd");
    }

    /**
     * 格式[yyyy-MM-dd]
     *
     * @param strDate
     * @return
     */
    public static Date parseDate(String strDate) {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date parseDate(String strDate,String pattern) {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date stringToDate(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        Long time = new Long(date);

        String d = format.format(time);

        Date result = null;
        try {
            result = format.parse(d);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static Date stringToDate(String date) {
        return stringToDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getDateAfterDays(Date date, int day) {
        if (date!=null){
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, day);// 把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
            return date;
        }else{
            return null;
        }

    }

    public static Date getDateAfterYears(Date date, int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        return date;
    }
    
    public static Date getDateAfterMonths(Date date, int months) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        return date;
    }

    public static Date getDateAfterMinutes(Date date, int minute) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        return date;
    }

    public static Date getDateAfterHours(Date date, int hour) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        return date;
    }

    public static int getMinDiff(Date earlierDate, Date laterDate) {
        return (int) (TimeUnit.MILLISECONDS.toMinutes(getTimeDiff(earlierDate, laterDate)));
    }
    
    public static int getSecDiff(Date earlierDate, Date laterDate) {
        return (int) (TimeUnit.MILLISECONDS.toSeconds(getTimeDiff(earlierDate, laterDate)));
    }

    public static long getTimeDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null)
            return 0;
        return earlierDate.getTime() - laterDate.getTime();
    }

    public static int getDayDiff(Date earlierDate, Date laterDate) {
        return (int) (TimeUnit.MILLISECONDS.toDays(getTimeDiff(earlierDate, laterDate)));
    }

    public static String getDayOfWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static Date getZero(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -calendar.get(Calendar.HOUR_OF_DAY));
        calendar.add(Calendar.MINUTE, -calendar.get(Calendar.MINUTE));
        calendar.add(Calendar.SECOND, -calendar.get(Calendar.SECOND));
        return calendar.getTime();
    }

    public static Date getEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static String getBeforeTimeFromToTime(Date from, Date to, String prefix, String suffix) {
        String result = "";
        try {
            long l = to.getTime() - from.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            if (day > 0)
                // if(day>3){
                // return dateToStr(from);
                // }else{
                return result = prefix + day + "天" + suffix;
            // }
            if (hour > 0)
                return result = prefix + hour + "小时" + suffix;
            if (min > 0)
                return result = prefix + min + "分钟" + suffix;
            if (s > 0) {
                return result = prefix + s + "秒" + suffix;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getBeforeTimeFromNow(Date time, String prefix, String suffix) {
        return getBeforeTimeFromToTime(new Date(), time, prefix, suffix);
    }

    /**
     * get first date of given month and year
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {
        String monthStr = month < 10 ? "0" + month : String.valueOf(month);
        return year + "-" + monthStr + "-" + "01";
    }

    /**
     * get the last date of given month and year
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * get Calendar of given year
     *
     * @param year
     * @return
     */
    private static Calendar getCalendarFormYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    /**
     * get start date of given week no of a year
     *
     * @param year
     * @param weekNo
     * @return
     */
    public static String getStartDayOfWeekNo(int year, int weekNo) {
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);

    }

    /**
     * get the end day of given week no of a year.
     *
     * @param year
     * @param weekNo
     * @return
     */
    public static String getEndDayOfWeekNo(int year, int weekNo) {
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginStr
     * @param endStr
     * @return List<Date>
     */
    public static Set<String> getDatesBetweenTwoDate(String beginStr, String endStr) {
        Date beginDate = parseDate(beginStr);
        Date endDate = parseDate(endStr);

        Date today = new Date();

        if (beginDate.compareTo(today) >= 0) {
            return new TreeSet<String>();
        }

        if (endDate.compareTo(today) >= 0) {
            endDate = today;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        // 转成String
        Set<String> resultDateStr = new TreeSet<String>();
        if (lDate.size() > 0) {
            for (Date date : lDate) {
                resultDateStr.add(sdf.format(date));
            }
        }
        return resultDateStr;
    }

    public static String getStrDateAfterDays(String inputDate, int day) {
        Date afterDay = getDateAfterDays(parseDate(inputDate), day);
        return dateToStr(afterDay, YMR_SLASH);
    }

    public static boolean isBefore(Date date, String startTime) {
        try {
            Date startDate = new SimpleDateFormat("HH:mm").parse(startTime);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            startCalendar.add(Calendar.DATE, 1);

            Date hmDate = new SimpleDateFormat("HH:mm").parse(dateToStr(date, 8));
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTime(hmDate);
            nowCalendar.add(Calendar.DATE, 1);
            return nowCalendar.before(startCalendar);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean isAfter(Date date, String endTime) {
        try {
            Date startDate = new SimpleDateFormat("HH:mm").parse(endTime);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            startCalendar.add(Calendar.DATE, 1);

            Date hmDate = new SimpleDateFormat("HH:mm").parse(dateToStr(date, 8));
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTime(hmDate);
            nowCalendar.add(Calendar.DATE, 1);
            return nowCalendar.after(startCalendar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 当前年的开始日期
     */
    public static Date getCurrentYearStart() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 1);
        String date = DateUtil.dateToStr(c.getTime(), DateUtil.PATTERN_DATE);
        return DateUtil.parseDate(date);
    }

    /**
     * 当前月的开始日期
     */
    public static Date getCurrentMonthStart() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        String date = DateUtil.dateToStr(c.getTime(), DateUtil.PATTERN_DATE);
        return DateUtil.parseDate(date);
    }

    /**
     * 当本周的开始日期
     */
    public static Date getCurrentWeekStart() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, 1);
        String date = DateUtil.dateToStr(c.getTime(), DateUtil.PATTERN_DATE);
        return DateUtil.parseDate(date);
    }


    /**
     * 当前季度的开始日期
     */
    public static Date getCurrentQuarterStart() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3) {
            c.set(Calendar.MONTH, 0);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            c.set(Calendar.MONTH, 3);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            c.set(Calendar.MONTH, 4);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            c.set(Calendar.MONTH, 9);
        }
        c.set(Calendar.DATE, 1);
        String date = DateUtil.dateToStr(c.getTime(), DateUtil.PATTERN_DATE);
        return DateUtil.parseDate(date);
    }

    public static int getBirthYearByAge(String age) {
        int ageInt = Integer.valueOf(age);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(sdf.format(new Date())) - ageInt;
        return year;
    }
    
    /**
	 * 
	 * @param n
	 *            推迟的周数，0本周，-1向前推迟一周，1下周，依次类推
	 * @param week
	 *            周几 1,2,3,4,5,6,7
	 * @return
	 */
	public static Date getWeek(int n, int week) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, n * 7);
		cal.set(Calendar.DAY_OF_WEEK, week);
		return cal.getTime();
	}
	
	public static String secToTime(String str,Long ms) {  
		Integer ss = 1000;  
	    Integer mi = ss * 60;  
	    Integer hh = mi * 60;  
	    Integer dd = hh * 24;  
	    
	    StringBuffer sb = new StringBuffer(); 
	    sb.append(str);
	    if(ms == 0){
	    	if(str.equals("耗时累计：") || str.equals("时隔：") || str.equals("总耗时：")){
	    		sb.append("0秒");
	    	}
	    	if(str.equals("未访天数：")){
	    		sb.append("0天");
	    	}
	    	
	    	return sb.toString();
	    }
	  
	    Long day = ms / dd;  
	    Long hour = (ms - day * dd) / hh;  
	    Long minute = (ms - day * dd - hour * hh) / mi;  
	    Long second = (ms - day * dd - hour * hh - minute * mi) / ss;  
	      
	    if(day > 0) { 
	        sb.append(day+"天");  
            if(str.equals("未访天数：")){
	    		return sb.toString();
	    	}
	    }  
	    if(hour > 0) {  
	        sb.append(hour+"小时");  
	    }  
	    if(minute > 0) {  
	        sb.append(minute+"分钟");  
	    }  
	    if(second > 0) {  
	        sb.append(second+"秒");  
	    }  
	    return sb.toString();  
    }

    public static int getWeekCount(String startDate, String endDate) {
        Calendar calendar = Calendar.getInstance();
        String[] array = {startDate, endDate};
        Date[] ds = new Date[array.length];
        for(int i = 0; i < array.length; i++) {
            String[] fs = array[i].split("[^\\d]+");
            calendar.set(Integer.parseInt(fs[0]), Integer.parseInt(fs[1]) - 1, Integer.parseInt(fs[2]));
            ds[i] = calendar.getTime();
        }
        int count = 1;


        for(Date d = ds[0]; d.compareTo(ds[1]) <= 0;) {
            calendar.setTime(d);
            calendar.add(Calendar.DATE, 1);
            d = calendar.getTime();
            if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
//                System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(d));
                count++;
            }
        }
        return count;
    }

    public static String second2Time(int seconds) {
        int temp=0;
        StringBuffer sb=new StringBuffer();
        temp = seconds/3600;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600/60;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600%60;
        sb.append((temp<10)?"0"+temp:""+temp);

        return sb.toString();
    }

    public static String second2TimeString(int seconds) {
        int temp = 0;
        StringBuffer sb=new StringBuffer();
        temp = seconds/3600;
        if(temp > 0) {
            sb.append((temp < 10) ? "0" + temp + "小时" : "" + temp + "小时");
        }
        temp=seconds%3600/60;
        if(temp > 0) {
            sb.append((temp < 10) ? "0" + temp + "分钟" : "" + temp + "分钟");
        }
        temp=seconds%3600%60;
        if(temp > 0) {
            sb.append((temp < 10) ? "0" + temp + "秒" : "" + temp + "秒");
        }

        return sb.toString();
    }


    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long oneday = 24 * hour;// 1天
    private final static long twoday = 48 * hour;// 2天
    private final static long month = 31 * oneday;// 月
    private final static long year = 12 * month;// 年

    /*
    内容发布时间展示规则：
    当年：
    1分钟内：刚刚
    1小时内：XX分钟前
    24小时内：XX小时前
    48小时内：昨天
    1月内（30天内即30*24小时内）：X天前 。X=（当前时间-发布时间）/24*3600 取整数部分，只取整数部分。
    1月及1月以上：xx月xx日
    往年：在信息流中标出年份，信息内容时间展示格式：XXXX年xx月xx日
     */

    public static String getTimeFormatText(Long date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date;
        long r = 0;
        if (diff > year) {
            //r = (diff / year);//return  r + "年前";
            return  dateToStr(new Date(date), "yyyy年MM月dd");//XXXX年xx月xx日
        }
        if (diff > month) {//超过一个月但不超过一年
            //r = (diff / month);//return r + "个月前";
            return  dateToStr(new Date(date), "MM月dd");//xx月xx日
        }

        if (diff > twoday) {//超过48小时,但在1个月内
            r = (diff / oneday);
            return r + "天前";
        }
        if (twoday > diff && diff > oneday) {//48小时内：昨天
            return "昨天";
        }
        if (diff > hour) {//24小时内：XX小时前
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {//1小时内：XX分钟前
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";//1分钟内：刚刚
    }

    /**
     * 给定起始日期段   输出期间所有日期
     * @param start
     * @param end
     * @return
     */
    public static List<Date> getBetweenDates(Date start, Date end) {
        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        //tempStart.add(Calendar.DAY_OF_YEAR, -1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    public static String getDateString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static void main(String[] args){
        System.out.println(second2TimeString(2090));

        //System.out.println(second2Time(20090));
    }

}
