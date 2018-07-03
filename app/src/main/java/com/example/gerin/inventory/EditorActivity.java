package com.example.gerin.inventory;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity
        Intent intent = getIntent();

        // Assume for now that user is inserting a new pet
        setTitle(getString(R.string.editor_activity_title_new_pet));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
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
