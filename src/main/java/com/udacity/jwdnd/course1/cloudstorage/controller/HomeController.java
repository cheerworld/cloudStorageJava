package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @GetMapping
    public String homePage(Model model, Principal principal) {
        String username = principal.getName();

        logger.info("Username: {}", username);
        logger.info("Principal: {}", principal.toString());
        try{
            Integer userId = userService.getUser(username).getUserId();
            List<File> files = fileService.getAllFilesByUserId(userId);
            List<Note> notes = noteService.getAllNotesByUserId(userId);
            List<Credential> credentials = credentialService.getAllCredentialsByUserId(userId);

            model.addAttribute("files", files);
            model.addAttribute("notes", notes);
            model.addAttribute("credentials", credentials);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        return "home";
    }

    @GetMapping("/result")
    public String resultPage(Model model) {
        return "result";
    }

}

