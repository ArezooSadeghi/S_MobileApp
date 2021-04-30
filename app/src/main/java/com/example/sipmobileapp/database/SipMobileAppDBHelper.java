package com.example.sipmobileapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SipMobileAppDBHelper extends SQLiteOpenHelper {

    public SipMobileAppDBHelper(@Nullable Context context) {
        super(context, SipMobileAppSchema.DB_NAME, null, SipMobileAppSchema.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("CREATE TABLE " + SipMobileAppSchema.ServerDataTable.NAME + " (");
        stringBuilder.append(SipMobileAppSchema.ServerDataTable.Cols.PRIMARY_KEY + " INTEGER PRIMARY KEY,");
        stringBuilder.append(SipMobileAppSchema.ServerDataTable.Cols.CENTER_NAME + " TEXT,");
        stringBuilder.append(SipMobileAppSchema.ServerDataTable.Cols.IP_ADDRESS + " TEXT,");
        stringBuilder.append(SipMobileAppSchema.ServerDataTable.Cols.PORT + " TEXT");
        stringBuilder.append(");");

        sqLiteDatabase.execSQL(stringBuilder.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
