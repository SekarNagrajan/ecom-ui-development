package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Type;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAdminCntrlBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.formbeans.ImageUploadForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.ImageUploadVo;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author gobinath.t
 */
public class ImageUploadAction extends org.apache.struts.action.Action {

    private static final String FORWARD = "success";
    EcomAdminCntrlBeanRemote adminBeanObj = EJBLookUp.getAdminBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            ImageUploadForm imgform = (ImageUploadForm) form;
            String type = request.getParameter("Stype");
            ServletContext contexts = servlet.getServletContext();
            String result = null;
            ImageUploadVo savevo = null;
            System.out.println("[ECOM-WAR] ImageUploadAction >>>entered in image update action");
            System.out.println("[ECOM-WAR] ImageUploadAction >>>type>>>" + type);
            if (!"Rearrange".equalsIgnoreCase(type)) {
                // Save and Edit operation
                List<ImageUploadVo> rowcountlis = adminBeanObj.getImageUploadDetails("N");  // N- No for Only active field
                String checkimagename = "not exist";
                for (ImageUploadVo imgvo : rowcountlis) {
                    if (imgvo.getImagename().equalsIgnoreCase(imgform.getImagename())) {
                        checkimagename = "exist";
                        break;
                    }
                }
                Map configDetails = (Map) ecomCntrlObj.getMasterConfigDetails();
                String imgtype = imgform.getImage().getContentType();
                int uploadlimit = Integer.parseInt(configDetails.get(GeneralConf.imageupload_limit).toString());
                System.out.println("[ECOM-WAR] ImageUploadAction  >>imgtype>>" + imgtype);
                if (!("image/jpg".equalsIgnoreCase(imgtype) || "image/jpeg".equalsIgnoreCase(imgtype))) {
                    request.setAttribute("pngerror", "Please upload jpg image");
                    request.setAttribute("ImgupType", "error");
                } else if ((!"edit".equalsIgnoreCase(type)) && "exist".equalsIgnoreCase(checkimagename)) {
                    request.setAttribute("limitexceedederror", "Image Name already Exist");
                    request.setAttribute("ImgupType", "error");

                } else if (((!"edit".equalsIgnoreCase(type)) && rowcountlis.size() >= uploadlimit)) {
                    request.setAttribute("limitexceedederror", "Image Upload Limit Exceeded");
                    request.setAttribute("ImgupType", "error");

                } else {

                    savevo = new ImageUploadVo();
                    int keyid = imgform.getKeyid();
                    String imagename = imgform.getImagename();
                    String imagepath = null;
                    int order = rowcountlis.size() + 1;

                    savevo.setImagename(imagename);
                    savevo.setKeyid(keyid);
                    savevo.setOrder(order);

                    FormFile imgfile = imgform.getImage();
                    if (imgfile.getFileData() != null && imgfile.getFileData().length > 0 && imgfile.getFileName().length() > 0) {

                        BufferedImage image = ImageIO.read(imgfile.getInputStream());

                        File compressedImageFile = new File(configDetails.get("JsCachePath") + File.separator + "bgimage" + File.separator + imagename + ".jpg");
                        OutputStream os = new FileOutputStream(compressedImageFile);
                        System.out.println("[ECOM-WAR] ImageUploadAction  >>>compressedImageFile>>>" + compressedImageFile);
                        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
                        ImageWriter writer = (ImageWriter) writers.next();

                        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
                        writer.setOutput(ios);

                        ImageWriteParam param = writer.getDefaultWriteParam();

                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(0.5f);  // Change the quality value you prefer
                        writer.write(null, new IIOImage(image, null, null), param);

                        os.close();
                        ios.close();
                        writer.dispose();

                        /**
                         * FileOutputStream fos = new
                         * FileOutputStream(configDetails.get("JsCachePath") +
                         * "\\bgimage\\" + imagename + ".jpg");
                        fos.write(imgfile.getFileData());*
                         */
                        imagepath =  "/bgimage/" + imagename + ".jpg";
                        savevo.setImagepath(imagepath);
                        //
                        if ("edit".equalsIgnoreCase(type)) {
                            result = adminBeanObj.updateImageUploadDetails(savevo);
                        } else {
                            result = adminBeanObj.saveImageUploadDetails(savevo);

                        }
                    }
                    request.setAttribute("ImgupType", "new");
                }
            } else {
                // Image Order Rearrange Update Operation
                //
                String jsonarraystr = request.getParameter("jsonarraystr");
                Gson gson = new Gson();
                Type jstype = new TypeToken<List<ImageUploadVo>>() {
                }.getType();
                List<ImageUploadVo> rearrlist = gson.fromJson(jsonarraystr, jstype);
                //
                result = adminBeanObj.updateImageUploadRearrangedDetails(rearrlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("ImgupType", "error");
            request.setAttribute("limitexceedederror", "Exception occurred");
        }

        return mapping.findForward(FORWARD);
    }
}
