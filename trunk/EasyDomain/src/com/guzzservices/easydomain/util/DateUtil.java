package com.guzzservices.easydomain.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期和时间相关工具方法. <BR>
 * 
 */
public class DateUtil {
 
    /**
     * 完成日期串到日期对象的转换. <BR>
     * @param dateString 日期字符串
     * @param dateFormat 日期格式
     * @return date 日期对象
     */
    public static Date stringToDate(String dateString, String dateFormat) {
        if ("".equals(dateString) || dateString == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(dateFormat).parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }
      
    public static String date2String(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            return null;
        }
    }  
 
    public static String date2String(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm");
    }
	
	
}