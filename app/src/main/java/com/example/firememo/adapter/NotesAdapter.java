package com.example.firememo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firememo.R;
import com.example.firememo.callbacks.NoteEventListener;
import com.example.firememo.model.Note;
import com.example.firememo.utils.NoteUtils;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {
    private List<Note> notes;
    private Context context;
    private NoteEventListener listener;

    public NotesAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }



    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.note_layout,parent,false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        final Note note=getNote(position);
        if (note != null){
            holder.noteText.setText(note.getNoteText());
            holder.noteDate.setText(NoteUtils.dateFromLong(note.getNoteDate()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNoteClick(note);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onNoteLongClick(note);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (notes==null){return 0;}
        return notes.size();
    }
    private Note getNote(int position){
        return notes.get(position);
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        TextView noteText, noteDate;
        //View itemView;
        public NoteHolder(View itemView) {
            super(itemView);
            //this.itemView=itemView;
            noteText=itemView.findViewById(R.id.note_text);
            noteDate=itemView.findViewById(R.id.note_date);
        }
    }

    public void setListener(NoteEventListener listener) {
        this.listener = listener;
    }
}
