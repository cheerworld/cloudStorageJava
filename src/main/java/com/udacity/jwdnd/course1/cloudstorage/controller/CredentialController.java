package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constants.Messages;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    @Autowired
    private UserService userService;

    @Autowired
    private CredentialService credentialService;

    @PostMapping("/save")
    public String saveCredential(@ModelAttribute Credential credential, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Integer userId = userService.getUser(username).getUserId();

        try {
            credentialService.saveCredential(credential, userId);
            redirectAttributes.addFlashAttribute("successMessage", Messages.SAVE_CREDENTIAL_SUCCESS_MSG);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", Messages.SAVE_CREDENTIAL_ERROR_MSG);
        }
        return "redirect:/home/result";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredential(@PathVariable("id") Integer credentialId, RedirectAttributes redirectAttributes) {
        try {
            credentialService.deleteCredential(credentialId);
            redirectAttributes.addFlashAttribute("successMessage", Messages.DELETE_CREDENTIAL_SUCCESS_MSG);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", Messages.DELETE_CREDENTIAL_ERROR_MSG);
        }
        return "redirect:/home/result";
    }

    @GetMapping("/decrypt/{id}")
    public ResponseEntity<String> getDecryptedPassword(@PathVariable Integer id) {
        Credential credential = credentialService.getCredential(id);
        String decryptedPassword = credentialService.decryptPassword(credential);
        return ResponseEntity.ok(decryptedPassword);
    }
}
