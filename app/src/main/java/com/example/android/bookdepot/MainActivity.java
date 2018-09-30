package com.example.android.bookdepot;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;

import com.example.android.bookdepot.data.BookContract.BookEntry;

// Credit: Starter code from Udacity ABND Pets App

// Displays list of books that were entered and stored in the app.

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // Log tag for debugging purposes.
    public static final String TAG = MainActivity.class.getSimpleName();

    // Identified for the book data loader.
    private static final int BOOK_LOADER = 0;

    // Adapter for the ListView.
    BookCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Floating Action Button to open EditorActivity.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the book data.
        ListView bookListView = findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        // Set up an adapter to create a list itme for each row of the book data in the Cursor.
        // There is no book data until the loader finishes, so pass in null for the Cursor.
        mCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);

        // Set up the item click listener.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create new intent to go to EditorActivity.
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific book that was clicked on.
                // Append "id" passed as input to this method onto the BookEntry content URI.
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent.
                intent.setData(currentBookUri);

                // Launch the EditorActivity to display that data for the current book.
                startActivity(intent);
            }
        });

        // Kick off the loader.
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    // Helper method to insert hardcoded book data into the database. For debugging purposes only.

    private void insertBook() {
        // Create ContentValues object where column names are keys and book attributes are values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_TITLE, "Charlotte's Web");
        values.put(BookEntry.COLUMN_BOOK_PRICE, "$7.99");
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 16);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "Powell's City of Books");
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, "800-878-7323");

        // Insert a new row for Charlotte's Web into the provider using the ContentResolver.
        // Use the BookEntry Content URI to indicate inserting into the books database table.
        // Receive the new content URI that will allow us to access Charlotte's Web data.
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

    }

    // Helper method to delete all books in the database.
    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI,
                null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from book database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the menu_main.xml and add menu items to app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow.
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option.
            case R.id.action_insert_dummy_data:
                insertBook();
                return true;
            // Respond to a click on the "Delete all entries" menu option.
            case R.id.action_delete_all_entries:
                deleteAllBooks();
                return true;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table of interest.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY};

        // This loader will execute the ContentProvider's query method on a background thread.
        return new CursorLoader(this,   // Context of parent activity.
                BookEntry.CONTENT_URI,          // Provider content URI to query.
                projection,                     // Columns to include in the resulting Cursor.
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update BookCursorAdapter with this new cursor containing updated book data.
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted.
        mCursorAdapter.swapCursor(null);
    }

}