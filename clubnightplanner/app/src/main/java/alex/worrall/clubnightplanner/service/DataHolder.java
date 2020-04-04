package alex.worrall.clubnightplanner.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.ui.main.courts.Court;
import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.players.Player;

public class DataHolder {
    private List<Player> players;
    private List<Court> courts;
    private Map<Integer, Fixture> fixtures;
    private static final DataHolder DATA_HOLDER = new DataHolder();

    private DataHolder() {
        this.players = new ArrayList<>();
        this.courts = new ArrayList<>();
        this.fixtures = new HashMap<>();
    }

    public static DataHolder getInstance() {
        return DATA_HOLDER;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Court> getCourts() {
        return courts;
    }

    public void setCourts(List<Court> courts) {
        this.courts = courts;
    }

    public Map<Integer, Fixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(Map<Integer, Fixture> fixtures) {
        this.fixtures = fixtures;
    }

    //package private because should be added through the scheduler
    void addPlayer(Player player) {
        this.players.add(player);
    }

    //package private because should be added through the scheduler
    void putFixture(int pos, Fixture fixture) {
        this.fixtures.put(pos, fixture);
    }

    void addCourt(Court court) {
        courts.add(court);
    }

    public void clearData() {
        this.players = new ArrayList<>();
        this.courts = new ArrayList<>();
        this.fixtures = new HashMap<Integer, Fixture>();
    }
}
