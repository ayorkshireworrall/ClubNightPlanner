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
        dataHolder.updatePlayer(player);
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

    public void removeFixture(Fixture fixture) {
        scheduler.removeFixture(fixture);
    }

    public List<Fixture> getOrderedFixtures() {
        return dataHolder.getOrderedFixtures();
    }
}
