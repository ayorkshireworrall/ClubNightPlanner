package alex.worrall.clubnightplanner.service;

import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.players.Player;

/**
 * Should define how the rest of the application interacts with this service layer because it got
 * a bit too messy and intertwined. This ought to prevent wrong usages of adds etc.
 */
public class ServiceApi {
    private DataHolder dataHolder = DataHolder.getInstance();
    private Scheduler scheduler = new Scheduler();
    private static ServiceApi serviceApi = new ServiceApi();

    private ServiceApi() {
//        addDemoData(true, true, true);
    }

    public static ServiceApi getInstance() {
        if (serviceApi == null) {
            serviceApi = new ServiceApi();
        }
        return serviceApi;
    }

    public void addPlayer(Player player) {
        scheduler.addPlayer(player.getName(), player.getLevel());
    }

    public void addPlayer(String name, int level) {
        scheduler.addPlayer(name, level);
    }

    public boolean isPlayerNameUsed(String name) {
        return dataHolder.isPlayerNameUsed(name);
    }

    public boolean isPlayerNameUsed(String name, String uuid) {
        return dataHolder.isPlayerNameUsed(name, uuid);
    }

    public void addCourt(String courtName) {
        dataHolder.addCourt(courtName);
    }

    public void addFixture(int timeSlot, List<String> courts) {
        scheduler.generateSchedule(timeSlot, courts);
    }

    public Player getPlayerByUuid(String uuid) {
        return dataHolder.getPlayerByUuid(uuid);
    }

    public void updatePlayer(Player player) {
        scheduler.updatePlayer(player);
    }

    public List<Player> getPlayers() {
        return dataHolder.getPlayers();
    }

    public List<String> getAvailableCourts() {
        return dataHolder.getAvailableCourts();
    }

    public Map<Integer, Fixture> getFixtures() {
        return dataHolder.getFixtures();
    }

    public void clearData() {
        dataHolder.clearData();
    }

    public void removeCourt(String courtName) {
        scheduler.disableCourt(courtName);
        dataHolder.removeCourt(courtName);
    }

    public void disableCourt(String courtName) {
        scheduler.disableCourt(courtName);
    }

    public void removePlayer(String playerId) {
        scheduler.removePlayer(playerId);
    }

    public void markFixtureComplete(Fixture fixture) {
        scheduler.markScheduleComplete(fixture);
    }

    public void startFixture(Fixture fixture) {
        scheduler.startFixture(fixture);
    }

    public void removeFixture(Fixture fixture) {
        scheduler.removeFixture(fixture);
    }

    public List<Fixture> getOrderedFixtures() {
        return dataHolder.getOrderedFixtures();
    }

    //purely for test purposes
    private void addDemoData(boolean players, boolean courts, boolean fixtures) {
        if (players) {
            addPlayer("Alex Worrall", 5);
            addPlayer("Amy Sanchez", 4);
            addPlayer("James Wass", 1);
            addPlayer("Ivor Green", 5);
            addPlayer("Ali Hoffman", 3);
            addPlayer("Heather Taylor", 15);
            addPlayer("Bob Marshall", 20);
            addPlayer("Adrian Forest", 17);
            addPlayer("Mike Sutcliffe", 3);
            addPlayer("Helen Jones", 10);
            addPlayer("Edgar Mallard", 8);
            addPlayer("Phillipa Ingram", 2);
            addPlayer("Bec Dixon", 7);
            addPlayer("Tom Withers", 6);
            addPlayer("Matt Haythornthwaite", 17);
            addPlayer("Amy Owens", 5);
            addPlayer("Paul Munch", 22);
            addPlayer("John Worrall", 18);
        }
        if (courts) {
            addCourt("Court 1");
            addCourt("Court 2");
            addCourt("Court 3");
            addCourt("Court 4");
            addCourt("Court 5");
            addCourt("Court 6");
            addCourt("Court 7");
        }

        if (fixtures) {
            addFixture(1170, dataHolder.getAvailableCourts());
            addFixture(1190, dataHolder.getAvailableCourts());
            addFixture(1210, dataHolder.getAvailableCourts());
            addFixture(1230, dataHolder.getAvailableCourts());
        }
    }
}
