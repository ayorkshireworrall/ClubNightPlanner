package alex.worrall.clubnightplanner.model.typeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

import alex.worrall.clubnightplanner.utils.Status;

public class StatusConverter implements Serializable {
    @TypeConverter
    public String fromStatus (Status status) {
        if (status == null) {
            return (null);
        }

//        Gson gson = new Gson();
//        Type type = new TypeToken<Status>(){}.getType();
//        return gson.toJson(status, type);
        return status.toString();
    }

    @TypeConverter
    public Status toStatus (String statusString) {
        if (statusString == null) {
            return (null);
        }
//        Gson gson = new Gson();
//        Type type = new TypeToken<Status>(){}.getType();
//        return gson.fromJson(jsonString, type);
        return Status.valueOf(statusString);
    }
}
