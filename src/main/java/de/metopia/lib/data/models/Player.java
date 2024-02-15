package de.metopia.lib.data.models;

import de.metopia.lib.data.Cachable;
import de.metopia.lib.data.constants.CacheConstants;
import de.metopia.lib.data.models.player.*;
import de.metopia.lib.data.models.player.impl.Punishment;
import de.metopia.lib.utils.MongoDatabase;
import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Entity("players")
@Indexes({
        @Index(options = @IndexOptions(name = "playerInfo.name"), fields = { @Field("playerInfo.name") }),
        @Index(options = @IndexOptions(name = "playerInfo.uuid"), fields = { @Field("playerInfo.uuid") })
})
public class Player implements Cachable {

    @Id
    private ObjectId id;

    /* Player */
    private PlayerInfo playerInfo;
    private int money;
    private long playtimeMinutes;

    private Ban ban;
    private Mute mute;
    private List<Punishment> punishments;
    private List<Report> reports;


    /* Utils */
    private boolean dsgvo;
    private long firstLogin;


    /* Team / Admin */
    private boolean loggedInTeam;



    /* Methods */

    public Player(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        this.money = 0;
        //ban
        //mute
        this.punishments = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.dsgvo = false;
        this.firstLogin = System.currentTimeMillis();
        this.loggedInTeam = false;

        //NOTICE: INSERT BY YOURSELF!
    }

    public Ban ban(PlayerInfo issuedBy, String reason, long duration) {
        Ban newBan = new Ban(issuedBy, reason, duration);

        this.ban = newBan;
        this.punishments.add(newBan);
        save();

        return newBan;
    }

    public Mute mute(PlayerInfo issuedBy, String reason, long duration) {
        Mute newMute = new Mute(issuedBy, reason, duration);

        this.mute = newMute;
        this.punishments.add(newMute);
        save();

        return newMute;
    }

    public Warn warn(PlayerInfo issuedBy, String reason) {
        Warn warn = new Warn(issuedBy, reason);

        this.punishments.add(warn);
        save();

        return warn;
    }

    public Report report(PlayerInfo reportedBy, String reason) {
        Report newReport = new Report(reportedBy, reason);

        this.reports.add(newReport);
        save();

        return newReport;
    }

    public Ban reduceBan(PlayerInfo issuedBy, String reason, long newDuration) {
        if(this.ban == null) return null;

        this.ban.setRevoked(issuedBy, reason);
        save();

        return this.ban;
    }

    public Mute reduceMute(PlayerInfo issuedBy, String reason, long newDuration) {
        if(this.mute == null) return null;

        this.mute.setRevoked(issuedBy, reason);
        save();

        return this.mute;

    }

    public double addMoney(int amount) {
        this.money += amount;
        save();

        return this.money;
    }

    public double removeMoney(int amount) {
        this.money -= amount;
        save();

        return this.money;
    }

    public double setMoney(int amount) {
        this.money = amount;
        save();

        return this.money;
    }

    public boolean hasMoney(int amount) {
        return this.money>=amount;
    }


    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        save();
    }

    public void increasePlaytime(int minutes) {
        this.playtimeMinutes += minutes;
        save();
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public double getMoney() {
        return money;
    }

    public Ban getBan() {
        return ban;
    }

    public Mute getMute() {
        return mute;
    }

    public boolean isBanned() {
        return ban!=null;
    }

    public boolean isMuted() {
        return mute!=null;
    }

    public List<Punishment> getPunishments() {
        return punishments;
    }

    public List<Report> getReports() {
        return reports;
    }

    public boolean isDSGVOAccepted() {
        return dsgvo;
    }

    public void acceptDSGVO() {
        this.dsgvo = true;
    }

    public boolean isLoggedInTeam() {
        return loggedInTeam;
    }

    public void setLoggedInTeam(boolean loggedInTeam) {
        this.loggedInTeam = loggedInTeam;
    }

    public long getPlaytimeMinutes() {
        return playtimeMinutes;
    }

    public String getPlaytimeStr() {
        int hours = (int) (playtimeMinutes / 60);
        int minutes = (int) (playtimeMinutes % 60);
        return hours + " Stunde(n), " + minutes + " Minute(n)";
    }

    @Override
    public String getCachePrefix() {
        return CacheConstants.PLAYER;
    }

    @Override
    public String getCacheId() {
        return this.playerInfo.getUUID();
    }

    public void save() {
        MongoDatabase.getDatastore().save(this);
    }
}
