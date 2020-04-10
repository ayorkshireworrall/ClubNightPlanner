package alex.worrall.clubnightplanner.service;

public enum Status {
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    LATER("Later");

    private String message;
    Status(String message) {
        this.message = message;
    }


}
