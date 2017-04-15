/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.QueriesResults;
import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.exception.RunTimeReasoningException;
import it.uniroma2.ase.model.reasoningHandler.ExecutionQuery;
import it.uniroma2.ase.model.reasoningHandler.Reasoner;
import java.util.List;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.stereotype.Service;

/**
 *
 * @author F.Camerlengo
 */
@Service
public class ReasoningServiceImpl implements IReasoningService{

    public QueriesResults executeReasoning(RepositoryConnection repository,Rules rules, Integer numberOfExecution, List<Integer> rulesId) throws RunTimeReasoningException  {
        Reasoner reasoner=new Reasoner();
        try{
            ExecutionQuery ex=new ExecutionQuery();
            List<Statement> ontologyTriple = ex.getOntologyTriples(repository);
            QueriesResults queriesResults = reasoner.reasoning(repository,rules, numberOfExecution, rulesId);
            queriesResults.setOntologyTriple(ontologyTriple);
            return queriesResults;
        }catch(Exception e){
            throw new RunTimeReasoningException(e);
        } 
    }
    
}
