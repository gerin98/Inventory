package com.example.gerin.inventory;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerin.inventory.data.ItemContract;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;

public class ItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Content URI for the existing item
     */
    private Uri mCurrentItemUri;

    /**
     * Identifier for the item data loader
     */
    private static final int EXISTING_ITEM_LOADER = 0;

    /**
     * Custom toolbar
     */
    private Toolbar toolbar;

    /**
     * References to TextViews
     */

    TextView quantityView;
    TextView priceView;
    TextView descriptionView;
    TextView tag1View;
    TextView tag2View;
    TextView tag3View;
    ImageView imageView;

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
        tag1View = (TextView) findViewById(R.id.item_tag1_field);
        tag2View = (TextView) findViewById(R.id.item_tag2_field);
        tag3View = (TextView) findViewById(R.id.item_tag3_field);
        imageView = (ImageView) findViewById(R.id.item_image_field);

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

        getSupportActionBar().setTitle("");

        getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);

    }

    // TODO: 2018-07-08 need a dialog here
    private void deleteItem() {
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

        finish();

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
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_edit_current_entry:
                Intent intent = new Intent(ItemActivity.this, EditorActivity.class);
                intent.setData(mCurrentItemUri);
                startActivity(intent);
                return true;
            case android.R.id.home:
                // Navigate up to parent activity
                // Show a dialog later on asking if user really wants to leave
//                NavUtils.navigateUpFromSameTask(ItemActivity.this);
                finish();
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
                ItemContract.ItemEntry.COLUMN_ITEM_DESCRIPTION,
                ItemContract.ItemEntry.COLUMN_ITEM_TAG1,
                ItemContract.ItemEntry.COLUMN_ITEM_TAG2,
                ItemContract.ItemEntry.COLUMN_ITEM_TAG3,
                ItemContract.ItemEntry.COLUMN_ITEM_IMAGE};

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
            int tag1ColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_TAG1);
            int tag2ColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_TAG2);
            int tag3ColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_TAG3);
            int imageColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_IMAGE);

            // Extract out the value from the Cursor for the given column index
            String name = data.getString(nameColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            double price = data.getDouble(priceColumnIndex);
            String description = data.getString(descriptionColumnIndex);
            String tag1 = data.getString(tag1ColumnIndex);
            String tag2 = data.getString(tag2ColumnIndex);
            String tag3 = data.getString(tag3ColumnIndex);
            byte[] photo = data.getBlob(imageColumnIndex);

            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);

            // set the title of the toolbar
            getSupportActionBar().setTitle(name);

            // Update the views on the screen with the values from the database
            quantityView.setText(Integer.toString(quantity));
            DecimalFormat formatter = new DecimalFormat("#0.00");
            priceView.setText(formatter.format(price));
            descriptionView.setText(description);
            imageView.setImageBitmap(theImage);

            // Initially no tags
            tag1View.setVisibility(View.GONE);
            tag2View.setVisibility(View.GONE);
            tag3View.setVisibility(View.GONE);

            // Determine which tags to fill
            if (!tag1.isEmpty()) {
                tag1View.setText(tag1);
                tag1View.setVisibility(View.VISIBLE);

                if (!tag2.isEmpty()) {
                    tag2View.setText(tag2);
                    tag2View.setVisibility(View.VISIBLE);

                    if (!tag3.isEmpty()) {
                        tag3View.setText(tag3);
                        tag3View.setVisibility(View.VISIBLE);
                        return;
                    }
                    else
                        return;
                } else if (!tag3.isEmpty()) {
                    tag2View.setText(tag3);
                    tag2View.setVisibility(View.VISIBLE);
                    return;
                }
            } else if (!tag2.isEmpty()) {
                tag1View.setText(tag2);
                tag1View.setVisibility(View.VISIBLE);

                if(!tag3.isEmpty()){
                    tag2View.setText(tag3);
                    tag2View.setVisibility(View.VISIBLE);
                    return;
                }
                else
                    return;
            } else if (!tag3.isEmpty()) {
                tag1View.setText(tag3);
                tag1View.setVisibility(View.VISIBLE);
                return;
            }



        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // set the title of the toolbar
        getSupportActionBar().setTitle("");

        // default bitmap
        Bitmap tempItemBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.image_prompt)).getBitmap();

        // Update the views on the screen with the values from the database
        quantityView.setText("");
        priceView.setText("");
        descriptionView.setText("");
        tag1View.setText("");
        tag2View.setText("");
        tag3View.setText("");
        imageView.setImageBitmap(tempItemBitmap);

    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog and continue editing
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
