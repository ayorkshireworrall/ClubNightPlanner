package alex.worrall.clubnightplanner.service;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.service.DataHolder;
import alex.worrall.clubnightplanner.service.Scheduler;
import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.players.Player;

import static junit.framework.TestCase.assertTrue;

public class SchedulerTest {
    Scheduler scheduler = new Scheduler();
    DataHolder dataHolder = DataHolder.getInstance();

    @Before
    public void setup() {
        //Add players
        scheduler.addPlayer("Henry", 1);
        scheduler.addPlayer("James", 1);
        scheduler.addPlayer("Joe", 1);
        scheduler.addPlayer("Tom", 2);
        scheduler.addPlayer("Andrew", 2);
        scheduler.addPlayer("Ali", 3);
        scheduler.addPlayer("Alex", 5);
        scheduler.addPlayer("Ivor", 5);
        scheduler.addPlayer("Jim", 7);
        scheduler.addPlayer("Amran", 8);
        scheduler.addPlayer("Brad", 8);
        scheduler.addPlayer("John Paul", 11);
        scheduler.addPlayer("Bob", 12);
        scheduler.addPlayer("Geoff", 13);
        scheduler.addPlayer("Dave", 15);
        scheduler.addPlayer("Jeff", 16);
        scheduler.addPlayer("Chris", 18);
        scheduler.addPlayer("Matt", 20);
        scheduler.addPlayer("John", 22);
    }

    @Test
    public void testGenerateSchedule() {
        List<String> courtNames = new ArrayList<>();
        courtNames.add("Court1");
        courtNames.add("Court2");
        courtNames.add("Court3");
        courtNames.add("Court4");
        courtNames.add("Court5");
        scheduler.generateSchedule(LocalTime.parse("20:00"), courtNames);
        assertEqualPlayTime();
        scheduler.generateSchedule(LocalTime.parse("20:20"), courtNames);
        assertEqualPlayTime();
        courtNames.add("Court6");
        courtNames.add("Court7");
        scheduler.generateSchedule(LocalTime.parse("20:40"), courtNames);
        assertEqualPlayTime();
        scheduler.generateSchedule(LocalTime.parse("21:00"), courtNames);
        assertEqualPlayTime();
        assertCloseMatches(2.7);
    }

    @Test
    public void testAddPlayer() {
        List<String> courtNames = new ArrayList<>();
        courtNames.add("Court1");
        courtNames.add("Court2");
        courtNames.add("Court3");
        courtNames.add("Court4");
        courtNames.add("Court5");
        scheduler.generateSchedule(LocalTime.parse("20:00"), courtNames);
        scheduler.generateSchedule(LocalTime.parse("20:20"), courtNames);
        courtNames.add("Court6");
        courtNames.add("Court7");
        scheduler.generateSchedule(LocalTime.parse("20:40"), courtNames);
        scheduler.generateSchedule(LocalTime.parse("21:00"), courtNames);
        scheduler.markScheduleComplete(dataHolder.getFixtures().get(LocalTime.parse("20:00")));
        scheduler.markScheduleComplete(dataHolder.getFixtures().get(LocalTime.parse("20:20")));
        scheduler.addPlayer("Fred", 16);
        assertEqualPlayTime();
        printPlayersCurrentMatches();
        assertCloseMatches(2.7);
    }

    @Test
    public void testRemovePlayer() {
        List<String> courtNames = new ArrayList<>();
        courtNames.add("Court1");
        courtNames.add("Court2");
        courtNames.add("Court3");
        courtNames.add("Court4");
        courtNames.add("Court5");
        scheduler.generateSchedule(LocalTime.parse("20:00"), courtNames);
        scheduler.generateSchedule(LocalTime.parse("20:20"), courtNames);
        courtNames.add("Court6");
        courtNames.add("Court7");
        scheduler.generateSchedule(LocalTime.parse("20:40"), courtNames);
        scheduler.generateSchedule(LocalTime.parse("21:00"), courtNames);
        scheduler.markScheduleComplete(dataHolder.getFixtures().get(LocalTime.parse("20:00")));
        scheduler.markScheduleComplete(dataHolder.getFixtures().get(LocalTime.parse("20:20")));
        //Will remove Henry
        Player playerToRemove = dataHolder.getPlayers().get(0);
        scheduler.removePlayer(playerToRemove.getUuid());
        assertEqualPlayTime();
        printPlayersCurrentMatches();
        assertCloseMatches(2.7);
    }

    @Test
    public void testDisableCourt() {
        List<String> courtNames = new ArrayList<>();
        courtNames.add("Court1");
        courtNames.add("Court2");
        courtNames.add("Court3");
        courtNames.add("Court4");
        courtNames.add("Court5");
        scheduler.generateSchedule(LocalTime.parse("20:00"), courtNames);
        scheduler.generateSchedule(LocalTime.parse("20:20"), courtNames);
        courtNames.add("Court6");
        courtNames.add("Court7");
        scheduler.generateSchedule(LocalTime.parse("20:40"), courtNames);
        scheduler.generateSchedule(LocalTime.parse("21:00"), courtNames);
        scheduler.markScheduleComplete(dataHolder.getFixtures().get(LocalTime.parse("20:00")));
        scheduler.markScheduleComplete(dataHolder.getFixtures().get(LocalTime.parse("20:20")));
        scheduler.disableCourt("Court1");
        Fixture fixture3 = dataHolder.getFixtures().get(LocalTime.parse("20:40"));
        Fixture fixture4 = dataHolder.getFixtures().get(LocalTime.parse("21:00"));
        assertTrue(fixture3.getCourts().size() == 6);
        assertTrue(fixture4.getCourts().size() == 6);
        assertEqualPlayTime();
        printPlayersCurrentMatches();
        assertCloseMatches(2.7);
    }

    //Check that on average players are playing closely grouped games but always worth checking
    // actual fixtures as this could be misleading. Designed to fail easily
    private void assertCloseMatches(double diffLimit) {
        int sum = 0;
        double avgDiff = 0;
        for (Player player : dataHolder.getPlayers()) {
            List<Player> opponentsPlayed = player.getOpponentsPlayed();
            int playerMaxDiff = 0;
            for (Player opponent : opponentsPlayed) {
                int currentDiff = Math.abs(player.getLevel() - opponent.getLevel());
                playerMaxDiff = currentDiff > playerMaxDiff ? currentDiff : playerMaxDiff;
            }
            sum += playerMaxDiff;
        }
        avgDiff = (double) sum / (double) dataHolder.getPlayers().size();
        if (avgDiff > diffLimit) {
            printPlayersCurrentMatches();
        }
        assertTrue(avgDiff < diffLimit);
    }

    //unsure if this can be used as a metric yet (possibly in a test redesign)
    private boolean havePlayed(String namePlayer1, String namePlayer2) {
        Map<String, Player> namePlayerMap = new HashMap<>();
        for (Player player : dataHolder.getPlayers()) {
            namePlayerMap.put(player.getName(), player);
        }
        Player player1 = namePlayerMap.get(namePlayer1);
        Player player2 = namePlayerMap.get(namePlayer2);
        return player1.getOpponentsPlayed().contains(player2);
    }

    //Check that after a schedule, no currently active player has played more than 2 more games
    // than any other active player. To be run after every generated schedule
    private void assertEqualPlayTime() {
        List<Player> players = dataHolder.getPlayers();
        int minGamesPlayed = players.get(0).getOpponentsPlayed().size();
        int maxGamesPlayed = players.get(0).getOpponentsPlayed().size();
        for (int i = 1; i < players.size(); i++) {
            int gamesPlayed = players.get(i).getOpponentsPlayed().size();
            minGamesPlayed = gamesPlayed < minGamesPlayed ? gamesPlayed : minGamesPlayed;
            maxGamesPlayed = gamesPlayed > maxGamesPlayed ? gamesPlayed : maxGamesPlayed;
        }
        int diff = maxGamesPlayed - minGamesPlayed;
        assertTrue(diff < 2);
    }

    //Useful for debugging and evaluating manually if tests fail
    private void printPlayersCurrentMatches() {
        for (Player player : dataHolder.getPlayers()) {
            System.out.println("Player: " + player.getName() + " - " + player.getLevel() + " has " +
                    "played:");
            List<Player> opponentsPlayed = player.getOpponentsPlayed();
            for (Player opponent : opponentsPlayed) {
                System.out.println(opponent.getName() + " - " + opponent.getLevel());
            }
            System.out.println();
        }
    }
}