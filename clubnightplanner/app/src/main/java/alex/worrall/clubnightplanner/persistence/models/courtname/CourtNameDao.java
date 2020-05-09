package alex.worrall.clubnightplanner.persistence.models.courtname;

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
    @Query("DELETE FROM court_names WHERE id = :courtNameId")
    void deleteCourtName(Integer courtNameId);
    @Query("DELETE FROM court_names where session_id = :sessionId")
    void deleteSession(long sessionId);
}
