package alex.worrall.clubnightplanner.service.models.courtnames;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class CourtnamesDBManager {
    private CourtnamesDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    private static CourtnamesDBManager instance;

    private CourtnamesDBManager(Context context) {
        this.context = context;
    }

    public static CourtnamesDBManager getInstance(Context context) {
        if (instance == null) {
            instance = new CourtnamesDBManager(context);
        }
        return instance;
    }

    public CourtnamesDBManager open() {
        dbHelper = new CourtnamesDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String courtName, int sessionId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CourtnamesDBHelper.NAME, courtName);
        contentValues.put(CourtnamesDBHelper.SESSION_ID, sessionId);
        database.insert(CourtnamesDBHelper.TABLE_NAME, null, contentValues);
    }

    public Cursor fetch() {
        String[] columns = new String[] { CourtnamesDBHelper._ID, CourtnamesDBHelper.NAME,
                CourtnamesDBHelper.SESSION_ID };
        Cursor cursor = database.query(CourtnamesDBHelper.TABLE_NAME, columns, null, null, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update (long _id, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CourtnamesDBHelper.NAME, name);
        return database.update(CourtnamesDBHelper.TABLE_NAME, contentValues,
                CourtnamesDBHelper._ID + " = " + _id, null);
    }

    public void delete (long _id) {
        database.delete(CourtnamesDBHelper.TABLE_NAME, CourtnamesDBHelper._ID + "=" + _id, null);
    }

    @Override
    protected void finalize() throws Throwable {
        if (dbHelper != null) {
            dbHelper.close();
        }
        if (database != null) {
            database.close();
        }
        super.finalize();
    }
}
