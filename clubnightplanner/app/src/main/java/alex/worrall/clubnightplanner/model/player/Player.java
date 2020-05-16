package alex.worrall.clubnightplanner.model.player;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "players")
public class Player {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;
    @ColumnInfo(name = "level")
    private int level;
    @ColumnInfo(name = "name")
    private String name;

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

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
