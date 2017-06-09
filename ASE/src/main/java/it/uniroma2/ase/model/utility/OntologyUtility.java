package it.uniroma2.ase.model.utility;


import it.uniroma2.ase.domain.EnrichedStatement;
import it.uniroma2.ase.domain.Graph;
import it.uniroma2.ase.domain.InferenceRule;
import it.uniroma2.ase.domain.Path;
import it.uniroma2.ase.domain.PathListAndBnodeStatementFromStatement;
import it.uniroma2.ase.domain.Triple;
import it.uniroma2.ase.model.exception.LoadOntologyException;
import it.uniroma2.ase.model.exception.LoadOntologyFileIoException;
import it.uniroma2.ase.model.exception.LoadOntologyRDFParseException;
import it.uniroma2.ase.model.exception.LoadOntologyRepositoryException;
import it.uniroma2.ase.model.exception.LoadOntologyUnsupportedRDFFormatException;
import it.uniroma2.ase.model.reasoningHandler.ExecutionQuery;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

/**
 *
 * @author L.Camerlengo
 */
public class OntologyUtility {

    public static final String SUPPORT_ONTOLOGY_GRAPH = "http://supportOntology";

    public static List<String> getAllVariable(InferenceRule inferenceRule) {
        List<String> variables = new ArrayList<>();
        List<Triple> premisesTriple = inferenceRule.getPremisesTriple();
        for (Triple triple : premisesTriple) {
            String subject = triple.getSubject();
            String predicate = triple.getPredicate();
            String object = triple.getObject();
            if (subject.charAt(0) == '?') {
                variables.add(subject.replace("?", ""));
            } else if (subject.charAt(0) == '$' && subject.charAt(1) != '$') {
                variables.add(subject.replace("$", ""));
            }
            if (predicate.charAt(0) == '?') {
                variables.add(predicate.replace("?", ""));
            }
            if (object.charAt(0) == '?') {
                variables.add(object.replace("?", ""));
            } else if (object.charAt(0) == '$' && object.charAt(1) != '$') {
                variables.add(object.replace("$", ""));
            }
        }
        return variables;
    }

    public static RepositoryConnection loadModel(String ontology) throws LoadOntologyException{
        Repository repository = new SailRepository(new MemoryStore());
        repository.initialize();
        RepositoryConnection repconn = repository.getConnection();
        try{
        repconn.add(new File(ontology), "http://art.uniroma2.it", OntologyUtility.guessRDFFormatFromExtension(ontology));
        }catch(UnsupportedRDFormatException ex){
            throw new LoadOntologyUnsupportedRDFFormatException(ex);
        }catch(RDFParseException ex){
            throw new LoadOntologyRDFParseException(ex.getMessage());
        }catch(RepositoryException ex){
            throw new LoadOntologyRepositoryException(ex.getMessage());
        }catch(IOException ex){
            throw new LoadOntologyFileIoException(ex.getMessage());
        }
        return repconn;
    }
    
    private static RDFFormat guessRDFFormatFromExtension(String ontologyPath) throws UnsupportedRDFormatException {
        int index = ontologyPath.indexOf(".");
        String exstension = ontologyPath.substring(index + 1);
        List<String> exstensionList = RDFFormat.BINARY.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.BINARY;
        }
        exstensionList = RDFFormat.TURTLE.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.TURTLE;
        }
        exstensionList = RDFFormat.RDFXML.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.RDFXML;
        }
        exstensionList = RDFFormat.JSONLD.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.JSONLD;
        }
        exstensionList = RDFFormat.N3.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.N3;
        }
        exstensionList = RDFFormat.NQUADS.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.NQUADS;
        }
        exstensionList = RDFFormat.NTRIPLES.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.NTRIPLES;
        }
        exstensionList = RDFFormat.RDFA.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.RDFA;
        }
        exstensionList = RDFFormat.RDFJSON.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.RDFJSON;
        }
        exstensionList = RDFFormat.TRIG.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.TRIG;
        }
        exstensionList = RDFFormat.TRIX.getFileExtensions();
        if (exstensionList.contains(exstension)) {
            return RDFFormat.TRIX;
        }
        return null;
    }

    public static List<List<Statement>> getTwoTypeOfStatementSet(Path path) {
        List<List<Statement>> listOfStatementList = new ArrayList<>();
        List<EnrichedStatement> enrichedStatementList = path.getStatements();
        List<Statement> listWithoutBnode=new ArrayList<>();
        List<Statement> listWithBnode=new ArrayList<>();
        for (EnrichedStatement enStat : enrichedStatementList) {
            Statement statement = enStat.getStatement();
            if (OntologyUtility.hasABnode(statement)) {
                listWithBnode.add(statement);
            } else {
                listWithoutBnode.add(statement);
            }
        }
        listOfStatementList.add(listWithBnode);
        listOfStatementList.add(listWithoutBnode);
        return listOfStatementList;
    }

    public static boolean isSamePath(Path path1, Path path2) {
        boolean path1HasBnode = path1.pathHasBnode();
        boolean path2HasBnode = path2.pathHasBnode();
        if (path1HasBnode && path2HasBnode) {
            Graph graph1=new Graph();
            Graph graph2=new Graph();
            List<Statement> statementList1=new ArrayList<>();
            for(EnrichedStatement enSt:path1.getStatements()){
                statementList1.add(enSt.getStatement());
            }
            List<Statement> statementList2=new ArrayList<>();
            for(EnrichedStatement enSt:path2.getStatements()){
                statementList2.add(enSt.getStatement());
            }
            graph1.setStatementList(statementList1);
            graph2.setStatementList(statementList2);
            if(graph1.hashCode()!=graph2.hashCode()){
                return false;
            }
            if (path1.containsAllBnodeStatement(path2) && path2.containsAllBnodeStatement(path1)) {
                return true;
            } else {
                return false;
            }
        } else if (!path1HasBnode && !path2HasBnode) {
            Set<Statement> p1 = new HashSet<>();
            for (EnrichedStatement stm : path1.getStatements()) {
                p1.add(stm.getStatement());
            }
            Set<Statement> p2 = new HashSet<>();
            for (EnrichedStatement stm : path2.getStatements()) {
                p2.add(stm.getStatement());
            }
            if (p1.containsAll(p2) && p2.containsAll(p1)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public static List<String> getAllBnodeFromStatementList(List<Statement> statementList){
        List<String> bnodeList=new ArrayList();
        for(Statement statement:statementList){
            if(statement.getSubject() instanceof BNode){
                if(!(bnodeList.contains(statement.getSubject().stringValue()))){
                    bnodeList.add(statement.getSubject().stringValue());
                }
            }
            if(statement.getObject() instanceof BNode){
                if(!(bnodeList.contains(statement.getObject().stringValue()))){
                    bnodeList.add(statement.getObject().stringValue());
                }
            }
        }
        return bnodeList;
    }
    
    public static boolean hasABnode(Statement statement) {
        Value subject = statement.getSubject();
        Value predicate = statement.getPredicate();
        Value object = statement.getObject();
        return subject instanceof BNode || object instanceof BNode;
    }

    private static boolean subjectIsBlank(Statement statement) {
        if (statement.getSubject() instanceof BNode) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean predicateIsBlank(Statement statement) {
        if (statement.getPredicate() instanceof BNode) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean objectIsBlank(Statement statement) {
        if (statement.getObject() instanceof BNode) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSameBnodeStatement(Statement statement, Statement statement2) {
        if (!OntologyUtility.hasABnode(statement)) {
            return false;
        }
        boolean correspondBnode;
        if (OntologyUtility.subjectIsBlank(statement2) && OntologyUtility.objectIsBlank(statement2)) {
            if (statement2.getSubject().stringValue().equals(statement2.getObject().stringValue())) {
                if (OntologyUtility.subjectIsBlank(statement) && OntologyUtility.objectIsBlank(statement)) {
                    if (statement.getSubject().stringValue().equals(statement.getObject().stringValue())) {
                        correspondBnode = true;
                    } else {
                        correspondBnode = false;
                    }
                } else {
                    correspondBnode = false;
                }
            } else if (OntologyUtility.subjectIsBlank(statement) && OntologyUtility.objectIsBlank(statement)) {
                correspondBnode = true;
            } else {
                correspondBnode = false;
            }
        } else if (OntologyUtility.subjectIsBlank(statement2)) {
            if (OntologyUtility.subjectIsBlank(statement) && !OntologyUtility.objectIsBlank(statement)) {
                correspondBnode = true;
            } else {
                correspondBnode = false;
            }
        } else if (OntologyUtility.objectIsBlank(statement) && !OntologyUtility.subjectIsBlank(statement)) {
            correspondBnode = true;
        } else {
            correspondBnode = false;
        }
        if (!correspondBnode) {
            return false;
        } else if (!OntologyUtility.subjectIsBlank(statement2)) {
            if (!statement2.getSubject().stringValue().equals(statement.getSubject().stringValue())) {
                return false;
            } else if (!statement2.getPredicate().stringValue().equals(statement.getPredicate().stringValue())) {
                return false;
            } else {
                return true;
            }
        } else if (!OntologyUtility.objectIsBlank(statement2)) {
            if (!statement2.getPredicate().stringValue().equals(statement.getPredicate().stringValue())) {
                return false;
            } else if (!statement2.getObject().stringValue().equals(statement.getObject().stringValue())) {
                return false;
            } else {
                return true;
            }
        } else if (!statement2.getPredicate().stringValue().equals(statement.getPredicate().stringValue())) {
            return false;
        } else {
            return true;
        }
    }

    private static Statement checkIfThereIsSameBnodeStatetementOnResultingMap(Map<Statement, List<Path>> resultingStatement, Statement statement) {
        Set<Statement> keySet = resultingStatement.keySet();
        for (Statement key : keySet) {
            if (OntologyUtility.isSameBnodeStatement(key, statement)) {
                return key;
            }
        }
        return null;
    }

    private static void addStatementOfPathToReverseReasoning(Map<Statement, List<Statement>> reverseReasoning, Path path, Statement statement) {
        List<Statement> inferredStatement;
        for (EnrichedStatement enrSt : path.getStatements()) {
            if (!enrSt.isIsInvented()) {
                inferredStatement = reverseReasoning.get(enrSt.getStatement());
                if (inferredStatement == null) {
                    inferredStatement = new ArrayList<>();
                    inferredStatement.add(statement);
                    reverseReasoning.put(enrSt.getStatement(), inferredStatement);
                } else if (!inferredStatement.contains(statement)) {
                    inferredStatement.add(statement);
                    reverseReasoning.put(enrSt.getStatement(), inferredStatement);
                }
            }
        }
    }
    
    private static void addStatementOfPathToReverseGraphReasoning(Map<Statement,List<Graph>> reverseGraphReasoning,Path path,Graph graph){
        List<Graph> inferredGraphs;
        for(EnrichedStatement enrSt: path.getStatements()){
            if(!enrSt.isIsInvented()){
                inferredGraphs=reverseGraphReasoning.get(enrSt.getStatement());
                if(inferredGraphs==null){
                    inferredGraphs=new ArrayList<>();
                    inferredGraphs.add(graph);
                    reverseGraphReasoning.put(enrSt.getStatement(), inferredGraphs);
                } else if(!inferredGraphs.contains(graph)){
                    inferredGraphs.add(graph);
                    reverseGraphReasoning.put(enrSt.getStatement(), inferredGraphs);
                }
            }
        }
    }
    
    public static Map<String, BNode> createBNodeMapFromTripleList(List<Triple> tripleList) {
        Map<String, BNode> bnodeMap = new HashMap<>();
        for (Triple triple : tripleList) {
            if (triple.getSubject().startsWith("_:")) {
                if (bnodeMap.get(triple.getSubject()) == null) {
                    bnodeMap.put(triple.getSubject(), null);
                }
            }
            if (triple.getObject().startsWith("_:")) {
                if (bnodeMap.get(triple.getObject()) == null) {
                    bnodeMap.put(triple.getObject(), null);
                }
            }
        }
        return bnodeMap;
    }
    
    public static String createValueRandomStringForMap(Map<String,String> map){
        String randomString;
        while(true){
            randomString=RandomStringUtils.randomAlphabetic(3);
            if(!map.containsValue(randomString)){
                break;
            }
        }
        return randomString;
    }

    public static boolean areAllBnodeOnBindingSet(BindingSet bindingSet) {
        Set<String> bindingNames = bindingSet.getBindingNames();
        boolean areBnode = true;
        for (String string : bindingNames) {
            if (bindingSet.getBinding(string).getValue() instanceof BNode) {
                continue;
            } else {
                areBnode = false;
                break;
            }
        }
        return areBnode;

    }
    
    public static PathListAndBnodeStatementFromStatement getPathListAndBnodeStatementFromStatement(Statement statement, Map<Statement, List<Path>> resultingStatement) {
        Statement newStatement=null;
        List<Path> pathList;
        if (OntologyUtility.hasABnode(statement)) {
            Statement bnodeStatement = checkIfThereIsSameBnodeStatetementOnResultingMap(resultingStatement, statement);
            if (bnodeStatement != null) {
                pathList = resultingStatement.get(bnodeStatement);
                newStatement = bnodeStatement;
            } else {
                pathList = resultingStatement.get(statement);
            }
        } else {
            pathList = resultingStatement.get(statement);
        }
        PathListAndBnodeStatementFromStatement result=new PathListAndBnodeStatementFromStatement();
        result.setStatement(statement);
        result.setBnodeStatement(newStatement);
        result.setPathList(pathList);
        return result;
    }
    
    
//    public static boolean areIsomorphicGraphs(List<Path> pathList1,List<Path> pathList2){
//        //verifica se graph1 è un sotto grafo di graph2
//        for(Path path1:pathList1){
//            boolean areSamePath=false;
//            for(Path path2:pathList2){
//                if(OntologyUtility.isSamePath(path1, path2)){
//                    areSamePath=true;
//                    break;
//                }
//            }
//            if(!areSamePath){
//                return false;
//            }
//        }
//        //tutti i path del grafo 1 sono le grafo 2
//        //verifica inversa
//        for(Path path2:pathList2){
//            boolean areSamePath=false;
//            for(Path path1:pathList1){
//                if(OntologyUtility.isSamePath(path1, path2)){
//                    areSamePath=true;
//                    break;
//                }
//            }    
//                if(!areSamePath){
//                    return false;
//                }
//        }
//        //grafi sono isomorfi
//        return true;
//    }
//    
    private static void addAllStatementOfGraphToSupportOntologyGraph(RepositoryConnection repository, Map<String, String> prefixes, Graph graph) {
        ExecutionQuery ex = new ExecutionQuery();
        for (Statement statement : graph.getStatementList()) {
            if(!repository.hasStatement(statement, true, repository.getValueFactory().createIRI(SUPPORT_ONTOLOGY_GRAPH))){
                repository.add(statement, repository.getValueFactory().createIRI(SUPPORT_ONTOLOGY_GRAPH));
            }
        }
    }
    
    public static boolean addStatementToReasoningResults(RepositoryConnection repository, Statement statement, Path newPath, List<Path> pathList, Map<Statement, List<Path>> resultingStatement, Map<Statement, List<Statement>> reverseReasoningResults) {
        if (pathList==null) {
            pathList = new ArrayList<>();
            pathList.add(newPath);
            resultingStatement.put(statement, pathList);
            if(!newPath.isGeneratesAnOntologyTriple()){
                repository.add(statement, repository.getValueFactory().createIRI(OntologyUtility.SUPPORT_ONTOLOGY_GRAPH));
            }
            OntologyUtility.addStatementOfPathToReverseReasoning(reverseReasoningResults, newPath, statement);
            return true;
        } else {
            int i = 0;
            for (; i < pathList.size(); i++) {
                Path oldPath = pathList.get(i);
                if (OntologyUtility.isSamePath(newPath, oldPath)) {
                    break;
                }
            }
            if (i == pathList.size()) {
                List<Path> newPathList=new ArrayList<>();
                newPathList.addAll(pathList);
                newPathList.add(newPath);
                resultingStatement.replace(statement, newPathList);
                OntologyUtility.addStatementOfPathToReverseReasoning(reverseReasoningResults, newPath, statement);
                return true;
            }
        }
        return false;
    }
    
    public static boolean addGraphToReasoningResults(RepositoryConnection repository,Map<String,String> prefixes, Graph graph, Path newPath, List<Path> pathList, Map<Graph, List<Path>> resultingGraph, Map<Statement, List<Graph>> reverseGraphReasoning) {
        if (pathList == null || pathList.isEmpty()) {
            pathList = new ArrayList<>();
            pathList.add(newPath);
            resultingGraph.put(graph, pathList);
            if(!graph.isIsAnOntologyGraph()){
                addAllStatementOfGraphToSupportOntologyGraph(repository,prefixes, graph);
            }
            addStatementOfPathToReverseGraphReasoning(reverseGraphReasoning, newPath, graph);
            return true;
        } else {
            int k = 0;
            for (; k < pathList.size(); k++) {
                Path oldPath = pathList.get(k);
                if (OntologyUtility.isSamePath(newPath, oldPath)) {
                    break;
                }
            }
            if (k == pathList.size()) {
                pathList.add(newPath);
                resultingGraph.put(graph, pathList);
                addStatementOfPathToReverseGraphReasoning(reverseGraphReasoning, newPath, graph);
                return true;
            }
        }
        return false;
    }

}
