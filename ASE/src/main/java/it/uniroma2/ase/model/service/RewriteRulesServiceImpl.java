/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.inferenceRuleHandler.RewriterOfInferenceRuleOrRuleGraph;
import org.springframework.stereotype.Service;

/**
 *
 * @author F.Camerlengo
 */
@Service
public class RewriteRulesServiceImpl implements IRewriteRulesService{

    public Rules rewriteRules(Rules rules) {
        RewriterOfInferenceRuleOrRuleGraph rewriter=new RewriterOfInferenceRuleOrRuleGraph();
        rewriter.rewriteInferenceRuleOrRuleGraphIfContainsList(rules);
        return rules;
    }
    
}
