/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.exception;

import java.io.IOException;

/**
 *
 * @author F.Camerlengo
 */
public class LoadOntologyFileIoException extends LoadOntologyException{

    public LoadOntologyFileIoException(String message) {
        super(message);
    }

    public LoadOntologyFileIoException(IOException cause){
        super(cause.getMessage());
    }
    
}
