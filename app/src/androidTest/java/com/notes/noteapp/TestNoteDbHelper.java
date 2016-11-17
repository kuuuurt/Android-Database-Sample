package com.notes.noteapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.notes.noteapp.data.NoteDbHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

/**
 * Created by qwerasdf on 8/1/16.
 */
@RunWith(AndroidJUnit4.class)
public class TestNoteDbHelper {

    @Test
    public void testCreateDB(){
        Context context = InstrumentationRegistry.getTargetContext();
        NoteDbHelper dbHelper = new NoteDbHelper(context);
        context.deleteDatabase(dbHelper.DATABASE_NAME);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue("Database not created!", db.isOpen());
    }

}
