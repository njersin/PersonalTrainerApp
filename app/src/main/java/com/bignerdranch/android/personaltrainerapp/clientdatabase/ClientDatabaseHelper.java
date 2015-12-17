package com.bignerdranch.android.personaltrainerapp.clientdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bignerdranch.android.personaltrainerapp.clientdatabase.ClientDBSchema.ClientTable;
import com.bignerdranch.android.personaltrainerapp.clientdatabase.ClientDBSchema.SessionTable;

public class ClientDatabaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "clientDatabase.db";

    public ClientDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ClientDBSchema.ClientTable.NAME + "(" +
                        " _uniqueid integer primary key autoincrement, " +
                        ClientTable.Cols.UUID + ", " +
                        ClientTable.Cols.NAME + ", " +
                        ClientTable.Cols.EMAIL_ADDRESS + ", " +
                        ClientTable.Cols.CREATE_DATE +
                        ")"
        );

        db.execSQL("create table " + ClientDBSchema.SessionTable.NAME + "(" +
                        " _uniqueid integer primary key autoincrement, " +
                        SessionTable.Cols.SESSION_ID + ", " +
                        SessionTable.Cols.CLIENT_ID + ", " +
                        SessionTable.Cols.TITLE + ", " +
                        SessionTable.Cols.LOCATION + ", " +
                        SessionTable.Cols.DATE + ", " +
                        SessionTable.Cols.WEIGHT +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
