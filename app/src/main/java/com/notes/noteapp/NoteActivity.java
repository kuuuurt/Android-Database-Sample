package com.notes.noteapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.notes.noteapp.data.NoteContract;

public class NoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static int NOTE_LOADER = 0;
    private NoteAdapter mNoteAdapter;
    private long notebookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this, EditNoteActivity.class);
                intent.putExtra("notebookId", notebookId);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Intent intent = getIntent();
        if(intent != null){
            notebookId = intent.getLongExtra("notebookId", 1);
        }

        getSupportLoaderManager().initLoader(NOTE_LOADER, null, this);
        mNoteAdapter = new NoteAdapter(this, null, true);
        ListView listNote = (ListView)findViewById(R.id.list_view_note);
        listNote.setAdapter(mNoteAdapter);
        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor data = mNoteAdapter.getCursor();
                Intent noteIntent = new Intent(NoteActivity.this, ViewNoteActivity.class);
                noteIntent.putExtra("notebookId", notebookId);
                noteIntent.putExtra("name", data.getString(data.getColumnIndex(
                        NoteContract.NoteEntry.COLUMN_NAME)));
                startActivity(noteIntent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                NoteContract.NoteEntry.buildNoteUriWithNotebookId(notebookId),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mNoteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNoteAdapter.swapCursor(null);
    }
}
