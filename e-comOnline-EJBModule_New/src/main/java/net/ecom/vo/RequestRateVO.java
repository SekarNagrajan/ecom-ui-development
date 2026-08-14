/*
 * RequestRatedto.java
 *
 * Created on December 18, 2007, 4:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.ecom.vo;

/**
 *
 * @author udhaya
 */
public class RequestRateVO  implements java.io.Serializable {
    private int id=0;          // this is used in RateView to list details with S.No
    private String requestNumber=null;
    private String fromDate=null;
    private String toDate=null;
    private String org_country=null;
    private String org_Location=null;
    private String dest_country=null;
    private String dest_Location=null;
    private String org_Service_Term=null;
    private String dest_Service_Term=null;
    private String commodity=null;
    private String cargo_Type=null;
    private String containerType=null;
    private String volume=null;
    private String weight=null;
    private String requestRemarks=null;
    private String rateIdeaCode=null;
    private String rateIdeaName=null;
    private String blshipmentno = null;
    private String rateAmt=null;
    private String comments=null;
    private String custCode=null;
    private String personName=null;
    private String companyName=null;
    private String address=null;
    private String telephoneNumber=null;
    private String faxNumber=null;
    private String emailId=null;
    private String requestDate=null;
    private String status=null;
    private String action=null;
    private String userType=null;
    private String carrierTerms=null;    // it is used insted of originServiceTerm and DestinationServiceTerm
    private String requestStatus=null;   // it is used on Editing by line,agency,system to set status of request
    private String statusAction=null;    
    private String expectedAmt="";    //   created by:  k.udhaya   Date:20/06/08  purpose :  to stroe the expected Amount
    private String negotiateAmt="";   //   created by:  k.udhaya   Date:20/06/08  purpose :  to stroe the negotiable Amount
    private String negRemarks=""; //   created by:  k.udhaya   Date:20/06/08  purpose :  to stroe the negptiable Remarks
    private String cntType="";


    private String competitionCode="";
    private String competitionName="";
    private String competitionAmt = null;
  private String originID=null;
    private String polId=null;
    private String originTerID=null;
    private String originSerID=null;
    private String mode=null;
    private String ts1Id=null;
    private String trans1ID=null;
    private String ter1Id=null;
    private String ser1Id=null;
    private String ts2Id=null;
    private String trans2Id=null;
    private String ter2Id=null;
    private String ser2Id=null;
    private String ts3Id=null;
    private String trans3Id=null;
    private String ter3Id=null;
    private String ser3Id=null;
    private String podId=null;
    private String podTerId=null;
    private String destId=null;
    private String deftrouteId=null;
    private String mainSer=null;
    private String originModeId=null;
    private String deliveryMode=null;
    private String routingHiddenDetails=null;
    private String hdn_cargotype=null;
    
    private StringBuffer sb=new StringBuffer("");
    /** Creates a new instance of RequestRatedto */
    public RequestRateVO() {
    }

  
    public void setStatusAction(String statusAction){
        this.statusAction=statusAction;
    }
    public String getStatusAction(){
        if(getStatus().equalsIgnoreCase("R")){
            statusAction="Rejected";
        }
        else if(getStatus().equalsIgnoreCase("A")){
            statusAction="Answered";
        }
        else if(getStatus().equalsIgnoreCase("C") || getStatus().equalsIgnoreCase("N")){
            statusAction="Counter Offer";
        }
        else if(getStatus().equalsIgnoreCase("D")){
            statusAction="Request";
        }
    
        return statusAction;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the requestNumber
     */
    public String getRequestNumber() {
        return requestNumber;
    }

    /**
     * @param requestNumber the requestNumber to set
     */
    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    /**
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the org_country
     */
    public String getOrg_country() {
        return org_country;
    }

    /**
     * @param org_country the org_country to set
     */
    public void setOrg_country(String org_country) {
        this.org_country = org_country;
    }

    /**
     * @return the org_Location
     */
    public String getOrg_Location() {
        return org_Location;
    }

    /**
     * @param org_Location the org_Location to set
     */
    public void setOrg_Location(String org_Location) {
        this.org_Location = org_Location;
    }

    /**
     * @return the dest_country
     */
    public String getDest_country() {
        return dest_country;
    }

    /**
     * @param dest_country the dest_country to set
     */
    public void setDest_country(String dest_country) {
        this.dest_country = dest_country;
    }

    /**
     * @return the dest_Location
     */
    public String getDest_Location() {
        return dest_Location;
    }

    /**
     * @param dest_Location the dest_Location to set
     */
    public void setDest_Location(String dest_Location) {
        this.dest_Location = dest_Location;
    }

    /**
     * @return the org_Service_Term
     */
    public String getOrg_Service_Term() {
        return org_Service_Term;
    }

    /**
     * @param org_Service_Term the org_Service_Term to set
     */
    public void setOrg_Service_Term(String org_Service_Term) {
        this.org_Service_Term = org_Service_Term;
    }

    /**
     * @return the dest_Service_Term
     */
    public String getDest_Service_Term() {
        return dest_Service_Term;
    }

    /**
     * @param dest_Service_Term the dest_Service_Term to set
     */
    public void setDest_Service_Term(String dest_Service_Term) {
        this.dest_Service_Term = dest_Service_Term;
    }

    /**
     * @return the commodity
     */
    public String getCommodity() {
        return commodity;
    }

    /**
     * @param commodity the commodity to set
     */
    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    /**
     * @return the cargoType
     */
    public String getCargo_Type() {
        return cargo_Type;
    }

    /**
     * @param cargoType the cargoType to set
     */
    public void setCargo_Type(String cargo_Type) {
        this.cargo_Type = cargo_Type;
    }

    /**
     * @return the containerType
     */
    public String getContainerType() {
        return containerType;
    }

    /**
     * @param containerType the containerType to set
     */
    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    /**
     * @return the volume
     */
    public String getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(String volume) {
        this.volume = volume;
    }

    /**
     * @return the weight
     */
    public String getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }

    /**
     * @return the rateIdeaCode
     */
    public String getRateIdeaCode() {
        return rateIdeaCode;
    }

    /**
     * @param rateIdeaCode the rateIdeaCode to set
     */
    public void setRateIdeaCode(String rateIdeaCode) {
        this.rateIdeaCode = rateIdeaCode;
    }

    /**
     * @return the rateIdeaName
     */
    public String getRateIdeaName() {
        return rateIdeaName;
    }

    /**
     * @param rateIdeaName the rateIdeaName to set
     */
    public void setRateIdeaName(String rateIdeaName) {
        this.rateIdeaName = rateIdeaName;
    }

    /**
     * @return the rateAmt
     */
    public String getRateAmt() {
        return rateAmt;
    }

    /**
     * @param rateAmt the rateAmt to set
     */
    public void setRateAmt(String rateAmt) {
        this.rateAmt = rateAmt;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the custCode
     */
    public String getCustCode() {
        return custCode;
    }

    /**
     * @param custCode the custCode to set
     */
    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    /**
     * @return the personName
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName the personName to set
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the telephoneNumber
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * @param telephoneNumber the telephoneNumber to set
     */
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * @return the faxNumber
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * @param faxNumber the faxNumber to set
     */
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * @return the emailId
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * @param emailId the emailId to set
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * @return the requestDate
     */
    public String getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate the requestDate to set
     */
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the action
     */
    public String getAction() {

         if(("C".equalsIgnoreCase(status))){
               sb.append("<a href=\"javascript:void(0);\"");
               sb.append(" onclick=\"popUpWindow('MenuController.do?type=ratedetailview&reqno="+requestNumber+"',150,100,800,500)\"");
               sb.append(">View");
               sb.append("</a>");
               sb.append("&nbsp;|&nbsp;<a href=\"RateRequestEdit.do?type=ratedetailedit&reqno="+requestNumber+"\">Edit");
               sb.append("</a>");
            }
            else{
               sb.append("<a href=\"javascript:void(0);\"");
               sb.append(" onclick=\"popUpWindow('MenuController.do?type=ratedetailview&reqno="+requestNumber+"',150,100,800,500)\"");
               sb.append(">View");
               sb.append("</a>");
            }

       //System.out.println("[ECOM] RequestRateVO Action"+sb.toString());
       return sb.toString();
      
    }

    /**
     * @param action the action to set
     */
    public void setAction(String status) {
        this.action=status;
    }

    /**
     * @return the userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * @return the carrierTerms
     */
    public String getCarrierTerms() {
        return carrierTerms;
    }

    /**
     * @param carrierTerms the carrierTerms to set
     */
    public void setCarrierTerms(String carrierTerms) {
        this.carrierTerms = carrierTerms;
    }

    /**
     * @return the requestStatus
     */
    public String getRequestStatus() {
        return requestStatus;
    }

    /**
     * @param requestStatus the requestStatus to set
     */
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    /**
     * @return the requestRemarks
     */
    public String getRequestRemarks() {
        return requestRemarks;
    }

    /**
     * @param requestRemarks the requestRemarks to set
     */
    public void setRequestRemarks(String requestRemarks) {
        this.requestRemarks = requestRemarks;
    }

    /**
     * @return the expectedAmt
     */
    public String getExpectedAmt() {
        return expectedAmt;
    }

    /**
     * @param expectedAmt the expectedAmt to set
     */
    public void setExpectedAmt(String expectedAmt) {
        this.expectedAmt = expectedAmt;
    }

    /**
     * @return the negotiateAmt
     */
    public String getNegotiateAmt() {
        return negotiateAmt;
    }

    /**
     * @param negotiateAmt the negotiateAmt to set
     */
    public void setNegotiateAmt(String negotiateAmt) {
        this.negotiateAmt = negotiateAmt;
    }

    /**
     * @return the negRemarks
     */
    public String getNegRemarks() {
        return negRemarks;
    }

    /**
     * @param negRemarks the negRemarks to set
     */
    public void setNegRemarks(String negRemarks) {
        this.negRemarks = negRemarks;
    }

    /**
     * @return the cntType
     */
    public String getCntType() {
        return cntType;
    }

    /**
     * @param cntType the cntType to set
     */
    public void setCntType(String cntType) {
        this.cntType = cntType;
    }

    /**
     * @return the competitionCode
     */
    public String getCompetitionCode() {
        return competitionCode;
    }

    /**
     * @param competitionCode the competitionCode to set
     */
    public void setCompetitionCode(String competitionCode) {
        this.competitionCode = competitionCode;
    }

    /**
     * @return the competitionName
     */
    public String getCompetitionName() {
        return competitionName;
    }

    /**
     * @param competitionName the competitionName to set
     */
    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    /**
     * @return the competitionAmt
     */
    public String getCompetitionAmt() {
        return competitionAmt;
    }

    /**
     * @param competitionAmt the competitionAmt to set
     */
    public void setCompetitionAmt(String competitionAmt) {
        this.competitionAmt = competitionAmt;
    }

    /**
     * @return the origin
     */
   
    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the ts1
     */
   
    /**
     * @return the deliveryMode
     */
    public String getDeliveryMode() {
        return deliveryMode;
    }

    /**
     * @param deliveryMode the deliveryMode to set
     */
    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    /**
     * @return the sb
     */
    public StringBuffer getSb() {
        return sb;
    }

    /**
     * @param sb the sb to set
     */
    public void setSb(StringBuffer sb) {
        this.sb = sb;
    }

    /**
     * @return the originID
     */
    public String getOriginID() {
        return originID;
    }

    /**
     * @param originID the originID to set
     */
    public void setOriginID(String originID) {
        this.originID = originID;
    }

    /**
     * @return the polId
     */
    public String getPolId() {
        return polId;
    }

    /**
     * @param polId the polId to set
     */
    public void setPolId(String polId) {
        this.polId = polId;
    }

    /**
     * @return the originTerID
     */
    public String getOriginTerID() {
        return originTerID;
    }

    /**
     * @param originTerID the originTerID to set
     */
    public void setOriginTerID(String originTerID) {
        this.originTerID = originTerID;
    }

    /**
     * @return the originSerID
     */
    public String getOriginSerID() {
        return originSerID;
    }

    /**
     * @param originSerID the originSerID to set
     */
    public void setOriginSerID(String originSerID) {
        this.originSerID = originSerID;
    }

    /**
     * @return the ts1Id
     */
    public String getTs1Id() {
        return ts1Id;
    }

    /**
     * @param ts1Id the ts1Id to set
     */
    public void setTs1Id(String ts1Id) {
        this.ts1Id = ts1Id;
    }

    /**
     * @return the trans1ID
     */
    public String getTrans1ID() {
        return trans1ID;
    }

    /**
     * @param trans1ID the trans1ID to set
     */
    public void setTrans1ID(String trans1ID) {
        this.trans1ID = trans1ID;
    }

    /**
     * @return the ter1Id
     */
    public String getTer1Id() {
        return ter1Id;
    }

    /**
     * @param ter1Id the ter1Id to set
     */
    public void setTer1Id(String ter1Id) {
        this.ter1Id = ter1Id;
    }

    /**
     * @return the ser1Id
     */
    public String getSer1Id() {
        return ser1Id;
    }

    /**
     * @param ser1Id the ser1Id to set
     */
    public void setSer1Id(String ser1Id) {
        this.ser1Id = ser1Id;
    }

    /**
     * @return the ts2Id
     */
    public String getTs2Id() {
        return ts2Id;
    }

    /**
     * @param ts2Id the ts2Id to set
     */
    public void setTs2Id(String ts2Id) {
        this.ts2Id = ts2Id;
    }

    /**
     * @return the trans2Id
     */
    public String getTrans2Id() {
        return trans2Id;
    }

    /**
     * @param trans2Id the trans2Id to set
     */
    public void setTrans2Id(String trans2Id) {
        this.trans2Id = trans2Id;
    }

    /**
     * @return the ter2Id
     */
    public String getTer2Id() {
        return ter2Id;
    }

    /**
     * @param ter2Id the ter2Id to set
     */
    public void setTer2Id(String ter2Id) {
        this.ter2Id = ter2Id;
    }

    /**
     * @return the ser2Id
     */
    public String getSer2Id() {
        return ser2Id;
    }

    /**
     * @param ser2Id the ser2Id to set
     */
    public void setSer2Id(String ser2Id) {
        this.ser2Id = ser2Id;
    }

    /**
     * @return the ts3Id
     */
    public String getTs3Id() {
        return ts3Id;
    }

    /**
     * @param ts3Id the ts3Id to set
     */
    public void setTs3Id(String ts3Id) {
        this.ts3Id = ts3Id;
    }

    /**
     * @return the trans3Id
     */
    public String getTrans3Id() {
        return trans3Id;
    }

    /**
     * @param trans3Id the trans3Id to set
     */
    public void setTrans3Id(String trans3Id) {
        this.trans3Id = trans3Id;
    }

    /**
     * @return the ter3Id
     */
    public String getTer3Id() {
        return ter3Id;
    }

    /**
     * @param ter3Id the ter3Id to set
     */
    public void setTer3Id(String ter3Id) {
        this.ter3Id = ter3Id;
    }

    /**
     * @return the ser3Id
     */
    public String getSer3Id() {
        return ser3Id;
    }

    /**
     * @param ser3Id the ser3Id to set
     */
    public void setSer3Id(String ser3Id) {
        this.ser3Id = ser3Id;
    }

    /**
     * @return the podId
     */
    public String getPodId() {
        return podId;
    }

    /**
     * @param podId the podId to set
     */
    public void setPodId(String podId) {
        this.podId = podId;
    }

    /**
     * @return the podTerId
     */
    public String getPodTerId() {
        return podTerId;
    }

    /**
     * @param podTerId the podTerId to set
     */
    public void setPodTerId(String podTerId) {
        this.podTerId = podTerId;
    }

    /**
     * @return the destId
     */
    public String getDestId() {
        return destId;
    }

    /**
     * @param destId the destId to set
     */
    public void setDestId(String destId) {
        this.destId = destId;
    }

    /**
     * @return the deftrouteId
     */
    public String getDeftrouteId() {
        return deftrouteId;
    }

    /**
     * @param deftrouteId the deftrouteId to set
     */
    public void setDeftrouteId(String deftrouteId) {
        this.deftrouteId = deftrouteId;
    }

    /**
     * @return the mainSer
     */
    public String getMainSer() {
        return mainSer;
    }

    /**
     * @param mainSer the mainSer to set
     */
    public void setMainSer(String mainSer) {
        this.mainSer = mainSer;
    }

    /**
     * @return the originModeId
     */
    public String getOriginModeId() {
        return originModeId;
    }

    /**
     * @param originModeId the originModeId to set
     */
    public void setOriginModeId(String originModeId) {
        this.originModeId = originModeId;
    }

    /**
     * @return the routingHiddenDetails
     */
    public String getRoutingHiddenDetails() {
        return routingHiddenDetails;
    }

    /**
     * @param routingHiddenDetails the routingHiddenDetails to set
     */
    public void setRoutingHiddenDetails(String routingHiddenDetails) {
        this.routingHiddenDetails = routingHiddenDetails;
    }

    /**
     * @return the blshipmentno
     */
    public String getBlshipmentno() {
        return blshipmentno;
    }

    /**
     * @param blshipmentno the blshipmentno to set
     */
    public void setBlshipmentno(String blshipmentno) {
        this.blshipmentno = blshipmentno;
    }

    /**
     * @return the hdn_cargotype
     */
    public String getHdn_cargotype() {
        return hdn_cargotype;
    }

    /**
     * @param hdn_cargotype the hdn_cargotype to set
     */
    public void setHdn_cargotype(String hdn_cargotype) {
        this.hdn_cargotype = hdn_cargotype;
    }
 
}
