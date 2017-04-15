/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.converterHandler.RuleGraphConverter;
import org.springframework.stereotype.Service;

/**
 *
 * @author F.Camerlengo
 */
@Service
public class RuleGraphConverterService implements IRuleGraphConverterService{

    public Rules convertOnRuleGraphFromRule(Rules rules) {
        RuleGraphConverter ruleGrCon=new RuleGraphConverter();
        ruleGrCon.addGraphRulesToRules(rules);
        return rules;
    }

    
}
