package alex.worrall.clubnightplanner.persistence.models.fixture;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FixtureDao {
    @Query("Select * from fixtures")
    List<Fixture> getFixtureList();
    @Insert
    void insertFixture(Fixture fixture);
    @Update
    void updateFixture(Fixture fixture);
    @Delete
    void deleteFixture(Fixture fixture);
}
