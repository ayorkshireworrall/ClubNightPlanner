package alex.worrall.clubnightplanner.persistence.models.player;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {
    @Query("Select * from players")
    List<Player> getPlayerList();
    @Insert
    void insertPlayer(Player player);
    @Update
    void updatePlayer(Player player);
    @Delete
    void deletePlayer(Player player);
}
