package alex.worrall.clubnightplanner.mocks;

import android.app.Application;

import java.util.List;

import alex.worrall.clubnightplanner.model.history.History;
import alex.worrall.clubnightplanner.model.history.HistoryRepository;

public class MockHistoryRepository extends HistoryRepository {

    List<History> histories;

    public MockHistoryRepository() {
    }

    @Override
    public List<History> getPlayerHistory(String playerId) {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }
}
