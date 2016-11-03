package org.team2d.uncle_bob.Database;

/**
 * Created by nikolaev on 01.11.16.
 */
import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

class DataBaseHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "UncleBob.db";
    private static final int DATABASE_VERSION = 1;

    DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}