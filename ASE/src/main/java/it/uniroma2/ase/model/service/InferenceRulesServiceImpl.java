/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.exception.InferenceRulesFileIoException;
import it.uniroma2.ase.model.exception.InferenceRulesParseFileException;
import it.uniroma2.ase.model.exception.InferenceRulesParsingException;
import it.uniroma2.ase.model.exception.InferenceRulesRecognitonException;
import it.uniroma2.ase.model.inferenceRuleHandler.Parser;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;




/**
 *
 * @author F.Camerlengo
 */
@Service
public class InferenceRulesServiceImpl implements IInferenceRulesService{
    
    public Rules parseInferenceRulesFile(String filePath) throws InferenceRulesParsingException {
        File inferenceRulesFile=new File(filePath);
        Rules rules=null;
        Parser parser=new Parser(); 
        try {
            rules=parser.parsingInferenceRulesFile(inferenceRulesFile);
        }catch(InferenceRulesParsingException ex){
            if(ex instanceof InferenceRulesFileIoException){
                InferenceRulesFileIoException exception=(InferenceRulesFileIoException) ex;
                throw new InferenceRulesParsingException(exception);
            }
            if(ex instanceof InferenceRulesParseFileException){
                InferenceRulesParseFileException exception=(InferenceRulesParseFileException) ex;
                throw new InferenceRulesParsingException(exception);
            }
            if(ex instanceof InferenceRulesRecognitonException){
                InferenceRulesRecognitonException exception=(InferenceRulesRecognitonException) ex;
                throw new InferenceRulesParsingException(exception);
            }
        }
        return rules;
    }

    
    
}
