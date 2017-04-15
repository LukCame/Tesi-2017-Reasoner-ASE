/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.service;

import it.uniroma2.ase.model.exception.UploadFileException;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author F.Camerlengo
 */
@Service
public class UploadServiceImpl implements IUploadService {

    public String processUpload(MultipartFile file, String directoryPath) throws UploadFileException {
        String name = null;
        try {
            FileCopyUtils.copy(file.getBytes(), new File(directoryPath + "/" + file.getOriginalFilename()));
            name = file.getOriginalFilename();
        } catch (IOException exception) {
            throw new UploadFileException(exception);
        } 
        return name;
    }
}
