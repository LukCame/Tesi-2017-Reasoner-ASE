/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.reasoningHandler;

import it.uniroma2.ase.domain.EnrichedStatement;
import it.uniroma2.ase.domain.Graph;
import it.uniroma2.ase.domain.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.rdf4j.model.Statement;

/**
 *
 * @author F.Camerlengo
 */
public class ReasoningWithMaps {

    private Map<Statement, List<Path>> resultingStatement;
    
    private Map<Statement, List<Path>> resultingOntologyStatement;
    
    private Map<Statement, List<Statement>> reverseReasoningResults;

    private Map<Graph, List<Path>> resultingGraph;
    
    private Map<Graph, List<Path>> resultingOntologyGraph;
    
    private Map<Statement, List<Graph>> reverseGraphReasoning;

    public Map<Statement, List<Path>> getResultingStatement() {
        return resultingStatement;
    }

    public void setResultingStatement(Map<Statement, List<Path>> resultingStatement) {
        this.resultingStatement = resultingStatement;
    }

    public Map<Statement, List<Statement>> getReverseReasoningResults() {
        return reverseReasoningResults;
    }

    public Map<Statement, List<Path>> getResultingOntologyStatement() {
        return resultingOntologyStatement;
    }

    public void setResultingOntologyStatement(Map<Statement, List<Path>> resultingOntologyStatement) {
        this.resultingOntologyStatement = resultingOntologyStatement;
    }

    public void setReverseReasoningResults(Map<Statement, List<Statement>> reverseReasoningResults) {
        this.reverseReasoningResults = reverseReasoningResults;
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

    public Map<Statement, List<Graph>> getReverseGraphReasoning() {
        return reverseGraphReasoning;
    }

    public void setReverseGraphReasoning(Map<Statement, List<Graph>> reverseGraphReasoning) {
        this.reverseGraphReasoning = reverseGraphReasoning;
    }

    public void StatementAndGraphListAgainInferable(List<Statement> statementToBeRemove) {
        for (int i = 0; i < statementToBeRemove.size(); i++) {
            Statement statement = statementToBeRemove.get(i);
            List<Statement> inferredStatement = this.reverseReasoningResults.get(statement);
            if (inferredStatement != null) {
                this.reverseReasoningResults.remove(statement, inferredStatement);
            }
            List<Graph> inferredGraph = this.reverseGraphReasoning.get(statement);
            if (inferredGraph != null) {
                if (inferredStatement == null) {
                    inferredStatement = new ArrayList<>();
                }
                for (Graph graph : inferredGraph) {
                    for (Statement stm : graph.getStatementList()) {
                        if (!inferredStatement.contains(stm)) {
                            inferredStatement.add(stm);
                        }
                    }
                }
                this.reverseGraphReasoning.remove(statement, inferredGraph);
            }
            if (inferredStatement != null) {
                for (Statement stm : inferredStatement) {
                    List<Path> pathList;
                    List<Path> newPathList;
                    if (this.resultingStatement != null&&this.resultingOntologyStatement.get(statement)==null) {
                        pathList = this.resultingStatement.get(stm);
                        if (pathList != null) {
                            newPathList = getNewPathListFromStatementToBeRemove(pathList, statement);
                            this.updateOrRemoveReasoningMap(this.resultingStatement, newPathList, stm);
                            if (!statementToBeRemove.contains(stm)) {
                                statementToBeRemove.add(stm);
                            }
                        }
                    }
                    if (this.resultingOntologyStatement != null) {
                        pathList = this.resultingOntologyStatement.get(stm);
                        if (pathList != null) {
                            newPathList = this.getNewPathListFromStatementToBeRemove(pathList, statement);
                            this.updateOrRemoveReasoningMap(this.resultingOntologyStatement, newPathList, stm);
                            if (!statementToBeRemove.contains(stm)) {
                                statementToBeRemove.add(stm);
                            }
                        }
                    }
                    if (this.resultingGraph != null) {
                        Iterator iterator = this.resultingGraph.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry entry = (Map.Entry) iterator.next();
                            Graph graph = (Graph) entry.getKey();
                            pathList = (List<Path>) entry.getValue();
                            if(this.resultingOntologyGraph!=null&&this.resultingOntologyGraph.get(graph)!=null){
                                continue;
                            }
                            newPathList = this.getNewPathListFromStatementToBeRemove(pathList, statement);
                            if(newPathList!=null && !newPathList.isEmpty()){
                                this.resultingGraph.replace(graph, newPathList);
                            }else{
                                iterator.remove();
                            }
                            for (Statement st : graph.getStatementList()) {
                                if (!statementToBeRemove.contains(st)) {
                                    statementToBeRemove.add(st);
                                }
                            }
                        }
                    }
                    if (this.resultingOntologyGraph != null) {
                        Iterator iterator = this.resultingOntologyGraph.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry entry = (Map.Entry) iterator.next();
                            Graph graph = (Graph) entry.getKey();
                            pathList = (List<Path>) entry.getValue();
                            newPathList = this.getNewPathListFromStatementToBeRemove(pathList, statement);
                            if(newPathList!=null && !newPathList.isEmpty()){
                                this.resultingOntologyGraph.replace(graph, newPathList);
                            }else{
                                iterator.remove();
                            }
                            for (Statement st : graph.getStatementList()) {
                                if (!statementToBeRemove.contains(st)) {
                                    statementToBeRemove.add(st);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    
    
    private List<Path> getNewPathListFromStatementToBeRemove(List<Path> pathList, Statement statement) {
        List<Path> newPathList = new ArrayList<>();
        for (Path path : pathList) {
            boolean removePath = false;
            for (EnrichedStatement enSt : path.getStatements()) {
                if (enSt.getStatement().equals(statement)) {
                    removePath = true;
                    break;
                }
            }
            if (!removePath) {
                newPathList.add(path);
            }
        }
        return newPathList;
    } 
    
    private void updateOrRemoveReasoningMap(Map<Statement, List<Path>> resultingStatement, List<Path> newPathList, Statement stm) {
        if (newPathList != null && !newPathList.isEmpty()) {
            resultingStatement.replace(stm, newPathList);
        } else {
            resultingStatement.remove(stm);
        }
    }
    
    private void updateOrRemoveReasoningGraphMap(Map<Graph,List<Path>> resultingGraph,List<Path> newPathList,Graph graph){
        if(newPathList !=null && !newPathList.isEmpty()){
            resultingGraph.replace(graph, newPathList);
        } else {
            resultingGraph.remove(graph);
        }
    }
    
//        this.currentResultingStatement = new HashMap<>(this.resultingStatement);
//        Set<Statement> keySet1 = resultingStatement.keySet();
//        for (Statement key : keySet1) {
//            System.out.println(key);
//        }
//        this.currentReverseReasoningResults = new HashMap<>(this.reverseReasoningResults);
//        this.resultingGraph = new HashMap<>(this.resultingGraph);
//        this.reverseGraphReasoning = new HashMap<>(this.reverseGraphReasoning);
//        for (Statement statement : statementToBeRemove) {
//            eliminateAllStatementOrGraphFromMaps(statement);
//        }
////        for (Statement statement : statementAgainInferred) {
////            if (currentResultingStatement.get(statement) == null) {
////                return false;
////            }
////        }
//        Set<Statement> keySet = currentResultingStatement.keySet();
//        System.out.println();
//        for (Statement key : keySet) {
//            System.out.println(key);
//            for (Path path : currentResultingStatement.get(key)) {
//                for (EnrichedStatement enSt : path.getStatements()) {
//                    System.out.println(enSt.getStatement());
//                }
//                System.out.println();
//            }
//        }
//        return true;
//    }

//    public Map<Statement, List<Path>> getOntologyPathListIfStatementListAreAgainInferable(List<Statement> statementAgainInferred, List<Graph> graphAgainInferred, List<Statement> statementToBeRemove) {
//        if (StatementAndGraphListAreAgainInferable(statementAgainInferred, graphAgainInferred, statementToBeRemove)) {
//            Map<Statement, List<Path>> resultingMapReasoningStatement = new HashMap<>();
//            for (Statement statement : statementAgainInferred) {
//                List<Path> pathList = new ArrayList<>();
//                for (Path path : this.currentResultingStatement.get(statement)) {
//                    Path filteredPath = new Path();
//                    filteredPath.setInferenceRuleId(path.getInferenceRuleId());
//                    filteredPath.setInferenceRuleName(path.getInferenceRuleName());
//                    List<EnrichedStatement> enrichedStatementList = new ArrayList<>();
//                    for (EnrichedStatement enSt : path.getStatements()) {
//                        if (!enSt.isIsInferred()) {
//                            EnrichedStatement newEnSt = new EnrichedStatement();
//                            newEnSt.setIsInferred(false);
//                            if (enSt.isIsInvented()) {
//                                newEnSt.setIsInvented(true);
//                            } else {
//                                newEnSt.setIsInvented(false);
//                            }
//                            newEnSt.setStatement(enSt.getStatement());
//                            enrichedStatementList.add(newEnSt);
//                        }
//                    }
//                    filteredPath.setStatements(enrichedStatementList);
//                    pathList.add(filteredPath);
//                }
//                resultingMapReasoningStatement.put(statement, pathList);
//            }
//            return resultingMapReasoningStatement;
//        } else {
//            return null;
//        }
//    }

//    private void eliminateAllStatementOrGraphFromMaps(Statement statement) {
//        List<Object> graphAndStatementList = new ArrayList<>();
//        if (currentReverseReasoningResults != null && currentReverseReasoningResults.get(statement) != null) {
//            graphAndStatementList.addAll(currentReverseReasoningResults.get(statement));
//            currentReverseReasoningResults.remove(statement);
//        }
//        if (currentReverseGraphReasoning != null && currentReverseGraphReasoning.get(statement) != null) {
//            graphAndStatementList.addAll(currentReverseGraphReasoning.get(statement));
//            currentReverseGraphReasoning.remove(statement);
//        }
//        if (!graphAndStatementList.isEmpty()) {
//            for (Object obj : graphAndStatementList) {
//                boolean hasBeenRemove1 = false;
//                boolean hasBeenRemove2 = false;
//                if (obj instanceof Statement) {
//                    Statement stm = (Statement) obj;
//                    updateMapsIfIsNecessary(stm, statement);
//                } else {
//                    Graph graph = (Graph) obj;
//                    for (Statement stm : graph.getStatementList()) {
//                        updateMapsIfIsNecessary(stm, statement);
//                    }
//                }
//            }
//        }
//    }

//    private boolean removePathIfStatementIsOnPathList(List<Path> pathList, Statement statement, List<Path> newPathList) {
//        boolean hasBeenRemove = false;
//        for (Path path:pathList) {
//            boolean isUsedOnPath = false;
//            for (EnrichedStatement enSt : path.getStatements()) {
//                if (enSt.getStatement().equals(statement)) {
//                    isUsedOnPath = true;
//                    break;
//                }
//            }
//            if (isUsedOnPath){
//                hasBeenRemove = true;
//            } else {
//                newPathList.add(path);
//            }
//        }
//        return hasBeenRemove;
//    }

//    private void updateMapsIfIsNecessary(Statement statement, Statement statementToBeRemove) {
//        boolean hasBeenRemove1 = false;
//        boolean hasBeenRemove2 = false;
//        boolean callRecursion=false;
//        if (currentResultingGraph != null) {
//            Set<Graph> keySet = currentResultingGraph.keySet();
//            if (keySet != null) {
//                for (Graph key : keySet) {
//                    List<Path> pathList1 = currentResultingGraph.get(key);
//                    List<Path> newPathList1 = new ArrayList<>();
//                    hasBeenRemove1 = removePathIfStatementIsOnPathList(pathList1, statementToBeRemove, newPathList1);
//                    if (hasBeenRemove1) {
//                        if (newPathList1.size() > 0) {
//                            currentResultingGraph.put(key, newPathList1);
//                        } else {
//                            currentResultingGraph.remove(key);
//                            callRecursion=true;
//                        }
//                    }
//                }
//            }
//        }
//        if (currentResultingStatement != null) {
//            List<Path> pathList2 = currentResultingStatement.get(statement);
//            if (pathList2 != null) {
//                List<Path> newPathList2 = new ArrayList<>();
//                hasBeenRemove2 = removePathIfStatementIsOnPathList(pathList2, statementToBeRemove, newPathList2);
//                if (hasBeenRemove2) {
//                    if (newPathList2.size() > 0) {
//                        currentResultingStatement.replace(statement, newPathList2);
//                    } else {
//                        currentResultingStatement.remove(statement);
//                        callRecursion=true;
//                    }
//                }
//            }
//        }
//        if (callRecursion) {
//            eliminateAllStatementOrGraphFromMaps(statement);
//        }
//    }
}