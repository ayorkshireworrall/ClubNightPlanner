package alex.worrall.clubnightplanner.services;

import alex.worrall.clubnightplanner.models.Court;
import alex.worrall.clubnightplanner.models.Player;
import alex.worrall.clubnightplanner.models.Schedule;

import java.util.*;
import java.util.stream.Collectors;

public class SchedulerImpl implements Scheduler{

    private List<Player> activePlayers = new ArrayList<>();
    private Map<Integer, Schedule> currentSchedules = new HashMap<>();

    public void generateSchedule(Integer timeSlot, List<String> availableCourts) {
        List<Court> courts = new ArrayList<>();
        List<Player> players = getNextPlayers();
        List<Player> priorityPlayers = getPriorityPlayers(players);
        //First create a basic court list with the next players and their best unplayed opponents
        for (String courtName : availableCourts) {
            Player playerA = players.get(0);
            Player playerB = getBestMatch(playerA, players);
            Court court = new Court(courtName, playerA, playerB);
            courts.add(court);
            //Remove priority players who now have games and prevent multiple games in same
            //schedule for players
            priorityPlayers.remove(playerA);
            priorityPlayers.remove(playerB);
            players.remove(playerA);
            players.remove(playerB);
        }
        if (!priorityPlayers.isEmpty()) {
            //Identify courts where non priority players are playing
            List<Court> potentialSwapCourts = new ArrayList<>();
            for (Court court : courts) {
                Player playerA = court.getPlayerA();
                Player playerB = court.getPlayerB();
                if (playerA.getOpponentsPlayed().size() < playerB.getOpponentsPlayed().size()) {
                    potentialSwapCourts.add(court);
                }
            }
            for (Player priorityPlayer : priorityPlayers) {
                //If all courts already have priority players on them
                if (potentialSwapCourts.size() == 0) {
                    break;
                }
                Court targetCourt = null;
                int bestLevelMatch = 100000;
                //Find best court for the priority player to join
                for (Court court : potentialSwapCourts) {
                    int level = court.getPlayerA().getLevel();
                    if (Math.abs(priorityPlayer.getLevel() - level) < bestLevelMatch) {
                        targetCourt = court;
                    }
                }
                targetCourt.setPlayerB(priorityPlayer);
                potentialSwapCourts.remove(targetCourt);
            }
        }
        //Update player models now court schedule has been finalised
        for (Court court : courts) {
            Player playerA = court.getPlayerA();
            Player playerB = court.getPlayerB();
            playerA.addPlayedOpponent(playerB);
            playerB.addPlayedOpponent(playerA);
        }
        currentSchedules.put(timeSlot, new Schedule(timeSlot, courts));
    }

    //create a list of the players who have played less games (should always be at most one less
    //game than any other player on the list)
    private List<Player> getPriorityPlayers(List<Player> players) {
        List<Player> priorityPlayers = new ArrayList<>();
        if (players.get(0).getOpponentsPlayed().size() != players.get(players.size() - 1).getOpponentsPlayed().size()) {
            for (Player player : players) {
                if (player.getOpponentsPlayed().size() == players.get(players.size() - 1).getOpponentsPlayed().size()) {
                    break;
                }
                priorityPlayers.add(player);
            }
        } else {
            //clone don't modify player list object
            priorityPlayers = new ArrayList<>(players);
        }
        return priorityPlayers;
    }

    public void removePlayer(Player player) {
        this.activePlayers.remove(player);
    }

    //Order active players based on who has played the least matches so far
    private List<Player> getNextPlayers() {
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

    public Map<Integer, Schedule> getSchedules() {
        return currentSchedules;
    }

    public void addPlayer(String name, int level) {
        activePlayers.add(new Player(name, level));
    }
    public List<Player> getPlayers() {
        return this.activePlayers;
    }
}
