/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.view.responseBean;

import org.eclipse.rdf4j.repository.RepositoryConnection;

/**
 *
 * @author F.Camerlengo
 */
public class OntologyBean {

    private String fileName;
    
    private String filePath;
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    
   
}
