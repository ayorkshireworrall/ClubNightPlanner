package alex.worrall.clubnightplanner.persistence.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourtNameDao {
    @Query("Select * from court_names")
    List<CourtName> getCourtNameList();
    @Insert
    void insertCourtName(CourtName courtName);
    @Update
    void updateCourtName(CourtName courtName);
    @Delete
    void deleteCourtName(CourtName courtName);
}
