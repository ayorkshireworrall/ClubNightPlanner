package alex.worrall.clubnightplanner.ui.main.fixtures;

import java.time.LocalTime;
import java.util.List;

import alex.worrall.clubnightplanner.service.Status;
import alex.worrall.clubnightplanner.ui.main.courts.Court;

public class Fixture {
    private int timeSlot;
    private List<Court> courts;
    private Status playStatus;

    public Fixture() {
    }

    public Fixture(int timeSlot, List<Court> courts) {
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

    public int getTimeSlot() {
        return timeSlot;
    }

    @Override
    public String toString() {
        int mins = this.timeSlot % 60;
        int hours = (this.timeSlot - mins) / 60;
        String minutes = mins < 10 ? "0" + mins : "" + mins;
        return hours + ":" + minutes;
    }
}

