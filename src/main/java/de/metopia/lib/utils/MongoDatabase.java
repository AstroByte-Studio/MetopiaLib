package de.metopia.lib.utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.metopia.lib.data.LibConfig;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;

public class MongoDatabase {

    private static List<MongoDatabaseInitHandler> initHandlers = new ArrayList<>();
    private static Datastore datastore;

    public static Datastore getDatastore() {
        return datastore;
    }

    public static void addInitHandler(MongoDatabaseInitHandler handler) {
        initHandlers.add(handler);
    }

    public static void init() {
        MongoClient client = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(LibConfig.getMongoURI()))
                        .applyToConnectionPoolSettings(builder ->
                                builder.minSize(5)
                                .maxSize(20)
                                .build())
                        .build());

        datastore = Morphia.createDatastore(client, LibConfig.getDatabase());

        initHandlers.forEach(handler -> handler.handleDatabaseInit(datastore));


        System.out.println("[MetopiaLib] Database connected! Database: " + datastore.getDatabase().getName());


    }

}

