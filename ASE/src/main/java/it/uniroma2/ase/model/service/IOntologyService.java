/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.model.exception.LoadOntologyException;
import org.eclipse.rdf4j.repository.RepositoryConnection;

/**
 *
 * @author F.Camerlengo
 */
public interface IOntologyService {
    
    public RepositoryConnection loadOntology(String filePath) throws LoadOntologyException;
}
