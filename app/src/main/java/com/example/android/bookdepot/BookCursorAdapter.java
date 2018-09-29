package com.example.android.bookdepot;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.bookdepot.data.BookContract.BookEntry;

// Credit: Starter code from Udacity ABND Pets App

public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new BookCursorAdapter.
     *
     * @param context Context.
     * @param c       Cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is bound to views yet.
     *
     * @param context Context.
     * @param cursor  Cursor from which to get the data. It is already moved to the correct position.
     * @param parent  Parent to which the new view is attached to.
     * @return Newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout in list_item.xml.
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Binds book data in the current row pointed to by the cursor to the given list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method.
     * @param context Context.
     * @param cursor  Cursor from which to get the data. It is already moved to the correct position.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout.
        TextView titleTextView = view.findViewById(R.id.title);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);

        // Find the columns of book attributes that we're interested in.
        int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);

        // Read the book attributes from the cursor for the current book.
        String bookTitle = cursor.getString(titleColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        String bookQuantity = cursor.getString(quantityColumnIndex);

        // Update the TextViews with the attributes for the current book.
        titleTextView.setText(bookTitle);
        priceTextView.setText(bookPrice);
        quantityTextView.setText(bookQuantity);


    }

}
