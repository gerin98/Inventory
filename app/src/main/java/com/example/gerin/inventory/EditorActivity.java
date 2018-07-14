package com.example.gerin.inventory;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerin.inventory.data.ItemContract;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.xml.datatype.Duration;

// TODO: 2018-07-09 if user clicks save twice two copies are saved
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the item data loader
     */
    private static final int EXISTING_ITEM_LOADER = 0;

    /**
     * Content URI for the existing item (null if it's a new item)
     */
    private Uri mCurrentItemUri;

    /**
     * EditText field to enter the item's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the item's quantity
     */
    private EditText mQuantityEditText;

    /**
     * EditText field to enter the item's price
     */
    private EditText mPriceEditText;

    /**
     * EditText field for tag 1
     */
    private EditText mTag1EditText;

    /**
     * EditText field for tag 2
     */
    private EditText mTag2EditText;

    /**
     * EditText field for tag 3
     */
    private EditText mTag3EditText;

    /**
     * EditText field to enter the item's description
     */
    private EditText mDescriptionEditText;

    /**
     * ImageView to show product image
     */
    private ImageView mItemImageView;

    /**
     * Bitmap of item's image
     */
    public Bitmap mItemBitmap;

    /**
     * Camera FAB
     */
    public FloatingActionButton fab;

    /**
     * Boolean flag that keeps track of whether the item has been edited (true) or not (false)
     */
    private boolean mItemHasChanged = false;

    /**
     * ID for accessing image from gallery
     */
    private static final int GALLERY_REQUEST = 1;

    /**
     * Maximum size for an image file that can be stored in the database
     */
    private static final int FIVE_MB = 5000000;

    /**
     * URI of selected image
     */
    private Uri selectedImage = null;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new item or editing an existing one.
        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        // If the intent DOES NOT contain a content URI, then we know that we are
        // creating a new item.
        if (mCurrentItemUri == null) {
            // This is a new pet, so change the app bar to say "Add a Pet"
            setTitle(getString(R.string.editor_activity_title_new_item));
        } else {
            // Otherwise this is an existing pet, so change app bar to say "Edit Pet"
            setTitle(getString(R.string.editor_activity_title_edit_item));

            // Initialize a loader to read the item data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }


        mNameEditText = (EditText) findViewById(R.id.edit_item_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_item_quantity);
        mPriceEditText = (EditText) findViewById(R.id.edit_item_price);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_item_description);
        mItemImageView = (ImageView) findViewById(R.id.edit_item_image);
        mTag1EditText = (EditText) findViewById(R.id.edit_item_tag1);
        mTag2EditText = (EditText) findViewById(R.id.edit_item_tag2);
        mTag3EditText = (EditText) findViewById(R.id.edit_item_tag3);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        mNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mDescriptionEditText.setOnTouchListener(mTouchListener);
        mTag1EditText.setOnTouchListener(mTouchListener);
        mTag2EditText.setOnTouchListener(mTouchListener);
        mTag3EditText.setOnTouchListener(mTouchListener);
        fab.setOnTouchListener(mTouchListener);

        mItemBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.image_prompt)).getBitmap();

        mItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new Dialog(EditorActivity.this);
                d.setContentView(R.layout.custom_dialog);
                ImageView image_full = (ImageView) d.findViewById(R.id.image_full);
                if(mItemBitmap != null)
                    image_full.setImageBitmap(mItemBitmap);
                d.show();
            }
        });

    }

    private void saveItem() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String descriptionString = mDescriptionEditText.getText().toString().trim();
        String tag1String = mTag1EditText.getText().toString().trim();
        String tag2String = mTag2EditText.getText().toString().trim();
        String tag3String = mTag3EditText.getText().toString().trim();
        String imageUri;
        if(selectedImage == null)
            imageUri = "null";
        else
            imageUri = selectedImage.toString();     // may cause error since default is null

        int quantityInteger = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            quantityInteger = Integer.parseInt(quantityString);
        }

        double priceDouble = 0;
        if (!TextUtils.isEmpty(priceString)) {
            priceDouble = Double.parseDouble(priceString);
        }

        // TODO: 2018-07-08 check for blank inputs in edit mode
        //        // Check if this is supposed to be a new pet
        //        // and check if all the fields in the editor are blank
        //        if (mCurrentItemUri == null &&
        //                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(breedString) &&
        //                TextUtils.isEmpty(weightString) && mGender == PetEntry.GENDER_UNKNOWN) {
        //            // Since no fields were modified, we can return early without creating a new pet.
        //            // No need to create ContentValues and no need to do any ContentProvider operations.
        //            return;
        //        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mItemBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] photo = baos.toByteArray();

        Log.e("save method","converted to byte array");

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, nameString);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, quantityInteger);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, priceDouble);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_DESCRIPTION, descriptionString);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_TAG1, tag1String);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_TAG2, tag2String);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_TAG3, tag3String);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_IMAGE, photo);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_URI, imageUri);

        // if URI is null, then we are adding a new item
        if (mCurrentItemUri == null) {
            // This is a NEW item, so insert a new item into the provider,
            // returning the content URI for the new item.
            Uri newUri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING item, so update the item with content URI: mCurrentItemUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void deleteItem() {
        // Only perform the delete if this is an existing item.
        if (mCurrentItemUri != null) {
            // Call the ContentResolver to delete the item at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentItemUri
            // content URI already identifies the item that we want.
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
        NavUtils.navigateUpFromSameTask(EditorActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete entry" menu option
            case R.id.action_save:
                //save item to database
                saveItem();
                //finish activity
                finish();
                return true;
            case R.id.action_delete_entry:
                //delete item from database
                showDeleteConfirmationDialog();
                //go back to catalog activity
                return true;
            case android.R.id.home:
                // Navigate up to parent activity
                if(mItemHasChanged)
                    showUnsavedChangesDialog();
                else
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
                ItemContract.ItemEntry.COLUMN_ITEM_IMAGE,
                ItemContract.ItemEntry.COLUMN_ITEM_URI};

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

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
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
            int uriColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_URI);


            // Extract out the value from the Cursor for the given column index
            String name = data.getString(nameColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            double price = data.getDouble(priceColumnIndex);
            String description = data.getString(descriptionColumnIndex);
            String tag1 = data.getString(tag1ColumnIndex);
            String tag2 = data.getString(tag2ColumnIndex);
            String tag3 = data.getString(tag3ColumnIndex);
            byte[] photo = data.getBlob(imageColumnIndex);
            String imageURI = data.getString(uriColumnIndex);

            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);


            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mQuantityEditText.setText(Integer.toString(quantity));
            DecimalFormat formatter = new DecimalFormat("#0.00");
            mPriceEditText.setText(formatter.format(price));
            mDescriptionEditText.setText(description);
            mTag1EditText.setText(tag1);
            mTag2EditText.setText(tag2);
            mTag3EditText.setText(tag3);
            mItemImageView.setImageBitmap(theImage);
            mItemBitmap = theImage;
            if(imageURI == "null")
                selectedImage = null;
            else
                selectedImage = Uri.parse(imageURI);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        Bitmap tempItemBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.image_prompt)).getBitmap();

        mNameEditText.setText("");
        mQuantityEditText.setText("");
        mPriceEditText.setText("");
        mDescriptionEditText.setText("");
        mTag1EditText.setText("");
        mTag2EditText.setText("");
        mTag3EditText.setText("");
        mItemImageView.setImageBitmap(tempItemBitmap);
        selectedImage = null;
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

    private void showUnsavedChangesDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.return_dialog_msg);
        builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Discard" button
                finish();
            }
        });
        builder.setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
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

    public void insertImage(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST:
                    selectedImage = data.getData();
                    Log.e("editor activity", selectedImage.toString());
                    try {
                        mItemBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        int i = mItemBitmap.getAllocationByteCount();
                        // if less than 5MB set the image
                        if(i < FIVE_MB) {
                            mItemImageView.setImageBitmap(mItemBitmap);
                            Log.e("Editor Activity", "successfully converted image");
                        }
                        // otherwise keep the default image
                        else{
                            mItemBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.image_prompt)).getBitmap();
                            selectedImage = null;
                            Log.e("Editor Activity", "image too large");
                            Toast.makeText(this,"Image too large", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("Editor Activity", String.valueOf(i));
                    } catch (IOException e) {
                        Log.e("onActivityResult", "Some exception " + e);
                    }
                    break;
            }
    }

}
