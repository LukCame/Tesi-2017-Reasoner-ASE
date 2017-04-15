package it.uniroma2.ase.model.reasoningHandler;

import it.uniroma2.ase.domain.EnrichedStatement;
import it.uniroma2.ase.domain.Graph;
import it.uniroma2.ase.domain.InferenceRule;
import it.uniroma2.ase.domain.Path;
import it.uniroma2.ase.domain.PathListAndBnodeStatementFromStatement;
import it.uniroma2.ase.domain.RuleGraphFromBnode;
import it.uniroma2.ase.domain.Triple;
import it.uniroma2.ase.domain.Type;
import it.uniroma2.ase.model.converterHandler.StatementConverter;
import it.uniroma2.ase.model.utility.OntologyUtility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Literals;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.queryrender.RenderUtils;
import org.eclipse.rdf4j.repository.RepositoryConnection;


/**
 *
 * @author L.Camerlengo
 */
public class ExecutionQuery {

    
    /**
     * Create a sparql query from InferenceRule object
     * @param inferenceRule
     * @param prefixes
     * @return String query 
     * @author L.Camerlengo
     */
    private String createSparqlQueryFromInferenceRule(InferenceRule inferenceRule, Map<String, String> prefixes) {
        //LOGGER.log(Level.INFO, "Sto nel execution query handler");
        String query = addPrefixesToSparqlQuery(prefixes);
        query = query + "select * ";
        if (!inferenceRule.HasList()) {
            query = addWhereResourcesToSparqlQuery(inferenceRule.getPremisesTriple(), query);
            if (inferenceRule.getFilterCondition() != null) {
                query = addFilterConditionToSparqlQuery(inferenceRule, query);
            } else {
                query = query + "}";
            }
            return query;
        } else {
            query = addWhereResourcesToSparqlQuery(inferenceRule.getRewritePremisesTriple(), query);
            if (inferenceRule.getFilterCondition() != null) {
                query = addFilterConditionToSparqlQuery(inferenceRule, query);
            } else {
                query = query + "}";
            }
            return query;
        }
    }
    
    /**
     * Create a sparql query from Rule Graph object
     * @param ruleGraph
     * @param prefixes
     * @return String query
     * @author L.Camerlengo
     */
    private String createSparqlQueryFromRuleGraph(RuleGraphFromBnode ruleGraph, Map<String, String> prefixes) {
        InferenceRule inferenceRuleInfo = ruleGraph.getInferenceRuleInfo();
        return createSparqlQueryFromInferenceRule(inferenceRuleInfo, prefixes);
    }
    
    /**
     * Add prefixes to sparql query
     * @param prefixes
     * @return String prefixes to sparql query
     * @author L.Camerlengo
     */
    private String addPrefixesToSparqlQuery(Map<String, String> prefixes) {
        String query = "";
        Iterator<Map.Entry<String, String>> iterator = prefixes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            query = query + "prefix " + entry.getKey() + entry.getValue() + " ";
        }
        return query;
    }
    
    /**
     * Add where condition to sparql query
     * @param tripleList
     * @param query
     * @return String where condition
     * @author L.Camerlengo
     */
    private String addWhereResourcesToSparqlQuery(List<Triple> tripleList, String query) {
        query = query + "where { ";
        for (Triple triple : tripleList) {
            query = query + triple.getSubject() + " " + triple.getPredicate() + " " + triple.getObject() + ". ";
        }
        query = query + " ";
        return query;
    }

    /**
     * Add filter condition to sparql query
     * @param inferenceRule
     * @param query
     * @return String filter condition 
     * @author L.Camerlengo
     * 
     */
    private String addFilterConditionToSparqlQuery(InferenceRule inferenceRule, String query) {
        return query + "filter (" + inferenceRule.getFilterCondition() + ")}";
    }

    /**
     *
     * @param value
     * @return String 
     * @author Artificial Intelligence Research at Tor Vergata ART
     */
    private String toSPARQL(Value value) {
        StringBuilder builder = new StringBuilder();
        if (value instanceof IRI) {
            IRI aURI = (IRI) value;
            builder.append("<").append(aURI.toString()).append(">");
        } else if (value instanceof BNode) {
            builder.append("_:").append(((BNode) value).getID());
        } else if (value instanceof Literal) {
            Literal aLit = (Literal) value;

            builder.append("\"\"\"").append(RenderUtils.escape(aLit.getLabel())).append("\"\"\"");

            if (Literals.isLanguageLiteral(aLit)) {
                builder.append("@").append(aLit.getLanguage().get());
            } else {
                builder.append("^^<").append(aLit.getDatatype().toString()).append(">");
            }
        }

        return builder.toString();

    }

    /**
     * Create ask sparql query from statement
     * @param statement
     * @param prefixes
     * @return String sparql query
     * @author L.Camerlengo
     */
    private String createASKSparqlQuery(Statement statement, Map<String, String> prefixes) {
        String query = addPrefixesToSparqlQuery(prefixes);
        String subject = toSPARQL(statement.getSubject());
        String predicate = toSPARQL(statement.getPredicate());
        String object = toSPARQL(statement.getObject());
        return query + "ASK { " + subject + " " + predicate + " " + object + " }";
    }

    /**
     * Create Bnode sparql query from statement
     * @param statement
     * @param prefixes
     * @return String sparql query
     * @author L.Camerlengo
     */
    private String createBNodeSparqlQuery(Statement statement, Map<String, String> prefixes) {
        String query = addPrefixesToSparqlQuery(prefixes);
        query = query + " SELECT ";
        String subject = statement.getSubject().stringValue();
        String predicate = statement.getPredicate().stringValue();
        String object = statement.getObject().stringValue();
        String bnode = null;
        if (statement.getSubject() instanceof BNode) {
            subject = "?b1";
            bnode = statement.getSubject().stringValue();
        } else {
            subject = toSPARQL(statement.getSubject());
        }
        predicate = toSPARQL(statement.getPredicate());
        if (statement.getObject() instanceof BNode) {
            if (bnode != null && bnode.equals(statement.getObject().stringValue())) {
                object = "?b1";
            } else {
                object = "?b2";
            }
        } else {
            object = toSPARQL(statement.getObject());
        }
        if (subject.equals("?b1")) {
            query = query + subject + " ";
        }
        if (object.equals("?b2")) {
            query = query + object + " ";
        }
        return query + "WHERE { " + subject + " " + predicate + " " + object + "}";
    }
    
    /**
     * Create a graph query from statement. The questioned graph is the support ontology graph
     * @param statement
     * @param prefixes
     * @return String sparql graph query
     * @author L.Camerlengo
     */
    private String createGraphSparqlQuery(Statement statement, Map<String, String> prefixes) {
        String query = addPrefixesToSparqlQuery(prefixes);
        query = query + "select * ";
        String subject = toSPARQL(statement.getSubject());
        String predicate = toSPARQL(statement.getPredicate());
        String object = toSPARQL(statement.getObject());
        return query + "{ Graph <" + OntologyUtility.SUPPORT_ONTOLOGY_GRAPH + "> { " + subject + " " + predicate + " " + object + " }}";
    }

    /**
     * Create bnode graph sparql query. The questioned graph is the support ontology graph.
     * @param statement
     * @param prefixes
     * @return String sparql graph query 
     * @author L.Camerlengo
     */
    private String createGraphBnodeSparqlQuery(Statement statement, Map<String, String> prefixes) {
        String query = addPrefixesToSparqlQuery(prefixes);
        query = query + "select * ";
        String subject = statement.getSubject().stringValue();
        String predicate = statement.getPredicate().stringValue();
        String object = statement.getObject().stringValue();
        String bnode = null;
        if (statement.getSubject() instanceof BNode) {
            bnode = subject;
            subject = "?b1";
        } else {
            subject = toSPARQL(statement.getSubject());
        }
        predicate = toSPARQL(statement.getPredicate());
        if (statement.getObject() instanceof BNode) {
            if (bnode == null) {
                object = "?b1";
            } else if (bnode.equals(object)) {
                object = "?b1";
            } else {
                object = "?b2";
            }
        } else {
            object = toSPARQL(statement.getObject());
        }
        return query + "{ Graph <" + OntologyUtility.SUPPORT_ONTOLOGY_GRAPH + "> { " + subject + " " + predicate + " " + object + " }}";
    }
    
    /**
     * create sparql query from RuleGraph object
     * @param statementOfGraph
     * @param prefixes
     * @return return sparql query
     * @author L.Camerlengo
     */
    public String createRuleGraphSparqlQuery(List<Statement> statementOfGraph, Map<String, String> prefixes) {
        String query = addPrefixesToSparqlQuery(prefixes);
        query = query + " Select ";
        String whereCondition = "where { ";
        Map<String, String> correspondingBnode = new HashMap<>();
        whereCondition = whereCondition + this.createRuleGraphWhereConditonForSparqlQuery(statementOfGraph, correspondingBnode);
        Set<String> keySet = correspondingBnode.keySet();
        for (String key : keySet) {
            query = query + "?" + correspondingBnode.get(key) + " ";
        }
        return query + whereCondition + "}";
    }

    /**
     * Create sparql graph query from RuleGraph object. The questioned graph is support ontology graph.
     * @param statementOfGraph
     * @param prefixes
     * @return String sparql graph query
     * @author L.Camerlengo
     */
    private String createRuleGraphSparqlGraphQuery(List<Statement> statementOfGraph, Map<String, String> prefixes) {
        String query = addPrefixesToSparqlQuery(prefixes) + " select ";
        Map<String, String> correspondingBnode = new HashMap<>();
        String whereCondition = this.createRuleGraphWhereConditonForSparqlQuery(statementOfGraph, correspondingBnode);
        Set<String> keySet = correspondingBnode.keySet();
        for (String key : keySet) {
            query = query + "?" + correspondingBnode.get(key) + " ";
        }
        String graphCondition = "Where { GRAPH <" + OntologyUtility.SUPPORT_ONTOLOGY_GRAPH + "> {";
        query = query + graphCondition + whereCondition + "}}";
        return query;
    }
    
    /**
     * Create where condition for RuleGraph sparql query
     * @param statementOfGraph
     * @param correspondingBnode
     * @return String where condition for ruleGraph sparql query
     * @author L.Camerlengo
     */
    private String createRuleGraphWhereConditonForSparqlQuery(List<Statement> statementOfGraph, Map<String, String> correspondingBnode) {
        String whereCondition = "";
        for (Statement statement : statementOfGraph) {
            if (statement.getSubject() instanceof BNode) {
                if (correspondingBnode.get(statement.getSubject().stringValue()) == null) {
                    String value = OntologyUtility.createValueRandomStringForMap(correspondingBnode);
                    correspondingBnode.put(statement.getSubject().stringValue(), value);
                    whereCondition = whereCondition + "?" + value + " ";
                } else {
                    whereCondition = whereCondition + "?" + correspondingBnode.get(statement.getSubject().stringValue()) + " ";
                }
            } else {
                String subject = toSPARQL(statement.getSubject());
                whereCondition = whereCondition + subject + " ";
            }
            String predicate = toSPARQL(statement.getPredicate());
            whereCondition = whereCondition + predicate + " ";
            if (statement.getObject() instanceof BNode) {
                if (correspondingBnode.get(statement.getObject().stringValue()) == null) {
                    String value = OntologyUtility.createValueRandomStringForMap(correspondingBnode);
                    correspondingBnode.put(statement.getObject().stringValue(), value);
                    whereCondition = whereCondition + "?" + value + ". ";
                } else {
                    whereCondition = whereCondition + "?" + correspondingBnode.get(statement.getObject().stringValue()) + ". ";
                }
            } else {
                String object = toSPARQL(statement.getObject());
                whereCondition = whereCondition + object + ". ";
            }
        }
        return whereCondition;
    }

    /**
     * Get all ontology triples from the repository
     * @param repository
     * @return List<Statement> all ontology triples of the repository 
     * @author L.Camerlengo
     */
    public List<Statement> getOntologyTriples(RepositoryConnection repository){
        String query="select ?sub ?pred ?obj where{ ?sub ?pred ?obj. }";
        TupleQuery prepareTupleQuery = repository.prepareTupleQuery(QueryLanguage.SPARQL,query);
        TupleQueryResult results = prepareTupleQuery.evaluate();
        Triple triple=new Triple("?sub", "?pred", "?obj");
        List<Triple> tripleList=new ArrayList<>();
        tripleList.add(triple);
        List<Statement> statementList=new ArrayList<>();
        while(results.hasNext()){
            StatementConverter st=new StatementConverter();
            List<Statement> currentStatementList = st.getStatementsFromConclusionsTriple(tripleList, results.next(), repository);
            statementList.addAll(currentStatementList);
        }
        return statementList;
    }
    
    /**
     * Execute an inference rule and return the result of her application  
     * @param inferenceRule
     * @param prefixes
     * @param repository
     * @param resultingStatement
     * @param resultingOntologyStatement
     * @param reverseReasoning
     * @param inconsistenciesList
     * @return true if there is at least one statement that is generated by path not already found, false otherwhise
     * @author L.Camerlengo
     */
    public boolean executeInferenceRule(InferenceRule inferenceRule, Map<String, String> prefixes, RepositoryConnection repository, Map<Statement, List<Path>> resultingStatement, Map<Statement, List<Path>> resultingOntologyStatement, Map<Statement, List<Statement>> reverseReasoning, List<Path> inconsistenciesList)  {
        boolean thereIsAtLeastOneInference = false;
        // create a sparql query
        String query = createSparqlQueryFromInferenceRule(inferenceRule, prefixes);
        // create a tuple query
        TupleQuery prepareTupleQuery = repository.prepareTupleQuery(QueryLanguage.SPARQL, query);
        TupleQueryResult results = prepareTupleQuery.evaluate();
        List<Triple> premisesTriple;
        List<Triple> conclusionsTriple;
        //get rewrite premises and conclusions triples if necessary
        if (inferenceRule.HasList()) {
            premisesTriple = inferenceRule.getRewritePremisesTriple();
            conclusionsTriple = inferenceRule.getRewriteConclusionsTriple();
        } else {
            premisesTriple = inferenceRule.getPremisesTriple();
            conclusionsTriple = inferenceRule.getConclusionsTriple();
        }
        while (results.hasNext()) {
            StatementConverter statementConverter = new StatementConverter();
            BindingSet next = results.next();
            //get Enriched statement from premises triples template
            List<EnrichedStatement> statementsOfPremisesTriple = statementConverter.getStatementsFromPremisesTriple(premisesTriple, next, repository, prefixes);
            List<Statement> statementsOfConclusionsTriple = null;
            // if the rule type is not an inconsistency rule get statement from conclusions triple template
            if (inferenceRule.getType().equals(Type.rule)) {
                statementsOfConclusionsTriple = statementConverter.getStatementsFromConclusionsTriple(conclusionsTriple, next, repository);
            }
            Path newPath = new Path();
            newPath.setInferenceRuleId(inferenceRule.getInferenceRuleID());
            newPath.setInferenceRuleName(inferenceRule.getInferenceRuleName());
            newPath.setStatements(statementsOfPremisesTriple);
            //add the newPath if the inconsistency is not already inferred
            if (inferenceRule.getType().equals(Type.inconsistency)) {
                boolean isNewPath = true;
                for (Path path : inconsistenciesList) {
                    if (OntologyUtility.isSamePath(newPath, path)) {
                        isNewPath = false;
                        break;
                    }
                }
                if (isNewPath) {
                    inconsistenciesList.add(newPath);
                }
            } else {
                for (Statement stmt : statementsOfConclusionsTriple) {
                    // get statement without prefixes
                    Statement rewStm = statementConverter.getStatementWithoutPrefixes(stmt, repository, prefixes);
                    // check if the statement is a self generated statement
                    if (verifyIfTheStatementIsASelfGeneratedTripleOnOntology(rewStm, newPath, repository, prefixes)) {
                        continue;
                    }
                    if (!newPath.isGeneratesAnOntologyTriple()) {
                        // get Path and isomorphic bnode statement if the statement is already inferred ontology statement otherwise the value is null
                        PathListAndBnodeStatementFromStatement result = OntologyUtility.getPathListAndBnodeStatementFromStatement(rewStm, resultingStatement);
                        if (result.getBnodeStatement() != null) {
                            rewStm = result.getBnodeStatement();
                        }
                        List<Path> pathList = result.getPathList();
                        //add the statement to inferred ontology statements if necessary
                        if (OntologyUtility.addStatementToReasoningResults(repository, rewStm, newPath, pathList, resultingStatement, reverseReasoning)) {
                            thereIsAtLeastOneInference = true;
                        }
                    } else {
                         // get Path and isomorphic bnode statement if the statement is already inferred statement otherwise the value is null
                        PathListAndBnodeStatementFromStatement result = OntologyUtility.getPathListAndBnodeStatementFromStatement(rewStm, resultingOntologyStatement);
                        if (result.getBnodeStatement() != null) {
                            rewStm = result.getBnodeStatement();
                        }
                        //add the statement to inferred statements if necessary
                        List<Path> pathList = result.getPathList();
                        if (OntologyUtility.addStatementToReasoningResults(repository, rewStm, newPath, pathList, resultingOntologyStatement, reverseReasoning)) {
                            thereIsAtLeastOneInference = true;
                        }
                    }
                }
            }
        }
        results.close();
        return thereIsAtLeastOneInference;
    }

    /**
     * Check if the statement is a self generated statement on ontology
     * @param statement
     * @param path
     * @param repository
     * @param prefixes
     * @return true if the statement is a self generated, false otherwhise 
     * @author L.Camerlengo
     */
    private boolean verifyIfTheStatementIsASelfGeneratedTripleOnOntology(Statement statement, Path path, RepositoryConnection repository, Map<String, String> prefixes) {
        if (!OntologyUtility.hasABnode(statement)) {
            String query = createASKSparqlQuery(statement, prefixes);
            BooleanQuery booleanQuery = repository.prepareBooleanQuery(QueryLanguage.SPARQL, query);
             // check if the statement is already on ontology
            if (booleanQuery.evaluate()) {
                //check if the statement is on support ontology graph
                String queryGraph = createGraphSparqlQuery(statement, prefixes);
                TupleQuery tupleQuery = repository.prepareTupleQuery(QueryLanguage.SPARQL, queryGraph);
                TupleQueryResult result = tupleQuery.evaluate();
                // the statement is already on support ontology graph
                if (result.hasNext()) {
                    return false;
                }
                // if premises statements contains this statement then statement is a self generated ontology statement
                for (EnrichedStatement enSt : path.getStatements()) {
                    if (enSt.getStatement().equals(statement)) {
                        return true;
                    }
                }
                path.setGeneratesAnOntologyTriple(true);
                return false;
            } else {
                return false;
            }
        } else {
            //check if there is an isomorphic statement in the ontology
            String query = createBNodeSparqlQuery(statement, prefixes);
            TupleQuery tupleQuery = repository.prepareTupleQuery(QueryLanguage.SPARQL, query);
            TupleQueryResult results = tupleQuery.evaluate();
            boolean isOnOntology = false;
            while (results.hasNext()) {
                BindingSet next = results.next();
                // there is an isomorphic statement on ontology
                if (OntologyUtility.areAllBnodeOnBindingSet(next)) {
                    isOnOntology = true;
                    break;
                }
            }
            if (isOnOntology) {
                //check if there is an isomorphic statement on the support ontology graph
                String queryGraph = createGraphBnodeSparqlQuery(statement, prefixes);
                TupleQuery tupleQueryGraph = repository.prepareTupleQuery(QueryLanguage.SPARQL, queryGraph);
                TupleQueryResult resultsGraph = tupleQueryGraph.evaluate();
                while (resultsGraph.hasNext()) {
                    BindingSet next = resultsGraph.next();
                    //there is an isomorphic statement in the support ontology graph
                    if (OntologyUtility.areAllBnodeOnBindingSet(next)) {
                        return false;
                    }
                }
                // if premises statement cointains an isomorphic statement of this statement then statement is a self generated statement
                for (EnrichedStatement enSt : path.getStatements()) {
                    if (OntologyUtility.isSameBnodeStatement(enSt.getStatement(), statement)) {
                        return true;
                    }
                }
                path.setGeneratesAnOntologyTriple(true);
                return false;
            } else {
                return false;
            }
        }
    }
    
    /**
     * Check if the statement is already in the support ontology graph
     * @param statement
     * @param prefixes
     * @param repository
     * @return true if the statement is conteined on support ontology graph, false otherwhise
     * @author L.Camerlengo
     */
    public boolean verifyIfStatementIsOnSupportGraph(Statement statement, Map<String, String> prefixes, RepositoryConnection repository) {
        if (OntologyUtility.hasABnode(statement)) {
            // verify if bnode statement is already on support ontology graph 
            String query = createGraphBnodeSparqlQuery(statement, prefixes);
            TupleQuery tupleQuery = repository.prepareTupleQuery(QueryLanguage.SPARQL, query);
            TupleQueryResult results = tupleQuery.evaluate();
            // the bindingSet is an isomorphic statement
            if (results.hasNext()) {
                return true;
            } else {
                return false;
            }
        } else {
            //verify if statement is already on support ontology graph
            String query = createGraphSparqlQuery(statement, prefixes);
            TupleQuery tupleQuery = repository.prepareTupleQuery(QueryLanguage.SPARQL, query);
            TupleQueryResult results = tupleQuery.evaluate();
            // the statement is already on support ontology graph
            if (results.hasNext()) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    /**
     * Check if the graph is a self generated graph on ontology
     * @param statementOfGraph
     * @param path
     * @param prefixes
     * @param repository
     * @return true if the graph is self generated, false otherwhise
     * @author L.Camerlengo
     */
    private boolean verifyIfThereIsASelfGeneratedGraphOnOntology(List<Statement> statementOfGraph, Path path, Map<String, String> prefixes, RepositoryConnection repository) {
        if (statementOfGraph.size() == 1) {
            // the graph contains only one statement and verify if is an ontology self generated statement
            return verifyIfTheStatementIsASelfGeneratedTripleOnOntology(statementOfGraph.get(0), path, repository, prefixes);
        } else {
            //verify if the graph is already content on ontology
            String query = createRuleGraphSparqlQuery(statementOfGraph, prefixes);
            TupleQuery tupleQuery = repository.prepareTupleQuery(query);
            TupleQueryResult results = tupleQuery.evaluate();
            boolean isOnOntology = false;
            while (results.hasNext()) {
                BindingSet next = results.next();
                //the BindingSet is an isomorphic graph 
                if (OntologyUtility.areAllBnodeOnBindingSet(next)) {
                    isOnOntology = true;
                    break;
                }
            }
            if (isOnOntology) {
                // verify if the graph is already on the support ontology graph
                String queryGraph = createRuleGraphSparqlGraphQuery(statementOfGraph, prefixes);
                TupleQuery tupleGraphQuery = repository.prepareTupleQuery(QueryLanguage.SPARQL, queryGraph);
                TupleQueryResult resultsGraphQuery = tupleGraphQuery.evaluate();
                while (resultsGraphQuery.hasNext()) {
                    BindingSet next = resultsGraphQuery.next();
                    // the bindingSet is an isomorphic graph
                    if (OntologyUtility.areAllBnodeOnBindingSet(next)) {
                        return false;
                    }
                }
                path.setGeneratesAnOntologyGraph(true);
                return false;
            } else {
                return false;
            }
        }
    }

    /**
     * Execute an ruleGraph and return the result of her application.
     * @param ruleGraph
     * @param prefixes
     * @param repository
     * @param resultingStatement
     * @param resultingOntologyStatement
     * @param reverseReasoningResults
     * @param resultingGraph
     * @param resultingOntologyGraph
     * @param reverseGraphReasoning
     * @return true if there is at least one graph that is generated by path not already found, false otherwhise 
     * @author L.Camerlengo
     */
    public boolean executeRuleGraph(RuleGraphFromBnode ruleGraph, Map<String, String> prefixes, RepositoryConnection repository, Map<Statement, List<Path>> resultingStatement, Map<Statement, List<Path>> resultingOntologyStatement, Map<Statement, List<Statement>> reverseReasoningResults, Map<Graph, List<Path>> resultingGraph, Map<Graph, List<Path>> resultingOntologyGraph, Map<Statement, List<Graph>> reverseGraphReasoning)  {
        boolean thereIsAtLeastOneInference = false;
        //create ruleGraph query
        String query = this.createSparqlQueryFromRuleGraph(ruleGraph, prefixes);
        List<Triple> premisesTriple;
        //get ruleGraph information
        InferenceRule inferenceRuleInfo = ruleGraph.getInferenceRuleInfo();
        StatementConverter statementConverter = new StatementConverter();
        //get rewrite premises triples if necessary
        if (inferenceRuleInfo.HasList()) {
            premisesTriple = inferenceRuleInfo.getRewritePremisesTriple();
        } else {
            premisesTriple = inferenceRuleInfo.getPremisesTriple();
        }
        //create tupleQuery
        TupleQuery prepareTupleQuery = repository.prepareTupleQuery(QueryLanguage.SPARQL, query);
        //evaluate tupleQuery
        TupleQueryResult results = prepareTupleQuery.evaluate();
        //for each BindingSet
        while (results.hasNext()) {
            BindingSet next = results.next();
            int i = 0;
            int j = 0;
            int n = ruleGraph.getGraphList().size();
            //get size of graphs list
            List<Graph> graphList = ruleGraph.getGraphList();
            List<Graph> rewrittenGraphs = new ArrayList<>();
            //for each graphs of RuleGraph
            for (j = 0; j < n; j++) { 
                List<Triple> conclusionsTriple = graphList.get(j).getTripleList();
                // get the correct conclusions triples list for this graph
                if (inferenceRuleInfo.HasList()) {
                    List<Triple> rewriteGraph = inferenceRuleInfo.getRewriteConclusionsTriple().subList(i, i + conclusionsTriple.size());
                    i = i + conclusionsTriple.size();
                    conclusionsTriple = rewriteGraph;
                }
                //get EnrichedStatement from premises triples template
                List<EnrichedStatement> statementOfPremisesTriple = statementConverter.getStatementsFromPremisesTriple(premisesTriple, next, repository, prefixes);
                //get Statement from conclusions triples template
                List<Statement> statementOfConclusionsTriple = statementConverter.getStatementsFromConclusionsTriple(conclusionsTriple, next, repository);
                List<Statement> rewStmList = new ArrayList<>();
                //get statement of conclusions triples without prefixes
                for (Statement stmt : statementOfConclusionsTriple) {
                    rewStmList.add(statementConverter.getStatementWithoutPrefixes(stmt, repository, prefixes));
                }
                //create new Graph object
                Graph graph = new Graph();
                graph.setTripleList(conclusionsTriple);
                graph.setStatementList(rewStmList);
                rewrittenGraphs.add(graph);
            }
            List<Graph> filteredRewrittenGraphs = new ArrayList<>();
            Path newPath = new Path();
            //set the premises triple that is generated thisis graphs
            newPath.setStatements(statementConverter.getStatementsFromPremisesTriple(premisesTriple, next, repository, prefixes));
            //for each graph
            for (Graph graph : rewrittenGraphs) {
                List<Statement> statementsFromConclusionsTriple = graph.getStatementList();
                //verify this graph is a self generated Graph
                if (this.verifyIfThereIsASelfGeneratedGraphOnOntology(statementsFromConclusionsTriple, newPath, prefixes, repository)) {
                    continue;
                } else {
                    //add this graph to filteredGraphs list
                    Graph filteredGraph = new Graph();
                    filteredGraph.setTripleList(graph.getTripleList());
                    filteredGraph.setStatementList(graph.getStatementList());
                    if (newPath.isGeneratesAnOntologyGraph() || newPath.isGeneratesAnOntologyTriple()) {
                        filteredGraph.setIsAnOntologyGraph(true);
                    }
                    filteredRewrittenGraphs.add(filteredGraph);
                }
                newPath.setGeneratesAnOntologyGraph(false);
                newPath.setGeneratesAnOntologyTriple(false);
            }
            newPath.setInferenceRuleId(ruleGraph.getInferenceRuleInfo().getInferenceRuleID());
            newPath.setInferenceRuleName(ruleGraph.getInferenceRuleInfo().getInferenceRuleName());
            //for each graph
            for (Graph graph : filteredRewrittenGraphs) {
                newPath.setGeneratesAnOntologyGraph(graph.isIsAnOntologyGraph());
                newPath.setGeneratesAnOntologyTriple(graph.isIsAnOntologyGraph());
                //graph contains only one triple
                if (graph.getStatementList().size() == 1) {
                    //get Path and isomorphic bnode statement if the statement is already inferred otherwise the value is null
                    PathListAndBnodeStatementFromStatement result;
                    if (graph.isIsAnOntologyGraph()) {
                        result = OntologyUtility.getPathListAndBnodeStatementFromStatement(graph.getStatementList().get(0), resultingOntologyStatement);
                    } else {
                        result = OntologyUtility.getPathListAndBnodeStatementFromStatement(graph.getStatementList().get(0), resultingStatement);
                    }
                    Statement rewStm;
                    if (result.getBnodeStatement() != null) {
                        rewStm = result.getBnodeStatement();
                    } else {
                        rewStm = graph.getStatementList().get(0);
                    }
                    List<Path> pathList = result.getPathList();
                    //add the statement in the ontology inferred statements if neccessary
                    if (graph.isIsAnOntologyGraph()) {
                        if (OntologyUtility.addStatementToReasoningResults(repository, rewStm, newPath, pathList, resultingOntologyStatement, reverseReasoningResults)) {
                            thereIsAtLeastOneInference = true;
                        }
                    // add the statement in the inferred statements if necessary
                    } else if (OntologyUtility.addStatementToReasoningResults(repository, rewStm, newPath, pathList, resultingStatement, reverseReasoningResults)) {
                        thereIsAtLeastOneInference = true;
                    }
                } else if (graph.isIsAnOntologyGraph()) {
                    List<Path> pathList = resultingOntologyGraph.get(graph);
                    //add the graph in the ontology inferred graphs if necessary
                    if (OntologyUtility.addGraphToReasoningResults(repository, graph, newPath, pathList, resultingOntologyGraph, reverseGraphReasoning)) {
                        thereIsAtLeastOneInference = true;
                    }
                } else {
                    List<Path> pathList = resultingGraph.get(graph);
                    //add the graph in the inferred graphs if necessary
                    if (OntologyUtility.addGraphToReasoningResults(repository, graph, newPath, pathList, resultingGraph, reverseGraphReasoning)) {
                        thereIsAtLeastOneInference = true;
                    }
                }
            }
        }
        results.close();
        return thereIsAtLeastOneInference;
    }

}
