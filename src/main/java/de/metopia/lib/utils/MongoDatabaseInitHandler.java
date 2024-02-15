package de.metopia.lib.utils;

import dev.morphia.Datastore;

public interface MongoDatabaseInitHandler {
    void handleDatabaseInit(Datastore datastore);
}
