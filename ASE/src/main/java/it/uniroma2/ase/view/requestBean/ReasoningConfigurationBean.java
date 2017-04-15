/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.view.requestBean;

/**
 *
 * @author F.Camerlengo
 */
public class ReasoningConfigurationBean extends RequestBean{
    
    private Integer numberOfExecutions;
    
    private boolean unset;
    
    private String rulesApply;
    
    private boolean allRules;

    public Integer getNumberOfExecutions() {
        return numberOfExecutions;
    }

    public void setNumberOfExecutions(Integer numberOfExecutions) {
        this.numberOfExecutions = numberOfExecutions;
    }

    public boolean isUnset() {
        return unset;
    }

    public void setUnset(boolean unset) {
        this.unset = unset;
    }

    public String getRulesApply() {
        return rulesApply;
    }

    public void setRulesApply(String rulesApply) {
        this.rulesApply = rulesApply;
    }

    public boolean isAllRules() {
        return allRules;
    }

    public void setAllRules(boolean allRules) {
        this.allRules = allRules;
    }

    public String getStartButton() {
        return startButton;
    }

    public void setStartButton(String startButton) {
        this.startButton = startButton;
    }
    
    
}
