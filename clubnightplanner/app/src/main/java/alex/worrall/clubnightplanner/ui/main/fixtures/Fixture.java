package alex.worrall.clubnightplanner.ui.main.fixtures;

import java.time.LocalTime;
import java.util.List;

import alex.worrall.clubnightplanner.service.Status;
import alex.worrall.clubnightplanner.ui.main.courts.Court;

public class Fixture {
    LocalTime timeSlot;
    List<Court> courts;
    Status playStatus;

    public Fixture() {
    }

    public Fixture(LocalTime timeSlot, List<Court> courts) {
        this.timeSlot = timeSlot;
        this.courts = courts;
        this.playStatus = Status.LATER;
    }

    public void setPlayStatus(Status playStatus) {
        this.playStatus = playStatus;
    }

    public Status getPlayStatus() {
        return playStatus;
    }

    public List<Court> getCourts() {
        return courts;
    }

    public LocalTime getTimeSlot() {
        return timeSlot;
    }
}

