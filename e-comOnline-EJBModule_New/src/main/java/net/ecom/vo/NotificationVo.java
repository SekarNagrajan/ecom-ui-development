/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class NotificationVo implements Serializable {

    String message = null;
    String notificationdate = null;
    String msgid = null;
    boolean isviewed = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationdate() {
        return notificationdate;
    }

    public void setNotificationdate(String notificationdate) {
        this.notificationdate = notificationdate;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public boolean isIsviewed() {
        return isviewed;
    }

    public void setIsviewed(boolean isviewed) {
        this.isviewed = isviewed;
    }

}
