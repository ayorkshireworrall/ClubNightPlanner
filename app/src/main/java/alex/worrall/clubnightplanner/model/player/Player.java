package alex.worrall.clubnightplanner.model.player;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "players")
public class Player implements Parcelable {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;
    @ColumnInfo(name = "level")
    private int level;
    @ColumnInfo(name = "name")
    private String name;
    @Ignore
    private int scheduleRanking;
    @Ignore
    private String nextCourt;
    @Ignore
    private String currentCourt;

    public Player(int level, String name) {
        this.level = level;
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }

    @NonNull
    @Override
    public String toString() {
        return this.name + " (" + this.level + ")";
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScheduleRanking() {
        return scheduleRanking;
    }

    public void setScheduleRanking(int scheduleRanking) {
        this.scheduleRanking = scheduleRanking;
    }

    public String getNextCourt() {
        return nextCourt;
    }

    public void setNextCourt(String nextCourt) {
        this.nextCourt = nextCourt;
    }

    public String getCurrentCourt() {
        return currentCourt;
    }

    public void setCurrentCourt(String currentCourt) {
        this.currentCourt = currentCourt;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {

        @Override
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    private Player(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.level = in.readInt();
        this.scheduleRanking = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.level);
        dest.writeInt(this.scheduleRanking);
    }
}
