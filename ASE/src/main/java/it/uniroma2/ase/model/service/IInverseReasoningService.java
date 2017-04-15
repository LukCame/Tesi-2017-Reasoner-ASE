/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.QueriesResults;
import java.util.List;

/**
 *
 * @author F.Camerlengo
 */
public interface IInverseReasoningService {

    public QueriesResults executeInverseReasoningProcess(List<Integer> statementId,List<Integer> graphsId,List<Integer> ontologyId,QueriesResults results,boolean kind);
    
    
    
}
