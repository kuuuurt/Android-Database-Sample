package com.notes.noteapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.notes.noteapp.data.NoteContract;

public class NotebookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>   {

    private static final int NOTEBOOK_LOADER = 0;
    private NotebookAdapter mNotebookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDialog("Add", -1);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportLoaderManager().initLoader(NOTEBOOK_LOADER, null, this);
        mNotebookAdapter = new NotebookAdapter(this, null, true);
        ListView listView = (ListView)findViewById(R.id.list_view_notebook);
        listView.setAdapter(mNotebookAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor data = mNotebookAdapter.getCursor();
                Intent noteIntent = new Intent(NotebookActivity.this, NoteActivity.class);
                noteIntent.putExtra("notebookId", data.getLong(data.getColumnIndex(
                        NoteContract.NotebookEntry._ID)));
                startActivity(noteIntent);
            }
        });

        registerForContextMenu(listView);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notebook, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();
        if(id == R.id.action_edit){
            showAddEditDialog("Edit", info.id);
        } else if (id == R.id.action_delete){
            showDeleteDialog(info.id);
        }
        return super.onContextItemSelected(item);
    }

    public void showDeleteDialog(final long id){
        AlertDialog.Builder builder = new AlertDialog.Builder(NotebookActivity.this);
        LayoutInflater inflater = NotebookActivity.this.getLayoutInflater();

        builder.setTitle("Are you sure you want to delete this notebook?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NotebookActivity.this.getContentResolver().delete(
                                NoteContract.NotebookEntry.CONTENT_URI,
                                NoteContract.NotebookEntry._ID + " = ?",
                                new String[]{String.valueOf(id)}
                        );
                    }
                })
                .setNegativeButton("No", null)
                .create().show();
    }

    public void showAddEditDialog(final String mode, final long id){
        AlertDialog.Builder builder = new AlertDialog.Builder(NotebookActivity.this);
        LayoutInflater inflater = NotebookActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_new_notebook, null);

        builder.setTitle("New Notebook");
        final AlertDialog dialog = builder.setView(dialogView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextInputEditText txtName = (TextInputEditText) dialogView.findViewById(R.id.input_notebook_name);
                        TextInputEditText txtDesc = (TextInputEditText) dialogView.findViewById(R.id.input_description);

                        String name = txtName.getText().toString().trim();
                        String desc = txtDesc.getText().toString().trim();

                        if (!(name.equals("") || desc.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put(NoteContract.NotebookEntry.COLUMN_NAME, name);
                            values.put(NoteContract.NotebookEntry.COLUMN_DESCRIPTION, desc);

                            if(mode.equals("Add")) {
                                NotebookActivity.this.getContentResolver().insert(
                                        NoteContract.NotebookEntry.CONTENT_URI,
                                        values
                                );
                                Snackbar.make(findViewById(android.R.id.content), "Notebook Added!", Snackbar.LENGTH_SHORT).show();
                            } else if(mode.equals("Edit")){
                                NotebookActivity.this.getContentResolver().update(
                                        NoteContract.NotebookEntry.CONTENT_URI,
                                        values,
                                        NoteContract.NotebookEntry._ID + " = ?",
                                        new String[]{String.valueOf(id)}
                                );
                                Snackbar.make(findViewById(android.R.id.content), "Notebook Edited!", Snackbar.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        } else {
                            if(name.equals(""))
                                txtName.setError("Please enter a notebook name!");
                            if(desc.equals(""))
                                txtDesc.setError("Please enter a notebook description!");
                        }

                    }
                });
            }
        });
        dialog.show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                NoteContract.NotebookEntry.CONTENT_URI,
                null,null,null,null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mNotebookAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNotebookAdapter.swapCursor(null);
    }
}
