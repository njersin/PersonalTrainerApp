package com.bignerdranch.android.personaltrainerapp.clientdatabase;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.bignerdranch.android.personaltrainerapp.Client;
import com.bignerdranch.android.personaltrainerapp.Session;
import com.bignerdranch.android.personaltrainerapp.clientdatabase.ClientDBSchema.ClientTable;
import com.bignerdranch.android.personaltrainerapp.clientdatabase.ClientDBSchema.SessionTable;

import java.util.Date;
import java.util.UUID;

public class ClientCursorWrapper extends CursorWrapper {

    public ClientCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Client getClient() {
        String uuidString = getString(getColumnIndex(ClientTable.Cols.UUID));
        String clientName = getString(getColumnIndex(ClientTable.Cols.NAME));
        String emailAddress = getString(getColumnIndex(ClientTable.Cols.EMAIL_ADDRESS));
        long date = getLong(getColumnIndex(ClientTable.Cols.CREATE_DATE));

        Client client = new Client(UUID.fromString(uuidString));
        client.setName(clientName);
        client.setEmailAddress(emailAddress);
        client.setDate(new Date(date));

        return client;
    }

    public Session getSession() {
        String sessionIDString = getString(getColumnIndex(SessionTable.Cols.SESSION_ID));
        String clientIDString = getString(getColumnIndex(SessionTable.Cols.CLIENT_ID));
        String title = getString(getColumnIndex(SessionTable.Cols.TITLE));
        String location = getString(getColumnIndex(SessionTable.Cols.LOCATION));
        long date = getLong(getColumnIndex(SessionTable.Cols.DATE));
        double weight = getDouble(getColumnIndex(SessionTable.Cols.WEIGHT));

        Session session = new Session(UUID.fromString(sessionIDString));
        session.setClientID(UUID.fromString(clientIDString));
        session.setTitle(title);
        session.setLocation(location);
        session.setDate(new Date(date));
        session.setWeight(weight);

        return session;
    }

}
