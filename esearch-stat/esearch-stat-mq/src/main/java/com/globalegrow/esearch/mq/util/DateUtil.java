package com.globalegrow.esearch.mq.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 获取某日期的年份
     *
     * @param date
     * @return
     */
    public static Integer getYear(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取某日期的月份
     *
     * @param date
     * @return
     */
    public static String getMonth(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datestr = sdf.format(date);
        String [] datearr = datestr.split("-");
        String month = datearr[1];
        return month;
    }

    /**
     * 获取某日期的日数
     *
     * @param date
     * @return
     */
    public static String getDay(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datestr = sdf.format(date);
        String [] datearr = datestr.split("-");
        String day = datearr[2];
        return day;
    }

    public static boolean isAllListLead(long ytime,long ttime){
        Calendar c = Calendar.getInstance();
        Date ydate = new Date(ytime);
        c.setTime(ydate);
        int ymonth = c.get(Calendar.MONTH);
        int yday = c.get(Calendar.DAY_OF_MONTH);
        int yyear = c.get(Calendar.YEAR);
        Date tdate = new Date(ttime);
        c.setTime(tdate);
        int tmonth = c.get(Calendar.MONTH);
        int tday = c.get(Calendar.DAY_OF_MONTH);
        int tyear = c.get(Calendar.YEAR);
        if(ymonth == tmonth && yday == tday && yyear == tyear){
            return false;
        }else{
            return true;
        }
    }
}
