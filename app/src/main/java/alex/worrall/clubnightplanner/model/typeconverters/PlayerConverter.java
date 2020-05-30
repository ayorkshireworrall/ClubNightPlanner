package alex.worrall.clubnightplanner.model.typeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

import alex.worrall.clubnightplanner.model.player.Player;

public class PlayerConverter implements Serializable {
    @TypeConverter
    public String fromPlayer(Player player) {
        if (player == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Player>(){}.getType();
        return gson.toJson(player, type);
    }

    @TypeConverter
    public Player toPlayer(String json) {
        if (json == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Player>(){}.getType();
        return gson.fromJson(json, type);
    }
}
