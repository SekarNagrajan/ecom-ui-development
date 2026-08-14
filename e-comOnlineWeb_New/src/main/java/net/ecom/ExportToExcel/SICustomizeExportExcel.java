/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ExportToExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import static net.ecom.ExportToExcel.ExcelExportImport.copyFile;
import net.ecom.common.GeneralConf;
import net.ecom.lookup.EJBLookUp;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "SICustomizeExportExcel", urlPatterns = {"/SICustomizeExportExcel"})
public class SICustomizeExportExcel extends HttpServlet {

    Map hm = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletContext contexts = getServletContext();
        String output = request.getParameter("formval");
        String strtype = "ECOM_SI_Containerwithdetails";
        String filename = null;
        String exceluploadpath = null;
        String filepath = null;
        exceluploadpath = (String) contexts.getAttribute(GeneralConf.exceluploadpath);
        HttpSession session = request.getSession();
        if ("sessionjson".equalsIgnoreCase(output)) {
            try {
                // Getting record from SI
                String jsonform = request.getParameter("formjonval");
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
        } else if ("downloadexcel".equalsIgnoreCase(output)) {
            File file = null;
            try {
                String attachPath = "", accesspath = "";
                attachPath = contexts.getRealPath("/");
                accesspath = contexts.getRealPath("/");
                //  attachPath = exceluploadpath + "Template/" + strtype + ".xls";
                attachPath = new StringBuilder().append(exceluploadpath).append(File.separator).append("Template").append(File.separator).append(strtype + ".xls").toString();
                System.out.println("[ECOM-WAR] SICustomizeExportExcel  attache" + attachPath);
                //
                Hashtable packagemap = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
                List packagelist = new ArrayList();
                Iterator it = null;
                it = packagemap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    packagelist.add(pairs.getValue().toString().trim() + " ~~ " + pairs.getKey().toString().trim());
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
                        System.out.println("[ECOM-WAR] SICustomizeExportExcel  " +filedelete.getName() + " is deleted!");
                    } else {
                        System.out.println("[ECOM-WAR] SICustomizeExportExcel  Delete operation is failed.");
                    }
                    File current = new File(attachPath);
                    File destination = new File(accesspath);
                    copyFile(current, destination);
                    //
                    FileInputStream myFileSystem1 = new FileInputStream(new File(accesspath));
                    book1 = new HSSFWorkbook(myFileSystem1);
                    sheet1 = book1.getSheetAt(2);
                    String value = "";
                    HSSFSheet sheet3 = null;
                    boolean sheetflag = false;
                    System.out.println("[ECOM-WAR] SICustomizeExportExcel  Sheets counts::::::" + book1.getNumberOfSheets());
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
                    int start = 5;

                    int packsize = packagelist.size();
                    Collections.sort(packagelist);
                    row1 = sheet1.createRow((int) 4);
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

                    // commodity Hscode dropdown
                    Sheet sheet = book1.getSheet("ECOM_SI_Containerwithdetails"); // Sheet sheet1 
                    System.out.println("[ECOM-WAR] SICustomizeExportExcel  sheetsheetsheetsheet=> "+sheet);
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
                    buildDataSheet(sheet3, packsize); // Sheet sheet3 
                    //
                    int firstrow = 5;
                    int lastrow = 1000;
                    int firstcolumn = 7;
                    int lastcolumn = 7;
                    //

                    // package
                    CellRangeAddressList addressList2 = new CellRangeAddressList(firstrow, lastrow, 6, 6);
                    DataValidationHelper dvHelper2 = sheet.getDataValidationHelper();
                    DataValidationConstraint dvConstraint2 = dvHelper2.createFormulaListConstraint("packagelist");
                    DataValidation validation2 = dvHelper2.createValidation(dvConstraint2, addressList2);
                    sheet.addValidationData(validation2);
                    //

                    //
                    book1.setSheetHidden(2, true);
                    System.out.println("[ECOM-WAR] SICustomizeExportExcel  attachpath::::::::" + accesspath);
                    File f = new File(accesspath);
                    FileOutputStream fos = new FileOutputStream(f);
                    book1.write(fos);
                    fos.flush();
                    fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // Getting record from SI
                HashMap<String, String> jsonmap = (HashMap<String, String>) session.getAttribute("jsonmap");
                HashMap<String, String> configmap = new HashMap<String, String>();
                configmap.put(GeneralConf.CARRIER_CODE, (String) request.getServletContext().getAttribute(GeneralConf.CARRIER_CODE));
                configmap.put(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL, (String) request.getServletContext().getAttribute(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL));
                configmap.put("SHIPPERSEALCONFIG", (String) request.getSession().getAttribute("SHIPPERSEALCONFIG"));
                configmap.put(GeneralConf.HIDE_MASKPORT_CONTAINER, (String) request.getServletContext().getAttribute(GeneralConf.HIDE_MASKPORT_CONTAINER));
                //    System.out.println("[ECOM-WAR] SICustomizeExportExcel  Jsonvalue:::::::" + jsonmap);
                if (jsonmap != null) {
                    file = (File) ExcelExport(jsonmap, accesspath, exceluploadpath, configmap);
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
                    System.out.println("[ECOM-WAR] SICustomizeExportExcel  " +filedelete.getName() + " is deleted!");
                } else {
                    System.out.println("[ECOM-WAR] SICustomizeExportExcel  Delete operation is failed.");
                }
                filedelete = new File(fileupdatepath);
                if (filedelete.delete()) {
                    System.out.println("[ECOM-WAR] SICustomizeExportExcel "+filedelete.getName() + " is deleted!");
                } else {
                    System.out.println("[ECOM-WAR] SICustomizeExportExcel  Delete operation is failed.");
                }
                //
            } catch (Exception e) {
                System.out.println("[ECOM-WAR] SICustomizeExportExcel  ERROR::::::" + e.getMessage());
                e.printStackTrace();

            }
        }
    }

    private static final void buildDataSheet(Sheet dataSheet, int packsize) {
        try {
            Name name = null;
            String formula = "";
            //
            name = dataSheet.getWorkbook().createName();
            formula = "'sheet2'!$B$6:$B$" + (5 + packsize);
            name.setRefersToFormula(formula);
            name.setNameName("packagelist");
            //
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {
        //    System.out.println("[ECOM-WAR] SICustomizeExportExcel  output:::::" + jsonString);
        ArrayList<String> stringArray = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }
        //    System.out.println("[ECOM-WAR] SICustomizeExportExcel  output:::::" + stringArray);
        return stringArray;
    }

    private String getData(String str) {
        String result = "";
        if (str != null && str.length() > 0) {
            //   System.out.println("[ECOM-WAR] SICustomizeExportExcel  HM:::::::"+str);
            result = (String) hm.get(str);
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

    public static void copyFile(File from, File to) throws IOException {
        Files.copy(from.toPath(), to.toPath());
    }

    public File ExcelExport(HashMap<String, String> jsonmap, String attachPath, String excelpath, HashMap configmap) {
        File file = null;
        try {
            //
            String containerNo = "", carrierSeal = "", shipperSeal = "", strEqptype = "";
            String cmdName = "", qtyName = "", unitName = "", hscodename = "";
            String pkgName = "", volName = "", vutName = "", mrkName = "", desName = "";
            String contno = "", soc = "", cargoweight = "", selectbox = "";
            String cellanswer = "";
            ArrayList<String> conselect = new ArrayList<String>();
            ArrayList<String> totalsub = null;
            List totalcolumn = null;
            List stringarraylist = null;
            String commoditycode = "", commodityname = "";
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
            String HIDE_MASKPORT_CONTAINER = (String) configmap.get(GeneralConf.HIDE_MASKPORT_CONTAINER);
            System.out.println("[ECOM-WAR] SICustomizeExportExcel  HIDE_MASKPORT_CONTAINER--> " + HIDE_MASKPORT_CONTAINER);
            //
            HSSFWorkbook book = new HSSFWorkbook();
            HSSFRow row;
            HSSFCell cell;
            HSSFSheet sheet;
            int consize = 1;
            int k = 5;
            int subcount = 1;
            String vuint = null,qtyvalue=null;
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
            //sheet.getRow(4).getCell(5).setCellValue(("GWF".equalsIgnoreCase((String) configmap.get(GeneralConf.CARRIER_CODE)) ? "Quantity *" : "No.of Packages *"));
            //sheet.getRow(4).getCell(7).setCellValue(("GWF".equalsIgnoreCase((String) configmap.get(GeneralConf.CARRIER_CODE)) ? "Type *" : "Package *"));
            sheet.getRow(4).getCell(3).setCellValue(("Y".equalsIgnoreCase((String) configmap.get(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL)) ? "Carrier Seal" : "Carrier Seal *"));
            sheet.getRow(4).getCell(4).setCellValue(("YES".equalsIgnoreCase((String) configmap.get("SHIPPERSEALCONFIG")) ? "Shipper Seal *" : "Shipper Seal"));

            // CREATE STYLE
            HSSFCellStyle cellstyle = book.createCellStyle();
            HSSFFont headerEven = book.createFont();
            headerEven.setFontName("Verdana");
            headerEven.setColor(HSSFColor.BLACK.index);
            cellstyle.setFont(headerEven);
            //   System.out.println("[ECOM-WAR] SICustomizeExportExcel  jsonmap::::"+jsonmap);
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
            System.out.println("[ECOM-WAR] SICustomizeExportExcel  consize::::::" + consize + ":::::" + conselect);

//
            for (int v = 0; v < consize; v++) {
                totalcolumn = new ArrayList();
                stringarraylist = new ArrayList();
                selectbox = conselect.get(v);
                vuint = jsonmap.get(selectbox + "#vunit#");
                containerNo = selectbox;
                contno = containerNo + "#containerID#";   // contno
                strEqptype = containerNo + "#eqpType#";   // eqpsize
                carrierSeal = containerNo + "#carrier#";  // carrierseal
                shipperSeal = containerNo + "#shipper#";   // shipperseal
                qtyName = containerNo + "#newqty#";       // Quantity
                pkgName = containerNo + "#newpack#";     // PackageType
                cargoweight = containerNo + "#newwt#";   // cargoweight
                mrkName = containerNo + "#mark#";
                desName = containerNo + "#descp#";
                qtyvalue =jsonmap.get(qtyName);

                totalcolumn.add("Sequence");
                totalcolumn.add(contno);
                totalcolumn.add(strEqptype);
                totalcolumn.add(carrierSeal);
                totalcolumn.add(shipperSeal);

                totalcolumn.add(qtyName);
                totalcolumn.add(pkgName);
                totalcolumn.add(cargoweight);
                totalcolumn.add(mrkName);
                totalcolumn.add(desName);

                stringarraylist.add(qtyName);
                stringarraylist.add(pkgName);
                stringarraylist.add(cargoweight);
                stringarraylist.add(mrkName);
                stringarraylist.add(desName);

                System.out.println("[ECOM-WAR] SICustomizeExportExcel  qtyvalue==> "+qtyvalue);
                commonlist = new HashMap<String, String>();
                stringarrayvalue = new ArrayList<String>();
                totalsub = new ArrayList<String>();
                if (qtyvalue != null && qtyvalue.contains("[")) {
                    totalsub = jsonStringToArray(qtyvalue);
                    //     System.out.println("[ECOM-WAR] SICustomizeExportExcel  qtyvalue::::" + qtyvalue);
                    for (int i = 0; i < stringarraylist.size(); i++) {
                        string = "";
                        Stringval = "";
                        Stringval = (String) stringarraylist.get(i);
                        //      System.out.println("[ECOM-WAR] SICustomizeExportExcel  ::::::::::::" + Stringval);
                        string = jsonmap.get(Stringval);
                        //      System.out.println("[ECOM-WAR] SICustomizeExportExcel  string::::" + string);
                        stringarrayvalue = jsonStringToArray(string);
                        commonlist.put(Stringval, stringarrayvalue);
                    }
                } 
                    System.out.println("[ECOM-WAR] SICustomizeExportExcel  commonlist::::::::::::::::" + commonlist + "totalcolumntotalcolumn=> "+totalcolumn);
                int commcolumn = totalcolumn.size();
                // values insert
                subcount = 1;
                if (totalsub != null && totalsub.size() > 0) {
                    subcount = totalsub.size();
                }
//
                for (int n = 0; n < subcount; n++) {
                    row = sheet.createRow((int) k);
                    //arrkeycomp = new ArrayList();
                    for (int i = 0; i < commcolumn; i++) {
                        cellanswer = "";
                        keycolumn = (String) totalcolumn.get(i);
                           System.out.println("[ECOM-WAR] SICustomizeExportExcel  keycolumn:::::" + keycolumn + ":::::::" + i);
                        cell = row.createCell((int) i);
                        if ("Sequence".equalsIgnoreCase(keycolumn)) {
                            cell.setCellValue(c);
                        } else {
                           arrkeycomp = (ArrayList) commonlist.get(keycolumn);
                            //    System.out.println("[ECOM-WAR] SICustomizeExportExcel  yesss::::::" + arrkeycomp);
                           if (arrkeycomp != null) {
                                cellanswer = (String) arrkeycomp.get(n);
                            } else {
                                cellanswer = jsonmap.get(keycolumn);
                                // Mask container number if HIDE_MASKPORT_CONTAINER is enabled
                                if (keycolumn.contains("#containerID#") && "Y".equalsIgnoreCase(HIDE_MASKPORT_CONTAINER)) {
                                    cellanswer = "Container " + (v + 1);
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
                strFileName = filepath + "ECOM_SI_Containerwithdetails.xls";
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
