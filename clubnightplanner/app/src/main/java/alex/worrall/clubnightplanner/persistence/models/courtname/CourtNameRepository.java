package alex.worrall.clubnightplanner.persistence.models.courtname;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import alex.worrall.clubnightplanner.persistence.PlannerDatabase;

public class CourtNameRepository {
    private CourtNameDao courtNameDao;
    private List<CourtName> courtNames;


    public CourtNameRepository(Context context) {
        PlannerDatabase database = PlannerDatabase.getInstance(context);
        courtNameDao = database.courtNamesDao();
        courtNames = courtNameDao.getCourtNameList();
    }

    public List<CourtName> getCourtNames() {
        return courtNames;
    }

    public void insertCourtName(CourtName courtName) {
        new insertAsyncTask(courtNameDao).execute(courtName);
    }

    public void deleteCourtName(CourtName courtName) {
        new deleteAsyncTask(courtNameDao).execute(courtName);
    }

    public void deleteBySessionId(int id) {
        new deleteSessionAsyncTask(courtNameDao).execute(id);
    }


    private static class insertAsyncTask extends AsyncTask<CourtName, Void, Void> {

        private CourtNameDao mAsyncTaskDao;

        insertAsyncTask(CourtNameDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CourtName... params) {
            mAsyncTaskDao.insertCourtName(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<CourtName, Void, Void> {

        private CourtNameDao mAsyncTaskDao;

        deleteAsyncTask(CourtNameDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CourtName... params) {
            mAsyncTaskDao.deleteCourtName(params[0].getId());
            return null;
        }
    }

    private static class deleteSessionAsyncTask extends AsyncTask<Integer, Void, Void> {

        private CourtNameDao mAsyncTaskDao;

        deleteSessionAsyncTask(CourtNameDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteSession(params[0]);
            return null;
        }
    }

}
