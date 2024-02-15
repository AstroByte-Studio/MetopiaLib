package de.metopia.lib.data.models.player.impl;

import de.metopia.lib.data.models.player.PlayerInfo;
import dev.morphia.annotations.Entity;

@Entity
public interface Punishment {

    PunishmentType getType();
    PlayerInfo getIssuedBy();
    String getReason();

    long getCreatedAt();
    long getDuration();
    boolean isPermanent();

    PlayerInfo getRevokedBy();
    void setRevoked(PlayerInfo revokedBy, String revokedReason);
    boolean isRevoked();

}
