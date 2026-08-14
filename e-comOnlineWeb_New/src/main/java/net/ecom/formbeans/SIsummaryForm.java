/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.formbeans;

import java.util.ArrayList;
/**
 *
 * @author arunprasath
 */
public class SIsummaryForm extends org.apache.struts.action.ActionForm
{
    
     private ArrayList results;

    /**
     * @return the results
     */
    public ArrayList getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(ArrayList results) {
        this.results = results;
    }
}
