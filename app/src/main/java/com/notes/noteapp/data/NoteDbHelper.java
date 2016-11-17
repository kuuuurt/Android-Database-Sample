package com.notes.noteapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qwerasdf on 7/31/16.
 */
public class NoteDbHelper extends SQLiteOpenHelper{

    public static String DATABASE_NAME = "notes.db";
    private static int DATABASE_VERSION = 1;

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_NOTEBOOKS_TABLE = "CREATE TABLE " +
                NoteContract.NotebookEntry.TABLE_NAME + " (" +
                NoteContract.NotebookEntry._ID + " INTEGER PRIMARY KEY, " +
                NoteContract.NotebookEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                NoteContract.NotebookEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL" + ");";

        final String SQL_CREATE_NOTES_TABLE = "CREATE TABLE " +
                NoteContract.NoteEntry.TABLE_NAME + " (" +
                NoteContract.NoteEntry._ID + " INTEGER PRIMARY KEY, " +
                NoteContract.NoteEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                NoteContract.NoteEntry.COLUMN_NOTEBOOK_KEY + " INTEGER NOT NULL, " +
                NoteContract.NoteEntry.COLUMN_CONTENT + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_NOTEBOOKS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NoteContract.NotebookEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NoteContract.NoteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
