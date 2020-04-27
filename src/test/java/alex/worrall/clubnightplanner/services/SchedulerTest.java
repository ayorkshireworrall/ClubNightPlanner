package alex.worrall.clubnightplanner.services;

import alex.worrall.clubnightplanner.models.Court;
import alex.worrall.clubnightplanner.models.Player;
import alex.worrall.clubnightplanner.models.Schedule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;

public class SchedulerTest {
    SchedulerImpl scheduler = new SchedulerImpl();

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
        scheduler.generateSchedule(1, courtNames);
        assertEqualPlayTime();
        scheduler.generateSchedule(2, courtNames);
        assertEqualPlayTime();
        courtNames.add("Court6");
        courtNames.add("Court7");
        scheduler.generateSchedule(3, courtNames);
        assertEqualPlayTime();
        scheduler.generateSchedule(4, courtNames);
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
        scheduler.generateSchedule(1, courtNames);
        scheduler.generateSchedule(2, courtNames);
        courtNames.add("Court6");
        courtNames.add("Court7");
        scheduler.generateSchedule(3, courtNames);
        scheduler.generateSchedule(4, courtNames);
        scheduler.markScheduleComplete(scheduler.getSchedules().get(1));
        scheduler.markScheduleComplete(scheduler.getSchedules().get(2));
        scheduler.addPlayer("Fred", 16);
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
        scheduler.generateSchedule(1, courtNames);
        scheduler.generateSchedule(2, courtNames);
        courtNames.add("Court6");
        courtNames.add("Court7");
        scheduler.generateSchedule(3, courtNames);
        scheduler.generateSchedule(4, courtNames);
        scheduler.markScheduleComplete(scheduler.getSchedules().get(1));
        scheduler.markScheduleComplete(scheduler.getSchedules().get(2));
        //Will remove Henry
        Player playerToRemove = scheduler.getPlayers().get(0);
        scheduler.removePlayer(playerToRemove.getUuid());
        printPlayersCurrentMatches();
        assertCloseMatches(2.7);
    }

    //Check that on average players are playing closely grouped games but always worth checking
    // actual fixtures as this could be misleading. Designed to fail easily
    private void assertCloseMatches(double diffLimit) {
        int sum = 0;
        double avgDiff = 0;
        for (Player player : scheduler.getPlayers()) {
            List<Player> opponentsPlayed = player.getOpponentsPlayed();
            int playerMaxDiff = 0;
            for (Player opponent : opponentsPlayed) {
                int currentDiff = Math.abs(player.getLevel() - opponent.getLevel());
                playerMaxDiff = currentDiff > playerMaxDiff ? currentDiff : playerMaxDiff;
            }
            sum += playerMaxDiff;
        }
        avgDiff = (double) sum / (double) scheduler.getPlayers().size();
        if (avgDiff > diffLimit) {
            printPlayersCurrentMatches();
        }
        assertTrue(avgDiff < diffLimit);
    }

    //unsure if this can be used as a metric yet (possibly in a test redesign)
    private boolean havePlayed(String namePlayer1, String namePlayer2) {
        Map<String, Player> namePlayerMap = new HashMap<>();
        for (Player player : scheduler.getPlayers()) {
            namePlayerMap.put(player.getName(), player);
        }
        Player player1 = namePlayerMap.get(namePlayer1);
        Player player2 = namePlayerMap.get(namePlayer2);
        return player1.getOpponentsPlayed().contains(player2);
    }

    //Check that after a schedule, no currently active player has played more than 2 more games
    // than any other active player. To be run after every generated schedule
    private void assertEqualPlayTime() {
        List<Player> players = scheduler.getPlayers();
        int minGamesPlayed = players.get(0).getOpponentsPlayed().size();
        int maxGamesPlayed = players.get(0).getOpponentsPlayed().size();
        for (int i = 1; i < players.size(); i ++) {
            int gamesPlayed = players.get(i).getOpponentsPlayed().size();
            minGamesPlayed = gamesPlayed < minGamesPlayed ? gamesPlayed : minGamesPlayed;
            maxGamesPlayed = gamesPlayed > maxGamesPlayed ? gamesPlayed : maxGamesPlayed;
        }
        int diff = maxGamesPlayed - minGamesPlayed;
        assertTrue(diff < 2);
    }

    //Useful for debugging and evaluating manually if tests fail
    private void printPlayersCurrentMatches() {
        for (Player player : scheduler.getPlayers()) {
            System.out.println("Player: " + player.getName() + " - " + player.getLevel()+ " has " +
                    "played:");
            List<Player> opponentsPlayed = player.getOpponentsPlayed();
            for (Player opponent : opponentsPlayed) {
                System.out.println(opponent.getName() + " - " + opponent.getLevel());
            }
            System.out.println();
        }
    }
}
