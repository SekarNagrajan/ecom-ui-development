/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ExportToExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import net.ecom.common.GeneralConf;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author vasanthakumar.r
 */
public class ExcelExportBookingData extends HttpServlet {

    Map hm = null;

    public File ExcelExport(HashMap<String, String> jsonmap, String attachPath, String excelpath, Map EQPHASHMAP, HashMap configmap) {
        File file = null;
        try {
            //
            //Cargo strings
            String eqpType = null, eqpSize = null, eqpQty = null, soc = null, hzd = null, commodity = null, packagename = null, hscode = null;
            String packageQty = null, eqpStatus = null, tareWt = null, cargoWt = null, grossWt = null, volume = null, cargoWtUnit = null;
            String volUnit = null, eqp_type = null, marinePollut = null, unno = null, flashPoint = null, flashUnit = null, commodityothers = null;
            String shippingName = null, dgClass = null, presetTemp = null, minTemp = null, maxTemp = null, tempUnit = null, nor = null;
            String dimUnit = null, owLeft = null, owRight = null, oh = null, olAft = null, olForward = null, containerNo = null;
            String hidden_oog = null, hidden_lcl = null, hidden_rfr = null, packagecodes = null, packagecode = null, commoditycode = null, commodities = null;
            String comid = null, conid = null, othercommodity = null, commodityname = null, unnos = null, unnocode = null, otherhscode = null;
            String commodityvalue = "", commodityanswer = "", ventilation = null, co2 = null, o2 = null, humidity = null, drains = null;
            ArrayList<String> conselect = new ArrayList<String>();
            hm = new HashMap();
            hm.put("D", "Dry");
            hm.put("O", "Open");
            hm.put("R", "Reefer");
            hm.put("K", "Kilos");
            hm.put("P", "Pounds");
            hm.put("T", "Metric Tons");
            hm.put("N", "No");
            hm.put("Y", "Yes");
            hm.put("n", "No");
            hm.put("y", "Yes");
            hm.put("C", "Celsius");
            hm.put("F", "Fahrenheit");
            //
            HSSFWorkbook book = new HSSFWorkbook();
            HSSFRow row;
            HSSFCell cell;
            HSSFSheet sheet;
            int consize = 1;
            int k = 7;
            //
            FileInputStream myFileSystem = new FileInputStream(new File(attachPath));
            book = new HSSFWorkbook(myFileSystem);
            sheet = book.getSheetAt(0);
            String Ventilationlabel = (String) configmap.get("Ventilationlabel");
            System.out.println("[ECOM-WAR] ExcelExportBookingData Ventilationlabel  --" + Ventilationlabel);
            //Header name changes 
            sheet.getRow(6).getCell(13).setCellValue(("GWF".equalsIgnoreCase((String) configmap.get(GeneralConf.CARRIER_CODE)) ? "Quantity" : "No.of Packages"));
            sheet.getRow(6).getCell(10).setCellValue(("GWF".equalsIgnoreCase((String) configmap.get(GeneralConf.CARRIER_CODE)) ? "Type" : "Package"));
            sheet.getRow(6).getCell(30).setCellValue(Ventilationlabel);

            String userFruitCust = (String) configmap.get("userFruitCust");
            String Enable_Drains_Humidity = (String) configmap.get("Enable_Drains_Humidity");
            String ENABLE_OOG_DETAILS = (String) configmap.get("ENABLE_OOG_DETAILS");
            String HIDE_MASKPORT_CONTAINER = (String) configmap.get(GeneralConf.HIDE_MASKPORT_CONTAINER);
            System.out.println("[ECOM-WAR] ExcelExportBookingData  HIDE_MASKPORT_CONTAINER--> " + HIDE_MASKPORT_CONTAINER);
            if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {
                sheet.getRow(6).getCell(31).setCellStyle(sheet.getRow(6).getCell(30).getCellStyle());
                sheet.getRow(6).getCell(32).setCellStyle(sheet.getRow(6).getCell(30).getCellStyle());
                sheet.getRow(6).getCell(33).setCellStyle(sheet.getRow(6).getCell(30).getCellStyle());
                sheet.getRow(6).getCell(34).setCellStyle(sheet.getRow(6).getCell(30).getCellStyle());
                sheet.getRow(6).getCell(31).setCellValue("Humidity(%)");
                sheet.getRow(6).getCell(32).setCellValue("Drains");
                sheet.getRow(6).getCell(33).setCellValue("Co2");
                sheet.getRow(6).getCell(34).setCellValue("O2");
            } else if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {
                sheet.getRow(6).getCell(31).setCellStyle(sheet.getRow(6).getCell(30).getCellStyle());
                sheet.getRow(6).getCell(31).setCellValue("OOG");
            }

            // CREATE STYLE
            HSSFCellStyle cellstyle = book.createCellStyle();
            HSSFFont headerEven = book.createFont();
            headerEven.setFontName("Verdana");
            headerEven.setColor(HSSFColor.BLACK.index);
            cellstyle.setFont(headerEven);
            //  System.out.println("[ECOM-WAR] ExcelExportBookingData  jsonmap::::"+jsonmap);
            //
            String select = jsonmap.get("cargocount");
            if (select != null && select.contains("[")) {
                conselect = jsonStringToArray(select);
                consize = conselect.size();
            }
            if (conselect == null || conselect.size() == 0) {
                conselect.add(select);
            }

            // Booking Amenment
            int sno = 1;
            String conrow = "";
            System.out.println("[ECOM-WAR] ExcelExportBookingData  con size::::::::" + consize);
            for (int i = 0; i < consize; i++) {

                conrow = conselect.get(i);
                eqpType = "eqpType" + conrow;
                eqpSize = "eqpSize" + conrow;
                eqpQty = "eqpQty" + conrow;
                soc = "soc" + conrow;
                hzd = "hzd" + conrow;
                commodity = "commodity" + conrow;
                packagename = "packagename" + conrow;
                hscode = "hscode" + conrow;
                packageQty = "packageQty" + conrow;
                eqpStatus = "eqpStatus" + conrow;
                tareWt = "tareWt" + conrow;
                cargoWt = "cargoWt" + conrow;
                grossWt = "grossWt" + conrow;
                volume = "volume" + conrow;
                cargoWtUnit = "cargoWtUnit" + conrow;
                containerNo = "containerNo" + conrow;
                volUnit = "volUnit" + conrow;
                commodityothers = "commodityothers" + conrow;
                marinePollut = "marinePollut" + conrow;
                unno = "unno" + conrow;
                flashPoint = "flashPoint" + conrow;
                flashUnit = "flashUnit" + conrow;
                shippingName = "shippingName" + conrow;
                dgClass = "dgClass" + conrow;
                presetTemp = "presetTemp" + conrow;
                minTemp = "minTemp" + conrow;
                maxTemp = "maxTemp" + conrow;
                ventilation = "ventilation" + conrow;
                humidity = "humidity" + conrow;
                drains = "drains" + conrow;
                co2 = "co2" + conrow;
                o2 = "o2" + conrow;
                tempUnit = "tempUnit" + conrow;
                nor = "nor" + conrow;
                owLeft = "owLeft" + conrow;
                dimUnit = "dimUnit" + conrow;
                owRight = "owRight" + conrow;
                oh = "oh" + conrow;
                olAft = "olAft" + conrow;
                olForward = "olForward" + conrow;
                hidden_oog = "hidden_oog" + conrow;
                hidden_lcl = "hidden_oog" + conrow;
                hidden_rfr = "hidden_rfr" + conrow;
                othercommodity = "othercommodity" + conrow;
                otherhscode = "otherhscode" + conrow;
                comid = "comid" + conrow;
                conid = "conid" + conrow;
                packagename = "packagename" + conrow;
                commodityname = "commodity" + conrow;
                unnos = "unno" + conrow;
                commodityothers = jsonmap.get(commodityothers);
                volUnit = jsonmap.get(volUnit);
                eqpStatus = jsonmap.get(eqpStatus);
                dimUnit = jsonmap.get(dimUnit);
                eqpType = getData(jsonmap.get(eqpType));
                hzd = jsonmap.get(hzd);
                hzd = getData("Y".equalsIgnoreCase(hzd) ? "Y" : "N");
                if ("Y".equalsIgnoreCase(commodityothers)) {
                    commodityothers = "Yes";
                } else {
                    commodityothers = "No";
                }
                if ("F".equalsIgnoreCase(volUnit)) {
                    volUnit = "Cubic Feet";
                } else {
                    volUnit = "Cubic Meters";
                }
                if ("F".equalsIgnoreCase(eqpStatus)) {
                    eqpStatus = "Laden";
                } else {
                    eqpStatus = "Empty";
                }
                if ("M".equalsIgnoreCase(dimUnit)) {
                    dimUnit = "Meters";
                } else {
                    dimUnit = "Feet";
                }

                eqpSize = (String) EQPHASHMAP.get(jsonmap.get(eqpSize));
                //       Sno	Eqp No. *	SOC *	Eqp Size 	Commodity *	Others Commodity
                //      Volume 	Volume Unit	Package *	Cargo Weight *	Weight Unit
                // Package Quantity	Hazardous	HS Code	Eqp Status	Unit	Left	Right
                //      Forward	Alt	Top	NOR	Preset Temp	Min Temp	Max Temp
                //       Temp Unit	Marine Pollutant	UN No	Flash Point	Flash Point unit
                soc = jsonmap.get(soc);
                soc = getData("Y".equalsIgnoreCase(soc) ? "Y" : "N");
                row = sheet.createRow((int) k);
                row.createCell((int) 0).setCellValue(sno);
                // Mask container number if HIDE_MASKPORT_CONTAINER is enabled
                if ("Y".equalsIgnoreCase(HIDE_MASKPORT_CONTAINER)) {
                    row.createCell((int) 1).setCellValue("Container " + (i + 1));
                } else {
                    row.createCell((int) 1).setCellValue(checkNull(jsonmap.get(containerNo)));
                }
                row.createCell((int) 2).setCellValue(soc);   // Y OR N
                row.createCell((int) 3).setCellValue(checkNull(eqpSize));
                row.createCell((int) 4).setCellValue(checkNull(hzd));  // Y OR N
                row.createCell((int) 5).setCellValue(commodityothers);
                if ("N".equalsIgnoreCase(commodityothers) || "No".equalsIgnoreCase(commodityothers)) {
                    commodityvalue = checkNull(jsonmap.get(commodityname));
                    String[] Commoditygroupcode = commodityvalue.split("~~");
                    if (Commoditygroupcode != null && Commoditygroupcode.length == 2) {
                        commodityname = Commoditygroupcode[0];
                        commodityname = commodityname.trim();
                        commoditycode = Commoditygroupcode[1];
                        commoditycode = commoditycode.trim();
                        commodityanswer = commodityname + "000" + commoditycode;
                        commodityanswer = specialcharremove(commodityanswer);
                    }
                    row.createCell((int) 6).setCellValue(commodityanswer);
                } else {
                    row.createCell((int) 6).setCellValue(checkNull(jsonmap.get(othercommodity)));
                }
                if ("N".equalsIgnoreCase(commodityothers) || "No".equalsIgnoreCase(commodityothers)) {
                    row.createCell((int) 7).setCellValue(checkNull(jsonmap.get(hscode)));
                } else {
                    row.createCell((int) 7).setCellValue(checkNull(jsonmap.get(otherhscode)));
                }
                row.createCell((int) 8).setCellValue(checkNull(jsonmap.get(volume)));
                row.createCell((int) 9).setCellValue(checkNull(volUnit));
                row.createCell((int) 10).setCellValue(checkNull(jsonmap.get(packagename)));
                row.createCell((int) 11).setCellValue(checkNull(jsonmap.get(cargoWt)));
                row.createCell((int) 12).setCellValue(checkNull(getData(jsonmap.get(cargoWtUnit))));
                row.createCell((int) 13).setCellValue(checkNull(jsonmap.get(packageQty)));

                if ("Open".equalsIgnoreCase(eqpType)) {
                    row.createCell((int) 14).setCellValue(checkNull(dimUnit));
                    row.createCell((int) 15).setCellValue(checkNull(jsonmap.get(owLeft)));
                    row.createCell((int) 16).setCellValue(checkNull(jsonmap.get(owRight)));
                    row.createCell((int) 17).setCellValue(checkNull(jsonmap.get(olForward)));
                    row.createCell((int) 18).setCellValue(checkNull(jsonmap.get(olAft)));
                    row.createCell((int) 19).setCellValue(checkNull(jsonmap.get(oh)));
                } else {
                    row.createCell((int) 14).setCellValue("");
                    row.createCell((int) 15).setCellValue("");
                    row.createCell((int) 16).setCellValue("");
                    row.createCell((int) 17).setCellValue("");
                    row.createCell((int) 18).setCellValue("");
                    row.createCell((int) 19).setCellValue("");
                }
                if ("Reefer".equalsIgnoreCase(eqpType)) {
                    row.createCell((int) 20).setCellValue(checkNull(getData(jsonmap.get(nor))));
                    row.createCell((int) 21).setCellValue(checkNull(jsonmap.get(presetTemp)));
                    row.createCell((int) 22).setCellValue(checkNull(jsonmap.get(minTemp)));
                    row.createCell((int) 23).setCellValue(checkNull(jsonmap.get(maxTemp)));
                    row.createCell((int) 24).setCellValue(checkNull(getData(jsonmap.get(tempUnit))));
                    row.createCell((int) 30).setCellValue(checkNull(jsonmap.get(ventilation)));
                    if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {
                        row.createCell((int) 31).setCellValue(checkNull(jsonmap.get(humidity)));
                        row.createCell((int) 32).setCellValue(("O".equalsIgnoreCase(checkNull(jsonmap.get(drains))) ? "Open" : ("C".equalsIgnoreCase(checkNull(jsonmap.get(drains))) ? "Closed" : "")));
                        row.createCell((int) 33).setCellValue(checkNull(jsonmap.get(co2)));
                        row.createCell((int) 34).setCellValue(checkNull(jsonmap.get(o2)));
                    }
                } else {
                    row.createCell((int) 20).setCellValue("");
                    row.createCell((int) 21).setCellValue("");
                    row.createCell((int) 22).setCellValue("");
                    row.createCell((int) 23).setCellValue("");
                    row.createCell((int) 24).setCellValue("");
                    row.createCell((int) 30).setCellValue("");
                    row.createCell((int) 31).setCellValue("");
                    row.createCell((int) 32).setCellValue("");
                    row.createCell((int) 33).setCellValue("");
                    row.createCell((int) 34).setCellValue("");
                }
                if ("Yes".equalsIgnoreCase(hzd)) {
                    row.createCell((int) 25).setCellValue(checkNull(getData(jsonmap.get(marinePollut))));  // Y OR N
                    row.createCell((int) 26).setCellValue(checkNull(jsonmap.get(unno)));
                    row.createCell((int) 27).setCellValue(checkNull(jsonmap.get(flashPoint)));
                    row.createCell((int) 28).setCellValue(getData(jsonmap.get(flashUnit)));
                } else {
                    row.createCell((int) 25).setCellValue("");  // Y OR N
                    row.createCell((int) 26).setCellValue("");
                    row.createCell((int) 27).setCellValue("");
                    row.createCell((int) 28).setCellValue("");
                }
                row.createCell((int) 29).setCellValue(checkNull(eqpStatus));
                //
                System.out.println("[ECOM-WAR] ExcelExportBookingData ENABLE_OOG_DETAILS-< " + ENABLE_OOG_DETAILS);
                if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {
                    System.out.println("[ECOM-WAR] ExcelExportBookingData hidden_oog=>" + checkNull(jsonmap.get(hidden_oog)));
                    if ("Y".equalsIgnoreCase(checkNull(jsonmap.get(hidden_oog)))) {
                        row.createCell((int) 31).setCellValue("Yes");
                    } else {
                        row.createCell((int) 31).setCellValue("No");
                    }

                }
                //
                sno++;
                k++;
            }
            //
            try {
                myFileSystem.close();
                File dirs = null;
                File Filename = null;
                String exceluploadpath = null;
                String strFileName = null;
                String filepath = null;
                exceluploadpath = excelpath;
                strFileName = exceluploadpath;
                filepath = exceluploadpath;
                if (strFileName != null && strFileName.length() > 0 && strFileName.charAt(strFileName.length() - 1) == '/') {
                    strFileName = strFileName.substring(0, strFileName.length() - 1);
                }
                strFileName = exceluploadpath.replace("/", "\\");
                //
                dirs = new File(strFileName);
                if (!dirs.exists()) {
                    dirs.mkdirs();
                }
                //  strFileName = "C:\\Ecomm\\ECOM_SI_Containerwithdetails.xls";
                filepath = exceluploadpath.replace("/", "\\");
                strFileName = filepath + "ECOM_Booking_Containerwithdetails.xls";
                Filename = new File(strFileName);
                if (!Filename.exists()) {
                    Filename.createNewFile();
                }
                File f = new File(strFileName);
                FileOutputStream fos = new FileOutputStream(f);
                book.write(fos);
                fos.flush();
                fos.close();
                file = new File(strFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {
        //    System.out.println("[ECOM-WAR] ExcelExportBookingData  output:::::" + jsonString);
        ArrayList<String> stringArray = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }
        //    System.out.println("[ECOM-WAR] ExcelExportBookingData  output:::::" + stringArray);
        return stringArray;
    }

    private String checkNull(String str) {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private String getData(String str) {
        String result = "";
        if (str != null && str.length() > 0) {
            //   System.out.println("[ECOM-WAR] ExcelExportBookingData  HM:::::::"+str);
            result = (String) hm.get(str);
        }
        if (result == null || result.length() == 0) {
            result = str;
        }
        return result;
    }

    //
    private static String specialcharremove(String inputString) {
        String result = null;
        try {
            if (inputString != null && inputString.length() > 0) {
                result = inputString.replaceAll("[^a-zA-Z0-9]", "");
            } else {
                result = "";
            }
        } catch (Exception ex) {

        }
        return result;
    }

}
