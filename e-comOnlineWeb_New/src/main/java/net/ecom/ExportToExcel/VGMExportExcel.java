/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ExportToExcel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "VGMExportExcel", urlPatterns = {"/VGMExportExcel"})
public class VGMExportExcel extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String vgmjson = request.getParameter("vgmvalue");
            String vgmrefno = request.getParameter("vgmRefNo");
            String vgmcountstr = request.getParameter("vgmcount");
            int vgmcount = (vgmcountstr != null ? Integer.parseInt(vgmcountstr) : 0);
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            Map<String, String> vgmMap = gson.fromJson(vgmjson, type);
            HSSFWorkbook book = new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet("SOLAS - VGM");
            HSSFRow row;
            HSSFCell cell;

            HSSFFont Font = book.createFont();
            Font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            HSSFCellStyle cs = book.createCellStyle();
            Font.setFontHeight((short) 250);
            Font.setColor(HSSFColor.BLUE.index);
            Font.setFontName("Verdana");
            cs.setFont(Font);
            Font.setItalic(true);
            Font.setUnderline((byte) 2);
            cs.setFillForegroundColor(HSSFColor.YELLOW.index);
            cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cs.setAlignment(cs.ALIGN_CENTER);
            cs.setVerticalAlignment(cs.ALIGN_CENTER);

            //Future
            HSSFFont Font9 = book.createFont();
            Font9.setBoldweight(Font9.BOLDWEIGHT_BOLD);
            HSSFCellStyle Future = book.createCellStyle();
            Font9.setColor(HSSFColor.BLACK.index);
            Font9.setFontName("Verdana");
            Font9.setFontHeight((short) 160);
            Future.setBorderRight((short) 1);
            Future.setBorderLeft((short) 1);
            Future.setBorderBottom((short) 1);
            Future.setBorderTop((short) 1);
            Future.setFont(Font9);
            Future.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
            Future.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            HSSFFont Font2 = book.createFont();
            HSSFCellStyle valstyl = book.createCellStyle();
            Font2.setColor(HSSFColor.BLACK.index);
            Font2.setFontName("Verdana");
            Font2.setFontHeight((short) 160);
            valstyl.setBorderRight((short) 1);
            valstyl.setBorderLeft((short) 1);
            valstyl.setBorderBottom((short) 1);
            valstyl.setBorderTop((short) 1);
            valstyl.setFont(Font2);
            
            sheet.addMergedRegion(new CellRangeAddress(0,1,0,1));
            
            //Setting Title
            int rowindex = 0;
            row = sheet.createRow((int) rowindex);
            cell = row.createCell((int) 0);
            cell.setCellValue("SOLAS - VGM");
            cell.setCellStyle(cs);

            rowindex = rowindex + 3;

            row = sheet.createRow((int) rowindex);
            //Equipment Id.
            sheet.setColumnWidth((short) 0, (short) 5000);
            cell = row.createCell((int) 0);
            cell.setCellValue("Equipment Id.");
            cell.setCellStyle(Future);

            //VGM Weight
            sheet.setColumnWidth((short) 1, (short) 5000);
            cell = row.createCell((int) 1);
            cell.setCellValue("VGM Weight");
            cell.setCellStyle(Future);

            rowindex++;
            for (int i = 0; i < vgmcount; i++) {
                row = sheet.createRow((int) rowindex);
                //Equipment Id Value.
                cell = row.createCell((int) 0);
                cell.setCellValue(checkNull(vgmMap.get("containernoname"+i)));
                cell.setCellStyle(valstyl);

                //VGM Weight value
                cell = row.createCell((int) 1);
                cell.setCellValue(checkNull(vgmMap.get("vgmtxt"+i)));
                cell.setCellStyle(valstyl);
                
                rowindex++;
            }

            response.setContentType("application/vnd.ms-excel;Content-Transfer-Encoding:binary");
            response.setHeader("Content-Disposition", "attachment; filename=VGM-" + vgmrefno + ".xls");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                book.write(bos);
            } finally {
                bos.close();
            }
            response.getOutputStream().write(bos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
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

    private String checkNull(String str) {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
