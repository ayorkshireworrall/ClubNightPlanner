package alex.worrall.clubnightplanner.model.settings;

import android.app.Application;

import alex.worrall.clubnightplanner.model.PlannerDatabase;

public class PreferencesRepository {
    private PreferencesDao dao;

    public PreferencesRepository(Application application) {
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        this.dao = database.preferencesDao();
    }

    public Preferences getActivePreferences() {
        return dao.getActivePreferences();
    }
}
