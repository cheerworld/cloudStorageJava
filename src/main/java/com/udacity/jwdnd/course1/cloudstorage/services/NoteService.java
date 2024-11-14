package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    public List<Note> getAllNotesByUserId(Integer userId) {
        return noteMapper.getNotesByUser(userId);
    }

    public void saveOrUpdate(Note note) {
        if (note.getNoteId() == null) {
            noteMapper.insert(note);
        } else {
            noteMapper.update(note);
        }
    }

    public void delete(Integer noteId) {
        noteMapper.delete(noteId);
    }
}
