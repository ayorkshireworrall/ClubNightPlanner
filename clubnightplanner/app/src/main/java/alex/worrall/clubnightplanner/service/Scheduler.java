package alex.worrall.clubnightplanner.service;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.persistence.models.courtname.CourtName;
import alex.worrall.clubnightplanner.ui.main.courts.Court;
import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.players.Player;

import static alex.worrall.clubnightplanner.service.DataHolder.DatabaseAction.DELETE_ALL;

public class Scheduler {
    private static Scheduler instance;
    private DataHolder dataHolder;

    public static Scheduler getInstance(Context context) {
        if (instance == null) {
            instance = new Scheduler(context);
        }
        return instance;
    }

    private Scheduler(Context context) {
        dataHolder = DataHolder.getInstance(context);
    }

    void generateSchedule(int timeslot, List<CourtName> availableCourts) {
        List<Player> players = getRankedPlayers();
        ScheduleRankings.addPlayerRankings(players, timeslot, availableCourts, dataHolder);
        List<Player> priorityPlayers = getPriorityPlayers();
        List<Player[]> playerMatchings =
                getPlayerMatchings(availableCourts, players, priorityPlayers);
        List<Court> courts = new ArrayList<>();
        for (int i = 0; i < availableCourts.size(); i++) {
            if (playerMatchings.size() > i) {
                Player[] match = playerMatchings.get(i);
                courts.add(new Court(availableCourts.get(i), match[0], match[1]));
                addOpponentPlayed(match[0], match[1]);
            } else {
                courts.add(new Court(availableCourts.get(i), null, null));
            }
        }
        dataHolder.putFixture(timeslot, new Fixture(timeslot, courts));
        updatePlayStatus();
    }

    private void updatePlayStatus() {
        List<Fixture> orderedFixtures = dataHolder.getOrderedFixtures();
        boolean setInProgress = false;
        boolean setNext = false;
        for (Fixture current : orderedFixtures) {
            if (current.getPlayStatus().equals(Status.COMPLETED)) {
                continue;
            }
            if (current.getPlayStatus().equals(Status.IN_PROGRESS)) {
                setNext = true;
                continue;
            }
            if (setNext) {
                current.setPlayStatus(Status.NEXT);
                setNext = false;
                continue;
            }
            current.setPlayStatus(Status.LATER);
        }
    }

    private List<Player[]> getPlayerMatchings(List<CourtName> availableCourts, List<Player> players,
                                    List<Player> priorityPlayers) {
        int nonPriorityCap = 2*availableCourts.size() - priorityPlayers.size();
        List<Player[]> finalPlayerMatchings = new ArrayList<>();
        while (finalPlayerMatchings.size() < availableCourts.size() && players.size() > 1) {
            if (nonPriorityCap > 1) {
                Player[] matchPair = getPair(players);
                finalPlayerMatchings.add(matchPair);
                Player player1 = matchPair[0];
                Player player2 = matchPair[1];
                if (!priorityPlayers.contains(player1)) {
                    nonPriorityCap--;
                }
                if (!priorityPlayers.contains(player2)) {
                    nonPriorityCap--;
                }
                players.remove(player1);
                players.remove(player2);
                priorityPlayers.remove(player1);
                priorityPlayers.remove(player2);
            } else if (nonPriorityCap == 1) {
                Player[] matchPair = getPair(priorityPlayers, players);
                finalPlayerMatchings.add(matchPair);
                Player player1 = matchPair[0];
                Player player2 = matchPair[1];
                //Only player2 can be non priority
                if (!priorityPlayers.contains(player2)) {
                    nonPriorityCap--;
                }
                players.remove(player1);
                players.remove(player2);
                priorityPlayers.remove(player1);
                priorityPlayers.remove(player2);
            } else {
                Player[] matchPair = getPair(priorityPlayers);
                finalPlayerMatchings.add(matchPair);
                Player player1 = matchPair[0];
                Player player2 = matchPair[1];
                //By this point we're no longer using players list
                priorityPlayers.remove(player1);
                priorityPlayers.remove(player2);
            }
        }
        return finalPlayerMatchings;
    }

    //Get a best matching pair from a list of players
    private Player[] getPair(List<Player> players) {
        Player[] bestPair = new Player[2];
        for (Player player : players) {
            Player opponent = getBestMatch(player, players);
            Player[] workingPair = new Player[] {player, opponent};
            //Should never return a null pair (breaks a lot of things)
            if (bestPair[0] == null) {
                bestPair = workingPair;
            }
            //Check if choosing this working pair would result in a future pair who have already
            // played each other
            if (players.size() < 8 && players.size() > 3) {
                List<Player> modifiedList = new ArrayList<>(players);
                modifiedList.remove(player);
                modifiedList.remove(opponent);
                Player[] pair = getPair(modifiedList);
                if (pair[0].getOpponentsPlayed().contains(pair[1])) {
                    continue;
                }
            }
            if (evaluatePair(workingPair) < evaluatePair(bestPair)) {
                bestPair = workingPair;
            }
            Player opponentsOpponent = getBestMatch(opponent, players);
            if (player == opponentsOpponent) {
                return new Player[]{player, opponent};
            }
        }
        return bestPair;
    }

    private int evaluatePair(Player[] pair) {
        if (pair.length != 2 || pair[0] == null || pair[1] == null) {
            return 2147483647;
        }
        return Math.abs(pair[0].getScheduleRanking() - pair[1].getScheduleRanking());
    }

    //Get a best matching pair from a list of players where at least one of the pair must be
    //priority
    private Player[] getPair(List<Player> priorityPlayers, List<Player> players) {
        Map<Integer, Player[]> playerPlayerMap = new HashMap<>();
        for (Player player : priorityPlayers) {
            Player opponent = getBestMatch(player, players);
            Player opponentsOpponent = getBestMatch(opponent, players);
            Player[] matching = new Player[]{player, opponent};
            if (player == opponentsOpponent) {
                return matching;
            }
            int diff = player.getScheduleRanking() - opponent.getScheduleRanking();
            playerPlayerMap.put(diff, matching);
        }
        Integer minDiff = Collections.min(playerPlayerMap.keySet());
        return playerPlayerMap.get(minDiff);
    }

    //create a list of the players who have played less games (should always be at most one less
    //game than any other player on the list). Assumes list ordered by least played first
    private List<Player> getPriorityPlayers() {
        List<Player> players = getNextPlayers();
        if (players.isEmpty()) {
            return Collections.emptyList();
        }
        Player firstPlayer = players.get(0);
        Player lastPlayer = players.get(players.size() - 1);
        List<Player> priorityPlayers = new ArrayList<>();
        //Don't prioritise / stop prioritising when player's number of games is equal to the last
        //player's number of played games
        if (firstPlayer.getOpponentsPlayed().size() != lastPlayer.getOpponentsPlayed().size()) {
            for (Player player : players) {
                if (player.getOpponentsPlayed().size() == lastPlayer.getOpponentsPlayed().size()) {
                    break;
                }
                priorityPlayers.add(player);
            }
        } else {
            //clone don't modify player list object (mutations will occur later that shouldn't
            //affect original list)
            priorityPlayers = new ArrayList<>(players);
        }
        return priorityPlayers;
    }

    //Order active players based on who has played the least matches so far
    private List<Player> getNextPlayers() {
        List<Player> activePlayers = dataHolder.getPlayers();
        List<Player> prioritisedPlayers = new ArrayList<Player>(activePlayers);
        for (int i = 0; i < activePlayers.size(); i++) {
            for (int j = 0; j < activePlayers.size() - (i+1); j++) {
                Player playerA = prioritisedPlayers.get(j);
                Player playerB = prioritisedPlayers.get(j + 1);

                if (playerA.getOpponentsPlayed().size() > playerB.getOpponentsPlayed().size()) {
                    prioritisedPlayers.remove(j);
                    prioritisedPlayers.add(j + 1, playerA);
                }
            }
        }
        return prioritisedPlayers;
    }

    //Order active players based on level
    private List<Player> getRankedPlayers() {
        List<Player> activePlayers = dataHolder.getPlayers();
        List<Player> prioritisedPlayers = new ArrayList<Player>(activePlayers);
        for (int i = 0; i < activePlayers.size(); i++) {
            for (int j = 0; j < activePlayers.size() - (i+1); j++) {
                Player playerA = prioritisedPlayers.get(j);
                Player playerB = prioritisedPlayers.get(j + 1);

                if (playerA.getLevel() > playerB.getLevel()) {
                    prioritisedPlayers.remove(j);
                    prioritisedPlayers.add(j + 1, playerA);
                }
            }
        }
        return prioritisedPlayers;
    }

    //Out of unplayed opponents, find the closest in skill level to the current player
    private Player getBestMatch(Player player, List<Player> availablePlayers) {
        List<Player> yetToPlay = new ArrayList<Player>(availablePlayers);
        yetToPlay.remove(player);
        for (Player played : player.getOpponentsPlayed()) {
            yetToPlay.remove(played);
        }
        if (yetToPlay.size() == 0) {
            //All opponents have been played so no longer rank
            yetToPlay = new ArrayList<Player>(availablePlayers);
            yetToPlay.remove(player);
        }
        Player bestMatch = yetToPlay.get(0);
        for (int i = 1; i < yetToPlay.size(); i++) {
            Player potential = yetToPlay.get(i);
            int levelDifference = Math.abs(player.getScheduleRanking() - potential.getScheduleRanking());
            if (levelDifference < Math.abs(player.getScheduleRanking() - bestMatch.getScheduleRanking())) {
                bestMatch = potential;
            }
        }
        return bestMatch;
    }

    //If scheduling hasn't already happened, simply add the player. If it has, modify the
    //existing schedule
    void addPlayer(String name, int level) {
        if (dataHolder.getFixtures().size() == 0) {
            dataHolder.addPlayer(name, level);
        } else {
            try {
                Method addNewPlayer = this.getClass().getDeclaredMethod("addNewPlayer",
                        String.class, int.class);
                modifyFixtures(addNewPlayer, name, level);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private void addNewPlayer(String name, int level) {
        Player newPlayer = dataHolder.addPlayer(name, level);
        setNewPlayerPriority(newPlayer);
    }

    void updatePlayer(Player player) {
        dataHolder.updatePlayer(player);
        if (!dataHolder.getFixtures().isEmpty()) {
            modifyFixtures(null, null);
        }
    }

    void clearPlayers() {
        List<Player> players = new ArrayList<>(dataHolder.getPlayers());
        for (Player player : players) {
            removePlayer(player.getUuid());
        }
    }

    void removePlayer(String playerId) {
        try {
            Method removeExistingPlayer = this.getClass().getDeclaredMethod("removeExistingPlayer",
                    String.class);
            modifyFixtures(removeExistingPlayer, playerId);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void removeExistingPlayer(String playerId) {
        Iterator<Player> playerIterator = dataHolder.getPlayers().iterator();
        while(playerIterator.hasNext()) {
            Player player = playerIterator.next();
            if (player.getUuid().equals(playerId)) {
                playerIterator.remove();
                break;
            }
        }
    }

    //Modifies the players' opponents lists. Type of modification depends on the method passed
    private void modifyFixtures(Method playerListChange, Object ...methodArgs) {
        List<Fixture> toBeRescheduled = unscheduleUnplayedFixtures();
        if (playerListChange != null) {
            try {
                playerListChange.invoke(this, methodArgs);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        for (Fixture fixture : toBeRescheduled) {
            List<Court> courts = fixture.getCourts();
            List<CourtName> courtNames = new ArrayList<>();
            for (Court court : courts) {
                courtNames.add(court.getCourtName());
            }
            generateSchedule(fixture.getTimeSlot(), courtNames);
        }
    }

    void clearCourts() {
        List<CourtName> availableCourts = new ArrayList<>(dataHolder.getAvailableCourts());
        for (CourtName courtName : availableCourts) {
            disableCourt(courtName);
            dataHolder.removeCourt(courtName);
        }
        dataHolder.modifyCourtList(DELETE_ALL, null);
    }

    void disableCourt(CourtName courtName) {
        List<Fixture> fixturesToReschedule = unscheduleUnplayedFixtures();
        for (Fixture fixture : fixturesToReschedule) {
            List<Court> courts = fixture.getCourts();
            Court toBeRemoved = null;
            for (Court court : courts) {
                if (court.getCourtName().getName().equalsIgnoreCase(courtName.getName())) {
                    toBeRemoved = court;
                }
            }
            courts.remove(toBeRemoved);
            List<CourtName> courtNames = new ArrayList<>();
            for (Court court : courts) {
                courtNames.add(court.getCourtName());
            }
            generateSchedule(fixture.getTimeSlot(), courtNames);
        }
    }

    //find all fixtures yet to be played and remove their schedule
    List<Fixture> unscheduleUnplayedFixtures() {
        List<Fixture> toBeRescheduled = new ArrayList<>();
        for (Fixture fixture : dataHolder.getFixtures().values()) {
            if (fixture.getPlayStatus().equals(Status.COMPLETED) ||
                    fixture.getPlayStatus().equals(Status.IN_PROGRESS)) {
                continue;
            }
            int timeslot = fixture.getTimeSlot();
            unschedule(fixture);
            toBeRescheduled.add(fixture);
        }
        return toBeRescheduled;
    }

    //New player set to have same number of played matches as a priority player (so they
    //don't have ultimate priority)
    private void setNewPlayerPriority(Player newPlayer) {
        List<Player> orderedPlayers = getNextPlayers();
        orderedPlayers.remove(newPlayer);
        if (orderedPlayers.size() == 0) {
            return;
        }
        final int opponentsPlayed = orderedPlayers.get(0).getOpponentsPlayed().size();
        for (int i = 0; i < opponentsPlayed; i++) {
            Player arbitrary = new Player("arbitrary " + i, newPlayer.getLevel());
            addOpponentPlayed(newPlayer, arbitrary);
        }
    }

    //Correct data for opponents played when fixture is changed
    void unschedule(Fixture fixture) {
        List<Court> courts = fixture.getCourts();
        for (Court court : courts) {
            Player playerA = court.getPlayerA();
            if (playerA == null) {
                continue;
            }
            Player playerB = court.getPlayerB();

            List<Player> opponentsPlayedA = playerA.getOpponentsPlayed();
            opponentsPlayedA.remove(playerB);
            playerA.setOpponentsPlayed(opponentsPlayedA);

            List<Player> opponentsPlayedB = playerB.getOpponentsPlayed();
            opponentsPlayedB.remove(playerA);
            playerB.setOpponentsPlayed(opponentsPlayedB);
        }
    }

    void clearFixtures() {
        List<Fixture> fixtures = new ArrayList<Fixture>(dataHolder.getFixtures().values());
        for (Fixture fixture : fixtures) {
            unschedule(fixture);
            dataHolder.removeFixture(fixture);
        }
    }

    private void addOpponentPlayed(Player playerA, Player playerB) {
        List<Player> opponentsPlayedA = playerA.getOpponentsPlayed();
        opponentsPlayedA.add(playerB);
        playerA.setOpponentsPlayed(opponentsPlayedA);

        List<Player> opponentsPlayedB = playerB.getOpponentsPlayed();
        opponentsPlayedB.add(playerA);
        playerB.setOpponentsPlayed(opponentsPlayedB);
    }

    //set status as complete and find the next slot and mark it as next
    void markScheduleComplete(Fixture fixture) {
        //TODO data handling not great - mutates actual objects
        fixture.setPlayStatus(Status.COMPLETED);
        List<Fixture> orderedFixtures = dataHolder.getOrderedFixtures();
        boolean setInProgress = false;
        boolean setNext = false;
        for (Fixture current : orderedFixtures) {
            if (current == fixture) {
                setInProgress = true;
                continue;
            }
            if (setInProgress) {
                setInProgress = false;
                setNext = true;
                current.setPlayStatus(Status.IN_PROGRESS);
                continue;
            }
            if (setNext) {
                current.setPlayStatus(Status.NEXT);
                break;
            }
        }
    }

    void startFixture(Fixture fixture) {
        fixture.setPlayStatus(Status.IN_PROGRESS);
        List<Fixture> orderedFixtures = dataHolder.getOrderedFixtures();
        boolean setNext = false;
        for (Fixture current : orderedFixtures) {
            if (current == fixture) {
                setNext = true;
                continue;
            }
            if (setNext) {
                current.setPlayStatus(Status.NEXT);
                break;
            }
        }
    }

    void removeFixture(Fixture fixture) {
        dataHolder.removeFixture(fixture);
        unschedule(fixture);
        //If we're deleting the current "NEXT" fixture we need to set a new one if possible
        List<Fixture> orderedFixtures =
                fixture.getPlayStatus() == Status.NEXT ? dataHolder.getOrderedFixtures() :
                        Collections.<Fixture>emptyList();
        for (Fixture current : orderedFixtures) {
            if (current.getPlayStatus() == Status.LATER) {
                current.setPlayStatus(Status.NEXT);
                break;
            }
        }
    }
}

//NO LONGER RELEVANT AS SELECTED PAIR BROKE THE CONDITION THAT REMATCHES SHOULDN'T HAPPEN UNTIL A
//GOOD NUMBER OF GAMES HAVE BEEN PLAYED
//Why are we guaranteed a matchmaking pair?
//Visualise players as nodes and player mappings as directional edges
//To not select a repeat we would require a loop (ie/ every node has exactly 1 input edge and
//exactly one output edge)
//Loops can only occur if nodes have the same value
//Matchmaking function selects based on list order, so if the top of the input list registers as
//the best match it won't be replaced by another item
//Therefore matchmaking for first loop item loop will choose the second loop item as that will
//appear higher in the list than others of equal value. Similarly, the second loop item will
//chose the first list item.
//Hence, even in loops there will be a pair selected
