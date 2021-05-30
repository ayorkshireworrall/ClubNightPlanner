package alex.worrall.clubnightplanner.model.settings;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PreferencesDao {
    @Query("SELECT * FROM settings_preferences")
    List<Preferences> getPreferences();

    @Query("SELECT * FROM settings_preferences where is_active=1")
    Preferences getActivePreferences();

    @Update
    void updatePreferences(Preferences preferences);
}
