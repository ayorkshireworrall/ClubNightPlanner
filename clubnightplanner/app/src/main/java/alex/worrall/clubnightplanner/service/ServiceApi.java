package alex.worrall.clubnightplanner.service;

import android.content.Context;

import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.persistence.models.courtname.CourtName;
import alex.worrall.clubnightplanner.service.DataHolder.DatabaseAction;
import alex.worrall.clubnightplanner.persistence.models.fixture.Fixture;
import alex.worrall.clubnightplanner.persistence.models.player.Player;

/**
 * Should define how the rest of the application interacts with this service layer because it got
 * a bit too messy and intertwined. This ought to prevent wrong usages of adds etc.
 */
public class ServiceApi {
    private DataHolder dataHolder;
    private Scheduler scheduler;
    private static ServiceApi instance;
    private static Context context;

    private ServiceApi(Context context) {
        dataHolder = DataHolder.getInstance(context);
        scheduler = Scheduler.getInstance(context);
//        addDemoData(true, true, true);
    }

    public static ServiceApi getInstance() {
        return instance;
    }

    public static ServiceApi getInstance(Context context) {
        if (instance == null) {
            instance = new ServiceApi(context);
        }
        return instance;
    }

    public void addPlayer(Player player) {
        scheduler.addPlayer(player.getName(), player.getLevel());
    }

    public void addPlayer(String name, int level) {
        scheduler.addPlayer(name, level);
    }

    public void removePlayer(String playerId) {
        scheduler.removePlayer(playerId);
    }

    public boolean isPlayerNameUsed(String name) {
        return dataHolder.isPlayerNameUsed(name);
    }

    public boolean isPlayerNameUsed(String name, String uuid) {
        return dataHolder.isPlayerNameUsed(name, uuid);
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

    public void clearPlayers() {
        scheduler.clearPlayers();
    }

    public List<CourtName> getAvailableCourts() {
        return dataHolder.getAvailableCourts();
    }

    public void addCourt(String courtName) {
        dataHolder.addCourt(courtName);
    }

    public void removeCourt(CourtName courtName) {
        scheduler.disableCourt(courtName);
        dataHolder.removeCourt(courtName);
    }

    public void disableCourt(CourtName courtName) {
        scheduler.disableCourt(courtName);
    }

    public void clearCourts() {
        scheduler.clearCourts();
    }

    public void addFixture(int timeSlot, List<CourtName> courts) {
        scheduler.generateSchedule(timeSlot, courts);
    }

    public Map<Integer, Fixture> getFixtures() {
        return dataHolder.getFixtures();
    }

    public void markFixtureComplete(Fixture fixture) {
        System.out.println("markFixtureComplete Called correctly");
        scheduler.markScheduleComplete(fixture);
    }

    public void startFixture(Fixture fixture) {
        System.out.println("startFixture Called correctly");
        scheduler.startFixture(fixture);
    }

    public void removeFixture(Fixture fixture) {
        scheduler.removeFixture(fixture);
    }

    public List<Fixture> getOrderedFixtures() {
        return dataHolder.getOrderedFixtures();
    }

    public void clearFixtures() {
        scheduler.clearFixtures();
    }

    public void clearData() {
        dataHolder.clearData();
    }

    //purely for test purposes
    private void addDemoData(boolean players, boolean courts, boolean fixtures) {
        getPlayers();
        getAvailableCourts();
        getFixtures();
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
