/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.exception.BnodeInferenceRulesValidationException;
import it.uniroma2.ase.model.exception.BnodeRuleGraphValidationException;
import it.uniroma2.ase.model.exception.PrefixesValidationException;
import it.uniroma2.ase.model.exception.RuleConclusionsTripleValidationException;
import it.uniroma2.ase.model.exception.RuleFilterConditionValidationException;
import it.uniroma2.ase.model.exception.RuleInformationValidationException;
import it.uniroma2.ase.model.exception.RuleListValidationException;
import it.uniroma2.ase.model.exception.RulePremisesElementsValidationException;
import it.uniroma2.ase.model.exception.RulesValidationException;
import it.uniroma2.ase.model.inferenceRuleHandler.Validator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author F.Camerlengo
 */
@Service
public class ValidationRulesServiceImpl implements IValidationRulesService{

    
    public void validate(Rules rules) throws RulesValidationException {
        Validator validator= new Validator();
        try {
            validator.validate(rules);
        } catch (RulesValidationException ex) {
            if(ex instanceof PrefixesValidationException){
                PrefixesValidationException exception=(PrefixesValidationException) ex;
                throw  new RulesValidationException(exception);
            }
            if(ex instanceof RuleInformationValidationException){
                RuleInformationValidationException exception=(RuleInformationValidationException) ex;
                throw  new RulesValidationException(exception);
            }
            if(ex instanceof RulePremisesElementsValidationException){
                RulePremisesElementsValidationException exception=(RulePremisesElementsValidationException) ex;
                throw  new RulesValidationException(exception);
            }
            if(ex instanceof RuleFilterConditionValidationException){
                RuleFilterConditionValidationException exception=(RuleFilterConditionValidationException) ex;
                throw  new RulesValidationException(exception);
            }
            if(ex instanceof RuleConclusionsTripleValidationException){
                RuleConclusionsTripleValidationException exception=(RuleConclusionsTripleValidationException) ex;
                throw  new RulesValidationException(exception);
            }
            if(ex instanceof RuleListValidationException){
                RuleListValidationException exception=(RuleListValidationException) ex;
                throw  new RulesValidationException(exception);
            }
            if(ex instanceof BnodeInferenceRulesValidationException){
                BnodeInferenceRulesValidationException exception=(BnodeInferenceRulesValidationException) ex;
                throw  new RulesValidationException(exception);
            }
            if(ex instanceof BnodeRuleGraphValidationException){
                BnodeRuleGraphValidationException exception=(BnodeRuleGraphValidationException) ex;
                throw  new RulesValidationException(exception);
            }
        }
    }

    
    
}
