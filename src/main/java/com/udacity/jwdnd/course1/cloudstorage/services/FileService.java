package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public boolean isFilenameAvailable(String filename, Integer userId) {
        List<File> userFiles = fileMapper.getFilesByUser(userId);
        for (File file : userFiles) {
            if (file.getFilename().equalsIgnoreCase(filename)) {
                return false;
            }
        }
        return true;
    }

    public List<File> getAllFilesByUserId(Integer userId) {
        return fileMapper.getFilesByUser(userId);
    }

    public int saveFile(MultipartFile file, Integer userId) throws IOException {
        File newFile = new File();
        newFile.setFilename(file.getOriginalFilename());
        newFile.setContentType(file.getContentType());
        newFile.setFileSize(String.valueOf(file.getSize()));
        newFile.setUserId(userId);
        newFile.setFileData(file.getBytes());
        return fileMapper.insert(newFile);
    }

    public void deleteFile(Integer fileId) {
        fileMapper.delete(fileId);
    }

    public Resource loadFileAsResource(Integer fileId) {
        File file = fileMapper.getFile(fileId);
        return new ByteArrayResource(file.getFileData());
    }
}
