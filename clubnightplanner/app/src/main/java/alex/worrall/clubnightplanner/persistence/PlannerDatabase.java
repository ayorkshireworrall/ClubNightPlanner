package alex.worrall.clubnightplanner.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import alex.worrall.clubnightplanner.persistence.models.courtname.CourtName;
import alex.worrall.clubnightplanner.persistence.models.courtname.CourtNameDao;
import alex.worrall.clubnightplanner.persistence.models.fixture.Fixture;
import alex.worrall.clubnightplanner.persistence.models.fixture.FixtureDao;
import alex.worrall.clubnightplanner.persistence.models.player.Player;
import alex.worrall.clubnightplanner.persistence.models.player.PlayerDao;
import alex.worrall.clubnightplanner.persistence.typeconverters.ListCourtConverter;
import alex.worrall.clubnightplanner.persistence.typeconverters.ListStringConverter;
import alex.worrall.clubnightplanner.persistence.typeconverters.StatusConverter;

@Database(
        exportSchema = false,
        version = 4,
        entities = {
                Fixture.class,
                CourtName.class,
                Player.class,
        }
)
@TypeConverters({
        ListStringConverter.class,
        ListCourtConverter.class,
        StatusConverter.class
})
public abstract class PlannerDatabase extends RoomDatabase {
    private static final String DB_NAME = "planner_db";
    private static PlannerDatabase instance;

    public static synchronized PlannerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PlannerDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract CourtNameDao courtNamesDao();
    public abstract PlayerDao playerDao();
    public abstract FixtureDao fixtureDao();
}
