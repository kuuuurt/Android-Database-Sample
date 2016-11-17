package com.notes.noteapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.notes.noteapp.data.NoteContract;

public class EditNoteActivity extends AppCompatActivity {

    private long notebookId;
    private long noteId;
    private boolean editMode;
    private String title;
    private String content;

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        if(editMode){
            return new Intent(this, ViewNoteActivity.class)
                    .putExtra("notebookId", notebookId)
                    .putExtra("name", title);
        }
        return super.getSupportParentActivityIntent()
                .putExtra("notebookId", notebookId)
                .putExtra("name", title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent != null){
            notebookId = intent.getLongExtra("notebookId", 1);
            if(intent.hasExtra("name")) {
                title = intent.getStringExtra("name");
                content = intent.getStringExtra("content");
                noteId = intent.getLongExtra("noteId", 1);
                editMode = true;
                setTitle("Edit Note");
            } else {
                title = "";
                content = "";
                editMode = false;
                setTitle("Add Note");
            }


        }

        final TextInputEditText txtName = (TextInputEditText) findViewById(R.id.input_note_title);
        final TextInputEditText txtContent = (TextInputEditText) findViewById(R.id.input_content);

        txtName.setText(title);
        txtContent.setText(content);



        Button btnSave = (Button)findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtName.getText().toString().trim();
                String desc = txtContent.getText().toString().trim();

                if (!(name.equals("") || desc.equals(""))) {
                    ContentValues values = new ContentValues();
                    values.put(NoteContract.NoteEntry.COLUMN_NAME, name);
                    values.put(NoteContract.NoteEntry.COLUMN_CONTENT, desc);
                    values.put(NoteContract.NoteEntry.COLUMN_NOTEBOOK_KEY, notebookId);

                    Intent intent = null;
                    if(editMode){
                        EditNoteActivity.this.getContentResolver().update(
                                NoteContract.NoteEntry.CONTENT_URI,
                                values,
                                NoteContract.NoteEntry._ID + " = ?",
                                new String[]{String.valueOf(noteId)}
                        );
                        intent = new Intent(EditNoteActivity.this, ViewNoteActivity.class);
                        intent.putExtra("notebookId", notebookId);
                        intent.putExtra("name", title);

                    } else {
                        EditNoteActivity.this.getContentResolver().insert(
                                NoteContract.NoteEntry.CONTENT_URI,
                                values
                        );
                        Toast.makeText(EditNoteActivity.this, "Note Added!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(EditNoteActivity.this, NoteActivity.class);
                        intent.putExtra("notebookId", notebookId);

                    }

                    startActivity(intent);

                } else {
                    txtName.setError("Please enter the Note Title");
                    txtContent.setError("Please enter the Note Content");
                }
            }
        });
    }


}
