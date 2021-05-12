package alex.worrall.clubnightplanner.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import alex.worrall.clubnightplanner.model.court.CourtDao;
import alex.worrall.clubnightplanner.model.court.CourtName;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.model.fixture.FixtureDao;
import alex.worrall.clubnightplanner.model.history.History;
import alex.worrall.clubnightplanner.model.history.HistoryDao;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.model.player.PlayerDao;
import alex.worrall.clubnightplanner.model.settings.Preferences;
import alex.worrall.clubnightplanner.model.settings.PreferencesDao;
import alex.worrall.clubnightplanner.model.typeconverters.ListCourtConverter;
import alex.worrall.clubnightplanner.model.typeconverters.ListStringConverter;
import alex.worrall.clubnightplanner.model.typeconverters.StatusConverter;

@Database(
        version = 11,
        exportSchema = true,
        entities = {
            CourtName.class,
            Player.class,
            Fixture.class,
            History.class,
            Preferences.class
        }
)
@TypeConverters({
        ListStringConverter.class,
        StatusConverter.class,
        ListCourtConverter.class,
})
public abstract class PlannerDatabase extends RoomDatabase {

    private static final Migration MIGRATION_1_11 = new Migration(1, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE settings_preferences (name TEXT NOT NULL, " +
                    "session_length INTEGER NOT NULL, start_time INTEGER NOT NULL, is_active INTEGER" +
                    " NOT NULL, PRIMARY KEY (name))");
            database.execSQL("INSERT INTO settings_preferences VALUES('DEFAULT', 20, 1150, 1)");
        }
    };

    private static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE settings_preferences (name TEXT NOT NULL, session_length " +
                    "INTEGER NOT NULL, start_time INTEGER NOT NULL, is_active INTEGER NOT NULL, PRIMARY KEY (name))");
            database.execSQL("INSERT INTO settings_preferences VALUES('DEFAULT', 20, 1150, 1)");
        }
    };

    public abstract CourtDao courtDao();
    public abstract PlayerDao playerDao();
    public abstract FixtureDao fixtureDao();
    public abstract HistoryDao historyDao();
    public abstract PreferencesDao preferencesDao();

    private static PlannerDatabase INSTANCE;

    private static Callback CALLBACK = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("INSERT INTO settings_preferences VALUES('DEFAULT', 20, 1150, 1)");
        }
    };

    public static PlannerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlannerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlannerDatabase.class, "planner_database")
                            .addCallback(CALLBACK)
                            .addMigrations(MIGRATION_1_11, MIGRATION_10_11)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
