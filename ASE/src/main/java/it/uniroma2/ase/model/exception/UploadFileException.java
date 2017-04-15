/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.exception;

import java.io.IOException;

/**
 *
 * @author F.Camerlengo
 */
public class UploadFileException extends Exception{

    public UploadFileException(String message) {
        super(message);
    }

    public UploadFileException(IOException cause) {
        super("error on file upload",cause);
    }
    
}
