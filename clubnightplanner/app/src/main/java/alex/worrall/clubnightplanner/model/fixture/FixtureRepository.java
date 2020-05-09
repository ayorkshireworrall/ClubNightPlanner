package alex.worrall.clubnightplanner.model.fixture;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import alex.worrall.clubnightplanner.model.PlannerDatabase;

public class FixtureRepository {
    private FixtureDao fixtureDao;
    private LiveData<List<Fixture>> fixtures;

    public FixtureRepository(Application application) {
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        fixtureDao = database.fixtureDao();
        fixtures = fixtureDao.getAllFixtures(0);
    }

    public LiveData<List<Fixture>> getFixtures() {
        return fixtures;
    }

    public void addFixture(Fixture fixture) {
        new insertFixtureAsyncTask(fixtureDao).execute(fixture);
    }

    public void deleteFixture(Fixture fixture) {
        new deleteFixtureAsyncTask(fixtureDao).execute(fixture);
    }

    public void deleteSessionFixtures() {
        new deleteAllFixturesAsyncTask(fixtureDao).execute(0);
    }

    public void updateFixture(Fixture fixture) {
        new updateFixtureAsyncTask(fixtureDao).execute(fixture);
    }

    private static class insertFixtureAsyncTask extends AsyncTask<Fixture, Void, Void> {

        FixtureDao mAsyncTaskDao;

        public insertFixtureAsyncTask(FixtureDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Fixture... fixtures) {
            mAsyncTaskDao.insert(fixtures[0]);
            return null;
        }
    }

    private static class deleteFixtureAsyncTask extends  AsyncTask<Fixture, Void, Void> {

        FixtureDao mAsyncTaskDao;

        public deleteFixtureAsyncTask(FixtureDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Fixture... fixtures) {
            mAsyncTaskDao.deleteFixture(fixtures[0]);
            return null;
        }
    }

    private static class deleteAllFixturesAsyncTask extends AsyncTask<Integer, Void, Void> {

        FixtureDao mAsyncTaskDao;

        public deleteAllFixturesAsyncTask(FixtureDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mAsyncTaskDao.deleteAllSessionId(integers[0]);
            return null;
        }
    }

    private static class updateFixtureAsyncTask extends AsyncTask<Fixture, Void, Void> {

        FixtureDao mAsyncTaskDao;

        public updateFixtureAsyncTask(FixtureDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(Fixture... fixtures) {
            mAsyncTaskDao.updateFixture(fixtures[0]);
            return null;
        }
    }
}
