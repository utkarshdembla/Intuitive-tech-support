package com.techSupport.intuitiveTechSupportapi.utility;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtility {


      public Date getDateFromDateString(String dateString,String dateFormat) throws ParseException {
        DateFormat format = new SimpleDateFormat(dateFormat);
        return format.parse(dateString);
    }

    public String getDateStringFromDate(Date date,String dateFormat)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    public Date getFutureDate(int numberOfDays) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, numberOfDays);
        return cal.getTime();
    }


    public Date getFutureTime(int hours) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,0);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }

    public Date getDateWithTime(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        if(date==null){
            final Calendar calendar = Calendar.getInstance();
            return getDateFromDateString(formatter.format(calendar.getTime()),"HH:mm:ss");
        }
        return getDateFromDateString(formatter.format(date),"HH:mm:ss");
    }


}
