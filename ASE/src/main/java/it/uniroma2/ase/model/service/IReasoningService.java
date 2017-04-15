/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.QueriesResults;
import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.exception.RunTimeReasoningException;
import java.util.List;
import org.eclipse.rdf4j.repository.RepositoryConnection;

/**
 *
 * @author F.Camerlengo
 */
public interface IReasoningService {

    public QueriesResults executeReasoning(RepositoryConnection repository,Rules rules,Integer numberOfExecution,List<Integer> rulesId ) throws RunTimeReasoningException;
    
}
