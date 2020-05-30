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
    long insert(Fixture fixture);

    @Query("SELECT * FROM fixtures where session_id = :sessionId ORDER BY timeslot ASC")
    LiveData<List<Fixture>> getAllFixturesLive(int sessionId);

    @Query("SELECT * FROM fixtures where session_id = :sessionId ORDER BY timeslot ASC")
    List<Fixture> getAllFixtures(int sessionId);

    @Delete
    void deleteFixture(Fixture fixture);

    @Query("DELETE FROM fixtures WHERE session_id = :sessionId")
    void deleteAllSessionId(int sessionId);

    @Update
    void updateFixture(Fixture fixture);

    @Query("SELECT * FROM fixtures WHERE play_status = 'COMPLETED' OR play_status = 'IN_PROGRESS'" +
            "AND session_id = 0 ORDER BY timeslot DESC LIMIT 1")
    Fixture getMostRecentFixture();

    @Query("SELECT * FROM fixtures WHERE id = :id")
    Fixture getFixtureById(int id);

    //TODO reuse when > query actually works
//    @Query("SELECT * FROM fixtures WHERE timeslot > :timeslot & session_id = 0 ORDER BY timeslot " +
//            "ASC LIMIT 1")
//    Fixture getNextFixture(int timeslot);
//
//    @Query("SELECT * FROM fixtures WHERE timeslot > :timeslot & session_id = 0")
//    List<Fixture> getFollowingFixtures(int timeslot);

    /**
     * If there are no "IN PROGRESS" fixtures, the first "LATEST" fixture can be started so we
     * return this. Else return the "IN PROGRESS" fixture
     * @return a fixture that can be started or completed
     */
    @Query("SELECT * FROM fixtures WHERE play_status = 'IN_PROGRESS' OR play_status = 'LATER' AND" +
            " session_id = 0 ORDER BY timeslot ASC LIMIT 1")
    Fixture getChangeableFixture();

    @Query("SELECT * FROM fixtures WHERE play_status = 'NEXT' OR play_status = 'LATER' AND " +
            "session_id = 0")
    List<Fixture> getReschedulableFixtures();

    @Query("SELECT * FROM fixtures WHERE play_status = 'COMPLETED' OR play_status = 'IN_PROGRESS'" +
            " AND session_id = 0")
    List<Fixture> getNonReschedulableFixtures();
}
