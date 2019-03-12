package com.example.firememo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.firememo.database.NoteDao;
import com.example.firememo.database.NotesDB;
import com.example.firememo.model.Note;

import java.util.Date;

public class EditNote extends AppCompatActivity {
    private EditText inputNote;
    private NoteDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_KEY="note_id";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        inputNote=findViewById(R.id.input_note);
        dao=NotesDB.getInstance(this).noteDao();
        if (getIntent().getExtras()!=null){
            int id=getIntent().getExtras().getInt(NOTE_EXTRA_KEY,0);
            temp=dao.getNoteById(id);
            inputNote.setText(temp.getNoteText());
        }else temp=new Note();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.save_note)
            onSaveNote();
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
             finish();
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        String text=inputNote.getText().toString();
        if (!text.isEmpty()){
            long date=new Date().getTime();
            temp.setNoteDate(date);
            temp.setNoteText(text);
            //Note note=new Note(text,date);

            if (temp.getId()==-1)
                dao.insertNote(temp);
            else dao.updateNote(temp);
            finish();
        }


    }


}
