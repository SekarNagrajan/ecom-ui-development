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
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author vasanthakumar.r
 */
public class ExcelExportImportData {

    Map hm = null, vol_hm = null;

    public File ExcelExport(HashMap<String, String> jsonmap, String attachPath, String excelpath, HashMap configmap) {
        File file = null;
        try {
            //
            String containerNo = "", carrierSeal = "", shipperSeal = "", strEqptype = "";
            String cmdName = "", qtyName = "", unitName = "", hscodename = "";
            String pkgName = "", volName = "", vutName = "", mrkName = "", desName = "", oog = "", unit = "", left = "", right = "", alt = "", forward = "", top = "";
            String contno = "", soc = "", cargoweight = "", selectbox = "";
            String cellanswer = "";
            ArrayList<String> conselect = new ArrayList<String>();
            ArrayList<String> totalsub = null;
            List totalcolumn = null;
            List stringarraylist = null;
            String commoditycode = "", commodityname = "";
            String ENABLE_OOG_DETAILS = (String) configmap.get("ENABLE_OOG_DETAILS");
            String HIDE_MASKPORT_CONTAINER = (String) configmap.get(GeneralConf.HIDE_MASKPORT_CONTAINER);
            System.out.println("[ECOM-WAR] ExcelExportImportData  ENABLE_OOG_DETAILS--> " + ENABLE_OOG_DETAILS);
            System.out.println("[ECOM-WAR] ExcelExportImportData  HIDE_MASKPORT_CONTAINER--> " + HIDE_MASKPORT_CONTAINER);
            hm = new HashMap();
            hm.put("M", "Cubic Meter");
            hm.put("F", "Cubic Feet");
            hm.put("K", "Kilos");
            hm.put("P", "Pounds");
            hm.put("T", "Metric Tons");
            hm.put("N", "No");
            hm.put("n", "No");
            hm.put("y", "Yes");
            hm.put("Y", "Yes");
            hm.put("M", "Meters");
            hm.put("F", "Feet");
            hm.put("C", "Centimeters");
            //
            vol_hm = new HashMap();
            vol_hm.put("M", "Cubic Meters");
            vol_hm.put("F", "Cubic Feet");
            //
            HSSFWorkbook book = new HSSFWorkbook();
            HSSFRow row;
            HSSFCell cell;
            HSSFSheet sheet;
            int consize = 1;
            int k = 5;
            int subcount = 1;
            String vuint = null;
            Map commonlist = null;
            String string = "", Stringval = "";
            ArrayList<String> stringarrayvalue = null;
            String keycolumn = "";
            ArrayList arrkeycomp = null;
            int c = 1;

            FileInputStream myFileSystem = new FileInputStream(new File(attachPath));
            book = new HSSFWorkbook(myFileSystem);
            sheet = book.getSheetAt(0);
            //Header name changes 
            sheet.getRow(4).getCell(6).setCellValue(("GWF".equalsIgnoreCase((String) configmap.get(GeneralConf.CARRIER_CODE)) ? "Quantity *" : "No.of Packages *"));
            sheet.getRow(4).getCell(9).setCellValue(("GWF".equalsIgnoreCase((String) configmap.get(GeneralConf.CARRIER_CODE)) ? "Type *" : "Package *"));
            sheet.getRow(4).getCell(4).setCellValue(("Y".equalsIgnoreCase((String) configmap.get(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL)) ? "Carrier Seal" : "Carrier Seal *"));
            sheet.getRow(4).getCell(5).setCellValue(("YES".equalsIgnoreCase((String) configmap.get("SHIPPERSEALCONFIG")) ? "Shipper Seal *" : "Shipper Seal"));
            //
            if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {
                System.out.println("[ECOM-WAR] ExcelExportImportData  sheet.getRow(4).getCell(16)=> " + sheet.getRow(4).getCell(16));
                System.out.println("[ECOM-WAR] ExcelExportImportData  sheet.getRow(4).getCell(15)=> " + sheet.getRow(4).getCell(15));
                sheet.getRow(4).createCell(16).setCellStyle(sheet.getRow(4).getCell(15).getCellStyle());
                sheet.getRow(4).createCell(17).setCellStyle(sheet.getRow(4).getCell(15).getCellStyle());
                sheet.getRow(4).createCell(18).setCellStyle(sheet.getRow(4).getCell(15).getCellStyle());
                sheet.getRow(4).createCell(19).setCellStyle(sheet.getRow(4).getCell(15).getCellStyle());
                sheet.getRow(4).createCell(20).setCellStyle(sheet.getRow(4).getCell(15).getCellStyle());
                sheet.getRow(4).createCell(21).setCellStyle(sheet.getRow(4).getCell(15).getCellStyle());
                sheet.getRow(4).createCell(22).setCellStyle(sheet.getRow(4).getCell(15).getCellStyle());
                sheet.getRow(4).getCell(16).setCellValue("OOG");
                sheet.getRow(4).getCell(17).setCellValue("Unit");
                sheet.getRow(4).getCell(18).setCellValue("Left");
                sheet.getRow(4).getCell(19).setCellValue("Right");
                sheet.getRow(4).getCell(20).setCellValue("Forward");
                sheet.getRow(4).getCell(21).setCellValue("Aft");
                sheet.getRow(4).getCell(22).setCellValue("Top");
            }
            // CREATE STYLE
            HSSFCellStyle cellstyle = book.createCellStyle();
            HSSFFont headerEven = book.createFont();
            headerEven.setFontName("Verdana");
            headerEven.setColor(HSSFColor.BLACK.index);
            cellstyle.setFont(headerEven);
            //   System.out.println("[ECOM-WAR] ExcelExportImportData  jsonmap::::"+jsonmap);
            //
            String selectbox1 = jsonmap.get("select");
            if (selectbox1 != null && selectbox1.contains("[")) {
                conselect = jsonStringToArray(selectbox1);
                consize = conselect.size();
            }
            if (conselect == null || conselect.size() == 0) {
                conselect.add(selectbox1);
            }
//
            System.out.println("[ECOM-WAR] ExcelExportImportData  consize::::::" + consize + ":::::" + conselect);

//
            for (int v = 0; v < consize; v++) {
                totalcolumn = new ArrayList();
                stringarraylist = new ArrayList();
                selectbox = conselect.get(v);
                vuint = jsonmap.get(selectbox + "#vunit#");
                containerNo = selectbox;
                contno = containerNo + "#containerID#";   // contno
                hscodename = containerNo + "#hscode#";
                soc = containerNo + "#socID#";            // soc
                strEqptype = containerNo + "#eqpType#";   // eqpsize
                carrierSeal = containerNo + "#carrier#";  // carrierseal
                shipperSeal = containerNo + "#shipper#";   // shipperseal
                qtyName = containerNo + "#newqty#";       // Quantity
                cmdName = containerNo + "#newcmdty#";      // commoitysize
                pkgName = containerNo + "#newpack#";     // PackageType
                volName = containerNo + "#newvol#";     // volume
                unitName = containerNo + "#vunit#";     // volume unitname
                cargoweight = containerNo + "#newwt#";   // cargoweight
                vutName = containerNo + "#unit#";        // Weight Unit
                mrkName = containerNo + "#mark#";
                desName = containerNo + "#descp#";
                oog = containerNo + "#oog#";
                unit = containerNo + "#itemlengthUnit#";
                left = containerNo + "#itemleft#";
                right = containerNo + "#itemright#";
                forward = containerNo + "#itemforward#";
                alt = containerNo + "#itemaft#";
                top = containerNo + "#itemtop#";

                totalcolumn.add("sno");
                totalcolumn.add(contno);
                totalcolumn.add(soc);
                totalcolumn.add(strEqptype);
                totalcolumn.add(carrierSeal);
                totalcolumn.add(shipperSeal);

                totalcolumn.add(qtyName);
                totalcolumn.add(cmdName);
                totalcolumn.add(hscodename);
                totalcolumn.add(pkgName);
                totalcolumn.add(volName);
                totalcolumn.add(unitName);
                totalcolumn.add(cargoweight);
                totalcolumn.add(vutName);
                totalcolumn.add(mrkName);
                totalcolumn.add(desName);
                if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {
                    totalcolumn.add(oog);
                    totalcolumn.add(unit);
                    totalcolumn.add(left);
                    totalcolumn.add(right);
                    totalcolumn.add(forward);
                    totalcolumn.add(alt);
                    totalcolumn.add(top);
                }

                stringarraylist.add(qtyName);
                stringarraylist.add(cmdName);
                stringarraylist.add(hscodename);
                stringarraylist.add(pkgName);
                stringarraylist.add(volName);
                stringarraylist.add(unitName);
                stringarraylist.add(cargoweight);
                stringarraylist.add(vutName);
                stringarraylist.add(mrkName);
                stringarraylist.add(desName);
                if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {
                    stringarraylist.add(oog);
                    stringarraylist.add(unit);
                    stringarraylist.add(left);
                    stringarraylist.add(right);
                    stringarraylist.add(forward);
                    stringarraylist.add(alt);
                    stringarraylist.add(top);
                }

                commonlist = new HashMap<String, String>();
                stringarrayvalue = new ArrayList<String>();
                totalsub = new ArrayList<String>();
                if (vuint != null && vuint.contains("[")) {
                    totalsub = jsonStringToArray(vuint);
                    //     System.out.println("[ECOM-WAR] ExcelExportImportData  vnuint::::" + vuint);
                    for (int i = 0; i < stringarraylist.size(); i++) {
                        string = "";
                        Stringval = "";
                        Stringval = (String) stringarraylist.get(i);
                        //      System.out.println("[ECOM-WAR] ExcelExportImportData  ::::::::::::" + Stringval);
                        string = jsonmap.get(Stringval);
                        System.out.println("[ECOM-WAR] ExcelExportImportData  string::::" + string);
                        if (string != null && string.contains("[")) {
                            stringarrayvalue = jsonStringToArray(string);
                            commonlist.put(Stringval, stringarrayvalue);
                        }
                    }
                }
                //    System.out.println("[ECOM-WAR] ExcelExportImportData  commonlist::::::::::::::::" + commonlist);
                int commcolumn = totalcolumn.size();
                // values insert
                subcount = 1;
                if (totalsub != null && totalsub.size() > 0) {
                    subcount = totalsub.size();
                }
//
                for (int n = 0; n < subcount; n++) {
                    row = sheet.createRow((int) k);
                    arrkeycomp = new ArrayList();
                    for (int i = 0; i < commcolumn; i++) {
                        cellanswer = "";
                        keycolumn = (String) totalcolumn.get(i);
                        //   System.out.println("[ECOM-WAR] ExcelExportImportData  keycolumn:::::" + keycolumn + ":::::::" + i);
                        cell = row.createCell((int) i);
                        if ("sno".equalsIgnoreCase(keycolumn)) {
                            cell.setCellValue(c);
                        } else {
                            arrkeycomp = (ArrayList) commonlist.get(keycolumn);
                            //    System.out.println("[ECOM-WAR] ExcelExportImportData  yesss::::::" + arrkeycomp);
                            if (arrkeycomp != null) {
                                cellanswer = (String) arrkeycomp.get(n);
                                if (keycolumn.contains("#socID#") || keycolumn.contains("#unit#") || keycolumn.contains("#oog#") || keycolumn.contains("#itemlengthUnit#")) {
                                    cellanswer = getData(cellanswer);
                                }
                                if (keycolumn.contains("#vunit#")) {
                                    cellanswer = getDataVolumeUnit(cellanswer);
                                }
                                if (keycolumn.contains("#newcmdty#")) {
                                    String[] Commoditygroupcode = cellanswer.split("~~");
                                    if (Commoditygroupcode != null && Commoditygroupcode.length == 2) {
                                        commodityname = Commoditygroupcode[0];
                                        commodityname = commodityname.trim();
                                        commoditycode = Commoditygroupcode[1];
                                        commoditycode = commoditycode.trim();
                                        commodityname = Validation.checkNull(commodityname).replace("(" + commoditycode + ")", "").trim();
                                        cellanswer = commodityname + "000" + commoditycode;
                                    }
                                    cellanswer = specialcharremove(cellanswer);
                                }
                            } else {
                                cellanswer = jsonmap.get(keycolumn);
                                // Mask container number if HIDE_MASKPORT_CONTAINER is enabled
                                if (keycolumn.contains("#containerID#") && "Y".equalsIgnoreCase(HIDE_MASKPORT_CONTAINER)) {
                                    cellanswer = "Container " + (v + 1);
                                }
                                if (keycolumn.contains("#socID#") || keycolumn.contains("#unit#") || keycolumn.contains("#oog#") || keycolumn.contains("#itemlengthUnit#")) {
                                    cellanswer = getData(cellanswer);
                                }
                                if (keycolumn.contains("#vunit#")) {
                                    cellanswer = getDataVolumeUnit(cellanswer);
                                }
                                if (keycolumn.contains("#newcmdty#")) {
                                    String[] Commoditygroupcode = cellanswer.split("~~");
                                    if (Commoditygroupcode != null && Commoditygroupcode.length == 2) {
                                        commodityname = Commoditygroupcode[0];
                                        commodityname = commodityname.trim();
                                        commoditycode = Commoditygroupcode[1];
                                        commoditycode = commoditycode.trim();
                                        commodityname = Validation.checkNull(commodityname).replace("(" + commoditycode + ")", "").trim();
                                        cellanswer = commodityname + "000" + commoditycode;
                                    }
                                    cellanswer = specialcharremove(cellanswer);
                                }
                            }
                            cell.setCellValue(cellanswer);
                        }
                        cell.setCellStyle(cellstyle);
                    }
                    k++;
                    c++;
                }

            }

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
                //  String strFileName = "C:\\Ecomm";
                dirs = new File(strFileName);
                if (!dirs.exists()) {
                    dirs.mkdirs();
                }

                //  strFileName = "C:\\Ecomm\\ECOM_SI_Containerwithdetails.xls";
                filepath = exceluploadpath.replace("/", "\\");
                strFileName = filepath + "ECOM_Containerwithdetails.xls";
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
        //    System.out.println("[ECOM-WAR] ExcelExportImportData  output:::::" + jsonString);
        ArrayList<String> stringArray = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }
        //    System.out.println("[ECOM-WAR] ExcelExportImportData  output:::::" + stringArray);
        return stringArray;
    }

    private String getData(String str) {
        String result = "";
        if (str != null && str.length() > 0) {
            //   System.out.println("[ECOM-WAR] ExcelExportImportData  HM:::::::"+str);
            result = (String) hm.get(str);
        }
        if (result == null || result.length() == 0) {
            result = str;
        }
        return result;
    }

    private String getDataVolumeUnit(String str) {
        String result = "";
        if (str != null && str.length() > 0) {
            //   System.out.println("[ECOM-WAR] ExcelExportImportData  HM:::::::"+str);
            result = (String) vol_hm.get(str);
        }
        if (result == null || result.length() == 0) {
            result = str;
        }
        return result;
    }

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
