/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ExportToExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author vasanthakumar.r
 */
public class ExcelImportBooking extends HttpServlet {

    List commoditylist = null;
    List packagelist = null;
    List hscodelist = null;
    Map hm = null;
    Map conainterlen = null;
    Map EQPHASHMAP = null;
    Map CommodityHSCode = null;
    Map COMMODITYMAP = null;
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBookingBeanRemote ecombkg = EJBLookUp.getBookingBeanLookUp();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String filepath = "";//request.getParameter("taskfin");
        String path = getServletContext().getRealPath("/");
        ServletContext contexts = getServletContext();
        int findFirst = 0;
        String result = "";
        String finalresult = "";
        String errors = "", msg = "";
        String err_result = "";
        String finerr_result = "";
        String openerror = "", openerr = "", refererror = "", refererr = "", hazerror = "", hazerr = "";
        String finerr_output = "", final_message = "", final_msg = "";
        finerr_output = "<div class=\"table-responsive\"><table class=\"table table-condensed table-striped table-bordered  table-hover dataTable table-validation \" id=\"datatableexcel\"><thead><th>Excel Row Numbers</th><th>Error Description</th></thead></tr>";
        final_msg = "<div class=\"table-responsive\"><table class=\"table table-condensed table-striped table-bordered table-hover dataTable table-validation \" id=\"datatableexcel1\"><thead><th>Excel Row Numbers</th><th>Excel row values are updated - Messages</th></thead></tr>";
        String sno = "", eqpType = null, eqpsize = null, qty = null, soc = null, hzd = null, commodity = null, hscode = null;
        String eqpStatus = null, tareWt = null, cargoweight = null, grossWt = null, volume = null, weightunit = null;
        String volumeunit = null, eqp_type = null, marinePollut = null, unno = null, flashPoint = null, flashUnit = null, commodityothers = null;
        String shippingName = null, dgClass = null, presetTemp = null, minTemp = null, maxTemp = null, tempUnit = null, nor = null;
        String unit = null, owleft = null, owRight = null, owtop = null, owAft = null, owForward = null, containerno = null, pkg = null, dgvalue = null;
        String comid = null, conid = null, othercommodity = null, commoditycheck = null, unnos = null, unnocode = null, ventval = null;
        String message = "";
        String eqpval = "", eqpvalues = "", commoditycode = "";
        String str_tarewt = "";
        int conlen = 0;
        boolean flag = false;
        float tareweight = 0;
        String tarewgt = "";
        String invalidformat = "false";
        hm = new HashMap();
        List exceltitle1 = null;
        String attachPath = "";
        String fname = "";
        HttpSession session = request.getSession();
        String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
        loginUser = loginUser + "1";
        Map map_eqptype = ecomCntrlObj.getEqpMap();
        Map map_eqpimgclass = ecomCntrlObj.getIMGClass();
        List duplicatecontainer = new ArrayList();
        String hscodeconfig = (String) session.getAttribute("HSCODECONFIG");
        String Ventilationlabel = "Ventilation" + (String) contexts.getAttribute("Ventilation");
        String commoditydummy = "", commodityoriginal = "";
        String userFruitCust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
        String Enable_Drains_Humidity = (String) contexts.getAttribute(GeneralConf.Enable_Drains_Humidity);
        String ENABLE_OOG_DETAILS = (String) contexts.getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
        //
        hm.put("Dry", "D");
        hm.put("Open", "O");
        hm.put("Reefer", "R");
        hm.put("Kilos", "K");
        hm.put("Pounds", "P");
        hm.put("Metric Tons", "T");
        hm.put("No", "N");
        hm.put("Yes", "Y");
        hm.put("Celsius", "C");
        hm.put("Fahrenheit", "F");
        hm.put("Cubic Feet", "F");
        hm.put("Cubic Meters", "M");
        hm.put("Laden", "F");
        hm.put("Empty", "M");
        hm.put("Meters", "M");
        hm.put("Feet", "F");
        //
        conainterlen = new HashMap<String, String>();
        conainterlen.put("Eqp No", "11");
        conainterlen.put("Volume", "8");
        conainterlen.put("Cargo Weight", "9");
        conainterlen.put(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Quantity" : "No.of Packages"), "8");
        //  conainterlen.put("HS Code", "25");
        conainterlen.put("Left", "6");
        conainterlen.put("Right", "6");
        conainterlen.put("Forward", "6");
        conainterlen.put("After", "6");
        conainterlen.put("Top", "6");
        conainterlen.put("Preset Temp", "6");
        conainterlen.put("Min Temp", "6");
        conainterlen.put("Max Temp", "6");
        conainterlen.put("UN No", "600");
        conainterlen.put("Flash Point", "8");
        conainterlen.put(Ventilationlabel, "5");
        conainterlen.put("Humidity(%)", "5");
        conainterlen.put("Co2", "5");
        conainterlen.put("O2", "5");
        // Sno	Eqp No. *	SOC *	Eqp Size *	Others (Commodity)*	Commodity *
        //      Volume 	Volume Unit	Package	Cargo Weight *	Weight Unit 	No. of Packages
        //     Hazardous	HS Code	Unit	Left	Right	Forward	Alt	Top	NOR	Preset Temp
        //    Min Temp	Max Temp	Temp Unit	Marine Pollutant	UN No	Flash Point
        //    Flash Point unit	Eqp Status
        List exceltitle = new ArrayList();
        exceltitle.add("Sno");
        exceltitle.add("Eqp No. *");
        exceltitle.add("SOC *");
        exceltitle.add("Eqp Size *");
        exceltitle.add("Hazardous");
        exceltitle.add("Others (Commodity)*");
        exceltitle.add("Commodity *");
        exceltitle.add("HS Code");
        exceltitle.add("Volume");
        exceltitle.add("Volume Unit");
        exceltitle.add(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Type" : "Package"));
        exceltitle.add("Cargo Weight *");
        exceltitle.add("Weight Unit");
        exceltitle.add(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Quantity" : "No.of Packages"));
        exceltitle.add("Unit");
        exceltitle.add("Left");
        exceltitle.add("Right");
        exceltitle.add("Forward");
        exceltitle.add("Alt");
        exceltitle.add("Top");
        exceltitle.add("NOR");
        exceltitle.add("Preset Temp");
        exceltitle.add("Min Temp");
        exceltitle.add("Max Temp");
        exceltitle.add("Temp Unit");
        exceltitle.add("Marine Pollutant");
        exceltitle.add("UN No");
        exceltitle.add("Flash Point");
        exceltitle.add("Flash Point unit");
        exceltitle.add("Eqp Status");
        exceltitle.add(Ventilationlabel);
        if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {
            exceltitle.add("Humidity(%)");
            exceltitle.add("Drains");
            exceltitle.add("Co2");
            exceltitle.add("O2");
        } else if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {
            exceltitle.add("OOG");
        }
        //
        Map comlist = ecomCntrlObj.getCommodity();
        Hashtable packagemap = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
        EQPHASHMAP = ecomCntrlObj.getEqpments();
        Map hscodeMAP = ecomCntrlObj.getHSCode();
        CommodityHSCode = ecomCntrlObj.getcommodityrecords();
        //   System.out.println("[ECOM-WAR] ExcelImportBooking   EQPHASHMAP::::::" + EQPHASHMAP);
        commoditylist = new ArrayList();
        packagelist = new ArrayList();
        hscodelist = new ArrayList();
        COMMODITYMAP = new HashMap();
        Iterator it = null;
        it = comlist.entrySet().iterator();
        System.setProperty("file.encoding", "UTF-16");
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            commodityoriginal = pairs.getValue().toString().trim() + " ~~ " + pairs.getKey().toString().trim();
            commodityoriginal = new String(commodityoriginal.getBytes("UTF-8"), "ISO-8859-1");
            System.out.println("[ECOM-WAR] ExcelImportBooking   yess::::" + commodityoriginal);
            commoditydummy = pairs.getValue().toString().trim() + "000" + pairs.getKey().toString().trim();
            commoditydummy = specialcharremove(commoditydummy);
            COMMODITYMAP.put(commoditydummy, commodityoriginal);
            commoditylist.add(commoditydummy);
        }
        it = packagemap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            packagelist.add(pairs.getValue().toString().toUpperCase().trim() + " ~~ " + pairs.getKey().toString().toUpperCase().trim());
        }
        it = hscodeMAP.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            hscodelist.add(pairs.getValue().toString().toUpperCase().trim() + " ~~ " + pairs.getKey().toString().toUpperCase().trim());
        }
        //System.out.println("[ECOM-WAR] ExcelImportBooking   >>>hscodelist>>" + hscodelist);
        try {

            File dirs = null;
            //
            String exceluploadpath = null;
            String filefolder = null;
            exceluploadpath = (String) contexts.getAttribute(GeneralConf.exceluploadpath);
            attachPath = contexts.getRealPath("/");
            filefolder = exceluploadpath.replace("/", "\\");
            attachPath = filefolder + "Template";
            //
            dirs = new File(attachPath);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            attachPath = exceluploadpath + "Template/";
//
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List items = upload.parseRequest(request);
            Iterator iter1 = items.iterator();
            while (iter1.hasNext()) {
                FileItem item = (FileItem) iter1.next();
                if (item.isFormField()) {
                    String name = item.getFieldName();
                } else {
                    String filename = item.getName();
                    if (filename.lastIndexOf("\\") != -1) {
                        fname = filename.substring(filename.lastIndexOf("\\"), filename.length());
                    } else {
                        fname = filename.trim();
                        fname = loginUser + fname;
                    }
                }
            }

            File delfile = new File(attachPath + fname);
            delfile.delete();
//
            int r = 0;
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                    String name = item.getFieldName();
                } else {
                    String filename = item.getName();
                    if (filename.lastIndexOf("\\") != -1) {
                        fname = filename.substring(filename.lastIndexOf("\\"), filename.length());
                    } else {
                        fname = loginUser + filename.trim();
                    }
                    if (fname != null && fname.length() > 0 && r == 0) {
                        filepath = attachPath + fname;
                        File uploadedFile = new File(attachPath + fname);
                        item.write(uploadedFile);
                        r = 1;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] ExcelImportBooking   Exception in uploading");
            e.printStackTrace();

        }

        //save Done in the puticular place
        File file1 = new File(filepath.trim());
        if (filepath != null && file1.exists()) {
            HSSFWorkbook wb = null;
            HSSFSheet sheet = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            String cellvalue = "";
            int a = 0;
            String error = "";
            //
            String fileformat = filepath.substring(filepath.lastIndexOf("."), filepath.length());
            System.out.println("[ECOM-WAR] ExcelImportBooking   fileformat:::::::::::::::::::::::::::::::::::::::::" + fileformat + "::::::" + filepath);
            if (fileformat.trim().equalsIgnoreCase(".xls")) {
                try {
                    FileInputStream fis = new FileInputStream(file1);
                    //System.out.println("[ECOM-WAR] ExcelImportBooking   welcomeeeeeee"+fis);
                    POIFSFileSystem fs = new POIFSFileSystem(fis);
                    //System.out.println("[ECOM-WAR] ExcelImportBooking   welcomeeeeeee"+fs);
                    wb = new HSSFWorkbook(fs);
                    sheet = wb.getSheetAt(0);
                    // Template Checking
                    int rowStart = 6; // 0 based not 1 based rows
                    int rowEnd = 7;
                    HSSFRow rw = null;
                    HSSFCell c = null;
                    int lastColumn = ("Y".equalsIgnoreCase(Enable_Drains_Humidity) ? 35 : 31);
                    lastColumn = ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS) ? 32 : lastColumn);
                    for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                        exceltitle1 = new ArrayList();
                        rw = sheet.getRow(rowNum);
                        for (int cn = 0; cn < lastColumn; cn++) {
                            c = rw.getCell((int) cn);
                            if (c != null) {
                                cellvalue = getCellValue(c);
                                exceltitle1.add(cellvalue.trim());
                            }
                        }
                    }
                    //
                    cellvalue = "";
                    int lastrownum = sheet.getLastRowNum();
                    for (int r = 0; r <= lastrownum; r++) {
                        row = sheet.getRow(r);
                        if (row != null) {
                            int cct = 0;
                            for (a = row.getFirstCellNum(); a <= row.getLastCellNum(); a++) {
                                cell = row.getCell((int) a);
                                //  System.out.println("[ECOM-WAR] ExcelImportBooking   Excel:::" + cell);
                                if (cell != null) {
                                    cellvalue = getCellValue(cell);
                                    if (cellvalue.trim() != "" && cellvalue.trim().length() > 0) {
                                        //    System.out.println("[ECOM-WAR] ExcelImportBooking   cellvalue:::::::" + cellvalue);
                                        if (cellvalue.equalsIgnoreCase("Sno")) {
                                            findFirst = row.getRowNum();
                                        }
                                        cct++;
                                    }
                                }
                            }
                        }
                    }
//
                    findFirst = findFirst + 1;
                    System.out.println("[ECOM-WAR] ExcelImportBooking findFirst -" + findFirst);
                    for (int i = findFirst; i <= lastrownum; i++) {
                        row = sheet.getRow(i);
                        if (row != null) {
                            int cct = 0;
                            for (a = row.getFirstCellNum(); a <= row.getLastCellNum(); a++) {
                                cell = row.getCell((int) a);
                                if (cell != null) {
                                    cellvalue = getCellValue(cell);
                                    if (cellvalue.trim() != "" && cellvalue.trim().length() > 0) {
                                        cct++;
                                    }
                                }
                            }

                            if (cct > 0) {
                                for (a = row.getFirstCellNum(); a <= row.getLastCellNum(); a++) {
                                    cell = row.getCell((int) a);
                                    //      System.out.println("[ECOM-WAR] ExcelImportBooking   cell:::" + cell + "::::" + a);
                                    if (a == 0) // sno
                                    {
                                        //    System.out.println("[ECOM-WAR] ExcelImportBooking   cell:::::"+cell);
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            sno = checkEmpty(cellvalue.trim());
                                            result += sno + "~~~";
                                        } else {
                                            result += "no~records" + "~~~";
                                        }

                                    }
//
                                    if (a == 1) // Eqp No
                                    {
                                        if (cell != null) {
                                            containerno = "";
                                            cellvalue = getCellValue(cell);
                                            containerno = checkEmpty(cellvalue.trim());
                                            result += containerno + "~~~";
                                            err_result += checkMandatory("Eqp No", containerno, i);
                                            if (duplicatecontainer.contains(containerno)) {
                                                err_result += "Same Container number is not allowed multiple times.(container number:" + containerno + ")";
                                            }
                                            if (containerno != null && containerno.length() > 0 && !"no~records".equalsIgnoreCase(containerno)) {
                                                err_result += checklength("Eqp No", containerno, "11");
                                                duplicatecontainer.add(containerno);
                                            }
                                        } else {
                                            containerno = "";
                                            result += "no~records" + "~~~";
                                            err_result += checkMandatory("Eqp No", containerno, i);
                                        }
                                        conlen++;
                                    }

                                    if (a == 2) // SOC
                                    {
                                        if (cell != null) {
                                            soc = "";
                                            cellvalue = getCellValue(cell);
                                            soc = checkEmpty(cellvalue.trim());
                                            if (soc != null && !"".equalsIgnoreCase(soc) && !"no~records".equalsIgnoreCase(soc)) {
                                                soc = getData(soc);
                                            }
                                            result += soc + "~~~";
                                            err_result += checkMandatory("SOC", soc, i);
                                        } else {
                                            soc = "";
                                            result += "no~records" + "~~~";
                                            err_result += checkMandatory("SOC", soc, i);
                                        }

                                    }

                                    if (a == 3) // Eqp Size
                                    {
                                        if (cell != null) {
                                            eqpsize = "";
                                            eqpvalues = "";
                                            eqpval = "";
                                            cellvalue = getCellValue(cell);
                                            if (containerno != null && containerno.length() > 0 && !"no~records".equalsIgnoreCase(containerno)) {
                                                eqpsize = checkEmpty(cellvalue.trim());
                                                eqpsize = getKeyFromValue(eqpsize, eqpsize, cct);
                                                eqpval = eqpsize;
                                                //
                                                eqpsize = ecomCntrlObj.checkEQPSize(containerno);
                                                eqpvalues = eqpsize;
                                            }    // TA20
                                            if (eqpsize == null || eqpsize.length() == 0) {
                                                eqpsize = checkEmpty(cellvalue.trim());
                                                eqpsize = getKeyFromValue(eqpsize, eqpsize, cct);
                                            }
                                            System.out.println("[ECOM-WAR] ExcelImportBooking   eqpsizessssss:::::::::::::::" + eqpvalues + ":::::" + eqpval);
                                            result += eqpsize + "~~~";
                                            err_result += checkMandatory("Eqp Size", eqpsize, i);
                                            if (eqpsize != null && eqpsize.length() > 0 && !"no~records".equalsIgnoreCase(eqpsize)) {
                                                err_result += checkEqp("Eqp Size", cellvalue, cct);
                                                eqpType = (String) map_eqptype.get(eqpsize);
                                                if (eqpType != null && !"D".equalsIgnoreCase(eqpType) && !"O".equalsIgnoreCase(eqpType) && !"R".equalsIgnoreCase(eqpType)) {
                                                    eqpType = "D";
                                                }
                                                result += eqpType + "~~~";
                                                if (eqpval != null && eqpval.length() > 0 && eqpvalues != null && eqpvalues.length() > 0 && !eqpval.equalsIgnoreCase(eqpvalues)) {
                                                    message = "";
                                                    message += eqpchange(eqpsize, eqpsize, cct);
                                                }
                                                System.out.println("[ECOM-WAR] ExcelImportBooking   message::::::::::::::" + message);
                                            }
                                            if (eqpType == null || eqpType.length() == 0) {
                                                result += "no~records" + "~~~";
                                            }
                                        } else {
                                            eqpsize = "";
                                            result += "no~records" + "~~~";
                                            err_result += checkMandatory("Eqp Size ", eqpsize, i);
                                        }
                                    }

                                    if (a == 4) // Hazardous
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            hzd = checkEmpty(cellvalue.trim());
                                            if (hzd != null && !"".equalsIgnoreCase(hzd) && !"no~records".equalsIgnoreCase(hzd)) {
                                                hzd = getData(hzd);
                                            }
                                            result += hzd + "~~~";
                                        } else {
                                            result += "no~records" + "~~~";
                                        }
                                    }

                                    if (a == 5) // Others Commodity
                                    {
                                        if (cell != null) {
                                            othercommodity = "";
                                            cellvalue = getCellValue(cell);
                                            othercommodity = checkEmpty(cellvalue.trim());
                                            System.out.println("[ECOM-WAR] ExcelImportBooking   others commodity::::::::::::::::" + othercommodity);
                                            err_result += checkMandatory("Others Commodity ", othercommodity, i);
                                            if (othercommodity != null && !"".equalsIgnoreCase(othercommodity) && !"no~records".equalsIgnoreCase(othercommodity)) {
                                                othercommodity = getData(othercommodity);
                                            }
                                            result += othercommodity + "~~~";
                                        } else {
                                            othercommodity = "";
                                            result += "no~records" + "~~~";
                                            err_result += checkMandatory("Others Commodity ", othercommodity, i);
                                        }
                                    }

                                    if (a == 6) // Commodity
                                    {
                                        if (cell != null) {
                                            commodity = "";
                                            cellvalue = getCellValue(cell);
                                            commodity = checkEmpty(cellvalue.trim());
                                            //  result += commodity + "~~~";
                                            result += getCommodity(commodity) + "~~~";
                                            commoditycode = "";
                                            String[] Commoditygroupcode = commodity.split("~~");
//                                            if (Commoditygroupcode != null && Commoditygroupcode.length == 2) {
//                                                commoditycode = Commoditygroupcode[1];
//                                                commoditycode = commoditycode.trim();
//                                            }
                                            if (commodity != null && commodity.contains("000")) {
                                                commoditycode = commodity.substring(commodity.indexOf("000") + 3, commodity.length());
                                            }
                                            System.out.println("[ECOM-WAR] ExcelImportBooking   Commodity answerr::::::::::" + commodity);
                                            err_result += checkMandatory("Commodity", commodity, i);
                                            if (othercommodity != null && othercommodity.length() > 0 && ("N".equalsIgnoreCase(othercommodity) || "No".equalsIgnoreCase(othercommodity))) {
                                                err_result += checkCommodity("Commodity", commodity, i);
                                            }
                                            //  result += othercommodity + "~~~";
                                        } else {
                                            commodity = "";
                                            System.out.println("[ECOM-WAR] ExcelImportBooking   Commodity:::::" + commodity);
                                            result += "no~records" + "~~~";
                                            err_result += checkMandatory("Commodity ", commodity, i);
                                        }
                                    }

                                    if (a == 7) // HS Code
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            hscode = checkEmpty(cellvalue.trim());
                                            //  hscode = convertIntNumber(hscode);
                                            //    System.out.println("[ECOM-WAR] ExcelImportBooking   Hs Code:::::::::::::::" + hscode);
                                            result += hscode + "~~~";

                                            if ("Yes".equalsIgnoreCase(hscodeconfig)) {
                                                err_result += checkMandatory("HS Code ", hscode, i);
                                            }
                                            // err_result += checkMandatory("HS Code ", hscode, i);
                                            if (hscode != null && !"".equalsIgnoreCase(hscode) && !"no~records".equalsIgnoreCase(hscode)) {
                                                if (othercommodity != null && othercommodity.length() > 0 && ("N".equalsIgnoreCase(othercommodity) || "No".equalsIgnoreCase(othercommodity))) {
                                                    err_result += checkhscode("HS Code ", hscode, i);
                                                    err_result += checkHscodeCommodity("HS Code ", hscode, commoditycode, i);
                                                }
//                                                err_result += checkNumber("HS Code", hscode, i);
//                                                err_result += checklength("HS Code", hscode, "25");
                                            }
                                            if (hzd != null && hzd.length() > 0 && "Y".equalsIgnoreCase(hzd)) {
                                                hazerror += checkMandatoryCommon("HS Code", hscode, i);
                                            }

                                        } else {
                                            result += "no~records" + "~~~";
                                            if (hzd != null && hzd.length() > 0 && "Y".equalsIgnoreCase(hzd)) {
                                                hazerror += checkMandatoryCommon("HS Code", hscode, i);
                                            }
                                        }
                                    }

                                    if (a == 8) // Volume
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            volume = checkEmpty(cellvalue.trim());
                                            volume = convertIntNumber(volume);
                                            result += volume + "~~~";
                                            if (volume != null && !"".equalsIgnoreCase(volume) && !"no~records".equalsIgnoreCase(volume)) {
                                                err_result += checkInteger("Volume", volume, i);
                                                err_result += checklength("Volume", volume, "8");
                                            }
                                        } else {
                                            result += "no~records" + "~~~";
                                        }
                                    }

                                    if (a == 9) // Volume Unit
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            volumeunit = checkEmpty(cellvalue.trim());
                                            if (volumeunit != null && !"".equalsIgnoreCase(volumeunit) && !"no~records".equalsIgnoreCase(volumeunit)) {
                                                volumeunit = getData(volumeunit);
                                            }
                                            result += volumeunit + "~~~";
                                        } else {
                                            result += "no~records" + "~~~";
                                        }
                                    }

                                    if (a == 10) // Package
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            pkg = checkEmpty(cellvalue.trim());
                                            result += pkg + "~~~";
                                            if (pkg != null && !"".equalsIgnoreCase(pkg) && !"no~records".equalsIgnoreCase(pkg)) {
                                                err_result += checkPackage(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Type" : "Package"), pkg, i);
                                            }
                                        } else {
                                            result += "no~records" + "~~~";
                                        }
                                    }

                                    if (a == 11) // Cargo Weight
                                    {
                                        if (cell != null) {
                                            cargoweight = "";
                                            cellvalue = getCellValue(cell);
                                            cargoweight = checkEmpty(cellvalue.trim());
                                            cargoweight = convertIntNumber(cargoweight);
                                            result += cargoweight + "~~~";
                                            err_result += checkMandatory("Cargo Weight ", cargoweight, i);
                                            if (cargoweight != null && !"".equalsIgnoreCase(cargoweight) && !"no~records".equalsIgnoreCase(cargoweight)) {
                                                err_result += checkInteger("Cargo Weight ", cargoweight, i);
                                                err_result += checklength("Cargo Weight", cargoweight, "8");
                                            }
                                        } else {
                                            cargoweight = "";
                                            result += "no~records" + "~~~";
                                            err_result += checkMandatory("Cargo Weight", cargoweight, i);
                                        }
                                    }

                                    if (a == 12) // Weight Unit
                                    {
                                        if (cell != null) {
                                            tareweight = 0;
                                            cellvalue = getCellValue(cell);
                                            weightunit = checkEmpty(cellvalue.trim());
                                            if (weightunit != null && !"".equalsIgnoreCase(weightunit) && !"no~records".equalsIgnoreCase(weightunit)) {
                                                weightunit = getData(weightunit);
                                                //
                                                if (containerno != null && !"no~records".equalsIgnoreCase(containerno) && eqpType != null && !"no~records".equalsIgnoreCase(eqpType) && eqpsize != null && !"no~records".equalsIgnoreCase(eqpsize)) {
                                                    try {
                                                        tareweight = ecombkg.getTareWtForAmendment(containerno, eqpType, eqpsize);
                                                        if (tareweight > 0) {
                                                        } else {
                                                            tarewgt = ecombkg.getTareweight(eqpType, eqpsize);
                                                            tareweight = Float.parseFloat(tarewgt);
                                                        }
                                                        str_tarewt = String.valueOf(tarewgt);
                                                    } catch (Exception ex) {
                                                        System.out.println("[ECOM-WAR] ExcelImportBooking   tareweight Error::::" + ex.getMessage());
                                                        str_tarewt = "0";
                                                        tareweight = 0;
                                                    }
                                                }
                                                err_result += checkTons(weightunit, str_tarewt, cargoweight, i);
                                            }
                                            result += weightunit + "~~~";
                                        } else {
                                            result += "no~records" + "~~~";
                                        }
                                    }

                                    if (a == 13) // No. of Packages
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            qty = checkEmpty(cellvalue.trim());
                                            qty = convertIntNumber(qty);
                                            result += qty + "~~~";
                                            System.out.println("[ECOM-WAR] ExcelImportBooking   qty::::::" + qty);
                                            if (qty != null && !"".equalsIgnoreCase(qty) && !"no~records".equalsIgnoreCase(qty)) {
                                                err_result += checkInteger(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Quantity" : "No.of Packages"), qty, i);
                                                err_result += checklength(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Quantity" : "No.of Packages"), qty, "8");
                                            }
                                        } else {
                                            result += "no~records" + "~~~";
                                        }
                                    }

                                    //   
                                    if (a == 14) // Unit
                                    {
                                        if (cell != null) {
                                            unit = "";
                                            cellvalue = getCellValue(cell);
                                            unit = checkEmpty(cellvalue.trim());
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //  err_result += checkMandatory("Equipment is Open.UNIT ", unit, i);
                                                openerror += checkMandatoryCommon("UNIT", unit, i);
                                            }
                                            if (unit != null && !"".equalsIgnoreCase(unit) && !"no~records".equalsIgnoreCase(unit)) {
                                                unit = getData(unit);
                                            }
                                            result += unit + "~~~";
                                        } else {
                                            unit = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //   err_result += checkMandatory("Equipment is 'Open',UNIT ", unit, i);
                                                openerror += checkMandatoryCommon("UNIT", unit, i);
                                            }
                                        }
                                    }

                                    if (a == 15) // Left
                                    {
                                        if (cell != null) {
                                            owleft = "";
                                            cellvalue = getCellValue(cell);
                                            owleft = checkEmpty(cellvalue.trim());
                                            owleft = convertIntNumber(owleft);
                                            result += owleft + "~~~";
                                            System.out.println("[ECOM-WAR] ExcelImportBooking   owleft:::::::::::::::::::::::::::" + owleft);
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //   err_result += checkMandatory("Equipment is 'Open',Left ", owleft, i);
                                                openerror += checkMandatoryCommon("Left", owleft, i);
                                            }
                                            if (owleft != null && !"".equalsIgnoreCase(owleft) && !"no~records".equalsIgnoreCase(owleft)) {
                                                err_result += checkNumber("Left ", owleft, i);
                                                err_result += checklength("Left", owleft, "6");
                                            }
                                        } else {
                                            owleft = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //   err_result += checkMandatory("Equipment is 'Open',Left ", owleft, i);
                                                openerror += checkMandatoryCommon("Left", owleft, i);
                                            }
                                        }
                                    }

                                    if (a == 16) // Right
                                    {
                                        if (cell != null) {
                                            owRight = "";
                                            cellvalue = getCellValue(cell);
                                            owRight = checkEmpty(cellvalue.trim());
                                            owRight = convertIntNumber(owRight);
                                            result += owRight + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //    err_result += checkMandatory("Equipment is 'Open',Right ", owRight, i);
                                                openerror += checkMandatoryCommon("Right", owRight, i);
                                            }
                                            if (owRight != null && !"".equalsIgnoreCase(owRight) && !"no~records".equalsIgnoreCase(owRight)) {
                                                err_result += checkNumber("Right ", owRight, i);
                                                err_result += checklength("Right", owRight, "6");
                                            }
                                        } else {
                                            owRight = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //    err_result += checkMandatory("Equipment is 'Open',Right ", owRight, i);
                                                openerror += checkMandatoryCommon("Right", owRight, i);
                                            }
                                        }
                                    }

                                    if (a == 17) // Forward
                                    {
                                        if (cell != null) {
                                            owForward = "";
                                            cellvalue = getCellValue(cell);
                                            owForward = checkEmpty(cellvalue.trim());
                                            owForward = convertIntNumber(owForward);
                                            result += owForward + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //   err_result += checkMandatory("Equipment is 'Open',Forward ", owForward, i);
                                                openerror += checkMandatoryCommon("Forward", owForward, i);
                                            }
                                            if (owForward != null && !"".equalsIgnoreCase(owForward) && !"no~records".equalsIgnoreCase(owForward)) {
                                                err_result += checkNumber("Forward ", owForward, i);
                                                err_result += checklength("Forward", owForward, "6");
                                            }
                                        } else {
                                            owForward = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //   err_result += checkMandatory("Equipment is 'Open',Forward ", owForward, i);
                                                openerror += checkMandatoryCommon("Forward", owForward, i);
                                            }
                                        }
                                    }

                                    if (a == 18) // Alt
                                    {
                                        if (cell != null) {
                                            owAft = "";
                                            cellvalue = getCellValue(cell);
                                            owAft = checkEmpty(cellvalue.trim());
                                            owAft = convertIntNumber(owAft);
                                            result += owAft + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //    err_result += checkMandatory("Equipment is 'Open',Aft ", owAft, i);
                                                openerror += checkMandatoryCommon("Aft", owAft, i);
                                            }
                                            if (owAft != null && !"".equalsIgnoreCase(owAft) && !"no~records".equalsIgnoreCase(owAft)) {
                                                err_result += checkNumber("After ", owAft, i);
                                                err_result += checklength("After", owAft, "6");
                                            }
                                        } else {
                                            owAft = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //  err_result += checkMandatory("Equipment is 'Open',Aft ", owAft, i);
                                                openerror += checkMandatoryCommon("Aft", owAft, i);
                                            }
                                        }
                                    }

                                    if (a == 19) // Top
                                    {
                                        if (cell != null) {
                                            owtop = "";
                                            cellvalue = getCellValue(cell);
                                            owtop = checkEmpty(cellvalue.trim());
                                            owtop = convertIntNumber(owtop);
                                            result += owtop + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //   err_result += checkMandatory("Equipment is 'Open',Top ", owtop, i);
                                                openerror += checkMandatoryCommon("Top", owtop, i);
                                            }
                                            if (owtop != null && !"".equalsIgnoreCase(owtop) && !"no~records".equalsIgnoreCase(owtop)) {
                                                err_result += checkNumber("Top ", owtop, i);
                                                err_result += checklength("Top", owtop, "6");
                                            }
                                        } else {
                                            owtop = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "O".equalsIgnoreCase(eqpType)) {
                                                //   err_result += checkMandatory("Equipment is 'Open',Top ", owtop, i);
                                                openerror += checkMandatoryCommon("Top", owtop, i);
                                            }
                                        }
                                    }

                                    if (a == 20) // NOR
                                    {
                                        if (cell != null) {
                                            nor = "";
                                            cellvalue = getCellValue(cell);
                                            nor = checkEmpty(cellvalue.trim());
                                            if (eqpType != null && eqpType.length() > 0 && "R".equalsIgnoreCase(eqpType) && "N".equalsIgnoreCase(nor)) {
                                                // err_result += checkMandatory("Equipment is 'Reefer',Nor ", nor, i);
                                                refererror += checkMandatoryCommon("Nor", nor, i);
                                            }
                                            if (nor != null && !"".equalsIgnoreCase(nor) && !"no~records".equalsIgnoreCase(nor)) {
                                                nor = getData(nor);
                                            }
                                            result += nor + "~~~";
                                        } else {
                                            nor = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "R".equalsIgnoreCase(eqpType)) {
                                                //  err_result += checkMandatory("Equipment is 'Reefer',Nor ", nor, i);
                                                refererror += checkMandatoryCommon("Nor", nor, i);
                                            }
                                        }
                                    }

                                    if (a == 21) // Preset Temp
                                    {
                                        if (cell != null) {
                                            presetTemp = "";
                                            cellvalue = getCellValue(cell);
                                            presetTemp = checkEmpty(cellvalue.trim());
                                            presetTemp = convertIntNumber(presetTemp);
                                            result += presetTemp + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "R".equalsIgnoreCase(eqpType) && "N".equalsIgnoreCase(nor)) {
                                                //  err_result += checkMandatory("Equipment is 'Reefer',Preset Temp ", presetTemp, i);
                                                refererror += checkMandatoryCommon("Preset Temp", presetTemp, i);
                                            }
                                            if (presetTemp != null && !"".equalsIgnoreCase(presetTemp) && !"no~records".equalsIgnoreCase(presetTemp)) {
                                                err_result += checkNumber("Preset Temp ", presetTemp, i);
                                                err_result += checklength("Preset Temp ", presetTemp, "8");
                                            }
                                        } else {
                                            presetTemp = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "R".equalsIgnoreCase(eqpType) && "N".equalsIgnoreCase(nor)) {
                                                //  err_result += checkMandatory("Equipment is 'Reefer',Preset Temp ", presetTemp, i);
                                                refererror += checkMandatoryCommon("Preset Temp", presetTemp, i);
                                            }
                                        }
                                    }

                                    if (a == 22) // Min Temp
                                    {
                                        if (cell != null) {
                                            minTemp = "";
                                            cellvalue = getCellValue(cell);
                                            minTemp = checkEmpty(cellvalue.trim());
                                            minTemp = convertIntNumber(minTemp);
                                            result += minTemp + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "R".equalsIgnoreCase(eqpType) && ("N".equalsIgnoreCase(nor) || "No".equalsIgnoreCase(nor))) {
                                                //  err_result += checkMandatory("Equipment is 'Reefer',Min Temp ", minTemp, i);
                                                //refererror += checkMandatoryCommon("Min Temp", minTemp, i);
                                            }
                                            if (minTemp != null && !"".equalsIgnoreCase(minTemp) && !"no~records".equalsIgnoreCase(minTemp)) {
                                                err_result += checkNumber("Min Temp ", minTemp, i);
                                                err_result += checklength("Min Temp", minTemp, "8");
                                            }
                                        } else {
                                            minTemp = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "R".equalsIgnoreCase(eqpType) && "N".equalsIgnoreCase(nor)) {
                                                //  err_result += checkMandatory("Equipment is 'Reefer',Min Temp ", minTemp, i);
                                                //refererror += checkMandatoryCommon("Min Temp", minTemp, i);
                                            }
                                        }
                                    }

                                    if (a == 23) // Max Temp
                                    {
                                        flag = false;
                                        if (cell != null) {
                                            maxTemp = "";
                                            cellvalue = getCellValue(cell);
                                            maxTemp = checkEmpty(cellvalue.trim());
                                            maxTemp = convertIntNumber(maxTemp);
                                            result += maxTemp + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "R".equalsIgnoreCase(eqpType) && "N".equalsIgnoreCase(nor)) {
                                                //   err_result += checkMandatory("Equipment is 'Reefer',Max Temp ", maxTemp, i);
                                                //refererror += checkMandatoryCommon("Max Temp", maxTemp, i);
                                                flag = true;
                                            }
                                            if (maxTemp != null && !"".equalsIgnoreCase(maxTemp) && !"no~records".equalsIgnoreCase(maxTemp)) {
                                                err_result += checkNumber("Max Temp ", maxTemp, i);
                                                err_result += checklength("Max Temp", maxTemp, "8");
                                            }
                                        } else {
                                            maxTemp = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "R".equalsIgnoreCase(eqpType) && "N".equalsIgnoreCase(nor)) {
                                                // err_result += checkMandatory("Equipment is 'Reefer',Max Temp ", maxTemp, i);
                                                // refererror += checkMandatoryCommon("Max Temp", maxTemp, i);
                                            }
                                        }
                                        if (presetTemp != null && !"".equalsIgnoreCase(presetTemp) && !"no~records".equalsIgnoreCase(presetTemp) && flag == true) {
                                            if (minTemp != null && !"".equalsIgnoreCase(minTemp) && !"no~records".equalsIgnoreCase(minTemp)) {
                                                if (maxTemp != null && !"".equalsIgnoreCase(maxTemp) && !"no~records".equalsIgnoreCase(maxTemp)) {
                                                    //  System.out.println("[ECOM-WAR] ExcelImportBooking   output:::::::::::::::::"+minTemp+"::::"+maxTemp+":::::"+presetTemp);
                                                    err_result += checkPresetTemp("Preset Temp ", minTemp, maxTemp, presetTemp);
                                                    err_result += checkreferTemp("Min Temp ", minTemp, maxTemp);
                                                    err_result += checkreferTemp("Max Temp ", minTemp, maxTemp);
                                                }
                                            }
                                        }
                                    }

                                    if (a == 24) // Temp Unit
                                    {
                                        if (cell != null) {
                                            tempUnit = "";
                                            cellvalue = getCellValue(cell);
                                            tempUnit = checkEmpty(cellvalue.trim());
                                            if (eqpType != null && eqpType.length() > 0 && "R".equalsIgnoreCase(eqpType) && "N".equalsIgnoreCase(nor)) {
                                                //  err_result += checkMandatory("Equipment is 'Reefer',Temp Unit ", tempUnit, i);
                                                refererror += checkMandatoryCommon("Temp Unit", tempUnit, i);
                                            }
                                            if (tempUnit != null && !"".equalsIgnoreCase(tempUnit) && !"no~records".equalsIgnoreCase(tempUnit)) {
                                                tempUnit = getData(tempUnit);
                                            }
                                            result += tempUnit + "~~~";
                                        } else {
                                            tempUnit = "";
                                            result += "no~records" + "~~~";
                                            if (eqpType != null && eqpType.length() > 0 && "R".equalsIgnoreCase(eqpType) && "N".equalsIgnoreCase(nor)) {
                                                //  err_result += checkMandatory("Equipment is 'Reefer',Temp Unit ", tempUnit, i);
                                                refererror += checkMandatoryCommon("Temp Unit", tempUnit, i);
                                            }
                                        }
                                    }

                                    //
                                    if (a == 25) // Marine Pollutant
                                    {
                                        if (cell != null) {
                                            marinePollut = "";
                                            cellvalue = getCellValue(cell);
                                            marinePollut = checkEmpty(cellvalue.trim());
                                            System.out.println("[ECOM-WAR] ExcelImportBooking   hzd::::::::::::::::::" + hzd);
                                            if (hzd != null && hzd.length() > 0 && "Y".equalsIgnoreCase(hzd)) {
                                                // err_result += checkMandatory("Hazardous value is 'YES'.Marine Pollutant ", marinePollut, i);
                                                hazerror += checkMandatoryCommon("Marine Pollutant", marinePollut, i);
                                            }
                                            if (marinePollut != null && !"".equalsIgnoreCase(marinePollut) && !"no~records".equalsIgnoreCase(marinePollut)) {
                                                marinePollut = getData(marinePollut);
                                            }
                                            result += marinePollut + "~~~";
                                        } else {
                                            marinePollut = "";
                                            result += "no~records" + "~~~";
                                            if (hzd != null && hzd.length() > 0 && "Y".equalsIgnoreCase(hzd)) {
                                                //  err_result += checkMandatory("Hazardous value is 'YES',Marine Pollutant ", marinePollut, i);
                                                hazerror += checkMandatoryCommon("Marine Pollutant", marinePollut, i);
                                            }
                                        }
                                    }

                                    if (a == 26) // UN No
                                    {
                                        if (cell != null) {
                                            unno = "";
                                            dgvalue = "";
                                            cellvalue = getCellValue(cell);
                                            unno = checkEmpty(cellvalue.trim());
                                            result += unno + "~~~";
                                            if (hzd != null && hzd.length() > 0 && "Y".equalsIgnoreCase(hzd)) {
                                                //  err_result += checkMandatory("Hazardous value is 'YES',UN No ", unno, i);
                                                hazerror += checkMandatoryCommon("UN No", unno, i);
                                            }
                                            if (unno != null && !"".equalsIgnoreCase(unno) && !"no~records".equalsIgnoreCase(unno)) {
                                                dgClass = (String) map_eqpimgclass.get(unno);
                                                if (dgClass == null) {
                                                    dgvalue = unno;
                                                    dgvalue = dgvalue.replace("?", "¦");
                                                    dgClass = (String) map_eqpimgclass.get(dgvalue);
                                                }

                                                if (unno != null && unno.contains("~~")) {
                                                    String splitun[] = unno.split("~~");
                                                    shippingName = splitun[1];
                                                    if (shippingName != null) {
                                                        shippingName = shippingName.trim();
                                                    }
                                                }
                                                dgClass = checkEmpty(dgClass);
                                                result += dgClass + "~~~";
                                                result += shippingName + "~~~";
                                            } else {
                                                result += "no~records" + "~~~";
                                                result += "no~records" + "~~~";
                                            }
                                        } else {
                                            unno = "";
                                            result += "no~records" + "~~~";
                                            result += "no~records" + "~~~";
                                            result += "no~records" + "~~~";
                                            if (hzd != null && hzd.length() > 0 && "Y".equalsIgnoreCase(hzd)) {
                                                //  err_result += checkMandatory("Hazardous value is 'YES',UN No ", unno, i);
                                                hazerror += checkMandatoryCommon("UN No", unno, i);
                                            }
                                        }
                                    }

                                    if (a == 27) // Flash Point
                                    {
                                        if (cell != null) {
                                            flashPoint = "";
                                            cellvalue = getCellValue(cell);
                                            flashPoint = checkEmpty(cellvalue.trim());
                                            flashPoint = convertIntNumber(flashPoint);
                                            result += flashPoint + "~~~";
                                            if (hzd != null && hzd.length() > 0 && "Y".equalsIgnoreCase(hzd)) {
                                                //  err_result += checkMandatory("Hazardous value is 'YES',Flash Point ", flashPoint, i);
                                                hazerror += checkMandatoryCommon("Flash Point", flashPoint, i);
                                            }
                                            if (flashPoint != null && !"".equalsIgnoreCase(flashPoint) && !"no~records".equalsIgnoreCase(flashPoint)) {
                                                err_result += checkNumber("Flash Point ", flashPoint, i);
                                                err_result += checklength("Flash Point", flashPoint, "8");
                                            }
                                        } else {
                                            flashPoint = "";
                                            result += "no~records" + "~~~";
                                            if (hzd != null && hzd.length() > 0 && "Y".equalsIgnoreCase(hzd)) {
                                                // err_result += checkMandatory("Hazardous value is 'YES',Flash Point ", flashPoint, i);
                                                hazerror += checkMandatoryCommon("Flash Point", flashPoint, i);
                                            }
                                        }
                                    }

                                    if (a == 28) // Flash Point unit
                                    {
                                        if (cell != null) {
                                            flashUnit = "";
                                            cellvalue = getCellValue(cell);
                                            flashUnit = checkEmpty(cellvalue.trim());
                                            if (hzd != null && hzd.length() > 0 && "Y".equalsIgnoreCase(hzd)) {
                                                //  err_result += checkMandatory("Hazardous value is 'YES',Flash Point unit ", flashUnit, i);
                                                hazerror += checkMandatoryCommon("Flash Point unit", flashUnit, i);
                                            }
                                            if (flashUnit != null && !"".equalsIgnoreCase(flashUnit) && !"no~records".equalsIgnoreCase(flashUnit)) {
                                                flashUnit = getData(flashUnit);
                                            }
                                            result += flashUnit + "~~~";
                                        } else {
                                            flashUnit = "";
                                            result += "no~records" + "~~~";
                                            if (hzd != null && hzd.length() > 0 && "Y".equalsIgnoreCase(hzd)) {
                                                // err_result += checkMandatory("Hazardous value is 'YES',Flash Point unit ", flashUnit, i);
                                                hazerror += checkMandatoryCommon("Flash Point unit", flashUnit, i);
                                            }
                                        }
                                    }
                                    if (a == 29) // eqp Status
                                    {
                                        if (cell != null) {
                                            eqpStatus = "";
                                            cellvalue = getCellValue(cell);
                                            eqpStatus = checkEmpty(cellvalue.trim());
                                            if (eqpStatus != null && !"".equalsIgnoreCase(eqpStatus) && !"no~records".equalsIgnoreCase(eqpStatus)) {
                                                eqpStatus = getData(eqpStatus);
                                                err_result += checkMandatory("Eqp Status ", eqpStatus, i);
                                            }
                                            if (!"Y".equalsIgnoreCase(soc)) {
                                                eqpStatus = "F";
                                            }
                                            result += eqpStatus + "~~~";
                                        } else {
                                            eqpStatus = "";
                                            result += "no~records" + "~~~";
                                            err_result += checkMandatory("Eqp Status ", eqpStatus, i);
                                        }
                                    }
                                    if (a == 30) // Ventilation(CBM)
                                    {
                                        if (cell != null) {
                                            ventval = "";
                                            cellvalue = getCellValue(cell);
                                            ventval = checkEmpty(cellvalue.trim());
                                            ventval = removeHyphen(ventval);
                                            result += ventval + "~~~";

                                            if (ventval != null && !"".equalsIgnoreCase(ventval) && !"no~records".equalsIgnoreCase(ventval)) {
                                                err_result += validateHumidityDigit(Ventilationlabel, ventval);
                                                err_result += checklength(Ventilationlabel, ventval, "5");
                                            }
                                        }
                                    }
                                    if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {
                                        if (a == 31) // Humidity(%)
                                        {
                                            if (cell != null) {
                                                ventval = "";
                                                cellvalue = getCellValue(cell);
                                                ventval = checkEmpty(cellvalue.trim());
                                                ventval = removeHyphen(ventval);
                                                result += ventval + "~~~";

                                                if (ventval != null && !"".equalsIgnoreCase(ventval) && !"no~records".equalsIgnoreCase(ventval)) {
                                                    err_result += validateHumidityDigit("Humidity(%)", ventval);
                                                    err_result += checklength("Humidity(%)", ventval, "5");
                                                }
                                            }
                                        }

                                        if (a == 32) // Drains
                                        {
                                            if (cell != null) {
                                                ventval = "";
                                                cellvalue = getCellValue(cell);
                                                ventval = checkEmpty(cellvalue.trim());
                                                ventval = removeHyphen(ventval);
                                                ventval = ("Open".equalsIgnoreCase(checkNull(ventval)) ? "O" : ("Closed".equalsIgnoreCase(checkNull(ventval)) ? "C" : ""));
                                                result += ventval + "~~~";
                                            }
                                        }

                                        if (a == 33) // Co2
                                        {
                                            if (cell != null) {
                                                ventval = "";
                                                cellvalue = getCellValue(cell);
                                                ventval = checkEmpty(cellvalue.trim());
                                                ventval = removeHyphen(ventval);
                                                result += ventval + "~~~";

                                                if (ventval != null && !"".equalsIgnoreCase(ventval) && !"no~records".equalsIgnoreCase(ventval)) {
                                                    err_result += validateHumidityDigit("Co2", ventval);
                                                    err_result += checklength("Co2", ventval, "5");
                                                }
                                            }
                                        }

                                        if (a == 34) // O2
                                        {
                                            if (cell != null) {
                                                ventval = "";
                                                cellvalue = getCellValue(cell);
                                                ventval = checkEmpty(cellvalue.trim());
                                                ventval = removeHyphen(ventval);
                                                result += ventval + "~~~";

                                                if (ventval != null && !"".equalsIgnoreCase(ventval) && !"no~records".equalsIgnoreCase(ventval)) {
                                                    err_result += validateHumidityDigit("O2", ventval);
                                                    err_result += checklength("O2", ventval, "5");
                                                }
                                            }
                                        }

                                    } else if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {
                                        if (a == 31) // OOG
                                        {
                                            if (cell != null) {
                                                ventval = "";
                                                cellvalue = getCellValue(cell);
                                                ventval = checkEmpty(cellvalue.trim());
                                                if (ventval != null && !"".equalsIgnoreCase(ventval) && !"no~records".equalsIgnoreCase(ventval)) {
                                                    ventval = getData(ventval);
                                                }
                                                result += ventval + "~~~";
                                                err_result += checkMandatory("OOG", ventval, i);
                                            } else {
                                                ventval = "";
                                                result += "no~records" + "~~~";
                                                err_result += checkMandatory("OOG", ventval, i);
                                            }

                                        }
                                    }
                                }
                                finalresult += result + "~~~~";

                                if (openerror != null && openerror.length() > 0) {
                                    if (openerror != null && openerror.endsWith(",")) {
                                        openerror = openerror.substring(0, openerror.lastIndexOf(","));
                                    }
                                    openerr = "Equipment is 'Open'-" + openerror + " is mandatory." + " ";
                                    err_result += openerr;
                                }
                                if (refererror != null && refererror.length() > 0) {
                                    if (refererror != null && refererror.endsWith(",")) {
                                        refererror = refererror.substring(0, refererror.lastIndexOf(","));
                                    }
                                    refererr = "Equipment is 'Reefer'-" + refererror + " is mandatory." + " ";
                                    err_result += refererr;

                                }
                                if (hazerror != null && hazerror.length() > 0) {
                                    if (hazerror != null && hazerror.endsWith(",")) {
                                        hazerror = hazerror.substring(0, hazerror.lastIndexOf(","));
                                    }
                                    hazerr = "Hazardous value is 'YES'-" + hazerror + " is mandatory." + " ";
                                    err_result += hazerr;

                                }
                                if (err_result != null && err_result.length() > 0) {
                                    errors = "";
                                    errors = errors + "<tr><td>" + (i + 1) + "</td>";
                                    errors = errors + "<td>" + err_result + "</td></tr>";
                                    finerr_result += errors;
                                }
                                //
                                if (message != null && message.length() > 0) {
                                    msg = "";
                                    msg = msg + "<tr><td>" + (i + 1) + "</td>";
                                    msg = msg + "<td>" + message + "</td></tr>";
                                    final_message += msg;
                                }
                                //
                                message = "";
                                result = "";
                                err_result = "";
                                openerror = "";
                                openerr = "";
                                refererror = "";
                                refererr = "";
                                hazerror = "";
                                hazerr = "";
                            }
                        }

                    }

                    if (fis != null) {
                        fis.close();
                    }
                    if (fs != null) {
                        fs = null;
                    }
                    if (wb != null) {
                        wb = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } //

        try {
            File uploadedFile = new File(attachPath + fname);
            uploadedFile.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

// return output
System.out.println("[ECOM-WAR] ExcelImportBooking   exceltitle  -- " + exceltitle+" exceltitle1  -- " + exceltitle1);
        if (exceltitle != null && exceltitle1 != null) {
            if (!exceltitle.equals(exceltitle1)) {
                invalidformat = "true";
                out.println(invalidformat);
            } else {
                if (finerr_result != null && finerr_result.trim().length() > 0) {
                    finerr_output = finerr_output + finerr_result + "</table></div>";
                    finalresult = finalresult + "~~~~~~~~~~" + finerr_output;
                }

                if (conlen != 0) {
                    //      System.out.println("[ECOM-WAR] ExcelImportBooking   lenngth::::::" + conlen);
                    finalresult = finalresult + "~*~~~~~~~~~~~*~" + conlen;
                }
                if (final_message != null && final_message.length() > 0) {
                    final_msg = final_msg + final_message + "</table></div>";
                    //  System.out.println("[ECOM-WAR] ExcelImportBooking   final message output::::" + final_msg);
                    finalresult = finalresult + "**~~~~~~~~~~~**" + final_msg;
                }
                //
                out.println(finalresult);
            }
        } else {
            invalidformat = "true";
            out.println(invalidformat);
        }
    }

    private String getCellValue(HSSFCell cell) {
        DecimalFormat dc = new DecimalFormat("##############0");
        int celltype = cell.getCellType();
        String value = "";
        switch (celltype) {
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                //  value = String.valueOf(dc.format(cell.getNumericCellValue()));
                // System.out.println("[ECOM-WAR] ExcelImportBooking   yesssss value:::::::" + String.valueOf(cell.getNumericCellValue()));
                try {
                    value = String.valueOf(cell.getNumericCellValue());
                    value = String.valueOf(new BigDecimal(value).toPlainString());
                    //  System.out.println("[ECOM-WAR] ExcelImportBooking   Answer:::::::::::"+value);
                } catch (Exception ex) {
                    value = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                try {
                    value = String.valueOf(cell.getStringCellValue());
                } catch (Exception ex) {
                    value = String.valueOf(dc.format(cell.getNumericCellValue()));
                }
                break;
            case HSSFCell.CELL_TYPE_STRING:
                value = String.valueOf(cell.getStringCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                value = "";
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                value = String.valueOf(cell.getErrorCellValue());
                break;
        }
        return value;
    }

    public String checkEmpty(String str) throws Exception {

        String result = "";
        try {
            //  System.out.println("[ECOM-WAR] ExcelImportBooking   strempty::::::::::::::::::::::::::::::::::::::::::::::::::::::::"+str);
            result = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "no~records";
            // System.out.println("[ECOM-WAR] ExcelImportBooking   result:::::::::::::::::::::::::::::::::::::::::::::::::::::::"+result);
        } catch (Exception e) {
        }
        return result;
    }

    public String checkMandatory(String str, String strvalue, int row) throws Exception {
        String result = "";
        try {
            if (strvalue == null || "".equalsIgnoreCase(strvalue) || "no~records".equalsIgnoreCase(strvalue) || "null".equalsIgnoreCase(strvalue)) {
                // result+= str + (row + 1) + " is mandatory.";
                result = str + " is mandatory." + " ";
            }
        } catch (Exception e) {
        }
        return result;
    }

    public String checkMandatoryCommon(String str, String strvalue, int row) throws Exception {
        String result = "";
        try {
            if (strvalue == null || "".equalsIgnoreCase(strvalue) || "no~records".equalsIgnoreCase(strvalue) || "null".equalsIgnoreCase(strvalue)) {
                result = str + ",";
            }
        } catch (Exception e) {
        }
        return result;
    }

    public String eqpchange(String str, String strvalue, int row) throws Exception {
        String result = "";
        try {
            if (strvalue != null || strvalue.length() > 0 || !"no~records".equalsIgnoreCase(strvalue) || !"null".equalsIgnoreCase(strvalue)) {
                // result+= str + (row + 1) + " is mandatory.";
                result = "Eqp Size/Tare Weight/Gross Weight values are changed based on Excel Container number." + " ";
            }
        } catch (Exception e) {
        }
        return result;
    }

    public String checkNull(String str) throws Exception {
        String result = "";
        try {
            result = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
        }
        return result;
    }

    private String checkInteger(String str, String strvalue, int row) throws Exception {
        String result = "";
        if (strvalue != null && (strvalue.matches("\\d+(\\.\\d+)?")) == false) {
            //  result += str + " " + (row + 1) + " is not valid Integer.";
            result += str + " is not valid number." + " ";
        }
        if ("0".equalsIgnoreCase(strvalue) || "0.0".equalsIgnoreCase(strvalue) || "0.00".equalsIgnoreCase(strvalue)) {
            result += str + " should not be zero." + " ";
        }
        return result;
    }

    private String checkNumberOnly(String str, String strvalue, int row) throws Exception {
        String result = "";
        if (strvalue != null && (Pattern.matches("\\d+", (CharSequence) strvalue)) == false) {
            //  result += str + " " + (row + 1) + " is not valid Integer.";
            result += str + " is not valid number." + " ";
        }
        if ("0".equalsIgnoreCase(strvalue) || "0.0".equalsIgnoreCase(strvalue)) {
            result += str + " should not be zero." + " ";
        }
        return result;
    }

    private String checkNumber(String str, String strvalue, int row) throws Exception {
        String result = "";
        if (strvalue != null && (strvalue.matches("-?\\d+(\\.\\d+)?")) == false) {
            result += str + " is not valid number." + " ";
        }
        return result;
    }

    private String checkCommodity(String str, String strvalue, int row) throws Exception {
        String result = "";
        if (strvalue != null && !"".equalsIgnoreCase(strvalue) && !"no~records".equalsIgnoreCase(strvalue)) {
            if (!commoditylist.contains(strvalue.toUpperCase().trim())) {
                result += str + " is not valid." + " ";
            }
        }
        return result;
    }

    private String checkPackage(String str, String strvalue, int row) throws Exception {
        String result = "";
        if (!packagelist.contains(strvalue.toUpperCase().trim())) {
            result += str + " is not valid." + " ";
        }
        return result;
    }

    private String getData(String str) {
        String result = "";
        if (str != null && str.length() > 0) {
            result = (String) hm.get(str);
        }
        if (result == null || result.length() == 0) {
            result = str;
        }
        return result;
    }

    private String checklength(String str, String strvalue, String length) throws Exception {
        String result = "";
        String len = (String) conainterlen.get(str.trim());
        int lenth = 0;
        int intlen = 0;
        if (strvalue != null && !"".equalsIgnoreCase(strvalue) && !"no~records".equalsIgnoreCase(strvalue)) {
            lenth = strvalue.trim().length();
            intlen = Integer.parseInt(len);
        }
        if (intlen < lenth) {
            result += str + " length is exceed (Max Length:" + length + ")." + " ";
        }
        return result;
    }

    private String checkAlphaNumric(String str, String strvalue, int row) throws Exception {
        String result = "";
        if (strvalue.matches("[A-Za-z0-9]+") == false) {
            result += str + " is allowed only alphanumeric." + " ";
        }
        return result;
    }

    private String checkreferTemp(String str, String mintemp, String maxtemp) throws Exception {
        String result = "";
        try {
            DecimalFormat twoPlaces = new DecimalFormat("0.00");
            String min = mintemp;
            String max = maxtemp;
            double minval = Double.parseDouble(min);
            double maxval = Double.parseDouble(max);
            double minresult = Math.max(minval, maxval);
            double maxresult = Math.min(minval, maxval);
            if ("Min Temp".equalsIgnoreCase(str.trim())) {
                if (minval == maxval) {
                    result += " Min Temp should be less than max temp." + " ";
                } else if (minval == minresult) {
                    result += " Min Temp should be less than max temp." + " ";
                }
            } else if ("Max Temp".equalsIgnoreCase(str.trim())) {
                if (minval == maxval) {
                    result += " Max Temp should be greater than min temp." + " ";
                } else if (maxval == maxresult) {
                    result += " Max Temp should be greater than min temp." + " ";
                }
            }
        } catch (Exception ex) {
            result += "Min Temp / Max Temp is not valid number." + " ";
        }
        return result;
    }

    private String checkPresetTemp(String str, String mintemp, String maxtemp, String presetTemp) throws Exception {
        String result = "";
        try {
            //  System.out.println("[ECOM-WAR] ExcelImportBooking   mintemp:::"+mintemp+"::maxtem::::"+maxtemp+"::preset:::"+presetTemp);
            double minval = Double.parseDouble(mintemp);
            double maxval = Double.parseDouble(maxtemp);
            double inbet = Double.parseDouble(presetTemp);

            if (inbet == minval) {
                result += " Preset Temp should be between min and max temp." + " ";
            } else if (inbet == maxval) {
                result += " Preset Temp should be between min and max temp." + " ";
            } else if (minval > inbet) {
                result += " Preset Temp should be between min and max temp." + " ";
            } else if (maxval < inbet) {
                result += " Preset Temp should be between min and max temp." + " ";
            }
            //  System.out.println("[ECOM-WAR] ExcelImportBooking   result:::"+result);
        } catch (Exception ex) {
            System.out.println("[ECOM-WAR] ExcelImportBooking   Error::::::" + ex.getMessage());
            result += "Preset Temp /Min Temp / Max Temp is not valid number." + " ";
        }
        return result;
    }

    private String checkEqp(String str, String strvalue, int row) throws Exception {
        String result = "";
        String answer = null;
        boolean flag = false;
        String returnval = "";
        for (Object obj : EQPHASHMAP.keySet()) {
            result = (String) obj;
            answer = (String) EQPHASHMAP.get(result);
            if (answer != null && answer.equalsIgnoreCase(strvalue)) {
                flag = true;
            }
        }
        if (flag == false) {
            returnval += str + " is not valid." + " ";
        }
        return returnval;
    }

    private String getKeyFromValue(String str, String strvalue, int row) {
        String result = null, answer = null;
        if (strvalue == null || strvalue.length() == 0 || "no~records".equalsIgnoreCase(strvalue)) {
            answer = "no~records";
        } else {
            for (Object obj : EQPHASHMAP.keySet()) {
                result = (String) obj;
                answer = (String) EQPHASHMAP.get(result);
                if (answer != null && answer.equalsIgnoreCase(strvalue)) {
                    return result;
                }
            }
            if (answer == null) {
                answer = "no~records";
            }
        }
        return answer;
    }

    private String checkTons(String weightunit, String tareweight, String cargoweight, int row) throws Exception {
        String result = "";
        try {
            double tarewt = Double.parseDouble(tareweight);
            double cargowt = Double.parseDouble(cargoweight);

            if (tareweight != null && !"".equalsIgnoreCase(tareweight) && !"no~records".equalsIgnoreCase(tareweight)) {
                if ("T".equalsIgnoreCase(weightunit)) {
                    tarewt = tarewt / 1000;
                    double grossweight = tarewt + cargowt;
                    if (grossweight > 50) {
                        result += "Gross weight should be less than or equal to 50 Tons" + " ";
                    }
                } else {
                    double grossweight = tarewt + cargowt;
                    if (grossweight > 50000) {
                        result += "Gross weight should be less than or equal to 50,000 Kilos" + " ";
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("[ECOM-WAR] ExcelImportBooking   Errors:::::::::::" + ex.getMessage());
            result = "";
        }
        return result;
    }

    private String checkhscode(String str, String strvalue, int row) throws Exception {
        String result = "";
        System.out.println("[ECOM-WAR] ExcelImportBooking   >>>size>> " + hscodelist.size() + " ::strvalue::::" + strvalue + "----");
        if (!hscodelist.contains(strvalue.toUpperCase().trim())) {
            result += str + " is not valid." + " ";
        }
        System.out.println("[ECOM-WAR] ExcelImportBooking   >>result>hs code valildation>" + result);
        return result;
    }

    private String checkHscodeCommodity(String str, String strvalue, String commoditycode, int row) throws Exception {
        String result = "";
        System.out.println("[ECOM-WAR] ExcelImportBooking   >>>commoditycode>>>" + commoditycode+ " checkHscodeCommodity::::" + strvalue);
        String groupcode = (String) CommodityHSCode.get(strvalue.toUpperCase());
        System.out.println("[ECOM-WAR] ExcelImportBooking   groupcode:::::" + groupcode + " :commoditycode:: " + commoditycode);
        if (groupcode == null) {
//            System.out.println("[ECOM-WAR] ExcelImportBooking   111111>>>");
            result += str + " is not valid." + " ";
        } else if (commoditycode == null) {
//            System.out.println("[ECOM-WAR] ExcelImportBooking   22222>>>");
            result += str + " is not valid." + " ";
        } else if (!groupcode.equalsIgnoreCase(commoditycode)) {
//            System.out.println("[ECOM-WAR] ExcelImportBooking   >>>33333");
            result += str + " is not valid." + " ";
        }
        ///   System.out.println("[ECOM-WAR] ExcelImportBooking   groupcode:::::"+groupcode+"::::commoditycode:::"+commoditycode);
        return result;
    }

    private String getCommodity(String strvalue) throws Exception {
        //   System.out.println("[ECOM-WAR] ExcelImportBooking   commodityMap::::::"+COMMODITYMAP+":::::::::"+strvalue);
        String result = "";
        String s = (String) COMMODITYMAP.get(strvalue);
        if (s != null) {
            result = s;
        } else {
            result = strvalue;
        }
        return result;
    }

    private String convertIntNumber(String str) {
        String result = "";
        //  System.out.println("[ECOM-WAR] ExcelImportBooking   yes::::::"+str);
        try {
            if (str != null && !"".equalsIgnoreCase(str) && !"no~records".equalsIgnoreCase(str) && str.endsWith(".0")) {
                // result = str.substring(0, str.indexOf("."));
                result = str.replace(".0", "");
            } else {
                result = str;
            }
            //  System.out.println("[ECOM-WAR] ExcelImportBooking   result answer::::::::::::"+result);
        } catch (Exception ex) {
            ex.printStackTrace();
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

    private static String validateHumidityDigit(String str, String val) {
        String result = "";
        if (!Pattern.matches("^[0-9.-]*$", val)) {
            result += str + " is not valid number." + " ";
        }
        return result;
    }

    private static String removeHyphen(String val) {
        if (val.startsWith("-")) {
            val = val.substring(1, val.length() - 1);
        }
        if (val.endsWith("-")) {
            val = val.substring(0, val.length() - 1);
        }
        return val;
    }

}
