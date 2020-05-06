package alex.worrall.clubnightplanner.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import alex.worrall.clubnightplanner.persistence.models.CourtName;
import alex.worrall.clubnightplanner.persistence.models.CourtNameDao;

@Database(
        exportSchema = false,
        version = 1,
        entities = {
                CourtName.class
        }
)
public abstract class PlannerDatabase extends RoomDatabase {
    private static final String DB_NAME = "planner_db";
    private static PlannerDatabase instance;

    public static synchronized PlannerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PlannerDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract CourtNameDao courtNamesDao();
}
