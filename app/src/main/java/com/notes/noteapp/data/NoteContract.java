package com.notes.noteapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by qwerasdf on 7/31/16.
 */
public class NoteContract {
    public static final String CONTENT_AUTHORITY = "com.notes.noteapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NOTEBOOKS = "notebook";
    public static final String PATH_NOTES = "note";

    public static final class NoteEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_NOTES).build();

        public static final String CONTENT_TYPE_DIR =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;
        public static final String CONTENT_TYPE_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;

        public static final String TABLE_NAME = "note";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_NOTEBOOK_KEY = "notebook_id";

        public static Uri buildNoteUriWithTitle(String title){
            return CONTENT_URI.buildUpon().appendPath(title).build();
        }

        public static Uri buildNoteUriWithNotebookId(long id){
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static String getTitleFromUri(Uri uri){
            return uri.getLastPathSegment();
        }

        public static long getIDFromUri(Uri uri){
            return Long.parseLong(uri.getLastPathSegment());
        }
    }

    public static final class NotebookEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_NOTEBOOKS).build();

        public static final String CONTENT_TYPE_DIR =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTEBOOKS;
        public static final String CONTENT_TYPE_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTEBOOKS;

        public static final String TABLE_NAME = "notebook";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}