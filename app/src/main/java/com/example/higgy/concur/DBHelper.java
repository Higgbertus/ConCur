package com.example.higgy.concur;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Higgy on 27.12.2017.
 */

public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "currencyDB.db";
    public static final String CUR_TABLE = "currency";
    public static final String CUR_COL_TITLE = "cur";
    public static final String CUR_COL_VALUE = "value";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + CUR_TABLE + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + CUR_COL_TITLE + " TEXT"
            + CUR_COL_VALUE + " TEXT)";


    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + CUR_TABLE;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

}
