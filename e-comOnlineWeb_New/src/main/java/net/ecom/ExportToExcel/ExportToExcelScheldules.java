/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ExportToExcel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.ecom.vo.RegisterMenuVO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.hssf.util.Region;

/**
 *
 * @author sasikumar.r
 */
public class ExportToExcelScheldules {

    public ByteArrayOutputStream ExcelCration(ArrayList<String> heading, ArrayList<Integer> columSize, ArrayList schlist, String atport, String fromdate, String todate, String strtype, String bwlocpol, String bwlocpod, String client_code, List<RegisterMenuVO> menulist) {
        ByteArrayOutputStream bos=null;
        try {
            int nRow = 11;
            int nCol = 11;
            //
            HSSFWorkbook book = new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet("Schedules Result");
            HSSFRow row;
            HSSFCell cell;
            // CREATE STYLE
            HSSFPrintSetup printset = sheet.getPrintSetup();
            printset.setLandscape(true);
            printset.setPaperSize(printset.A4_PAPERSIZE);

            sheet.getFooter().setRight("Page No : " + sheet.getFooter().page());

            HSSFFont Font = book.createFont();
            Font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            HSSFCellStyle cs = book.createCellStyle();
            Font.setFontHeight((short) 250);
            Font.setColor(HSSFColor.RED.index);
            Font.setFontName("Verdana");
            cs.setFont(Font);
            Font.setItalic(true);
            Font.setUnderline((byte) 2);
            cs.setFillForegroundColor(HSSFColor.WHITE.index);
            cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cs.setAlignment(cs.ALIGN_CENTER);
            cs.setVerticalAlignment(cs.ALIGN_CENTER);

            //Blue
            HSSFFont Font1 = book.createFont();
            Font1.setBoldweight(Font1.BOLDWEIGHT_BOLD);
            HSSFCellStyle Blue = book.createCellStyle();
            Font1.setColor(HSSFColor.BLACK.index);
            Font1.setFontName("Verdana");
            Font1.setFontHeight((short) 160);
            Blue.setFont(Font1);
            Blue.setAlignment(Blue.ALIGN_CENTER);
            Blue.setVerticalAlignment(Blue.VERTICAL_CENTER);
            Blue.setBorderBottom((short) 1);
            Blue.setBorderTop((short) 1);
            Blue.setBorderLeft((short) 1);
            Blue.setBorderRight((short) 1);

            //Red
            HSSFFont Font4 = book.createFont();
            HSSFCellStyle Red = book.createCellStyle();
            Font4.setColor(HSSFColor.BLUE.index);
            Font4.setFontName("Verdana");
            Font4.setFontHeight((short) 160);
            Red.setFont(Font4);
            Red.setAlignment(Red.ALIGN_RIGHT);

            //
            HSSFFont Font5 = book.createFont();
            HSSFCellStyle RedLft = book.createCellStyle();
            Font5.setColor(HSSFColor.RED.index);
            Font5.setFontName("Verdana");
            Font5.setFontHeight((short) 160);
            RedLft.setFont(Font5);
            RedLft.setAlignment(RedLft.ALIGN_LEFT);

            HSSFFont Font6 = book.createFont();
            Font6.setBoldweight(Font1.BOLDWEIGHT_BOLD);
            Font6.setUnderline((byte) 1);
            HSSFCellStyle cr = book.createCellStyle();
            Font6.setColor(HSSFColor.BLUE.index);
            Font6.setFontName("Verdana");
            Font6.setFontHeight((short) 160);
            cr.setFont(Font6);
            cr.setAlignment(cr.ALIGN_CENTER);

            //Sailed
            HSSFFont Font3 = book.createFont();
            HSSFCellStyle Sailed = book.createCellStyle();
            Font3.setColor(HSSFColor.BLACK.index);
            Font3.setFontName("Verdana");
            Font3.setFontHeight((short) 160);
            Sailed.setBorderLeft((short) 1);
            Sailed.setBorderRight((short) 1);
            Sailed.setBorderBottom((short) 1);
            Sailed.setBorderTop((short) 1);
            Sailed.setFont(Font3);
            Sailed.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
            Sailed.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            //Future
            HSSFFont Font9 = book.createFont();
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

            //Yellow
            HSSFFont Font7 = book.createFont();
            HSSFCellStyle Yellow = book.createCellStyle();
            Font7.setColor(HSSFColor.BLACK.index);
            Font7.setFontName("Verdana");
            Font7.setFontHeight((short) 160);
            Yellow.setBorderLeft((short) 1);
            Yellow.setBorderRight((short) 1);
            Yellow.setBorderBottom((short) 1);
            Yellow.setBorderTop((short) 1);
            Yellow.setFont(Font7);
            Yellow.setFillForegroundColor(HSSFColor.YELLOW.index);
            Yellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            //Green
            HSSFFont Font2 = book.createFont();
            HSSFCellStyle Green = book.createCellStyle();
            Font2.setColor(HSSFColor.BLACK.index);
            Font2.setFontName("Verdana");
            Font2.setFontHeight((short) 160);
            Green.setBorderRight((short) 1);
            Green.setBorderLeft((short) 1);
            Green.setBorderBottom((short) 1);
            Green.setBorderTop((short) 1);
            Green.setFont(Font2);
            Green.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
            Green.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            // Delete
            HSSFFont Font10 = book.createFont();
            HSSFCellStyle Delete = book.createCellStyle();
            Font10.setColor(HSSFColor.BLACK.index);
            Font10.setFontName("Verdana");
            Font10.setFontHeight((short) 160);
            Delete.setBorderRight((short) 1);
            Delete.setBorderLeft((short) 1);
            Delete.setBorderBottom((short) 1);
            Delete.setBorderTop((short) 1);
            Delete.setFont(Font10);
            Delete.setFillForegroundColor(HSSFColor.RED.index);
            Delete.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            sheet.setDisplayGridlines(false);
            //Setting Title
            int rowindex = 0;
            row = sheet.createRow((int) rowindex);
            cell = row.createCell((int) 3);
            cell.setCellValue(client_code + "-Schedule Viewer");
            cell.setCellStyle(cs);
            //   sheet.addMergedRegion(new Region(rowindex, (short) 0, rowindex, (short) (4)));
            rowindex++;
            rowindex++;
            int k = 0;
            String Type = "";
            if ("Location".equalsIgnoreCase(strtype)) {
                //Type = "Between Location";
                Type = menulist.stream().filter(o -> "BLS".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getMenuName).reduce("", String::concat);
            } else if ("Vessel".equalsIgnoreCase(strtype)) {
                //Type = "By Vessel";
                Type = menulist.stream().filter(o -> "BVS".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getMenuName).reduce("", String::concat);
            } else if ("Port".equalsIgnoreCase(strtype)) {
                //Type = "By Port";
                Type = menulist.stream().filter(o -> "BPS".equalsIgnoreCase(o.getRefNo())).map(RegisterMenuVO::getMenuName).reduce("", String::concat);
            }
            row = sheet.createRow((int) rowindex);
            cell = row.createCell((int) 0);
            cell.setCellValue(Type);
            cell.setCellStyle(cr);
            //   sheet.addMergedRegion(new Region(rowindex, (short) 0, rowindex, (short) (1)));
            String FromDate = fromdate;
            String ToDate = todate;
            if (FromDate != null && !FromDate.equalsIgnoreCase("") && FromDate.length() != 0) {
                cell = row.createCell((int) 2);
                cell.setCellValue(("1Location".equalsIgnoreCase(strtype)) ? "Shipment Date" : "From Date :");
                cell.setCellStyle(Red);
                //       sheet.addMergedRegion(new Region(rowindex, (short) 2, rowindex, (short) (3)));
                cell = row.createCell((int) 4);
                //cell.setCellValue(txtFmDate.getText().trim());
                cell.setCellValue(FromDate);
                cell.setCellStyle(RedLft);
                //       sheet.addMergedRegion(new Region(rowindex, (short) 4, rowindex, (short) (5)));

                if (!("1Location".equalsIgnoreCase(strtype))) {
                    //  row = sheet.createRow((int) rowindex);
                    cell = row.createCell((int) 6);
                    cell.setCellValue("To Date :");
                    cell.setCellStyle(Red);

                    cell = row.createCell((int) 7);
                    //cell.setCellValue(txtToDate.getText().trim());
                    cell.setCellValue(ToDate);
                    cell.setCellStyle(RedLft);
                    //       sheet.addMergedRegion(new Region(rowindex, (short) 7, rowindex, (short) (8)));
                }
            }

            String FromPort = atport;

            if ("Port".equalsIgnoreCase(strtype)) { //by Port
                rowindex++;//Service
                row = sheet.createRow((int) rowindex);
                cell = row.createCell((int) 2);
                cell.setCellValue("Port Name :");
                cell.setCellStyle(Red);
                //           sheet.addMergedRegion(new Region(rowindex, (short) 2, rowindex, (short) (3)));
                cell = row.createCell((int) 4);
                //cell.setCellValue(txtPortCode.getText().trim() + "  ("+txtPortName.getText().trim()+")");
                cell.setCellValue(FromPort);
                cell.setCellStyle(RedLft);
                //            sheet.addMergedRegion(new Region(rowindex, (short) 4, rowindex, (short) (6)));
                k = 4;
            } else if ("Vessel".equalsIgnoreCase(strtype)) //By Vessel
            {
                rowindex++;//Service
                row = sheet.createRow((int) rowindex);
                cell = row.createCell((int) 2);
                cell.setCellValue("Vessel Name :");
                cell.setCellStyle(Red);
                //        sheet.addMergedRegion(new Region(rowindex, (short) 2, rowindex, (short) (3)));
                cell = row.createCell((int) 4);
                //cell.setCellValue(txtPortCode.getText().trim() + "  ("+txtPortName.getText().trim()+")");
                cell.setCellValue(FromPort);
                cell.setCellStyle(RedLft);
                //       sheet.addMergedRegion(new Region(rowindex, (short) 4, rowindex, (short) (6)));
                k = 4;
            } else if ("Location".equalsIgnoreCase(strtype)) //By Vessel
            {
                rowindex++;//Service
                row = sheet.createRow((int) rowindex);
                cell = row.createCell((int) 2);
                cell.setCellValue("Port Of Load :");
                cell.setCellStyle(Red);
                //        sheet.addMergedRegion(new Region(rowindex, (short) 2, rowindex, (short) (3)));
                cell = row.createCell((int) 4);
                //cell.setCellValue(txtPortCode.getText().trim() + "  ("+txtPortName.getText().trim()+")");
                cell.setCellValue(bwlocpol);
                cell.setCellStyle(RedLft);
                //        sheet.addMergedRegion(new Region(rowindex, (short) 4, rowindex, (short) (6)));
                k = 4;
                row = sheet.createRow((int) 4);
                cell = row.createCell((int) 2);
                cell.setCellValue("Port of Discharge :");
                cell.setCellStyle(Red);
                //       sheet.addMergedRegion(new Region(4, (short) 2, 4, (short) (3)));
                cell = row.createCell((int) 4);
                //cell.setCellValue(txtPortCode.getText().trim() + "  ("+txtPortName.getText().trim()+")");
                cell.setCellValue(bwlocpod);
                cell.setCellStyle(RedLft);
                //       sheet.addMergedRegion(new Region(4, (short) 4, 4, (short) (6)));
                k = 5;
            }
            // POPULATING  GRID VALUE
            row = sheet.createRow((int) k);
            //  row.setHeight((short)(480));
            int colWidth = 0;
            int iHeaderSize = heading.size();
            for (int i = 0; i < iHeaderSize; i++) {
                colWidth = (Integer) columSize.get(i);
                sheet.setColumnWidth((short) i, (short) colWidth);
                cell = row.createCell((int) i);
                cell.setCellValue(heading.get(i));
                cell.setCellStyle(Blue);
            }
            k = k + 1;
            //System.out.println("[ECOM-WAR] ExportToExcelScheldules K is "+k);
            int countList = schlist.size();
            for (int n = 0; n < countList; n++) {
                ArrayList schelist = (ArrayList) schlist.get(n);
                row = sheet.createRow((int) k);
                for (int i = 0; i < schelist.size(); i++) {
                    //  row.setHeight((short)(VslGrid.getRowHeight(i)*16));
                    cell = row.createCell((int) i);
                    cell.setCellValue(schelist.get(i).toString());
                    cell.setCellStyle(Future);
                }
                k++;
            }
            //
            try {
                bos = new ByteArrayOutputStream();
                book.write(bos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bos;
    }
}
