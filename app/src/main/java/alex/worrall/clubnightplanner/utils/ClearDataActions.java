package alex.worrall.clubnightplanner.utils;

public enum ClearDataActions {
    PLAYERS("Reset Players", "Are you sure you wish to clear data for all players in the current " +
            "list? This action is irreversible."),
    COURTS("Reset Courts", "Are you sure you wish to clear data for all courts in the current " +
            "list? This action is irreversible."),
    FIXTURES("Reset Fixtures", "Are you sure you wish to clear data for all fixtures in the " +
            "current list? This action is irreversible."),
    ALL("Reset All Data", "Are you sure you wish to reset all data for this current session? All " +
            "players, courts and fixtures will be removed. This action is irreversible.");

    private String message;
    private String title;

    private ClearDataActions(String title, String message) {
        this.message = message;
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
}
