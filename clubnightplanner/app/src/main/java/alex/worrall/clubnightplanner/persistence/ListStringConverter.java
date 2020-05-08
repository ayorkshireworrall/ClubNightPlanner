package alex.worrall.clubnightplanner.persistence;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class ListStringConverter implements Serializable {

    @TypeConverter
    public String fromStringList(List<String> stringList) {
        if (stringList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.toJson(stringList, type);
    }

    @TypeConverter
    public List<String> toStringList(String jsonString) {
        if (jsonString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }
}
