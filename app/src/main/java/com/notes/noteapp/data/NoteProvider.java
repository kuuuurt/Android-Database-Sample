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

    private Cursor getNoteByTitle(Uri uri, String[] projection, String sortOrder) {
        return mDbHelper.getReadableDatabase().query(
                NoteContract.NoteEntry.TABLE_NAME,
                projection,
                NoteContract.NoteEntry.COLUMN_NAME + " = ?",
                new String[]{NoteContract.NoteEntry.getTitleFromUri(uri)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getNotesByNotebook(Uri uri, String[] projection, String sortOrder) {
        return mDbHelper.getReadableDatabase().query(
                NoteContract.NoteEntry.TABLE_NAME,
                projection,
                NoteContract.NoteEntry.COLUMN_NOTEBOOK_KEY + " = ?",
                new String[]{String.valueOf(NoteContract.NoteEntry.getIDFromUri(uri))},
                null,
                null,
                sortOrder
        );
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
        switch (uriMatcher().match(uri)){
            case NOTEBOOK:
                data = mDbHelper.getReadableDatabase().query(
                        NoteContract.NotebookEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder
                );
                break;
            case NOTE:
                data = mDbHelper.getReadableDatabase().query(
                        NoteContract.NoteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder
                );
                break;
            case NOTE_WITH_TITLE:
                data = getNoteByTitle(uri, projection, sortOrder);
                break;
            case NOTE_WITH_NOTEBOOK:
                data = getNotesByNotebook(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri: " + uri.toString());
        }
        data.setNotificationUri(getContext().getContentResolver(), uri);
        return data;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher().match(uri)){
            case NOTEBOOK:
                return NoteContract.NotebookEntry.CONTENT_TYPE_DIR;
            case NOTE:
                return NoteContract.NoteEntry.CONTENT_TYPE_DIR;
            case NOTE_WITH_TITLE:
                return NoteContract.NoteEntry.CONTENT_TYPE_ITEM;
            case NOTE_WITH_NOTEBOOK:
                return NoteContract.NoteEntry.CONTENT_TYPE_DIR;
            default:
                throw new UnsupportedOperationException("Unsupported Uri: " + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = -1;
        switch (uriMatcher().match(uri)){
            case NOTEBOOK:
                id = db.insert(NoteContract.NotebookEntry.TABLE_NAME, null, contentValues);
                if(id != -1)
                    return NoteContract.NotebookEntry.CONTENT_URI;
                break;
            case NOTE:
                id = db.insert(NoteContract.NoteEntry.TABLE_NAME, null, contentValues);
                if(id != -1)
                    return NoteContract.NoteEntry.CONTENT_URI;
                break;
            default:
                throw new UnsupportedOperationException("Unsopported Uri: " + uri.toString());
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        if(selection == null)
            selection = "1";
        switch (uriMatcher().match(uri)) {
            case NOTEBOOK:
                rowsDeleted = db.delete(
                        NoteContract.NotebookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTE:
                rowsDeleted = db.delete(
                        NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int rowsUpdated;

        switch(uriMatcher().match(uri)){
            case NOTEBOOK:
                rowsUpdated = mDbHelper.getWritableDatabase().update(
                        NoteContract.NotebookEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case NOTE:
                rowsUpdated = mDbHelper.getWritableDatabase().update(
                        NoteContract.NoteEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown/Unsupported uri: " + uri);
        }
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}