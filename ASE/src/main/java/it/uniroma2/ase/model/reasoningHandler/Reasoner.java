package it.uniroma2.ase.model.reasoningHandler;

import it.uniroma2.ase.domain.Graph;
import it.uniroma2.ase.domain.InferenceRule;
import it.uniroma2.ase.domain.Path;
import it.uniroma2.ase.domain.QueriesResults;
import it.uniroma2.ase.domain.RuleGraphFromBnode;
import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.utility.OntologyUtility;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

/**
 *
 * @author L.Camerlengo
 */
public class Reasoner {
    
    /**
     * Execute reasoning process and return the results of her application 
     * @param repository
     * @param rules
     * @param numberOfExecution
     * @param rulesId
     * @return QueriesResults the results of reasoning process
     */
    public QueriesResults reasoning(RepositoryConnection repository,Rules rules,Integer numberOfExecution,List<Integer> rulesId) {
        // define all object needed for reasoning process
        QueriesResults queriesResults=new QueriesResults();
        ExecutionQuery executionQuery = new ExecutionQuery();
        List<Path> inconsistenciesList=new ArrayList<>();
        Map<Statement, List<Path>> resultingStatement = new HashMap<>();
        Map<Statement, List<Path>> resultingOntologyStatement=new HashMap<>();
        Map<Statement, List<Statement>> reverseReasoningResults = new HashMap<>();
        Map<Graph, List<Path>> resultingGraph=new HashMap<>();
        Map<Graph, List<Path>> resultingOntologyGraph=new HashMap<>();
        Map<Statement,List<Graph>> reverseGraphReasoning=new HashMap<>();
        List<InferenceRule> inferenceRulesList = rules.getInferenceRules();
        List<RuleGraphFromBnode> graphRulesList = rules.getGraphRules();
        List<Object> rulesList=new ArrayList<>();
        rulesList.addAll(inferenceRulesList);
        rulesList.addAll(graphRulesList);
        List<Statement> statementToBeRemove = new ArrayList<>();
        if(numberOfExecution==null){
            numberOfExecution = 10000;
        }
        int count;
        
        
        long firstTime=System.currentTimeMillis();
        long lastTime=firstTime;
        // iterations of reasoning process
        for (count = 1; count <= numberOfExecution; count++) {
            boolean thereIsAtLeastOneInference = false;
            //for each rule
            for (Object rule : rulesList) {
                if (rule instanceof RuleGraphFromBnode) {
                    RuleGraphFromBnode ruleGraph=(RuleGraphFromBnode) rule;
                    if(rulesId!=null){
                        if(rulesId.contains(ruleGraph.getInferenceRuleInfo().getInferenceRuleID())){
                            boolean thereIsAnInference=executionQuery.executeRuleGraph(ruleGraph, rules.getPrefixes(), repository,resultingStatement,resultingOntologyStatement,reverseReasoningResults, resultingGraph,resultingOntologyGraph,reverseGraphReasoning);
                            thereIsAtLeastOneInference= thereIsAtLeastOneInference || thereIsAnInference;
                        }
                    }else{
                    boolean thereIsAnInference=executionQuery.executeRuleGraph(ruleGraph, rules.getPrefixes(), repository,resultingStatement,resultingOntologyStatement,reverseReasoningResults, resultingGraph,resultingOntologyGraph,reverseGraphReasoning);
                    thereIsAtLeastOneInference= thereIsAtLeastOneInference || thereIsAnInference;
                    }
                } else {
                    InferenceRule inferenceRule=(InferenceRule) rule;
                    if(rulesId!=null){
                        if(rulesId.contains(inferenceRule.getInferenceRuleID())){
                             boolean thereIsAnInference = executionQuery.executeInferenceRule(inferenceRule,rules.getPrefixes(), repository, resultingStatement,resultingOntologyStatement, reverseReasoningResults,inconsistenciesList);
                        thereIsAtLeastOneInference = thereIsAtLeastOneInference || thereIsAnInference;
                        }
                    }else{
                    boolean thereIsAnInference = executionQuery.executeInferenceRule(inferenceRule,rules.getPrefixes(), repository, resultingStatement,resultingOntologyStatement, reverseReasoningResults,inconsistenciesList);
                    thereIsAtLeastOneInference = thereIsAtLeastOneInference || thereIsAnInference;
                    }
                    }
            }
            long newTime=System.currentTimeMillis();
            System.out.println(newTime-lastTime+" it "+count);
            lastTime=newTime;
            // if there isn't at least one inference in this iteration than the reasoning process is stopped
            if (!thereIsAtLeastOneInference) {
                break;
            }
        }
        if (count > numberOfExecution) {
            count--;
        }
        repository.close();
        // all the reasoning results are stored in the apposite special data structures
        queriesResults.setNumberOfExecution(count);
        queriesResults.setNumberOfInferredTriples(resultingStatement.size());
        queriesResults.setNumberOfInferredGraphs(resultingGraph.size());
        queriesResults.setNumberOfInconsistencies(inconsistenciesList.size());
        queriesResults.setInconsistenciesList(inconsistenciesList);
        queriesResults.setResultingStatement(resultingStatement);
        queriesResults.setResultingOntologyStatement(resultingOntologyStatement);
        queriesResults.setReverseReasoningResult(reverseReasoningResults);
        queriesResults.setResultingGraph(resultingGraph);
        queriesResults.setResultingOntologyGraph(resultingOntologyGraph);
        Set<Graph> keySet = resultingOntologyGraph.keySet();
        queriesResults.setReverseReasoningGraphResult(reverseGraphReasoning); 
        return queriesResults;
    }

}
