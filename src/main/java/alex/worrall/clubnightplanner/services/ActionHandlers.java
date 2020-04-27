package alex.worrall.clubnightplanner.services;

import alex.worrall.clubnightplanner.models.Player;

public interface ActionHandlers {
    public void addPlayer(Player player);
    public void deletePlayer(Player player);
    public void deletePlayer(String playerId);
}
