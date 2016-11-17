package com.notes.noteapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.notes.noteapp.data.NoteContract;

/**
 * Created by qwerasdf on 8/1/16.
 */
public class NotebookAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final TextView nameView;
        public final TextView descView;


        public ViewHolder(View view) {
            nameView = (TextView) view.findViewById(R.id.text_name);
            descView = (TextView) view.findViewById(R.id.text_desc);
        }
    }

    public NotebookAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_notebook_note,
                viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.nameView.setText(cursor.getString(cursor.getColumnIndex(
                NoteContract.NotebookEntry.COLUMN_NAME)));
        viewHolder.descView.setText(cursor.getString(cursor.getColumnIndex(
                NoteContract.NotebookEntry.COLUMN_DESCRIPTION)));
    }
}