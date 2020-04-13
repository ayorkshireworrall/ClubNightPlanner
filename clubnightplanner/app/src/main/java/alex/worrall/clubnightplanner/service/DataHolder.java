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
    private Map<Integer, Fixture> fixtures;
    private static final DataHolder DATA_HOLDER = new DataHolder();
    private Map<String, String> dulllNameMapping;

    private DataHolder() {
        this.players = new ArrayList<>();
        this.availableCourts = new ArrayList<>();
        this.fixtures = new HashMap<>();
        this.dulllNameMapping = doNameMapping();
        availableCourts.add("Court 1");
        availableCourts.add("Court 2");
        availableCourts.add("Court 3");
        players.add(new Player("Alex", 1));
        players.add(new Player("Matt", 2));
        players.add(new Player("Chris", 3));
        players.add(new Player("John", 4));
        players.add(new Player("Henry", 1));
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

    Map<Integer, Fixture> getFixtures() {
        return fixtures;
    }

    void setFixtures(Map<Integer, Fixture> fixtures) {
        this.fixtures = fixtures;
    }

    Player addPlayer(String name, int level) {
        name = dulllNameMapping.containsKey(name) ? dulllNameMapping.get(name) : name;
        Player player = new Player(name, level);
        this.players.add(player);
        return player;
    }

    void updatePlayer(Player updatedPlayer) {
        for (Player player : players) {
            if (player.getUuid().equals(updatedPlayer.getUuid())) {
                String requestedName = updatedPlayer.getName();
                String rename = dulllNameMapping.containsKey(requestedName) ?
                        dulllNameMapping.get(requestedName) : requestedName;
                player.setLevel(updatedPlayer.getLevel());
                player.setName(rename);
            }
        }
    }

    void putFixture(Integer pos, Fixture fixture) {
        if (fixtures.isEmpty()) {
            fixture.setPlayStatus(Status.NEXT);
        }
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
        this.fixtures = new HashMap<Integer, Fixture>();
    }

    boolean isPlayerNameUsed(String name) {
        return isPlayerNameUsed(name, "");
    }

    boolean isPlayerNameUsed(String name, String uuid) {
        name = dulllNameMapping.containsKey(name) ? dulllNameMapping.get(name) : name;
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name) && !player.getUuid().equals(uuid)) {
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

    List<Fixture> getOrderedFixtures() {
        List<Fixture> orderedFixtures = new ArrayList<>();
        int currentLargestInList = 0;
        for (int i = 0; i < fixtures.size(); i++) {
            int workingSmallest = 1439; //11:59
            for (int time : fixtures.keySet()) {
                if (time <= currentLargestInList) {
                    //Fixture already added to ordered list
                    continue;
                }
                if (time < workingSmallest) {
                    workingSmallest = time;
                }
            }
            currentLargestInList = workingSmallest;
            orderedFixtures.add(fixtures.get(workingSmallest));
        }
        return orderedFixtures;
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
