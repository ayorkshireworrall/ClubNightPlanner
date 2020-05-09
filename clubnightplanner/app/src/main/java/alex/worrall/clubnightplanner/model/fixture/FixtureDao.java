package alex.worrall.clubnightplanner.model.fixture;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FixtureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Fixture fixture);

    @Query("SELECT * FROM fixtures where session_id = :sessionId ORDER BY timeslot ASC")
    LiveData<List<Fixture>> getAllFixtures(int sessionId);

    @Delete
    void deleteFixture(Fixture fixture);

    @Query("DELETE FROM fixtures WHERE session_id = :sessionId")
    void deleteAllSessionId(int sessionId);

    @Update
    void updateFixture(Fixture fixture);
}
