/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.inferenceRuleHandler;

import it.uniroma2.ase.domain.Graph;
import it.uniroma2.ase.domain.InferenceRule;
import it.uniroma2.ase.domain.RuleGraphFromBnode;
import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.domain.Triple;
import it.uniroma2.ase.model.exception.BnodeInferenceRulesValidationException;
import it.uniroma2.ase.model.exception.BnodeRuleGraphValidationException;
import it.uniroma2.ase.model.exception.PrefixesValidationException;
import it.uniroma2.ase.model.exception.RuleConclusionsTripleValidationException;
import it.uniroma2.ase.model.exception.RuleFilterConditionValidationException;
import it.uniroma2.ase.model.exception.RuleInformationValidationException;
import it.uniroma2.ase.model.exception.RuleListValidationException;
import it.uniroma2.ase.model.exception.RulePremisesElementsValidationException;
import it.uniroma2.ase.model.exception.RulesValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author L.Camerlengo
 */
public class Validator {

    private final static Logger LOGGER = Logger.getLogger(Validator.class.getName());

    public boolean validate(Rules rules) throws RulesValidationException {
        LOGGER.log(Level.INFO, "Sto nel validatore");
        List<InferenceRule> inferenceRulesList = rules.getInferenceRules();
        List<RuleGraphFromBnode> graphRulesList = rules.getGraphRules();
        List<Object> rulesList = new ArrayList<>();
        rulesList.addAll(inferenceRulesList);
        rulesList.addAll(graphRulesList);
        Map<String, String> prefixes = rules.getPrefixes();
        validateGeneralInfoOfInferenceRules(inferenceRulesList);
        for (Object objectRule : rulesList) {
            if (objectRule instanceof RuleGraphFromBnode) {
                RuleGraphFromBnode rule = (RuleGraphFromBnode) objectRule;
                List<Triple> premisesTriple = rule.getInferenceRuleInfo().getPremisesTriple();
                validatePrefixes(premisesTriple, prefixes);
                Map<String, String> premisesResources = getElementsOfTripleList(premisesTriple);
                validatePremisesElements(premisesResources, rule.getInferenceRuleInfo().getInferenceRuleName());
                String filter = rule.getInferenceRuleInfo().getFilterCondition();
                validateFilterCondition(filter, premisesResources, rule.getInferenceRuleInfo().getInferenceRuleName());
                List<Graph> graphList = rule.getGraphList();
                boolean thereIsAtLeastBnode=false;
                List<Triple> conclusionsTriple=new ArrayList<>();
                for(Graph graph:graphList){
                    conclusionsTriple.addAll(graph.getTripleList());
                    if(!thereIsAtLeastBnode){
                        thereIsAtLeastBnode=verifyIfThereIsAtLeastBnodeOnTheConclusions(conclusionsTriple);
                    }
                }
                if(!thereIsAtLeastBnode){
                    throw new BnodeRuleGraphValidationException("The rule graph "+rule.getInferenceRuleInfo().getInferenceRuleName()+" need at least a bnode on conclusions");
                }
                validateConclusionsElements(conclusionsTriple, premisesResources, rule.getInferenceRuleInfo().getInferenceRuleName());
                Map<String, String> conclusionsResources = getElementsOfTripleList(conclusionsTriple);
                validateResourceListOfInferenceRule(conclusionsResources, premisesResources, rule.getInferenceRuleInfo().getInferenceRuleName());
            } else {
                InferenceRule rule = (InferenceRule) objectRule;
                List<Triple> premisesTriple = rule.getPremisesTriple();
                validatePrefixes(premisesTriple, prefixes);
                Map<String, String> premisesResources = getElementsOfTripleList(premisesTriple);
                validatePremisesElements(premisesResources, rule.getInferenceRuleName());
                String filter = rule.getFilterCondition();
                validateFilterCondition(filter, premisesResources, rule.getInferenceRuleName());
                List<Triple> conclusionsTriple = rule.getConclusionsTriple();
                if(verifyIfThereIsAtLeastBnodeOnTheConclusions(conclusionsTriple)){
                    throw new BnodeInferenceRulesValidationException("The inferenceRule "+rule.getInferenceRuleName()+" may not have an bnode on conclusions");
                }
                validatePrefixes(conclusionsTriple, prefixes);
                validateConclusionsElements(conclusionsTriple, premisesResources, rule.getInferenceRuleName());
                Map<String, String> conclusionsResources = getElementsOfTripleList(conclusionsTriple);
                validateResourceListOfInferenceRule(conclusionsResources, premisesResources, rule.getInferenceRuleName());
            }
        }
        return true;
    }

    private Map<String, String> getElementsOfTripleList(List<Triple> tripleList) {
        Map<String, String> elements = new HashMap<>();
        for (Triple triple : tripleList) {
            String subject = triple.getSubject();
            String predicate = triple.getPredicate();
            String object = triple.getObject();
            if (subject.startsWith("$")) {
                elements.put(subject, "list");
            } else if (subject.startsWith("?")) {
                elements.put(subject, "var");
            } else if (subject.startsWith("_")) {
                elements.put(subject, "bnode");
            }
            if (predicate.startsWith("?")) {
                elements.put(predicate, "var");
            }
            if (object.startsWith("?")) {
                elements.put(object, "var");
            } else if (object.startsWith("$")) {
                elements.put(object, "list");
            } else if (object.startsWith("_")) {
                elements.put(object, "bnode");
            }
        }
        return elements;
    }

    private void validatePrefixes(List<Triple> triple, Map<String, String> prefixes) throws RulesValidationException{
        for (Triple trip : triple) {
            String subject = trip.getSubject();
            String predicate = trip.getPredicate();
            String object = trip.getObject();
            if (!(subject.startsWith("$") || subject.startsWith("?") || subject.startsWith("_")||subject.startsWith("<"))) {
                String[] split = subject.split(":");
                if (prefixes.get(split[0].concat(":")) == null) {
                    throw new PrefixesValidationException("The prefix "+split[0]+" is invalid");
                }
            }
            if (!(predicate.startsWith("?")||predicate.startsWith("<"))) {
                String[] split = predicate.split(":");
                if (prefixes.get(split[0].concat(":")) == null) {
                    throw new PrefixesValidationException("The prefix "+split[0]+" is invalid");
                }
            }
            if (!((object.codePointAt(0) >= 48 && object.codePointAt(0) <= 57) || (object.startsWith("?") || object.startsWith("$") || object.startsWith("_") || object.contains("@")||object.startsWith("<")))) {
                if (object.contains("^^")) {
                    String[] split = object.split("\\^");
                    String[] split2 = split[2].split(":");
                    if (prefixes.get(split2[0].concat(":")) == null) {
                        throw new PrefixesValidationException("The prefix "+split2[0]+" is invalid");
                    }
                } else {
                    String[] split = object.split(":");
                    if (prefixes.get(split[0].concat(":")) == null) {
                        throw new PrefixesValidationException("The prefix "+split[0]+" is invalid");
                    }
                }
            }
        }
    }

    private void validateGeneralInfoOfInferenceRules(List<InferenceRule> inferenceRules) throws RuleInformationValidationException {
        Map<Integer, InferenceRule> mapInferenceRule = new HashMap<Integer, InferenceRule>();
        Map<String, InferenceRule> mapInferenceRulesName = new HashMap<String, InferenceRule>();
        for (InferenceRule inferenceRule : inferenceRules) {
            if (mapInferenceRule.get(inferenceRule.getInferenceRuleID()) == null) {
                mapInferenceRule.put(inferenceRule.getInferenceRuleID(), inferenceRule);
            } else {
                throw new RuleInformationValidationException("rules with id "+inferenceRule.getInferenceRuleID()+" in shared");
            }
            if (mapInferenceRulesName.get(inferenceRule.getInferenceRuleName()) == null) {
                mapInferenceRulesName.put(inferenceRule.getInferenceRuleName(), inferenceRule);
            } else {
                throw new RuleInformationValidationException("rules with name "+inferenceRule.getInferenceRuleName()+" in shared");
            }
        }
    }

    private void validatePremisesElements(Map<String, String> elements, String ruleName) throws RulePremisesElementsValidationException {
        Set<String> keySet = elements.keySet();
        for (String key : keySet) {
            if (key.startsWith("$")) {
                if (elements.containsKey(key.replace("$", "?"))) {
                    throw new RulePremisesElementsValidationException("In the rule "+ruleName+" you can not use the list "+key+" inside the premises");
                }
            }
        }
    }

    private void validateFilterCondition(String filter, Map<String, String> elements, String ruleName) throws RuleFilterConditionValidationException {
        if (filter != null) {
            char[] charFilter = filter.toCharArray();
            int size = charFilter.length;
            for (int i = 0; i < size; i++) {
                if (charFilter[i] == '?' || charFilter[i] == '$') {
                    String element = "" + String.valueOf(charFilter[i]);
                    for (int j = i + 1; j < size; j++) {
                        if (Character.isLetter(charFilter[j]) || Character.isDigit(charFilter[j]) || charFilter[j] == '_') {
                            element = element + String.valueOf(charFilter[j]);
                        } else {
                            i = j;
                            break;
                        }
                    }
                    if (elements.get(element) == null) {
                        throw new RuleFilterConditionValidationException("In the rule "+ruleName+" you can not use the resource "+element+" inside the filter condition");
                    }
                }
            }
        }
    }

    private void validateConclusionsElements(List<Triple> conclusionsTriple, Map<String, String> elements, String ruleName) throws RuleConclusionsTripleValidationException {
        for (Triple triple : conclusionsTriple) {
            String subject = triple.getSubject();
            String predicate = triple.getPredicate();
            String object = triple.getObject();
            if (subject.startsWith("?")) {
                if (elements.get(subject) == null) {
                    throw new RuleConclusionsTripleValidationException("In the rule "+ruleName+" you can not use the variable "+subject+" inside the conclusions");
                }
            }
            if (subject.startsWith("_")) {
                if (elements.get(subject) != null) {
                    throw new RuleConclusionsTripleValidationException("In the rule "+ruleName+" you can not use the blank node "+subject+" inside the conclusions");
                }
            }
            if (predicate.startsWith("?")) {
                if (elements.get(predicate) == null) {
                    throw new RuleConclusionsTripleValidationException("In the rule "+ruleName+" you can not use the variable "+predicate+" inside the conclusions");
                }
            }
            if (object.startsWith("?")) {
                if (elements.get(object) == null) {
                    throw new RuleConclusionsTripleValidationException("In the rule "+ruleName+" you can not use the variable "+object+" inside the conclusions");
                }
            }
            if (object.startsWith("_")) {
                if (elements.get(object) != null) {
                    throw new RuleConclusionsTripleValidationException("In the rule "+ruleName+" you can not use the blank node "+object+" inside the conclusions");
                }
            }
        }
    }
    
    private boolean verifyIfThereIsAtLeastBnodeOnTheConclusions(List<Triple> conclusions){
        boolean thereIsAtLeastBnode=false;
        for(Triple triple:conclusions){
            if(triple.getSubject().startsWith("_:")){
                thereIsAtLeastBnode=true;
                break;
            }
            if(triple.getPredicate().startsWith("_:")){
                thereIsAtLeastBnode=true;
                break;
            }
            if(triple.getObject().startsWith("_:")){
                thereIsAtLeastBnode=true;
                break;
            }
        }
        return thereIsAtLeastBnode;
    }
    
    private void validateResourceListOfInferenceRule(Map<String, String> conclusions, Map<String, String> premises, String ruleName) throws RuleListValidationException {
        Set<String> premisesKeySet = premises.keySet();
//        System.out.println("premesse");
//        for(String key:premisesKeySet){
//            System.out.println(key+" "+premises.get(key));
//        }
//        System.out.println("conclusioni");
        Set<String> conclusionsKeySet = conclusions.keySet();
//        for(String key:conclusionsKeySet){
//            System.out.println(key+" "+conclusions.get(key));
//        }
        for (String key : premisesKeySet) {
            if (key.startsWith("$$")) {
                String keyList = key.replace("$$", "$");
                if (premises.get(keyList) == null) {
                    throw new RuleListValidationException("In the rule "+ruleName+" you can not use the construct "+key+" without using the list "+keyList);
                }
            } else if (key.startsWith("$")) {
                String keyResource = key.replace("$", "$$");
                if (premises.get(keyResource) == null && conclusions.get(keyResource) == null) {
                    throw new RuleListValidationException("In the rule "+ruleName+" you can not use the list "+key+" without using the construct "+keyResource);
                }
            }
        }
        for (String key : conclusionsKeySet) {
            if (key.startsWith("$$")) {
                String keyList = key.replace("$$", "$");
                if (premises.get(keyList) == null) {
                    LOGGER.log(Level.INFO, "non puoi utilizzare il costrutto " + key + " all'interno delle conclusioni");
                }
            } else if (key.startsWith("$")) {
                if (premises.get(key) == null) {
                    LOGGER.log(Level.INFO, "non puoi utilizzare la lista " + key + " all'interno delle conclusioni");
                }
            }
        }
    }

}
