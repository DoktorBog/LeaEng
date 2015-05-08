package doktor.bog.leaeng.data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Grechanuyk B.O. on 10.03.2015.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "leaengDB.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "table_name";
    public static final String UID = "_id";
    public static final String WORD= "word";
    public static final String WORD_TRANSLATE= "word_translate";
    public static final String LANGV= "langv";
    public static final String NAME_TABLE_USER= "nameTUser";
    public static final String TABLES = "nameTs";
    public static final String TABLE_WORDS = "table_words";
    public static final String STAR = "table_words";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLES + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TABLE_NAME + " TEXT NOT NULL, "
            + NAME_TABLE_USER + " TEXT NOT NULL, "
            + LANGV + " TEXT NOT NULL);";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
