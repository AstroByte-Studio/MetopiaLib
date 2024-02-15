package de.metopia.lib.data.models.player;

import dev.morphia.annotations.Entity;

@Entity
public class Report {

    private PlayerInfo issuedBy;
    private String reason;

    private long createdAt;

    private PlayerInfo finishedBy;

    public Report(PlayerInfo issuedBy, String reason) {
        this.issuedBy = issuedBy;
        this.reason = reason;
        this.createdAt = System.currentTimeMillis();
    }

    public void finish(PlayerInfo finishedBy) {
        this.finishedBy = finishedBy;
    }

    public PlayerInfo getIssuedBy() {
        return issuedBy;
    }

    public String getReason() {
        return reason;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public PlayerInfo getFinishedBy() {
        return finishedBy;
    }

    public boolean isFinished() {
        return finishedBy!=null;
    }
}
