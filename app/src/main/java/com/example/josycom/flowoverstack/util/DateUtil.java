package com.example.josycom.flowoverstack.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String toNormalDate(long seconds){
        Date date = new Date(seconds * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}
