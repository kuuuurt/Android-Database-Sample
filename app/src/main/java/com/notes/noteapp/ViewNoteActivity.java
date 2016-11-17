package com.notes.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewNoteActivity extends AppCompatActivity {

    private long notebookId;
    private long noteId;
    private String noteName;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent != null){
            notebookId = intent.getLongExtra("notebookId", 1);
            noteName = intent.getStringExtra("name");
        }

        TextView txtTitle = (TextView)findViewById(R.id.text_note_title);
        TextView txtContent = (TextView)findViewById(R.id.text_note_content);


        txtContent.setText(content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_edit){
            Intent intent = new Intent(this, EditNoteActivity.class);
            intent.putExtra("notebookId", notebookId);
            intent.putExtra("noteId", noteId);
            intent.putExtra("name", noteName);
            intent.putExtra("content", content);
            startActivity(intent);
        } else if(id == R.id.action_delete){


            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra("notebookId", notebookId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
