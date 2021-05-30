package alex.worrall.clubnightplanner.model.settings;

import android.app.Application;

import java.util.List;

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

    public List<Preferences> getAllPreferences() {
        return dao.getPreferences();
    }

    public void updatePreferences(Preferences preferences) {
        dao.updatePreferences(preferences);
    }
}
