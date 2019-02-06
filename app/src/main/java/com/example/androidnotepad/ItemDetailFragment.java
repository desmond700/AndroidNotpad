package com.example.androidnotepad;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.androidnotepad.util.DatabaseHelper;
import com.example.androidnotepad.util.DatabaseQueries;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private String mTitle;
    private String mNote;
    private String mDate;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            readNotes(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mTitle);
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mNote != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mNote);
        }

        return rootView;
    }

    public void readNotes(int id){
        SQLiteDatabase database = new DatabaseHelper(getContext()).getReadableDatabase();
        String[] projection = {
                DatabaseQueries._ID,
                DatabaseQueries.COLUMN_TITLE,
                DatabaseQueries.COLUMN_NOTE,
                DatabaseQueries.COLUMN_WRITTEN_DATE
        };

        Cursor cursor = database.query(
                DatabaseQueries.TABLE_NAME,    // The table to query
                projection,                    // The columns to return
                null,                 // The columns for the WHERE clause
                null,              // The values for the WHERE clause
                null,                  // don't group the rows
                null,                   // don't filter by row groups
                null                   // don't sort
        );

        while(cursor.moveToNext()) {
            int index;
            index = cursor.getColumnIndexOrThrow(projection[0]);
            int note_id = cursor.getInt(index);
            if(id == note_id) {
                Log.d("sqliteid", Integer.toString(id));
                index = cursor.getColumnIndexOrThrow(projection[1]);
                mTitle = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow(projection[2]);
                mNote = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow(projection[3]);
                mDate = cursor.getString(index);
            }
        }
    }
}
