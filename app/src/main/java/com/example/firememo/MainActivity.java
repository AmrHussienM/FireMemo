package com.example.firememo;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firememo.adapter.NotesAdapter;
import com.example.firememo.callbacks.NoteEventListener;
import com.example.firememo.database.NoteDao;
import com.example.firememo.database.NotesDB;
import com.example.firememo.model.Note;
import com.example.firememo.utils.NoteUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.firememo.EditNote.NOTE_EXTRA_KEY;

public class MainActivity extends AppCompatActivity implements NoteEventListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private List<Note> notes;
    private NotesAdapter adapter;
    private NoteDao dao;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.notesrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 09-Mar-19 add new note
                onAddNewNote();
            }
        });

        dao=NotesDB.getInstance(this).noteDao();



    }

    private void loadNotes(){
        this.notes=new ArrayList<>();
        List<Note>list=dao.getNotes();
        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this.notes, this);
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);

    }

    private void onAddNewNote(){
        // TODO: 10-Mar-19 Start EditNote
        startActivity(new Intent(this,EditNote.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();

    }

    @Override
    public void onNoteClick(Note note) {
        //Log.d(TAG, "onNoteClick: "+note.toString());
        Intent edit=new Intent(this,EditNote.class);
        edit.putExtra(NOTE_EXTRA_KEY,note.getId());
        startActivity(edit);
    }

    @Override
    public void onNoteLongClick(final Note note) {
        //Log.d(TAG, "onNoteLongClick: "+note.getId());
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao.deleteNote(note);
                        loadNotes();
                    }
                })
                .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent share=new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT,
                                note.getNoteText()+"\n Create on :"+
                                        NoteUtils.dateFromLong(note.getNoteDate())+
                                        " By :"+getString(R.string.app_name)
                                );
                        startActivity(share);


                    }
                })
                .create()
                .show();
    }
}
