/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.converterHandler;

import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.domain.Triple;
import it.uniroma2.ase.domain.RuleGraphFromBnode;
import it.uniroma2.ase.domain.Type;
import it.uniroma2.ase.domain.InferenceRule;
import it.uniroma2.ase.domain.Graph;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author F.Camerlengo
 */
public class RuleGraphConverter {


    private RuleGraphFromBnode getRuleGraphFromInferenceRule(InferenceRule inferenceRule) {
        List<Triple> conclusionsTriple = inferenceRule.getConclusionsTriple();
        List<Graph> graphList = new ArrayList<>();
        List<String> bnodeList = new ArrayList<>();
        /*creo una lista che contiene tutti i bnode che si trovano all'intero delle conclusioni di una regola di inferenza e se
        una tripla non contiene bnode allora viene creato un grafo che la contiene ed esso verrà gestito come una normale regola di inferenza.*/
        for (int i = 0; i < conclusionsTriple.size(); i++) {
            String sub = conclusionsTriple.get(i).getSubject();
            String obj = conclusionsTriple.get(i).getObject();
            if (sub.startsWith("_:")) {
                if (!bnodeList.contains(sub)) {
                    bnodeList.add(sub);
                }
            }
            if (obj.startsWith("_:")) {
                if (!bnodeList.contains(obj)) {
                    bnodeList.add(obj);
                }
            }
            if (!sub.startsWith("_:") && !obj.startsWith("_:")) {
                List<Triple> tripleOfGraph = new ArrayList<>();
                Graph graph = new Graph();
                tripleOfGraph.add(conclusionsTriple.get(i));
                graph.setTripleList(tripleOfGraph);
                graphList.add(graph);
                conclusionsTriple.remove(i);
                i--;
            }
        }
        String bnode = null;
        //prelevo un bnode dalla lista se essa ne contiene almeno uno 
        if (!bnodeList.isEmpty()) {
            bnode = bnodeList.get(0);
        }
        List<Triple> supportList = new ArrayList<>();
        List<Triple> tripleOfGraph = new ArrayList<>();
        //Ogni tripla delle conclusioni che contiene il bnode corrente viene aggiunta ad una lista di appoggio
        while (!conclusionsTriple.isEmpty()) {
            int i = 0;
            while (i < conclusionsTriple.size()) {
                Triple triple = conclusionsTriple.get(i);
                if (triple.getSubject().equals(bnode)) {
                    if(!supportList.contains(triple)){
                        supportList.add(triple);
                    }
                } else if (triple.getObject().equals(bnode)) {
                    if(!supportList.contains(triple)){
                        supportList.add(triple);
                    }
                }
                i++;
            }
            bnodeList.remove(bnode);
            //Ogni tripla all'interno della lista di supporto viene analizzata
            while (!supportList.isEmpty()) {
                Triple triple = supportList.get(0);
                String sub = triple.getSubject();
                String obj = triple.getObject();
                /*Se nella tripla è contenuto il bnode corrente come soggetto oppure contiene come soggetto un bnode analizzato in precedenza
                allora viene creato lo statement relativo a tale tripla,viene aggiunto tale statement al grafo corrente e viene rimossa la tripla dalla lista d'appoggio*/
                if (sub.startsWith("_:") && !obj.startsWith("_:")) {
                    if (sub.equals(bnode) || !bnodeList.contains(sub)) {
                        tripleOfGraph.add(triple);
                        conclusionsTriple.remove(triple);
                        supportList.remove(0);
                        /*Altrimenti viene ripetuto il ciclo più esterno in modo da inserire all'interno della lista di appoggio le triple che contengono
                il bnode contenuto nel soggetto*/
                    } else {
                        bnode = sub;
                        break;
                    }
                }
                //come sopra per l'oggetto
                if (!sub.startsWith("_:") && obj.startsWith("_:")) {
                    if (sub.equals(bnode) || !bnodeList.contains(obj)) {
                        tripleOfGraph.add(triple);
                        conclusionsTriple.remove(triple);
                        supportList.remove(0);
                    } else {
                        bnode = obj;
                        break;
                    }
                }
                if (sub.startsWith("_:") && obj.startsWith("_:")) {
                    /*se la tripla corrente contiene due bnode uguali a quello corronte oppure contiene due bnode analizzati in precedenza
                    allora viene creato lo statement relativo a tale tripla, viene aggiunto tale statement al grafo corrente e viene rimossa la tripla dalla lista di appoggio*/
                    if ((sub.equals(bnode) && obj.equals(bnode)) || (!bnodeList.contains(sub) && !bnodeList.contains(obj))) {
                        tripleOfGraph.add(triple);
                        conclusionsTriple.remove(triple);
                        supportList.remove(0);
                        /* altrimenti il bnode del soggetto deve essere ancora analizzato viene ripetuto il ciclo più esterno
                       in modo tale da inserire nella lista d'appoggio tutte le triple che hanno tale bnode. 
                         */
                    } else if (bnodeList.contains(sub)) {
                        bnode = sub;
                        break;
                        /*altrimenti viene ripetuto il ciclo più esterno in modo tale da inserire nella lista d'appoggio tutte le
                      triple che hanno il bnode dell'oggetto.*/
                    } else {
                        bnode = obj;
                        break;
                    }
                }
            }

            /*se sono state analizzate tutte le triple delle lista di appoggio allora viene creato un oggetto GraphWithBnode che contiene tutti gli statement
            del grafo corrente*/
            if (supportList.isEmpty()) {
                Graph graph = new Graph();
                graph.setTripleList(tripleOfGraph);
                graphList.add(graph);
                tripleOfGraph = null;
                tripleOfGraph = new ArrayList<>();
                if (!bnodeList.isEmpty()) {
                    bnode = bnodeList.get(0);
                }
            }
        }
        RuleGraphFromBnode ruleGraph = new RuleGraphFromBnode();
        List<Triple> conclusionsTripleToBeSet=new ArrayList<>();
        for(Graph graph:graphList){
            for(Triple triple:graph.getTripleList()){
                conclusionsTripleToBeSet.add(triple);
            }
        }
        inferenceRule.setConclusionsTriple(conclusionsTripleToBeSet);
        ruleGraph.setInferenceRuleInfo(inferenceRule);
        ruleGraph.setGraphList(graphList);
        return ruleGraph;
    }
    
    public void addGraphRulesToRules(Rules rules){
        List<InferenceRule> inferenceRuleList=rules.getInferenceRules();
        List<RuleGraphFromBnode> ruleGraphList=new ArrayList<>();
        int i=0;
        int n=inferenceRuleList.size();
        int j=0;
        while(i<n){
            if(inferenceRuleList.get(j).getType().equals(Type.ruleGraph)){
                ruleGraphList.add(getRuleGraphFromInferenceRule(inferenceRuleList.get(j)));
                rules.getInferenceRules().remove(j);
            }else{
                j++;
            }
            i++;
        }
        rules.setGraphRules(ruleGraphList);
    }
    
}
