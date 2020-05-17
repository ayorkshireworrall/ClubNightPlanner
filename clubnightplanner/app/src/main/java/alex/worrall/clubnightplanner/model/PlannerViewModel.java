package alex.worrall.clubnightplanner.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import alex.worrall.clubnightplanner.model.court.CourtName;
import alex.worrall.clubnightplanner.model.court.CourtRepository;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.model.fixture.FixtureRepository;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.model.player.PlayerRepository;

public class PlannerViewModel extends AndroidViewModel {

    private CourtRepository mCourtRepository;
    private PlayerRepository mPlayerRepository;
    private FixtureRepository mFixtureRepository;

    private LiveData<List<CourtName>> mAllCourts;
    private LiveData<List<Player>> mAllPlayers;
    private LiveData<List<Fixture>> mAllFixtures;

    public PlannerViewModel(@NonNull Application application) {
        super(application);
        mCourtRepository = new CourtRepository(application);
        mPlayerRepository = new PlayerRepository(application);
        mFixtureRepository = new FixtureRepository(application);
        mAllCourts = mCourtRepository.getAllCourts();
        mAllPlayers = mPlayerRepository.getActivePlayers();
        mAllFixtures = mFixtureRepository.getFixtures();
    }

    public LiveData<List<CourtName>> getAllCourts() {
        return mAllCourts;
    }

    public void addCourt(String courtName) {
        mCourtRepository.insert(new CourtName(courtName));
    }

    public void deleteCourt(CourtName courtName) {
        mCourtRepository.deleteCourt(courtName);
    }

    public void deleteAllSessionCourts(int seshId) {
        mCourtRepository.deleteSessionCourts(seshId);
    }

    public LiveData<List<Player>> getActivePlayers() {
        return mAllPlayers;
    }

    public void addPlayer(Player player) {
        mPlayerRepository.addPlayer(player);
    }

    public Player getPlayer(String playerId) {
        return mPlayerRepository.getPlayerById(playerId);
    }

    public void updatePlayer(Player player) {
        mPlayerRepository.updatePlayer(player);
    }

    public void deletePlayer(Player player) {
        mPlayerRepository.deletePlayer(player);
    }

    public void deleteAllPlayers() {
        mPlayerRepository.deleteAllPlayers();
    }

    public LiveData<List<Fixture>> getAllFixtures() {
        return mAllFixtures;
    }

    public void deleteAllSessionFixtures(int seshId) {
        mFixtureRepository.deleteSessionFixtures(seshId);
    }


}
