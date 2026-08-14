/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import net.ecom.formbeans.SIEditForm;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SISealVO;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;

public class SIValidation {

    public ActionErrors validateSIForm(SIEditForm editForm, Map<SIMasterVO, SIConatainerValueVO> SICntInfoHashObj, Map<SIMasterVO, SIConatainerValueVO> submitteSICntInfoHashObj, ServletContext contexts, HttpSession session) {
        ActionErrors errors = new ActionErrors();

        // Shipper Party
        if (editForm.getSpname().length() == 0) {
            errors.add("SI_SPNAME_ERROR", new ActionMessage("error.SI.SPNAME.Msg"));
        }
        if (editForm.getSpaddr().length() == 0) {
            errors.add("SI_SPADDR_ERROR", new ActionMessage("error.SI.SPADDR.Msg"));
        }

        if (editForm.getSpphone().length() == 0) {
            //errors.add("SI_SP_PHONE_ERROR", new ActionMessage("error.SI.SPPHONE.Msg"));
        }
        if (editForm.getSpphone().length() > 0) {
            /**if (editForm.getSpphone().length() < 2 && editForm.getSpphone().length() > 50) {
            errors.add("SI_SP_PHONE_ERROR", new ActionMessage("error.SI.SPPHONEINVALID.Msg"));
            }**/
        }
        if (editForm.getSpfax().length() > 0) {
            /**if (editForm.getSpfax().length() < 2 && editForm.getSpfax().length() > 20) {
            errors.add("SI_SP_FAX_ERROR", new ActionMessage("error.SI.SPFAXINVALID.Msg"));
            }**/
        }

        /**if (editForm.getSpcountry().length() == 0) {
            errors.add("SI_SPCOUNTRY_ERROR", new ActionMessage("error.SI.SPCOUNTRY.Msg"));
        } else if (editForm.getSpcountry().length() > 0 && editForm.getSpIsCountry().length() == 0) {
            errors.add("SI_SPCOUNTRY_ERROR", new ActionMessage("error.SI.SPCOUNTRYINVALID.Msg"));
        }
        if (editForm.getSpcity().length() == 0) {
            errors.add("SI_SPCITY_ERROR", new ActionMessage("error.SI.SPCITY.Msg"));
        }**/

        /**if ((editForm.getSpemail().length() > 0) && (!Pattern.matches("^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$", editForm.getSpemail()))) {
            errors.add("SI_SPEMAIL_ERROR", new ActionMessage("error.SI.SPEMAILINVALID.Msg"));
        }**/

        //Consignee
        /**if (editForm.getConsname().length() == 0) {
            errors.add("SI_CONSNAME_ERROR", new ActionMessage("error.SI.CONSNAME.Msg"));
        }**/
        /**if (editForm.getConsaddr().length() == 0) {
            errors.add("SI_CONSADDR_ERROR", new ActionMessage("error.SI.CONSADDR.Msg"));
        }

        /**if (editForm.getConsphone().length() == 0) {
        errors.add("SI_CONS_PHONE_ERROR", new ActionMessage("error.SI.CONSPHONE.Msg"));
        }**/
        /**if (editForm.getConsphone().length() > 0) {
        if (editForm.getConsphone().length() < 2 || editForm.getConsphone().length() > 50) {
        errors.add("SI_CONS_PHONE_ERROR", new ActionMessage("error.SI.CONSPHONEINVALID.Msg"));
        }
        }**/
        /** if (editForm.getConsfax().length() > 0) {
        if (editForm.getConsfax().length() < 2 || editForm.getConsfax().length() > 20) {
        errors.add("SI_CONS_FAX_ERROR", new ActionMessage("error.SI.CONSFAXINVALID.Msg"));
        }
        }**/
        /**if (editForm.getConscountry().length() == 0) {
            errors.add("SI_CONSCOUNTRY_ERROR", new ActionMessage("error.SI.CONSCOUNTRY.Msg"));
        } else if (editForm.getConscountry().length() > 0 && editForm.getConsIsCountry().length() == 0) {
            errors.add("SI_CONSCOUNTRY_ERROR", new ActionMessage("error.SI.CONSCOUNTRYINVALID.Msg"));
        }
        if (editForm.getConscity().length() == 0) {
            errors.add("SI_CONSCITY_ERROR", new ActionMessage("error.SI.CONSCITY.Msg"));
        }

        if ((editForm.getConsemail().length() > 0) && (!Pattern.matches("^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$", editForm.getConsemail()))) {
            errors.add("SI_CONSEMAIL_ERROR", new ActionMessage("error.SI.CONSEMAILINVALID.Msg"));
        }

        //Notify
//        if (editForm.getNpname().length() == 0) {
//            errors.add("SI_NTNAME_ERROR", new ActionMessage("error.SI.NTNAME.Msg"));
//        }
//        if (editForm.getNpaddr().length() == 0) {
//            errors.add("SI_NTADDR_ERROR", new ActionMessage("error.SI.NTADDR.Msg"));
//        }

        /**if (editForm.getNpphone().length() > 0) {
        if (editForm.getNpphone().length() < 2 || editForm.getNpphone().length() > 50) {
        errors.add("SI_NT_PHONE_ERROR", new ActionMessage("error.SI.NTPHONEINVALID.Msg"));
        }
        }
        if (editForm.getNpfax().length() > 0) {
        if (editForm.getNpfax().length() < 2 || editForm.getNpfax().length() > 20) {
        errors.add("SI_NT_FAX_ERROR", new ActionMessage("error.SI.NTFAXINVALID.Msg"));
        }
        }**/
//        if (editForm.getNpcountry().length() == 0) {
//            errors.add("SI_NTCOUNTRY_ERROR", new ActionMessage("error.SI.NTCOUNTRY.Msg"));
//        } else if (editForm.getNpcountry().length() > 0 && editForm.getNpIsCountry().length() == 0) {
//            errors.add("SI_NTCOUNTRY_ERROR", new ActionMessage("error.SI.NTCOUNTRYINVALID.Msg"));
//        }
//        if (editForm.getNpcity().length() == 0) {
//            errors.add("SI_NTCITY_ERROR", new ActionMessage("error.SI.NTCITY.Msg"));
//        }
        /**if ((editForm.getNpemail().length() > 0) && (!Pattern.matches("^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$", editForm.getNpemail()))) {
            errors.add("SI_NTEMAIL_ERROR", new ActionMessage("error.SI.NTEMAILINVALID.Msg"));
        }**/

        // Container details   validation
        List comListObj = (List) contexts.getAttribute("SESSION_COMMODITYLIST");
        int comListSize = 0;
        if (comListObj != null && comListObj.size() > 0) {
            comListSize = comListObj.size();
        }

        List pacListObj = (List) contexts.getAttribute("SESSION_PACKAGELIST");
        int pakListSize = 0;
        if (pacListObj != null && pacListObj.size() > 0) {
            pakListSize = pacListObj.size();
        }
        String strListObj = "";
        boolean isCommodity = false;
        boolean isPackage = false;
        int alsize = 0;
        String cntNo = "", shipperSealName = "", carrierSealName = "", shipperSeal = "", carrierSeal = "", eqpType = "", eqpTypeName = "";
        String dtCmd = "", dtWt = "", dtUnit = "", dtQty = "", dtPkg = "", dtVol = "", dtVut = "", dtMrk = "", dtDes = "";
        int con = 0;
        String errorMsg = "", errStr = "";
        int cntIncr = 1, dtIncr = 1, cntListSize = 0;
        if (submitteSICntInfoHashObj != null && submitteSICntInfoHashObj.size() > 0) {
            alsize = submitteSICntInfoHashObj.size();
            Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = submitteSICntInfoHashObj.entrySet();
            for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {

                SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                cntNo = siMasterVO.getStrCntNo();
                con = siMasterVO.getStrSelect();
                shipperSeal = siSealVOObj.getStrShipperSeal();
                carrierSeal = siSealVOObj.getStrCarrierSeal();
                eqpType = siMasterVO.getStrType();
                errorMsg = "SI_SHIPPERSEAL_ERROR" + cntIncr;
//                System.out.println("[ECOM-WAR] SIValidation shipperSeal in SI VALIATION length "+shipperSeal.length());
//                System.out.println("[ECOM-WAR] SIValidation carrierSeal in SI VALIATION "+carrierSeal.length());
               /* if(shipperSeal.length()==0){
                //                    System.out.println("[ECOM-WAR] SIValidation shipperSeal in SI VALIATION inside"+shipperSeal);
                errStr="Shipper Seal is required for Container "+cntNo;
                //errors.add(errorMsg,new ActionMessage(errorMsg, errStr));
                errors.add("SI_SHIPPERSEAL_ERROR",new ActionMessage("error.SI.SHIPPERSEAL.Msg"));
                }*/
                /**  if(shipperSeal.trim().length()>0 && !Pattern.matches("^[a-zA-Z-0-9]{1,40}$",shipperSeal)){
                errStr="Invalid Shipper Seal for Container "+cntNo;
                errors.add("SI_SHIPPERSEAL_ERROR",new ActionMessage("error.SI.SHIPPERSEALINVALID.Msg"));
                }
                errorMsg="SI_CARRIERSEAL_ERROR"+cntIncr;
                /* if(carrierSeal.length()==0){ commented by gnanamoorthi.S==14.08.2013
                //                       System.out.println("[ECOM-WAR] SIValidation carrierSeal in SI VALIATION inside "+carrierSeal);
                errStr="Carrier Seal is required for Container "+cntNo;
                errors.add("SI_CARRIERSEAL_ERROR",new ActionMessage("error.SI.CARRIER.Msg"));
                }else*/
                /**  if(carrierSeal.trim().length()>0 && !Pattern.matches("^[a-zA-Z-0-9]{1,12}$",carrierSeal)){
                errStr="Invalid Carrier Seal for Container "+cntNo;
                errors.add("SI_CARRIERSEAL_ERROR",new ActionMessage("error.SI.CARRIERSEALINVALID.Msg"));
                }**/
                /**     if (cntList != null && cntList.size() > 0) {
                cntListSize = 0;
                cntListSize = cntList.size();
                for (int j = 0; j < cntListSize; j++) {
                SIContainerDetailVO siCntDetVo=(SIContainerDetailVO)cntList.get(j);
                dtCmd = siCntDetVo.getStrCommodity();
                dtWt = siCntDetVo.getStrWgt();
                dtUnit = siCntDetVo.getStrUnit();
                dtQty = siCntDetVo.getStrQty();
                dtPkg = siCntDetVo.getStrPkg();
                dtVol = siCntDetVo.getStrVol();
                dtVut = siCntDetVo.getStrVut();
                dtMrk = siCntDetVo.getStrMrk();
                dtDes = siCntDetVo.getStrDes();
                errorMsg="SI_COMMODITY_ERROR"+dtIncr;

                if (dtCmd.length() == 0) {
                errStr="Commodity is required in row "+dtIncr;
                //errors.add(errorMsg, new ActionMessage(errorMsg, errStr));
                errors.add("SI_COMMODITY_ERROR",new ActionMessage("error.SI.COMMODITY.Msg"));
                } else if (comListSize>0) { //comListObj
                isCommodity=false;
                for (int i = 0; i < comListSize; i++) {
                dtCmd=dtCmd.toLowerCase();
                strListObj="";
                strListObj = comListObj.get(i).toString().toLowerCase();
                if (strListObj.contains(dtCmd)) {
                isCommodity=true;
                }
                }
                if(!isCommodity){
                errors.add("SI_COMMODITY_ERROR",new ActionMessage("error.SI.COMMODITYINVALID.Msg"));
                }

                }
                errorMsg="SI_PACKAGE_ERROR"+dtIncr;
                //                        if (dtPkg.length() == 0) { //Commented by Shikin PLARPENH-440 4/2/2013
                //                            errStr="Package is required in row "+dtIncr;
                //                            //errors.add(errorMsg, new ActionMessage(errorMsg, errStr));
                //                            errors.add("SI_PACKAGE_ERROR",new ActionMessage("error.SI.PACKAGE.Msg"));
                //                        }
                if(dtPkg.length() != 0)
                {
                if (pakListSize>0) { //comListObj
                isPackage=false;
                for (int i = 0; i < pakListSize; i++) {
                dtPkg=dtPkg.toLowerCase();
                strListObj="";
                strListObj = pacListObj.get(i).toString().toLowerCase();
                if (strListObj.contains(dtPkg)) {
                isPackage=true;
                }
                }
                if(!isPackage){
                errors.add("SI_PACKAGE_ERROR",new ActionMessage("error.SI.PACKAGEINVALID.Msg"));
                }
                }
                }
                /**    errorMsg="SI_CARGOWT_ERROR"+dtIncr;
                if (dtWt.length() == 0) {
                errStr="Cargo Weight is required in row "+dtIncr;
                errors.add("SI_CARGOWT_ERROR", new ActionMessage("error.SI.CARGOWT.Msg"));
                }

                else if (!Pattern.matches("^[0-9][0-9]{0,7}[\\.]{0,1}[0-9]{0,3}$", dtWt)) {
                errStr="Invalid Cargo Weight in row "+dtIncr;
                errors.add("SI_CARGOWT_ERROR", new ActionMessage("error.SI.CARGOWTINVALID.Msg"));
                }
                errorMsg="SI_CARGOQTY_ERROR"+dtIncr;
                if (dtQty.length() == 0) {
                errStr="Cargo Qty is required in row "+dtIncr;
                errors.add("SI_CARGOQTY_ERROR", new ActionMessage("error.SI.CARGOQTY.Msg"));
                }
                if (!Pattern.matches("^[1-9][0-9]{0,7}$", dtQty)) {
                errStr="Invalid Cargo Qty in row "+dtIncr;
                errors.add("SI_CARGOQTY_ERROR", new ActionMessage("error.SI.CARGOQTYINVALID.Msg"));
                }
                errorMsg="SI_CARGOVOL_ERROR"+dtIncr;
                //                        if (dtVol.length() == 0) { //Commented by Shikin PLARPENH-440 4/2/2013
                //                            errStr="Cargo Volume is required in row "+dtIncr;
                //                            errors.add("SI_CARGOVOL_ERROR", new ActionMessage("error.SI.CARGOVOL.Msg"));
                //                        }
                if(dtVol.length() != 0)
                {
                if (!Pattern.matches("^[1-9][0-9]{0,7}[\\.]{0,1}[0-9]{0,3}$", dtVol)) {
                errStr="Invalid Cargo Volume in row "+dtIncr;
                errors.add("SI_CARGOVOL_ERROR", new ActionMessage("error.SI.CARGOVOLINVALID.Msg"));
                }
                }
                errorMsg="SI_CARGOMRK_ERROR"+dtIncr;
                //                        if (dtMrk.length() == 0) { //Commented by Shikin PLARPENH-440 4/2/2013
                //                            errStr="Cargo Marks is required in row "+dtIncr;
                //                            errors.add("SI_CARGOMRK_ERROR", new ActionMessage("error.SI.CARGOMRK.Msg"));
                //                        }
                if (dtMrk.length() > 255){
                errStr="Invalid Cargo Marks in row "+dtIncr;
                errors.add("SI_CARGOMRK_ERROR", new ActionMessage("error.SI.CARGOMRKINVALID.Msg"));
                }
                errorMsg="SI_CARGODES_ERROR"+dtIncr;
                //                        if (dtDes.length() == 0) { //Commented by Shikin PLARPENH-440 4/2/2013
                //                            errStr="Cargo Description is required in row "+dtIncr;
                //                            errors.add("SI_CARGODES_ERROR", new ActionMessage("error.SI.CARGODES.Msg"));
                //                        }
                if (dtDes.length() > 3000){
                errStr="Invalid Cargo Description in row "+dtIncr;
                errors.add("SI_CARGODES_ERROR", new ActionMessage("error.SI.CARGODESINVALID.Msg"));
                }

                dtIncr=dtIncr+1;
                }
                }**/
                cntIncr = cntIncr + 1; // cntIncr for container increment
            }
        } else {
            //errors.add("SI_SELECT_ERROR", new ActionMessage("error.SI.SELECT.Msg"));
        }



        // Container master
      /*  String selectCnt[] = editForm.getSelect();
        String carrsealCnt[] = editForm.getCarrierseal();
        String shippersealCnt[] = editForm.getShipperseal();
        String othersealCnt[] = editForm.getOtherseal();
        String commodityCnt[] = editForm.getCommodity();
        String cargowtCnt[] = editForm.getCargowt();
        String unitCnt[] = editForm.getUnit();
        String qtyCnt[] = editForm.getItemqty();
        String listno ="",cont_num="";
        SISubmitVO submitVO=null;
        String strSeal="",strCarieer="",strComm="",strCargoWt="",strUnit="",strQty="",strCommId="";
        int iHypenIndex=0;
        List comListObj=(List)contexts.getAttribute("SESSION_COMMODITYLIST");

        if(selectCnt!=null && selectCnt.length>0){
        int selectLen=selectCnt.length;
        for (int i = 0; i < selectLen; i++) {
        strSeal=Validation.checkNull(shippersealCnt[i]);
        strCarieer=Validation.checkNull(carrsealCnt[i]);
        strComm=Validation.checkNull(commodityCnt[i]);
        strCargoWt=Validation.checkNull(cargowtCnt[i]);
        strUnit=Validation.checkNull(unitCnt[i]);
        strQty=Validation.checkNull(qtyCnt[i]);
        if(strSeal.length()==0){
        errors.add("SI_SHIPPERSEAL_ERROR",new ActionMessage("error.SI.SHIPPERSEAL.Msg"));
        }else if(!Pattern.matches("^[a-zA-Z-0-9]$",strSeal)){
        errors.add("SI_SHIPPERSEAL_ERROR",new ActionMessage("error.SI.SHIPPERSEALINVALID.Msg"));
        }
        if(strCarieer.length()==0){
        errors.add("SI_CARRIERSEAL_ERROR",new ActionMessage("error.SI.CARRIERSEAL.Msg"));
        }else if(!Pattern.matches("^[a-zA-Z-0-9]$",strCarieer)){
        errors.add("SI_CARRIERSEAL_ERROR",new ActionMessage("error.SI.CARRIERSEALINVALID.Msg"));
        }
        if(strComm.length()==0){
        errors.add("SI_COMMODITY_ERROR",new ActionMessage("error.SI.COMMODITY.Msg"));
        }else if(comListObj.contains(strComm)){
        errors.add("SI_COMMODITY_ERROR",new ActionMessage("error.SI.COMMODITYINVALID.Msg"));
        }
        if(strCargoWt.length()==0){
        errors.add("SI_CARGOWT_ERROR",new ActionMessage("error.SI.CARGOWT.Msg"));
        }else if(!Pattern.matches("^[0-9]{1,7}[\\.][0-9]{0,2}$",strCargoWt)){
        errors.add("SI_CARGOWT_ERROR",new ActionMessage("error.SI.CARGOWTINVALID.Msg"));
        }
        if(strUnit.length()==0){
        errors.add("SI_CARGOUNIT_ERROR",new ActionMessage("error.SI.CARGOUNIT.Msg"));
        }else if(!Pattern.matches("^[a-z-A-Z]$",strUnit)){
        errors.add("SI_CARGOUNIT_ERROR",new ActionMessage("error.SI.CARGOUNITINVALID.Msg"));
        }
        if(strQty.length()==0){
        errors.add("SI_CARGOQTY_ERROR",new ActionMessage("error.SI.CARGOQTY.Msg"));
        }else if(!Pattern.matches("^[0-9]{1,7}[\\.][0-9]{0,2}$",strQty)){
        errors.add("SI_CARGOQTY_ERROR",new ActionMessage("error.SI.CARGOQTYINVALID.Msg"));
        }
        /*listno = select[i];
        submitVO = new SISubmitVO();
        int listnum = Integer.parseInt(listno);
        SIContainerVO contVO = (SIContainerVO) cList.get(listnum);
        submitVO.setContainerno(contVO.getContainerno());
        cont_num = Validation.checkNull(submitVO.getContainerno());*
        }
        }else{
        errors.add("SI_SELECT_ERROR",new ActionMessage("error.SI.SELECT.Msg"));
        }
         */
        return errors;
    }
}
