package alex.worrall.clubnightplanner.persistence.models.player;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {
    private int level;
    private String name;
    private String uuid;
    private List<String> opponentsPlayed;
    private int scheduleRanking;

    public Player() {
    }

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

