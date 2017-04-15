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
public class RunTimeReasoningException extends Exception{

    public RunTimeReasoningException(String message) {
        super(message);
    }
    
    public RunTimeReasoningException(Exception cause) {
        super(cause.getMessage(),cause);
    }
    
}
