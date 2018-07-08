package com.example.gerin.inventory;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity {

    /**
     * Content URI for the existing item
     */
    private Uri mCurrentItemUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.item_fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemActivity.this, EditorActivity.class);
                intent.setData(mCurrentItemUri);
                startActivity(intent);
            }
        });
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
            Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
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
}
