package alex.worrall.clubnightplanner.model.fixture.court;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FixtureCourtDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert (Court court);

    @Query("SELECT * FROM fixture_courts WHERE fixture_id = :fixtureId")
    List<Court> getCourtsByFixtureId(int fixtureId);
}
