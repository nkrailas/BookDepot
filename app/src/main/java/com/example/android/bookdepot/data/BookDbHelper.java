package com.example.android.bookdepot.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bookdepot.data.BookContract.BookEntry;

// Credit: Starter code from Udacity ABND Pets App

// Database helper manages database creation and version management.
public class BookDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BookDbHelper.class.getSimpleName();

    // Name of the database file.
    private static final String DATABASE_NAME = "books.db";

    // Name of the database version.
    private static final int DATABASE_VERSION = 1;

    // Construct a new instance of BookDbHelper.
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // The is called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the books table.
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_BOOK_TITLE + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_PRICE + " TEXT, "
                + BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " TEXT, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    //This is called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}