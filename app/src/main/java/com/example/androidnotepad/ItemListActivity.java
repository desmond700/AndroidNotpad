package com.example.androidnotepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidnotepad.util.DatabaseHelper;
import com.example.androidnotepad.util.DatabaseQueries;
import com.example.androidnotepad.util.Note;
import com.example.androidnotepad.util.NoteContent;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidnotepad.util.GlobalApplication.getContext;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewNoteActivity.class);
                startActivity(intent);
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, readNotes(this), mTwoPane));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<Note> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note item = (Note) view.getTag();
                if (mTwoPane) {
                    Log.d("noteTitle",mValues.get(item._ID).title);
                    Log.d("noteContent",mValues.get(item._ID).note);
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, Integer.toString(item._ID));
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, Integer.toString(item._ID));

                    context.startActivity(intent);
                }
            }
        };
        private final ImageButton clearBtn = (ImageButton) findViewById(R.id.clear_note);
        private final View.OnClickListener mClearBtnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note mItem = (Note)v.getTag();
                SQLiteDatabase database = new DatabaseHelper(getContext()).getReadableDatabase();
                int value = database.delete(DatabaseQueries.CREATE_TABLE,DatabaseQueries._ID+"="+mItem._ID, null);

                open(v, value);

            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<Note> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            for(int i = 0; i < items.size(); i++){
                Log.d("recycler items", Integer.toString(items.get(i)._ID));
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).title);
            holder.mContentView.setText(mValues.get(position).date);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
            holder.itemView.setOnClickListener(mClearBtnOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.note_title);
                mContentView = view.findViewById(R.id.note_date);
            }
        }
    }

    public void open(View view, final int value){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(getContext(),"You clicked yes button",Toast.LENGTH_LONG).show();
                                if(value > 0){
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static List<Note> readNotes(Context context){
        SQLiteDatabase database = new DatabaseHelper(context).getReadableDatabase();
        List<Note> list = new ArrayList<Note>();
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
            int id = cursor.getInt(index);
            Log.d("sqliteid",Integer.toString(id));
            index = cursor.getColumnIndexOrThrow(projection[1]);
            String title = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(projection[2]);
            String note = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(projection[3]);
            String date = cursor.getString(index);

            list.add(new Note(id,title,note,date));
        }

        return list;
    }
}
