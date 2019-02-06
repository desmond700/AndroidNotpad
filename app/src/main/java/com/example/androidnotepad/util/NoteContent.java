package com.example.androidnotepad.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<NoteContent.NoteItem> ITEMS = new ArrayList<NoteContent.NoteItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, NoteContent.NoteItem> ITEM_MAP = new HashMap<Integer, NoteContent.NoteItem>();
    public static List<Note> list = new ArrayList<Note>();

    static {
        // Add some sample items.
        for (int i = 0; i < list.size(); i++) {
            Log.d("items", Integer.toString(i));
            addItem(createNoteItem(list.get(i)));
        }
    }

    private static void addItem(NoteContent.NoteItem item) {
        Log.d("addItem",Integer.toString(item.id));
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static NoteContent.NoteItem createNoteItem(Note mNote) {

        return new NoteContent.NoteItem(mNote._ID, mNote.title, mNote.note, mNote.date);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class NoteItem {
        public final int id;
        public final String title;
        public final String note;
        public final String date;

        public NoteItem(int id, String title, String note, String date) {
            this.id = id;
            this.title = title;
            this.note = note;
            this.date = date;
        }

        @Override
        public String toString() {
            return title + "\n" + date;
        }
    }


}
