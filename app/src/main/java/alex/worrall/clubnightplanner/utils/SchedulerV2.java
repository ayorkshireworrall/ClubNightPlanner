package alex.worrall.clubnightplanner.utils;

import android.service.autofill.DateValueSanitizer;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.model.court.CourtRepository;
import alex.worrall.clubnightplanner.model.fixture.Court;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.model.fixture.FixtureRepository;
import alex.worrall.clubnightplanner.model.history.History;
import alex.worrall.clubnightplanner.model.history.HistoryRepository;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.model.player.PlayerRepository;

public class SchedulerV2 {
    AppCompatActivity activity;
    Map<Player, List<History>> playerHistoryMap;
    Map<String, Player> playerIdMap;
    PlayerRepository playerRepository;
    FixtureRepository fixtureRepository;
    CourtRepository courtRepository;
    HistoryRepository historyRepository;

    public SchedulerV2(AppCompatActivity activity) {
        this.activity = activity;
        playerRepository = new PlayerRepository(activity.getApplication());
        fixtureRepository = new FixtureRepository(activity.getApplication());
        courtRepository = new CourtRepository(activity.getApplication());
        historyRepository = new HistoryRepository(activity.getApplication());
    }

    public void generateSchedule(int timeslot, List<String> availableCourts) {
        Collections.sort(availableCourts);
        populatePlayerHistoryMap();
        List<Player> players = playerRepository.getOrderedPlayers();
        List<Court> courts = new ArrayList<>();
        if (players.size() > 1) {
            populatePlayerIdMap(players);
            ScheduleRankings scheduleRankings = new ScheduleRankings(fixtureRepository, activity);
            scheduleRankings.addPlayerRankings(players, timeslot, availableCourts);
            List<Player> priorityPlayers = getPriorityPlayers(players);
            List<Player[]> playerMatchings = getPlayerMatchings(availableCourts, players, priorityPlayers);
            for (int i = 0; i < availableCourts.size(); i++) {
                if (playerMatchings.size() > i) {
                    Player[] match = playerMatchings.get(i);
                    courts.add(new Court(availableCourts.get(i), match[0], match[1], timeslot));
                    historyRepository.insertHistory(new History(match[0].getId(), match[1].getId()));
                    historyRepository.insertHistory(new History(match[1].getId(), match[0].getId()));
                } else {
                    courts.add(new Court(availableCourts.get(i), null, null, timeslot));
                }
            }
        } else {
            for (int i = 0; i < availableCourts.size(); i++) {
                courts.add(new Court(availableCourts.get(i), null, null, timeslot));
            }
        }
        fixtureRepository.addFixture(new Fixture(timeslot, courts));
    }

    public void unschedule(Fixture fixture) {
        List<Court> courts = fixture.getCourts();
        for (Court court : courts) {
            if (court.getPlayerA() == null) {
                continue;
            }
            String id1 = court.getPlayerA().getId();
            String id2 = court.getPlayerB().getId();
            historyRepository.deleteHistory(id1, id2);
            historyRepository.deleteHistory(id2, id1);
        }
        fixtureRepository.deleteFixture(fixture);
    }

    public void reschedule(List<Fixture> laterFixtures) {
        for (Fixture later : laterFixtures) {
            unschedule(later);
        }
        for (Fixture later : laterFixtures) {
            List<String> courtnames = new ArrayList<>();
            for (Court court : later.getCourts()) {
                courtnames.add(court.getCourtName());
            }
            generateSchedule(later.getTimeslot(), courtnames);
        }
    }

    private List<Player[]> getPlayerMatchings(List<String> availableCourts, List<Player> players,
                                              List<Player> priorityPlayers) {
        int nonPriorityCap = 2*availableCourts.size() - priorityPlayers.size();
        List<Player[]> finalPlayerMatchings = new ArrayList<>();
        while (finalPlayerMatchings.size() < availableCourts.size() && players.size() > 1) {
            if (priorityPlayers.isEmpty() && nonPriorityCap > 1) {
                Player[] matchPair = getPair(players, players);
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
            } else if (!priorityPlayers.isEmpty() && nonPriorityCap > 0) {
                Player[] matchPair = getPair(players, priorityPlayers);
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
                Player[] matchPair = getPair(priorityPlayers, priorityPlayers);
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
    private Player[] getPair(List<Player> players, List<Player> priorityPlayers) {
        Player[] bestPair = new Player[2];
        Player[] ifNothing = new Player[2];
        playerIterator: for (Player player : priorityPlayers) {
            Player opponent = getBestMatch(player, players);
            Player[] workingPair = new Player[] {player, opponent};
            //Should never return a null pair (breaks a lot of things)
            if (ifNothing[0] == null) {
                ifNothing = workingPair;
            }
            //Check if choosing this working pair would result in a future pair who have already
            // played each other
            if (priorityPlayers.size() < 8 && priorityPlayers.size() > 3) {
                List<Player> modifiedList = new ArrayList<>(players);
                modifiedList.remove(player);
                modifiedList.remove(opponent);
                List<Player> modifiedPriorityList = new ArrayList<>(priorityPlayers);
                modifiedPriorityList.remove(player);
                modifiedPriorityList.remove(opponent);
                Player[] pair = getPair(modifiedList, modifiedPriorityList);
                List<History> histories = playerHistoryMap.get(pair[0]);
                for (History history : histories) {
                    if (history.getOpponentId().equals(pair[1].getId())) {
                        continue playerIterator;
                    }
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
        return bestPair[0] == null ? ifNothing : bestPair;
    }

    private int evaluatePair(Player[] pair) {
        if (pair.length != 2 || pair[0] == null || pair[1] == null) {
            return 2147483647;
        }
        return Math.abs(pair[0].getScheduleRanking() - pair[1].getScheduleRanking());
    }

    private Player getBestMatch(Player player, List<Player> availablePlayers) {
        List<Player> yetToPlay = new ArrayList<Player>(availablePlayers);
        yetToPlay.remove(player);
        for (History history :
                playerHistoryMap.get(player)) {
            Player played = playerIdMap.get(history.getOpponentId());
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

    private List<Player> getPriorityPlayers(List<Player> players) {
        List<Player> orderedPlayers = getNextPlayers(players);
        List<Player> priorityPlayers = new ArrayList<>(orderedPlayers);
        Player firstPlayer = priorityPlayers.get(0);
        Player lastPlayer = priorityPlayers.get(players.size() - 1);
        List<History> firstPlayerHistory = playerHistoryMap.get(firstPlayer);
        List<History> lastPlayerHistory = playerHistoryMap.get(lastPlayer);
        int count = 1;
        while (firstPlayerHistory.size() != lastPlayerHistory.size()) {
            priorityPlayers.remove(lastPlayer);
            lastPlayer = orderedPlayers.get(orderedPlayers.size() - ++count);
            lastPlayerHistory = playerHistoryMap.get(lastPlayer);
        }
        return priorityPlayers;
    }

    private List<Player> getNextPlayers(List<Player> players) {
        List<Player> prioritisedPlayers = new ArrayList<>(players);
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < players.size() - (i+1); j++) {
                Player player1 = prioritisedPlayers.get(j);
                Player player2 = prioritisedPlayers.get(j + 1);

                List<History> player1History = playerHistoryMap.get(player1);
                List<History> player2History = playerHistoryMap.get(player2);

                if (player1History != null && player2History != null &&
                        player1History.size() > player2History.size()) {
                    prioritisedPlayers.remove(j);
                    prioritisedPlayers.add(j + 1, player1);
                }
            }
        }
        return prioritisedPlayers;
    }

    private void populatePlayerHistoryMap() {
        playerHistoryMap = new HashMap<>();
        List<Player> players = playerRepository.getOrderedPlayers();
        List<History> histories = historyRepository.getAllHistories();
        //TODO test below for performance (multiple calls to DB instead of double loop)
//        for (Player player : players) {
//            playerHistoryMap.put(player, historyRepository.getPlayerHistory(player.getId()));
//        }
        for (Player player : players) {
            List<History> playerHistory = new ArrayList<>();
            for (History history : histories) {
                if (history.getPlayerId().equals(player.getId())) {
                    playerHistory.add(history);
                }
            }
            playerHistoryMap.put(player, playerHistory);
        }
    }

    private void populatePlayerIdMap(List<Player> players) {
        playerIdMap = new HashMap<>();
        for (Player player : players) {
            playerIdMap.put(player.getId(), player);
        }
    }
}
