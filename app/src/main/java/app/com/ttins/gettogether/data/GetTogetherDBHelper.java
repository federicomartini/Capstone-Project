package app.com.ttins.gettogether.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GetTogetherDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = GetTogetherDBHelper.class.getSimpleName();

    public GetTogetherDBHelper(Context context) {
        super(context, GetTogetherContract.DATABASE_NAME, null, GetTogetherContract.DATABASE_VERSIONE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GetTogetherContract.Events.TABLE_CREATE);
        db.execSQL(GetTogetherContract.Guests.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Upgrading DB from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + GetTogetherContract.Events.TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + GetTogetherContract.Guests.TABLE_GUESTS);
        onCreate(db);
    }


}
