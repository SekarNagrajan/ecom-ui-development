/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ExportToExcel;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.DocumentException;

import java.io.OutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;

/**
 *
 * @author vasanthakumar.r
 */
public class VesselInformation extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = getServletContext();
        HttpSession session = request.getSession();

        try {
            // Get the text that will be added to the PDF
            ArrayList VesselDetails = (ArrayList) session.getAttribute("Vesseldetails");
            String VesselName = "", vesselcode = "", callsign = "", vesseloperator = "", vesselowner = "", vesseltype = "";
            String flag = "", portofregistry = "", built = "", imo = "", grosston = "", netton = "";
            String lengthoverall = "", teunominal = "";
            String carriercode = (String) context.getAttribute(GeneralConf.CARRIER_CODE);
            if (VesselDetails != null && VesselDetails.size() > 0) {
                if (VesselDetails.get(0) != null && VesselDetails.get(0).toString().trim().length() > 0) {
                    VesselName = VesselDetails.get(0).toString();
                } else {
                    VesselName = "-";
                }
                if (VesselDetails.get(1) != null && VesselDetails.get(1).toString().trim().length() > 0) {
                    vesselcode = VesselDetails.get(1).toString();
                } else {
                    vesselcode = "-";
                }
                if (VesselDetails.get(2) != null && VesselDetails.get(2).toString().trim().length() > 0) {
                    callsign = VesselDetails.get(2).toString();
                } else {
                    callsign = "-";
                }

                if (VesselDetails.get(3) != null && VesselDetails.get(3).toString().trim().length() > 0) {
                    vesseloperator = VesselDetails.get(3).toString();
                } else {
                    vesseloperator = "-";
                }

                if (VesselDetails.get(4) != null && VesselDetails.get(4).toString().trim().length() > 0) {
                    vesselowner = VesselDetails.get(4).toString();
                } else {
                    vesselowner = "-";
                }

                if (VesselDetails.get(5) != null && VesselDetails.get(5).toString().trim().length() > 0) {
                    vesseltype = VesselDetails.get(5).toString();
                } else {
                    vesseltype = "-";
                }

                if (VesselDetails.get(6) != null && VesselDetails.get(6).toString().trim().length() > 0) {
                    flag = VesselDetails.get(6).toString();
                } else {
                    flag = "-";
                }

                if (VesselDetails.get(7) != null && VesselDetails.get(7).toString().trim().length() > 0) {
                    portofregistry = VesselDetails.get(7).toString();
                } else {
                    portofregistry = "-";
                }

                if (VesselDetails.get(8) != null && VesselDetails.get(8).toString().trim().length() > 0) {
                    built = VesselDetails.get(8).toString();
                } else {
                    built = "-";
                }

                if (VesselDetails.get(9) != null && VesselDetails.get(9).toString().trim().length() > 0) {
                    imo = VesselDetails.get(9).toString();
                } else {
                    imo = "-";
                }

                if (VesselDetails.get(10) != null && VesselDetails.get(10).toString().trim().length() > 0) {
                    lengthoverall = VesselDetails.get(10).toString();
                } else {
                    lengthoverall = "-";
                }

                if (VesselDetails.get(11) != null && VesselDetails.get(11).toString().trim().length() > 0) {
                    teunominal = VesselDetails.get(11).toString();
                } else {
                    teunominal = "-";
                }

                if (VesselDetails.get(12) != null && VesselDetails.get(12).toString().trim().length() > 0) {
                    grosston = VesselDetails.get(12).toString();
                } else {
                    grosston = "-";
                }

                if (VesselDetails.get(13) != null && VesselDetails.get(13).toString().trim().length() > 0) {
                    netton = VesselDetails.get(13).toString();
                } else {
                    netton = "-";
                }

                // step 1
                Document document = new Document();
                // step 2
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter.getInstance(document, baos);
                // step 3
                document.open();
                String par = "The information contained in this page is for general information purposes only. The ";
                if ("ESL".equalsIgnoreCase(carriercode)) {
                    par += "information is provided by Emirates Shipping Line FZE and whilst we endeavour to keep ";
                } else {
                    par += "information is provided by "+carriercode+" and whilst we endeavour to keep ";
                }
                par += "the information up-to-date and correct, we make no representations or warranties of any kind, "
                        + "express or implied, about the completeness, accuracy, reliability, suitability or availability with "
                        + "respect to the website or the information, products, services, or related graphics contained on "
                        + "the website for any purpose. Any reliance you place on such information is therefore strictly at "
                        + "your own risk.";

                String par1 = "In no event will we be liable for any loss or damage including without limitation, indirect or "
                        + "consequential loss or damage, or any loss or damage whatsoever arising from use of this "
                        + "information";

                String imagepath = (String) context.getAttribute("Imagepath");
                String logo_name = (String) context.getAttribute(GeneralConf.Logo_Name);
                //  String imagepath="http://localhost:8070/Ecommaster/";
                String logo = imagepath + logo_name;
                //Inserting Image in PDF
                Image image = Image.getInstance(logo);
                image.scaleAbsolute(194f, 40f);//image width,height
//Inserting Table in PDF

                PdfPTable table = new PdfPTable(2);
                PdfPTable table1 = new PdfPTable(2);

                BaseColor fontcolor = WebColors.getRGBColor("1146a0");

                BaseColor fontcolor1 = WebColors.getRGBColor("666666");

                Font myFonColor = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, fontcolor);

                BaseColor backcolor = WebColors.getRGBColor("f2f2ff");

                PdfPCell cell = new PdfPCell(new Paragraph("Vessel Information", myFonColor));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(cell);

                table1.setSpacingBefore(1.0f);       // Space Before table starts, like margin-top in CSS
                table1.setSpacingAfter(1.0f);        // Space After table starts, like margin-Bottom in CSS

                Font myFonColor1 = FontFactory.getFont(FontFactory.HELVETICA, 10, fontcolor);
                Font myFonColor2 = FontFactory.getFont(FontFactory.HELVETICA, 10, fontcolor1);

                cell = new PdfPCell(new Phrase("Vessel Name:", myFonColor1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(VesselName, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Vessel Code:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(vesselcode, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Call Sign:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(callsign, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Vessel Operator:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(vesseloperator, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Vessel Type:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(vesseltype, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Flag:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(flag, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Port Of Registry:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(portofregistry, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Build:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(built, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("IMO/Lloyd's Number:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(imo, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TEUs Nominal:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(teunominal, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Length Over All:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(lengthoverall, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Gross Tonnage:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(grosston, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Net Tonnage:", myFonColor1));
                cell.disableBorderSide(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(netton, myFonColor2));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.disableBorderSide(Rectangle.BOX);
                cell.setBackgroundColor(BaseColor.CYAN);
                cell.setBackgroundColor(backcolor);
                table.addCell(cell);

                table.setSpacingBefore(6.0f);        // Space Before table starts, like margin-top in CSS
                table.setSpacingAfter(10.0f);        // Space After table starts, like margin-Bottom in CSS

                table1.setWidthPercentage(100);

                table.setWidthPercentage(60);
                table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.getDefaultCell().setFixedHeight(30);
                table.getDefaultCell().setBackgroundColor(BaseColor.BLUE);

                document.open();//PDF document opened........

                //Now Insert Every Thing Into PDF Document
                document.add(image);

                document.add(table1);
                document.add(table);

                document.addTitle("E-Commerce");

                document.add(new Paragraph(par, myFonColor2));

                Paragraph para1 = new Paragraph(32);
                para1.setSpacingBefore(5);
                para1.setSpacingAfter(5);
                para1.add("");
                document.add(para1);

                document.add(new Paragraph(par1, myFonColor2));

                //document.newPage();            //Opened new page
                // step 5
                document.close();

                // setting some response headers
                response.setHeader("Expires", "0");
                response.setHeader("Cache-Control",
                        "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                // setting the content type
                //    response.setContentType("application/pdf; name=\"MyFile.pdf\"");
                response.setContentType("application/pdf");
                // the contentlength
                response.setContentLength(baos.size());
                // write ByteArrayOutputStream to the ServletOutputStream
                OutputStream os = response.getOutputStream();
                baos.writeTo(os);
                os.flush();
                os.close();
            } else {
                RequestDispatcher requestdep = request.getRequestDispatcher("SessionExpired.jsp");
                requestdep.forward(request, response);
            }
        } catch (DocumentException e) {  // DocumentException
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 6067021675155015602L;

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
