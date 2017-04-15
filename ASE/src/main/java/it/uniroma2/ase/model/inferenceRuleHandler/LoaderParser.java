package it.uniroma2.ase.model.inferenceRuleHandler;

import grammar.InferenceRulesGrammarBaseListener;
import grammar.InferenceRulesGrammarParser;
import grammar.InferenceRulesGrammarParser.InconsistencyContext;
import grammar.InferenceRulesGrammarParser.New_ruleContext;
import grammar.InferenceRulesGrammarParser.Rule_graphContext;
import grammar.InferenceRulesGrammarParser.Start_ruleContext;
import grammar.InferenceRulesGrammarParser.TripleContext;
import it.uniroma2.ase.domain.InferenceRule;
import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.domain.Triple;
import it.uniroma2.ase.domain.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Get the parsed inference rule from parsing tree.
 *
 * @author L.Camerlengo
 *
 */
public class LoaderParser extends InferenceRulesGrammarBaseListener {

    private Rules inferenceRules = new Rules();

    @Override
    public void enterParseInferenceRule(InferenceRulesGrammarParser.ParseInferenceRuleContext ctx) {
        Start_ruleContext start_rule = ctx.start_rule();
        List<New_ruleContext> rules = start_rule.new_rule();
        List<InconsistencyContext> inconsistencies = start_rule.inconsistency();
        List<InferenceRulesGrammarParser.Rule_graphContext> rule_graph = start_rule.rule_graph();
        List<InferenceRule> inferenceRules = new ArrayList<>();
        Map<String, String> mapPrefixes = new HashMap<>();
        for (New_ruleContext rule : rules) {
            String name;
            String id;
            List<TripleContext> premisesTriple = rule.premises().triple();
            List<TripleContext> conclusionsTriple = rule.conclusions().triple();
            InferenceRule inferenceRule = new InferenceRule();
            if (rule.rule_information1().RULE_NAME().toString().contains("name :")) {
                name = rule.rule_information1().RULE_NAME().toString().replaceFirst("name : ", "");
            } else {
                name = rule.rule_information1().RULE_NAME().toString().replaceFirst("name: ", "");
            }
            if (rule.rule_information1().RULE_ID().toString().contains("id :")) {
                id = rule.rule_information1().RULE_ID().toString().replaceFirst("id : ", "");
            } else {
                id = rule.rule_information1().RULE_ID().toString().replaceFirst("id: ", "");
            }
            inferenceRule.setPremisesTriple(parseTripleList(premisesTriple, inferenceRule));
            inferenceRule.setConclusionsTriple(parseTripleList(conclusionsTriple, inferenceRule));
            inferenceRule.setType(Type.rule);
            inferenceRule.setInferenceRuleName(name);
            inferenceRule.setInferenceRuleID(Integer.parseInt(id));
            if (rule.filter() != null) {
                inferenceRule.setFilterCondition(rule.filter().condition().getText());
            }
            inferenceRules.add(inferenceRule);
        }
        for (InconsistencyContext rule : inconsistencies) {
            String name;
            String id;
            List<TripleContext> premisesTriple = rule.premises().triple();
            InferenceRule inferenceRule = new InferenceRule();
            if (rule.rule_information2().RULE_NAME().toString().contains("name :")) {
                name = rule.rule_information2().RULE_NAME().toString().replaceFirst("name : ", "");
            } else {
                name = rule.rule_information2().RULE_NAME().toString().replaceFirst("name: ", "");
            }
            if (rule.rule_information2().RULE_ID().toString().contains("id :")) {
                id = rule.rule_information2().RULE_ID().toString().replaceFirst("id : ", "");
            } else {
                id = rule.rule_information2().RULE_ID().toString().replaceFirst("id: ", "");
            }
            boolean hasList = false;
            inferenceRule.setPremisesTriple(parseTripleList(premisesTriple, inferenceRule));
            inferenceRule.setType(Type.inconsistency);
            inferenceRule.setInferenceRuleName(name);
            inferenceRule.setInferenceRuleID(Integer.parseInt(id));
            if (rule.filter() != null) {
                inferenceRule.setFilterCondition(rule.filter().condition().getText());
            }
            inferenceRules.add(inferenceRule);
        }
        for (Rule_graphContext rule : rule_graph) {
            String name;
            String id;
            List<TripleContext> premisesTriple = rule.premises().triple();
            List<TripleContext> conclusionsTriple = rule.conclusions().triple();
            InferenceRule inferenceRule = new InferenceRule();
            if (rule.rule_information3().RULE_NAME().toString().contains("name :")) {
                name = rule.rule_information3().RULE_NAME().toString().replaceFirst("name : ", "");
            } else {
                name = rule.rule_information3().RULE_NAME().toString().replaceFirst("name: ", "");
            }
            if (rule.rule_information3().RULE_ID().toString().contains("id :")) {
                id = rule.rule_information3().RULE_ID().toString().replaceFirst("id : ", "");
            } else {
                id = rule.rule_information3().RULE_ID().toString().replaceFirst("id: ", "");
            }
            inferenceRule.setPremisesTriple(parseTripleList(premisesTriple, inferenceRule));
            inferenceRule.setConclusionsTriple(parseTripleList(conclusionsTriple, inferenceRule));
            inferenceRule.setType(Type.ruleGraph);
            inferenceRule.setInferenceRuleName(name);
            inferenceRule.setInferenceRuleID(Integer.parseInt(id));
            if (rule.filter() != null) {
                inferenceRule.setFilterCondition(rule.filter().condition().getText());
            }
            inferenceRules.add(inferenceRule);
        }
        this.inferenceRules.setInferenceRules(inferenceRules);
        if (start_rule.prefix() != null) {
            List<TerminalNode> prefixes = start_rule.prefix().PNAME_NS();
            List<TerminalNode> namespaces = start_rule.prefix().URIREF();
            List<String> prefixesList = new ArrayList<>();
            List<String> namespacesList = new ArrayList<>();
            for (TerminalNode prefix : prefixes) {
                prefixesList.add(prefix.getText());
            }
            for (TerminalNode namespace : namespaces) {
                namespacesList.add(namespace.getText());
            }
            for (int i = 0; i < prefixesList.size(); i++) {
                mapPrefixes.put(prefixesList.get(i), namespacesList.get(i));
            }
            this.inferenceRules.setPrefixes(mapPrefixes);
        }
    }

    private List<Triple> parseTripleList(List<TripleContext> tripleCon, InferenceRule inferenceRule) {
        List<Triple> tripleList = new ArrayList<>();
        for (TripleContext triple : tripleCon) {
            if (inferenceRule.HasList() == false && (triple.subject().getText().charAt(0) == '$' || triple.predicate().getText().charAt(0) == '$' || triple.object().getText().charAt(0) == '$')) {
                inferenceRule.setHasList(true);
            }
            Triple trip = new Triple(triple.subject().getText(), triple.predicate().getText(), triple.object().getText());
            tripleList.add(trip);
        }
        return tripleList;
    }

    public Rules getInferenceRules() {
        return inferenceRules;
    }

    public void setInferenceRules(Rules inferenceRules) {
        this.inferenceRules = inferenceRules;
    }

}
