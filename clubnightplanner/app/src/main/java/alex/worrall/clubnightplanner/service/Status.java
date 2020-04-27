package alex.worrall.clubnightplanner.service;

public enum Status {
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    NEXT("Next"),
    LATER("Later");

    private String message;
    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
