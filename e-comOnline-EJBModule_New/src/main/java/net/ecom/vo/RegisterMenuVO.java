/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author mahalakshmi.r
 */
public class RegisterMenuVO implements Serializable {

    private int MenuId;
    private String MenuName;
    private String DevelopedBy;
    private String MenuStatus;
    private String Date;
    private String RefNo;
    private String Category;
    private String CreatedBy;
    private String CrDate;
    private String CrUsr;
    private String ChgUsr;
    private String ChgDate;
    private int MaxId;
    private String Custid;
    private String Message;
    private String UserType;
    private String menuOrder;
    private String attrValue;
    private String labelValue;
    private String classValue;
    private String menuType;
    private String parentMenu;
    private String screenType;

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(String parentMenu) {
        this.parentMenu = parentMenu;
    }

    public String getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(String menuOrder) {
        this.menuOrder = menuOrder;
    }

    /**
     * @return the MenuId
     */
    public int getMenuId() {
        return MenuId;
    }

    /**
     * @param MenuId the MenuId to set
     */
    public void setMenuId(int MenuId) {
        this.MenuId = MenuId;
    }

    /**
     * @return the MenuName
     */
    public String getMenuName() {
        return MenuName;
    }

    /**
     * @param MenuName the MenuName to set
     */
    public void setMenuName(String MenuName) {
        this.MenuName = MenuName;
    }

    /**
     * @return the DevelopedBy
     */
    public String getDevelopedBy() {
        return DevelopedBy;
    }

    /**
     * @param DevelopedBy the DevelopedBy to set
     */
    public void setDevelopedBy(String DevelopedBy) {
        this.DevelopedBy = DevelopedBy;
    }

    /**
     * @return the MenuStatus
     */
    public String getMenuStatus() {
        return MenuStatus;
    }

    /**
     * @param MenuStatus the MenuStatus to set
     */
    public void setMenuStatus(String MenuStatus) {
        this.MenuStatus = MenuStatus;
    }

    /**
     * @return the Date
     */
    public String getDate() {
        return Date;
    }

    /**
     * @param Date the Date to set
     */
    public void setDate(String Date) {
        this.Date = Date;
    }

    /**
     * @return the RefNo
     */
    public String getRefNo() {
        return RefNo;
    }

    /**
     * @param RefNo the RefNo to set
     */
    public void setRefNo(String RefNo) {
        this.RefNo = RefNo;
    }

    /**
     * @return the Category
     */
    public String getCategory() {
        return Category;
    }

    /**
     * @param Category the Category to set
     */
    public void setCategory(String Category) {
        this.Category = Category;
    }

    /**
     * @return the CreatedBy
     */
    public String getCreatedBy() {
        return CreatedBy;
    }

    /**
     * @param CreatedBy the CreatedBy to set
     */
    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    /**
     * @return the CrDate
     */
    public String getCrDate() {
        return CrDate;
    }

    /**
     * @param CrDate the CrDate to set
     */
    public void setCrDate(String CrDate) {
        this.CrDate = CrDate;
    }

    /**
     * @return the CrUser
     */
    public String getCrUsr() {
        return CrUsr;
    }

    /**
     * @param CrUser the CrUser to set
     */
    public void setCrUsr(String CrUsr) {
        this.CrUsr = CrUsr;
    }

    /**
     * @return the ChgUsr
     */
    public String getChgUsr() {
        return ChgUsr;
    }

    /**
     * @param ChgUsr the ChgUsr to set
     */
    public void setChgUsr(String ChgUsr) {
        this.ChgUsr = ChgUsr;
    }

    /**
     * @return the ChgDate
     */
    public String getChgDate() {
        return ChgDate;
    }

    /**
     * @param ChgDate the ChgDate to set
     */
    public void setChgDate(String ChgDate) {
        this.ChgDate = ChgDate;
    }

    /**
     * @return the MaxId
     */
    public int getMaxId() {
        return MaxId;
    }

    /**
     * @param MaxId the MaxId to set
     */
    public void setMaxId(int MaxId) {
        this.MaxId = MaxId;
    }

    /**
     * @return the Custid
     */
    public String getCustid() {
        return Custid;
    }

    /**
     * @param Custid the Custid to set
     */
    public void setCustid(String Custid) {
        this.Custid = Custid;
    }

    /**
     * @return the Message
     */
    public String getMessage() {
        return Message;
    }

    /**
     * @param Message the Message to set
     */
    public void setMessage(String Message) {
        this.Message = Message;
    }

    /**
     * @return the UserType
     */
    public String getUserType() {
        return UserType;
    }

    /**
     * @param UserType the UserType to set
     */
    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }
    
}
