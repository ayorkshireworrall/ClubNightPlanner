package alex.worrall.clubnightplanner.model.typeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import alex.worrall.clubnightplanner.model.fixture.Court;

public class ListCourtConverter implements Serializable {
    @TypeConverter
    public String fromCourtList(List<Court> courtList) {
        if (courtList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Court>>(){}.getType();
        return gson.toJson(courtList, type);
    }

    @TypeConverter
    public List<Court> toCourtList(String jsonString) {
        if (jsonString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Court>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }
}
