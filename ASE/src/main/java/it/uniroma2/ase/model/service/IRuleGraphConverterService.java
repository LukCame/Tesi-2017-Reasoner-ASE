/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.domain.Rules;

/**
 *
 * @author F.Camerlengo
 */
public interface IRuleGraphConverterService {

    public Rules convertOnRuleGraphFromRule(Rules rules);
   
}
