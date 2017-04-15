/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

/**
 *
 * @author F.Camerlengo
 */
public interface IServiceFactory {

    IUploadService getUploadService();
    
    IOntologyService getOntologyService();
    
    IInferenceRulesService getInferenceRulesService();
    
    IRuleGraphConverterService getRuleGraphConverterService();
    
    IValidationRulesService getValidationRulesService();
    
    IRewriteRulesService getRewriteRulesService();
    
    IReasoningService getReasoningService();
    
    IInverseReasoningService getInverseReasoningService();
    
}
