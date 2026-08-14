/*
 * ColumnList.java
 *
 * Created on March 29, 2008, 11:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author udhaya
 */
public class ColumnList implements Serializable  {
    
    private String columnName="";
    private String columnType="";
    private String code="";
    private int columnDisplaySize=0;
    /** Creates a new instance of ColumnList */
    public ColumnList() {
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public int getColumnDisplaySize() {
        return columnDisplaySize;
    }

    public void setColumnDisplaySize(int columnDisplaySize) {
        this.columnDisplaySize = columnDisplaySize;
    }
    public void setCode(String code){
        this.code=code;
    }
    public String getCode(){
        return code;
    }
    
}
