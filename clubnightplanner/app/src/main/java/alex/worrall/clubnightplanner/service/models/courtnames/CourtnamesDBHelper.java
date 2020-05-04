package alex.worrall.clubnightplanner.service.models.courtnames;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CourtnamesDBHelper extends SQLiteOpenHelper {

    //DB info
    private static final String DB_NAME = "CLUBNIGHT_PLANNER.DB";
    private static final int DB_VERSION = 1;

    //Table info
    static final String TABLE_NAME = "COURT_NAMES";
    static final String _ID = "_id";
    static final String NAME = "name";
    static final String SESSION_ID = "session_id";

    //Create table String
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + SESSION_ID +
            " INTEGER);";


    public CourtnamesDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
