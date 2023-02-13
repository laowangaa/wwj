package cn.cyberict.ncha.business.commons.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class TimeUtils {

    
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }

    
    public static Date getDayNext(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1); 
        date = calendar.getTime();
        return date;
    }

    
    public static Date getMonthFirstDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.add(Calendar.MONTH,0);
        return calendar.getTime();
    }


    
    public static Date getLastMonthFirstDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); 
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); 
        date = calendar.getTime();
        return date;
    }
}
