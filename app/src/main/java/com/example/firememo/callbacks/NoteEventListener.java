package com.example.firememo.callbacks;

import com.example.firememo.model.Note;

public interface NoteEventListener {
    void onNoteClick(Note note);

    void onNoteLongClick(Note note);
}
