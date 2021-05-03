package alex.worrall.clubnightplanner.model.settings;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings_preferences")
public class Preferences {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "session_length")
    private int sessionLength;
    @ColumnInfo(name = "start_time")
    private int startTime;
    @ColumnInfo(name = "is_active")
    private boolean isActive;

    public Preferences(int sessionLength, int startTime) {
        this.sessionLength = sessionLength;
        this.startTime = startTime;
    }

    public int getSessionLength() {
        return sessionLength;
    }

    public void setSessionLength(int sessionLength) {
        this.sessionLength = sessionLength;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
