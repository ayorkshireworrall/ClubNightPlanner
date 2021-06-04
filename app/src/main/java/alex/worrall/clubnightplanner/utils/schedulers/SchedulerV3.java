package alex.worrall.clubnightplanner.utils.schedulers;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alex.worrall.clubnightplanner.model.court.CourtRepository;
import alex.worrall.clubnightplanner.model.fixture.Court;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.model.fixture.FixtureRepository;
import alex.worrall.clubnightplanner.model.history.History;
import alex.worrall.clubnightplanner.model.history.HistoryRepository;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.model.player.PlayerRepository;
import alex.worrall.clubnightplanner.utils.graphs.Edge;
import alex.worrall.clubnightplanner.utils.graphs.Graph;
import alex.worrall.clubnightplanner.utils.graphs.Node;

/**
 * A new scheduler that represents potential player matchings as a graph with players as the
 * nodes and a weighting based on rank difference, number of games played and opponent history.
 * The scheduler calculates the shortest path connecting these nodes using by applying the
 * nearest neighbour algorithm to each node in turn. It then eliminates the highest weight edges
 * from the shortest path to leave the best pairings
 */
public class SchedulerV3 extends Scheduler {

    //highest possible level difference is 99998. Rematches are initially penalised more than
    // extra games (although additional extra games get penalised exponentially later)
    private static final int EXTRA_GAME_CONSTANT = 100000;
    private static final int PLAYED_OPPONENT_CONSTANT = 10000000;

    public SchedulerV3(AppCompatActivity activity) {
        playerRepository = new PlayerRepository(activity.getApplication());
        fixtureRepository = new FixtureRepository(activity.getApplication());
        courtRepository = new CourtRepository(activity.getApplication());
        historyRepository = new HistoryRepository(activity.getApplication());
    }

    public SchedulerV3(PlayerRepository playerRepository, FixtureRepository fixtureRepository, CourtRepository courtRepository, HistoryRepository historyRepository) {
        this.playerRepository = playerRepository;
        this.fixtureRepository = fixtureRepository;
        this.courtRepository = courtRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    public void generateSchedule(int timeslot, List<String> availableCourts) {
        List<Court> courts = new ArrayList<>();
        List<Player> players = playerRepository.getOrderedPlayers();
        int targetPairings = (int) Math.min(availableCourts.size(), Math.floor(players.size() / 2));
        List<Player[]> playerPairings = getPlayerPairings(targetPairings, players);
        if (players.size() > 1) {
            for (int i = 0; i < availableCourts.size(); i++) {
                if (playerPairings.size() > i) {
                    Player[] match = playerPairings.get(i);
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

    List<Player[]> getPlayerPairings(int pairings, List<Player> players) {
        Map<String, Player> idPlayerMap = new HashMap<>();
        for (Player player : players) {
            idPlayerMap.put(player.getId(), player);
        }
        List<Player[]> playerPairings = new ArrayList<>();
        List<Edge> optimalPairings = getOptimalPairings(pairings);
        for (Edge e : optimalPairings) {
            Player a = idPlayerMap.get(e.getSourceNode());
            Player b = idPlayerMap.get(e.getTargetNode());
            playerPairings.add(new Player[]{a, b});
        }
        return playerPairings;
    }

    /**
     * Creates a fully connected graph with players as nodes and edges as match pairing weights
     * @return graph of all possible pairings
     */
    Graph createFullGraph() {
        Graph graph = new Graph();
        List<Player> orderedPlayers = playerRepository.getOrderedPlayers();
        int lowestMatches = getMinHistory();
        boolean isFirstRun = true;
        Set<Player> addedPlayers = new HashSet<>();
        for (Player targetPlayer : orderedPlayers) {
            addedPlayers.add(targetPlayer);
            //add the currently considered player as a node
            Node targetPlayerNode = null;
            if (isFirstRun) {
                targetPlayerNode = new Node(targetPlayer.getId());
                graph.addNode(targetPlayerNode);
                //next iteration all players will have been added as nodes to the graph
                isFirstRun = false;
            } else {
                targetPlayerNode = graph.getNodeByName(targetPlayer.getId());
            }

            //add potential opponents for the player as nodes with respective weights
            for (Player opponent: orderedPlayers) {
                //prevent self edges
                if (opponent.equals(targetPlayer)) {
                    continue;
                }
                //add opponent as node to graph if not already there
                Node opponentNode = graph.getNodeByName(opponent.getId());
                if (opponentNode == null) {
                    opponentNode = new Node(opponent.getId());
                    graph.addNode(opponentNode);
                }
                // calculate weight based on rankings, games in hand and if the pair have already played
                int weight = Math.abs(targetPlayer.getLevel() - opponent.getLevel());
                List<History> historyList = historyRepository.getPlayerHistory(targetPlayer.getId());
                if (historyList != null && !historyList.isEmpty()) {
                    int matchesInHand = historyList.size() - lowestMatches;
                    if (historyList.size() - lowestMatches > 0) {
                        //this weighting grows exponentially
                        weight = (int) (weight + EXTRA_GAME_CONSTANT * Math.pow(100, matchesInHand));
                    }
                    for (History history : historyList) {
                        if (history.getOpponentId().equals(opponent.getId())) {
                            weight += PLAYED_OPPONENT_CONSTANT;
                            break;
                        }
                    }
                }
                targetPlayerNode.addDestination(opponentNode, weight);
            }
        }
        return graph;
    }

    /**
     * Gets the best player pairings as edges from the graph
     * @param maxPairings maximum number of pairings allowed
     * @return best pairing edges
     */
    List<Edge> getOptimalPairings(int maxPairings) {
        Graph fullGraph = createFullGraph();
        List<Edge> optimalEdges = null;
        int optimalWeight = Integer.MAX_VALUE;
        for (Node node : fullGraph.getNodes()) {
            Node nearestNeighbour = getNearestNeighbour(fullGraph, node);
            List<Edge> nearestNeighbourEdges = getNearestNeighbourEdges(nearestNeighbour);
            List<Edge> edges = selectBestEdges(nearestNeighbourEdges, maxPairings);
            int currentWeight = getEdgesTotalWeight(edges);
            if (currentWeight < optimalWeight) {
                optimalEdges = edges;
                optimalWeight = currentWeight;
            }
        }
        return optimalEdges;
    }

    /**
     * Finds the sum of weights of a given collection of edges
     * @param edges edges
     * @return weight
     */
    int getEdgesTotalWeight(Collection<Edge> edges) {
        int weight = 0;
        for (Edge e : edges) {
            weight += e.getWeight();
        }
        return weight;
    }

    /**
     * Selects the best edges from an ordered list of edges forming a path between all nodes in
     * the graph. These edges should be a list in path order
     * @param edges edges
     * @param target number of edges to be picked
     * @return edges
     */
    List<Edge> selectBestEdges(List<Edge> edges, int target) {
        if (target > Math.ceil(edges.size() / 2.0)) {
            throw new RuntimeException("Target should be less than half the number of edges so " +
                    "that no node has more than one neighbour");
        }
        List<Edge> selectedEdges = new ArrayList<>();
        if (target > Math.floor(edges.size() / 2.0)) {
            selectedEdges = getEdgesSingleSolution(edges);
        } else if (target == Math.floor(edges.size() / 2.0)) {
            selectedEdges = getEdgesDualSolution(edges);
        } else {
            selectedEdges = getEdgesMultiSolution(edges, target);
        }
        return selectedEdges;
    }

    /**
     * Returns a list of edges by choosing the first edge and every alternate edge from the
     * edges provided ending with the final edge. For use in cases where there can only be one
     * solution ie/ an odd number of edges.
     * @param edges list of edges ordered by path position
     * @return selected edges
     */
    List<Edge> getEdgesSingleSolution(List<Edge> edges) {
        //edges are already ordered in path direction
        List<Edge> selectedEdges = new ArrayList<>();
        for (int i = 0; i < edges.size(); i += 2) {
            selectedEdges.add(edges.get(i));
        }
        return selectedEdges;
    }

    /**
     * Similar to the single solution this chooses alternate edges in the path, however in even
     * cases it must also consider the complement of this set of nodes because that is also a
     * potential solution.
     * @param edges list of edges ordered by path position
     * @return the lowest weight of the 2 best possible solutions
     */
    private List<Edge> getEdgesDualSolution(List<Edge> edges) {
        List<Edge> selectedEdges;
        List<Edge> workingEdges1 = new ArrayList<>();
        List<Edge> workingEdges2 = new ArrayList<>();
        //edges are already ordered in path direction
        for (int i = 0; i < edges.size(); i+=2) {
            workingEdges1.add(edges.get(i));
        }
        for (int i = 1; i < edges.size(); i+=2) {
            workingEdges2.add(edges.get(i));
        }
        int w1 = getEdgesTotalWeight(workingEdges1);
        int w2 = getEdgesTotalWeight(workingEdges2);
        if (w2 < w1) {
            selectedEdges = workingEdges2;
        } else {
            selectedEdges = workingEdges1;
        }
        return selectedEdges;
    }

    /**
     * Picks lowest weight edge from the list provided and eliminates adjacent edges. Continues
     * to do this until it has picked the desired number of edges. Used when the target number is
     * less than half the number of edges.
     * @param edges list of edges ordered by path position
     * @param target desired number of edges
     * @return best edges
     */
    private List<Edge> getEdgesMultiSolution(List<Edge> edges, int target) {
        List<Edge> selectedEdges = new ArrayList<>();
        while (selectedEdges.size() < target) {
            int freeEdges = edges.size() - (target - selectedEdges.size()) * 2;
            Edge selected = null;
            int index = 0;
            int minWeight = Integer.MAX_VALUE;
            for (int i = 0; i < edges.size(); i++) {
                Edge e = edges.get(i);
                if (freeEdges < 1 && !hasLessThanTwoAdjacentEdges(edges, e)) {
                    continue;
                }
                if (e.getWeight() < minWeight) {
                    selected = e;
                    minWeight = e.getWeight();
                    index = i;
                }
            }
            selectedEdges.add(selected);
            edges.remove(selected);
            for (Edge remove : selected.getAdjacentEdges()) {
                edges.remove(remove);
            }
        }
        return selectedEdges;
    }

    /**
     * Check that the two adjacent edges of the currently considered edge are not both still
     * selectable so that we don't eliminate too many edges
     * @param selectableEdges edges which can still be chosen from
     * @param consideredEdge the current edge we're considering to add
     * @return true if 1 or 0 adjacent edges that could be eliminated
     */
    private boolean hasLessThanTwoAdjacentEdges(List<Edge> selectableEdges, Edge consideredEdge) {
        List<Edge> adjacentEdges = consideredEdge.getAdjacentEdges();
        int count = 0;
        for (Edge e : adjacentEdges) {
            if (selectableEdges.contains(e)) {
                count++;
            }
        }
        return count < 2;
    }

    /**
     * Converts a Node with nearest neighbours into a path ordered list of edges
     * @param startNode node from which the nearest neighbour path starts
     * @return nearest neighbour path edges
     */
    List<Edge> getNearestNeighbourEdges(Node startNode) {
        List<Edge> edges = new ArrayList<>();
        Node current = startNode;
        while (current != null) {
            Node nearestNeighbour = current.getNearestNeighbour();
            if (nearestNeighbour == null) {
                break;
            }
            Edge newEdge = new Edge(current.getName(), nearestNeighbour.getName(), current.getDistance());
            if (edges.size() > 0) {
                Edge previousEdge = edges.get(edges.size() - 1);
                previousEdge.getAdjacentEdges().add(newEdge);
                newEdge.getAdjacentEdges().add(previousEdge);
            }
            edges.add(newEdge);
            current = nearestNeighbour;
        }
        return edges;
    }

    /**
     * Finds the nearest neighbour path on a graph given a start node
     * @param graph full graph
     * @param start starting node
     * @return Nearest neighbour node acts like a linked list with a pointer field
     * (nearestNeighbour) to another node
     */
    Node getNearestNeighbour(Graph graph, Node start) {
        Set<Node> chosenNodes = new HashSet<>();
        chosenNodes.add(start);
        Node pathNode = start.clone();
        Node currentNode = pathNode;
        while (chosenNodes.size() < graph.getNodes().size()) {
            Map<Node, Integer> neighbourWeights = currentNode.getAdjacentNodes();
            Set<Node> neighbours = neighbourWeights.keySet();
            Node actual = null;
            Node clone = null;
            for (Node node : neighbours) {
                if (chosenNodes.contains(node)) {
                    continue;
                }
                //can ignore potential null pointer warnings because graph is always fully connected
                if (clone == null
                        || neighbourWeights.get(node) < neighbourWeights.get(actual)) {
                    actual = node;
                    clone = node.clone();
                }
            }
            chosenNodes.add(actual);
            currentNode.setNearestNeighbour(clone);
            currentNode.setDistance(neighbourWeights.get(actual));
            currentNode = clone;
        }
        return pathNode;
    }

    /**
     * @return smallest number of games played by any player
     */
    int getMinHistory() {
        List<Player> players = playerRepository.getOrderedPlayers();
        int minMatches = 99999;
        for (Player player : players) {
            List<History> playerHistory = historyRepository.getPlayerHistory(player.getId());
            if (playerHistory != null) {
                int numMatches = playerHistory.size();
                if (numMatches < minMatches) {
                    minMatches = numMatches;
                }
            }
        }
        return minMatches;
    }
}
