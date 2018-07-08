package com.example.gerin.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerin.inventory.data.ItemContract;

import java.text.DecimalFormat;

public class EditorActivity extends AppCompatActivity {

    /**
     * Identifier for the pet data loader
     */
    private static final int EXISTING_PET_LOADER = 0;

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
     * EditText field to enter the item's description
     */
    private EditText mDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity
        Intent intent = getIntent();

        // Assume for now that user is inserting a new pet
        setTitle(getString(R.string.editor_activity_title_new_pet));

        mNameEditText = (EditText) findViewById(R.id.edit_item_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_item_quantity);
        mPriceEditText = (EditText) findViewById(R.id.edit_item_price);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_item_description);


    }

    // TODO: 2018-07-08 change from string to int/double 
    private void saveItem() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String descriptionString = mDescriptionEditText.getText().toString().trim();

        int quantityInteger = 0;
        if(!TextUtils.isEmpty(quantityString)){
            quantityInteger = Integer.parseInt(quantityString);
        }

        double priceDouble = 0;
        if(!TextUtils.isEmpty(priceString)){
            priceDouble = Double.parseDouble(priceString);
        }


//        // Check if this is supposed to be a new pet
//        // and check if all the fields in the editor are blank
//        if (mCurrentPetUri == null &&
//                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(breedString) &&
//                TextUtils.isEmpty(weightString) && mGender == PetEntry.GENDER_UNKNOWN) {
//            // Since no fields were modified, we can return early without creating a new pet.
//            // No need to create ContentValues and no need to do any ContentProvider operations.
//            return;
//        }

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, nameString);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, quantityInteger);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, priceDouble);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_DESCRIPTION, descriptionString);


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
                return true;
            case android.R.id.home:
                // Navigate up to parent activity
                // Show a dialog later on asking if user really wants to leave
                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
