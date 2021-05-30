package alex.worrall.clubnightplanner.model.court;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import alex.worrall.clubnightplanner.model.PlannerDatabase;

public class CourtRepository {
    private CourtDao dao;
    private LiveData<List<CourtName>> courts;

    public CourtRepository() {
    }

    public CourtRepository(Application application) {
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        this.dao =database.courtDao();
        this.courts = dao.getAllCourtsLiveBySessionId(0);
    }

    public LiveData<List<CourtName>> getAllCourtsLive() {
        return courts;
    }

    public List<CourtName> getAllCourts() {
        return dao.getAllCourtsBySessionId(0);
    }

    public void insert(CourtName courtName) {
        new insertCourtAsyncTask(dao).execute(courtName);
    }

    public void deleteCourt(CourtName courtName) {
        new deleteCourtAsyncTask(dao).execute(courtName);
    }

    public void deleteSessionCourts(int seshId) {
        new deleteSessionCourtsAsyncTask(dao).execute(seshId);
    }

    private static class insertCourtAsyncTask extends AsyncTask<CourtName, Void, Void> {

        private CourtDao mAsyncTaskDao;

        insertCourtAsyncTask(CourtDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CourtName... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteSessionCourtsAsyncTask extends AsyncTask<Integer, Void, Void> {
        private CourtDao mAsyncTaskDao;

        deleteSessionCourtsAsyncTask(CourtDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mAsyncTaskDao.deleteAllSessionId(params[0]);
            return null;
        }
    }

    private static class deleteCourtAsyncTask extends AsyncTask<CourtName, Void, Void> {
        private CourtDao mAsyncTaskDao;

        deleteCourtAsyncTask(CourtDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CourtName... params) {
            mAsyncTaskDao.deleteCourt(params[0]);
            return null;
        }
    }
}
