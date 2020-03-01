package alex.worrall.clubnightplanner.models;

import java.util.List;

public class Schedule {
    int timeSlot;
    List<Court> courts;
    boolean played;

    public Schedule() {
    }

    public Schedule(int timeSlot, List<Court> courts) {
        this.timeSlot = timeSlot;
        this.courts = courts;
        this.played = false;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public boolean isPlayed() {
        return played;
    }

    public List<Court> getCourts() {
        return courts;
    }

    public int getTimeSlot() {
        return timeSlot;
    }
}
