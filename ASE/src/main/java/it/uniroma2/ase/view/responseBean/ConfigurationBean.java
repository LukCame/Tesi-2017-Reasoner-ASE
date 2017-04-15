/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.view.responseBean;

import java.util.List;

/**
 *
 * @author F.Camerlengo
 */
public class ConfigurationBean {
    
    private Integer numberOfExecution;
    
    private List<Integer> rulesId;

    public Integer getNumberOfExecution() {
        return numberOfExecution;
    }

    public void setNumberOfExecution(Integer numberOfExecution) {
        this.numberOfExecution = numberOfExecution;
    }

    public List<Integer> getRulesId() {
        return rulesId;
    }

    public void setRulesId(List<Integer> rulesId) {
        this.rulesId = rulesId;
    }
    
    
}
