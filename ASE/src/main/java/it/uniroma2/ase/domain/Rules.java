package it.uniroma2.ase.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object that stores the output of inference rules parsing.
 *
 * @author L.Camerlengo
 *
 */
public class Rules {

    private Map<String, String> prefixes;

    private List<InferenceRule> inferenceRules;

    private List<RuleGraphFromBnode> graphRules;

    public List<InferenceRule> getInferenceRules() {
        if (inferenceRules == null) {
            inferenceRules = new ArrayList<InferenceRule>();
        }
        return inferenceRules;
    }

    public void setInferenceRules(List<InferenceRule> inferenceRules) {
        this.inferenceRules = inferenceRules;
    }

    public List<RuleGraphFromBnode> getGraphRules() {
        return graphRules;
    }

    public void setGraphRules(List<RuleGraphFromBnode> graphRules) {
        this.graphRules = graphRules;
    }

    public Map<String, String> getPrefixes() {
        if (prefixes == null) {
            prefixes = new HashMap<String, String>();
        }
        return prefixes;
    }

    public void setPrefixes(Map<String, String> prefixes) {
        this.prefixes = prefixes;
    }

}
