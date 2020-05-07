package alex.worrall.clubnightplanner.service;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.persistence.PlannerDatabase;
import alex.worrall.clubnightplanner.persistence.models.CourtName;
import alex.worrall.clubnightplanner.persistence.models.CourtNameDao;
import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.players.Player;

public class DataHolder {
    private List<Player> players;
    private List<CourtName> availableCourts;
    private Map<Integer, Fixture> fixtures;
    private static DataHolder instance;
    private Map<String, String> dulllNameMapping;
    private PlannerDatabase database;

    private DataHolder(Context context) {
        this.players = new ArrayList<>();
        this.fixtures = new HashMap<>();
        this.dulllNameMapping = doNameMapping();
        database = PlannerDatabase.getInstance(context);
    }

    static DataHolder getInstance(Context context) {
        if (instance == null) {
            instance = new DataHolder(context);
        }
        return instance;
    }

    List<Player> getPlayers() {
        return players;
    }

    void setPlayers(List<Player> players) {
        this.players = players;
    }

    List<CourtName> getAvailableCourts() {
        if (availableCourts == null) {
            availableCourts = database.courtNamesDao().getCourtNameList();
        }
        return availableCourts;
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
        this.fixtures.put(pos, fixture);
    }
    void removeFixture(Fixture fixture) {
        fixtures.remove(fixture.getTimeSlot());
    }

    void addCourt(String name) {
        CourtName courtName = new CourtName(name);
        availableCourts.add(courtName);
        modifyCourtList(DatabaseAction.INSERT, courtName);
    }

    void removeCourt(CourtName courtName) {
        availableCourts.remove(courtName);
    }

    void clearData() {
        this.players = new ArrayList<>();
        this.availableCourts = new ArrayList<>();
        modifyCourtList(DatabaseAction.DELETE_ALL, null);
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


    //Asynchronously modifies the data saved in the court list
    void modifyCourtList(final DatabaseAction action, @Nullable final CourtName courtName) {
        final CourtNameDao dao = database.courtNamesDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (action) {
                    case INSERT:
                        dao.insertCourtName(courtName);
                        break;
                    case DELETE_ONE:
                        dao.deleteCourtName(courtName);
                        break;
                    case DELETE_ALL:
                        List<CourtName> availableCourts = dao.getCourtNameList();
                        for (CourtName c : availableCourts) {
                            if (c.getSessionId() == 0) {
                                dao.deleteCourtName(c);
                            }
                        }
                        break;
                }

            }
        }).start();
    }

    enum DatabaseAction {
        INSERT,
        DELETE_ONE,
        DELETE_ALL
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
