package alex.worrall.clubnightplanner.services;

import alex.worrall.clubnightplanner.models.Player;
import alex.worrall.clubnightplanner.models.Schedule;

import java.util.List;
import java.util.Map;

public interface Scheduler {
    public Map<Integer, Schedule> getSchedules();
    public void addPlayer(String name, int level);
    public List<Player> getPlayers();
    public void markScheduleComplete(Schedule schedule);
}
