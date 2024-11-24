package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.constants.Messages;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private UserService userService;

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile file, Principal principal, Model model, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Integer userId = userService.getUser(username).getUserId();

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", Messages.UPLOAD_FILE_EMPTY_ERROR);
            return "redirect:/home/result";
        }

        try {
            if (!fileService.isFilenameAvailable(file.getOriginalFilename(), userId)) {
                redirectAttributes.addFlashAttribute("errorMessage", Messages.UPLOAD_SAME_FILE_ERROR);
                return "redirect:/home/result";
            }
            fileService.saveFile(file, userId);
            redirectAttributes.addFlashAttribute("successMessage", Messages.UPLOAD_FILE_SUCCESS);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", Messages.UPLOAD_FILE_ERROR);
        }
        return "redirect:/home/result";
    }

    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Resource> viewFile(@PathVariable("id") Integer fileId) {
        Resource file = fileService.loadFileAsResource(fileId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") Integer fileId, RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(fileId);
            redirectAttributes.addFlashAttribute("successMessage", Messages.DELETE_FILE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", Messages.DELETE_FILE_ERROR);
        }
        return "redirect:/home/result";
    }
}
