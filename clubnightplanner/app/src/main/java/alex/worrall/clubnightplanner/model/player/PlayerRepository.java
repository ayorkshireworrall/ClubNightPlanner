package alex.worrall.clubnightplanner.model.player;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import alex.worrall.clubnightplanner.model.PlannerDatabase;

public class PlayerRepository {
    private PlayerDao playerDao;
    private LiveData<List<Player>> activePlayers;
    private List<Player> orderedPlayers;

    public PlayerRepository(Application application) {
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        playerDao = database.playerDao();
        activePlayers = playerDao.getPlayers();
        orderedPlayers = playerDao.getOrderedPlayers();
    }

    public LiveData<List<Player>> getActivePlayers() {
        return this.activePlayers;
    }

    public List<Player> getOrderedPlayers() {
        return this.orderedPlayers;
    }

    public Player getPlayerById(String playerId) {
        return playerDao.getPlayerById(playerId);
    }

    public Player getPlayerByName(String name) {
        return playerDao.getPlayerByName(name);
    }

    public void addPlayer(Player player) {
        new insertPlayerAsyncTask(playerDao).execute(player);
    }

    public void deletePlayer(Player player) {
        new deletePlayerAsyncTask(playerDao).execute(player);
    }

    public void deleteAllPlayers() {
        new deleteAllPlayersAsyncTask(playerDao).execute();
    }

    public void updatePlayer(Player player) {
        new updatePlayerAsyncTask(playerDao).execute(player);
    }

    private static class insertPlayerAsyncTask extends AsyncTask<Player, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        insertPlayerAsyncTask(PlayerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Player... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deletePlayerAsyncTask extends AsyncTask<Player, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        deletePlayerAsyncTask(PlayerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Player... params) {
            mAsyncTaskDao.deletePlayer(params[0]);
            return null;
        }
    }

    private static class deleteAllPlayersAsyncTask extends AsyncTask<Void, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        deleteAllPlayersAsyncTask(PlayerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class updatePlayerAsyncTask extends AsyncTask<Player, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        public updatePlayerAsyncTask(PlayerDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(Player... players) {
            mAsyncTaskDao.updatePlayer(players[0]);
            return null;
        }
    }
}
