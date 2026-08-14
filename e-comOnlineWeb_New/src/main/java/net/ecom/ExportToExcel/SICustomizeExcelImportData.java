/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomShippingInstructionBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import org.apache.commons.collections.map.HashedMap;
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
 * @author gobinath.t
 */
@WebServlet(name = "SICustomizeExcelImportData", urlPatterns = {"/SICustomizeExcelImportData"})
public class SICustomizeExcelImportData extends HttpServlet {

    List packagelist = null;
    Map hm = null;
    Map conainterlen = null;
    Map EQPHASHMAP = null;
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomShippingInstructionBeanRemote sibeanobj = EJBLookUp.getSIBeanRemoteLookUp();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String filepath = "";//request.getParameter("taskfin");
        String path = getServletContext().getRealPath("/");
        ServletContext contexts = getServletContext();
        int findFirst = 0;
        String result = "";
        String finalresult = "";
        String errors = "";
        String err_result = "";
        String finerr_result = "";
        String finerr_output = "";

        String conamounterror = "";
        String final_conamounterrorr = "";
        String cntno = "", totalamt = "", wunit = "", rowno = "", wunitrw = "";
        float floattotalamt = 0;
        String[] wunitrow = null;
        int rows = 0;
        final_conamounterrorr = "<div class=\"table-responsive\"><table class=\"table table-condensed table-striped table-bordered table-hover dataTable table-validation \" id=\"datatableexcel1\"><thead><th>Error Description</th></thead></tr>";

        finerr_output = "<div class=\"table-responsive\"><table class=\"table table-condensed table-striped table-bordered table-hover dataTable table-validation \" id=\"datatableexcel\"><thead><th>Excel Row Numbers</th><th>Error Description</th></thead></tr>";
        String containerno = "", carrierseal = "", shipperseal = "", eqpsize = "", sno = "";
        String qty = "", soc = "", cargoweight = "", weightunit = "";
        String pkg = "", marks = "", description = "";
        String eqpsizeallval = "";
        String commodityallval = "";
        ArrayList eqpsizelist = new ArrayList();
        int conlen = 0;
        String str_tarewt = "";
        float tareweight = 0;
        float cargowt = 0;
        float totaltare = 0;
        String invalidformat = "false";
        ArrayList conarrlenth = new ArrayList();
        HashMap maptareweight = new HashMap();
        HashMap mapcontnounit = new HashMap();
        hm = new HashMap();
        List exceltitle1 = null;
        String attachPath = "";
        String fname = "";
        HttpSession session = request.getSession();
        String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
        loginUser = loginUser + "1";
        ArrayList importexcellist = (ArrayList) session.getAttribute("SI_Import_Excel_Cargo_Details");
        List EBK_plgn = (List) contexts.getAttribute("EBK_PLUGIN_DETAILS");
        HashMap importexcelmap = new HashMap();
        //
        hm.put("Cubic Meter", "M");
        hm.put("Cubic Feet", "F");
        hm.put("Kilos", "K");
        hm.put("Pounds", "P");
        hm.put("Metric Tons", "T");
        hm.put("No", "N");
        hm.put("Yes", "y");
        //
        conainterlen = new HashMap<String, String>();
        conainterlen.put("Container No", "11");
        conainterlen.put("Carrier Seal", "12");
        conainterlen.put("Shipper Seal", "40");
        conainterlen.put("Marks", "5000");
        conainterlen.put("Description", "5000");
        conainterlen.put(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Quantity" : "No.of Packages"), "8");
        //conainterlen.put("Volume", "8");
        conainterlen.put("Cargo Weight", "9");
        //
        List exceltitle = new ArrayList();
        exceltitle.add("Sequence");
        exceltitle.add("Container No. *");
        exceltitle.add("Eqp Size");
        exceltitle.add(("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL)) ? "Carrier Seal" : "Carrier Seal *"));
        exceltitle.add(("YES".equalsIgnoreCase((String) session.getAttribute("SHIPPERSEALCONFIG")) ? "Shipper Seal *" : "Shipper Seal"));
        exceltitle.add(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Quantity *" : "No.of Packages *"));
        exceltitle.add(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Type *" : "Package *"));
        exceltitle.add("Cargo Weight *");
        exceltitle.add("Marks");
        //
        Hashtable packagemap = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
        EQPHASHMAP = ecomCntrlObj.getEqpments();
        // System.out.println("[ECOM-WAR] SICustomizeExcelImportData hscodeMAP::::::" + hscodeMAP);
        packagelist = new ArrayList();
        Iterator it = null;
        System.setProperty("file.encoding", "UTF-16");

        it = packagemap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            packagelist.add(pairs.getValue().toString().toUpperCase().trim() + " ~~ " + pairs.getKey().toString().toUpperCase().trim());
        }

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
            System.out.println("[ECOM-WAR] SICustomizeExcelImportData Exception in uploading");
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
            //System.out.println("[ECOM-WAR] SICustomizeExcelImportData fileformat......"+fileformat);
            if (fileformat.trim().equalsIgnoreCase(".xls")) {
                try {
                    FileInputStream fis = new FileInputStream(file1);
                    //System.out.println("[ECOM-WAR] SICustomizeExcelImportData welcomeeeeeee"+fis);
                    POIFSFileSystem fs = new POIFSFileSystem(fis);
                    //System.out.println("[ECOM-WAR] SICustomizeExcelImportData welcomeeeeeee"+fs);
                    wb = new HSSFWorkbook(fs);
                    sheet = wb.getSheetAt(0);
                    // Template Checking
                    int rowStart = 4; // 0 based not 1 based rows
                    int rowEnd = 5;
                    HSSFRow rw = null;
                    HSSFCell c = null;
                    int lastColumn = 9;
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
                                //  System.out.println("[ECOM-WAR] SICustomizeExcelImportData Excel:::" + cell);
                                if (cell != null) {
                                    cellvalue = getCellValue(cell);
                                    if (cellvalue.trim() != "" && cellvalue.trim().length() > 0) {
                                        System.out.println("[ECOM-WAR] SICustomizeExcelImportData cellvalue:::::::" + cellvalue);
                                        if (cellvalue.equalsIgnoreCase("Sequence")) {
                                            findFirst = row.getRowNum();
                                        }
                                        cct++;
                                    }
                                }
                            }
                        }
                    }

                    System.out.println("[ECOM-WAR] SICustomizeExcelImportData findFirst::::" + findFirst);

                    findFirst = findFirst + 1;
                    System.out.println("[ECOM-WAR] SICustomizeExcelImportData findFirst--" + findFirst);
                    int concount = 0;
                    String newsoc = null, newnor = null, neweqpsize = null, neweqptype = null, rateeqpid = null, oldeqpsize = null;
                    for (int i = findFirst; i <= lastrownum; i++) {
                        row = sheet.getRow(i);
                        newsoc = null;
                        newnor = null;
                        neweqpsize = null;
                        neweqptype = null;
                        rateeqpid = null;
                        oldeqpsize = null;
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
                                /* if(importexcellist!=null && concount<importexcellist.size()){
                                importexcelmap = (HashMap) importexcellist.get(concount);
                                } 
                                concount=concount+1;*/
                                for (a = row.getFirstCellNum(); a <= row.getLastCellNum(); a++) {
                                    cell = row.getCell((int) a);
                                    //      System.out.println("[ECOM-WAR] SICustomizeExcelImportData cell:::" + cell + "::::" + a);
                                    if (a == 0) // sno
                                    {
                                        //    System.out.println("[ECOM-WAR] SICustomizeExcelImportData cell:::::"+cell);
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            sno = checkEmpty(cellvalue.trim());
                                            result += sno + "~~~";
                                        }
                                    }

                                    if (a == 1) // contno
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            containerno = checkEmpty(cellvalue.trim());
                                            result += containerno + "~~~";
                                            System.out.println("[ECOM-WAR] SICustomizeExcelImportData containerno:::::::::" + containerno);
                                            err_result += checkMandatory("Container No", containerno, i);
                                            if (containerno != null && containerno.length() > 0 && !"no~records".equalsIgnoreCase(containerno)) {
                                                if (!conarrlenth.contains(containerno)) {
                                                    conarrlenth.add(containerno);
                                                    if (importexcellist != null && concount < importexcellist.size()) {
                                                        importexcelmap = (HashMap) importexcellist.get(concount);
                                                    }
                                                    concount = concount + 1;
                                                }
                                                err_result += checklength("Container No", containerno, "11");
                                                err_result += checkAlphaNumric("Container No", containerno, i);
                                            }
                                        }

                                        String eqp_sub_plgn = (EBK_plgn != null && EBK_plgn.contains("EES")) ? "Y" : "N";
                                        System.out.println("[ECOM-WAR] SICustomizeExcelImportData importexcelmap===>" + importexcelmap);
                                        String convalidresult = ecomCntrlObj.checkContainerAndEqpType(containerno, (String) importexcelmap.get("eqpsize"), (String) importexcelmap.get("nor"), (String) importexcelmap.get("type"), eqp_sub_plgn);
                                        System.out.println("[ECOM-WAR] SICustomizeExcelImportData convalidresult================> " + convalidresult);
                                        boolean checkcontno = false;
                                        if (convalidresult != null && convalidresult.length() > 0) {
                                            String resultarr[] = convalidresult.split("~~");
                                            String status, fleetgroup;
                                            System.out.println("[ECOM-WAR] SICustomizeExcelImportData resultarr==> " + resultarr.toString() + " resultarr LEN  " + resultarr.length);
                                            if (resultarr.length >= 2) {
                                                status = resultarr[0];
                                                fleetgroup = (resultarr.length >= 3) ? resultarr[2] : "";
                                                if ("Exists".equalsIgnoreCase(status)) {//(!"STAR CARE".equalsIgnoreCase(fleetgroup)) &&
                                                    newsoc = resultarr[1];
                                                    System.out.println("[ECOM-WAR] SICustomizeExcelImportData inside condition====> " + status);
                                                    checkcontno = true;
                                                    if (resultarr.length >= 5) {
                                                        neweqpsize = resultarr[3];
                                                        neweqptype = resultarr[4];
                                                        newnor = (resultarr.length >= 6) ? resultarr[5] : "";
                                                        oldeqpsize = (String) importexcelmap.get("eqpsize");
                                                    }
                                                }
                                            }
                                        }
                                        String eqpsize1 = (String) importexcelmap.get("eqpsize");
                                        //if ((!checkcontno) && (!"Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL)) || (!containerno.contains(eqpsize1 + "TNB")) && (!containerno.contains(eqpsize1 + "TBN")))) {
                                        if ((!checkcontno)) {
                                            err_result += "Container No  or Eqp Size" + " is not valid." + " ";
                                        }
                                        result += newsoc + "~~~";

                                    }

                                    if (a == 2) // Eqp Size
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            //System.out.println("[ECOM-WAR] SICustomizeExcelImportData cellvaluee"+cellvalue);
                                            eqpsize = checkEmpty(cellvalue.trim());
                                            if (neweqpsize != null && neweqpsize.length() > 0) {
                                                eqpsize = neweqpsize;
                                            }
                                            result += eqpsize + "~~~";
                                            if (eqpsize != null && eqpsize.length() > 0 && !"no~records".equalsIgnoreCase(eqpsize)) {
                                                err_result += checkEqp("Eqp Size ", cellvalue, cct); // soc, cellvalue, cct
                                                if (!eqpsizelist.contains(containerno)) {  //containerno eqpsize
                                                    eqpsizeallval += eqpsize + ",";
                                                }
                                                eqpsizelist.add(containerno);
                                                error = "";

                                            }
                                            if (containerno != null && !"no~records".equalsIgnoreCase(containerno) && eqpsize != null && !"no~records".equalsIgnoreCase(eqpsize)) {
                                                try {
                                                    str_tarewt = sibeanobj.getTareWtForCntr(containerno, eqpsize, "", soc);
                                                    tareweight = Float.parseFloat(str_tarewt);

                                                } catch (Exception ex) {
                                                    str_tarewt = "0";
                                                    tareweight = 0;
                                                }
                                                if (!maptareweight.containsKey(containerno)) {
                                                    System.out.println("[ECOM-WAR] SICustomizeExcelImportData welcome ifff::::::::::");
                                                    tareweight = tareweight + cargowt;
                                                    maptareweight.put(containerno, tareweight);
                                                    mapcontnounit.put(containerno, weightunit + "~~" + i);
                                                    System.out.println("[ECOM-WAR] SICustomizeExcelImportData maptareweight iffff::::::" + maptareweight);
                                                } else {
                                                    System.out.println("[ECOM-WAR] SICustomizeExcelImportData welcome else:::::::::::::::::::>>>>>>>>>");
                                                    totaltare = (float) maptareweight.get(containerno);
                                                    System.out.println("[ECOM-WAR] SICustomizeExcelImportData >>>>>total tare:::::" + totaltare);
                                                    tareweight = totaltare + cargowt;
                                                    maptareweight.put(containerno, tareweight);
                                                    System.out.println("[ECOM-WAR] SICustomizeExcelImportData maptareweight else::::::" + maptareweight);
                                                }
                                                System.out.println("[ECOM-WAR] SICustomizeExcelImportData containeno:::" + containerno + "tareweight::::::" + tareweight);
                                            }
                                        }
                                    }

                                    if (a == 3) // Carrier Seal;
                                    {
                                        System.out.println("[ECOM-WAR] SICustomizeExcelImportData Carrier Seal => " + cell);
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            carrierseal = checkEmpty(cellvalue.trim());
                                            if (!"Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL))) {
                                                err_result += checkMandatory("Carrier Seal", carrierseal, i);
                                            }
                                            result += carrierseal + "~~~";
                                            if (carrierseal != null && !"".equalsIgnoreCase(carrierseal) && !"no~records".equalsIgnoreCase(carrierseal)) {
                                                err_result += checklength("Carrier Seal", carrierseal, "17");
                                            }
                                        } else {
                                            result += checkEmpty(null) + "~~~";
                                        }
                                    }

                                    if (a == 4) // Shipper Seal;
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            shipperseal = checkEmpty(cellvalue.trim());
                                            result += shipperseal + "~~~";
                                            if ("YES".equalsIgnoreCase((String) session.getAttribute("SHIPPERSEALCONFIG"))) {
                                                err_result += checkMandatory("Shipper Seal", shipperseal, i);
                                            }
                                            if (shipperseal != null && !"".equalsIgnoreCase(shipperseal) && !"no~records".equalsIgnoreCase(shipperseal)) {
                                                err_result += checklength("Shipper Seal", shipperseal, "40");
                                            }
                                        }

                                    }

                                    if (a == 5) // Quantity
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            qty = checkEmpty(cellvalue.trim());
                                            qty = convertIntNumber(qty);
                                            result += qty + "~~~";
                                            err_result += checkMandatory(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Quantity" : "No.of Packages"), qty, i);
                                            if (qty != null && !"".equalsIgnoreCase(qty) && !"no~records".equalsIgnoreCase(qty)) {
                                                err_result += checkInteger(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Quantity" : "No.of Packages"), qty, i);
                                                err_result += checklength(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Quantity" : "No.of Packages"), qty, "8");
                                            }
                                        }
                                    }

                                    if (a == 6) // Package
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            pkg = checkEmpty(cellvalue.trim());
                                            result += pkg + "~~~";
                                            error = "";
                                            error = checkMandatory(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Type" : "Package Type"), pkg, i);
                                            err_result += error;
                                            if (error == null || error.length() == 0) {
                                                err_result += checkPackage(("GWF".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.CARRIER_CODE)) ? "Type" : "Package Type"), pkg, i);
                                            }
                                        }

                                    }

                                    if (a == 7) // Cargo Weight
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            cargoweight = checkEmpty(cellvalue.trim());
                                            result += cargoweight + "~~~";
                                            err_result += checkMandatory("Cargo Weight ", cargoweight, i);
                                            cargowt = 0;
                                            if (cargoweight != null && !"".equalsIgnoreCase(cargoweight) && !"no~records".equalsIgnoreCase(cargoweight)) {
                                                err_result += checkNumber("Cargo Weight ", cargoweight, i);
                                                err_result += checklength("Cargo Weight", cargoweight, "8");
                                                try {
                                                    cargowt = Float.parseFloat(cargoweight);
                                                } catch (Exception ex) {
                                                    cargowt = 0;
                                                }
                                            }

                                        }
                                    }

                                    if (a == 8) // marks
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            marks = checkEmpty(cellvalue.trim());
                                            result += marks + "~~~";
                                            if (marks != null && !"".equalsIgnoreCase(marks) && !"no~records".equalsIgnoreCase(marks)) {
                                                err_result += checklength("Marks", marks, "5000");
                                            }
                                        } else {
                                            result += checkEmpty(null) + "~~~";
                                        }
                                    }

                                    if (a == 9) // description
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            description = checkEmpty(cellvalue.trim());
                                            result += description + "~~~";
                                            if (description != null && !"".equalsIgnoreCase(description) && !"no~records".equalsIgnoreCase(description)) {
                                                err_result += checklength("Description", description, "5000");
                                            }
                                        } else {
                                            result += checkEmpty(null) + "~~~";
                                        }
                                    }
                                }
                                result += oldeqpsize + "~~~";
                                result += neweqptype + "~~~";
                                result += newnor;
                                finalresult += result + "~~~~";
                                if (err_result != null && err_result.length() > 0) {
                                    errors = "";
                                    errors = errors + "<tr><td>" + (i + 1) + "</td>";
                                    errors = errors + "<td>" + err_result + "</td></tr>";
                                    finerr_result += errors;
                                }
                                result = "";
                                err_result = "";
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
                    //System.out.println("[ECOM-WAR] SICustomizeExcelImportData welcomeeeeeee"+fs);
                    ///   wb = new HSSFWorkbook(fs);
                    //   sheet = wb.getSheetAt(0);

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
        try {
            System.out.println("[ECOM-WAR] SICustomizeExcelImportData welcome maptareweight::::::::::" + maptareweight);
            Iterator iterator = maptareweight.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                cntno = (String) pair.getKey();
                floattotalamt = (float) pair.getValue();
                totalamt = String.valueOf(floattotalamt);
                wunitrw = (String) mapcontnounit.get(cntno);
                wunitrow = wunitrw.split("~~");
                wunit = wunitrow[0];
                rowno = wunitrow[1];
                rows = Integer.parseInt(rowno);
                System.out.println("[ECOM-WAR] SICustomizeExcelImportData ::>>>>>wunit::>>>" + wunit + "::>>>>>totalamt:::>" + totalamt);
                conamounterror += checkTons(wunit, totalamt, containerno, rows);
                System.out.println("[ECOM-WAR] SICustomizeExcelImportData conamounterror::::" + conamounterror);
                iterator.remove();
            }
            if (conamounterror != null && conamounterror.length() > 0) {
                final_conamounterrorr = final_conamounterrorr + "<tr><td>" + conamounterror + "</td></tr>" + "</table></div>";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (exceltitle != null && exceltitle1 != null) {
            if (!exceltitle.equals(exceltitle1)) {
                invalidformat = "true";
                out.println(invalidformat);
            } else {
                System.out.println("[ECOM-WAR] SICustomizeExcelImportData :commodityallval:::>>>" + commodityallval);
                if (finerr_result != null && finerr_result.trim().length() > 0) {
                    finerr_output = finerr_output + finerr_result + "</table></div>";
                    finalresult = finalresult + "~~~~~~~~~~" + finerr_output;
                }
                if (conarrlenth != null) {
                    System.out.println("[ECOM-WAR] SICustomizeExcelImportData lenngth::::::" + conarrlenth);
                    conlen = conarrlenth.size();
                    finalresult = finalresult + "~*~~~~~~~~~~~*~" + conlen;
                }
                if (conamounterror != null && conamounterror.length() > 0) {
                    finalresult = finalresult + "~*~~~~~~~~~~~*~" + final_conamounterrorr;
                }
                if (eqpsizeallval != null && eqpsizeallval.length() > 0 && eqpsizeallval.endsWith(",")) {
                    eqpsizeallval = eqpsizeallval.substring(0, eqpsizeallval.length() - 1);
                    finalresult = finalresult + "**~~~~~~~~~~~**" + eqpsizeallval;
                }
                if (commodityallval != null && commodityallval.length() > 0 && commodityallval.endsWith(",")) {
                    commodityallval = commodityallval.substring(0, commodityallval.length() - 1);
                    finalresult = finalresult + "***~~~~~~~~~~~***" + commodityallval;
                }

                out.println(finalresult);
            }
        } else {
            invalidformat = "true";
            out.println(invalidformat);
        }
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

    private String getCellValue(HSSFCell cell) {
        DecimalFormat dc = new DecimalFormat("##############0");
        int celltype = cell.getCellType();
        String value = "";
        switch (celltype) {
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                try {
                    value = String.valueOf(cell.getNumericCellValue());
                    value = String.valueOf(new BigDecimal(value).toPlainString());
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
            //  System.out.println("[ECOM-WAR] SICustomizeExcelImportData strempty::::::"+str);
            result = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "no~records";
            // System.out.println("[ECOM-WAR] SICustomizeExcelImportData result::::"+result);
        } catch (Exception e) {
        }
        return result;
    }

    public String checkMandatory(String str, String strvalue, int row) throws Exception {
        String result = "";
        try {
            if (strvalue == null || "".equalsIgnoreCase(strvalue) || "no~records".equalsIgnoreCase(strvalue)) {
                // result+= str + (row + 1) + " is mandatory.";
                result = str + " is mandatory." + " ";
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
        if ((Pattern.matches("\\d+", (CharSequence) strvalue)) == false) {
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
        System.out.println("[ECOM-WAR] SICustomizeExcelImportData strvalue=====> strvalue => " + strvalue);
        if ((strvalue.matches("-?\\d+(\\.\\d+)?")) == false) {
            result += str + " is not valid number." + " ";
        }
        if ("0".equalsIgnoreCase(strvalue) || "0.0".equalsIgnoreCase(strvalue) && "Volume".equalsIgnoreCase(str.trim())) {
            result += str + " should not be zero." + " ";
        }
        return result;
    }

    private String checkPackage(String str, String strvalue, int row) throws Exception {
        String result = "";
        if (!packagelist.contains(strvalue.toUpperCase())) {
            result += str + " is not valid." + " ";
        }
        return result;
    }

    private String getData(String str) {
        String result = "";
        if (str != null && str.length() > 0) {
            //   System.out.println("[ECOM-WAR] SICustomizeExcelImportData HM:::::::"+str);
            result = (String) hm.get(str);
        }
        if (result == null || result.length() == 0) {
            result = str;
        }
        return result;
    }

    private String checklength(String str, String strvalue, String length) throws Exception {
        String result = "";
        String len = (String) conainterlen.get(str);
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

    private String checkEqp(String str, String strvalue, int row) throws Exception {
        String result = "";
        String s = (String) EQPHASHMAP.get(strvalue);
        if (s == null) {
            result += str + " is not valid." + " ";
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

    private String convertIntNumber(String str) {
        String result = "";
        //  System.out.println("[ECOM-WAR] SICustomizeExcelImportData yes::::::"+str);
        try {
            if (str != null && !"".equalsIgnoreCase(str) && !"no~records".equalsIgnoreCase(str) && str.contains(".")) {
                result = str.substring(0, str.indexOf("."));
                //result = str.replace(".0", "");
            } else {
                result = str;
            }
            //  System.out.println("[ECOM-WAR] SICustomizeExcelImportData result answer::::::::::::"+result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private String checkTons(String weightunit, String totalamt, String containerno, int row) throws Exception {
        String result = "";
        try {
            double totalwt = Double.parseDouble(totalamt);
            // wunit, totalamt,containerno,rows
            if ("T".equalsIgnoreCase(weightunit)) {
                if (totalwt > 50) {
                    result += containerno + " - Gross weight should be less than or equal to 50 Tons" + " ";
                }
            } else {
                if (totalwt > 50000) {
                    result += containerno + " - Gross weight should be less than or equal to 50,000 Kilos" + " ";
                }
            }

        } catch (Exception ex) {
            System.out.println("[ECOM-WAR] SICustomizeExcelImportData Errors:::::::::::" + ex.getMessage());
            result = "";
        }
        return result;
    }

}
