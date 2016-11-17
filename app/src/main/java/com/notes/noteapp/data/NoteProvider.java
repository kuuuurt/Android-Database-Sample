package com.notes.noteapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by qwerasdf on 8/1/16.
 */
public class NoteProvider extends ContentProvider{
    NoteDbHelper mDbHelper;
    static final int NOTEBOOK = 100;
    static final int NOTE = 200;
    static final int NOTE_WITH_TITLE = 201;
    static final int NOTE_WITH_NOTEBOOK = 202;

    static UriMatcher uriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NoteContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, NoteContract.PATH_NOTEBOOKS, NOTEBOOK);
        matcher.addURI(authority, NoteContract.PATH_NOTES, NOTE);
        matcher.addURI(authority, NoteContract.PATH_NOTES + "/#", NOTE_WITH_NOTEBOOK);
        matcher.addURI(authority, NoteContract.PATH_NOTES + "/*", NOTE_WITH_TITLE);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new NoteDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor data = null;

        return data;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }
}