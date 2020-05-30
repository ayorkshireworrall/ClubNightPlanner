package alex.worrall.clubnightplanner.model.fixture;

import alex.worrall.clubnightplanner.model.player.Player;

public class Court implements Comparable<Court> {
    private String courtName;
    private Player playerA;
    private Player playerB;
    private int timeslot;

    public Court(String courtName, Player playerA, Player playerB, int timeslot) {
        this.courtName = courtName;
        this.playerA = playerA;
        this.playerB = playerB;
        this.timeslot = timeslot;
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

    public int getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public int compareTo(Court o) {
        return courtName.compareTo(o.getCourtName());
    }
}
