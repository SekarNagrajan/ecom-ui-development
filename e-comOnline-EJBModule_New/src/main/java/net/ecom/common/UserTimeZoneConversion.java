/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.naming.NamingException;
import static net.ecom.common.DateConversion_New.strDbFormat;

/**
 *
 * @author priyadharshinim
 */
public class UserTimeZoneConversion {

    //    dateconversion based on preftime
    static String strDbFormat = GeneralConf.getStrDbFormat();

    public String getUserDateFormat(Date input, String timezone, Map versionUTC) throws NamingException {
        String dateConverted = "";
        Date addSubHours = null;
      //  System.out.println("[ECOM] UserTimeZoneConversion   input Date is: " + input);

        try {

            Calendar cal = Calendar.getInstance();
            if (timezone != null) {

                String[] sp = timezone.split(":");
                int hhh = Integer.parseInt(sp[0]);
                int mmm = Integer.parseInt(sp[1]);
                if (timezone.contains("-")) {
                    cal.setTime(input);
                    cal.add(10, hhh);
                    cal.add(12, -mmm);
                    addSubHours = cal.getTime();
                //    System.out.println("[ECOM] UserTimeZoneConversion   if - returned addSubHours String is : " + addSubHours);

                } else {

                    cal.setTime(input);
                    cal.add(10, hhh);
                    cal.add(12, mmm);
                    addSubHours = cal.getTime();
                //    System.out.println("[ECOM] UserTimeZoneConversion   else - returned addSubHours String is : " + addSubHours);

                }
            }

            if (versionUTC.containsKey("UTC")) {
                String UTCTime = (String) versionUTC.get("UTC");
                String[] sp1 = UTCTime.split(":");

                int h = Integer.parseInt(sp1[0]);
                int m = Integer.parseInt(sp1[1]);
                if (UTCTime.contains("-")) {
                    cal.setTime(addSubHours);
                    cal.add(10, h);
                    cal.add(12, -m);
                    addSubHours = cal.getTime();
                //    System.out.println("[ECOM] UserTimeZoneConversion   if utc - returned addSubHours String is: " + addSubHours);
                } else {
                    cal.setTime(addSubHours);
                    cal.add(10, h);
                    cal.add(12, m);
                    addSubHours = cal.getTime();
               //     System.out.println("[ECOM] UserTimeZoneConversion   else utc - returned addSubHours String is: " + addSubHours);

                }
            }

            SimpleDateFormat formatter = new SimpleDateFormat(GeneralConf.getStrDbFormat());

            if (addSubHours != null) {
              //  System.out.println("[ECOM] UserTimeZoneConversion   if condition addSubHours:" + addSubHours);
                dateConverted = formatter.format(addSubHours);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

     //   System.out.println("[ECOM] UserTimeZoneConversion   finalreturned String is: " + dateConverted);

        return dateConverted;
    }

}
