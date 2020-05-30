package alex.worrall.clubnightplanner.model.fixture.court;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import alex.worrall.clubnightplanner.model.player.Player;
@Entity(tableName = "fixture_courts")
public class Court implements Comparable<Court> {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "timeslot")
    private int timeslot;
    @ColumnInfo(name = "court_name")
    private String courtName;
    @ColumnInfo(name = "player_a")
    private Player playerA;
    @ColumnInfo(name = "player_b")
    private Player playerB;
    @ColumnInfo(name = "fixture_id")
    private int fixtureId;

    public Court(int timeslot, String courtName, Player playerA, Player playerB) {
        this.timeslot = timeslot;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    public int getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(int fixtureId) {
        this.fixtureId = fixtureId;
    }

    @Override
    public int compareTo(Court o) {
        return courtName.compareTo(o.getCourtName());
    }
}
