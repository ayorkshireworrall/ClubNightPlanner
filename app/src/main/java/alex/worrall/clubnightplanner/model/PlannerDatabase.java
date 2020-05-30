package alex.worrall.clubnightplanner.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import alex.worrall.clubnightplanner.model.court.CourtDao;
import alex.worrall.clubnightplanner.model.court.CourtName;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.model.fixture.FixtureDao;
import alex.worrall.clubnightplanner.model.fixture.court.Court;
import alex.worrall.clubnightplanner.model.fixture.court.FixtureCourtDao;
import alex.worrall.clubnightplanner.model.history.History;
import alex.worrall.clubnightplanner.model.history.HistoryDao;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.model.player.PlayerDao;
import alex.worrall.clubnightplanner.model.typeconverters.ListCourtConverter;
import alex.worrall.clubnightplanner.model.typeconverters.ListStringConverter;
import alex.worrall.clubnightplanner.model.typeconverters.PlayerConverter;
import alex.worrall.clubnightplanner.model.typeconverters.StatusConverter;

@Database(
        version = 11,
        exportSchema = false,
        entities = {
            CourtName.class,
            Player.class,
            Fixture.class,
            History.class,
            Court.class,
        }
)
@TypeConverters({
        ListStringConverter.class,
        StatusConverter.class,
        ListCourtConverter.class,
        PlayerConverter.class
})
public abstract class PlannerDatabase extends RoomDatabase {

    public abstract CourtDao courtDao();
    public abstract PlayerDao playerDao();
    public abstract FixtureDao fixtureDao();
    public abstract HistoryDao historyDao();
    public abstract FixtureCourtDao fixtureCourtDao();

    private static PlannerDatabase INSTANCE;

    public static PlannerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlannerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlannerDatabase.class, "planner_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
