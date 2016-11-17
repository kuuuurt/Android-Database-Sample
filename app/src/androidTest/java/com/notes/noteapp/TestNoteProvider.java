package com.notes.noteapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.notes.noteapp.data.NoteContract;
import com.notes.noteapp.data.NoteDbHelper;
import com.notes.noteapp.data.NoteProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by qwerasdf on 8/1/16.
 */
@RunWith(AndroidJUnit4.class)
public class TestNoteProvider {

    private Context mContext;
    private NoteDbHelper dbHelper;
    private SQLiteDatabase db;


    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getTargetContext();
        dbHelper = new NoteDbHelper(mContext);
        db = dbHelper.getWritableDatabase();
    }

    @Test
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                NoteProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            Assert.assertEquals("DataProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + NoteContract.CONTENT_AUTHORITY,
                    providerInfo.authority, NoteContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("DataProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    @Test
    public void testGetType(){
        String type = mContext.getContentResolver().getType(NoteContract.NotebookEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE_DIR",
                NoteContract.NotebookEntry.CONTENT_TYPE_DIR, type);

        type = mContext.getContentResolver().getType(NoteContract.NoteEntry.CONTENT_URI);
        assertEquals("Uri should return CONTENT_TYPE_DIR",
                NoteContract.NoteEntry.CONTENT_TYPE_DIR, type);

        type = mContext.getContentResolver().getType(NoteContract.NoteEntry.buildNoteUriWithTitle("title"));
        assertEquals("Uri should return CONTENT_TYPE_ITEM",
                NoteContract.NoteEntry.CONTENT_TYPE_ITEM, type);

        type = mContext.getContentResolver().getType(NoteContract.NoteEntry.buildNoteUriWithNotebookId(1));
        assertEquals("Uri should return CONTENT_TYPE_DIR",
                NoteContract.NoteEntry.CONTENT_TYPE_DIR, type);

    }
}
