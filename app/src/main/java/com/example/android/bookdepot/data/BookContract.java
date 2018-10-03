package com.example.android.bookdepot.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

// Credit: Starter code from Udacity ABND Pets App

public final class BookContract {

    //An empty private constructor makes sure that the class is not going to be initialized.
    private BookContract() {}

    // Name for the entire content provider. Usually the package name for app.
    public static final String CONTENT_AUTHORITY = "com.example.android.bookdepot";

    // Content Authority creates the base of all URIs which apps use to contact the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible path appended to base content URI for possible URIs.
    public static final String PATH_BOOKS = "books";

    // Inner class that defines constant values for the books database table.
    // Each entry in the table represents a single book.
    public static final class BookEntry implements BaseColumns {

        // The content URI to access the book data in the provider.
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        // The MIME type of the Content URI for a list of books.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        // The MIME type of the CONTENT URI for a single book.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        // Name of database table for books.
        public static final String TABLE_NAME = "books";

        // Unique ID number for a book.
        public static final String _ID = BaseColumns._ID;

        // Title, price, quantity, supplier name and supplier phone of a book.
        public static final String COLUMN_BOOK_TITLE = "title";                     // Text.
        public static final String COLUMN_BOOK_PRICE = "price";                     // Integer.
        public static final String COLUMN_BOOK_QUANTITY = "quantity";               // Integer.
        public static final String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";     // Text.
        public static final String COLUMN_BOOK_SUPPLIER_PHONE = "supplier_phone";   // Integer.

    }
}

