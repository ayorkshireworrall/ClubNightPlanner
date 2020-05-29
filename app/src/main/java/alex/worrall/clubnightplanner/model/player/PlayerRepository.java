package alex.worrall.clubnightplanner.model.player;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.model.PlannerDatabase;

public class PlayerRepository {
    private PlayerDao playerDao;
    private LiveData<List<Player>> activePlayers;
    private List<Player> orderedPlayers;
    private Map<String, String> dulllNameMap;

    public PlayerRepository(Application application) {
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        playerDao = database.playerDao();
        activePlayers = playerDao.getPlayers();
        orderedPlayers = playerDao.getOrderedPlayers();
        dulllNameMap = doNameMapping();
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
        String modifiedName = dulllNameMap.get(name) == null ? name : dulllNameMap.get(name);
        return playerDao.getPlayerByName(modifiedName);
    }

    public void addPlayer(Player player) {
        String name = player.getName();
        String modifiedName = dulllNameMap.get(name);
        if (modifiedName != null) {
            player.setName(modifiedName);
        }
        new insertPlayerAsyncTask(playerDao).execute(player);
    }

    public void deletePlayer(Player player) {
        new deletePlayerAsyncTask(playerDao).execute(player);
    }

    public void deleteAllPlayers() {
        new deleteAllPlayersAsyncTask(playerDao).execute();
    }

    public void updatePlayer(Player player) {
        String name = player.getName();
        String modifiedName = dulllNameMap.get(name);
        if (modifiedName != null) {
            player.setName(modifiedName);
        }
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

    private Map<String, String> doNameMapping() {
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("Ivor Green", "Ivor Punctuality Problem");
        nameMap.put("Oli Palmer", "Oli Face Palmer");
        nameMap.put("Oly Palmer", "Oli Face Palmer");
        nameMap.put("Olly Palmer", "Oli Face Palmer");
        nameMap.put("Ollie Palmer", "Oli Face Palmer");
        nameMap.put("Oliver Palmer", "Oli Face Palmer");
        nameMap.put("Ali Hoffman", "Ali Owes Everyone a Pint");
        nameMap.put("Ali Hoffman de Visme", "Ali Owes Everyone a Pint");
        nameMap.put("Ali de Visme", "Ali Owes Everyone a Pint");
        nameMap.put("Ben Bickers", "Bickers Never Drops From The Back");
        nameMap.put("Benjamin Bickers", "Bickers Never Drops From The Back");
        nameMap.put("Tom Withers", "The Mighty Welsh Rump");
        nameMap.put("Thomas Withers", "The Mighty Welsh Rump");
        nameMap.put("Harry Wildy", "Wildebeest");
        nameMap.put("Harry Wildey", "Wildebeest");
        nameMap.put("Bec Dixon", "Bec Nicks Not Nuts");
        nameMap.put("Rebecca Dixon", "Bec Nicks Not Nuts");
        nameMap.put("James Wass", "Cocky Lamp Post");
        nameMap.put("Jim Wass", "Cocky Lamp Post");
        nameMap.put("Jimmy Wass", "Cocky Lamp Post");
        nameMap.put("Henry Worrall", "Sir Henry of the Tin");
        nameMap.put("Henry Worral", "Sir Henry of the Tin");
        nameMap.put("Henry Woral", "Sir Henry of the Tin");
        nameMap.put("Henry Worall", "Sir Henry of the Tin");
        nameMap.put("Henry Worrel", "Sir Henry of the Tin");
        nameMap.put("Henry Worrell", "Sir Henry of the Tin");
        nameMap.put("Henry Worell", "Sir Henry of the Tin");
        nameMap.put("Ben Bryant", "Bryant's Tower of Power");
        nameMap.put("Benjamin Bryant", "Bryant's Tower of Power");
        nameMap.put("Alex Osbourne", "Alex Ladbourne");
        nameMap.put("Alexander Osbourne", "Alex Ladbourne");
        nameMap.put("Joe Weavers", "Mr Beavers");
        nameMap.put("Joseph Weavers", "Mr Beavers");
        nameMap.put("Tom Howard", "Tom Howard That Happen?");
        nameMap.put("Thomas Howard", "Tom Howard That Happen?");
        return nameMap;
    }
}
