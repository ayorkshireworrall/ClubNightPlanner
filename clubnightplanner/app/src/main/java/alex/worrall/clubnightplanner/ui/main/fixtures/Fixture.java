package alex.worrall.clubnightplanner.ui.main.fixtures;

import java.util.List;

import alex.worrall.clubnightplanner.ui.main.courts.Court;

public class Fixture {
    int timeSlot;
    List<Court> courts;
    boolean played;

    public Fixture() {
    }

    public Fixture(int timeSlot, List<Court> courts) {
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

