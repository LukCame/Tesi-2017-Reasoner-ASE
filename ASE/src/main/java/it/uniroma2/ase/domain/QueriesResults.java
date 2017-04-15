package it.uniroma2.ase.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.rdf4j.model.Statement;
import org.springframework.beans.factory.support.ManagedMap;

/**
 *
 * @author F.Camerlengo
 */
public class QueriesResults {

    private int numberOfExecution;
    
    private int numberOfInferredTriples;
    
    private int numberOfInferredGraphs;
    
    private int numberOfInconsistencies;
    
    List<Path> inconsistenciesList;
    
    List<Statement> ontologyTriple;
    
    private Map<Statement, List<Path>> resultingStatement;
    
    private Map<Statement, List<Path>> resultingOntologyStatement;

    private Map<Statement, List<Statement>> reverseReasoningResult;

    private Map<Graph, List<Path>> resultingGraph;
    
    private Map<Graph, List<Path>> resultingOntologyGraph;
    
    private Map<Statement,List<Graph>> reverseReasoningGraphResult;

    public int getNumberOfExecution() {
        return numberOfExecution;
    }

    public QueriesResults() {
    }

    public QueriesResults(QueriesResults copyInstance) {
        this.resultingStatement=new HashMap<>(copyInstance.resultingStatement);
        this.resultingGraph=new HashMap<>(copyInstance.resultingGraph);
        this.resultingOntologyStatement=new HashMap<>(copyInstance.resultingOntologyStatement);
        this.resultingOntologyGraph=new HashMap<>(copyInstance.resultingOntologyGraph);
        this.reverseReasoningResult=new HashMap<>(copyInstance.reverseReasoningResult);
        this.reverseReasoningGraphResult=new HashMap<>(copyInstance.reverseReasoningGraphResult);
        this.ontologyTriple=new ArrayList<>(copyInstance.ontologyTriple);
    }

    

 

    public void setNumberOfExecution(int numberOfExecution) {
        this.numberOfExecution = numberOfExecution;
    }

    public int getNumberOfInferredTriples() {
        return numberOfInferredTriples;
    }

    public void setNumberOfInferredTriples(int numberOfInferredTriples) {
        this.numberOfInferredTriples = numberOfInferredTriples;
    }

    public int getNumberOfInferredGraphs() {
        return numberOfInferredGraphs;
    }

    public void setNumberOfInferredGraphs(int numberOfInferredGraphs) {
        this.numberOfInferredGraphs = numberOfInferredGraphs;
    }

    public int getNumberOfInconsistencies() {
        return numberOfInconsistencies;
    }

    public void setNumberOfInconsistencies(int numberOfInconsistencies) {
        this.numberOfInconsistencies = numberOfInconsistencies;
    }

    public List<Path> getInconsistenciesList() {
        return inconsistenciesList;
    }

    public void setInconsistenciesList(List<Path> inconsistenciesList) {
        this.inconsistenciesList = inconsistenciesList;
    }

    public List<Statement> getOntologyTriple() {
        return ontologyTriple;
    }

    public void setOntologyTriple(List<Statement> ontologyTriple) {
        this.ontologyTriple = ontologyTriple;
    }
    
    public Map<Statement, List<Path>> getResultingStatement() {
        return resultingStatement;
    }

    public void setResultingStatement(Map<Statement, List<Path>> resultingStatement) {
        this.resultingStatement = resultingStatement;
    }

    public Map<Statement, List<Path>> getResultingOntologyStatement() {
        return resultingOntologyStatement;
    }

    public void setResultingOntologyStatement(Map<Statement, List<Path>> resultingOntologyStatement) {
        this.resultingOntologyStatement = resultingOntologyStatement;
    }

    public Map<Graph, List<Path>> getResultingGraph() {
        return resultingGraph;
    }

    public void setResultingGraph(Map<Graph, List<Path>> resultingGraph) {
        this.resultingGraph = resultingGraph;
    }

    public Map<Graph, List<Path>> getResultingOntologyGraph() {
        return resultingOntologyGraph;
    }

    public void setResultingOntologyGraph(Map<Graph, List<Path>> resultingOntologyGraph) {
        this.resultingOntologyGraph = resultingOntologyGraph;
    }
        
    public Map<Statement, List<Statement>> getReverseReasoningResult() {
        return reverseReasoningResult;
    }

    public void setReverseReasoningResult(Map<Statement, List<Statement>> reverseResoningResult) {
        this.reverseReasoningResult = reverseResoningResult;
    }

    public Map<Statement, List<Graph>> getReverseReasoningGraphResult() {
        return reverseReasoningGraphResult;
    }

    public void setReverseReasoningGraphResult(Map<Statement, List<Graph>> reverseReasoningGraphResult) {
        this.reverseReasoningGraphResult = reverseReasoningGraphResult;
    }

    
   
}
