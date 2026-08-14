/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author sasikumar.r
 */
public class DateConversion_New {

    // static String strConfDefDateTimeFormat = GeneralConf.getStrConfDefDateTimeFormat();
    //  static String strConfDefDate = GeneralConf.getStrConfDefDate();
    static String strDbFormat = GeneralConf.getStrDbFormat();

    public static String convertDefaultFormat(String strDate) {
        String strConfDefDate = GeneralConf.getStrConfDefDate();
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat(strDbFormat);
                SimpleDateFormat traget = new SimpleDateFormat(strConfDefDate);
                strDate = traget.format(source.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertDefaultFormatwithTime(String strDate) {
        String strConfDefDate = GeneralConf.getStrConfDefDate() + " " + "HH:mm";
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat(strDbFormat);
                SimpleDateFormat traget = new SimpleDateFormat(strConfDefDate);
                strDate = traget.format(source.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertDefaultDBFormatwithTime(String strDate) {
        String strConfDefDate = GeneralConf.getStrConfDefDate() + " " + "HH:mm";
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat(strConfDefDate);
                SimpleDateFormat traget = new SimpleDateFormat(strDbFormat);
                strDate = traget.format(source.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertTrackingFormat(String strDate, String trackingDateFormat) {
        String strConfDefDate = GeneralConf.getStrConfDefDate();
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat(trackingDateFormat);
                SimpleDateFormat traget = new SimpleDateFormat(strConfDefDate);
                strDate = traget.format(source.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertDefaultTimeFormat(String strDate) {
        String strConfDefDateTimeFormat = GeneralConf.getStrConfDefDateTimeFormat();
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat(strDbFormat);
                SimpleDateFormat traget = new SimpleDateFormat(strConfDefDateTimeFormat);
                strDate = traget.format(source.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertDefaultwithoutTimeFormat(String strDate) {
        String strConfDefDateTimeFormat = GeneralConf.getStrConfDefDateTimeFormat();
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat(strDbFormat);
                SimpleDateFormat traget = new SimpleDateFormat(strConfDefDateTimeFormat);
                strDate = traget.format(source.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertDefaultDBFormat(String strDate) {
        String strConfDefDate = GeneralConf.getStrConfDefDate();
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat(strConfDefDate);
                SimpleDateFormat traget = new SimpleDateFormat(strDbFormat);
                strDate = traget.format(source.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String convertDefaultDBFormatwithoutTime(String strDate) {
        String strConfDefDate = GeneralConf.getStrConfDefDate();
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat(strConfDefDate);
                SimpleDateFormat traget = new SimpleDateFormat("yyyy-MM-dd");
                strDate = traget.format(source.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static String compareTwoDate(String date1, String date2) {
        String strConfDefDate = GeneralConf.getStrConfDefDate();
        String result = "N";
        try {
            if (date1 != null && date1.length() > 0 && date2 != null && date2.length() > 0) {
                SimpleDateFormat source = new SimpleDateFormat(strConfDefDate);
                Date d1 = source.parse(date1);
                Date d2 = source.parse(date2);
                System.out.println("[ECOM] DateConversion_New >>d1.compareTo(d2)>>>" + d1.compareTo(d2));
                if (d1.compareTo(d2) > 0) {
                    result = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String convertDefaultDBFormatForTime(String strDate) {
        String strConfDefDateTimeFormat = GeneralConf.getStrConfDefDateTimeFormat();
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat(strConfDefDateTimeFormat);
                SimpleDateFormat traget = new SimpleDateFormat(strDbFormat);
                strDate = traget.format(source.parse(strDate));
            } else {
                strDate = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    public static int daysdifferent(String startdate) throws ParseException {
        SimpleDateFormat formatter = null;
        Date depaturedate = null;
        String todaydate = null;
        Date today_date = null;
        int result = 0;
        try {
            //System.out.println("[ECOM] DateConversion_New=====================>>>>>>daysdifferent");
            String strConfDefDateTimeFormat = GeneralConf.getStrConfDefDate();
            SimpleDateFormat source = new SimpleDateFormat(strConfDefDateTimeFormat);
            SimpleDateFormat traget = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = traget.format(source.parse(startdate));

            formatter = new SimpleDateFormat("yyyy-MM-dd");
            depaturedate = formatter.parse(strDate);
            todaydate = formatter.format(new Date());
            today_date = formatter.parse(todaydate);
            result = (int) ((depaturedate.getTime() - today_date.getTime()) / (24 * 60 * 60 * 1000));

        } catch (Exception e) {
            result = 0;
            e.printStackTrace();
        }
        return result;
    }

    public static Date convertaddDays(Date d, int days) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, days);
        Date date = cal.getTime();

        return date;
    }

    public static Date convertsubDays(Date d, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, -days);
        Date date = cal.getTime();
        return date;
    }
    public static String getCurrentDate() {
        String currentDate = null;
        try {
            SimpleDateFormat target = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
            currentDate = target.format(new Date());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentDate;
    }

    public static String cutOffCalc(String dateVal, int hrs) {
        String result = null;
        String strConfDefDate = GeneralConf.getStrConfDefDate() + " " + "HH:mm";
        SimpleDateFormat source = new SimpleDateFormat(strDbFormat);
        SimpleDateFormat target = new SimpleDateFormat(strConfDefDate);
        try {
            Date date = source.parse(dateVal);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR_OF_DAY, -hrs);
            result = target.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
