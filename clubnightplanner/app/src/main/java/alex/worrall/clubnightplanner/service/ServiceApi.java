package alex.worrall.clubnightplanner.service;

import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.ui.main.courts.Court;
import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.players.Player;

/**
 * Should define how the rest of the application interacts with this service layer because it got
 * a bit too messy and intertwined. This ought to prevent wrong usages of adds etc.
 */
public class ServiceApi {
    private DataHolder dataHolder = DataHolder.getInstance();
    private Scheduler scheduler = new Scheduler();

    public void addPlayer(Player player) {
        scheduler.addPlayer(player.getName(), player.getLevel());
    }

    public void addPlayer(String name, int level) {
        scheduler.addPlayer(name, level);
    }

    public void addCourt(Court court) {
        dataHolder.addCourt(court);
    }

    public void addCourt(String name, Player playerA, Player playerB) {
        dataHolder.addCourt(new Court(name, playerA, playerB));
    }

    public void addFixture(int pos, List<String> courts) {
        scheduler.generateSchedule(pos, courts);
    }

    public List<Player> getPlayers() {
        return dataHolder.getPlayers();
    }

    public List<Court> getCourts() {
        return dataHolder.getCourts();
    }

    public Map<Integer, Fixture> getFixtures() {
        return dataHolder.getFixtures();
    }

    public void clearData() {
        dataHolder.clearData();
    }

    public void removeCourt(Court court) {
        //TODO implications with scheduling
    }

    public void removePlayer(String playerId) {
        scheduler.removePlayer(playerId);
    }

    public void markFixtureComplete(Fixture fixture) {
        fixture.setPlayed(true);
    }

    public void removeFixture(Fixture fixture) {
        scheduler.unschedule(fixture);
    }
}
