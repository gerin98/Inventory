package com.example.gerin.inventory;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gerin.inventory.data.ItemContract;

import java.text.DecimalFormat;

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);

        // Find the columns of item attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE);

        // Read the item attributes from the Cursor for the current item
        String itemName = cursor.getString(nameColumnIndex);
        String itemQuantity = String.valueOf(cursor.getInt(quantityColumnIndex));
        DecimalFormat formatter = new DecimalFormat("#0.00");
        String itemPrice = formatter.format(cursor.getDouble(priceColumnIndex));

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(itemName);
        quantityTextView.setText(itemQuantity);
        priceTextView.setText(itemPrice);
    }
}
