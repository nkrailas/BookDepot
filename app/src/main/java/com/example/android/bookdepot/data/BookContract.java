package com.example.android.bookdepot.data;

import android.provider.BaseColumns;

// Credit: Starter code from Udacity ABND Pets App

public final class BookContract {

    //An empty private constructor makes sure that the class is not going to be initialized.
    private BookContract() {}

    public static final class BookEntry implements BaseColumns {

        public static final String TABLE_NAME = "books";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BOOK_TITLE = "title";
        public static final String COLUMN_BOOK_PRICE = "price";
        public static final String COLUMN_BOOK_QUANTITY = "quantity";
        public static final String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_BOOK_SUPPLIER_PHONE = "supplier_phone";

    }
}

