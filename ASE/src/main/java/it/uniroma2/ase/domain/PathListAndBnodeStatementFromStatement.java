/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.domain;

import java.util.List;
import org.eclipse.rdf4j.model.Statement;

/**
 *
 * @author F.Camerlengo
 */
public class PathListAndBnodeStatementFromStatement {
    
    private Statement statement;
    
    private List<Path> pathList;
    
    private Statement bnodeStatement;

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public List<Path> getPathList() {
        return pathList;
    }

    public void setPathList(List<Path> pathList) {
        this.pathList = pathList;
    }

    public Statement getBnodeStatement() {
        return bnodeStatement;
    }

    public void setBnodeStatement(Statement bnodeStatement) {
        this.bnodeStatement = bnodeStatement;
    }
    
}
