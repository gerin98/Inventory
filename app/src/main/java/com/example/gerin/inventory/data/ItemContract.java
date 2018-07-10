package com.example.gerin.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ItemContract {

    // to prevent from accidently using this class
    // give it a private constructor
    private ItemContract() {}


    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.gerin.inventory";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_INVENTORY = "inventory";


    /**
     * Inner class that defines constant values for the pets database table.
     */
    public static final class ItemEntry implements BaseColumns{

        /** The content URI to access the item data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /** Name of database table for items */
        public final static String TABLE_NAME = "inventory";

        /**
         * Unique ID number for the item (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the item.
         *
         * Type: TEXT (String)
         */
        public final static String COLUMN_ITEM_NAME ="name";

        /**
         * Quantity of the item.
         *
         * Type: TEXT (Integer)
         */
        public final static String COLUMN_ITEM_QUANTITY = "quantity";

        /**
         * Price of the item.
         *
         * Type: TEXT (Float)
         */
        public final static String COLUMN_ITEM_PRICE = "price";

        /**
         * Description of the product
         *
         * Type: TEXT (String)
         */
        public final static String COLUMN_ITEM_DESCRIPTION = "description";

        /**
         * First tag for the item.
         *
         * Type: TEXT (String)
         */
        public final static String COLUMN_ITEM_TAG1 = "tag1";

        /**
         * Second tag for the item.
         *
         * Type: TEXT (String)
         */
        public final static String COLUMN_ITEM_TAG2 = "tag2";

        /**
         * Third tag for the item.
         *
         * Type: TEXT (String)
         */
        public final static String COLUMN_ITEM_TAG3 = "tag3";

        /**
         * Image for the item.
         *
         * Type: BLOB
         */
        public final static String COLUMN_ITEM_IMAGE = "image";

        /**
         * URI for the image
         *
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_URI = "imageuri";

    }

}
