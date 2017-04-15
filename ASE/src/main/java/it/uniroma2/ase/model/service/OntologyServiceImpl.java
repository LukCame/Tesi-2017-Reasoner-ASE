/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;


import it.uniroma2.ase.model.exception.LoadOntologyException;
import it.uniroma2.ase.model.exception.LoadOntologyFileIoException;
import it.uniroma2.ase.model.exception.LoadOntologyRDFParseException;
import it.uniroma2.ase.model.exception.LoadOntologyRepositoryException;
import it.uniroma2.ase.model.exception.LoadOntologyUnsupportedRDFFormatException;
import it.uniroma2.ase.model.utility.OntologyUtility;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.stereotype.Service;

/**
 *
 * @author F.Camerlengo
 */
@Service
public class OntologyServiceImpl implements IOntologyService {

    public RepositoryConnection loadOntology(String filePath) throws LoadOntologyException{
        RepositoryConnection repository=null;
        try{
            repository = OntologyUtility.loadModel(filePath);
        }catch(LoadOntologyException ex){
            if(ex instanceof LoadOntologyUnsupportedRDFFormatException){
                LoadOntologyUnsupportedRDFFormatException exception=(LoadOntologyUnsupportedRDFFormatException) ex;
                throw new LoadOntologyException(exception);
            }
            if(ex instanceof LoadOntologyRDFParseException){
               LoadOntologyRDFParseException exception=(LoadOntologyRDFParseException) ex;
               throw new LoadOntologyException(exception);
            }
            if(ex instanceof LoadOntologyRepositoryException){
                LoadOntologyRepositoryException exception=(LoadOntologyRepositoryException) ex;
                throw new LoadOntologyException(exception);
            }
            if(ex instanceof LoadOntologyFileIoException){
                LoadOntologyFileIoException exception=(LoadOntologyFileIoException) ex;
                throw new LoadOntologyException(exception);
            }
        }
        return repository;
    }
}
