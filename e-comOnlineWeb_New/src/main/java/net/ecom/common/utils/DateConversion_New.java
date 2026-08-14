/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;

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

    public static String convertDefaultFormatTime(String strDate) {
        String strConfDefDate = GeneralConf.getStrConfDefDate();
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd");
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
            String dateformat[] = Validation.checkNull(trackingDateFormat).split(" ");
            if (dateformat != null && dateformat.length > 1) {
                strConfDefDate = strConfDefDate + " " + dateformat[1];
            }
            if ((strDate != null) && (strDate.length() > 0) && !"-".equalsIgnoreCase(strDate)) {
                SimpleDateFormat source = new SimpleDateFormat(trackingDateFormat);
                SimpleDateFormat traget = new SimpleDateFormat(strConfDefDate);
                strDate = traget.format(source.parse(strDate));
            } else {
                strDate = "";
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
                strDate = traget.format(source.parse(strDate.trim()));
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
            if (date1 != null && date1.length() > 0 && date2 != null && date2.length() > 0 && (!"undefined".equalsIgnoreCase(date1)) && (!"undefined".equalsIgnoreCase(date2))) {
                SimpleDateFormat source = new SimpleDateFormat(strConfDefDate);
                Date d1 = source.parse(date1);
                Date d2 = source.parse(date2);
                System.out.println("[ECOM-WAR] DateConversion_New >>d1.compareTo(d2)>>>" + d1.compareTo(d2));
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
            //System.out.println("[ECOM-WAR] DateConversion_New =====================>>>>>>daysdifferent");
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

    public static String getCurrentDateTime() {
        String currentDate = null;
        try {
            SimpleDateFormat target = new SimpleDateFormat(GeneralConf.getStrConfDefDate() + " " + "HH:mm");
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
            System.out.println("[ECOM-WAR] DateConversion_New date" + date);
            cal.add(Calendar.HOUR_OF_DAY, -hrs);
            result = target.format(cal.getTime());
            System.out.println("[ECOM-WAR] DateConversion_New reslt==" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String TrackingcutOffCalc(String dateVal, int hrs, String excludeweekend) {
        String result = null;
        String strConfDefDate = GeneralConf.getStrConfDefDate() + " " + "HH:mm";
        SimpleDateFormat source = new SimpleDateFormat(strDbFormat);
        SimpleDateFormat target = new SimpleDateFormat(strConfDefDate);
        try {
            Date date = source.parse(dateVal);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if ("Y".equalsIgnoreCase(excludeweekend)) {
                int hours = hrs;
                while (hours > 0) {
                    int step = 0;
                    if (hours > 24) {
                        step = 24;
                    } else {
                        step = hours;
                    }
                    hours -= step;
                    cal.add(Calendar.HOUR_OF_DAY, -step);
                    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SATURDAY) {
                        hours += 24;
                    }
                    if (dayOfWeek == Calendar.SUNDAY) {
                        hours += 24;
                    }
                }
            } else {
                cal.add(Calendar.HOUR_OF_DAY, -hrs);
            }
            result = target.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String convertETATrackingFormat(String strDate, String trackingDateFormat, String carrierCode) {
        System.out.println("[ECOM-WAR] DateConversion_New strDate=>" + strDate + "trackingDateFormat=>" + trackingDateFormat + "carrierCode=>" + carrierCode);
        String strConfDefDate = GeneralConf.getStrConfDefDate();
        if ("In Transit".equalsIgnoreCase(strDate)) {
            return strDate;
        }
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                SimpleDateFormat source = new SimpleDateFormat(trackingDateFormat);
                SimpleDateFormat traget = new SimpleDateFormat(strConfDefDate);
                Date d1 = new Date();
                Date d2 = source.parse(strDate);
                if (d2.compareTo(d1) > 0 || (!"ESL".equalsIgnoreCase(carrierCode))) {
                    strDate = traget.format(source.parse(strDate));
                } else {
                    strDate = "";
                }
            } else {
                strDate = "";
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        System.out.println("[ECOM-WAR] DateConversion_New strDate=>" + strDate);
        return strDate;
    }

    public static int daysDifferentBetweenTwoDays(String fromdate, String todate) throws ParseException {
        SimpleDateFormat formatter = null;
        Date depaturedate = null;
        Date arrdate = null;
        int result = 0;
        try {
            System.out.println("[ECOM-WAR] DateConversion_New ====>>daysDifferentBetweenTwoDays=>" + fromdate + " & " + todate);
            String strConfDefDateTimeFormat = GeneralConf.getStrConfDefDate();
            SimpleDateFormat source = new SimpleDateFormat(strConfDefDateTimeFormat);
            SimpleDateFormat traget = new SimpleDateFormat("yyyy-MM-dd");
            fromdate = traget.format(source.parse(fromdate));
            todate = traget.format(source.parse(todate));

            formatter = new SimpleDateFormat("yyyy-MM-dd");
            depaturedate = formatter.parse(fromdate);
            arrdate = formatter.parse(todate);
            result = (int) ((depaturedate.getTime() - arrdate.getTime()) / (24 * 60 * 60 * 1000));
            result = Math.abs(result);
        } catch (Exception e) {
            result = 0;
            e.printStackTrace();
        }
        return result;
    }

    public static int calculateTrackingPercentage(String fromdate, String todate, String trackingDateFormat) throws ParseException {
        SimpleDateFormat formatter = null;
        Date depaturedate = null;
        Date arrdate = null;
        int trackpercentage = 0, totalmins = 0, curmins = 0;
        float minsofhundprcnt = 0.0f, totalpercentage = 0.0f;
        //        // Prevent parsing special values like "In Transit"
         if ("In Transit".equalsIgnoreCase(fromdate) || "In Transit".equalsIgnoreCase(todate)) {
            return 0; // -1 or 0, or any value that means "not applicable"
        }
        try {
            if (fromdate != null && todate != null) {
                fromdate = fromdate.trim();
                todate = todate.trim();
                //System.out.println("[ECOM-WAR] DateConversion_New =====================>>>>>>daysdifferent");
                //  String strConfDefDateTimeFormat = GeneralConf.getStrConfDefDate();
                SimpleDateFormat source = new SimpleDateFormat(trackingDateFormat);
                SimpleDateFormat traget = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                fromdate = traget.format(source.parse(fromdate));
                todate = traget.format(source.parse(todate));

                depaturedate = traget.parse(fromdate);
                arrdate = traget.parse(todate);
                if (depaturedate.compareTo(new Date()) > 0) {
                    trackpercentage = -1;
                } else if (arrdate.compareTo(new Date()) < 0) {
                    trackpercentage = 93;
                } else {
                    totalmins = (int) ((depaturedate.getTime() - arrdate.getTime()) / (60 * 1000));
                    totalmins = Math.abs(totalmins);
                    curmins = (int) ((depaturedate.getTime() - new Date().getTime()) / (60 * 1000));
                    curmins = Math.abs(curmins);
                    minsofhundprcnt = (float) ((totalmins * 100) / 93);
                    totalpercentage = (float) ((curmins * 100) / minsofhundprcnt);
                    trackpercentage = Math.round(totalpercentage);
                }
            }
        } catch (Exception e) {
            trackpercentage = -1;
            e.printStackTrace();
        }
        return trackpercentage;
    }

    public static String getCurrentDateDbFormat() {
        String currentDate = null;
        try {
            SimpleDateFormat target = new SimpleDateFormat(strDbFormat);
            currentDate = target.format(new Date());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentDate;
    }

    public static Date convertStringToDate(String date) {
        Date result = null;
        try {
            if (date != null) {
                SimpleDateFormat target = new SimpleDateFormat(GeneralConf.getStrConfDefDate());
                result = target.parse(date);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String convertDateToString(Date date) {
        String result = null;
        try {
            if (date != null) {
                SimpleDateFormat target = new SimpleDateFormat(strDbFormat);
                result = target.format(date);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String convertsubHours(String strdate, int hrs) {
        String result = "";
        try {
            System.out.println("[ECOM-WAR] DateConversion_New inside convertsubHours");
            result = strdate;
            String strConfDefDateTimeFormat = GeneralConf.getStrConfDefDateTimeFormat();
            SimpleDateFormat sdt = new SimpleDateFormat(strDbFormat);
            SimpleDateFormat traget = new SimpleDateFormat(strConfDefDateTimeFormat);
            if (strdate != null && strdate.length() > 0 && hrs > 0) {

                Date date = sdt.parse(strdate);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.HOUR, -hrs);
                Date rsdate = cal.getTime();
                result = sdt.format(rsdate);
            }
            result = traget.format(sdt.parse(result.trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM-WAR] DateConversion_New result => " + result);
        return result;
    }

    public static long calculateMinutesbetweentwoDay(String date1) {
        Date dt1 = null;
        long diffMinutes = 0;
        try {
            SimpleDateFormat source = new SimpleDateFormat(strDbFormat);
            System.out.println("[ECOM-WAR] DateConversion_New calculateMinutesbetweentwoDay=>" + date1);
            Date dt2 = new Date();
            dt1 = source.parse(date1);
            long diff = dt2.getTime() - dt1.getTime();
            diffMinutes = diff / 1000 / 60;
            System.out.println("[ECOM-WAR] DateConversion_New diffMinutes=> " + diffMinutes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffMinutes;
    }

    public static int daysDifferentBetweenTwoDaysTrackETA(String fromdate, String todate) throws ParseException {
        SimpleDateFormat formatter = null;
        Date depaturedate = null;
        Date arrdate = null;
        int result = 0;
        if ("In Transit".equalsIgnoreCase(fromdate) || "In Transit".equalsIgnoreCase(todate)) {
            return 0;
        }
        try {
            System.out.println("[ECOM-WAR] DateConversion_New ===>>>>daysDifferentBetweenTwoDays=>" + fromdate + " & " + todate);
            String strConfDefDateTimeFormat = GeneralConf.getStrConfDefDate();
            SimpleDateFormat source = new SimpleDateFormat(strConfDefDateTimeFormat);
            SimpleDateFormat traget = new SimpleDateFormat("yyyy-MM-dd");
            fromdate = traget.format(source.parse(fromdate));
            todate = traget.format(source.parse(todate));

            formatter = new SimpleDateFormat("yyyy-MM-dd");
            depaturedate = formatter.parse(fromdate);
            arrdate = formatter.parse(todate);
            result = (int) ((depaturedate.getTime() - arrdate.getTime()) / (24 * 60 * 60 * 1000));
//            result = Math.abs(result);
        } catch (Exception e) {
            result = 0;
            e.printStackTrace();
        }
        return result;
    }
}
