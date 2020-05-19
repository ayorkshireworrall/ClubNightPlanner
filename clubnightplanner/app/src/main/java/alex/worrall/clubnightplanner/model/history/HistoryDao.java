package alex.worrall.clubnightplanner.model.history;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM player_history")
    List<History> getAllHistories();

    @Query("SELECT * FROM player_history WHERE player_id = :playerId")
    List<History> getPlayerHistory(String playerId);

    @Query("DELETE FROM player_history WHERE player_id = :playerId AND opponent_id = :opponentId")
    void deleteHistory(String playerId, String opponentId);

    @Insert
    void insertHistory(History history);
}
