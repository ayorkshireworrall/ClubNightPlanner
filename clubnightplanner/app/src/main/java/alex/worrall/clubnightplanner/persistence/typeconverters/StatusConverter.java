package alex.worrall.clubnightplanner.persistence.typeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

import alex.worrall.clubnightplanner.service.Status;

public class StatusConverter implements Serializable {

    @TypeConverter
    public String fromStatus(Status status) {
        if (status == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Status>(){}.getType();
        return gson.toJson(status, type);
    }

    @TypeConverter
    public Status toStatus(String jsonString) {
        if (jsonString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Status>(){}.getType();
        return gson.fromJson(jsonString, type);
    }
}
