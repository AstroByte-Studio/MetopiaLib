package de.metopia.lib.data;

public class LibConfig {

    private static final String HOST = "127.0.0.1";
    private static final String DATABASE = "metopia";

    private static final int MONGO_PORT = 27017;
    private static final int REDIS_PORT = 6379;

    private static final String AUTH_USER = "admin";
    private static final String AUTH_PASS = "admin";

    public static String getMongoURI() {
        return String.format("mongodb://%s:%s@%s:%s/%s?authSource=admin&w=1", AUTH_USER, AUTH_PASS, HOST, MONGO_PORT, DATABASE);
    }

    public static String getRedisURI() {
        return String.format("redis://:%s@%s:%s", AUTH_PASS, HOST, REDIS_PORT);
    }

    public static String getDatabase() {
        return DATABASE;
    }

}
