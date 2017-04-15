/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.exception.RulesValidationException;

/**
 *
 * @author F.Camerlengo
 */
public interface IValidationRulesService {

    public void validate(Rules rules) throws RulesValidationException;
}
