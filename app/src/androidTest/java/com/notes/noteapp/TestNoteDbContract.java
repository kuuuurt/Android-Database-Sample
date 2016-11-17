package com.notes.noteapp;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.notes.noteapp.data.NoteContract;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by qwerasdf on 8/1/16.
 */

@RunWith(AndroidJUnit4.class)
public class TestNoteDbContract {
    @Test
    public void testBuildNoteWithTitle(){
        Uri titleUri = NoteContract.NoteEntry.buildNoteUriWithTitle("notetitle");
        assertNotNull("Uri not created!", titleUri);
        assertEquals("Uri not created correctly!", titleUri.toString(),
                NoteContract.NoteEntry.CONTENT_URI + "/notetitle");
    }

    @Test
    public void testBuildNoteWithId(){
        Uri idUri = NoteContract.NoteEntry.buildNoteUriWithNotebookId(1);
        assertNotNull("Uri not created!", idUri);
        assertEquals("Uri not created correctly!", idUri.toString(),
                NoteContract.NoteEntry.CONTENT_URI + "/1");
    }
}
