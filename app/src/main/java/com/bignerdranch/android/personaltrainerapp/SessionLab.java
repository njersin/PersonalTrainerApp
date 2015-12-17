package com.bignerdranch.android.personaltrainerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bignerdranch.android.personaltrainerapp.clientdatabase.ClientCursorWrapper;
import com.bignerdranch.android.personaltrainerapp.clientdatabase.ClientDatabaseHelper;
import com.bignerdranch.android.personaltrainerapp.clientdatabase.ClientDBSchema.SessionTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionLab {

    private static SessionLab sSessionLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static SessionLab get(Context context) {
        if (sSessionLab == null) {
            sSessionLab = new SessionLab(context);
        }
        return sSessionLab;
    }

    private SessionLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ClientDatabaseHelper(mContext).getWritableDatabase();
    }

    public void addSession(Session session){
        ContentValues values = getContentValues(session);
        mDatabase.insert(SessionTable.NAME, null, values);
    }

    public void deleteSession(Session session) {
        ContentValues values = getContentValues(session);
        mDatabase.delete(SessionTable.NAME, SessionTable.Cols.SESSION_ID + " = ?", new String[]{session.getSessionID().toString()});
    }

    public void updateSession(Session session) {
        ContentValues values = getContentValues(session);
        mDatabase.update(SessionTable.NAME, values, SessionTable.Cols.SESSION_ID + " = ?", new String[]{session.getSessionID().toString()});
    }

    public List<Session> getSessions() {
        List<Session> sessions = new ArrayList<>();
        ClientCursorWrapper cursor = querySessions(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                sessions.add(cursor.getSession());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return sessions;
    }

    public Session getSession(UUID id) {
        ClientCursorWrapper cursor = querySessions(SessionTable.Cols.SESSION_ID + " = ?", new String[]{id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getSession();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Session session) {
        ContentValues values = new ContentValues();
        values.put(SessionTable.Cols.SESSION_ID, session.getSessionID().toString());
        values.put(SessionTable.Cols.CLIENT_ID, session.getClientID().toString());
        values.put(SessionTable.Cols.TITLE, session.getTitle());
        values.put(SessionTable.Cols.LOCATION, session.getLocation());
        values.put(SessionTable.Cols.DATE, session.getDate().getTime());
        values.put(SessionTable.Cols.WEIGHT, session.getWeight());
        return values;
    }

    public List<Session> searchSessions(UUID clientID) {
        List<Session> sessions = new ArrayList<>();
        ClientCursorWrapper cursor = querySessions(SessionTable.Cols.CLIENT_ID + " = ?", new String[]{clientID.toString()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                sessions.add(cursor.getSession());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return sessions;
    }

    private ClientCursorWrapper querySessions(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SessionTable.NAME,
                null, //columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                SessionTable.Cols.DATE  + " DESC"//orderBy
        );
        return new ClientCursorWrapper(cursor);
    }

    public File getPictureFile(Session session) {
        File externalFilesDir = new File(Environment.getExternalStorageDirectory(), session.getPictureFilename());
        if (externalFilesDir == null) {
            return null;
        } else {
            return externalFilesDir;
        }
    }
}
