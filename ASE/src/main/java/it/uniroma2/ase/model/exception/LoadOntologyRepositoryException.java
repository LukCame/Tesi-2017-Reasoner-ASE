/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.exception;

import org.eclipse.rdf4j.repository.RepositoryException;

/**
 *
 * @author F.Camerlengo
 */
public class LoadOntologyRepositoryException  extends LoadOntologyException{

    public LoadOntologyRepositoryException(String message) {
        super(message);
    }

    public LoadOntologyRepositoryException(RepositoryException cause) {
        super(cause.getMessage());
    }
    
}
