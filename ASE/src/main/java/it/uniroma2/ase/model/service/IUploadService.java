/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.model.exception.UploadFileException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author F.Camerlengo
 */
public interface IUploadService {

    public String processUpload(MultipartFile file,String path) throws UploadFileException;
}
