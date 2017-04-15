package it.uniroma2.ase.domain;

import it.uniroma2.ase.model.utility.GraphHelp;
import it.uniroma2.ase.model.utility.OntologyUtility;
import java.util.List;
import org.eclipse.rdf4j.model.Statement;

/**
 *
 * @author L.Camerlengo
 */
public class Path {

    private List<EnrichedStatement> statements;

    private int inferenceRuleId;

    private String inferenceRuleName;

    private boolean generatesAnOntologyTriple;
    
    private boolean generatesAnOntologyGraph;
    
    public List<EnrichedStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<EnrichedStatement> statements) {
        this.statements = statements;
    }

    public int getInferenceRuleId() {
        return inferenceRuleId;
    }

    public void setInferenceRuleId(int inferenceRuleId) {
        this.inferenceRuleId = inferenceRuleId;
    }

    public String getInferenceRuleName() {
        return inferenceRuleName;
    }

    public void setInferenceRuleName(String inferenceRuleName) {
        this.inferenceRuleName = inferenceRuleName;
    }

    public boolean isGeneratesAnOntologyTriple() {
        return generatesAnOntologyTriple;
    }

    public boolean isGeneratesAnOntologyGraph() {
        return generatesAnOntologyGraph;
    }

    public void setGeneratesAnOntologyGraph(boolean generatesAnOntologyGraph) {
        this.generatesAnOntologyGraph = generatesAnOntologyGraph;
    }
    
    public void setGeneratesAnOntologyTriple(boolean generatesAnOntologyTriple) {
        this.generatesAnOntologyTriple = generatesAnOntologyTriple;
    }

    public boolean pathHasBnode() {
        List<EnrichedStatement> enrichedStatementList = this.getStatements();
        for (EnrichedStatement enStat : enrichedStatementList) {
            if (OntologyUtility.hasABnode(enStat.getStatement())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verify the isomorphism between graphs.
     * @param path
     * @return true if the curret path is isomorphic to path taken input 
     */
    public boolean containsAllBnodeStatement(Path path) {
        List<EnrichedStatement> enrichedStatementList = path.getStatements();
        List<List<Statement>> listOfStatementList = OntologyUtility.getTwoTypeOfStatementSet(path);
        List<List<Statement>> listOfStatementListOfThisPath = OntologyUtility.getTwoTypeOfStatementSet(this);
        List<Statement> listWithBnode = listOfStatementList.get(0);
        List<Statement> listWithoutBnode = listOfStatementList.get(1);
        List<Statement> listWithBnodeOfThisPath = listOfStatementListOfThisPath.get(0);
        List<Statement> listWithoutBnodeOfThisPath = listOfStatementListOfThisPath.get(1);
        //check if the all statements without bnode are equivalent
        if (!listWithoutBnodeOfThisPath.containsAll(listWithoutBnode)) {
            return false;
        }
        // check if the size of bnode statements list is same
        if(listWithBnode.size()!=listWithBnodeOfThisPath.size()){
            return false;
        }
        List<String> bnodeList = OntologyUtility.getAllBnodeFromStatementList(listWithBnode);
        List<String> bnodeListOfThisPath = OntologyUtility.getAllBnodeFromStatementList(listWithBnodeOfThisPath);
        GraphHelp graphHelp=new GraphHelp();
        // check if the number of bnodes are same
        if(bnodeList.size()!=bnodeListOfThisPath.size()){
            return false;
        //check if the list all triples of statements list contains two bnode
        }else if(bnodeList.size()==2*listWithBnode.size()){
            // check if the graphs are isomorphic
            if(graphHelp.checkIfStatementCorrespond(listWithBnodeOfThisPath, listWithBnode)){
                return true;
            }else{
                return false;
            }
        }
        //check if there is a statements correspondence
        if(!graphHelp.checkIfStatementCorrespond(listWithBnodeOfThisPath, listWithBnode)){
            return false;
        }
        // this  part needs to be replaced
        List<List<Statement>> listOfAllPermutationsOfStatementList = graphHelp.generateAllPermutationsOfStatementList(listWithBnodeOfThisPath);
        for(List<Statement> permutationList:listOfAllPermutationsOfStatementList){
            if(graphHelp.checkIfBnodeCorrespond(permutationList, listWithBnode)){
                return true;
            }
        }
        return false;
}

    @Override
    public String toString() {
        String value="";
        for(EnrichedStatement enSt:this.statements){
            value=value+enSt.getStatement()+"\n";
        }
        return value;
    }

    
}