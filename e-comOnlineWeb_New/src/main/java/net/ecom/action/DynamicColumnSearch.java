package net.ecom.action;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote; 
import net.ecom.AppTierCntrlBean.EcomShippingInstructionBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.RemoveDuplicateValues;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1BookingBeanRemote;
import net.ecom.v1.cntlbean.EcomV1SIBeanRemote;
import net.ecom.vo.ColumnList; 
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

public class DynamicColumnSearch extends Action {

    EcomV1BookingBeanRemote v1BkgObj = EJBLookUp.getV1BKGBeanLookUp();
    EcomV1SIBeanRemote v1SIObj = EJBLookUp.getV1SIBeanLookUp(); 
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            response.setContentType("text/html;charset=UTF-8");
            HttpSession session = request.getSession();
            EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
            EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
            EcomShippingInstructionBeanRemote siObj = EJBLookUp.getSIBeanRemoteLookUp();
            ServletContext contexts = servlet.getServletContext();
            String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
            String APCustCodeSearch = (String) contexts.getAttribute(GeneralConf.ENABLE_AP_CUSTCODE_SEARCH);
            String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));
            String type = Validation.checkNull(request.getParameter("type"));
            String userCode = (String) session.getAttribute("SESSION_USERCODE");
            String entry_Type = request.getParameter("entry_type");
            entry_Type = (entry_Type != null && (!"".equalsIgnoreCase(entry_Type))) ? entry_Type : "0";

            String columnName = Validation.checkNull(request.getParameter("columnName"));
            String constraints = Validation.checkNull(request.getParameter("constraints"));
            String columnValue = Validation.checkNull(request.getParameter("consValue"));
            columnValue = spaceSplit(columnValue);
             System.out.println("[ECOM-WAR] DynamicColumnSearch      (DynamicColumnSearch)Type       ==>" + type);
            if ("customer".equalsIgnoreCase(type)) {
                ArrayList customerField = bookingObj.getBookingColumns(userCode);
                ArrayList customerInfo = null;
                if ("1".equalsIgnoreCase(entry_Type)) {
                    customerInfo = (ArrayList) bookingObj.getCustomerData(columnName, constraints, columnValue);
                }
                request.setAttribute("Criteria", "customer");
                request.setAttribute("SearchInfo", customerField);
                request.setAttribute("SearchTitle", "Customer");
                session.setAttribute("ListInfo", customerInfo);
                request.setAttribute("ColumnCount", "6");
            }
            if ("esiSearch".equalsIgnoreCase(type)) {
                String fieldName = Validation.checkNull(request.getParameter("fieldName"));
              //  System.out.println("[ECOM-WAR] DynamicColumnSearch   esiSearch   fieldName--------  " + fieldName );
                List customerField = bookingObj.geteBookingSearch();
                List esiInfo = null;
                List v1Cust = null;
                if ("1".equalsIgnoreCase(entry_Type)) {
                    esiInfo = bookingObj.geteBookingCustomerInfo(columnName, constraints, columnValue, userCode, custTypeConfig, fieldName);
                    if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                        v1Cust = v1SIObj.getV1SICustomerInfo(columnName, constraints, columnValue, userCode);
                        if (v1Cust != null && esiInfo != null) {
                            esiInfo = (List) Stream.concat(v1Cust.stream(), esiInfo.stream()).collect(Collectors.toList());
                            new RemoveDuplicateValues().removeCustomerListDuplicate(esiInfo);
                        }
                    }
                }
                request.setAttribute("Criteria", "esiSearch");
                request.setAttribute("SearchInfo", customerField);
                request.setAttribute("FieldName", fieldName);
                request.setAttribute("SearchTitle", "Customer");
                session.setAttribute("ListInfo", esiInfo);
                request.setAttribute("ColumnCount", "15");
            } else if ("ebookingSearch".equalsIgnoreCase(type)) {
                String fieldName = request.getParameter("fieldName");
               // System.out.println("[ECOM-WAR] DynamicColumnSearch   ebookingSearch   fieldName--------  " + fieldName );
               String fieldID = request.getParameter("FieldID");
                fieldName = (fieldName != null && (!"".equalsIgnoreCase(fieldName))) ? fieldName : "";
                String tempfieldName = fieldName;
                List customerField = bookingObj.geteBookingSearch();
                List esiInfo = null;
                List v1Cust = null;
                if ("1".equalsIgnoreCase(entry_Type)) {
                  //   System.out.println("[ECOM-WAR] DynamicColumnSearch      fieldName--------  " + fieldName + " columnName--iscustcodesearch----  " + columnName);
                   /* if (!"Y".equalsIgnoreCase(APCustCodeSearch)) {
                        tempfieldName = "";
                    } */
                   // commented by gobi for fixing LRPS-17518 and this condtion added in SP.
                    esiInfo = bookingObj.geteBookingCustomerInfo(columnName, constraints, columnValue, userCode, custTypeConfig, tempfieldName);
                    if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                        v1Cust = v1BkgObj.getV1eBookingCustomerInfo(columnName, constraints, columnValue, userCode);
                        if (v1Cust != null && esiInfo != null) {
                             System.out.println("[ECOM-WAR] DynamicColumnSearch      >>v1 customer list>>" + v1Cust);
                            esiInfo = (List) Stream.concat(v1Cust.stream(), esiInfo.stream()).collect(Collectors.toList());
                            new RemoveDuplicateValues().removeCustomerListDuplicate(esiInfo);
                        }
                    }
                }
                request.setAttribute("Criteria", "ebookingSearch");
                request.setAttribute("SearchInfo", customerField);
                request.setAttribute("FieldName", fieldName);
                request.setAttribute("fieldID", fieldID);
                request.setAttribute("SearchTitle", "Customer");
                session.setAttribute("ListInfo", esiInfo);
                request.setAttribute("ColumnCount", "15");
            } else if (type.equalsIgnoreCase("registSearch")) {
                String fieldName = request.getParameter("fieldName");
                fieldName = (fieldName != null && (!"".equalsIgnoreCase(fieldName))) ? fieldName : "";
                //List customerField = bookingObj.geteBookingSearch();
                ColumnList vo = new ColumnList();
                List customerField = new ArrayList();
                vo.setColumnName("CustCode");
                customerField.add(vo);
                vo = new ColumnList();
                vo.setColumnName("Custname");
                customerField.add(vo);
                vo = new ColumnList();
                vo.setColumnName("Email");
                customerField.add(vo);
                List esiInfo = null;
                esiInfo = ecomCntrlObj.UserModuleMappingCustomerInfo(columnName, constraints, columnValue, userCode);
                request.setAttribute("Criteria", "registSearch");
                request.setAttribute("SearchInfo", customerField);
                request.setAttribute("FieldName", fieldName);
                request.setAttribute("SearchTitle", "Customer");
                session.setAttribute("ListInfo", esiInfo);
                request.setAttribute("ColumnCount", "15");
            } else if (type.equalsIgnoreCase("SISearch")) {
                String fieldName = Validation.checkNull(request.getParameter("fieldName"));
               // System.out.println("[ECOM-WAR] DynamicColumnSearch   SISearch   fieldName--------  " + fieldName );
                List customerField = bookingObj.geteBookingSearch();
                List esiInfo = null;
                List v1Cust = null;
                if ("1".equalsIgnoreCase(entry_Type)) {
                    esiInfo = bookingObj.geteBookingCustomerInfo(columnName, constraints, columnValue, userCode, custTypeConfig, fieldName);
                    if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                        v1Cust = v1BkgObj.getV1eBookingCustomerInfo(columnName, constraints, columnValue, userCode);
                        if (v1Cust != null && esiInfo != null) {
                            esiInfo = (List) Stream.concat(v1Cust.stream(), esiInfo.stream()).collect(Collectors.toList());
                            new RemoveDuplicateValues().removeCustomerListDuplicate(esiInfo);
                        }
                    }
                }
                request.setAttribute("Criteria", "SISearch");
                request.setAttribute("SearchInfo", customerField);
                request.setAttribute("FieldName", fieldName);
                request.setAttribute("SearchTitle", "Customer");
                session.setAttribute("ListInfo", esiInfo);
                request.setAttribute("ColumnCount", "15");
            }
            request.setAttribute("COLUMN_NAME", columnName);
            request.setAttribute("CONSTRIANTS_NAME", constraints);
            request.setAttribute("CONSTRIANTS_VALUE", columnValue);
            request.setAttribute("ENTITY_TYPE", entry_Type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward("DynamicSearch");
    }

    public String spaceSplit(String str) {
        char[] ch = str.toCharArray();
        String data = "";
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == ' ') {
                data += ch[i];
                while (ch[++i] == ' ');
                i--;
            } else {
                data += ch[i];
            }
        }
        return data.trim();
    }
}
