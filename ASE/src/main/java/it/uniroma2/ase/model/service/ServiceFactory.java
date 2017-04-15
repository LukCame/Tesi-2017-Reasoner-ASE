/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author F.Camerlengo
 */
@Component
@Scope("singleton")
public class ServiceFactory implements IServiceFactory {
    
    @Autowired
    private IUploadService uploadService;
    
    @Autowired
    private IOntologyService ontologyService;
    
    @Autowired
    private IInferenceRulesService inferenceRulesService;
    
    @Autowired
    private IRuleGraphConverterService ruleGraphConverterService;
    
    @Autowired
    private IValidationRulesService validationRulesService;
    
    @Autowired
    private IRewriteRulesService rewriteRulesService;
    
    @Autowired
    private IReasoningService reasoningService;
    
    @Autowired
    private IInverseReasoningService inverseReasoningService;
   
    public IUploadService getUploadService() {
        return uploadService;
    }

    public IOntologyService getOntologyService(){
        return ontologyService;
    }
    
    public IInferenceRulesService getInferenceRulesService(){
        return inferenceRulesService;
    }
    
    public IRuleGraphConverterService getRuleGraphConverterService(){
        return ruleGraphConverterService;
    }

    public IValidationRulesService getValidationRulesService() {
        return validationRulesService;
    }
    
    public IRewriteRulesService getRewriteRulesService(){
        return rewriteRulesService;
    }
    
    public IReasoningService getReasoningService(){
        return reasoningService;
    }
    
    public IInverseReasoningService getInverseReasoningService(){
        return inverseReasoningService;
    }
    
}
