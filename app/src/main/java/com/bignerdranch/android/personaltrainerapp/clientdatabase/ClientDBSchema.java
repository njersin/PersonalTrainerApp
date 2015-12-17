package com.bignerdranch.android.personaltrainerapp.clientdatabase;

public class ClientDBSchema {

    public static final class ClientTable {
        public static final String NAME = "client";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String EMAIL_ADDRESS = "emailaddress";
            public static final String CREATE_DATE = "createdate";
        }
    }

    public static final class SessionTable {
        public static final String NAME = "session";

        public static final class Cols {
            public static final String SESSION_ID = "sessionid";
            public static final String CLIENT_ID = "clientid";
            public static final String TITLE = "title";
            public static final String LOCATION = "location";
            public static final String DATE = "date";
            public static final String WEIGHT = "weight";
        }
    }
}
