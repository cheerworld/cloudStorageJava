package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    @PostMapping("/save")
    public String saveNote(@ModelAttribute Note note, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            Integer userId = userService.getUser(username).getUserId();
            note.setUserId(userId);
            noteService.saveOrUpdate(note);
            redirectAttributes.addFlashAttribute("successMessage", "Note saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while saving the note. Please try again.");
        }
        return "redirect:/home/result";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable("id") Integer noteId, RedirectAttributes redirectAttributes) {
        try {
            noteService.delete(noteId);
            redirectAttributes.addFlashAttribute("successMessage", "Note deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the note. Please try again.");
        }
        return "redirect:/home/result";
    }
}
