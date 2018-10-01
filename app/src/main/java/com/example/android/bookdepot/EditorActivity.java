package com.example.android.bookdepot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookdepot.data.BookContract.BookEntry;

import org.w3c.dom.Text;

// Credit: Starter code from Udacity ABND Pets App.

// Allow user to create a new book or edit an existing one.
public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // Identifier for the book data loader.
    private static final int EXISTING_BOOK_LOADER = 0;

    // Content URI for the existing book. Use null if it is a new book.
    private Uri mCurrentBookUri;

    // EditText for book title.
    private EditText mTitleEditText;

    // EditText for book price.
    private EditText mPriceEditText;

    // EditText for book quantity.
    private EditText mQuantityEditText;

    // EditText for supplier name.
    private EditText mSupplierNameEditText;

    // EditText for supplier phone.
    private EditText mSupplierPhoneEditText;

    // Boolean that keeps track of whether the book has been edited (true) or not (false).
    private boolean mBookHasChanged = false;

    // OnTouchListener listens for any user touches on a View that imply edits, so boolean is true.
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine intent used to launch activity to determine if creating a new book or editing
        // an existing one.
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        // If the intent does NOT contain a book content URI, then creating a new book.
        if (mCurrentBookUri == null) {
            // This is a new book, so change the app bar to "Add a Book."
            setTitle(getString(R.string.editor_activity_title_add_book));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing book, so change the app bar to "Edit a Book."
            setTitle(getString(R.string.editor_activity_title_edit_book));

            // Initialize a loader to read the book data from the database.
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        // Find all relevant views needed to read user input from.
        mTitleEditText = (EditText) findViewById(R.id.edit_book_title);
        mPriceEditText = findViewById(R.id.edit_book_price);
        mQuantityEditText = findViewById(R.id.edit_book_quantity);
        mSupplierNameEditText = findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEditText = findViewById(R.id.edit_supplier_phone);


        // Set up OnTouchListeners on all input fields to determine if user has touched or modified them.
        // This will signal if there are unsaved changes or not, if user leaves the editor without saving.
        mTitleEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchListener);

        // Respond when "minus" button is clicked to decrease quantity.
        Button decreaseQuantityButton = findViewById(R.id.decrease_quantity);
        decreaseQuantityButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int bookQuantity = 0;
                String bookQuantityString = mQuantityEditText.getText().toString();
                if (!TextUtils.isEmpty(bookQuantityString)) {
                    bookQuantity = Integer.parseInt(bookQuantityString);
                    // If quantity is greater than 0, then decrease by 1.
                    if (bookQuantity > 0) {
                        mBookHasChanged = true;
                        bookQuantity = bookQuantity - 1;
                        mQuantityEditText.setText(Integer.toString(bookQuantity));
                    } else {
                        // Otherwise set quantity to 0.
                        mQuantityEditText.setText(Integer.toString(bookQuantity));
                    }
                }
            }
        });

        // Respond when "plus" button is clicked to increase quantity.
        Button increaseQuantityButton = findViewById(R.id.increase_quantity);
        increaseQuantityButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                mBookHasChanged = true;
                int bookQuantity = 0;
                String bookQuantityString = mQuantityEditText.getText().toString();
                if (!TextUtils.isEmpty(bookQuantityString)) {
                    bookQuantity = Integer.parseInt(bookQuantityString);
                    bookQuantity = bookQuantity + 1;
                    mQuantityEditText.setText(Integer.toString(bookQuantity));
                } else if (mCurrentBookUri == null) {
                    bookQuantity = bookQuantity + 1;
                    mQuantityEditText.setText(Integer.toString(bookQuantity));
                }
            }
        });

        // Respond when "phone" button is pressed.
        FloatingActionButton fabCall = findViewById(R.id.fab_call);
        fabCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                        mSupplierPhoneEditText.getText().toString(), null));
                startActivity(intent);
                finish();
            }
        });

    }

    // Get user input from editor and save book into database.
    private void saveBook() {
        // Read from input fields and trim to eliminate leading or trailing white space.
        String titleString = mTitleEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        // Check if this is supposed to be a new book and if all the fields in the editor are blank.
        if (mCurrentBookUri == null &&
                TextUtils.isEmpty(titleString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(supplierNameString) &&
                TextUtils.isEmpty(supplierPhoneString)) {

            // Since no fields were modified, return early without creating a new book.
            // No need to create ContentValues or do any ContentProvider operations.
            return;
        }

        // Create ContentValues object where column names are key and attributes from editor are values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_TITLE, titleString);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantityString);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameString);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, supplierPhoneString);

        // For price, include default value.
        String priceDefault = "0.00";
        if (!TextUtils.isEmpty(priceString)) {
            values.put(BookEntry.COLUMN_BOOK_PRICE, priceString);
        } else {
            values.put(BookEntry.COLUMN_BOOK_PRICE, priceDefault);
        }

        // Determine if this is a new or existing book by checking if mCurrentBookUri is null or not.
        if (mCurrentBookUri == null) {
            // This is a new book, so insert a new book into the provider, returning the content URI.
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful, so display a toast message.
                Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // This is an existing book, so update the book with content URI and new ContentValues.
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values,
                    null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the menu_editor.xml to add menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    // This method is called after invalidateOptionsMenu() so that the menu can be updated.
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new book, then hide the "Delete" menu item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu.
        switch (item.getItemId()) {
            case R.id.action_save:              // Respond to click "Save" in menu option.
                saveBook();                     // Save book to database.
                finish();                       // Exit activity.
                return true;
            case R.id.action_delete:             // Respond to click "Delete" in menu option.
                showDeleteConfirmationDialog();  // Pop up confirmation dialog for deletion.
                return true;
            case android.R.id.home:             // Respond to click "up" arrow button in app bar.
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);  // Go back to MainActivity.
                    return true;
                }

                // Otherwise, if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                // Respond to click "Discard" button and navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user that they have unsaved changes.
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    // This method is called with the "back" button is pressed.
    @Override
    public void onBackPressed() {
        // If the book hasn't changed, then continue with handling "back" button press.
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise, it there are unsaved changes, setup dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // User clicked "Discard" button, close current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes.
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // The editor shows all book attributes.
        // Define a projection containing all columns from the books table.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread.
        return new CursorLoader(this,       // Parent activity context.
                mCurrentBookUri,                    // Query the content URI for the current book.
                projection,                         // Columns to include in the resulting Cursor.
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor.
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it.
        if (cursor.moveToFirst()) {
            // Find the columns of book attributes of interest.
            int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

            // Extract the value from the cursor for the given column index.
            String title = cursor.getString(titleColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            // Update the views on the screen with the values from the database.
            mTitleEditText.setText(title);
            mPriceEditText.setText(price);
            mQuantityEditText.setText(Integer.toString(quantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneEditText.setText(supplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mTitleEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierPhoneEditText.setText("");
    }

    /**
     * Show a dialog warning the user there are unsaved changes that will be lost if they
     * continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when the user
     *                                   confirms they want to discard their changes.
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder. Set the message and click listeners for the positive
        // and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Keep editing" button, so dismiss dialog and continue editing book.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog.
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the book.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Cancel" button, so dismiss dialog and continue editing book.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Perform the deletion of the book in the database.
    private void deleteBook() {
        // Only perform the delete if this is an existing book.
        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the book at the given content URI.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri,
                    null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity.
        finish();
    }
}