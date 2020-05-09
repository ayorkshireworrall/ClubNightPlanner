package alex.worrall.clubnightplanner.service;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.persistence.PlannerDatabase;
import alex.worrall.clubnightplanner.persistence.models.courtname.CourtName;
import alex.worrall.clubnightplanner.persistence.models.courtname.CourtNameDao;
import alex.worrall.clubnightplanner.persistence.models.courtname.CourtNameRepository;
import alex.worrall.clubnightplanner.persistence.models.fixture.FixtureDao;
import alex.worrall.clubnightplanner.persistence.models.player.PlayerDao;
import alex.worrall.clubnightplanner.persistence.models.fixture.Fixture;
import alex.worrall.clubnightplanner.persistence.models.player.Player;

public class DataHolder {
    private List<Player> players;
    private List<CourtName> availableCourts;
    private Map<Integer, Fixture> fixtures;
    private static DataHolder instance;
    private Map<String, String> dulllNameMapping;
    private PlannerDatabase database;
    private CourtNameRepository courtNameRepository;

    private DataHolder(Context context) {
        this.dulllNameMapping = doNameMapping();
        this.courtNameRepository = new CourtNameRepository(context);
        database = PlannerDatabase.getInstance(context);
    }

    static DataHolder getInstance(Context context) {
        if (instance == null) {
            instance = new DataHolder(context);
        }
        return instance;
    }

    List<Player> getPlayers() {
        if (players == null) {
            players = database.playerDao().getPlayerList();
        }
        return players;
    }

    void setPlayers(List<Player> players) {
        this.players = players;
    }

    List<CourtName> getAvailableCourts() {
        if (availableCourts == null) {
            availableCourts = courtNameRepository.getCourtNames();
        }
        return availableCourts;
    }

    Map<Integer, Fixture> getFixtures() {
        if (fixtures == null) {
            List<Fixture> fixtureList = database.fixtureDao().getFixtureList();
            Map<Integer, Fixture> fixtureMap = new HashMap<>();
            for (Fixture fixture : fixtureList) {
                fixtureMap.put(fixture.getTimeSlot(), fixture);
            }
            fixtures = fixtureMap;
        }
        return fixtures;
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
        courtNameRepository.insertCourtName(courtName);
    }

    void removeCourt(CourtName courtName) {
        availableCourts.remove(courtName);
        courtNameRepository.deleteCourtName(courtName);
    }

    void removeSessionCourts(int sessionId) {
        availableCourts.clear();
        courtNameRepository.deleteBySessionId(sessionId);
    }

    void clearData() {
        this.players = new ArrayList<>();
        modifyPlayerList(DatabaseAction.DELETE_ALL, null);
        this.availableCourts = new ArrayList<>();
        courtNameRepository.deleteBySessionId(0);
        this.fixtures = new HashMap<Integer, Fixture>();
        modifyFixtureList(DatabaseAction.DELETE_ALL, null);
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
        if (fixtures == null) {
            getFixtures();
        }
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

    //Asynchronously modifies the data saved in the player list
    void modifyPlayerList(final DatabaseAction action, @Nullable final Player player) {
        final PlayerDao dao = database.playerDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (action) {
                    case INSERT:
                        dao.insertPlayer(player);
                        break;
                    case UPDATE:
                        dao.updatePlayer(player);
                        break;
                    case DELETE_ONE:
                        dao.deletePlayer(player);
                        break;
                    case DELETE_ALL:
                        List<Player> players = dao.getPlayerList();
                        for (Player p : players) {
                            if (p.getSesiondId() == 0) {
                                dao.deletePlayer(p);
                            }
                        }
                        break;
                }
            }
        }).start();
    }

    //Asynchronously modifies the data saved in the fixture list
    synchronized void modifyFixtureList(final DatabaseAction action, @Nullable final Fixture fixture) {
        final FixtureDao dao = database.fixtureDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (action) {
                    case INSERT:
                        dao.insertFixture(fixture);
                        break;
                    case UPDATE:
                        dao.updateFixture(fixture);
                        break;
                    case DELETE_ONE:
                        dao.deleteFixture(fixture);
                        break;
                    case DELETE_ALL:
                        List<Fixture> fixtures = dao.getFixtureList();
                        for (Fixture f : fixtures) {
                            if (f.getSessionId() == 0) {
                                dao.deleteFixture(f);
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
        DELETE_ALL,
        UPDATE
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
        nameMap.put("Ben Bryant", "Bryant's Tower of Power");
        return nameMap;
    }
}
