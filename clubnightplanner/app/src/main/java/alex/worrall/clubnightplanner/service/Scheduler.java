package alex.worrall.clubnightplanner.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.ui.main.courts.Court;
import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.players.Player;

public class Scheduler {
    private DataHolder dataHolder = DataHolder.getInstance();

    void generateSchedule(Integer timeSlot, List<String> availableCourts) {
        List<Court> courts = new ArrayList<>();
        List<Player> players = getNextPlayers();
        List<Player> priorityPlayers = getPriorityPlayers(players);
        //First create a basic court list with the next players and their best unplayed opponents
        for (String courtName : availableCourts) {
            if (players.size() < 1) {
                break;
            }
            Player playerA = players.get(0);
            Player playerB = getBestMatch(playerA, players);
            if (playerB == null) {
                //To prevent a bad swap slightly later
                priorityPlayers.remove(playerA);
                break;
            }
            Court court = new Court(courtName, playerA, playerB);
            courts.add(court);
            //Remove priority players who now have games and prevent multiple games in same
            //schedule for players
            priorityPlayers.remove(playerA);
            priorityPlayers.remove(playerB);
            players.remove(playerA);
            players.remove(playerB);
        }
        //Don't change court fixtures if player prioritisation is already fair
        if (!priorityPlayers.isEmpty() || priorityPlayers.equals(players)) {
            addMissedPriorityPlayers(priorityPlayers, getSwappableCourts(courts));
        }
        //Update player models now court schedule has been finalised
        for (Court court : courts) {
            Player playerA = court.getPlayerA();
            Player playerB = court.getPlayerB();
            addOpponentPlayed(playerA, playerB);
        }
        dataHolder.putFixture(timeSlot, new Fixture(timeSlot, courts));
    }

    //Put missed priority players against their best match on the courts that could be fairly
    // swapped based on the number of games played
    private void addMissedPriorityPlayers(List<Player> priorityPlayers, List<Court> swappableCourts) {
        for (Player priorityPlayer : priorityPlayers) {
            //If all courts already have priority players on them
            if (swappableCourts.size() == 0) {
                break;
            }
            Court targetCourt = null;
            int bestLevelMatch = 2147483647;
            //Find best court for the priority player to join
            for (Court court : swappableCourts) {
                int level = court.getPlayerA().getLevel();
                if (Math.abs(priorityPlayer.getLevel() - level) < bestLevelMatch) {
                    targetCourt = court;
                }
            }
            //note that this modifies the actual court object referenced in the list from which
            //swappableCourts are derived
            targetCourt.setPlayerB(priorityPlayer);
            swappableCourts.remove(targetCourt);
        }
    }

    //Check for courts where a non priority player is playing. Can check in this way on the
    // assumption that all courts should contain at least one priority player whose opponent may
    // have played one more game than them
    private List<Court> getSwappableCourts(List<Court> courts) {
        List<Court> potentialSwapCourts = new ArrayList<>();
        for (Court court : courts) {
            Player playerA = court.getPlayerA();
            Player playerB = court.getPlayerB();
            if (playerA.getOpponentsPlayed().size() < playerB.getOpponentsPlayed().size()) {
                potentialSwapCourts.add(court);
            }
        }
        return potentialSwapCourts;
    }

    //create a list of the players who have played less games (should always be at most one less
    //game than any other player on the list). Assumes list ordered by least played first
    private List<Player> getPriorityPlayers(List<Player> players) {
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

    //Out of unplayed opponents, find the closest in skill level to the current player
    private Player getBestMatch(Player player, List<Player> availablePlayers) {
        List<Player> yetToPlay = new ArrayList<Player>(availablePlayers);
        yetToPlay.remove(player);
        for (Player played : player.getOpponentsPlayed()) {
            yetToPlay.remove(played);
        }
        if (yetToPlay.size() == 0) {
            return null;
        }
        Player bestMatch = yetToPlay.get(0);
        for (int i = 1; i < yetToPlay.size(); i++) {
            Player potential = yetToPlay.get(i);
            int levelDifference = Math.abs(player.getLevel() - potential.getLevel());
            if (levelDifference < Math.abs(player.getLevel() - bestMatch.getLevel())) {
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
                modifyPlayerList(addNewPlayer, name, level);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private void addNewPlayer(String name, int level) {
        Player newPlayer = dataHolder.addPlayer(name, level);
        setNewPlayerPriority(newPlayer);
    }

    void removePlayer(String playerId) {
        try {
            Method removeExistingPlayer = this.getClass().getDeclaredMethod("removeExistingPlayer",
                    String.class);
            modifyPlayerList(removeExistingPlayer, playerId);
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
    private void modifyPlayerList(Method method, Object ...methodArgs) {
        List<Fixture> toBeRescheduled = unplayedFixtures();
        try {
            method.invoke(this, methodArgs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        for (Fixture fixture : toBeRescheduled) {
            List<Court> courts = fixture.getCourts();
            List<String> courtNames = new ArrayList<>();
            for (Court court : courts) {
                courtNames.add(court.getCourtName());
            }
            generateSchedule(fixture.getTimeSlot(), courtNames);
        }
    }

    void disableCourt(String courtName) {
        List<Fixture> fixturesToReschedule = unplayedFixtures();
        for (Fixture fixture : fixturesToReschedule) {
            List<Court> courts = fixture.getCourts();
            Court toBeRemoved = null;
            for (Court court : courts) {
                if (court.getCourtName().equalsIgnoreCase(courtName)) {
                    toBeRemoved = court;
                }
            }
            courts.remove(toBeRemoved);
            List<String> courtNames = new ArrayList<>();
            for (Court court : courts) {
                courtNames.add(court.getCourtName());
            }
            generateSchedule(fixture.getTimeSlot(), courtNames);
        }
    }

    //find all fixtures yet to be played and remove their schedule
    private List<Fixture> unplayedFixtures() {
        List<Fixture> toBeRescheduled = new ArrayList<>();
        for (Fixture fixture : dataHolder.getFixtures().values()) {
            if (fixture.isPlayed()) {
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
            Player playerB = court.getPlayerB();

            List<Player> opponentsPlayedA = playerA.getOpponentsPlayed();
            opponentsPlayedA.remove(playerB);
            playerA.setOpponentsPlayed(opponentsPlayedA);

            List<Player> opponentsPlayedB = playerB.getOpponentsPlayed();
            opponentsPlayedB.remove(playerA);
            playerB.setOpponentsPlayed(opponentsPlayedB);
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
    void markScheduleComplete(Fixture fixture) {
        fixture.setPlayed(true);
    }
}
