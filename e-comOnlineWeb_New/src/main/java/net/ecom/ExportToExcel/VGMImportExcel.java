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
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
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
@WebServlet(name = "VGMImportExcel", urlPatterns = {"/VGMImportExcel"})
public class VGMImportExcel extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    HashMap<String, String> conainterlen = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String attachPath = "", fname = "", filepath = "", result = "", finalresult = "", errors = "", err_result = "", finerr_result = "", finerr_output = "", containerno = "", vgmwgt = "";
        finerr_output = "<div class=\"table-responsive\"><table class=\"table table-condensed table-striped table-bordered table-hover dataTable table-validation \" id=\"datatableexcel\"><thead><th>Excel Row Numbers</th><th>Error Description</th></thead></tr>";
        List exceltitle = new ArrayList();
        exceltitle.add("Equipment Id.");
        exceltitle.add("VGM Weight");
        List exceltitle1 = null;
        String invalidformat = "false";
        ArrayList conarrlenth = new ArrayList();
        try {
            System.out.println("[ECOM-WAR] VGMImportExcel   inside VGMImportExcel");
            conainterlen = new HashMap<String, String>();
            conainterlen.put("Equipment Id", "11");
            conainterlen.put("VGM Weight", "9");
            HttpSession session = request.getSession();
            ServletContext contexts = getServletContext();
            File dirs = null;
            String loginUser = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
            //
            String exceluploadpath = null;
            String filefolder = null;
            exceluploadpath = (String) contexts.getAttribute(GeneralConf.exceluploadpath);//"C:\\Ecomm\\Ecomm";
            attachPath = contexts.getRealPath("/");
            filefolder = exceluploadpath.replace("/", "\\");
            attachPath = filefolder + "Template";
            //
            dirs = new File(attachPath);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            attachPath = exceluploadpath + "Template/";

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
            System.out.println("[ECOM-WAR] VGMImportExcel   Exception in uploading");
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

            if (fileformat.trim().equalsIgnoreCase(".xls")) {
                try {
                    FileInputStream fis = new FileInputStream(file1);
                    //System.out.println("[ECOM-WAR] VGMImportExcel   welcomeeeeeee"+fis);
                    POIFSFileSystem fs = new POIFSFileSystem(fis);
                    //System.out.println("[ECOM-WAR] VGMImportExcel   welcomeeeeeee"+fs);
                    wb = new HSSFWorkbook(fs);
                    sheet = wb.getSheetAt(0);
                    // Template Checking
                    int rowStart = 3; // 0 based not 1 based rows
                    int rowEnd = 4;
                    HSSFRow rw = null;
                    HSSFCell c = null;
                    int lastColumn = 2;
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

                    int findFirst = 4;
                    System.out.println("[ECOM-WAR] VGMImportExcel   findFirst  " + findFirst);
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
                                    if (a == 0) // containerno
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            containerno = checkEmpty(cellvalue.trim());
                                            System.out.println("[ECOM-WAR] VGMImportExcel   containerno:::::::::" + containerno);
                                            err_result += checkMandatory("Container No", containerno, i);
                                            result += containerno + "~~~";
                                            if (containerno != null && containerno.length() > 0 && !"no~records".equalsIgnoreCase(containerno)) {
                                                if (!conarrlenth.contains(containerno)) {
                                                    conarrlenth.add(containerno);
                                                }
                                                err_result += checklength("Equipment Id", containerno, "11");
                                            }
                                        }
                                    }

                                    if (a == 1) // VGM weight
                                    {
                                        if (cell != null) {
                                            cellvalue = getCellValue(cell);
                                            vgmwgt = checkEmpty(cellvalue.trim());
                                            result += vgmwgt + "~~~";
                                            System.out.println("[ECOM-WAR] VGMImportExcel   VGM Weight:::::::::" + vgmwgt);
                                            err_result += checkMandatory("VGM Weight", vgmwgt, i);
                                            if (vgmwgt != null && vgmwgt.length() > 0 && !"no~records".equalsIgnoreCase(vgmwgt)) {
                                                err_result += checklength("VGM Weight", vgmwgt, "9");
                                                err_result += checkNumber("VGM Weight", vgmwgt, i);
                                                err_result += checkTons("K", vgmwgt,containerno, i);
                                            }
                                        }

                                    }

                                }
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        try {
            File uploadedFile = new File(attachPath + fname);
            uploadedFile.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (exceltitle != null && exceltitle1 != null) {
            if (!exceltitle.equals(exceltitle1)) {
                invalidformat = "true";
                out.println(invalidformat);
            } else {
                if (finerr_result != null && finerr_result.trim().length() > 0) {
                    finerr_output = finerr_output + finerr_result + "</table></div>";
                    finalresult = finalresult + "~~~~~~~~~~" + finerr_output;
                }
                if (conarrlenth != null) {
                    System.out.println("[ECOM-WAR] VGMImportExcel   lenngth::::::" + conarrlenth);
                    int conlen = conarrlenth.size();
                    finalresult = finalresult + "~*~~~~~~~~~~~*~" + conlen;
                }

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
            //  System.out.println("[ECOM-WAR] VGMImportExcel   strempty:::::::"+str);
            result = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "no~records";
            // System.out.println("[ECOM-WAR] VGMImportExcel   result::"+result);
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

    private String checkNumber(String str, String strvalue, int row) throws Exception {
        String result = "";
        System.out.println("[ECOM-WAR] VGMImportExcel   strvalue=====> strvalue => " + strvalue);
        if ((strvalue.matches("-?\\d+(\\.\\d+)?")) == false) {
            result += str + " is not valid number." + " ";
        }
        if ("0".equalsIgnoreCase(strvalue) || "0.0".equalsIgnoreCase(strvalue)) {
            result += str + " should not be zero." + " ";
        }
        return result;
    }

    private String checklength(String str, String strvalue, String length) throws Exception {
        String result = "";
        int lenth = 0;
        String len = (String) conainterlen.get(str);
        int intlen = 0;
        if (strvalue != null && !"".equalsIgnoreCase(strvalue) && !"no~records".equalsIgnoreCase(strvalue)) {
            lenth = strvalue.trim().length();
            intlen = Integer.parseInt(len);
        }
        if (intlen < lenth) {
            result += str + " length is exceed (Max Length:" + intlen + ")." + " ";
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
                    result += containerno + " - VGM weight should be less than or equal to 50 Tons" + " ";
                }
            } else {
                if (totalwt > 50000) {
                    result += containerno + " - VGM weight should be less than or equal to 50,000 Kilos" + " ";
                }
            }

        } catch (Exception ex) {
            System.out.println("[ECOM-WAR] VGMImportExcel   Errors:::::::::::" + ex.getMessage());
            result = "";
        }
        return result;
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
