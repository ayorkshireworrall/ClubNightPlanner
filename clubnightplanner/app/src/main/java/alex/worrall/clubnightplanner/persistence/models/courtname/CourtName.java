package alex.worrall.clubnightplanner.persistence.models.courtname;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "court_names")
public class CourtName implements Comparable {
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "session_id")
    private int sessionId;

    public CourtName(String name) {
        this.name = name;
        this.sessionId = 0;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int compareTo(CourtName courtName) {
        return this.getName().compareTo(courtName.getName());
    }

    @Override
    public int compareTo(Object o) {
        return this.getName().compareTo(((CourtName) o).getName());
    }
}
