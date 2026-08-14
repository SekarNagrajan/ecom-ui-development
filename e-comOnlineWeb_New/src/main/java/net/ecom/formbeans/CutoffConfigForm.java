/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author deepa.p
 */
public class CutoffConfigForm extends org.apache.struts.action.ActionForm {

    private String name;
    private int number;
    private String port = null;
    private String terminal = null;
    private String cfsClosing = null;
    private String vgmClosing = null;
    private String documentClosing = null;
    private String ediDecClosing = null;
    private String fullCntrGateClosing = null;
    private String saveType = null;

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public String getEdiDecClosing() {
        return ediDecClosing;
    }

    public void setEdiDecClosing(String ediDecClosing) {
        this.ediDecClosing = ediDecClosing;
    }

    public String getFullCntrGateClosing() {
        return fullCntrGateClosing;
    }

    public void setFullCntrGateClosing(String fullCntrGateClosing) {
        this.fullCntrGateClosing = fullCntrGateClosing;
    }

    public String getDocumentClosing() {
        return documentClosing;
    }

    public void setDocumentClosing(String documentClosing) {
        this.documentClosing = documentClosing;
    }

    public String getCfsClosing() {
        return cfsClosing;
    }

    public void setCfsClosing(String cfsClosing) {
        this.cfsClosing = cfsClosing;
    }

    public String getVgmClosing() {
        return vgmClosing;
    }

    public void setVgmClosing(String vgmClosing) {
        this.vgmClosing = vgmClosing;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param i
     */
    public void setNumber(int i) {
        number = i;
    }

    /**
     *
     */
    public CutoffConfigForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getName() == null || getName().length() < 1) {
            errors.add("name", new ActionMessage("error.name.required"));
            // TODO: add 'error.name.required' key to your resources
        }
        return errors;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        System.out.println("[ECOM-WAR] CutoffConfigForm >>entered in cut off reset");
        setPort("");
        setTerminal("");
        setCfsClosing("");
        setVgmClosing("");
        setDocumentClosing("");
        setFullCntrGateClosing("");
        setEdiDecClosing("");
        setSaveType("");
    }
}
