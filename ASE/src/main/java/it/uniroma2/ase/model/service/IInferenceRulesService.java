/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.exception.InferenceRulesParsingException;

/**
 *
 * @author F.Camerlengo
 */
public interface IInferenceRulesService {
 
    public Rules parseInferenceRulesFile(String filePath) throws InferenceRulesParsingException;
}
