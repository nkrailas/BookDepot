package com.example.android.bookdepot;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that need to be modified in the list item layout.
        // Find the columns of book attributes of interest.
        // Read the book attributes from the cursor for the current book.
        // Update the TextViews with the attributes for the current book.

        // For title of book.
        TextView titleTextView = view.findViewById(R.id.title);
        int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE);
        String bookTitle = cursor.getString(titleColumnIndex);
        titleTextView.setText(bookTitle);

        // For price of book.
        TextView priceTextView = view.findViewById(R.id.price);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        String bookPriceAmount = cursor.getString(priceColumnIndex);
        String suggestedPrice = context.getString(R.string.suggested_price_label);
        String bookPrice = suggestedPrice + bookPriceAmount;
        priceTextView.setText(bookPrice);

        // For quantity of book.
        final TextView quantityTextView = view.findViewById(R.id.quantity);
        final Button sellBookButton = view.findViewById(R.id.sellBookButton);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
        final int bookQuantity = cursor.getInt(quantityColumnIndex);

        final BookData bookData = new BookData();
        bookData.setBookQuantity(bookQuantity);

        // Set function for "Sell Book" button. If quantity is 0, then disable.
        if (bookQuantity == 0) {
            sellBookButton.setEnabled(false);
        } else {
            sellBookButton.setEnabled(true);
        }

        // Respond when "Sell Book" button is clicked and decrease quantity.
        sellBookButton.setOnClickListener(new View.OnClickListener() {
            int currentBookId = cursor.getInt(cursor.getColumnIndex(BookEntry._ID));
            Uri contentUri = Uri.withAppendedPath(BookEntry.CONTENT_URI, Integer.toString(currentBookId));

            public void onClick(View view) {

                // Create ContentValues object where column names are keys and attributes are values.
                ContentValues values = new ContentValues();
                int bookQuantityValue = bookData.getBookQuantity();
                bookQuantityValue = bookQuantityValue - 1;
                if (bookQuantityValue == 0) {
                    sellBookButton.setEnabled(false);
                }
                values.put(BookEntry.COLUMN_BOOK_QUANTITY, bookQuantityValue);
                bookData.setBookQuantity(bookQuantityValue);

                // Credit (10/1/2018): "Try Catch vs. Null Check in Android by Karandeep Atwal,
                // https://medium.com/mindorks/try-catch-v-s-null-check-in-android-41ba3eba3b65
                try {
                    int rowUpdatedId = context.getContentResolver().update(contentUri, values,
                            null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        String inStock = context.getString(R.string.quantity_label);
        String inStockValue = String.valueOf(bookData.getBookQuantity());
        String quantityInStock = inStock + " " + inStockValue;
        quantityTextView.setText(quantityInStock);
    }


    static class BookData {
        int bookQuantity;

        public int getBookQuantity() {
            return bookQuantity;
        }

        public void setBookQuantity(int bookQuantity) {
            this.bookQuantity = bookQuantity;
        }
    }
}

