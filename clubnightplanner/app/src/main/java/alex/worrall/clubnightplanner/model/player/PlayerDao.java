package alex.worrall.clubnightplanner.model.player;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Player player);

    @Query("DELETE FROM players")
    void deleteAll();

    @Delete
    void deletePlayer(Player player);

    @Query("SELECT * FROM players")
    LiveData<List<Player>> getPlayers();

    @Update
    void updatePlayer(Player player);
}
