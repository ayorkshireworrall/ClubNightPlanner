package alex.worrall.clubnightplanner.model.fixture;

import alex.worrall.clubnightplanner.model.player.Player;

public class Court implements Comparable<Court> {
    private String courtName;
    private Player playerA;
    private Player playerB;

    public Court(String courtName, Player playerA, Player playerB) {
        this.courtName = courtName;
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public Player getPlayerA() {
        return playerA;
    }

    public void setPlayerA(Player playerA) {
        this.playerA = playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setPlayerB(Player playerB) {
        this.playerB = playerB;
    }

    @Override
    public int compareTo(Court o) {
        return courtName.compareTo(o.getCourtName());
    }
}
