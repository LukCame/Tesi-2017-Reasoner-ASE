package it.uniroma2.ase.domain;

import java.util.List;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Statement;

/**
 *
 * @author L.Camerlengo
 */
public class RuleGraphFromBnode {

    private List<Graph> graphList;

    private InferenceRule inferenceRuleInfo;

    public boolean subjectIsBlank(Statement statement) {
        if (statement.getSubject() instanceof BNode) {
            return true;
        }
        return false;
    }

    public boolean objectIsBlank(Statement statement) {
        if (statement.getObject() instanceof BNode) {
            return true;
        }
        return false;
    }

    public List<Graph> getGraphList() {
        return graphList;
    }

    public void setGraphList(List<Graph> graphList) {
        this.graphList = graphList;
    }

    public InferenceRule getInferenceRuleInfo() {
        return inferenceRuleInfo;
    }

    public void setInferenceRuleInfo(InferenceRule inferenceRuleInfo) {
        this.inferenceRuleInfo = inferenceRuleInfo;
    }
 
}
