package com.bignerdranch.android.personaltrainerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.personaltrainerapp.clientdatabase.ClientCursorWrapper;
import com.bignerdranch.android.personaltrainerapp.clientdatabase.ClientDBSchema.ClientTable;
import com.bignerdranch.android.personaltrainerapp.clientdatabase.ClientDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientLab {
    private static ClientLab sClientLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ClientLab get(Context context) {
        if (sClientLab == null) {
            sClientLab = new ClientLab(context);
        }
        return sClientLab;
    }

    private ClientLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ClientDatabaseHelper(mContext).getWritableDatabase();
    }

    public void addClient(Client client){
        ContentValues values = getContentValues(client);
        mDatabase.insert(ClientTable.NAME, null, values);
    }

    public void deleteClient(Client client) {
        ContentValues values = getContentValues(client);
        mDatabase.delete(ClientTable.NAME, ClientTable.Cols.UUID + " = ?", new String[]{client.getID().toString()});
    }

    public void updateClient(Client client) {
        ContentValues values = getContentValues(client);
        mDatabase.update(ClientTable.NAME, values, ClientTable.Cols.UUID + " = ?", new String[]{client.getID().toString()});
    }

    public List<Client> getClients() {
        List<Client> clients = new ArrayList<>();
        ClientCursorWrapper cursor = queryClients(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                clients.add(cursor.getClient());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return clients;
    }

    public Client getClient(UUID id) {
        ClientCursorWrapper cursor = queryClients(ClientTable.Cols.UUID + " = ?", new String[]{id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getClient();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Client client) {
        ContentValues values = new ContentValues();
        values.put(ClientTable.Cols.UUID, client.getID().toString());
        values.put(ClientTable.Cols.NAME, client.getName());
        values.put(ClientTable.Cols.EMAIL_ADDRESS, client.getEmailAddress());
        values.put(ClientTable.Cols.CREATE_DATE, client.getDate().getTime());
        return values;
    }

    private ClientCursorWrapper queryClients(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ClientTable.NAME,
                null, //columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                ClientTable.Cols.CREATE_DATE  + " DESC"//orderBy
        );
        return new ClientCursorWrapper(cursor);
    }
}
