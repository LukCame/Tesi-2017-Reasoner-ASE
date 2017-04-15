/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.exception;

import org.antlr.v4.runtime.RecognitionException;

/**
 *
 * @author F.Camerlengo
 */
public class InferenceRulesRecognitonException extends InferenceRulesParsingException{

    public InferenceRulesRecognitonException(String message) {
        super(message);
    }

    public InferenceRulesRecognitonException(RecognitionException cause) {
        super(cause.getMessage());
    }
    
}
