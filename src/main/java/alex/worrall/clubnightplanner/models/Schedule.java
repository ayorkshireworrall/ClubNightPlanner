package alex.worrall.clubnightplanner.models;

import java.util.List;

public class Schedule {
    int timeSlot;
    List<Court> courts;
    boolean unPlayed;

    public Schedule() {
    }

    public Schedule(int timeSlot, List<Court> courts) {
        this.timeSlot = timeSlot;
        this.courts = courts;
        this.unPlayed = true;
    }

    public void setUnPlayed(boolean unPlayed) {
        this.unPlayed = unPlayed;
    }

    public List<Court> getCourts() {
        return courts;
    }
}
