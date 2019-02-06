package com.example.androidnotepad;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.androidnotepad.util.DatabaseHelper;
import com.example.androidnotepad.util.DatabaseQueries;
import com.example.androidnotepad.util.Datetime;
import com.example.androidnotepad.util.Note;
import com.github.clans.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        FloatingActionButton fab = findViewById(R.id.fab);
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

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<Note> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            TextView tvEmpty = findViewById(R.id.empty_view);
            RecyclerView rc = findViewById(R.id.item_list);
            Log.d("size", Integer.toString(items.size()));
            if(items.size() == 0){
                tvEmpty.setVisibility(View.VISIBLE);
                rc.setVisibility(View.GONE);
            }else{
                tvEmpty.setVisibility(View.GONE);
                rc.setVisibility(View.VISIBLE);
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
            //holder.itemView.setOnClickListener(mClearBtnOnClickListener);
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

    public static List<Note> readNotes(Context context){
        SQLiteDatabase database = new DatabaseHelper(context).getReadableDatabase();
        List<Note> list = new ArrayList<Note>();
        Datetime datetime;
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
            //String date = dateFormat(cursor.getString(index));
            datetime = new Datetime(date);
            list.add(new Note(id,title,note,datetime.getDate()));
        }

        return list;
    }
}
