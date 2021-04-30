package com.example.sipmobileapp.database;

public class SipMobileAppSchema {

    public static final String DB_NAME = "sipMobileApp.db";
    public static final int DB_VERSION = 1;

    public static final class ServerDataTable {
        public static final String NAME = "serverData_table";

        public static final class Cols {
            public static final String PRIMARY_KEY = "primaryKey";
            public static final String CENTER_NAME = "centerName";
            public static final String IP_ADDRESS = "ipAddress";
            public static final String PORT = "port";
        }
    }
}
