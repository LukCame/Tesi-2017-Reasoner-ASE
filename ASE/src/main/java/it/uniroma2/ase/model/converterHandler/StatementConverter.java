package it.uniroma2.ase.model.converterHandler;


import it.uniroma2.ase.domain.EnrichedStatement;
import it.uniroma2.ase.domain.Triple;
import it.uniroma2.ase.model.reasoningHandler.ExecutionQuery;
import it.uniroma2.ase.model.utility.OntologyUtility;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.RepositoryConnection;

/**
 *
 * @author L.Camerlengo
 */
public class StatementConverter {

    public List<EnrichedStatement> getStatementsFromPremisesTriple(List<Triple> premisesTriple, BindingSet tuple, RepositoryConnection repository, Map<String, String> prefixes) {
        Map<String, BNode> bnodeMap = OntologyUtility.createBNodeMapFromTripleList(premisesTriple);
        List<EnrichedStatement> statementList = new ArrayList<>();
        StatementConverter statementConverter=new StatementConverter();
        try {
            for (Triple triple : premisesTriple) {
                EnrichedStatement enrStatement = new EnrichedStatement();
                if (triple.getPredicate().equals("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>*/<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>")) {
                    Statement artStatement = getRewriteStatementFromTriple(triple, tuple, repository);
                    enrStatement.setStatement(artStatement);
                    enrStatement.setIsInvented(true);
                } else {
                    Statement statement =statementConverter.getStatementWithoutPrefixes(getStatementFromTriple(triple, tuple, repository, bnodeMap),repository,prefixes);
                    enrStatement.setStatement(statement);
                    ExecutionQuery executionQuery = new ExecutionQuery();
                    if (executionQuery.verifyIfStatementIsOnSupportGraph(statement, prefixes, repository)) {
                        enrStatement.setIsInferred(true);
                    } else {
                        enrStatement.setIsInferred(false);
                    }
                }
                statementList.add(enrStatement);
            }
        } catch (Exception e) {
            System.out.println("eccezione");
        }
        return statementList;
    }

    public List<Statement> getStatementsFromConclusionsTriple(List<Triple> conclusionsTriple, BindingSet tuple, RepositoryConnection repository) {
        Map<String, BNode> bnodeMap = OntologyUtility.createBNodeMapFromTripleList(conclusionsTriple);
        List<Statement> statementList = new ArrayList<>();
        for (Triple triple : conclusionsTriple) {
            statementList.add(getStatementFromTriple(triple, tuple, repository, bnodeMap));
        }
        return statementList;
    }

    private Statement getStatementFromTriple(Triple triple, BindingSet tuple, RepositoryConnection repository, Map<String, BNode> bnodeMap) {
        Resource subject;
        IRI predicate;
        Value object;
        ValueFactory factory = repository.getValueFactory();
        if (triple.getSubject().startsWith("?")) {
            subject = (Resource) tuple.getBinding(triple.getSubject().replace("?", "")).getValue();
        } else if (triple.getSubject().startsWith("_:")) {
            if (bnodeMap.get(triple.getSubject()) == null) {
                BNode bnode = factory.createBNode();
                subject = bnode;
                bnodeMap.put(triple.getSubject(), bnode);
            } else {
                subject = bnodeMap.get(triple.getSubject());
            }
        } else {
            subject = (Resource) factory.createIRI(triple.getSubject());
        }
        if (triple.getPredicate().startsWith("?")) {
            predicate = (IRI) tuple.getBinding(triple.getPredicate().replace("?", "")).getValue();
        } else {
            predicate = factory.createIRI(triple.getPredicate());
        }
        if (triple.getObject().startsWith("?")) {
            object = tuple.getBinding(triple.getObject().replace("?", "")).getValue();
        } else if (triple.getObject().startsWith("_:")) {
            if (bnodeMap.get(triple.getObject()) == null) {
                BNode bnode = factory.createBNode();
                object = bnode;
                bnodeMap.put(triple.getObject(), bnode);
            } else {
                object = bnodeMap.get(triple.getObject());
            }
        } else {
            if(triple.getObject().startsWith("\"")){
                if(triple.getObject().contains("@")){
                    String[] split = triple.getObject().split("@");
                    String label=split[0].replaceAll("\"", "");
                    String language=split[1];
                    object  = factory.createLiteral(label, language);
                }else{
                    int index=triple.getObject().indexOf('^');
                    String label=triple.getObject().substring(0, index).replaceAll("\"", "");
                    IRI datatype=factory.createIRI(triple.getObject().substring(index+2));
                    object= factory.createLiteral(label, datatype);
                }
            }else{
//                if(triple.getObject().startsWith("<")||triple.getObject().contains(":")){
                    object = (Value) factory.createIRI(triple.getObject());
//                }else{
//                    int integer=Integer.parseInt(triple.getObject());
//                    object = factory.createLiteral(integer);
//                }
            }
        }
        return factory.createStatement(subject, predicate, object);
    }
    
    public Statement getStatementWithoutPrefixes(Statement statement, RepositoryConnection repository, Map<String, String> prefixes) {
        Resource subject = statement.getSubject();
        IRI predicate = statement.getPredicate();
        Value object = statement.getObject();
        boolean subjectIsSet = false;
        boolean predicateIsSet = false;
        boolean objectIsSet = false;
        ValueFactory factory = repository.getValueFactory();
        if (statement.getSubject().stringValue().startsWith("<")) {
            subject = factory.createIRI(statement.getSubject().stringValue().replaceAll("[<>]", ""));
            subjectIsSet = true;
        }
        if (statement.getPredicate().stringValue().startsWith("<")) {
            predicate = factory.createIRI(statement.getPredicate().stringValue().replaceAll("[<>]", ""));
            predicateIsSet = true;
        }
        if (statement.getObject().stringValue().startsWith("<")) {
            object = factory.createIRI(statement.getObject().stringValue().replaceAll("[<>]", ""));
            objectIsSet = true;
        }
        Set<String> keySet = prefixes.keySet();
        for (String prefix : keySet) {
            if (!subjectIsSet && statement.getSubject().stringValue().startsWith(prefix)) {
                String[] split = statement.getSubject().stringValue().split(":");
                String res = split[1];
                subject = factory.createIRI(prefixes.get(prefix).replaceAll("[<>]", "") + res);
                subjectIsSet = true;
            }
            if (!predicateIsSet && statement.getPredicate().stringValue().startsWith(prefix)) {
                String[] split = statement.getPredicate().stringValue().split(":");
                String res = split[1];
                predicate = factory.createIRI(prefixes.get(prefix).replaceAll("[<>]", "") + res);
                predicateIsSet = true;
            }
            if (!objectIsSet && statement.getObject().stringValue().startsWith(prefix)) {
                String[] split = statement.getObject().stringValue().split(":");
                String res = split[1];
                object = factory.createIRI(prefixes.get(prefix).replaceAll("[<>]", "") + res);
                objectIsSet = true;
            }
        }
        return factory.createStatement(subject, predicate, object);
    }

    private Statement getRewriteStatementFromTriple(Triple triple, BindingSet tuple, RepositoryConnection repository) {
        Resource subject = (Resource) tuple.getBinding(triple.getObject().replace("?", "")).getValue();
        IRI predicate = repository.getValueFactory().createIRI("http://www.w3.org/2000/01/rdf-schema#member");
        Value object = tuple.getBinding(triple.getSubject().replace("?", "")).getValue();
        return repository.getValueFactory().createStatement(subject, predicate, object);
    }

    public List<EnrichedStatement> convertListOfStatementToEnrichedStatementList(List<Statement> statementList){
        List<EnrichedStatement> enrichedStatementList=new ArrayList<>();
        for(Statement statement:statementList){
            EnrichedStatement enrSt=new EnrichedStatement();
            enrSt.setStatement(statement);
            enrichedStatementList.add(enrSt);
        }
        return enrichedStatementList;
    }
}
