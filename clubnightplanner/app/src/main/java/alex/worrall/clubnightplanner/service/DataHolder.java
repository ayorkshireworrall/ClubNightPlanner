package alex.worrall.clubnightplanner.service;

import java.util.ArrayList;
import java.util.List;

import alex.worrall.clubnightplanner.ui.main.courts.Court;
import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.players.Player;

public class DataHolder {
    private List<Player> players;
    private List<Court> courts;
    private List<Fixture> fixtures;
    private static final DataHolder DATA_HOLDER = new DataHolder();

    private DataHolder() {
        this.players = new ArrayList<>();
        this.courts = new ArrayList<>();
        this.fixtures = new ArrayList<>();
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

    public List<Fixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<Fixture> fixtures) {
        this.fixtures = fixtures;
    }

    public void clearData() {
        this.players = new ArrayList<>();
        this.courts = new ArrayList<>();
        this.fixtures = new ArrayList<>();
    }
}
