package alex.worrall.clubnightplanner.model.fixture;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import alex.worrall.clubnightplanner.model.PlannerDatabase;
import alex.worrall.clubnightplanner.model.fixture.court.Court;
import alex.worrall.clubnightplanner.model.fixture.court.FixtureCourtDao;

public class FixtureRepository {
    private FixtureDao fixtureDao;
    private FixtureCourtDao fixtureCourtDao;
    private LiveData<List<Fixture>> fixtures;

    public FixtureRepository(Application application) {
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        fixtureDao = database.fixtureDao();
        fixtureCourtDao = database.fixtureCourtDao();
        fixtures = fixtureDao.getAllFixturesLive(0);
    }

    public LiveData<List<Fixture>> getFixturesLive() {
        return fixtures;
    }

    public List<Fixture> getFixtures() {
        return fixtureDao.getAllFixtures(0);
    }

    public Fixture getFixtureById(int id) {
        return fixtureDao.getFixtureById(id);
    }

    public void addFixture(Fixture fixture, List<Court> courts) {
        int fixtureId = (int) fixtureDao.insert(fixture);
        for (Court court : courts) {
            court.setFixtureId(fixtureId);
            fixtureCourtDao.insert(court);
        }
//        new insertFixtureAsyncTask(fixtureDao).execute(fixture);
    }

    public void deleteFixture(Fixture fixture) {
        fixtureDao.deleteFixture(fixture);
//        new deleteFixtureAsyncTask(fixtureDao).execute(fixture);
    }

    public void deleteSessionFixtures(int seshId) {
        new deleteAllFixturesAsyncTask(fixtureDao).execute(seshId);
    }

    public void updateFixture(Fixture fixture) {
        new updateFixtureAsyncTask(fixtureDao).execute(fixture);
    }

    public Fixture getMostRecentFixture() {
        return fixtureDao.getMostRecentFixture();
    }

    public Fixture getChangeableFixture() {
        return fixtureDao.getChangeableFixture();
    }

    public List<Fixture> getReschedulableFixtures() {
        return fixtureDao.getReschedulableFixtures();
    }

    public List<Fixture> getNonReschedulableFixtures() {
        return fixtureDao.getNonReschedulableFixtures();
    }

    public List<Court> getCourtsByFixtureId(int fixtureId) {
        return fixtureCourtDao.getCourtsByFixtureId(fixtureId);
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
