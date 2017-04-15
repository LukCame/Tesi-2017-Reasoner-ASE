package it.uniroma2.ase.model.inferenceRuleHandler;


import it.uniroma2.ase.domain.InferenceRule;
import it.uniroma2.ase.domain.RuleGraphFromBnode;
import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.domain.Triple;
import it.uniroma2.ase.model.utility.OntologyUtility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author L.Camerlengo
 */
public class RewriterOfInferenceRuleOrRuleGraph {

    private String createDinamicVariable(List<String> allVariables) {
        String dinamicVariable = "?a";
        for (int i = 0; true; i++) {
            String variable = dinamicVariable + i;
            if (!allVariables.contains(variable)) {
                return variable;
            }
        }
    }

    private Map<String, String> createDinamicVariablesForSparqlListQuery(InferenceRule inferenceRule) {
        Map<String, String> variables = new HashMap<>();
        List<String> allVariables = OntologyUtility.getAllVariable(inferenceRule);
        List<Triple> premisesTriple = inferenceRule.getPremisesTriple();
        List<Triple> conclusionsTriple = inferenceRule.getConclusionsTriple();
        List<Triple> tripleList = new ArrayList<>();
        tripleList.addAll(premisesTriple);
        tripleList.addAll(conclusionsTriple);
        for (Triple triple : tripleList) {
            String subject = triple.getSubject();
            String predicate = triple.getPredicate();
            String object = triple.getObject();
            if (subject.startsWith("$$") && variables.get(subject) == null) {
                String dinamicVariable = createDinamicVariable(allVariables);
                allVariables.add(dinamicVariable);
                variables.put(subject, dinamicVariable);
            } else if (subject.startsWith("$") && variables.get(subject)==null) {
                String dinamicVariable = subject.replace("$", "?");
                variables.put(subject, dinamicVariable);
            }
            if (predicate.startsWith("$$") && variables.get(predicate) == null) {
                String dinamicVariable = createDinamicVariable(allVariables);
                allVariables.add(dinamicVariable);
                variables.put(predicate, dinamicVariable);
            }
            if (object.startsWith("$$") && variables.get(object) == null) {
                String dinamicVariable = createDinamicVariable(allVariables);
                allVariables.add(dinamicVariable);
                variables.put(object, dinamicVariable);
            } else if (object.startsWith("$") && variables.get(object)==null) {
                String dinamicVariable = object.replace("$", "?");
                variables.put(object, dinamicVariable);
            }
        }
        return variables;
    }

    private void rewriteInferenceRule(InferenceRule inferenceRule, Map<String, String> dinamicVariables) {
        List<Triple> premisesTriple = inferenceRule.getPremisesTriple();
        int count = premisesTriple.size();
        int i = 1;
        List<Triple> conclusionsTriple = inferenceRule.getConclusionsTriple();
        List<Triple> newPremisesTriple = new ArrayList<>();
        List<Triple> newConclusionsTriple = new ArrayList<>();
        List<Triple> tripleList = new ArrayList<>();
        tripleList.addAll(premisesTriple);
        tripleList.addAll(conclusionsTriple);
        for (Triple triple : tripleList) {
            String subject = triple.getSubject();
            String predicate = triple.getPredicate();
            String object = triple.getObject();
            Triple newTriple = new Triple();
            Triple propertyPathTriple1 = null;
            Triple propertyPathTriple2 = null;
            if (subject.startsWith("$$")) {
                newTriple.setSubject(dinamicVariables.get(subject));
            } else if (subject.startsWith("$")) {
                newTriple.setSubject(dinamicVariables.get(subject));
                propertyPathTriple1 = new Triple();
                propertyPathTriple1.setSubject(dinamicVariables.get(subject));
                propertyPathTriple1.setPredicate("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>*/<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>");
                propertyPathTriple1.setObject(dinamicVariables.get("$" + subject));
            } else {
                newTriple.setSubject(subject);
            }
            if (predicate.startsWith("$$")) {
                newTriple.setPredicate(dinamicVariables.get(predicate));
            } else {
                newTriple.setPredicate(predicate);
            }
            if (object.startsWith("$$")) {
                newTriple.setObject(dinamicVariables.get(object));
            } else if (object.startsWith("$")) {
                newTriple.setObject(dinamicVariables.get(object));
                propertyPathTriple2 = new Triple();
                propertyPathTriple2.setSubject(dinamicVariables.get(object));
                propertyPathTriple2.setPredicate("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>*/<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>");
                propertyPathTriple2.setObject(dinamicVariables.get("$" + object));
            } else {
                newTriple.setObject(object);
            }
            if (i <= count) {
                newPremisesTriple.add(newTriple);
                if (propertyPathTriple1 != null) {
                    newPremisesTriple.add(propertyPathTriple1);
                }
                if (propertyPathTriple2 != null) {
                    newPremisesTriple.add(propertyPathTriple2);
                }
                i = i + 1;
            } else {
                newConclusionsTriple.add(newTriple);
                if (propertyPathTriple1 != null) {
                    newConclusionsTriple.add(propertyPathTriple1);
                }
                if (propertyPathTriple2 != null) {
                    newConclusionsTriple.add(propertyPathTriple2);
                }
            }
        }
        inferenceRule.setRewritePremisesTriple(newPremisesTriple);
        inferenceRule.setRewriteConclusionsTriple(newConclusionsTriple);
    }
    
    public void rewriteInferenceRuleOrRuleGraphIfContainsList(Rules rules) {
        List<InferenceRule> inferenceRulesList = rules.getInferenceRules();
        List<RuleGraphFromBnode> graphRulesList = rules.getGraphRules();
        List<Object> rulesList=new ArrayList<>();
        rulesList.addAll(inferenceRulesList);
        rulesList.addAll(graphRulesList);
        for (Object rule:rulesList) {
            if(rule instanceof RuleGraphFromBnode){
                RuleGraphFromBnode ruleGraph=(RuleGraphFromBnode) rule;
                if(ruleGraph.getInferenceRuleInfo().HasList()){
                    Map<String, String> dinamicVariables = createDinamicVariablesForSparqlListQuery(ruleGraph.getInferenceRuleInfo());
                    Set<String> keySet = dinamicVariables.keySet();
                    this.rewriteInferenceRule(ruleGraph.getInferenceRuleInfo(), dinamicVariables);
                }
            }else{
                InferenceRule inferenceRule=(InferenceRule) rule;
            if (inferenceRule.HasList()) {
                Map<String, String> dinamicVariables = createDinamicVariablesForSparqlListQuery(inferenceRule);
                this.rewriteInferenceRule(inferenceRule, dinamicVariables);
            }
            }
        }
    }

}
