package alex.worrall.clubnightplanner.utils.schedulers;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import alex.worrall.clubnightplanner.mocks.MockCourtRepository;
import alex.worrall.clubnightplanner.mocks.MockFixtureRepository;
import alex.worrall.clubnightplanner.mocks.MockHistoryRepository;
import alex.worrall.clubnightplanner.mocks.MockPlayerRepository;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.utils.graphs.Edge;
import alex.worrall.clubnightplanner.utils.schedulers.SchedulerV3;

public class SchedulerV3Test {

    MockPlayerRepository playerRepository = new MockPlayerRepository();
    MockCourtRepository courtRepository = new MockCourtRepository();
    MockFixtureRepository fixtureRepository = new MockFixtureRepository();
    MockHistoryRepository historyRepository = new MockHistoryRepository();

    private Player playerA = new Player("A", 1, "A");
    private Player playerB = new Player("B", 2, "B");
    private Player playerC = new Player("C", 3, "C");
    private Player playerD = new Player("D", 4, "D");
    private Player playerE = new Player("E", 15, "E") ;
    private Player playerF = new Player("F", 6, "F");
    private Player playerG = new Player("G", 7, "G");
    private Player playerH = new Player("H", 8, "H");
    private Player playerI = new Player("I", 8, "H");
    private Player playerJ = new Player("J", 8, "H");
    private Player playerK = new Player("K", 8, "H");
    private Player playerL = new Player("L", 8, "H");
    private Player playerM = new Player("M", 8, "H");
    private Player playerN = new Player("N", 8, "H");
    private Player playerO = new Player("O", 8, "H");
    private Player playerP = new Player("P", 8, "H");
    private Player playerQ = new Player("Q", 8, "H");
    private Player playerR = new Player("R", 8, "H");
    private Player playerS = new Player("S", 8, "H");
    private Player playerT = new Player("T", 8, "H");
    private Player playerU = new Player("U", 8, "H");
    private Player playerV = new Player("V", 8, "H");
    private Player playerW = new Player("W", 8, "H");
    private Player playerX = new Player("X", 8, "H");
    private Player playerY = new Player("Y", 8, "H");
    private Player playerZ = new Player("Z", 8, "H");
    private Player playerA2 = new Player("A2", 1, "H");
    private Player playerB2 = new Player("B2", 2, "H");
    private Player playerC2 = new Player("C2", 3, "H");
    private Player playerD2 = new Player("D2", 4, "H");

    @Before
    public void setup() {
        List<Player> orderedPlayers = new ArrayList<>();

        orderedPlayers.add(playerA);
        orderedPlayers.add(playerB);
        orderedPlayers.add(playerC);
        orderedPlayers.add(playerD);
        orderedPlayers.add(playerE);
        orderedPlayers.add(playerF);
        orderedPlayers.add(playerG);
        orderedPlayers.add(playerH);
        orderedPlayers.add(playerI);
        orderedPlayers.add(playerJ);
        orderedPlayers.add(playerK);
        orderedPlayers.add(playerL);
        orderedPlayers.add(playerM);
        orderedPlayers.add(playerN);
        orderedPlayers.add(playerO);
        orderedPlayers.add(playerP);
        orderedPlayers.add(playerQ);
        orderedPlayers.add(playerR);
        orderedPlayers.add(playerS);
        orderedPlayers.add(playerT);
        orderedPlayers.add(playerU);
        orderedPlayers.add(playerV);
        orderedPlayers.add(playerW);
        orderedPlayers.add(playerX);
        orderedPlayers.add(playerY);
        orderedPlayers.add(playerZ);
        orderedPlayers.add(playerA2);
        orderedPlayers.add(playerB2);
        orderedPlayers.add(playerC2);
        orderedPlayers.add(playerD2);
        playerRepository.setMockPlayersList(orderedPlayers);
    }

    @Test
    public void testGraphCreation() {
        SchedulerV3 scheduler = new SchedulerV3(playerRepository, fixtureRepository,
                courtRepository, historyRepository);
        System.out.println("START");
        List<Edge> optimalPairings = scheduler.getOptimalPairings(7);
        System.out.println("DONE");
    }

    @Test
    public void roundingTest() {
        int a = 5;
        int b = 2;
        System.out.println(Math.ceil(a / 2.0));
        System.out.println(b < Math.ceil(a / 2.0));
    }
}
