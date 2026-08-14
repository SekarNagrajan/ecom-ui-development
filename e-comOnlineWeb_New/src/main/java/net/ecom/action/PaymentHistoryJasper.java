package net.ecom.action;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.phase2.service.vo.PaymentHistoryVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;

public class PaymentHistoryJasper extends org.apache.struts.action.Action {

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    private static final Font COMPANY_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font LABEL_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
    private static final Font INFO_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD);
    private static final Font DATA_FONT = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);
    private static final Font PAGE_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);

    private static final String[] HEADER_KEYS = {
        "ecom.ph.bankname", "ecom.ph.bankcode", "ecom.ph.currency", "ecom.ph.accountno", "ecom.ph.referenceno",
        "ecom.ph.statementdate", "ecom.ph.customername", "ecom.ph.customercode", "ecom.ph.customeraccountno",
        "ecom.ph.checkno", "ecom.ph.checkamount", "ecom.ph.checkallocatedamt", "ecom.ph.unallocatedamt",
        "ecom.ph.transactionno", "ecom.ph.transactiondate", "ecom.ph.blno", "ecom.ph.mcnno",
        "ecom.ph.blamount", "ecom.ph.blallocatedamt", "ecom.ph.blbalanceamt"
    };

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "success";
        try {
            HttpSession session = request.getSession();
            ServletContext context = servlet.getServletContext();
            System.out.println("[ECOM-WAR] PaymentHistoryJasper >>sessionId>>>" + session.getId());

            List<PaymentHistoryVO> paymentHistoryList =
                    (List<PaymentHistoryVO>) session.getAttribute("paymentHistoryListForPrint");

            System.out.println("[ECOM-WAR] PaymentHistoryJasper >>listSize>>>"
                    + (paymentHistoryList != null ? paymentHistoryList.size() : "null"));

            if (paymentHistoryList == null || paymentHistoryList.isEmpty()) {
                SUCCESS = "fail";
                request.setAttribute("croprintstatus", "fail");
                session.setAttribute("crojasperbyte", null);
                System.out.println("[ECOM-WAR] PaymentHistoryJasper >>No data to export");
                return mapping.findForward(SUCCESS);
            }

            String clientcode = Validation.checkNull((String) session.getAttribute("clientcode"));
            MessageResources messages = null;
            if (clientcode.length() > 0) {
                messages = getResources(request, clientcode);
            }
            if (messages == null) {
                messages = getResources(request);
            }

            String[] headers = new String[HEADER_KEYS.length];
            for (int i = 0; i < HEADER_KEYS.length; i++) {
                String msg = messages.getMessage(HEADER_KEYS[i]);
                headers[i] = (msg != null) ? msg : HEADER_KEYS[i];
            }

            String carriercode = Validation.checkNull((String) context.getAttribute(GeneralConf.CARRIER_CODE));
            String customerCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            String customerName = Validation.checkNull((String) session.getAttribute("SESSION_USERNAME"));
            List personalInfo = ecomCntrlObj.getAPDetails(customerCode);
            String country = Validation.checkNull((String) personalInfo.get(7));
            if (country.contains(" - ")) {
                country = country.split(" - ", 2)[1].trim();
            }
            String custdetails = personalInfo.get(1) + "\n" + personalInfo.get(2) + "\n" 
            + personalInfo.get(8) + "\n" + personalInfo.get(13) + ", " + personalInfo.get(14) + ", " + personalInfo.get(15) + 
            "\n" + country;
            System.out.println("personalInfo>>>" + personalInfo);
            System.out.println("customerName>>>" + customerName);
            System.out.println("custdetails>>>" + custdetails);

            List carrierInfo = ecomCntrlObj.getCarrierDetails(carriercode);
            String companyDetails = "";
            if (carrierInfo != null && carrierInfo.size() >= 5) {
                String carrierName = Validation.checkNull(String.valueOf(carrierInfo.get(0)));
                String carrierAddress = Validation.checkNull(String.valueOf(carrierInfo.get(1)));
                String cityName = Validation.checkNull(String.valueOf(carrierInfo.get(2)));
                String stateName = Validation.checkNull(String.valueOf(carrierInfo.get(3)));
                String zipCode = Validation.checkNull(String.valueOf(carrierInfo.get(4)));
                StringBuilder cityLine = new StringBuilder();
                if (cityName.length() > 0) cityLine.append(cityName);
                if (stateName.length() > 0) {
                    if (cityLine.length() > 0) cityLine.append(", ");
                    cityLine.append(stateName);
                }
                if (zipCode.length() > 0) {
                    if (cityLine.length() > 0) cityLine.append(" ");
                    cityLine.append(zipCode);
                }
                companyDetails = carrierName + "\n" + carrierAddress + "\n" + cityLine.toString();
            }
            System.out.println("companyDetails>>>" + companyDetails);

            String logoUrl = null;
            try {
                String imagePath = Validation.checkNull((String) context.getAttribute("Imagepath"));
                String logoName = Validation.checkNull((String) context.getAttribute(GeneralConf.Logo_Name));
                if (imagePath.length() > 0 && logoName.length() > 0) {
                    logoUrl = imagePath + logoName;
                }
            } catch (Exception e) {
                System.out.println("[ECOM-WAR] PaymentHistoryJasper >>Logo not available: " + e.getMessage());
            }

            byte[] pdfBytes = generatePdf(paymentHistoryList, headers, companyDetails, customerCode, custdetails, customerName, logoUrl);
            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

            session.setAttribute("crojasperbyte", base64Pdf);
            request.setAttribute("croprintstatus", "success");
            request.setAttribute("viewType", "P");
            request.setAttribute("responetype", "200");

            System.out.println("[ECOM-WAR] PaymentHistoryJasper >>PDF generated, size>>>" + pdfBytes.length);

        } catch (Exception e) {
            request.setAttribute("croprintstatus", "fail");
            SUCCESS = "fail";
            System.out.println("[ECOM-WAR] PaymentHistoryJasper >>Exception>>>" + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("[ECOM-WAR] PaymentHistoryJasper ======>SUCCESS>" + SUCCESS);
        return mapping.findForward(SUCCESS);
    }

    private byte[] generatePdf(List<PaymentHistoryVO> list, String[] headers, String companyName, String customerCode, String custdetails, String customerName, String logoUrl) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 30);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setPageEvent(new PageNumberEvent());
        document.open();

        buildHeader(document, writer, companyName, logoUrl);
        buildCustomerInfo(document, customerCode, custdetails, customerName);

        System.out.println("[ECOM-WAR] PaymentHistoryJasper >>customerCode>>>" + customerCode);
        System.out.println("[ECOM-WAR] PaymentHistoryJasper >>custdetails>>>" + custdetails);

        PdfPTable table = buildDataTable(list, headers);
        document.add(table);

        document.close();
        return baos.toByteArray();
    }

    private void buildHeader(Document document, PdfWriter writer, String companyName, String logoUrl) throws Exception {
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{25f, 50f, 25f});

        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        try {
            if (logoUrl != null && logoUrl.length() > 0) {
                Image logo = Image.getInstance(logoUrl);
                logo.scaleToFit(100, 60);
                logoCell.addElement(logo);
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] PaymentHistoryJasper >>Could not load logo: " + e.getMessage());
        }
        headerTable.addCell(logoCell);

        PdfPCell companyCell = new PdfPCell();
        companyCell.setBorder(Rectangle.NO_BORDER);
        companyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        companyCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        System.out.println("[ECOM-WAR] PaymentHistoryJasper >>companyName>>>" + companyName);
        if (companyName.length() > 0) {
            String[] compLines = companyName.split("\n");
            for (int idx = 0; idx < compLines.length; idx++) {
                Font lineFont = (idx == 0) ? COMPANY_FONT : LABEL_FONT;
                Paragraph linePara = new Paragraph(compLines[idx], lineFont);
                linePara.setAlignment(Element.ALIGN_CENTER);
                companyCell.addElement(linePara);
            }
        }
        headerTable.addCell(companyCell);

        PdfPCell emptyCell = new PdfPCell();
        emptyCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(emptyCell);

        document.add(headerTable);

        PdfPTable lineTable = new PdfPTable(1);
        lineTable.setWidthPercentage(100);
        lineTable.setSpacingBefore(5f);
        PdfPCell lineCell = new PdfPCell(new Phrase(""));
        lineCell.setBorder(Rectangle.BOTTOM);
        lineCell.setBorderColor(BaseColor.BLACK);
        lineCell.setBorderWidth(1f);
        lineCell.setFixedHeight(1f);
        lineTable.addCell(lineCell);
        document.add(lineTable);

        String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        PdfPTable titleTable = new PdfPTable(3);
        titleTable.setWidthPercentage(100);
        titleTable.setWidths(new float[]{25f, 50f, 25f});
        titleTable.setSpacingBefore(5f);
        titleTable.setSpacingAfter(3f);

        PdfPCell dateCell = new PdfPCell(new Phrase("Date :  " + currentDate, LABEL_FONT));
        dateCell.setBorder(Rectangle.NO_BORDER);
        dateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleTable.addCell(dateCell);

        PdfPCell titleCell = new PdfPCell(new Phrase("PAYMENT HISTORY", TITLE_FONT));
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleTable.addCell(titleCell);

        PdfPCell pageCell = new PdfPCell(new Phrase("", PAGE_FONT));
        pageCell.setBorder(Rectangle.NO_BORDER);
        pageCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        titleTable.addCell(pageCell);

        document.add(titleTable);

        PdfPTable lineTable2 = new PdfPTable(1);
        lineTable2.setWidthPercentage(100);
        PdfPCell lineCell2 = new PdfPCell(new Phrase(""));
        lineCell2.setBorder(Rectangle.BOTTOM);
        lineCell2.setBorderColor(BaseColor.BLACK);
        lineCell2.setBorderWidth(1f);
        lineCell2.setFixedHeight(1f);
        lineTable2.addCell(lineCell2);
        lineTable2.setSpacingAfter(8f);
        document.add(lineTable2);
    }

    private void buildCustomerInfo(Document document, String customerCode, String custdetails, String customerName) throws Exception {
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{50f, 50f});
        infoTable.setSpacingAfter(10f);

        PdfPCell leftCell = new PdfPCell();
        leftCell.setBorder(Rectangle.NO_BORDER);
        if (customerCode.length() > 0) {
            leftCell.addElement(new Phrase(customerCode, INFO_FONT));
        }
        if (custdetails.length() > 0) {
            leftCell.addElement(new Phrase(custdetails, INFO_FONT));
        }
        infoTable.addCell(leftCell);

        PdfPCell rightCell = new PdfPCell();
        if (customerName.length() > 0) {
            rightCell.addElement(new Phrase("ATTN.  : " + customerName, LABEL_FONT));
        }
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setPadding(5f);
        rightCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        infoTable.addCell(rightCell);

        document.add(infoTable);
    }

    private PdfPTable buildDataTable(List<PaymentHistoryVO> list, String[] headers) throws Exception {
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{
            6f, 4f, 3.5f, 5f, 5f,
            5f, 7f, 5f, 5.5f,
            4.5f, 5.5f, 5.5f, 5.5f,
            5f, 5f, 5.5f, 4.5f,
            5.5f, 5.5f, 5.5f
        });
        table.setHeaderRows(1);

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            cell.setBorderWidth(0.5f);
            cell.setBorderColor(BaseColor.BLACK);
            table.addCell(cell);
        }

        DecimalFormat df = new DecimalFormat("#,##0.00");

        for (int i = 0; i < list.size(); i++) {
            PaymentHistoryVO vo = list.get(i);

            addTextCell(table, Validation.checkNull(vo.getBankname()));
            addTextCell(table, Validation.checkNull(vo.getBankcode()));
            addTextCell(table, Validation.checkNull(vo.getCurrency()));
            addTextCell(table, Validation.checkNull(vo.getAccountno()));
            addTextCell(table, Validation.checkNull(vo.getReferenceno()));
            addTextCell(table, formatDate(vo.getStatementdate()));
            addTextCell(table, Validation.checkNull(vo.getCustomername()));
            addTextCell(table, Validation.checkNull(vo.getCustomercode()));
            addTextCell(table, Validation.checkNull(vo.getCustomeraccountno()));
            addTextCell(table, Validation.checkNull(vo.getCheckno()));
            addAmountCell(table, vo.getCheckamount(), df);
            addAmountCell(table, vo.getCheckallocatedamount(), df);
            addAmountCell(table, vo.getUnallocatedamount(), df);
            addTextCell(table, Validation.checkNull(vo.getTransactionno()));
            addTextCell(table, formatDate(vo.getAllocationdate()));
            addTextCell(table, Validation.checkNull(vo.getBlno()));
            addTextCell(table, Validation.checkNull(vo.getMcnno()));
            addAmountCell(table, vo.getBlamount(), df);
            addAmountCell(table, vo.getBlallocatedamount(), df);
            addAmountCell(table, vo.getBlbalanceamount(), df);
        }

        return table;
    }

    private void addTextCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", DATA_FONT));
        cell.setPadding(3f);
        cell.setBorderWidth(0.5f);
        cell.setBorderColor(BaseColor.BLACK);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private void addAmountCell(PdfPTable table, String value, DecimalFormat df) {
        String formatted = "";
        String val = Validation.checkNull(value);
        if (val.length() > 0 && !"null".equalsIgnoreCase(val)) {
            try {
                formatted = df.format(Double.parseDouble(val));
            } catch (NumberFormatException e) {
                formatted = val;
            }
        }
        PdfPCell cell = new PdfPCell(new Phrase(formatted, DATA_FONT));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(3f);
        cell.setBorderWidth(0.5f);
        cell.setBorderColor(BaseColor.BLACK);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private String formatDate(String dateStr) {
        try {
            String converted = DateConversion_New.convertDefaultFormat(dateStr);
            return Validation.checkNull(converted);
        } catch (Exception e) {
            return Validation.checkNull(dateStr);
        }
    }

    private static class PageNumberEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            int pageNum = writer.getPageNumber();
            Phrase footer = new Phrase("Page  " + pageNum, PAGE_FONT);
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, footer,
                    document.right(), document.bottom() - 15, 0);
        }
    }
}
