/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common.utils;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author prakash.mani
 */
public class FileExtensionValidation {
    

    public boolean checkFileValidation(String enableFileBackendValidation, FormFile curFileExt, String ecomFileExt, HttpServletRequest request) {
        boolean result = true;
        String restrictedFiled = null;
        try {
            restrictedFiled = Validation.checkNull((String) request.getAttribute("restrictedFile"));
            ecomFileExt = Validation.checkNull(ecomFileExt).toLowerCase();
            System.out.println("[ECOM-WAR] FileExtensionValidation restrictedFiled====>" + restrictedFiled);
            if ("Y".equalsIgnoreCase(enableFileBackendValidation) && !ecomFileExt.contains(FilenameUtils.getExtension(curFileExt.getFileName().toLowerCase()))) {              
                restrictedFiled = restrictedFiled + curFileExt.getFileName() + ",";
                System.out.println("[ECOM-WAR] FileExtensionValidation restrictedFile<========" + restrictedFiled);
                result = false;
            }
            request.setAttribute("restrictedFile", restrictedFiled);
            if (result) {
                result = fileSizeValidation(curFileExt, request);
            }
            System.out.println("[ECOM-WAR] FileExtensionValidation checkFileValidation boolean  result Final:====>" + result);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    public boolean fileSizeValidation(FormFile curFileExt, HttpServletRequest request) {
        
        boolean result = true;
        String restrictedFiled = null;
        try {
            String ecomFileUpdSize = (String) request.getServletContext().getAttribute(GeneralConf.EcomFileUploadSize);
            int fileSize=Integer.parseInt(ecomFileUpdSize);  
            restrictedFiled = Validation.checkNull((String) request.getAttribute("restrictedFileSize"));
            if (curFileExt.getFileSize() > 1024*1024*fileSize) {                  
                    restrictedFiled = restrictedFiled + curFileExt.getFileName() + ",";
                    result = false;
            }
            request.setAttribute("restrictedFileSize", restrictedFiled);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
