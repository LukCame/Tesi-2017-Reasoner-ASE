/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.exception;

import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
/**
 *
 * @author F.Camerlengo
 */
public class LoadOntologyUnsupportedRDFFormatException extends LoadOntologyException{

    public LoadOntologyUnsupportedRDFFormatException(String message) {
        super(message);
    }

    public LoadOntologyUnsupportedRDFFormatException(UnsupportedRDFormatException  cause) {
        super(cause.getMessage());
    }
    
}
