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
public class LoadOntologyException extends Exception{

    public LoadOntologyException(String message) {
        super(message);
    }

    public LoadOntologyException(LoadOntologyFileIoException cause) {
        super("error on loading ontology",cause);
    }

    public LoadOntologyException(LoadOntologyRDFParseException cause) {
        super("error on loading ontology",cause);
    }

    public LoadOntologyException(LoadOntologyRepositoryException cause) {
        super("error on loading ontology",cause);
    }

    public LoadOntologyException(LoadOntologyUnsupportedRDFFormatException cause) {
        super("error on loading ontology",cause);
    }
    
    
    
    
    
    
    
}
