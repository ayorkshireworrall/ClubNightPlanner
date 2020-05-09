package alex.worrall.clubnightplanner.model.fixture;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

import alex.worrall.clubnightplanner.utils.Status;

@Entity(tableName = "fixtures")
public class Fixture {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "timeslot")
    private int timeslot;
    @ColumnInfo(name = "play_status")
    private Status playStatus;
    @ColumnInfo(name = "courts")
    private List<Court> courts;
    @ColumnInfo(name = "session_id")
    private int sessionId;

    public Fixture(int timeslot, List<Court> courts) {
        this.timeslot = timeslot;
        this.courts = courts;
        this.playStatus = Status.LATER;
    }

    public int getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    public Status getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(Status playStatus) {
        this.playStatus = playStatus;
    }

    public List<Court> getCourts() {
        return courts;
    }

    public void setCourts(List<Court> courts) {
        this.courts = courts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
