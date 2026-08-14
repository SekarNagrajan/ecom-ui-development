/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ExportToExcel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.ecom.vo.RegisterMenuVO;

/**
 *
 * @author sasikumar.r
 */
public class Attechment {

    public ByteArrayOutputStream buildAttachment(String fileType, String fileName, ArrayList schlist, String atport, String fromdate, String todate, String bwlocpol, String bwlocpod, String client_code, List<RegisterMenuVO> menulist, String Display_LCT_CUTOFF) {
        ByteArrayOutputStream bos = null;
        StringBuffer strHtmlBuffer = null;
        ArrayList<String> headerList = new ArrayList<String>();
        ArrayList<String> headerList1 = new ArrayList<String>();
        System.out.println("[ECOM-WAR] Attechment ===>buildAttachment:fileName" + fileName);
        if ("Port".equalsIgnoreCase(fileName)) {
            headerList.add("Service");
            //  headerList.add("Vessel Code");
            headerList.add("Vessel Name");
            headerList.add("Voyage");
            headerList.add("Bound");
            //  headerList.add("Port");
            headerList.add("Port Name");
            headerList.add("Terminal Name");
            headerList.add("ETA");
            headerList.add("ETD");
            if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                headerList.add("Container Acceptance Cut-Off");
            }
            //  headerList.add("Vessel Type");
        } else if ("Vessel".equalsIgnoreCase(fileName)) {
            headerList.add("Service");
            //   headerList.add("Vessel Code");
            headerList.add("Vessel Name");
            headerList.add("Voyage");
            headerList.add("Bound");
            //  headerList.add("Port");
            headerList.add("Port Name");
            headerList.add("Terminal Name");
            headerList.add("ETA");    // Arrival Date
            headerList.add("ETD");   // Departure Date
            if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                headerList.add("Container Acceptance Cut-Off");
            }
            //  headerList.add("Vessel Type");
        } else if ("Location".equalsIgnoreCase(fileName)) {
            headerList.add("Port Of Load");
            headerList.add("Dep.Date");
            headerList.add("Service Name");
            headerList.add("Vessel");
            headerList.add("Voyage");
            headerList.add("Bound");
            headerList.add("Port Of Discharge");
            headerList.add("Arr.Date");
            headerList.add("Leg Type");
            if ("Y".equalsIgnoreCase(Display_LCT_CUTOFF)) {
                headerList.add("Container Acceptance Cut-Off");
            }

        }
        System.out.println("[ECOM-WAR] Attechment ===fileType==>>>>" + fileType);
        if (fileType.equalsIgnoreCase("Excel")) {
            bos = buildExcelAttachment(fileName, headerList, headerList1, schlist, atport, fromdate, todate, bwlocpol, bwlocpod, client_code, menulist);
        }
        return bos;
    }

    private ByteArrayOutputStream buildExcelAttachment(String fileName, ArrayList headerList, ArrayList headerList1, ArrayList schlist, String atport, String fromdate, String todate, String bwlocpol, String bwlocpod, String client_code, List<RegisterMenuVO> menulist) {
        ByteArrayOutputStream bos = null;
        ArrayList<Integer> columnSize = new ArrayList<Integer>();
        ArrayList<Integer> columnSize1 = new ArrayList<Integer>();
        if (fileName.equalsIgnoreCase("port")) {
            columnSize.add(3200);//Service
            //  columnSize.add(3200);//Vessel Code
            columnSize.add(4500);//Vessel Name
            columnSize.add(3200);//Voyage
            columnSize.add(3300);//Bound
            //  columnSize.add(3300);//Port
            columnSize.add(6000);//Port Name
            columnSize.add(7000);//Terminal Name
            columnSize.add(7000);//Arrival Date
            columnSize.add(7000);//Departure Date
            columnSize.add(7000);//Container Acceptance Cut-Off
            //  columnSize.add(3300);//Vessel Type
        } else if ("Vessel".equalsIgnoreCase(fileName)) {
            columnSize.add(3200);//Service
            //   columnSize.add(3200);//Vessel Code
            columnSize.add(4500);//Vessel Name
            columnSize.add(3200);//Voyage
            columnSize.add(3300);//Bound
            //   columnSize.add(3300);//Port
            columnSize.add(6000);//Port Name
            columnSize.add(7000);//Terminal
            columnSize.add(7000);//Arrival Date
            columnSize.add(7000);//Departure Date
            columnSize.add(7000);//Container Acceptance Cut-Off
            //   columnSize.add(3300);//Vessel Type
        } else if ("Location".equalsIgnoreCase(fileName)) {
            columnSize.add(7000);
            columnSize.add(7000);
            columnSize.add(7000);
            columnSize.add(7000);
            columnSize.add(7000);
            columnSize.add(3300);
            columnSize.add(7000);
            columnSize.add(7000);
            columnSize.add(7000);
            columnSize.add(7000);//Container Acceptance Cut-Off
        }

        ExportToExcelScheldules exr = new ExportToExcelScheldules();
        bos = (ByteArrayOutputStream) exr.ExcelCration(headerList, columnSize, schlist, atport, fromdate, todate, fileName, bwlocpol, bwlocpod, client_code, menulist);

        return bos;
    }
}
