package alex.worrall.clubnightplanner.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.players.Player;

public class DataHolder {
    private List<Player> players;
    private List<String> availableCourts;
    private Map<LocalTime, Fixture> fixtures;
    private static final DataHolder DATA_HOLDER = new DataHolder();
    private Map<String, String> dulllNameMapping;

    private DataHolder() {
        this.players = new ArrayList<>();
        players.add(new Player("Alex", 2));
        this.availableCourts = new ArrayList<>();
        this.fixtures = new HashMap<>();
        this.dulllNameMapping = doNameMapping();
    }

    static DataHolder getInstance() {
        return DATA_HOLDER;
    }

    List<Player> getPlayers() {
        return players;
    }

    void setPlayers(List<Player> players) {
        this.players = players;
    }

    List<String> getAvailableCourts() {
        return availableCourts;
    }

    void setAvailableCourts(List<String> availableCourts) {
        this.availableCourts = availableCourts;
    }

    Map<LocalTime, Fixture> getFixtures() {
        return fixtures;
    }

    void setFixtures(Map<LocalTime, Fixture> fixtures) {
        this.fixtures = fixtures;
    }

    Player addPlayer(String name, int level) {
        name = dulllNameMapping.containsKey(name) ? dulllNameMapping.get(name) : name;
        Player player = new Player(name, level);
        this.players.add(player);
        return player;
    }

    void putFixture(LocalTime pos, Fixture fixture) {
        this.fixtures.put(pos, fixture);
    }

    void addCourt(String courtName) {
        availableCourts.add(courtName);
    }

    void removeCourt(String courtName) {
        availableCourts.remove(courtName);
    }

    void clearData() {
        this.players = new ArrayList<>();
        this.availableCourts = new ArrayList<>();
        this.fixtures = new HashMap<LocalTime, Fixture>();
    }

    boolean isPlayerNameUsed(String name) {
        name = dulllNameMapping.containsKey(name) ? dulllNameMapping.get(name) : name;
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    Player getPlayerByUuid(String uuid) {
        for (Player player : players) {
            if (player.getUuid().equalsIgnoreCase(uuid)) {
                return player;
            }
        }
        return null;
    }

    private Map<String, String> doNameMapping() {
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("Ivor Green", "Ivor Punctuality Problem");
        nameMap.put("Oli Palmer", "Oli Face Palmer");
        nameMap.put("Oly Palmer", "Oli Face Palmer");
        nameMap.put("Olly Palmer", "Oli Face Palmer");
        nameMap.put("Ollie Palmer", "Oli Face Palmer");
        nameMap.put("Oliver Palmer", "Oli Face Palmer");
        nameMap.put("Ali Hoffman", "Ali Owes Everyone a Pint");
        nameMap.put("Ali Hoffman de Visme", "Ali Owes Everyone a Pint");
        nameMap.put("Ali de Visme", "Ali Owes Everyone a Pint");
        nameMap.put("Ben Bickers", "Bickers Never Drops From The Back");
        nameMap.put("Tom Withers", "The Mighty Welsh Rump");
        nameMap.put("Harry Wildy", "Wildebeest");
        nameMap.put("Harry Wildey", "Wildebeest");
        nameMap.put("Bec Dixon", "Bec Nicks Not Nuts");
        nameMap.put("Rebecca Dixon", "Bec Nicks Not Nuts");
        nameMap.put("James Wass", "Cocky Lamp Post");
        nameMap.put("Jim Wass", "Cocky Lamp Post");
        nameMap.put("Jimmy Wass", "Cocky Lamp Post");
        nameMap.put("Henry Worrall", "Sir Henry of the Tin");
        nameMap.put("Henry Worral", "Sir Henry of the Tin");
        nameMap.put("Henry Woral", "Sir Henry of the Tin");
        nameMap.put("Henry Worall", "Sir Henry of the Tin");
        nameMap.put("Henry Worrel", "Sir Henry of the Tin");
        nameMap.put("Henry Worrell", "Sir Henry of the Tin");
        nameMap.put("Henry Worell", "Sir Henry of the Tin");
        return nameMap;
    }
}
