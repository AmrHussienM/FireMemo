package com.example.firememo.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.firememo.model.Note;

@Database(entities = Note.class,version = 1,exportSchema = false)
public  abstract class NotesDB extends RoomDatabase {

    public abstract NoteDao noteDao();
    public static NotesDB instance;

    public static NotesDB getInstance(Context context){
        if (instance==null)
            instance=Room.databaseBuilder(context,NotesDB.class,"notesDB").allowMainThreadQueries().build();
        return instance;
    }


}
