package com.example.android.bookdepot;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.bookdepot.data.BookContract.BookEntry;
import com.example.android.bookdepot.data.BookDbHelper;

// Credit: Starter code from Udacity ABND Pets App

// Displays list of books that were entered and stored in the app.

public class MainActivity extends AppCompatActivity {

    // Log tag for debugging purposes.
    public static final String TAG = MainActivity.class.getSimpleName();

    // Database helper that will provide access to the database.
    private BookDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Floating Action Button to open EditorActivity.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Access database by instantiating subclass of SQLiteOpenHelper.
        dbHelper = new BookDbHelper(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    // Temp helper method to display information in textview about state of books database.
    private void displayDatabaseInfo() {

        // Create and/or open database to read from it.
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE
        };

        Cursor cursor = db.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.text_view_book);

        try {
            // Create a header in the TextView.
            displayView.setText("The books table contains " + cursor.getCount() + " entries.\n\n");
            displayView.append(BookEntry._ID + " - " +
                    BookEntry.COLUMN_BOOK_TITLE + " - " +
                    BookEntry.COLUMN_BOOK_PRICE + " - " +
                    BookEntry.COLUMN_BOOK_QUANTITY + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + " - " +
                    "\n");

            // Figure out the index of each column.
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

            // Iterate through all the returned rows in the cursor.
            // Display the information from each column in this order.
            while (cursor.moveToNext()) {
                // Use this index to extract String or int value at row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                // Display the values from each column of the current row in the cursor in TextView.
                displayView.append(("\n" + currentID + " - " +
                        currentTitle + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone));
            }

        } finally {
            // Always close the cursor to release all of it resources and makes it invalid.
            cursor.close();
        }
    }

    private void insertBook() {
        // Gets the database in write mode.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object where names are the keys and book attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_TITLE, "Sharing is Caring");
        values.put(BookEntry.COLUMN_BOOK_PRICE, 9);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 9);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "Rainbow Books");
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, "509-216-9823");

        // Insert a new row for "Sharing is Caring" in the database

        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New row ID " + newRowId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu options menu_main.xml to add menu items to app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar.
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data: // Respond to click on "Insert Dummy Data."
                insertBook();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_entries: // Respond to a click on "Delete All Entries."
                // Do nothing for now.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


