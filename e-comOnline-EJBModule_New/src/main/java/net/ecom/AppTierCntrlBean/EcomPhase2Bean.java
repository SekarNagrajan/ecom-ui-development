/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.json.SvmJsonList;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.inject.Inject;
import net.ecom.phase2.service.vo.ARNSummaryVO;
import net.ecom.phase2.service.vo.AccountSummaryVO;
import net.ecom.phase2.service.vo.AmountSummaryVO;
import net.ecom.phase2.service.vo.CROSummaryVO;
import net.ecom.phase2.service.vo.CSInvoiceSummaryVO;
import net.ecom.phase2.service.vo.DOStatusUpdateVO;
import net.ecom.phase2.service.vo.DOSummaryVO;
import net.ecom.phase2.service.vo.OutstandingSummaryVO;
import net.ecom.phase2.service.vo.PaymentHistoryVO;
import net.ecom.phase2.service.vo.PaymentSummaryVO;
import net.ecom.phase2.service.vo.Phase2Model;
import net.ecom.phase2.service.vo.SurchargeSummaryVO;
import net.ecom.phase2.service.vo.TariffSummaryVO;
import net.ecom.surcharge.RateContractBean;
import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vds.service.EcomPhase2Service;
import net.ecom.vds.serviceVo.Routedetail;
import net.ecom.vds.serviceVo.RoutingVo;
import net.ecom.vo.ContractVo;
import net.ecom.vo.CustomerProfile;
import net.ecom.vo.NewCustomerstatementvo;
import net.ecom.vo.OnGoingTransactionVo;
import net.ecom.vo.RatesContractsVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.stream.Collectors;
import net.ecom.common.GeneralConf;


/**
 *
 * @author deepa.p
 */
@Stateless(name = "EcomPhase2Bean")
@Remote(EcomPhase2BeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS"),
    @Resource(name = "jdbc/ReportDB", mappedName = "java:/REPORTDS"),
    @Resource(name = "jdbc/COMDB", mappedName = "java:/COMDS")
})
public class EcomPhase2Bean implements EcomPhase2BeanRemote {

    protected static final String customerstatementsp = "exec usp_CustomerStatement ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";


    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Inject
    VDSInvoker vds;

    EcomPhase2Service phase2VdsService;

    @PostConstruct
    public void init() {
        phase2VdsService = vds.create(EcomPhase2Service.class);
    }

    @Override
    public List<CROSummaryVO> getCROSummaryDetails(String custCode, String fromDate, String toDate, String bookNo, String releaseNo) throws RemoteException {
        List<CROSummaryVO> croList = null;
        SvmJsonList alist = null;
        Gson gson = new Gson();
        try {
            alist = new SvmJsonList();
            alist.add(custCode);
            alist.add(fromDate);
            alist.add(toDate);
            alist.add(bookNo);
            alist.add(releaseNo);
            Map croMap = phase2VdsService.getEcomCROSummaryDetails(alist);
            //System.out.println("[ECOM] EcomPhase2Bean >>>croMap>>>" + croMap);
            if (croMap != null) {
                croMap = (Map) croMap.get("rs");
                if (croMap != null) {
                    croList = gson.fromJson(gson.toJson((ArrayList) croMap.get("rs0")), new TypeToken<List<CROSummaryVO>>() {
                    }.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return croList;
    }

    @Override
    public List<CROSummaryVO> getCRODetails(String bookNo, String releaseNo, String eqpType) throws RemoteException {
        List<CROSummaryVO> croList = null;
        Map resultMap = null;
        Gson gson = new Gson();
        try {
            if (checkNull(releaseNo).length() > 0) {
                resultMap = phase2VdsService.getEcomCRODetails(bookNo, eqpType, releaseNo);
            } else {
                resultMap = phase2VdsService.getEcomCROBKGDetails(bookNo, eqpType);
            }
            //System.out.println("[ECOM] EcomPhase2Bean >>resultMap>>" + resultMap);

            if (resultMap != null) {
                croList = gson.fromJson(gson.toJson((ArrayList) resultMap.get("plotdomaster")), new TypeToken<List<CROSummaryVO>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return croList;
    }

    @Override
    public List<ARNSummaryVO> getARNSummary(String custCode, String fromDate, String toDate) throws RemoteException {
        List<ARNSummaryVO> arnSummaryList = null;
        Map resultMap = null;
        Gson gson = new Gson();
        try {
            fromDate = fromDate + "," + toDate;
            resultMap = phase2VdsService.getEcomARNSummaryDetails(custCode, fromDate);
            //System.out.println("[ECOM] EcomPhase2Bean >>.result arn map>>" + resultMap);
            if (resultMap != null) {
                arnSummaryList = gson.fromJson(gson.toJson((ArrayList) resultMap.get("reportcargoarrival")), new TypeToken<List<ARNSummaryVO>>() {
                }.getType());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arnSummaryList;
    }

    @Override
    public List<DOSummaryVO> getDOSummary(String custCode, String fromDate, String toDate) throws RemoteException {
        List<DOSummaryVO> doSummaryList = null;
        Map resultMap = null;
        Gson gson = new Gson();
        try {
            fromDate = fromDate + "," + toDate;
            resultMap = phase2VdsService.getEcomDOSummaryDetails(custCode, fromDate);
            //System.out.println("[ECOM] EcomPhase2Bean >>.result arn map>>" + resultMap);
            if (resultMap != null) {
                doSummaryList = gson.fromJson(gson.toJson((ArrayList) resultMap.get("delordmaster")), new TypeToken<List<DOSummaryVO>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doSummaryList;
    }

    @Override
    public DOStatusUpdateVO updateDOStatus(String delOrdNo) throws RemoteException {
        DOStatusUpdateVO updateVO = null;
        Map resultMap = null, map = null;
        Phase2Model model = null;
        List<DOStatusUpdateVO> doStatusList = null;
        List list = null;
        String printStatus = null, ecomPrintStatus = null;
        try {
            resultMap = phase2VdsService.getEcomDOPrintStatus(delOrdNo);
            if (resultMap != null) {
                list = (List) resultMap.get("delordmaster");
                if (list != null && list.size() > 0) {
                    map = (Map) list.get(0);
                    if (map != null) {
                        printStatus = (String) map.get("printstatus");
                        ecomPrintStatus = (String) map.get("ecomprintstatus");
                    }
                }
            }
            updateVO = new DOStatusUpdateVO();
            updateVO.setChgusr("ECOM");
            updateVO.setDelordno(delOrdNo);
            //updateVO.setPrintstatus(printStatus);
            updateVO.setEcomprintstatus(ecomPrintStatus);
            if ("N".equalsIgnoreCase(printStatus) && "1".equalsIgnoreCase(ecomPrintStatus)) {
                doStatusList = new ArrayList<DOStatusUpdateVO>();
                doStatusList.add(updateVO);
                model = new Phase2Model();
                model.setDoStatusList(doStatusList);
                phase2VdsService.putEcomDOStatus(model);
                phase2VdsService.putEcomDOHouseBL(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateVO;
    }

    @Override
    public List<TariffSummaryVO> getTarriffSummary(String origin, String delivery, String commoditycode, String eqpType, String shipmentDate, String carriercode, String ratesplugin, String showExternalRateTotalAmount, String checkRouting) throws RemoteException {
        List<TariffSummaryVO> tariffSummaryList = null, tariffSummaryList1 = null, resultList = null, tariffSummaryList2 = null, tariffSummaryList3 = null;
        Map resultMap = null, resultMap1 = null, resultMap2 = null, resultMap3 = null, map = null;
        String pol = "", pod = "", rateType = "";
        SvmJsonList alist = null;
        Gson gson = new Gson();
        try {
            pol = origin;
            pod = delivery;
            rateType = "T";
            commoditycode = checkNull(commoditycode);
            eqpType = checkNull(eqpType);
            RoutingVo hs = null;
            if ("Y".equalsIgnoreCase(checkRouting)) {
                String defRoute = "Y";
                hs = (RoutingVo) phase2VdsService.getecomroutinginformation(origin, delivery, defRoute, shipmentDate, shipmentDate);
                List lst = hs.getRoutedetail();
                if (lst != null && lst.size() > 0) {
                    Routedetail rst = (Routedetail) lst.get(0);
                    pol = rst.getBlpol();
                    pod = rst.getBlpod();

                }
            }
            alist = new SvmJsonList();
            alist.add(pol);
            alist.add(pod);
            alist.add(eqpType);
            alist.add(commoditycode);
            alist.add(shipmentDate);
            alist.add("");
            alist.add(rateType);
            if (commoditycode.length() > 0 && eqpType.length() > 0) {

                if ("N".equalsIgnoreCase(ratesplugin)) {
                    resultMap2 = phase2VdsService.getEcomRateTarriffEqpComdty(alist);
                    //resultMap2 = phase2VdsService.getEcomTariffDetComEqp_RR(pol, pod, commoditycode, eqpType, shipmentDate, shipmentDate);
                    resultMap3 = phase2VdsService.getEcomBBLCLTariffDetComEqp_RR(pol, pod, commoditycode, eqpType, shipmentDate, shipmentDate);
                } else {
                    resultMap = phase2VdsService.getEcomTariffDetComEqp(pol, pod, commoditycode, eqpType, shipmentDate, shipmentDate);
                    resultMap1 = phase2VdsService.getEcomBBLCLTariffDetComEqp(pol, pod, commoditycode, eqpType, shipmentDate, shipmentDate);
                }

            } else if (commoditycode.length() > 0) {

                if ("N".equalsIgnoreCase(ratesplugin)) {
                    resultMap2 = phase2VdsService.getEcomRateTarriffEqpComdty(alist);
                    //resultMap2 = phase2VdsService.getEcomTariffDetCom_RR(pol, pod, commoditycode, shipmentDate, shipmentDate);
                    resultMap3 = phase2VdsService.getEcomBBLCLTariffDetCom_RR(pol, pod, commoditycode, shipmentDate, shipmentDate);
                } else {
                    resultMap = phase2VdsService.getEcomTariffDetCom(pol, pod, commoditycode, shipmentDate, shipmentDate);
                    resultMap1 = phase2VdsService.getEcomBBLCLTariffDetCom(pol, pod, commoditycode, shipmentDate, shipmentDate);
                }

            } else if (eqpType.length() > 0) {

                if ("N".equalsIgnoreCase(ratesplugin)) {
                    resultMap2 = phase2VdsService.getEcomRateTarriffEqpComdty(alist);
                    //resultMap2 = phase2VdsService.getEcomTariffDetEqp_RR(pol, pod, eqpType, shipmentDate, shipmentDate);
                    resultMap3 = phase2VdsService.getEcomBBLCLTariffDetEqp_RR(pol, pod, eqpType, shipmentDate, shipmentDate);
                } else {
                    resultMap = phase2VdsService.getEcomTariffDetEqp(pol, pod, eqpType, shipmentDate, shipmentDate);
                    resultMap1 = phase2VdsService.getEcomBBLCLTariffDetEqp(pol, pod, eqpType, shipmentDate, shipmentDate);
                }

            } else {
                if ("N".equalsIgnoreCase(ratesplugin)) {
                    resultMap2 = phase2VdsService.getEcomRateTarriffEqpComdty(alist);
                    //resultMap2 = phase2VdsService.getEcomTariffDetails_RR(pol, pod, shipmentDate, shipmentDate);
                    resultMap3 = phase2VdsService.getEcomBBLCLTariffDetails_RR(pol, pod, shipmentDate, shipmentDate);
                } else {
                    resultMap = phase2VdsService.getEcomTariffDetails(pol, pod, shipmentDate, shipmentDate);
                    resultMap1 = phase2VdsService.getEcomBBLCLTariffDetails(pol, pod, shipmentDate, shipmentDate);
                }
            }
            //System.out.println("[ECOM] EcomPhase2Bean >>.result phase2 tariff map>>" + resultMap);
            if (resultMap != null) {
                tariffSummaryList = gson.fromJson(gson.toJson((ArrayList) resultMap.get("bookingrates_tariff")), new TypeToken<List<TariffSummaryVO>>() {
                }.getType());
                System.out.println("[ECOM] EcomPhase2Bean tariffSummaryList>>" + tariffSummaryList); 
               System.out.println("[ECOM] EcomPhase2Bean showExternalRateTotalAmount>>" + showExternalRateTotalAmount);
                if ("Y".equalsIgnoreCase(showExternalRateTotalAmount)) {
                if (tariffSummaryList != null) {
                    tariffSummaryList.removeIf(tariff -> tariff.getExternalrate() == null || tariff.getExternalrate().isEmpty() || "0".equals(tariff.getExternalrate()) || "0.0".equals(tariff.getExternalrate()) || "0.00".equals(tariff.getExternalrate()));
                }}
            }

            if (resultMap1 != null) {
                tariffSummaryList1 = gson.fromJson(gson.toJson((ArrayList) resultMap1.get("bookingrates_lcltariff")), new TypeToken<List<TariffSummaryVO>>() {
                }.getType());
            }
            /*if (resultMap2 != null) {
                tariffSummaryList2 = gson.fromJson(gson.toJson((ArrayList) resultMap2.get("bookingrates")), new TypeToken<List<TariffSummaryVO>>() {
                }.getType());
            }*/

            if (resultMap2 != null) {
                map = (Map) resultMap2.get("rs");
                if (map != null) {
                    tariffSummaryList2 = gson.fromJson(gson.toJson((ArrayList) map.get("rs0")), new TypeToken<List<TariffSummaryVO>>() {
                    }.getType());

                }
            }

            if (resultMap3 != null) {
                tariffSummaryList3 = gson.fromJson(gson.toJson((ArrayList) resultMap3.get("bookingrates_roro")), new TypeToken<List<TariffSummaryVO>>() {
                }.getType());
            }

            if ("N".equalsIgnoreCase(ratesplugin)) {
                resultList = Stream.concat(tariffSummaryList2.stream(), tariffSummaryList3.stream()).collect(Collectors.toList());
            } else {
                resultList = Stream.concat(tariffSummaryList.stream(), tariffSummaryList1.stream()).collect(Collectors.toList());
            }

//            System.out.println("[ECOM] EcomPhase2Bean tarifflist"+ resultList);
//            resultList.forEach(rsl-> {
//            rsl.setOriginPort(origin);
//            rsl.setDeliveryPort(delivery); 
//            System.out.println("[ECOM] EcomPhase2Bean originvaluetariff"+origin);
//          });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<SurchargeSummaryVO> getSurchargeSummary(String loadPort, String dischargePort, String eqpType, String shipmentDate) throws RemoteException {

        List<SurchargeSummaryVO> surchargeList = null;
        List eqpList = null;
        SvmJsonList alist = null;
        Gson gson = new Gson();
        Map resultMap = null, map = null;
        String type = null;
        String strChargeCode = null;
        //String formula = null;
        Float fAmount = 0f;
        Float fAmountInUsd = 0f;
        String equ_type = null;
        SurchargeSummaryVO summaryVo = null;
        HashMap hmAlreadyCharge = new HashMap();
        try {

            alist = new SvmJsonList();
            alist.add(loadPort);
            alist.add(dischargePort);
            alist.add(eqpType);
            alist.add("");
            alist.add(shipmentDate);
            resultMap = phase2VdsService.getEcomSurchargeEqpType(eqpType);
           // System.out.println("[ECOM] EcomPhase2Bean eqptype result map>>>" + resultMap);
            if (resultMap != null) {
                eqpList = (List) resultMap.get("ecom_equipments");
                if (eqpList != null && eqpList.size() > 0) {
                    resultMap = (Map) eqpList.get(0);
                    type = checkNull((String) resultMap.get("type"));
                }
            }
            resultMap = null;
            if (type != null && type.length() > 0) {
                resultMap = phase2VdsService.getEcomSurchargeDetails(alist);
            } else {
                resultMap = phase2VdsService.getEcomBBLCLSurchargeDetails(alist);
            }
            //System.out.println("[ECOM] EcomPhase2Bean >>>resultMap surcharge>>>" + resultMap);
            if (resultMap != null) {
                map = (Map) resultMap.get("rs");
                if (map != null) {
                    surchargeList = gson.fromJson(gson.toJson((ArrayList) map.get("rs0")), new TypeToken<List<SurchargeSummaryVO>>() {
                    }.getType());
                }
            }
            if (surchargeList != null) {
                hmAlreadyCharge.put("OFR", null);
                hmAlreadyCharge.put("OOG", null);
                hmAlreadyCharge.put("OWC", null);
                hmAlreadyCharge.put("FAC", null);
                for (int i = 0; i < surchargeList.size(); i++) {
                    summaryVo = surchargeList.get(i);
                    summaryVo.setDischargeRegion(dischargePort);
                    summaryVo.setLoadRegion(loadPort);
                    strChargeCode = checkNull(summaryVo.getChargecode());
                    if (!(hmAlreadyCharge.containsKey(strChargeCode))) {
                        if ("A".equalsIgnoreCase(summaryVo.getAmttype())) {

                            equ_type = checkNull(summaryVo.getEqptype());
                            if (eqpType.startsWith("40") && (equ_type.equalsIgnoreCase("TEUS"))) {
                                fAmount = 2 * Float.parseFloat(summaryVo.getAmount());
                                fAmountInUsd = 2 * Float.parseFloat(summaryVo.getAmountinusd());
                            } else {
                                fAmount = Float.parseFloat(summaryVo.getAmount());
                                fAmountInUsd = Float.parseFloat(summaryVo.getAmountinusd());
                            }
                            summaryVo.setAmount(String.valueOf(fAmount));
                            if ("LP".equalsIgnoreCase(equ_type) || "TEUS".equalsIgnoreCase(equ_type)) {
                                equ_type = eqpType;
                            }
                            summaryVo.setEqptype(equ_type);
                            summaryVo.setDischargeRegion(dischargePort);
                            summaryVo.setLoadRegion(loadPort);
                            surchargeList.set(i, summaryVo);
                        }//                    else {
//                        if ("USD".equalsIgnoreCase(rs.getString("currency"))) {
//                            formula = rs.getString("formula");
//                            Float pcntg = CalculatePercentage(formula);
//                            if (pcntg < 100 && pcntg > 0) {
//                                summaryVo.setCurrency(checkNull(rs.getString("currency")));
//                                summaryVo.setAmount(String.valueOf(pcntg));
//                                summaryVo.setChargeName(checkNull(rs.getString("chargeName")));
//                                summaryVo.setChargeCode(checkNull(strChargeCode));
//                                summaryVo.setEqpType(checkNull(rs.getString("eqpType")));
//                                summaryVo.setDischargeRegion(dischargePort);
//                                summaryVo.setLoadRegion(loadPort);
//                            }
//                        }
//                    }

                        hmAlreadyCharge.put(strChargeCode, null);
                    }
                }

            }
            //System.out.println("[ECOM] EcomPhase2Bean >>surchargeList>>" + surchargeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return surchargeList;
    }

    @Override
    public List<SurchargeSummaryVO> getSurchargeSummaryNew(String loadPort, String dischargePort, String eqpType, String shipmentDate) throws RemoteException {
        List<SurchargeSummaryVO> surchargeList = null;
        List eqpList = null;
        List typeList = null;
        SvmJsonList alist = null;
        Gson gson = new Gson();
        Map resultMap = null, map = null;
        String type = null;
        String surExpType = null;
        String strChargeCode = null;
        String nor = null;
        String chargeCode = null;
        List reslis = null;
        //String formula = null;
        Float fAmount = 0f;
        Float fAmountInUsd = 0f;
        String equ_type = null;
        SurchargeSummaryVO summaryVo = null;
        HashMap hmAlreadyCharge = new HashMap();
        try {
            alist = new SvmJsonList();
            alist.add(loadPort);
            alist.add(dischargePort);
            alist.add(eqpType);
            alist.add(shipmentDate);

            //alist.add(shipmentDateto);
            resultMap = phase2VdsService.getEcomSurchargeEqpType(eqpType);
            //System.out.println("[ECOM] EcomPhase2Bean eqptype result map>>>" + resultMap);
            if (resultMap != null) {
                eqpList = (List) resultMap.get("ecom_equipments");
                if (eqpList != null && eqpList.size() > 0) {
                    resultMap = (Map) eqpList.get(0);
                    type = checkNull((String) resultMap.get("type"));

                }
            }
            //   
            resultMap = null;
            if (type != null && type.length() > 0) {
                resultMap = phase2VdsService.getecomsurchargefromflat1new(loadPort, dischargePort, eqpType, shipmentDate, shipmentDate);
            }
            //           
            if (resultMap != null) {
                surchargeList = gson.fromJson(gson.toJson((ArrayList) resultMap.get("surchargeflat")), new TypeToken<List<SurchargeSummaryVO>>() {
                }.getType());
            }
            if (surchargeList != null) {
                hmAlreadyCharge.put("OFR", null);
                hmAlreadyCharge.put("OOG", null);
                hmAlreadyCharge.put("OWC", null);
                hmAlreadyCharge.put("FAC", null);
                for (int i = 0; i < surchargeList.size(); i++) {
                    summaryVo = surchargeList.get(i);
                    summaryVo.setDischargeRegion(dischargePort);
                    summaryVo.setLoadRegion(loadPort);
                    summaryVo.setDelivery(dischargePort);
                    summaryVo.setOrigin(loadPort);
                    //
                    surExpType = summaryVo.getSurExcType();
                    if ("DE".equalsIgnoreCase(surExpType)
                            || "GDE".equalsIgnoreCase(surExpType)
                            || "XDE".equalsIgnoreCase(surExpType)) {
                        summaryVo.setDelivery(summaryVo.getCode());

                    } else {
                        summaryVo.setOrigin(summaryVo.getCode());
                    }
                    //
                    strChargeCode = checkNull(summaryVo.getChargecode());
                    if (!(hmAlreadyCharge.containsKey(strChargeCode))) {
                        if ("A".equalsIgnoreCase(summaryVo.getAmttype())) {

                            equ_type = checkNull(summaryVo.getEqptype());
                            if (eqpType.startsWith("40") && (equ_type.equalsIgnoreCase("TEUS"))) {
                                fAmount = 2 * Float.parseFloat(summaryVo.getAmount());
                                fAmountInUsd = 2 * Float.parseFloat(summaryVo.getAmountinusd());
                            } else {
                                fAmount = Float.parseFloat(summaryVo.getAmount());
                                fAmountInUsd = Float.parseFloat(summaryVo.getAmountinusd());
                            }
                            summaryVo.setAmount(String.valueOf(fAmount));
                            if ("LP".equalsIgnoreCase(equ_type) || "TEUS".equalsIgnoreCase(equ_type)) {
                                equ_type = eqpType;
                            }
                            summaryVo.setEqptype(equ_type);
                            summaryVo.setDischargeRegion(dischargePort);
                            summaryVo.setLoadRegion(loadPort);
                            summaryVo.setCurrency(type);
                            summaryVo.setNor(nor);
                            summaryVo.setChargecode(chargeCode);
                            surchargeList.set(i, summaryVo);
                        }

                        hmAlreadyCharge.put(strChargeCode, null);
                    }
                }

            }
            //System.out.println("[ECOM] EcomPhase2Bean >>surchargeList>>" + surchargeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return surchargeList;
    }

    @Override
    public Map<String, List> getAccountSummary(String strType, String custCode,
            String fromDate, String toDate,
            String option) throws RemoteException {
        Map<String, List> accountMap = new HashMap<String, List>();
        List<AccountSummaryVO> accountsummaryList = null;
        List<AmountSummaryVO> amountSummaryList = null;
        List<Map> amountCal = null;
        SvmJsonList alist = null;
        Gson gson = new Gson();
        boolean flag = true;
        String strReceiptNo = "", paymentMode = null;
        String strPaymentMode = "", newReceiptNo = null;
        Map resultMap = null, map = null;
        try {
            alist = new SvmJsonList();
            alist.add(strType);
            alist.add(checkNull(custCode));
            alist.add(fromDate);
            alist.add(toDate);
            alist.add(option);
            resultMap = phase2VdsService.getEcomCustomerStatementView(alist);
           // System.out.println("[ECOM] EcomPhase2Bean acc summary map>>" + resultMap);
            if (resultMap != null) {
                map = (Map) resultMap.get("rs");
                if (map != null) {
                    accountsummaryList = gson.fromJson(gson.toJson((ArrayList) map.get("rs0")), new TypeToken<List<AccountSummaryVO>>() {
                    }.getType());

                    amountSummaryList = gson.fromJson(gson.toJson((ArrayList) map.get("rs1")), new TypeToken<List<AmountSummaryVO>>() {
                    }.getType());

                    amountCal = (List<Map>) map.get("rs2");
                    if (amountCal != null && amountCal.size() > 0) {
                        for (Map locMap : amountCal) {

                            strReceiptNo = (String) locMap.get("receiptno");
                            paymentMode = (String) locMap.get("paymentmode");
                            if (flag) {
                                newReceiptNo = strReceiptNo;
                                flag = false;
                            }
                            if (newReceiptNo.equals(strReceiptNo)) {
                                strPaymentMode = strPaymentMode + "," + (String) locMap.get("paymentmode");
                            } else {
                                setAccPaymentMode(newReceiptNo, strPaymentMode, accountsummaryList);
                                strPaymentMode = paymentMode;
                                newReceiptNo = strReceiptNo;
                            }

                        }
                    }
                    setAccPaymentMode(newReceiptNo, strPaymentMode, accountsummaryList);
                }
                accountMap.put("AccountSummary", accountsummaryList);
                accountMap.put("AmountSummary", amountSummaryList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountMap;
    }

    @Override
    public Map<String, List> getNewAccountSummaryFromCCR(String fromDate, String custCode, String carrierCode) throws RemoteException {
        Map<String, List> accountMap = new HashMap();
        List accountsummaryList = null;
        List accountsummaryListResult = new ArrayList();
        SvmJsonList alist = null;
        AccountSummaryVO accSummaryvoObj;
        Map resultMap = null, map = null;
        try {
            System.out.println("[ECOM] EcomPhase2Bean carriercodevalue" + carrierCode);
            alist = new SvmJsonList();
            alist.add(fromDate);
            alist.add(custCode);
            alist.add(carrierCode);
            alist.add("");
            alist.add("");
            resultMap = phase2VdsService.getEcomCustStmtNew(alist);
           // System.out.println("[ECOM] EcomPhase2Bean acc summary map from CCR>>" + resultMap);
            if (resultMap != null) {
                map = (Map) resultMap.get("rs");
               // System.out.println("[ECOM] EcomPhase2Bean ejbmapvalue" + map);
                if (map != null) {
                    accountsummaryList = (List) map.get("rs0");
                    if (accountsummaryList != null && accountsummaryList.size() > 0) {
                        for (int i = 0; i < accountsummaryList.size(); i++) {
                            accSummaryvoObj = new AccountSummaryVO();
                            Map rs = (Map) accountsummaryList.get(i);
                            accSummaryvoObj.setBlNo(checkNull((String) rs.get("blno")));
                            accSummaryvoObj.setInvoiceDate(checkNull((String) rs.get("")));
                            accSummaryvoObj.setInvoiceNo(checkNull((String) rs.get("invno")));
                            accSummaryvoObj.setInvCurrency(checkNull((String) rs.get("currency")));
                            accSummaryvoObj.setInvAmount(checkNull((String) rs.get("blamount")));
                            accSummaryvoObj.setInvROE("1.00");
                            accSummaryvoObj.setInvAmountUSD(checkNull((String) rs.get("blamount")));
                            accSummaryvoObj.setReceiptNo(checkNull((String) rs.get("")));
                            accSummaryvoObj.setPaidAmtUSD(checkNull((String) rs.get("receivedamt")));
                            accSummaryvoObj.setPaymentMode(checkNull((String) rs.get("")));
                            accSummaryvoObj.setExcessAmtUSD(checkNull((String) rs.get("")));
                            accSummaryvoObj.setOutStandingAmtUSD(checkNull((String) rs.get("blbalamount")));
                            accountsummaryListResult.add(accSummaryvoObj);
                        }
                    }
                }
                accountMap.put("AccountSummary", accountsummaryListResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountMap;
    }

    @Override
    public Map<String, List> getInvoiceDetails(String strType, String custCode,
            String fromDate, String toDate,
            String option) throws RemoteException {
        Map<String, List> invMap = new HashMap<String, List>();
        List<CSInvoiceSummaryVO> invoicesummaryList = null;
        List<AmountSummaryVO> amtsummaryList = null;
        Map resultMap = null, map = null;
        SvmJsonList alist = null;
        Gson gson = new Gson();
        try {
            alist = new SvmJsonList();
            alist.add(strType);
            alist.add(checkNull(custCode));
            alist.add(fromDate);
            alist.add(toDate);
            alist.add(option);
            resultMap = phase2VdsService.getEcomCustomerStatementView(alist);
            if (resultMap != null) {
                map = (Map) resultMap.get("rs");
                if (map != null) {
                    invoicesummaryList = gson.fromJson(gson.toJson((ArrayList) map.get("rs0")), new TypeToken<List<CSInvoiceSummaryVO>>() {
                    }.getType());

                    amtsummaryList = gson.fromJson(gson.toJson((ArrayList) map.get("rs1")), new TypeToken<List<AmountSummaryVO>>() {
                    }.getType());
                }
            }
            invMap.put("InvoiceSummary", invoicesummaryList);
            invMap.put("AmountSummary", amtsummaryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invMap;
    }

    @Override
    public Map<String, List> getPaymentDetials(String strType, String custCode,
            String fromDate, String toDate,
            String option) throws RemoteException {
        Map<String, List> payMap = new HashMap<String, List>();
        List<PaymentSummaryVO> paymentsummaryList = null;
        List<AmountSummaryVO> amtsummaryList = null;
        Map resultMap = null, map = null;
        SvmJsonList alist = null;
        Gson gson = new Gson();
        List<Map> amountCal = null;
        boolean flag = true;
        String strReceiptNo = "", paymentMode = null;
        String strPaymentMode = "", newReceiptNo = null;
        try {
            alist = new SvmJsonList();
            alist.add(strType);
            alist.add(checkNull(custCode));
            alist.add(fromDate);
            alist.add(toDate);
            alist.add(option);
            resultMap = phase2VdsService.getEcomCustomerStatementView(alist);
            if (resultMap != null) {
                map = (Map) resultMap.get("rs");
                if (map != null) {
                    paymentsummaryList = gson.fromJson(gson.toJson((ArrayList) map.get("rs0")), new TypeToken<List<PaymentSummaryVO>>() {
                    }.getType());

                    amtsummaryList = gson.fromJson(gson.toJson((ArrayList) map.get("rs1")), new TypeToken<List<AmountSummaryVO>>() {
                    }.getType());
                    amountCal = (List<Map>) map.get("rs2");
                    if (amountCal != null && amountCal.size() > 0) {
                        for (Map locMap : amountCal) {

                            strReceiptNo = (String) locMap.get("receiptno");
                            paymentMode = (String) locMap.get("paymentmode");
                            if (flag) {
                                newReceiptNo = strReceiptNo;
                                flag = false;
                            }
                            if (newReceiptNo.equals(strReceiptNo)) {
                                strPaymentMode = strPaymentMode + "," + (String) locMap.get("paymentmode");
                            } else {
                                setPayPaymentMode(newReceiptNo, strPaymentMode, paymentsummaryList);
                                strPaymentMode = paymentMode;
                                newReceiptNo = strReceiptNo;
                            }

                        }
                    }
                    setPayPaymentMode(newReceiptNo, strPaymentMode, paymentsummaryList);
                }
            }
            payMap.put("PaymentSummary", paymentsummaryList);
            payMap.put("AmountSummary", amtsummaryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payMap;
    }

    @Override
    public Map<String, List> getOutstandingDetails(String strType, String custCode,
            String fromDate, String toDate,
            String option) throws RemoteException {
        Map<String, List> outMap = new HashMap<String, List>();
        List<OutstandingSummaryVO> outstandingsummaryList = null;
        List<AmountSummaryVO> amtsummaryList = null;
        Map resultMap = null, map = null;
        SvmJsonList alist = null;
        Gson gson = new Gson();
        List<Map> amountCal = null;
        boolean flag = true;
        String strReceiptNo = "", paymentMode = null;
        String strPaymentMode = "", newReceiptNo = null, totalOutbal = null;
        try {
            alist = new SvmJsonList();
            alist.add(strType);
            alist.add(checkNull(custCode));
            alist.add(fromDate);
            alist.add(toDate);
            alist.add(option);
            resultMap = phase2VdsService.getEcomCustomerStatementView(alist);
            if (resultMap != null) {
                map = (Map) resultMap.get("rs");
                if (map != null) {
                    outstandingsummaryList = gson.fromJson(gson.toJson((ArrayList) map.get("rs0")), new TypeToken<List<OutstandingSummaryVO>>() {
                    }.getType());

                    amtsummaryList = gson.fromJson(gson.toJson((ArrayList) map.get("rs1")), new TypeToken<List<AmountSummaryVO>>() {
                    }.getType());

                    amountCal = (List<Map>) map.get("rs2");
                    if (amountCal != null && amountCal.size() > 0) {
                        for (Map locMap : amountCal) {

                            strReceiptNo = (String) locMap.get("receiptno");
                            paymentMode = (String) locMap.get("paymentmode");
                            if (flag) {
                                newReceiptNo = strReceiptNo;
                                flag = false;
                            }
                            if (newReceiptNo.equals(strReceiptNo)) {
                                strPaymentMode = strPaymentMode + "," + (String) locMap.get("paymentmode");
                            } else {
                                setOutPaymentMode(newReceiptNo, strPaymentMode, outstandingsummaryList);
                                strPaymentMode = paymentMode;
                                newReceiptNo = strReceiptNo;
                            }

                        }
                    }
                    setOutPaymentMode(newReceiptNo, strPaymentMode, outstandingsummaryList);
                    amountCal = (List<Map>) map.get("rs3");
                    if (amountCal != null && amountCal.size() > 0) {
                        outMap.put("totalOutstandingBal", (List) amountCal.get(0).values().stream().collect(Collectors.toList()));
                      //  System.out.println("[ECOM] EcomPhase2Bean totalOutstandingBal=============> " + outMap.get("totalOutstandingBal"));
                    }
                }
            }
            outMap.put("OutstandingSummary", outstandingsummaryList);
            outMap.put("AmountSummary", amtsummaryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outMap;

    }

    private void setAccPaymentMode(String newReceiptNo, String strPaymentMode, List<AccountSummaryVO> accountsummaryList) {
        int paymentlen = 0;
        AccountSummaryVO accSummaryVO = null;
        try {
            if (strPaymentMode != null && strPaymentMode.length() > 0) {
                paymentlen = strPaymentMode.length();
                String chare = strPaymentMode.trim().substring(0, 1);
                if (chare.equals(",")) {
                    strPaymentMode = strPaymentMode.trim().substring(1, paymentlen);
                }
            }
            for (int i = 0; i < accountsummaryList.size(); i++) {
                accSummaryVO = (AccountSummaryVO) accountsummaryList.get(i);
                if (newReceiptNo.equals(accSummaryVO.getReceiptNo())) {
                    accSummaryVO.setPaymentMode(strPaymentMode);

                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomPhase2Bean Error::" + e.getMessage());
        }
    }

    private void setPayPaymentMode(String newReceiptNo, String strPaymentMode, List<PaymentSummaryVO> paymentsummaryList) {
        int paymentlen = 0;
        PaymentSummaryVO paySummaryVO = null;
        try {
            if (strPaymentMode != null && strPaymentMode.length() > 0) {
                paymentlen = strPaymentMode.length();
                String chare = strPaymentMode.trim().substring(0, 1);
                if (chare.equals(",")) {
                    strPaymentMode = strPaymentMode.trim().substring(1, paymentlen);
                }
            }
            for (int i = 0; i < paymentsummaryList.size(); i++) {
                paySummaryVO = (PaymentSummaryVO) paymentsummaryList.get(i);
                if (newReceiptNo.equals(paySummaryVO.getReceiptNo())) {
                    paySummaryVO.setPaymentMode(strPaymentMode);

                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomPhase2Bean Error::" + e.getMessage());
        }
    }

    private void setOutPaymentMode(String newReceiptNo, String strPaymentMode, List<OutstandingSummaryVO> outstandingsummaryList) {
        int paymentlen = 0;
        OutstandingSummaryVO outSummaryVO = null;
        try {
            if (strPaymentMode != null && strPaymentMode.length() > 0) {
                paymentlen = strPaymentMode.length();
                String chare = strPaymentMode.trim().substring(0, 1);
                if (chare.equals(",")) {
                    strPaymentMode = strPaymentMode.trim().substring(1, paymentlen);
                }
            }
            for (int i = 0; i < outstandingsummaryList.size(); i++) {
                outSummaryVO = (OutstandingSummaryVO) outstandingsummaryList.get(i);
                if (newReceiptNo.equals(outSummaryVO.getReceiptNo())) {
                    outSummaryVO.setPaymentMode(strPaymentMode);
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomPhase2Bean Error::" + e.getMessage());
        }
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    @Override
    public Map<String, List> getCreditCustomerStatementDetails(String ondate, String custCode, String carriercode) throws RemoteException {
        Map<String, List> accountMap = new HashMap();
        List custstmtList = null;
        List custstmtListResult = null;
        SvmJsonList alist = null;
        NewCustomerstatementvo custstmtvoObj;
        Map resultMap = null, map = null;
        try {
            System.out.println("[ECOM] EcomPhase2Bean carriercodevalue" + carriercode);
            alist = new SvmJsonList();
            alist.add(ondate);
            alist.add(custCode);
            alist.add(carriercode);
            alist.add("");
            alist.add("");
            resultMap = phase2VdsService.getEcomCustStmtNew(alist);
           // System.out.println("[ECOM] EcomPhase2Bean acc summary map from CCR>>" + resultMap);
            if (resultMap != null) {
                map = (Map) resultMap.get("rs");
               // System.out.println("[ECOM] EcomPhase2Bean ejbmapvalue" + map);
                if (map != null) {
                    custstmtList = (List) map.get("rs0");
                    if (custstmtList != null && custstmtList.size() > 0) {
                        custstmtListResult = new ArrayList();
                        for (int i = 0; i < custstmtList.size(); i++) {
                            custstmtvoObj = new NewCustomerstatementvo();
                            resultMap = (Map) custstmtList.get(i);
                            custstmtvoObj.setCmpcode(checkNull((String) resultMap.get("cmpcode")));
                            custstmtvoObj.setCustcode(checkNull((String) resultMap.get("custcode")));
                            custstmtvoObj.setCustname(checkNull((String) resultMap.get("custname")));
                            custstmtvoObj.setPhonenumber(checkNull((String) resultMap.get("phonenumber")));
                            custstmtvoObj.setEmailaddress(checkNull((String) resultMap.get("emailaddress")));
                            custstmtvoObj.setTransdate(checkNull((String) resultMap.get("transdate")));
                            custstmtvoObj.setActivitydate(checkNull((String) resultMap.get("activitydate")));
                            custstmtvoObj.setInputdate(checkNull((String) resultMap.get("inputdate")));
                            custstmtvoObj.setDuedate(checkNull((String) resultMap.get("duedate")));
                            custstmtvoObj.setTransno(checkNull((String) resultMap.get("transno")));
                            custstmtvoObj.setBlno(checkNull((String) resultMap.get("blno")));
                            custstmtvoObj.setFcnverno(checkNull((String) resultMap.get("fcnverno")));
                            custstmtvoObj.setBookno(checkNull((String) resultMap.get("bookno")));
                            custstmtvoObj.setServicecode(checkNull((String) resultMap.get("servicecode")));
                            custstmtvoObj.setVesselcode(checkNull((String) resultMap.get("vesselcode")));
                            custstmtvoObj.setVoyage(checkNull((String) resultMap.get("voyage")));

                            custstmtvoObj.setBound(checkNull((String) resultMap.get("bound")));
                            custstmtvoObj.setPort(checkNull((String) resultMap.get("port")));
                            custstmtvoObj.setTerminal(checkNull((String) resultMap.get("terminal")));
                            custstmtvoObj.setGroupcallid(checkNull((String) resultMap.get("groupcallid")));
                            custstmtvoObj.setPol(checkNull((String) resultMap.get("pol")));
                            custstmtvoObj.setPod(checkNull((String) resultMap.get("pod")));
                            custstmtvoObj.setPolterminal(checkNull((String) resultMap.get("polterminal")));
                            custstmtvoObj.setPodterminal(checkNull((String) resultMap.get("podterminal")));
                            custstmtvoObj.setDivision(checkNull((String) resultMap.get("division")));
                            custstmtvoObj.setCurrency(checkNull((String) resultMap.get("currency")));
                            custstmtvoObj.setBlamount(checkNull((String) resultMap.get("blamount")));
                            custstmtvoObj.setReceivedamt(checkNull((String) resultMap.get("receivedamt")));
                            custstmtvoObj.setBlbalamount(checkNull((String) resultMap.get("blbalamount")));
                            custstmtvoObj.setCreditlimit(checkNull((String) resultMap.get("creditlimit")));
                            custstmtvoObj.setCreditperiod(checkNull((String) resultMap.get("creditperiod")));
                            custstmtvoObj.setAgingdays(checkNull((String) resultMap.get("agingdays")));
                            custstmtvoObj.setAgingdayscurrent(checkNull((String) resultMap.get("agingdayscurrent")));
                            custstmtvoObj.setAgingdays1upto17(checkNull((String) resultMap.get("agingdays1upto17")));
                            custstmtvoObj.setAgingdays18upto30(checkNull((String) resultMap.get("agingdays18upto30")));
                            //custstmtvoObj.setAgingdays0upto30(checkNull((String) resultMap.get("agingdays0upto30")));
                            custstmtvoObj.setAgingdays30upto60(checkNull((String) resultMap.get("agingdays30upto60")));
                            custstmtvoObj.setAgingdays60upto90(checkNull((String) resultMap.get("agingdays60upto90")));
                            custstmtvoObj.setAgingdaysover90(checkNull((String) resultMap.get("agingdaysover90")));

                            custstmtListResult.add(custstmtvoObj);
                        }
                    }
                    accountMap.put("CustStmtList", custstmtListResult);
                    custstmtList = (List) map.get("rs1");
                    if (custstmtList != null && custstmtList.size() > 0) {
                        custstmtListResult = new ArrayList();
                        for (int i = 0; i < custstmtList.size(); i++) {
                            custstmtvoObj = new NewCustomerstatementvo();
                            resultMap = (Map) custstmtList.get(i);
                            custstmtvoObj.setCmpcode(checkNull((String) resultMap.get("cmpcode")));
                            custstmtvoObj.setTotal(checkNull((String) resultMap.get("total")));
                            custstmtvoObj.setAgingdayscurrent(checkNull((String) resultMap.get("agingdayscurrent")));
                            custstmtvoObj.setAgingdays1upto17(checkNull((String) resultMap.get("agingdays1upto17")));
                            custstmtvoObj.setAgingdays18upto30(checkNull((String) resultMap.get("agingdays18upto30")));
                            //custstmtvoObj.setAgingdays0upto30(checkNull((String) resultMap.get("agingdays0upto30")));
                            custstmtvoObj.setAgingdays30upto60(checkNull((String) resultMap.get("agingdays30upto60")));
                            custstmtvoObj.setAgingdays60upto90(checkNull((String) resultMap.get("agingdays60upto90")));
                            custstmtvoObj.setAgingdaysover90(checkNull((String) resultMap.get("agingdaysover90")));
                            custstmtListResult.add(custstmtvoObj);
                        }
                    }

                }
                accountMap.put("CustStmtGroupList", custstmtListResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountMap;
    }

    @Override
    public Map getRateContract(String shipmentDate, String custCode, String pol, String pod, String eqpType, String commodity) {
        Map result = null, resultMap = null, map = null;
        SvmJsonList alist = null;
        List lis = null, cmlis = null;
        ContractVo convo = null;
        try {
            result = new HashMap();
            alist = new SvmJsonList();
            alist.add(pol);
            alist.add(pod);
            alist.add(eqpType);
            alist.add(commodity);
            alist.add(shipmentDate);
            alist.add(custCode);

            resultMap = phase2VdsService.getEcomRateContract(alist);
            if (resultMap != null) {
                map = (Map) resultMap.get("rs");
                if (map != null) {
                    lis = (List) map.get("rs0");
                    if (lis != null) {
                        HashMap cmdmap = new HashMap();
                        String clas = null;
                        for (int i = 0; i < lis.size(); i++) {
                            resultMap = (Map) lis.get(i);
                            clas = (String) resultMap.get("class");
                            if (cmdmap.containsKey(clas)) {
                                cmlis = (List) cmdmap.get(clas);
                                cmlis.add((String) resultMap.get("vizname"));
                                cmdmap.put(clas, cmlis);
                            } else {
                                cmlis = new ArrayList();
                                cmlis.add((String) resultMap.get("vizname"));
                                cmdmap.put(clas, cmlis);
                            }
                        }
                        result.put("commoditynamelis", cmdmap);
                    }
                    // 2nd result set (rs1) -> ContractVo
                    lis = (List) map.get("rs1");
                    if (lis != null) {
                        HashMap conmap = new HashMap();
                        TreeSet classset = new TreeSet();
                        String clas = null;
                        for (int i = 0; i < lis.size(); i++) {
                            resultMap = (Map) lis.get(i);
                            convo = new ContractVo();
                            convo.setAllinrate((String) resultMap.get("allinrate"));
                            convo.setAmendmentno((String) resultMap.get("amendmentno"));
                            convo.setBaserate((String) resultMap.get("baserate"));
                            convo.setDestination((String) resultMap.get("dischport"));
                            convo.setDestinationcountrycode((String) resultMap.get("dischport_country"));
                            convo.setDestinationstatecode((String) resultMap.get("dischport_state"));
                            convo.setEffective((String) resultMap.get("effectivedate"));
                            convo.setEqptype((String) resultMap.get("eqptype"));
                            convo.setExpiration((String) resultMap.get("expiration"));
                            convo.setNor((String) resultMap.get("nor"));
                            convo.setNotes((String) resultMap.get("ratedescription"));
                            convo.setOrigin((String) resultMap.get("loadport"));
                            convo.setOrigincountrycode((String) resultMap.get("loadport_country"));
                            convo.setOriginstatecode((String) resultMap.get("loadport_state"));
                            convo.setPod((String) resultMap.get("pod"));
                            convo.setPol((String) resultMap.get("pol"));
                            convo.setRateno((String) resultMap.get("contractno"));
                            convo.setSoc((String) resultMap.get("soc"));
                            convo.setTypeofservice((String) resultMap.get("tservice"));
                            convo.setValidfrom((String) resultMap.get("validfrom"));
                            convo.setValidto((String) resultMap.get("validto"));
                            convo.setItemno((String) resultMap.get("itemno"));
                            convo.setVid((String) resultMap.get("vid"));
                            // newly added rs1 columns
                            convo.setContractstatus((String) resultMap.get("contractstatus"));
                            convo.setAllin((String) resultMap.get("allin"));
                            convo.setAllinAmtO((String) resultMap.get("allinamto"));
                            convo.setAllinAmtA((String) resultMap.get("allinamta"));
                            clas = (String) resultMap.get("class");
                            classset.add(clas);
                            if (conmap.containsKey(clas)) {
                                cmlis = (List) conmap.get(clas);
                                cmlis.add(convo);
                                conmap.put(clas, cmlis);
                            } else {
                                cmlis = new ArrayList();
                                cmlis.add(convo);
                                conmap.put(clas, cmlis);
                            }
                        }
                        result.put("ContractDetails", conmap);
                        result.put("ClassDetailsSet", classset);
                    }
                    // 3rd result set (rs2) -> ApplicableCharges (amt~~vatos~~eqptype)
                    lis = (List) map.get("rs2");
                    if (lis != null) {
                        Map acmap = new HashMap();
                        Map chargemap = null;
                        String contract = null;
                        TreeSet chargeset = new TreeSet();
                        for (int i = 0; i < lis.size(); i++) {
                            resultMap = (Map) lis.get(i);
                            contract = (String) resultMap.get("contractno") + "-" + (String) resultMap.get("itemno");
                            chargeset.add((String) resultMap.get("chargecode"));
                            String chargeVal = (String) resultMap.get("amtusd") + "~~" + (String) resultMap.get("vatos")
                                    + "~~" + (String) resultMap.get("eqptype");
                            if (acmap.containsKey(contract)) {
                                chargemap = (Map) acmap.get(contract);
                                chargemap.put((String) resultMap.get("chargecode"), chargeVal);
                                acmap.put(contract, chargemap);
                            } else {
                                chargemap = new HashMap();
                                chargemap.put((String) resultMap.get("chargecode"), chargeVal);
                                acmap.put(contract, chargemap);
                            }
                        }
                        result.put("ApplicableCharges", acmap);
                        result.put("Chargecodeset", chargeset);
                    }

                    // After existing VDS mapping: recalculate ApplicableCharges / all-in
                    Gson gson = new Gson();
                    logPrinter("BEFORE populateRateContractFromVdsMap result: " + gson.toJson(result));
                    new RateContractBean().populateRateContractFromVdsMap(result, map, shipmentDate, custCode,
                            pol, pod, eqpType, commodity, true);
                    logPrinter("AFTER populateRateContractFromVdsMap result: " + gson.toJson(result));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map getRateContractQuote(String shipmentdate, String custcode, String pol, String pod, String eqpType, String commodity) {
        Map result = null, resultMap = null, map = null;
        SvmJsonList alist = null;
        List lis = null, cmlis = null;
        ContractVo convo = null;
        try {
            result = new HashMap();
            alist = new SvmJsonList();
            alist.add(pol);
            alist.add(pod);
            alist.add(eqpType);
            alist.add(commodity);
            alist.add(shipmentdate);
            alist.add(custcode);

            resultMap = phase2VdsService.getEcomRateContractQuote(alist);
            //System.out.println("[ECOM] EcomPhase2Bean resultMap===> "+resultMap);
            if (resultMap != null) {
                map = (Map) resultMap.get("rs");
                if (map != null) {
                    lis = (List) map.get("rs0");
                    if (lis != null) {
                        HashMap cmdmap = new HashMap();
                        String clas = null;
                        for (int i = 0; i < lis.size(); i++) {
                            resultMap = (Map) lis.get(i);
                            clas = (String) resultMap.get("class");
                            if (cmdmap.containsKey(clas)) {
                                cmlis = (List) cmdmap.get(clas);
                                cmlis.add((String) resultMap.get("vizname"));
                                cmdmap.put(clas, cmlis);
                            } else {
                                cmlis = new ArrayList();
                                cmlis.add((String) resultMap.get("vizname"));
                                cmdmap.put(clas, cmlis);
                            }
                        }
                        result.put("commoditynamelis", cmdmap);
                    }
                    lis = (List) map.get("rs1");
                    if (lis != null) {
                        HashMap conmap = new HashMap();
                        TreeSet classset = new TreeSet();
                        String clas = null;
                        for (int i = 0; i < lis.size(); i++) {
                            resultMap = (Map) lis.get(i);
                            convo = new ContractVo();
                            convo.setAllinrate((String) resultMap.get("allinrate"));
                            convo.setAmendmentno((String) resultMap.get("amendmentno"));
                            convo.setBaserate((String) resultMap.get("baserate"));
                            convo.setDestination((String) resultMap.get("dischport"));
                            convo.setDestinationcountrycode((String) resultMap.get("dischport_country"));
                            convo.setDestinationstatecode((String) resultMap.get("dischport_state"));
                            convo.setEffective((String) resultMap.get("effectivedate"));
                            convo.setEqptype((String) resultMap.get("eqptype"));
                            convo.setExpiration((String) resultMap.get("expiration"));
                            convo.setNor((String) resultMap.get("nor"));
                            convo.setNotes((String) resultMap.get("ratedescription"));
                            convo.setOrigin((String) resultMap.get("loadport"));
                            convo.setOrigincountrycode((String) resultMap.get("loadport_country"));
                            convo.setOriginstatecode((String) resultMap.get("loadport_state"));
                            convo.setPod((String) resultMap.get("pod"));
                            convo.setPol((String) resultMap.get("pol"));
                            convo.setRateno((String) resultMap.get("contractno"));
                            convo.setSoc((String) resultMap.get("soc"));
                            convo.setTypeofservice((String) resultMap.get("tservice"));
                            convo.setValidfrom((String) resultMap.get("validfrom"));
                            convo.setValidto((String) resultMap.get("validto"));
                            convo.setItemno((String) resultMap.get("itemno"));
                            convo.setVid((String) resultMap.get("vid"));
                            convo.setContractstatus((String) resultMap.get("contractstatus"));
                            clas = (String) resultMap.get("class");
                            classset.add(clas);
                            if (conmap.containsKey(clas)) {
                                cmlis = (List) conmap.get(clas);
                                cmlis.add(convo);
                                conmap.put(clas, cmlis);
                            } else {
                                cmlis = new ArrayList();
                                cmlis.add(convo);
                                conmap.put(clas, cmlis);
                            }
                        }
                        result.put("ContractDetails", conmap);
                        result.put("ClassDetailsSet", classset);
                    }
                    lis = (List) map.get("rs2");
                    if (lis != null) {
                        Map acmap = new HashMap();
                        Map chargemap = null;
                        String contract = null;
                        TreeSet chargeset = new TreeSet();
                        for (int i = 0; i < lis.size(); i++) {
                            resultMap = (Map) lis.get(i);
                            /*if(i%2==0){
                            resultMap.put("vatos","Y");
                            }*/
                            contract = (String) resultMap.get("contractno") + "-" + (String) resultMap.get("itemno");
                            chargeset.add((String) resultMap.get("chargecode"));
                            if (acmap.containsKey(contract)) {
                                chargemap = (Map) acmap.get(contract);
                                chargemap.put((String) resultMap.get("chargecode"), (String) resultMap.get("amtusd")+"~~"+(String) resultMap.get("vatos"));
                                acmap.put(contract, chargemap);
                            } else {
                                chargemap = new HashMap();
                                chargemap.put((String) resultMap.get("chargecode"), (String) resultMap.get("amtusd")+"~~"+(String) resultMap.get("vatos"));
                                acmap.put(contract, chargemap);
                            }
                        }
                        result.put("ApplicableCharges", acmap);
                        result.put("Chargecodeset", chargeset);
                    }
                }
            }
            //System.out.println("[ECOM] EcomPhase2Bean result=======> "+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, List> getAccountSummaryfromcuststmtsp(String strType, String custCode, String fromDate, String toDate, String option, String carrierCode) throws RemoteException {
        Map<String, List> accountMap = new HashMap<String, List>();
        List<AccountSummaryVO> accountsummaryList = new ArrayList<>();
        List<AmountSummaryVO> amountSummaryList = new ArrayList<>();
        Map<String, double[]> currencyAggregation = new HashMap<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String inputType;
            if (fromDate == null || fromDate.isEmpty() || toDate == null || toDate.isEmpty()) {
                // "AS ON DATE" mode - SP expects FROMDATE=today, TODATE=empty
                inputType = "AS ON DATE";
                fromDate = sdf.format(new java.util.Date()); // today's date
                toDate = ""; // empty for AS ON DATE mode
            } else {
                inputType = "DATE";
            }
            System.out.println("[ECOM] customerstatementsp option: " + option + ", inputType: " + inputType);
            con = new lrp.serverutils.refs.ServerObject("ReportDB").getConnection();
            System.out.println("[ECOM] DB URL: " + con.getMetaData().getURL() + ", User: " + con.getMetaData().getUserName());
            ps = con.prepareStatement(customerstatementsp);
            ps.setString(1, "N/A");                    // 1-LOCATIONCODE
            ps.setString(2, "N/A");                    // 2-AGENCYCODE
            ps.setString(3, "CS");                     // 3-VIEWBY
            ps.setString(4, inputType);                // 4-INPUTTYPE
            ps.setString(5, checkNull(custCode));      // 5-CUSTCODE
            ps.setString(6, fromDate);                 // 6-FROMDATE
            ps.setString(7, toDate);                   // 7-TODATE
            ps.setString(8, "customer");            // 8-OPTION
            ps.setString(9, "");                       // 9-SERCODE
            ps.setString(10, "");                      // 10-VSLCODE
            ps.setString(11, "");                      // 11-VOYCODE
            ps.setString(12, "");                      // 12-BOUND
            ps.setString(13, "");                      // 13-PORT
            ps.setString(14, "");                      // 14-CURRENCY
            ps.setString(15, "L");                     // 15-SYSTEMTYPE
            ps.setString(16, "ALL");                   // 16-REPORTFOR
            ps.setString(17, "");                      // 17-CORPCODE
            ps.setString(18, "ALL");                   // 18-INVTYPE
            ps.setString(19, "ALL");                   // 19-CHARGETYPE
            ps.setString(20, "");                      // 20-EXECUTEFROM
            ps.setString(21, checkNull(carrierCode));  // 21-CARRIERCODE
            ps.setString(22, "");                      // 22-SESSIONID
            ps.setString(23, "N");                     // 23-AGSAILDATE
            
            System.out.println("[ECOM] ===== SP CALL v2 ===== custCode=" + checkNull(custCode) + ", fromDate=" + fromDate + 
                ", toDate=" + toDate + ", inputType=" + inputType + ", carrierCode=" + checkNull(carrierCode));
            
            boolean hasResults = ps.execute();
            
            if (hasResults) {
                rs = ps.getResultSet();
                System.out.println("[ECOM] getAccountSummaryFromCustStat processing rs0 (Account Summary)");
                while (rs.next()) {
                    String invNo = checkNull(rs.getString("invno"));
                    String receiptNo = checkNull(rs.getString("receiptno"));
                    String currency = checkNull(rs.getString("currency"));
                    String blNo = checkNull(rs.getString("blno"));
                    String invDate = checkNull(rs.getString("date"));
                    String invAmount = checkNull(rs.getString("invamount"));
                    String roe = checkNull(rs.getString("roe"));
                    String invUsdAmt = checkNull(rs.getString("invusdamt"));
                    String excessAmtUsd = checkNull(rs.getString("excessamtinusd"));
                    String balUsdAmt = checkNull(rs.getString("balusdamt"));
                    
                    System.out.println("[ECOM] rs0 ROW - invno: " + invNo + ", receiptno: " + receiptNo + 
                        ", blno: " + blNo + ", currency: " + currency + ", invamount: " + invAmount + ", balusdamt: " + balUsdAmt);

                    AccountSummaryVO vo = new AccountSummaryVO();
                    vo.setBlNo(blNo);
                    vo.setInvoiceDate(invDate);
                    vo.setInvoiceNo(invNo);
                    vo.setInvCurrency(currency);
                    vo.setInvAmount(invAmount);
                    vo.setInvROE(roe);
                    vo.setInvAmountUSD(invUsdAmt);
                    vo.setReceiptNo(receiptNo);
                    vo.setPaidAmtUSD("");
                    vo.setExcessAmtUSD(excessAmtUsd);
                    vo.setOutStandingAmtUSD(balUsdAmt);
                    vo.setPaymentMode("");
                    accountsummaryList.add(vo);
                    
                    // Aggregate for Currency Balance (fallback if rs1 is empty)
                    if (currency != null && !currency.isEmpty()) {
                        double invAmountD = parseDoubleSafe(invAmount);
                        double invUsdAmtD = parseDoubleSafe(invUsdAmt);
                        double excessAmtD = parseDoubleSafe(excessAmtUsd);
                        double balUsdAmtD = parseDoubleSafe(balUsdAmt);
                        
                        double[] totals = currencyAggregation.get(currency);
                        if (totals != null) {
                            totals[0] += invAmountD;
                            totals[1] += invUsdAmtD;
                            totals[2] += excessAmtD;
                            totals[3] += balUsdAmtD;
                        } else {
                            currencyAggregation.put(currency, new double[]{invAmountD, invUsdAmtD, excessAmtD, balUsdAmtD});
                        }
                    }
                }
                rs.close();
            }
            
            if (ps.getMoreResults()) {
                rs = ps.getResultSet();
                System.out.println("[ECOM] getAccountSummaryFromCustStat processing rs1 (Currency Balance)");
                while (rs.next()) {
                    AmountSummaryVO vo = new AmountSummaryVO();
                    vo.setInvCurrency(checkNull(rs.getString("currency")));
                    vo.setInvAmount(checkNull(rs.getString("invamount")));
                    vo.setInvAmtUSD(checkNull(rs.getString("invusdamt")));
                    vo.setPayAmtUSD("");  // Paid Amt (USD) - no SP column mapping, set empty
                    vo.setExcessAmtUSD(checkNull(rs.getString("excessamtinusd")));
                    vo.setOutStandingAmtUSD(checkNull(rs.getString("balusdamt")));
                    amountSummaryList.add(vo);
                }
                rs.close();
                System.out.println("[ECOM] getAccountSummaryFromCustStat rs1 processed - amountSummaryList size: " + amountSummaryList.size());
            }
            
            if (amountSummaryList.isEmpty() && !currencyAggregation.isEmpty()) {
                System.out.println("[ECOM] getAccountSummaryFromCustStat rs1 is empty, generating Currency Balance from rs0 aggregation");
                for (Map.Entry<String, double[]> entry : currencyAggregation.entrySet()) {
                    AmountSummaryVO vo = new AmountSummaryVO();
                    vo.setInvCurrency(entry.getKey());
                    double[] totals = entry.getValue();
                    vo.setInvAmount(String.valueOf(totals[0]));
                    vo.setInvAmtUSD(String.valueOf(totals[1]));
                    vo.setPayAmtUSD("");
                    vo.setExcessAmtUSD(String.valueOf(totals[2]));
                    vo.setOutStandingAmtUSD(String.valueOf(totals[3]));
                    amountSummaryList.add(vo);
                }
            }
            
        } catch (Exception e) {
            System.out.println("[ECOM] getAccountSummaryFromCustStat Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("[ECOM] getAccountSummaryFromCustStat Error closing resources: " + e.getMessage());
            }
        }
        accountMap.put("AccountSummary", accountsummaryList);
        accountMap.put("AmountSummary", amountSummaryList);
        return accountMap;
    }
    
    @Override
    public Map<String, List> getInvoiceDetailsfromcuststmtsp(String strType, String custCode, String fromDate, String toDate, String option, String carrierCode) throws RemoteException {
        Map<String, List> invMap = new HashMap<String, List>();
        List<CSInvoiceSummaryVO> invoicesummaryList = new ArrayList<>();
        List<AmountSummaryVO> amtsummaryList = new ArrayList<>();
        Map resultMap = null, map = null;
        SvmJsonList alist = null;
        try {
            System.out.println("[ECOM] getInvoiceDetailsFromCustStat INPUT - fromDate: [" + fromDate + "], toDate: [" + toDate + "]");
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            if (fromDate == null || fromDate.trim().isEmpty()) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.YEAR, -5);
                fromDate = sdf.format(cal.getTime());
            }
            if (toDate == null || toDate.trim().isEmpty()) {
                toDate = sdf.format(new java.util.Date());
            }
            // invdate is datetime; BETWEEN with date-only toDate (midnight) excludes
            // same-day invoices after 00:00:00. Use full-day bounds so Selected Date
            // (and All defaulted to today) includes the current/end date.
            if (fromDate != null && fromDate.trim().length() == 10) {
                fromDate = fromDate.trim() + " 00:00:00";
            }
            if (toDate != null && toDate.trim().length() == 10) {
                toDate = toDate.trim() + " 23:59:59";
            }
            
            String invDateRange = fromDate + "," + toDate;
            System.out.println("[ECOM] getInvoiceDetailsFromCustStat invDateRange: [" + invDateRange + "]");
            Map<String, String> paidInvoiceMap = loadCompletedPaymentInvoices(custCode);

            resultMap = phase2VdsService.getInvoiceCustomerStatementView(custCode, invDateRange);
            if (resultMap != null) {
                List<Map> rs0List = (List<Map>) resultMap.get("invoicemaster");
                if (rs0List != null && rs0List.size() > 0) {
                    
                    Map<String, double[]> currencyAggregation = new HashMap<>();
                    int rowIdx = 0;
                    
                    for (Map row : rs0List) {
                        rowIdx++;
                        System.out.println("[ECOM] INV ROW " + rowIdx + ": " + row);
                        String blNo = checkNull((String) row.get("invoicedetail.blno"));
                        if (blNo == null || blNo.isEmpty()) {
                            blNo = checkNull((String) row.get("blno"));
                        }
                        if (blNo == null || blNo.isEmpty()) { 
                            for (Object key : row.keySet()) {
                                if (key.toString().toLowerCase().contains("blno")) {
                                    blNo = checkNull((String) row.get(key));
                                    System.out.println("[ECOM] Found BL using key: " + key + " = " + blNo);
                                    break;
                                }
                            }
                        }
                        System.out.println("[ECOM] Final BL Number: " + blNo);
                        
                        CSInvoiceSummaryVO vo = new CSInvoiceSummaryVO();
                        vo.setInvoiceNo(checkNull((String) row.get("invno")));
                        vo.setInvoiceDate(checkNull((String) row.get("invdate")));
                        vo.setInvCurrency(checkNull((String) row.get("invcurrency")));
                        vo.setInvAmount(checkNull((String) row.get("invamount")));
                        vo.setInvBalAmount(checkNull((String) row.get("invbalamount")));
                        vo.setInvAmountUSD(checkNull((String) row.get("invamountusd")));
                        vo.setInvBalAmountUSD(checkNull((String) row.get("invbalamountusd")));
                        vo.setInvType(checkNull((String) row.get("creditdebitnote")));
                        vo.setBookNo(checkNull((String) row.get("bookno")));
                        vo.setBlNo(blNo);
                        vo.setVessel(checkNull((String) row.get("vslcode")));
                        vo.setVoyage(checkNull((String) row.get("voycode")));
                        vo.setService(checkNull((String) row.get("sercode")));
                        vo.setBound(checkNull((String) row.get("bound")));
                        vo.setLocationCode(checkNull((String) row.get("locationcode")));
                        vo.setPay_customer_type("IN");

                        String balUsdStr = checkNull((String) row.get("invbalamountusd"));
                        double balUsd = 0;
                        try { if (balUsdStr != null && !balUsdStr.isEmpty()) balUsd = Double.parseDouble(balUsdStr); } catch (NumberFormatException ignored) {}
                        vo.setIs_payment_eligible(balUsd > 0 ? "Y" : "N");

                        String invNo = vo.getInvoiceNo();
                        if (invNo != null && paidInvoiceMap.containsKey(invNo)) {
                            vo.setIs_payment_completed("Y");
                            vo.setPaymentRequestId(paidInvoiceMap.get(invNo));
                        } else {
                            vo.setIs_payment_completed("N");
                        }

                        vo.setRefNo(""); 
                        invoicesummaryList.add(vo);
 
                        String currency = checkNull((String) row.get("invcurrency"));
                        if (currency != null && !currency.isEmpty()) {
                            double invAmount = Double.parseDouble((String) row.get("invamount"));
                            
                            if (currencyAggregation.containsKey(currency)) {
                                double[] totals = currencyAggregation.get(currency);
                                totals[0] += invAmount;  
                            } else {
                                currencyAggregation.put(currency, new double[]{invAmount});
                            }
                        }
                    }
                     
                    System.out.println("[ECOM] getInvoiceDetailsFromCustStat generating Currency Balance from aggregation");
                    for (Map.Entry<String, double[]> entry : currencyAggregation.entrySet()) {
                        AmountSummaryVO vo = new AmountSummaryVO();
                        vo.setCiInvCurrency(entry.getKey());
                        double[] totals = entry.getValue();
                        vo.setCiInvAmount(String.valueOf(totals[0]));
                        vo.setInvCurrency(entry.getKey());
                        vo.setInvAmount(String.valueOf(totals[0]));
                        amtsummaryList.add(vo);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM] getInvoiceDetailsFromCustStat Error: " + e.getMessage());
            e.printStackTrace();
        } 
        invMap.put("InvoiceSummary", invoicesummaryList);
        invMap.put("AmountSummary", amtsummaryList);
        System.out.println("[ECOM] getInvoiceDetailsFromCustStat final - invoicesummaryList size: " + invoicesummaryList.size() + ", amtsummaryList size: " + amtsummaryList.size());
        return invMap;
    }

    private Map<String, String> loadCompletedPaymentInvoices(String custCode) {
        Map<String, String> paidMap = new HashMap<>();
        final String sql = "SELECT DISTINCT r.refNo, p.requestId " +
                          "FROM Ecom_OnlinePayment p (NOLOCK) " +
                          "INNER JOIN Ecom_OnlinePayment_Reference r (NOLOCK) ON p.requestId = r.requestId " +
                          "WHERE p.customerCode = ? AND r.refType = 'INV' AND p.status = 'COMPLETED'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, custCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                String refNo = rs.getString("refNo");
                String requestId = rs.getString("requestId");
                if (refNo != null && !refNo.isEmpty()) {
                    paidMap.put(refNo, requestId);
                }
            }
            System.out.println("[ECOM] loadCompletedPaymentInvoices -> custCode=" + custCode + ", paid invoices=" + paidMap.size());
        } catch (Exception e) {
            System.out.println("[ECOM] loadCompletedPaymentInvoices Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
        return paidMap;
    }

    @Override
    public Map<String, List> getPaymentDetialsfromcuststmtsp(String strType, String custCode, String fromDate, String toDate, String option, String carrierCode) throws RemoteException {
        Map<String, List> payMap = new HashMap<String, List>();
        List<PaymentSummaryVO> paymentsummaryList = new ArrayList<>();
        List<AmountSummaryVO> amtsummaryList = new ArrayList<>();
        Map<String, double[]> currencyAggregation = new HashMap<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String inputType;
            if (fromDate == null || fromDate.isEmpty() || toDate == null || toDate.isEmpty()) {
                // "AS ON DATE" mode - SP expects FROMDATE=today, TODATE=empty
                inputType = "AS ON DATE";
                fromDate = sdf.format(new java.util.Date()); // today's date
                toDate = ""; // empty for AS ON DATE mode
            } else {
                inputType = "DATE";
            }
            System.out.println("[ECOM] getPaymentDetialsfromcuststmtsp - inputType: " + inputType + ", custCode: " + custCode);
            
            con = new lrp.serverutils.refs.ServerObject("ReportDB").getConnection();
            ps = con.prepareStatement(customerstatementsp);
            ps.setString(1, "N/A");                    // 1-LOCATIONCODE
            ps.setString(2, "N/A");                    // 2-AGENCYCODE
            ps.setString(3, "CC");                     // 3-VIEWBY (CC for Payment/Collection)
            ps.setString(4, inputType);                // 4-INPUTTYPE
            ps.setString(5, checkNull(custCode));      // 5-CUSTCODE
            ps.setString(6, fromDate);                 // 6-FROMDATE
            ps.setString(7, toDate);                   // 7-TODATE
            ps.setString(8, "customer");               // 8-OPTION
            ps.setString(9, "");                       // 9-SERCODE
            ps.setString(10, "");                      // 10-VSLCODE
            ps.setString(11, "");                      // 11-VOYCODE
            ps.setString(12, "");                      // 12-BOUND
            ps.setString(13, "");                      // 13-PORT
            ps.setString(14, "");                      // 14-CURRENCY
            ps.setString(15, "L");                     // 15-SYSTEMTYPE
            ps.setString(16, "ALL");                   // 16-REPORTFOR
            ps.setString(17, "");                      // 17-CORPCODE
            ps.setString(18, "ALL");                   // 18-INVTYPE
            ps.setString(19, "ALL");                   // 19-CHARGETYPE
            ps.setString(20, "");                      // 20-EXECUTEFROM
            ps.setString(21, checkNull(carrierCode));  // 21-CARRIERCODE
            ps.setString(22, "");                      // 22-SESSIONID
            ps.setString(23, "N");                     // 23-AGSAILDATE
            
            System.out.println("[ECOM] getPaymentDetialsfromcuststmtsp SP CALL - custCode=" + checkNull(custCode) + 
                ", fromDate=" + fromDate + ", toDate=" + toDate + ", carrierCode=" + checkNull(carrierCode));
            
            boolean hasResults = ps.execute();
            
            if (hasResults) {
                rs = ps.getResultSet();
                System.out.println("[ECOM] getPaymentDetialsfromcuststmtsp processing rs0 (Payment Summary)");
                while (rs.next()) {
                    String receiptNo = checkNull(rs.getString("receiptno"));
                    String receiptDate = checkNull(rs.getString("receiptdate"));
                    String receiptMode = checkNull(rs.getString("receiptmode"));
                    String docType = checkNull(rs.getString("doctype"));
                    String receiptAmt = checkNull(rs.getString("receiptamt"));
                    String recUsdAmt = checkNull(rs.getString("recusdamt"));
                    String excessAmtUsd = checkNull(rs.getString("excessamtinusd"));
                    String invNo = checkNull(rs.getString("invno"));
                    String bookNo = checkNull(rs.getString("bookno"));
                    String blNo = checkNull(rs.getString("blno"));
                    String invVessel = checkNull(rs.getString("invvessel"));
                    String currency = checkNull(rs.getString("currency"));
                    
                    System.out.println("[ECOM] PAY ROW - receiptno: " + receiptNo + ", receiptdate: " + receiptDate + 
                        ", receiptamt: " + receiptAmt + ", invno: " + invNo + ", blno: " + blNo);
                    
                    PaymentSummaryVO vo = new PaymentSummaryVO();
                    vo.setReceiptNo(receiptNo);
                    vo.setReceiptDate(receiptDate);
                    vo.setPaymentMode(receiptMode);
                    vo.setPaymentType(docType);
                    vo.setPaidAmt(receiptAmt);
                    vo.setPaidAmtUSD(recUsdAmt);
                    vo.setExcessAmtUSD(excessAmtUsd);
                    vo.setInvoiceNo(invNo);
                    vo.setBookNo(bookNo);
                    vo.setBlNo(blNo);
                    vo.setVessel(invVessel);
                    vo.setVoyage("");
                    vo.setBound("");
                    paymentsummaryList.add(vo);
                    
                    // Aggregate for Currency Balance
                    if (currency == null || currency.isEmpty()) currency = "USD";
                    double excessAmtD = parseDoubleSafe(excessAmtUsd);
                    double paidAmtD = parseDoubleSafe(recUsdAmt);
                    
                    double[] totals = currencyAggregation.get(currency);
                    if (totals != null) {
                        totals[0] += paidAmtD;
                        totals[1] += excessAmtD;
                    } else {
                        currencyAggregation.put(currency, new double[]{paidAmtD, excessAmtD});
                    }
                }
                rs.close();
            }
            
            // Process rs1 (Currency Balance)
            if (ps.getMoreResults()) {
                rs = ps.getResultSet();
                System.out.println("[ECOM] getPaymentDetialsfromcuststmtsp processing rs1 (Currency Balance)");
                while (rs.next()) {
                    AmountSummaryVO vo = new AmountSummaryVO();
                    vo.setPayAmtUSD(checkNull(rs.getString("recusdamt")));
                    vo.setExcessAmtUSD(checkNull(rs.getString("excessamtinusd")));
                    amtsummaryList.add(vo);
                }
                rs.close();
            }
            
            // Fallback: generate Currency Balance from rs0 aggregation if rs1 is empty
            if (amtsummaryList.isEmpty() && !currencyAggregation.isEmpty()) {
                System.out.println("[ECOM] getPaymentDetialsfromcuststmtsp rs1 empty, generating from rs0 aggregation");
                for (Map.Entry<String, double[]> entry : currencyAggregation.entrySet()) {
                    AmountSummaryVO vo = new AmountSummaryVO();
                    double[] totals = entry.getValue();
                    vo.setPayAmtUSD(String.valueOf(totals[0]));
                    vo.setExcessAmtUSD(String.valueOf(totals[1]));
                    amtsummaryList.add(vo);
                }
            }
            
        } catch (Exception e) {
            System.out.println("[ECOM] getPaymentDetialsfromcuststmtsp Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("[ECOM] getPaymentDetialsfromcuststmtsp Error closing resources: " + e.getMessage());
            }
        }
        payMap.put("PaymentSummary", paymentsummaryList);
        payMap.put("AmountSummary", amtsummaryList);
        System.out.println("[ECOM] getPaymentDetialsfromcuststmtsp final - paymentsummaryList size: " + paymentsummaryList.size());
        return payMap;
    }

    @Override  
    public Map<String, List> getOutstandingDetailsfromcuststmtsp(String strType, String custCode, String fromDate, String toDate, String option, String carrierCode) throws RemoteException {
        Map<String, List> outMap = new HashMap<String, List>();
        List<OutstandingSummaryVO> outstandingsummaryList = new ArrayList<>();
        List<AmountSummaryVO> amtsummaryList = new ArrayList<>();
        Map<String, double[]> currencyAggregation = new HashMap<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String inputType;
            if (fromDate == null || fromDate.isEmpty() || toDate == null || toDate.isEmpty()) {
                inputType = "AS ON DATE";
                fromDate = sdf.format(new java.util.Date()); 
                toDate = "";
            } else {
                inputType = "DATE";
            }
            System.out.println("[ECOM] getOutstandingDetailsfromcuststmtsp - inputType: " + inputType + ", custCode: " + custCode);
            
            con = new lrp.serverutils.refs.ServerObject("ReportDB").getConnection();
            ps = con.prepareStatement(customerstatementsp);
            ps.setString(1, "N/A");                    // 1-LOCATIONCODE
            ps.setString(2, "N/A");                    // 2-AGENCYCODE
            ps.setString(3, "CO");                     // 3-VIEWBY (CO for Outstanding)
            ps.setString(4, inputType);                // 4-INPUTTYPE
            ps.setString(5, checkNull(custCode));      // 5-CUSTCODE
            ps.setString(6, fromDate);                 // 6-FROMDATE
            ps.setString(7, toDate);                   // 7-TODATE
            ps.setString(8, "customer");               // 8-OPTION
            ps.setString(9, "");                       // 9-SERCODE
            ps.setString(10, "");                      // 10-VSLCODE
            ps.setString(11, "");                      // 11-VOYCODE
            ps.setString(12, "");                      // 12-BOUND
            ps.setString(13, "");                      // 13-PORT
            ps.setString(14, "");                      // 14-CURRENCY
            ps.setString(15, "L");                     // 15-SYSTEMTYPE
            ps.setString(16, "ALL");                   // 16-REPORTFOR
            ps.setString(17, "");                      // 17-CORPCODE
            ps.setString(18, "ALL");                   // 18-INVTYPE
            ps.setString(19, "ALL");                   // 19-CHARGETYPE
            ps.setString(20, "");                      // 20-EXECUTEFROM
            ps.setString(21, checkNull(carrierCode));  // 21-CARRIERCODE
            ps.setString(22, "");                      // 22-SESSIONID
            ps.setString(23, "N");                     // 23-AGSAILDATE
            
            System.out.println("[ECOM] getOutstandingDetailsfromcuststmtsp SP CALL - custCode=" + checkNull(custCode) + 
                ", fromDate=" + fromDate + ", toDate=" + toDate + ", carrierCode=" + checkNull(carrierCode));
            
            boolean hasResults = ps.execute();
            
            // rs0 - Outstanding Summary
            if (hasResults) {
                rs = ps.getResultSet();
                System.out.println("[ECOM] getOutstandingDetailsfromcuststmtsp processing rs0 (Outstanding Summary)");
                while (rs.next()) {
                    String blNo = checkNull(rs.getString("blno"));
                    String invDate = checkNull(rs.getString("date"));
                    String invNo = checkNull(rs.getString("invno"));
                    String currency = checkNull(rs.getString("currency"));
                    String invAmount = checkNull(rs.getString("invamount"));
                    String roe = checkNull(rs.getString("roe"));
                    String invUsdAmt = checkNull(rs.getString("invusdamt"));
                    String ageing = checkNull(rs.getString("ageing"));
                    String receiptNo = checkNull(rs.getString("receiptno"));
                    String balUsdAmt = checkNull(rs.getString("balusdamt"));
                    String excessAmtUsd = checkNull(rs.getString("excessamtinusd"));
                    
                    System.out.println("[ECOM] OUT ROW - invno: " + invNo + ", blno: " + blNo + 
                        ", currency: " + currency + ", invamount: " + invAmount + ", balusdamt: " + balUsdAmt);
                    
                    OutstandingSummaryVO vo = new OutstandingSummaryVO();
                    vo.setBlNo(blNo);
                    vo.setInvoiceDate(invDate);
                    vo.setInvoiceNo(invNo);
                    vo.setInvCurrency(currency);
                    vo.setInvAmount(invAmount);
                    vo.setInvROE(roe);
                    vo.setInvAmountUSD(invUsdAmt);
                    vo.setAgeing(ageing);
                    vo.setReceiptNo(receiptNo);
                    vo.setPaidAmtUSD("");
                    vo.setOutStandingAmtUSD(balUsdAmt);
                    vo.setPaymentMode("");
                    outstandingsummaryList.add(vo);
                    
                    // Aggregate for Currency Balance
                    if (currency != null && !currency.isEmpty()) {
                        double invAmountD = parseDoubleSafe(invAmount);
                        double invUsdAmtD = parseDoubleSafe(invUsdAmt);
                        double excessAmtD = parseDoubleSafe(excessAmtUsd);
                        double balUsdAmtD = parseDoubleSafe(balUsdAmt);
                        
                        double[] totals = currencyAggregation.get(currency);
                        if (totals != null) {
                            totals[0] += invAmountD;
                            totals[1] += invUsdAmtD;
                            totals[2] += excessAmtD;
                            totals[3] += balUsdAmtD;
                        } else {
                            currencyAggregation.put(currency, new double[]{invAmountD, invUsdAmtD, excessAmtD, balUsdAmtD});
                        }
                    }
                }
                rs.close();
            }
            
            // rs1 - Currency Balance
            if (ps.getMoreResults()) {
                rs = ps.getResultSet();
                System.out.println("[ECOM] getOutstandingDetailsfromcuststmtsp processing rs1 (Currency Balance)");
                while (rs.next()) {
                    AmountSummaryVO vo = new AmountSummaryVO();
                    vo.setInvCurrency(checkNull(rs.getString("currency")));
                    vo.setInvAmount(checkNull(rs.getString("invamount")));
                    vo.setInvAmtUSD(checkNull(rs.getString("invusdamt")));
                    vo.setPayAmtUSD("");
                    vo.setExcessAmtUSD(checkNull(rs.getString("excessamtinusd")));
                    vo.setOutStandingAmtUSD(checkNull(rs.getString("balusdamt")));
                    amtsummaryList.add(vo);
                }
                rs.close();
            }
            
            // Fallback: generate Currency Balance from rs0 if rs1 is empty
            if (amtsummaryList.isEmpty() && !currencyAggregation.isEmpty()) {
                System.out.println("[ECOM] getOutstandingDetailsfromcuststmtsp rs1 empty, generating from rs0 aggregation");
                for (Map.Entry<String, double[]> entry : currencyAggregation.entrySet()) {
                    AmountSummaryVO vo = new AmountSummaryVO();
                    vo.setInvCurrency(entry.getKey());
                    double[] totals = entry.getValue();
                    vo.setInvAmount(String.valueOf(totals[0]));
                    vo.setInvAmtUSD(String.valueOf(totals[1]));
                    vo.setPayAmtUSD("");
                    vo.setExcessAmtUSD(String.valueOf(totals[2]));
                    vo.setOutStandingAmtUSD(String.valueOf(totals[3]));
                    amtsummaryList.add(vo);
                }
            }
            
            // rs2 - skip (payment mode processing not needed for direct SP)
            // rs3 - Total Outstanding Balance
            if (ps.getMoreResults()) { // skip rs2
                ps.getResultSet().close();
            }
            if (ps.getMoreResults()) { // rs3
                rs = ps.getResultSet();
                if (rs.next()) {
                    List<String> totalList = new ArrayList<>();
                    totalList.add(checkNull(rs.getString(1)));
                    outMap.put("totalOutstandingBal", totalList);
                    System.out.println("[ECOM] getOutstandingDetailsfromcuststmtsp totalOutstandingBal: " + totalList);
                }
                rs.close();
            }
            
        } catch (Exception e) {
            System.out.println("[ECOM] getOutstandingDetailsfromcuststmtsp Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("[ECOM] getOutstandingDetailsfromcuststmtsp Error closing resources: " + e.getMessage());
            }
        }
        outMap.put("OutstandingSummary", outstandingsummaryList);
        outMap.put("AmountSummary", amtsummaryList);
        System.out.println("[ECOM] getOutstandingDetailsfromcuststmtsp final - outstandingsummaryList size: " + outstandingsummaryList.size());
        return outMap;
    }

    private double parseDoubleSafe(String value) {
        try {
            if (value != null && !value.trim().isEmpty() && !value.equalsIgnoreCase("null")) {
                return Double.parseDouble(value.trim());
            }
        } catch (NumberFormatException e) {
            // ignore
        }
        return 0.0;
    }

    @Override
    public List<PaymentHistoryVO> getpaymentHistoryDetails(String custCode, String fromDate, String toDate, String bookNo, String paymentHistoryCompCode) throws RemoteException {
        List<PaymentHistoryVO> paymentHistoryList = new ArrayList<>();
        Gson gson = new Gson(); 
        System.out.println("getpaymentHistoryDetails ==================>>>" + custCode + " " + fromDate + " " + toDate + " " + bookNo);
        try {
            for (String companyCode : paymentHistoryCompCode.split(",")) {
                SvmJsonList alist = new SvmJsonList();
                alist.add(companyCode);
                alist.add("CUSTOMER");
                alist.add(bookNo);
                alist.add(custCode);
                alist.add(fromDate);
                alist.add(toDate);
                System.out.println("getpaymentHistoryDetails ==================>>>" + alist);
                Map resultMap = phase2VdsService.getEcomPaymentHistory(alist);
                System.out.println("getpaymentHistoryDetails ==================>>>" + resultMap);
                if (resultMap != null) {
                    resultMap = (Map) resultMap.get("rs");
                    if (resultMap != null) {
                        List<PaymentHistoryVO> companyList = gson.fromJson(
                                gson.toJson((ArrayList) resultMap.get("rs0")),
                                new TypeToken<List<PaymentHistoryVO>>() {}.getType());
                        if (companyList != null) {
                            paymentHistoryList.addAll(companyList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentHistoryList;
    }

    private void logPrinter(String message) {
        if (GeneralConf.Enable_Sout_Log) {
            System.out.println("[ECOM] EcomPhase2Bean logPrinter - " + message);
        }
    }

}