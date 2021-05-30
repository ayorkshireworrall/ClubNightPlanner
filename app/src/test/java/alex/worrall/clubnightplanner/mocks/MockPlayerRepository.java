package alex.worrall.clubnightplanner.mocks;

import android.app.Application;

import java.util.List;

import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.model.player.PlayerRepository;

public class MockPlayerRepository extends PlayerRepository {
    List<Player> mockPlayersList;

    public MockPlayerRepository() {
    }

    @Override
    public List<Player> getOrderedPlayers() {
        return mockPlayersList;
    }

    public List<Player> getMockPlayersList() {
        return mockPlayersList;
    }

    public void setMockPlayersList(List<Player> mockPlayersList) {
        this.mockPlayersList = mockPlayersList;
    }
}
