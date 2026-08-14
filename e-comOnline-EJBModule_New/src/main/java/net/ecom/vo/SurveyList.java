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
public class SurveyList implements Serializable{

    private String surveyOptionCode;
    private String surveyOptionValue;
    private int version;

    public String getSurveyOptionCode() {
        return surveyOptionCode;
    }

    public void setSurveyOptionCode(String surveyOptionCode) {
        this.surveyOptionCode = surveyOptionCode;
    }

    public String getSurveyOptionValue() {
        return surveyOptionValue;
    }

    public void setSurveyOptionValue(String surveyOptionValue) {
        this.surveyOptionValue = surveyOptionValue;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
