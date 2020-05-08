package alex.worrall.clubnightplanner.persistence.models.player;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "players")
public class Player {
    @PrimaryKey
    @NonNull
    private String uuid;
    @ColumnInfo(name = "level")
    private int level;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "opponents_played")
    private List<String> opponentsPlayed;
    @ColumnInfo(name = "session_id")
    private int sesiondId;

    //Not persisted field
    private int scheduleRanking;

    /**
     * Create a player with a name and league level, generate them a uuid and initialise an empty opponent list
     * @param name player's full name
     * @param level box league level (or ranking equivalent)
     */
    public Player(String name, int level) {
        this.level = level;
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
        this.opponentsPlayed = new ArrayList<String>();
        this.sesiondId = 0;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getOpponentsPlayed() {
        return opponentsPlayed;
    }

    public void setOpponentsPlayed(List<String> opponentsPlayed) {
        this.opponentsPlayed = opponentsPlayed;
    }

    public int getSesiondId() {
        return sesiondId;
    }

    public void setSesiondId(int sesiondId) {
        this.sesiondId = sesiondId;
    }

    public int getScheduleRanking() {
        return scheduleRanking;
    }

    public void setScheduleRanking(int scheduleRanking) {
        this.scheduleRanking = scheduleRanking;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " (" + level + ")";
    }
}

