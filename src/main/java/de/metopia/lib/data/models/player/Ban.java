package de.metopia.lib.data.models.player;

import de.metopia.lib.data.models.player.impl.Punishment;
import de.metopia.lib.data.models.player.impl.PunishmentType;
import dev.morphia.annotations.Entity;

@Entity
public class Ban implements Punishment {

    private PlayerInfo issuedBy;
    private String reason;
    private long duration;

    private PlayerInfo revokedBy;
    private String revokedReason;
    private long revokedAt;

    public Ban(PlayerInfo issuedBy, String reason, long duration) {
        this.issuedBy = issuedBy;
        this.reason = reason;
        this.duration = duration;
    }

    @Override
    public PunishmentType getType() {
        return PunishmentType.BAN;
    }

    @Override
    public PlayerInfo getIssuedBy() {
        return this.issuedBy;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public long getCreatedAt() {
        return System.currentTimeMillis();
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @Override
    public boolean isPermanent() {
        return this.duration== Long.MAX_VALUE;
    }

    @Override
    public PlayerInfo getRevokedBy() {
        return this.revokedBy;
    }

    @Override
    public void setRevoked(PlayerInfo revokedBy, String revokedReason) {
        this.revokedBy = revokedBy;
        this.revokedReason = revokedReason;
        this.revokedAt = System.currentTimeMillis();
    }

    @Override
    public boolean isRevoked() {
        return this.revokedBy != null;
    }
}
