package de.metopia.lib.data.models.player;

import dev.morphia.annotations.Entity;

@Entity
public class PlayerInfo {

    private String name;
    private String uuid;

    public PlayerInfo(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
}
