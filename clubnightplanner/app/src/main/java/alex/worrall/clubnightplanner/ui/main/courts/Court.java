package alex.worrall.clubnightplanner.ui.main.courts;

import alex.worrall.clubnightplanner.persistence.models.courtname.CourtName;
import alex.worrall.clubnightplanner.ui.main.players.Player;

public class Court {
    CourtName courtName;
    Player playerA;
    Player playerB;

    public Court() {
    }

    public Court(CourtName courtName, Player playerA, Player playerB) {
        this.courtName = courtName;
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public CourtName getCourtName() {
        return courtName;
    }

    public void setCourtName(CourtName courtName) {
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
}

