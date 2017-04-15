/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Statement;

/**
 *
 * @author F.Camerlengo
 */
public class GraphHelp {
    
    /**
     * Scambia l'elemento in posizione i con l'elemento in posizione j all'interno di statementList e viceversa.
     * @param statementList
     * @param i
     * @param j 
     */
     private void swap(List<Statement> statementList,int i,int j){
        Statement statement=statementList.get(i);
        statementList.set(i, statementList.get(j));
        statementList.set(j, statement); 
    }
    
     /**
      * Ciascuno dei k elementi in statementList viene scambiato a turno con quello in posizione k-1 e i precedenti
      * k-1 elementi vengono permutati ricorsivamente.
      * @param statementList
      * @param listOfAllPermutations
      * @param n 
      */
    private void generateAllPermutationsRec(List<Statement> statementList,List<List<Statement>> listOfAllPermutations,int n){
        if(n==0){
            List<Statement> newStatementList=new ArrayList<>();
            for(Statement statement:statementList){
                newStatementList.add(statement);
            }
            listOfAllPermutations.add(newStatementList);
        }else{
            for(int i=0;i<n;i++){
                swap(statementList, i, n-1);
                generateAllPermutationsRec(statementList,listOfAllPermutations,n-1);
                swap(statementList, i, n-1);
            }
        }
        
    }
    
    public List<List<Statement>> generateAllPermutationsOfStatementList(List<Statement> statementList){
        int n=statementList.size();
        List<List<Statement>> listOfAllPermutations=new ArrayList<>();
        generateAllPermutationsRec(statementList, listOfAllPermutations, n);
        return listOfAllPermutations;
    }
    
    /**
     * Effettua la corrispondenza tra statement
     * @param statementList1
     * @param statementList2
     * @return resitutisce true se ad ogni statement di statementList2 corrisponde uno statement in statementList1
     */  
    public boolean checkIfStatementCorrespond(List<Statement> statementList1,List<Statement> statementList2){
        List<Statement> supportStatementList1=new ArrayList<>();
        for(Statement statement:statementList1){
            supportStatementList1.add(statement);
        }
        for(Statement statement2: statementList2 ){
            boolean isSameStatement= false;
            for(Statement statement1 : supportStatementList1){
                if(OntologyUtility.isSameBnodeStatement(statement1, statement2)){
                    isSameStatement=true;
                    supportStatementList1.remove(statement1);
                    break;
                }
            }
            if(!isSameStatement){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Effettua la corrispondenza delle posizioni dei bnode.
     * @param statementList1 
     * @param statementList2
     * @return restituisce true se statementList2 è un sottoinsieme di statementList1 false altrimenti.
     */
    public boolean checkIfBnodeCorrespond(List<Statement> statementList1,List<Statement> statementList2){
        Map<String,String> correspondingBnode=new HashMap<>();
        for (Statement statement2 : statementList2) {
            boolean isSameStatement = false;
            for (Statement statement1 : statementList1) {
                if (OntologyUtility.isSameBnodeStatement(statement2, statement1)) {
                    String subject = null;
                    if (statement2.getSubject() instanceof BNode) {
                        if (correspondingBnode.get(statement2.getSubject().stringValue()) == null) {
                            subject = statement1.getSubject().stringValue();
                        } else if (!correspondingBnode.get(statement2.getSubject().stringValue()).equals(statement1.getSubject().stringValue())) {
                            continue;
                        }
                    }
                    if (statement2.getObject() instanceof BNode) {
                        if (correspondingBnode.get(statement2.getObject().stringValue()) == null) {
                            correspondingBnode.put(statement2.getObject().stringValue(), statement1.getObject().stringValue());
                            if (subject != null) {
                                correspondingBnode.put(statement2.getSubject().stringValue(), statement1.getSubject().stringValue());
                                isSameStatement = true;
                                statementList1.remove(statement1);
                                break;
                            }
                            statementList1.remove(statement1);
                            isSameStatement = true;
                            break;
                        } else {
                            if (!correspondingBnode.get(statement2.getObject().stringValue()).equals(statement1.getObject().stringValue())) {
                                continue;
                            }
                            if (subject != null) {
                                correspondingBnode.put(statement2.getSubject().stringValue(), statement1.getSubject().stringValue());
                            }
                            statementList1.remove(statement1);
                            isSameStatement = true;
                            break;
                        }
                    } else {
                        if (subject != null) {
                            correspondingBnode.put(statement2.getSubject().stringValue(), statement1.getSubject().stringValue());
                            statementList1.remove(statement1);
                            isSameStatement = true;
                            break;
                        }
                        statementList1.remove(statement1);
                        isSameStatement = true;
                        break;
                    }
                }
            }
            if (!isSameStatement) {
                return false;
            }
        }
        return true;
    }
}
