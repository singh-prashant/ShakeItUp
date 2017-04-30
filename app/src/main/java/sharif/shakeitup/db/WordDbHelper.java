package sharif.shakeitup.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sharif.shakeitup.db.model.WordContract;

/**
 * Created by Sharif-PC on 4/28/2017.
 */

public class WordDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "word_db";

    public static final int DATABASE_VERSION = 1;

    public WordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(WordContract.WordEntry.getWordCreateQuery());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(WordContract.WordEntry.getWordDeleteQuery());
        onCreate(sqLiteDatabase);
    }
}
