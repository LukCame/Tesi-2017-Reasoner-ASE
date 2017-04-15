package it.uniroma2.ase.domain;



import it.uniroma2.ase.model.converterHandler.StatementConverter;
import it.uniroma2.ase.model.utility.OntologyUtility;
import java.util.List;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Statement;

/**
 *
 * @author L.Camerlengo
 */
public class Graph {

    private List<Triple> tripleList;
    
    private List<Statement> statementList;
    
    private boolean isAnOntologyGraph;

    public List<Triple> getTripleList() {
        return tripleList;
    }

    public void setTripleList(List<Triple> tripleList) {
        this.tripleList = tripleList;
    }

    public List<Statement> getStatementList() {
        return statementList;
    }

    public void setStatementList(List<Statement> statementList) {
        this.statementList = statementList;
    }

    public boolean isIsAnOntologyGraph() {
        return isAnOntologyGraph;
    }

    public void setIsAnOntologyGraph(boolean isAnOntologyGraph) {
        this.isAnOntologyGraph = isAnOntologyGraph;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }
        if(!(obj instanceof Graph)){
            return false;
        }
        Graph graph=(Graph) obj;
        List<Statement> statementOfGraph = graph.getStatementList();
        Path path=new Path();
        StatementConverter statConv=new StatementConverter();
        path.setStatements(statConv.convertListOfStatementToEnrichedStatementList(statementList));
        Path pathOfThisGraph = new Path();
        pathOfThisGraph.setStatements(statConv.convertListOfStatementToEnrichedStatementList(this.statementList));
        return OntologyUtility.isSamePath(path, pathOfThisGraph);
    }

    @Override
    public int hashCode() {
        int hashCode=0;     
        for(Statement statement:this.statementList){
            if(statement.getSubject() instanceof BNode){
                hashCode=hashCode+1;
            }else{
                hashCode=hashCode+statement.getSubject().stringValue().hashCode();
            }
            hashCode=hashCode+statement.getPredicate().stringValue().hashCode();
            if(statement.getObject() instanceof BNode){
                hashCode=hashCode+1;
            }else{
                hashCode=hashCode+statement.getObject().stringValue().hashCode();
            }
        }
        return hashCode;
    }

    
}
