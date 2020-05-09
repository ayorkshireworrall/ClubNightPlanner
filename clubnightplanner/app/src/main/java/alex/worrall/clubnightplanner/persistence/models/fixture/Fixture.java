package alex.worrall.clubnightplanner.persistence.models.fixture;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.UUID;

import alex.worrall.clubnightplanner.service.Status;
import alex.worrall.clubnightplanner.service.TimeUtil;
import alex.worrall.clubnightplanner.persistence.models.fixture.court.Court;

@Entity(tableName = "fixtures")
public class Fixture {
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo(name = "time_slot")
    private int timeSlot;
    @ColumnInfo(name = "courts")
    private List<Court> courts;
    @ColumnInfo(name = "play_status")
    private Status playStatus;
    @ColumnInfo(name = "session_id")
    private int sessionId;

    public Fixture(int timeSlot, List<Court> courts) {
        this.id = UUID.randomUUID().toString();
        this.timeSlot = timeSlot;
        this.courts = courts;
        this.playStatus = Status.LATER;
        this.sessionId = 0;
    }

    public void setPlayStatus(Status playStatus) {
        this.playStatus = playStatus;
    }

    public Status getPlayStatus() {
        return playStatus;
    }

    public List<Court> getCourts() {
        return courts;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return TimeUtil.timeConverter(this.timeSlot);
    }
}

