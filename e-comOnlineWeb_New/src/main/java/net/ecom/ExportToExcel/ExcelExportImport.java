/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ExportToExcel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.lookup.EJBLookUp;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.json.JSONObject;

/**
 *
 * @author vasanthakumar.r
 */
public class ExcelExportImport extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
        ServletContext contexts = getServletContext();
        String output = request.getParameter("formval");
        String strtype = "ECOM_Containerwithdetails";
        String filename = null;
        String exceluploadpath = null;
        String filepath = null;
        exceluploadpath = (String) contexts.getAttribute(GeneralConf.exceluploadpath);
        String weight_unit_kilos_only = (String) contexts.getAttribute(GeneralConf.WEIGHT_UNIT_KILOS_ONLY);
        String opencommoditystr = (String) contexts.getAttribute("OpenCommodityJson");
        HttpSession session = request.getSession();
        //  filename = exceluploadpath + strtype + ".xls";

        if ("sessionjson".equalsIgnoreCase(output)) {
            try {
                // Getting record from SI
                String jsonform = request.getParameter("formjonval");
                //    System.out.println("[ECOM-WAR] ExcelExportImport welcome  sessionsijson::::" + jsonform);
                HashMap<String, String> jsonmap = new HashMap<String, String>();
                JSONObject jObject = new JSONObject(jsonform);
                Iterator<?> keys = jObject.keys();
                //
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    String value = jObject.optString(key);
                    jsonmap.put(key, value);
                }
                session.setAttribute("jsonmap", jsonmap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if ("download".equalsIgnoreCase(output)) {
            try {
                String fileupdatepath = "";
                filename = new StringBuilder().append(exceluploadpath).append(File.separator).append(strtype + ".xls").toString();
                System.out.println("[ECOM-WAR] ExcelExportImport filename SIandBL:>>>>>>" + filename);
                //
//                File dirs = null;
//                File Filename = null;
//                dirs = new File(exceluploadpath);
//                if (!dirs.exists()) {
//                    dirs.mkdirs();
//                }
//                Filename = new File(filename);
//                if (!Filename.exists()) {
//                    Filename.createNewFile();
//                }
                //
                FileInputStream fileIn = new FileInputStream(filename);
                BufferedInputStream buffIn = new BufferedInputStream(fileIn);
                // Add logic to detect and change MIME Type
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition", "attachment; filename=\"" + strtype + ".xls" + "\"");
                //
                BufferedOutputStream buffOut = new BufferedOutputStream(response.getOutputStream());
                byte[] data = new byte[1024];
                int dataSize = 0;
                while ((dataSize = buffIn.read(data)) != -1) {
                    buffOut.write(data, 0, dataSize);
                }
                fileIn.close();
                buffIn.close();
                buffOut.flush();
                buffOut.close();
                //
                System.out.println("[ECOM-WAR] ExcelExportImport  filename:::::" + filename);
                filepath = filename.replace("/", "\\");
                fileupdatepath = new StringBuilder().append(exceluploadpath).append(File.separator).append("Accesspath").append(File.separator).append(strtype + ".xls").toString();
                File filedelete = new File(filepath);
                if (filedelete.delete()) {
                    System.out.println("[ECOM-WAR] ExcelExportImport " + filedelete.getName() + " is deleted!");
                } else {
                    System.out.println("[ECOM-WAR] ExcelExportImport Delete operation is failed.");
                }
                filedelete = new File(fileupdatepath);
                if (filedelete.delete()) {
                    System.out.println("[ECOM-WAR] ExcelExportImport " + filedelete.getName() + " is deleted!");
                } else {
                    System.out.println("[ECOM-WAR] ExcelExportImport Delete operation is failed.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("downloadexcel".equalsIgnoreCase(output)) {
            File file = null;
            try {
                String attachPath = "", accesspath = "";
                attachPath = contexts.getRealPath("/");
                accesspath = contexts.getRealPath("/");
                //  attachPath = exceluploadpath + "Template/" + strtype + ".xls";
                attachPath = new StringBuilder().append(exceluploadpath).append(File.separator).append("Template").append(File.separator).append(strtype + ".xls").toString();
                System.out.println("[ECOM-WAR] ExcelExportImport attache" + attachPath);
                //
                Map comlist = ecomCntrlObj.getCommodity();
                //System.out.println("[ECOM-WAR] ExcelExportImport >>comlist>>>>" + comlist);
                Hashtable packagemap = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
                Map EQPHASHMAP = ecomCntrlObj.getEqpments();
                Map hscodeMAP = ecomCntrlObj.getHSCode();
                Map hscodecommodity = ecomCntrlObj.getcommoditygroupcodemaps(opencommoditystr);
                //    System.out.println("[ECOM-WAR] ExcelExportImport hscodecommodity::::::::" + hscodecommodity);

                String fakCommodity = (String) contexts.getAttribute(GeneralConf.FAKCOMMODITY);
                String fakComdtyCode = "";
                String fakComdtyName = "";
                String dummycomdty = "";
                System.out.println("[ECOM-WAR] ExcelExportImport fakCommodity>>>>" + fakCommodity);
                fakCommodity = fakCommodity != null ? fakCommodity : "";
                ArrayList arrdummy = new ArrayList();
                if (fakCommodity != null && fakCommodity.length() > 0) {
                    String fakCommodityArr[] = fakCommodity.split("~~");
                    if (fakCommodityArr.length == 2) {
                        fakComdtyCode = fakCommodityArr[0].trim();
                        fakComdtyName = fakCommodityArr[1].trim();
                    }
                }
                dummycomdty = fakComdtyName + "000" + fakComdtyCode;
                //dummycomdty = specialcharremove(dummycomdty);
                //System.out.println("[ECOM-WAR] ExcelExportImport >>dummycomdty>>" + dummycomdty);
                if (hscodecommodity != null && !hscodecommodity.containsKey(dummycomdty)) {
                    hscodecommodity.put(dummycomdty, arrdummy);
                }
                System.out.println("[ECOM-WAR] ExcelExportImport >>>hscodecommodity>>>" + hscodecommodity);

                List commoditylist = new ArrayList();
                List packagelist = new ArrayList();
                List eqplist = new ArrayList();
                List hscodelist = new ArrayList();
                List wgtunitlist = new ArrayList();
                wgtunitlist.add("Kilos");
                if ("N".equalsIgnoreCase(weight_unit_kilos_only)) {
                    wgtunitlist.add("Pounds");
                }
                Iterator it = null;
                it = comlist.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    commoditylist.add(pairs.getValue().toString().trim() + " ~~ " + pairs.getKey().toString().trim());
                }
                it = packagemap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    packagelist.add(pairs.getValue().toString().trim() + " ~~ " + pairs.getKey().toString().trim());
                }
                Collections.sort(packagelist);
                it = EQPHASHMAP.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    eqplist.add(pairs.getKey());
                }
                it = hscodeMAP.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    hscodelist.add(pairs.getValue().toString().trim() + " ~~ " + pairs.getKey().toString().trim());
                }
                //
                // Fill the records contatainer,package,eqp
                HSSFWorkbook book1;
                HSSFSheet sheet1;
                HSSFRow row1;
                HSSFCell cell1;
                try {
                    //
                    accesspath = new StringBuilder().append(exceluploadpath).append(File.separator).append("Accesspath").append(File.separator).append(strtype + ".xls").toString();
                    File filedelete = new File(accesspath);
                    if (filedelete.delete()) {
                        System.out.println("[ECOM-WAR] ExcelExportImport " + filedelete.getName() + " is deleted!");
                    } else {
                        System.out.println("[ECOM-WAR] ExcelExportImport Delete operation is failed.");
                    }
                    File current = new File(attachPath);
                    File destination = new File(accesspath);
                    copyFile(current, destination);
                    //
                    FileInputStream myFileSystem1 = new FileInputStream(new File(accesspath));
                    book1 = new HSSFWorkbook(myFileSystem1);
                    sheet1 = book1.getSheetAt(1);
                    String value = "";
                    HSSFSheet sheet3 = null;
                    boolean sheetflag = false;
                    System.out.println("[ECOM-WAR] ExcelExportImport Sheets counts:" + book1.getNumberOfSheets());
                    if (book1.getNumberOfSheets() != 0) {
                        for (int i = 0; i < book1.getNumberOfSheets(); i++) {
                            if (book1.getSheetName(i).equals("sheet3")) {
                                sheet3 = book1.getSheet("sheet3");
                                sheetflag = true;
                            }
                        }
                        if (sheetflag == false) {
                            sheet3 = book1.createSheet("sheet3");
                        }
                    } else {
                        // Create new sheet to the workbook if empty
                        sheet3 = book1.createSheet("sheet3");
                    }
                    //
                    int a = 0;
                    int lastrownum = sheet1.getLastRowNum();
                    for (int r = 0; r <= lastrownum; r++) {
                        row1 = sheet1.getRow(r);
                        if (row1 != null) {
                            for (a = row1.getFirstCellNum(); a <= row1.getLastCellNum(); a++) {
                                cell1 = row1.getCell((int) a);
                                if (cell1 != null) {
                                    cell1.setCellValue("");
                                }
                            }
                        }
                    }
                    //
                    int comsize = commoditylist.size();
                    int start = 5;
                    row1 = sheet1.createRow((int) 4);
                    cell1 = row1.createCell((int) 0);
                    cell1.setCellValue("COMMODITYVALUES");
                    for (int n = 0; n < comsize; n++) {
                        if (sheet1.getRow((int) start) == null) {
                            row1 = sheet1.createRow((int) start);
                            cell1 = row1.createCell((int) 0);
                        } else {
                            row1 = sheet1.getRow((int) start);
                            cell1 = row1.createCell((int) 0);
                        }
                        value = (String) commoditylist.get(n);
                        cell1.setCellValue(value);
                        start++;
                    }
                    //
                    int packsize = packagelist.size();
                    row1 = sheet1.getRow((int) 4);
                    cell1 = row1.createCell((int) 1);
                    cell1.setCellValue("PACKAGEVALUES");
                    start = 5;
                    for (int n = 0; n < packsize; n++) {
                        if (sheet1.getRow((int) start) == null) {
                            row1 = sheet1.createRow((int) start);
                            cell1 = row1.createCell((int) 1);
                        } else {
                            row1 = sheet1.getRow((int) start);
                            cell1 = row1.createCell((int) 1);
                        }
                        value = (String) packagelist.get(n);
                        cell1.setCellValue(value);
                        start++;
                    }

                    int eqpsize = eqplist.size();
                    row1 = sheet1.getRow((int) 4);
                    cell1 = row1.createCell((int) 2);
                    cell1.setCellValue("Eqp Size");
                    start = 5;
                    for (int n = 0; n < eqpsize; n++) {
                        if (sheet1.getRow((int) start) == null) {
                            row1 = sheet1.createRow((int) start);
                            cell1 = row1.createCell((int) 2);
                        } else {
                            row1 = sheet1.getRow((int) start);
                            cell1 = row1.createCell((int) 2);
                        }
                        value = (String) eqplist.get(n);
                        cell1.setCellValue(value);
                        start++;
                    }

                    int hscodesize = hscodelist.size();
                    row1 = sheet1.getRow((int) 4);
                    cell1 = row1.createCell((int) 3);
                    cell1.setCellValue("HS Code");
                    start = 5;
                    for (int n = 0; n < hscodesize; n++) {
                        if (sheet1.getRow((int) start) == null) {
                            row1 = sheet1.createRow((int) start);
                            cell1 = row1.createCell((int) 3);
                        } else {
                            row1 = sheet1.getRow((int) start);
                            cell1 = row1.createCell((int) 3);
                        }
                        value = (String) hscodelist.get(n);
                        cell1.setCellValue(value);
                        start++;
                    }
                    //Cargo weight unit 

                    int wgtunitsize = wgtunitlist.size();
                    row1 = sheet1.getRow((int) 4);
                    cell1 = row1.createCell((int) 4);
                    cell1.setCellValue("Weight Unit");
                    start = 5;
                    for (int n = 0; n < wgtunitsize; n++) {
                        if (sheet1.getRow((int) start) == null) {
                            row1 = sheet1.createRow((int) start);
                            cell1 = row1.createCell((int) 4);
                        } else {
                            row1 = sheet1.getRow((int) start);
                            cell1 = row1.createCell((int) 4);
                        }
                        value = (String) wgtunitlist.get(n);
                        cell1.setCellValue(value);
                        start++;
                    }

                    // commodity Hscode dropdown
                    Sheet sheet = book1.getSheet("ECOM_Containerwithdetails"); // Sheet sheet1 
                    //
                    //
                    int v = 0;
                    int lastrownumsheet3 = sheet3.getLastRowNum();
                    for (int r = 0; r <= lastrownumsheet3; r++) {
                        row1 = sheet3.getRow(r);
                        if (row1 != null) {
                            for (v = row1.getFirstCellNum(); v <= row1.getLastCellNum(); v++) {
                                cell1 = row1.getCell((int) v);
                                if (cell1 != null) {
                                    cell1.setCellValue("");
                                }
                            }
                        }
                    }
                    //
                    ExcelExportImport.buildDataSheet(sheet3, hscodecommodity, eqpsize, packsize, dummycomdty, wgtunitsize); // Sheet sheet3 
                    //
                    int firstrow = 5;
                    int lastrow = 1000;
                    int firstcolumn = 7;
                    int lastcolumn = 7;
                    //
                    CellRangeAddressList addressList = new CellRangeAddressList(firstrow, lastrow, firstcolumn, lastcolumn);
                    DataValidationHelper dvHelper = sheet.getDataValidationHelper();
                    DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("COMMODITYCODE");
                    DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
                    //    validation.createErrorBox("Ecommerce", "Cannot change the Commodity value");
                    //    validation.setSuppressDropDownArrow(true);
                    //  validation.setShowErrorBox(false);
                    //   validation.setShowPromptBox(false);
                    sheet.addValidationData(validation);
                    //
                    //Eqp size
                    CellRangeAddressList addressList1 = new CellRangeAddressList(firstrow, lastrow, 3, 3);
                    DataValidationHelper dvHelper1 = sheet.getDataValidationHelper();
                    DataValidationConstraint dvConstraint1 = dvHelper1.createFormulaListConstraint("eqpsizelist");
                    DataValidation validation1 = dvHelper1.createValidation(dvConstraint1, addressList1);
                    sheet.addValidationData(validation1);
                    //
                    // package
                    CellRangeAddressList addressList2 = new CellRangeAddressList(firstrow, lastrow, 9, 9);
                    DataValidationHelper dvHelper2 = sheet.getDataValidationHelper();
                    DataValidationConstraint dvConstraint2 = dvHelper2.createFormulaListConstraint("packagelist");
                    DataValidation validation2 = dvHelper2.createValidation(dvConstraint2, addressList2);
                    sheet.addValidationData(validation2);
                    //
                    // Cargo weight Unit
                    CellRangeAddressList addressList3 = new CellRangeAddressList(firstrow, lastrow, 13, 13);
                    DataValidationHelper dvHelper3 = sheet.getDataValidationHelper();
                    DataValidationConstraint dvConstraint3 = dvHelper3.createFormulaListConstraint("Cargoweightunit");
                    DataValidation validation3 = dvHelper3.createValidation(dvConstraint3, addressList3);
                    sheet.addValidationData(validation3);
                    //
                    int j = 6;
                    for (int i = 5; i <= lastrow; i++) {
                        addressList = new CellRangeAddressList(i, i, 8, 8);
                        dvConstraint = dvHelper.createFormulaListConstraint("INDIRECT(UPPER($H$" + j + "))");
                        validation = dvHelper.createValidation(dvConstraint, addressList);
                        validation.setShowErrorBox(false);
                        validation.setShowPromptBox(false);
                        sheet.addValidationData(validation);
                        j++;
                        // System.out.println("[ECOM-WAR] ExcelExportImport  i::::" + i + "::::::" + j);
                    }
                    //
                    book1.setSheetHidden(2, true);
                    System.out.println("[ECOM-WAR] ExcelExportImport attachpath::::::::" + accesspath);
                    File f = new File(accesspath);
                    FileOutputStream fos = new FileOutputStream(f);
                    book1.write(fos);
                    fos.flush();
                    fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //
                ExcelExportImportData exr = new ExcelExportImportData();
                // Getting record from SI
//                String jsonform = request.getParameter("formval");
//                //  System.out.println("values::::" + jsonform);
//                HashMap<String, String> jsonmap = new HashMap<String, String>();
//                JSONObject jObject = new JSONObject(jsonform);
//                Iterator<?> keys = jObject.keys();
//                //
//                while (keys.hasNext()) {
//                    String key = (String) keys.next();
//                    String value = jObject.optString(key);
//                    jsonmap.put(key, value);
//                }
                HashMap<String, String> jsonmap = (HashMap<String, String>) session.getAttribute("jsonmap");
                HashMap<String, String> configmap = new HashMap<String, String>();
                configmap.put(GeneralConf.CARRIER_CODE, (String) request.getServletContext().getAttribute(GeneralConf.CARRIER_CODE));
                configmap.put(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL, (String) request.getServletContext().getAttribute(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL));
                configmap.put("SHIPPERSEALCONFIG", (String) request.getSession().getAttribute("SHIPPERSEALCONFIG"));
                configmap.put("ENABLE_OOG_DETAILS", (String) request.getServletContext().getAttribute("ENABLE_OOG_DETAILS"));
                configmap.put(GeneralConf.HIDE_MASKPORT_CONTAINER, (String) request.getServletContext().getAttribute(GeneralConf.HIDE_MASKPORT_CONTAINER));

//    System.out.println("[ECOM-WAR] ExcelExportImport Jsonvalue::::" + jsonmap);
                if (jsonmap != null) {
                    file = (File) exr.ExcelExport(jsonmap, accesspath, exceluploadpath, configmap);
                    //
                    response.setContentType("application/vnd.ms-excel;Content-Transfer-Encoding:binary");
                    response.setHeader("Content-Disposition", "attachment; filename=" + strtype + ".xls");
                    FileInputStream fis = new FileInputStream(file);
                    int i;
                    while ((i = fis.read()) != -1) {
                        response.getOutputStream().write(i);
                    }
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    fis.close();
                }
                session.setAttribute("jsonmap", null);
                //
                String fileupdatepath = "";
                filename = new StringBuilder().append(exceluploadpath).append(File.separator).append(strtype + ".xls").toString();
                filepath = filename.replace("/", "\\");
                fileupdatepath = new StringBuilder().append(exceluploadpath).append(File.separator).append("Accesspath").append(File.separator).append(strtype + ".xls").toString();
                File filedelete = new File(filepath);
                if (filedelete.delete()) {
                    System.out.println("[ECOM-WAR] ExcelExportImport "+filedelete.getName() + " is deleted!");
                } else {
                    System.out.println("[ECOM-WAR] ExcelExportImport Delete operation is failed.");
                }
                filedelete = new File(fileupdatepath);
                if (filedelete.delete()) {
                    System.out.println("[ECOM-WAR] ExcelExportImport " +filedelete.getName() + " is deleted!");
                } else {
                    System.out.println("[ECOM-WAR] ExcelExportImport Delete operation is failed.");
                }
                //
            } catch (Exception e) {
                System.out.println("[ECOM-WAR] ExcelExportImport ERROR::::::" + e.getMessage());
                e.printStackTrace();

            }
        }
    }

    private static final void buildDataSheet(Sheet dataSheet, Map hscodecommodity, int eqpsize, int packsize, String dummycmdty, int wgtunitsize) {
        try {
            Row row = null;
            Cell cell = null;
            Name name = null;
//            ArrayList sheetnamelist = new ArrayList();
            //
            String key = "", answer = "";
            String keyremovespecialchar = "";
            ArrayList arrayvalue = null;
            Iterator it = null;
            int start = 5;
            int begin = 6;
            int end = 0;
            int inc = 0;
            String formula = "";
            String Columnlabel = "";

            //
            TreeMap<String, Integer> sortedHscode = new TreeMap<>(hscodecommodity);
            it = sortedHscode.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                arrayvalue = new ArrayList();
                key = pairs.getKey().toString().trim();
                keyremovespecialchar = specialcharremove(key);
                arrayvalue = (ArrayList) pairs.getValue();
                Collections.sort(arrayvalue);
                //   System.out.println("[ECOM-WAR] ExcelExportImport yes:::::" + keyremovespecialchar);
                //
                if (dataSheet.getRow((int) 4) == null) {
                    row = dataSheet.createRow((int) 4);
                    cell = row.createCell((int) inc);
                } else {
                    row = dataSheet.getRow((int) 4);
                    cell = row.createCell((int) inc);
                }
                //   System.out.println("[ECOM-WAR] ExcelExportImport keyremovespecialchar::::::::::::::"+keyremovespecialchar);
                cell.setCellValue(keyremovespecialchar);
                int arraysize = arrayvalue.size();
                //
                for (int n = 0; n < arraysize; n++) {
                    if (dataSheet.getRow((int) start) == null) {
                        row = dataSheet.createRow((int) start);
                        cell = row.createCell((int) inc);
                    } else {
                        row = dataSheet.getRow((int) start);
                        cell = row.createCell((int) inc);
                    }

                    answer = (String) arrayvalue.get(n);
                    cell.setCellValue(answer);
                    start++;
                    begin++;
                }
                end = start;
                start = 5;
                begin = 6;
                Columnlabel = CellReference.convertNumToColString(inc);
                inc++;
                // Name funtion
                System.out.println(keyremovespecialchar + "::[ECOM-WAR] ExcelExportImport ::" + dummycmdty);
                //    if(!keyremovespecialchar.equalsIgnoreCase(dummycmdty)){
                name = dataSheet.getWorkbook().createName();
                formula = "'sheet3'!$" + Columnlabel + "$" + begin + ":$" + Columnlabel + "$" + end;
                //   System.out.println("[ECOM-WAR] ExcelExportImport columnlabel:::::" + Columnlabel + ":::Formula:::::" + formula + "::::" + keyremovespecialchar);
                name.setRefersToFormula(formula);
                keyremovespecialchar = (keyremovespecialchar != null && keyremovespecialchar.length() > 255) ? keyremovespecialchar.substring(0, 255) : keyremovespecialchar;
                name.setNameName(keyremovespecialchar);
                //  }
            }
            //   COMMODITYCODE
            name = dataSheet.getWorkbook().createName();
            formula = "'sheet3'!$A$5:$" + Columnlabel + "$5";
            //  System.out.println("[ECOM-WAR] ExcelExportImport columnlabel for commmoitycode:::::" + Columnlabel + ":::Formula:::::" + formula);
            name.setRefersToFormula(formula);

            if (name.getSheetName() != null && "COMMODITYCODE".equalsIgnoreCase(name.getSheetName())) {
                dataSheet.getWorkbook().removeName("COMMODITYCODE");
            }
            if (name.getSheetName() != null && !"COMMODITYCODE".equalsIgnoreCase(name.getSheetName())) {
                name.setNameName("COMMODITYCODE");
            }

            name = dataSheet.getWorkbook().createName();
            formula = "'sheet2'!$C$6:$C$" + (5 + eqpsize);
            name.setRefersToFormula(formula);
            name.setNameName("eqpsizelist");
            // 
            name = dataSheet.getWorkbook().createName();
            formula = "'sheet2'!$B$6:$B$" + (5 + packsize);
            name.setRefersToFormula(formula);
            name.setNameName("packagelist");
            //
            name = dataSheet.getWorkbook().createName();
            formula = "'sheet2'!$E$6:$E$" + (5 + wgtunitsize);
            name.setRefersToFormula(formula);
            name.setNameName("Cargoweightunit");
            //
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public static void copyFile(File from, File to) throws IOException {
        Files.copy(from.toPath(), to.toPath());
    }
}
