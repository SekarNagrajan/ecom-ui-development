package net.ecom.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateConversion {

    public static String convertDefaultFormat(String strDefDateFormat, String strDate) {
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                strDefDateFormat = strDefDateFormat.replace("mm", "MM");
                strDefDateFormat = strDefDateFormat.replace("yy", "yyyy");
                strDefDateFormat = strDefDateFormat.replace(":MM", ":mm");
                SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat dateFormat = new SimpleDateFormat(strDefDateFormat);
                strDate = dateFormat.format(sdt.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertDBFormat(String strDefDateFormat, String strDate) {
        try {
            String strdate1 = strDate;
            if ((strDate != null) && (strDate.length() > 0)) {
                strDefDateFormat = strDefDateFormat.replace("mm", "MM");
                strDefDateFormat = strDefDateFormat.replace("yy", "yyyy");
                SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat(strDefDateFormat);
                strDate = sdt.format(dateFormat.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertDefaultTimeFormat(String strDefDateFormat, String strDate) {
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                strDefDateFormat = strDefDateFormat.replace("mm", "MM");
                if (!(strDefDateFormat.contains("yyyy"))) {
                    strDefDateFormat = strDefDateFormat.replaceAll("yy", "yyyy");
                }
                SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat(strDefDateFormat + " " + "HH:mm");
                strDate = dateFormat.format(sdt.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertEcomTimeFormat(String strDefDateFormat, String strDate) {
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                strDefDateFormat = strDefDateFormat.replace("mm", "MM");
                SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat(strDefDateFormat + " " + "HH:mm");
                strDate = dateFormat.format(sdt.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertDBTimeFormat(String strDefDateFormat, String strDate) {
        try {
            if ((strDate != null) && (strDate.length() > 0)) {

                strDefDateFormat = strDefDateFormat.replace("mm", "MM");
                SimpleDateFormat targetDate = new SimpleDateFormat(strDefDateFormat + " " + "HH:mm");
                SimpleDateFormat finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                strDate = finalDate.format(targetDate.parse(strDate));

            } else {
                strDate = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public String convertNormalFormat(String strSrc, String strtarget, String strDate) {
        return strDate;
    }
}
