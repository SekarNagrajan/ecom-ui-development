/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.insurance.vo;

import net.ecom.vo.SurveyList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gobinath.t
 */
public class Survey {

    private int version;
    private ArrayList<SurveyList> surveyOptionSelections;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ArrayList<SurveyList> getSurveyOptionSelections() {
        return surveyOptionSelections;
    }

    public void setSurveyOptionSelections(ArrayList<SurveyList> surveyOptionSelections) {
        this.surveyOptionSelections = surveyOptionSelections;
    }

}
