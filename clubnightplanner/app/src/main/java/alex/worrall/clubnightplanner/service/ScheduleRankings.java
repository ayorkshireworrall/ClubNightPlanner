package alex.worrall.clubnightplanner.service;

import java.util.List;

import alex.worrall.clubnightplanner.persistence.models.CourtName;
import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.players.Player;

//TODO create more models and a way to select the most appropriate based on input values
public class ScheduleRankings {


    /**
     * Set player rankings which are used in best match making. This method should be adapted to
     * select the best model based on the input player list
     * @param players list of players, should already be ordered by level
     * @param timeslot fixture timeslot, used in rankers where the schedule ranks depend on which
     *                fixutre is being played
     * @param availableCourts could be useful to determine space
     */
    static void addPlayerRankings(List<Player> players, int timeslot,
                                  List<CourtName> availableCourts, DataHolder dataHolder) {
        powerTwoRanker(players, timeslot, dataHolder);
    }

    /**
     * Assigns ranking based on comparative position in the list of players and modifies it so
     * that players are grouped in powers of two as rankings go on, the idea being that these
     * groups will play each other first
     * Works well for large number of players with an even spread of ability and plenty of court
     * space
     * LIMITATIONS: If player size is slightly more than 2 ^ fixtureNumber then bottom players
     * will have interesting matches. If player spread is not even then people may end up grouped
     * in the wrong power of 2 and get poorly matched games
     * @param players
     * @param timeslot
     */
    private static void powerTwoRanker(List<Player> players, int timeslot, DataHolder dataHolder) {
        int fixtureNumber = getFixtureNumber(timeslot, dataHolder);
        for (int n = 1; n < fixtureNumber + 1; n++) {
            boolean isPowerTwo = isPowerOfTwo(n);
            boolean addExtra = true;
            int count = 0;
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (n == 1) {
                    player.setScheduleRanking(i);
                }
                if (isPowerTwo) {
                    int extra = calculateExtra(n);
                    if (addExtra) {
                        player.setScheduleRanking(player.getScheduleRanking() + extra);
                    }
                    if (++count == extra) {
                        count = 0;
                        addExtra = !addExtra;
                    }
                }
            }
        }
    }

    private static int getFixtureNumber(int timeslot, DataHolder dataHolder) {
        int fixtureNumber = 0;
        List<Fixture> orderedFixtures = dataHolder.getOrderedFixtures();
        for (int i = 0; i < orderedFixtures.size(); i++) {
            Fixture fixture = orderedFixtures.get(i);
            if (fixture.getTimeSlot() == timeslot) {
                fixtureNumber = i + 1;
                break;
            }
        }
        if (fixtureNumber == 0) {
            fixtureNumber = orderedFixtures.size() + 1;
        }
        return fixtureNumber;
    }

    private static int calculateExtra(int n) {
        double log2n = Math.log(n)/Math.log(2);
        return (int) Math.pow(2, log2n - 1);
    }

    private static boolean isPowerOfTwo(int n) {
        if (n == 1) {
            return false;
        }
        double log2n = Math.log(n)/Math.log(2);
        double epsilon = 0.01;
        if (log2n - epsilon < Math.floor(log2n) && Math.floor(log2n) < log2n + epsilon) {
            return true;
        }
        return false;
    }
}
