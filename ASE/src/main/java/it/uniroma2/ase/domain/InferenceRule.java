package it.uniroma2.ase.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain object that represents the Inference Rule. Each Inference rule has an
 * ID,name and a list of premises and conclusions.
 *
 * @author L.Camerlengo
 *
 */
public class InferenceRule {

    private Type type;

    private int inferenceRuleID;

    private String inferenceRuleName;

    private List<Triple> premisesTriple;
    
    private List<Triple> rewritePremisesTriple;
    
    private List<Triple> conclusionsTriple;

    private List<Triple> rewriteConclusionsTriple;
    
    private String filterCondition;

    private boolean hasList;
        
    public List<Triple> getPremisesTriple() {
        if (premisesTriple == null) {
            premisesTriple = new ArrayList<Triple>();
        }
        return premisesTriple;
    }

    public void setPremisesTriple(List<Triple> premisesTriple) {
        this.premisesTriple = premisesTriple;
    }

    public List<Triple> getConclusionsTriple() {
        if (conclusionsTriple == null) {
            conclusionsTriple = new ArrayList<Triple>();
        }
        return conclusionsTriple;
    }

    public void setConclusionsTriple(List<Triple> conclusionsTriple) {
        this.conclusionsTriple = conclusionsTriple;
    }

    public int getInferenceRuleID() {
        return inferenceRuleID;
    }

    public void setInferenceRuleID(int inferenceRuleID) {
        this.inferenceRuleID = inferenceRuleID;
    }

    public String getInferenceRuleName() {
        return inferenceRuleName;
    }

    public void setInferenceRuleName(String inferenceRuleName) {
        this.inferenceRuleName = inferenceRuleName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition;
    }

    public boolean HasList() {
        return hasList;
    }

    public void setHasList(boolean hasList) {
        this.hasList = hasList;
    }

    public List<Triple> getRewritePremisesTriple() {
        return rewritePremisesTriple;
    }

    public void setRewritePremisesTriple(List<Triple> rewritePremisesTriple) {
        this.rewritePremisesTriple = rewritePremisesTriple;
    }

    public List<Triple> getRewriteConclusionsTriple() {
        return rewriteConclusionsTriple;
    }

    public void setRewriteConclusionsTriple(List<Triple> rewriteConclusionsTriple) {
        this.rewriteConclusionsTriple = rewriteConclusionsTriple;
    }
    
}
