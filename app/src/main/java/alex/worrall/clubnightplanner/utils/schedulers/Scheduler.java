package alex.worrall.clubnightplanner.utils.schedulers;

import java.util.ArrayList;
import java.util.List;

import alex.worrall.clubnightplanner.model.court.CourtRepository;
import alex.worrall.clubnightplanner.model.fixture.Court;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.model.fixture.FixtureRepository;
import alex.worrall.clubnightplanner.model.history.HistoryRepository;
import alex.worrall.clubnightplanner.model.player.PlayerRepository;

public abstract class Scheduler {
    PlayerRepository playerRepository;
    FixtureRepository fixtureRepository;
    CourtRepository courtRepository;
    HistoryRepository historyRepository;

    public abstract void generateSchedule(int timeslot, List<String> availableCourts);

    public void unschedule(Fixture fixture) {
        List<Court> courts = fixture.getCourts();
        for (Court court : courts) {
            if (court.getPlayerA() == null) {
                continue;
            }
            String id1 = court.getPlayerA().getId();
            String id2 = court.getPlayerB().getId();
            historyRepository.deleteHistory(id1, id2);
            historyRepository.deleteHistory(id2, id1);
        }
        fixtureRepository.deleteFixture(fixture);
    }

    public void reschedule(List<Fixture> laterFixtures) {
        for (Fixture later : laterFixtures) {
            unschedule(later);
        }
        for (Fixture later : laterFixtures) {
            List<String> courtnames = new ArrayList<>();
            for (Court court : later.getCourts()) {
                courtnames.add(court.getCourtName());
            }
            generateSchedule(later.getTimeslot(), courtnames);
        }
    }
}
