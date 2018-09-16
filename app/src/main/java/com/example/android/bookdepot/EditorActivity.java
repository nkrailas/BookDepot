package com.example.android.bookdepot;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookdepot.data.BookContract.BookEntry;
import com.example.android.bookdepot.data.BookDbHelper;

/**
 * Allows user to create a new entry or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    // EditText for book title.
    private EditText titleEditText;

    // EditText for book price.
    private EditText priceEditText;

    // EditText for book quantity.
    private EditText quantityEditText;

    // EditText for supplier name.
    private EditText supplierNameEditText;

    // EditText for supplier phone.
    private EditText supplierPhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views needed to read user input from.
        titleEditText = findViewById(R.id.edit_book_title);
        priceEditText = findViewById(R.id.edit_book_price);
        quantityEditText = findViewById(R.id.edit_book_quantity);
        supplierNameEditText = findViewById(R.id.edit_supplier_name);
        supplierPhoneEditText = findViewById(R.id.edit_supplier_phone);
    }

    // Get user input from editor and save new book into database.
    private void insertBook() {
        String title = titleEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        int price = Integer.parseInt(priceString);
        String quantityString = quantityEditText.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        String supplierName = supplierNameEditText.getText().toString().trim();
        String supplierPhone = supplierPhoneEditText.getText().toString().trim();

        // Create database helper.
        BookDbHelper dbHelper = new BookDbHelper(this);

        // Gets the data in write mode.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues object where column names are keys
        // and attributes from editor are values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_TITLE, title);
        values.put(BookEntry.COLUMN_BOOK_PRICE, price);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierName);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, supplierPhone);

        // Insert a new row in the database and return ID of that new row.
        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);

        // Show toast message to notify if insert was successful or not.
        if (newRowId == -1) {
            Toast.makeText(this, "Error saving book", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Book saved (row id): " + newRowId, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu items to app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar.
        switch (item.getItemId()) {
            // Respond when click "save" in menu option.
            case R.id.action_save:
                insertBook();    // Save book to database.
                finish();        // Exit activity.
                return true;

            // Respond when click "delete" in menu option.
            case R.id.action_delete:
                // Do nothing for now.
                return true;

            // Respond when click "up" arrow button in app bar.
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);  // Go back to MainActivity.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
