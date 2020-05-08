package alex.worrall.clubnightplanner.persistence.models.courtname;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "court_names")
public class CourtName implements Comparable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "session_id")
    private int sessionId;

    public CourtName(int id, String name, int sessionId) {
        this.id = id;
        this.name = name;
        this.sessionId = sessionId;
    }

    @Ignore
    public CourtName(String name) {
        this.name = name;
        this.sessionId = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
