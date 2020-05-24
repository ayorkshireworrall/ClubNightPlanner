package alex.worrall.clubnightplanner.model.typeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class ListStringConverter implements Serializable {
    @TypeConverter
    public String fromIntegerList(List<String> integerList) {
        if (integerList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.toJson(integerList, type);
    }

    @TypeConverter
    public List<String> toIntegerList(String jsonString) {
        if (jsonString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }
}
