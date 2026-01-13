package com.espe.ListoEgsi.util;

import java.sql.Date;

public class DateUtils {

    

    public String getDateFormat(Date date) {
        String datePattern = "dd/MM/yyyy";
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat(datePattern);
        return simpleDateFormat.format(date);
    }

    public static String getDateNow() {
    return java.time.LocalDate.now()
               .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
}

}
