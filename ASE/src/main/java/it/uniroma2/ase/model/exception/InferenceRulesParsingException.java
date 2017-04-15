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
public class InferenceRulesParsingException extends Exception{

    public InferenceRulesParsingException(String message) {
        super(message);
    }

    public InferenceRulesParsingException(InferenceRulesFileIoException cause) {
        super("error on parsing inference rules",cause);
    }

    public InferenceRulesParsingException(InferenceRulesParseFileException cause) {
        super("error on parsing inference rules",cause);
    }

    public InferenceRulesParsingException(InferenceRulesRecognitonException cause) {
        super("error on parsing inference rules",cause);
    }

    public InferenceRulesParsingException(InferenceRuleFileFormatException cause) {
        super("error on parsing inference rule",cause);
    }
    
    
    
    
   

    
    
}
