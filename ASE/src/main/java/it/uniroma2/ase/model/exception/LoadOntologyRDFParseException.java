/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.exception;

import org.eclipse.rdf4j.rio.RDFParseException;

/**
 *
 * @author F.Camerlengo
 */
public class LoadOntologyRDFParseException extends LoadOntologyException{

    public LoadOntologyRDFParseException(String message) {
        super(message);
    }

    public LoadOntologyRDFParseException(RDFParseException cause) {
        super(cause.getMessage());
    }
    
}
