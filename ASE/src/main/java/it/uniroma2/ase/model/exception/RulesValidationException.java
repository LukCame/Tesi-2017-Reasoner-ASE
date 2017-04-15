/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.exception;

/**
 *
 * @author F.Camerlengo
 */
public class RulesValidationException extends Exception {

    public RulesValidationException(String message) {
        super(message);
    }

    public RulesValidationException(PrefixesValidationException cause) {
        super("error in the validation of rules",cause);
    }

    public RulesValidationException(RuleInformationValidationException cause) {
        super("error in the validation of rules",cause);
    }

    public RulesValidationException(RulePremisesElementsValidationException cause) {
        super("error in the validation of rules",cause);
    }

    public RulesValidationException(RuleFilterConditionValidationException cause) {
        super("error in the validation of rules",cause);
    }

    public RulesValidationException(RuleConclusionsTripleValidationException cause) {
        super("error in the validation of rules",cause);
    }

    public RulesValidationException(RuleListValidationException cause) {
        super("error in the validation of rules",cause);
    }

    public RulesValidationException(BnodeInferenceRulesValidationException cause) {
        super("error in the validation of inference rules",cause);
    }

    public RulesValidationException(BnodeRuleGraphValidationException cause) {
        super("error in the validation of rule graphs",cause);
    }
    
    
    
}
