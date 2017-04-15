/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.view.requestBean;

import java.util.List;

/**
 *
 * @author F.Camerlengo
 */
public class InverseReasoningBean {

    private List<String> elementsId;

    private boolean againInferable;
    
    public List<String> getElementsId() {
        return elementsId;
    }

    public void setElementsId(List<String> elementsId) {
        this.elementsId = elementsId;
    }

    public boolean isAgainInferable() {
        return againInferable;
    }

    public void setAgainInferable(boolean againInferable) {
        this.againInferable = againInferable;
    }

}
