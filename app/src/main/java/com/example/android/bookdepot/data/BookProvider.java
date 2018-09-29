package com.example.android.bookdepot.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.bookdepot.data.BookContract.BookEntry;

// Credit: Starter code from Udacity ABND Pets App.

public class BookProvider extends ContentProvider {

    // Tag for the log messages.
    public static final String LOG_TAG = BookProvider.class.getSimpleName();

    // URI matcher code for the content URI for the books table.
    private static final int BOOKS = 100;

    // URI matcher code for the content URI for a single book in the books table.
    private static final int BOOK_ID = 101;

    // URI matcher object to match a content URI to a corresponding code. The input passed into the
    // constructor represents the code to return for the root URI. It's common to use NO_MATCH.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer that is run the first time anything is called from this class.
    static {
        // Calls to addURI() go here for all content URI patterns that should be recognized by the
        // provider. All paths added to the UriMatcher have a corresponding code to return when a
        // match is found. This URI is used to provide access to MULTIPLE ROWS of the books table.
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);

        // This URI is used to provide access to ONE ROW of the books table. The # is a wildcard.
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#",
                BOOK_ID);
    }

    // Database helper object.
    private BookDbHelper =mDbHelper;

    pubic Cursor

    @Override
    query(Uri uri, String[] projection, String selection, String[] selectionArgs,
          String sortOrder) {

        // Get readable database.
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query.
        Cursor cursor;

        // Determine if the URI matcher can match the URI to a specific code.
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                // For BOOKS code, query the books table directly. Cursor could contain multiple rows.
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ID:
                // FOR BOOK_ID code, extract the ID from the URI.
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // Query the books table where _id equals an integer to return a Cursor for that row.
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor to know what the content URI was created for.
        // If the data at this URI changes, then need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor.
        return cursor;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    // Insert a book into the database with the given content values. Return the new content URI
    // for that specific row in the database.
    private Uri insertBook(Uri uri, ContentValues values) {
        // Check that the title is not null.
        String title = values.getAsString(BookEntry.COLUMN_BOOK_TITLE);
        if (title == null) {
            throw new IllegalArgumentException("Book requires a title");
        }

        // Check that the supplier name is not null.
        String supplierName = values.getAsString(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Book requires supplier name");
        }
    }
