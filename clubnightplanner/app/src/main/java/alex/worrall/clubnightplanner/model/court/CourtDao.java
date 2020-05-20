package alex.worrall.clubnightplanner.model.court;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourtDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CourtName courtName);

    @Query("DELETE FROM court_names WHERE session_id = :sessionId")
    void deleteAllSessionId(int sessionId);

    @Delete
    void deleteCourt(CourtName courtName);

    @Query("SELECT * FROM court_names WHERE session_id = :sessionId ORDER BY name ASC")
    LiveData<List<CourtName>> getAllCourtsLiveBySessionId(int sessionId);

    @Query("SELECT * FROM court_names WHERE session_id = :sessionId ORDER BY name ASC")
    List<CourtName> getAllCourtsBySessionId(int sessionId);
}
