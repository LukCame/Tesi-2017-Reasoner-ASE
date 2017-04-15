/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.Graph;
import it.uniroma2.ase.domain.Path;
import it.uniroma2.ase.domain.QueriesResults;
import it.uniroma2.ase.model.reasoningHandler.ReasoningWithMaps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.rdf4j.model.Statement;
import org.springframework.stereotype.Service;

/**
 *
 * @author F.Camerlengo
 */
@Service
public class InverseReasoningServiceImpl implements IInverseReasoningService {

    public QueriesResults executeInverseReasoningProcess(List<Integer> statementId, List<Integer> graphsId, List<Integer> ontologyId, QueriesResults results, boolean kind) {
        int i;
        Map<Statement, List<Path>> selectedResultingStatement = new HashMap<>();
        if (statementId != null && !statementId.isEmpty()) {
            i = 0;
            Iterator iterator = results.getResultingStatement().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (statementId.contains(i)) {
                    Statement key = (Statement) entry.getKey();
                    List<Path> value = (List<Path>) entry.getValue();
                    selectedResultingStatement.put(key, value);
                }
                i++;
            }
        }
        Map<Graph, List<Path>> selectedResultingGraph = new HashMap<>();
        if (graphsId != null && !graphsId.isEmpty()) {
            i = 0;
            Iterator iterator = results.getResultingGraph().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (graphsId.contains(i)) {
                    Graph key = (Graph) entry.getKey();
                    List<Path> value = (List<Path>) entry.getValue();
                    selectedResultingGraph.put(key, value);
                }
                i++;
            }
        }
        i = 0;
        QueriesResults copyOfQueriesResults = new QueriesResults(results);
        List<Statement> ontologyTriple = copyOfQueriesResults.getOntologyTriple();
        List<Statement> ontologyStatementToBeRemove = new ArrayList<>();
        for (Statement statement : ontologyTriple) {
            if (ontologyId.contains(i)) {
                ontologyStatementToBeRemove.add(statement);
            }
            i++;
        }
        ReasoningWithMaps reasoningWithMaps = new ReasoningWithMaps();
        reasoningWithMaps.setResultingStatement(copyOfQueriesResults.getResultingStatement());
        reasoningWithMaps.setResultingOntologyStatement(copyOfQueriesResults.getResultingOntologyStatement());
        reasoningWithMaps.setReverseReasoningResults(copyOfQueriesResults.getReverseReasoningResult());
        reasoningWithMaps.setResultingGraph(copyOfQueriesResults.getResultingGraph());
        reasoningWithMaps.setResultingOntologyGraph(copyOfQueriesResults.getResultingOntologyGraph());
        reasoningWithMaps.setReverseGraphReasoning(copyOfQueriesResults.getReverseReasoningGraphResult());
        reasoningWithMaps.StatementAndGraphListAgainInferable(ontologyStatementToBeRemove);
        QueriesResults newQueriesResults = new QueriesResults();
        Map<Statement, List<Path>> newResultingStatement = new HashMap<>();
        if (statementId != null && !statementId.isEmpty()) {
            Set<Statement> keySet = selectedResultingStatement.keySet();
            for (Statement st : keySet) {
                if (kind) {
                    if (reasoningWithMaps.getResultingStatement().get(st) != null) {
                        newResultingStatement.put(st, reasoningWithMaps.getResultingStatement().get(st));
                    }
                } else if (reasoningWithMaps.getResultingStatement().get(st) == null) {
                    newResultingStatement.put(st, null);
                }
            }
            newQueriesResults.setResultingStatement(newResultingStatement);
            newQueriesResults.setNumberOfInferredTriples(newResultingStatement.size());
        } else if (kind) {
            newQueriesResults.setResultingStatement(reasoningWithMaps.getResultingStatement());
            newQueriesResults.setNumberOfInferredTriples(reasoningWithMaps.getResultingStatement().size());
        } else {
            Set<Statement> keySet = results.getResultingStatement().keySet();
            for (Statement statement : keySet) {
                if (reasoningWithMaps.getResultingStatement().get(statement) == null) {
                    newResultingStatement.put(statement, null);
                }
            }
            newQueriesResults.setResultingStatement(newResultingStatement);
            newQueriesResults.setNumberOfInferredTriples(newResultingStatement.size());
        }
        Map<Graph, List<Path>> newResultingGraph = new HashMap<>();
        if (graphsId != null && !graphsId.isEmpty()) {
            Set<Graph> keySet = selectedResultingGraph.keySet();
            for (Graph graph : keySet) {
                if (kind) {
                    if (reasoningWithMaps.getResultingGraph().get(graph) != null) {
                        newResultingGraph.put(graph, reasoningWithMaps.getResultingGraph().get(graph));
                    }
                } else if (reasoningWithMaps.getResultingGraph().get(graph) == null) {
                    newResultingGraph.put(graph, null);
                }
            }
            newQueriesResults.setResultingGraph(newResultingGraph);
            newQueriesResults.setNumberOfInferredGraphs(newResultingGraph.size());
        } else if (kind) {
            newQueriesResults.setResultingGraph(reasoningWithMaps.getResultingGraph());
            newQueriesResults.setNumberOfInferredGraphs(reasoningWithMaps.getResultingGraph().size());
        } else {
            Set<Graph> keySet = results.getResultingGraph().keySet();
            for (Graph graph : keySet) {
                if (reasoningWithMaps.getResultingGraph().get(graph) == null) {
                    newResultingGraph.put(graph, null);
                }
            }
            newQueriesResults.setResultingGraph(newResultingGraph);
            newQueriesResults.setNumberOfInferredGraphs(newResultingGraph.size());
        }
        return newQueriesResults;
    }

}
