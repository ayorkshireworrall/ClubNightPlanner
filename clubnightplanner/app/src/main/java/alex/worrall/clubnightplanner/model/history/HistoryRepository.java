package alex.worrall.clubnightplanner.model.history;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import alex.worrall.clubnightplanner.model.PlannerDatabase;

public class HistoryRepository {
    private HistoryDao dao;

    public HistoryRepository(Application application) {
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        this.dao = database.historyDao();
    }

    public List<History> getPlayerHistory(String playerId) {
        return dao.getPlayerHistory(playerId);
    }

    public void deleteHistory(String playerId, String opponentId) {
        new deleteHistoryAsyncTask(dao).execute(playerId, opponentId);
    }

    public void insertHistory(History history) {
        new insertHistoryAsyncTask(dao).execute(history);
    }

    private static class deleteHistoryAsyncTask extends AsyncTask<String, Void, Void> {
        HistoryDao dao;

        public deleteHistoryAsyncTask(HistoryDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(String... playerIds) {
            dao.deleteHistory(playerIds[0], playerIds[1]);
            return null;
        }
    }

    private static class insertHistoryAsyncTask extends AsyncTask<History, Void, Void> {
        HistoryDao dao;

        public insertHistoryAsyncTask(HistoryDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(History... histories) {
            dao.insertHistory(histories[0]);
            return null;
        }
    }
}
