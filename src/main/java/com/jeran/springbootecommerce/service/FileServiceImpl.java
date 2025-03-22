package com.jeran.springbootecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //File name of Original
        String originalFileName = file.getOriginalFilename();

        //Generate unique filename
        String randomId = UUID.randomUUID().toString();

        //abc.jpg --> 1234 ---> 1234.jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        //check if the path exist and create
        File foler = new File(path);
        if(!foler.exists())
            foler.mkdirs();

        //Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        //returning file name
        return fileName;
    }
}
