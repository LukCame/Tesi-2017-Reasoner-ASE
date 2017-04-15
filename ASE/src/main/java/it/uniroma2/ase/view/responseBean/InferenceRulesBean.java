/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.view.responseBean;

import it.uniroma2.ase.domain.Rules;

/**
 *
 * @author F.Camerlengo
 */
public class InferenceRulesBean {
 
    private String fileName;
    
    private String filePath;
    
    private Rules rules;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }
    
    
    
}
