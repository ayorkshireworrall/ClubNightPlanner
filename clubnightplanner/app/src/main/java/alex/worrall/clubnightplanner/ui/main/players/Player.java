package alex.worrall.clubnightplanner.ui.main.players;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {
    private int level;
    private String name;
    private String uuid;
    private List<Player> opponentsPlayed;

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
        this.opponentsPlayed = new ArrayList<Player>();
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

    public List<Player> getOpponentsPlayed() {
        return opponentsPlayed;
    }

    public void setOpponentsPlayed(List<Player> opponentsPlayed) {
        this.opponentsPlayed = opponentsPlayed;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " (" + level + ")";
    }
}

