package com.example.gerin.inventory;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerin.inventory.data.ItemContract;

import java.text.DecimalFormat;

public class ItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    /**
     * Content URI for the existing item
     */
    private Uri mCurrentItemUri;

    /**
     * Identifier for the item data loader
     */
    private static final int EXISTING_ITEM_LOADER = 0;

    /**
     *  Custom toolbar
     */
    private Toolbar toolbar;

    /**
     * References to TextViews
     */

    TextView quantityView;
    TextView priceView;
    TextView descriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        // find references to TextViews
        quantityView = (TextView) findViewById(R.id.item_quantity_field);
        priceView = (TextView) findViewById(R.id.item_price_field);
        descriptionView = (TextView) findViewById(R.id.item_description_field);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.item_fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemActivity.this, EditorActivity.class);
                intent.setData(mCurrentItemUri);
                startActivity(intent);
            }
        });

        /**
         * Create custom toolbar for the collapsing toolbar menu
         *
         */
        // get ID of custom toolbar and set as the desired toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // this line shows back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);

    }

    // TODO: 2018-07-08 need a dialog here
    private void deleteItem(){
        int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_delete_item_successful),
                    Toast.LENGTH_SHORT).show();
        }

    }

    /* Methods to create menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_current_entry:
                // delete item from database
                deleteItem();
                //finish activity
                finish();
                return true;
            case android.R.id.home:
                // Navigate up to parent activity
                // Show a dialog later on asking if user really wants to leave
                NavUtils.navigateUpFromSameTask(ItemActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all item attributes, define a projection that contains
        // all columns from the inventory table
        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_ITEM_NAME,
                ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemContract.ItemEntry.COLUMN_ITEM_PRICE,
                ItemContract.ItemEntry.COLUMN_ITEM_DESCRIPTION};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentItemUri,         // Query the content URI for the current item
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME);
            int quantityColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY);
            int priceColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE);
            int descriptionColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_DESCRIPTION);

            // Extract out the value from the Cursor for the given column index
            String name = data.getString(nameColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            double price = data.getDouble(priceColumnIndex);
            String description = data.getString(descriptionColumnIndex);

            // set the title of the toolbar
            getSupportActionBar().setTitle(name);

            // Update the views on the screen with the values from the database
            quantityView.setText(Integer.toString(quantity));
            DecimalFormat formatter = new DecimalFormat("#0.00");
            priceView.setText(formatter.format(price));
            descriptionView.setText(description);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // set the title of the toolbar
        getSupportActionBar().setTitle("");

        // Update the views on the screen with the values from the database
        quantityView.setText("");
        priceView.setText("");
        descriptionView.setText("");

    }
}
