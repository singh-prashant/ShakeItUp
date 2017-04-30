package sharif.shakeitup.db.model;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sharif-PC on 4/28/2017.
 */

public class WordContract {

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.sharif.shakeitup";

    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static final String PATH_WORD = "word";


    public static abstract class WordEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Word table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WORD)
                .build();

        public static final Uri buildWordUriById(int wordId){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(wordId))
                    .build();
        }

        public static final String TABLE_NAME = "word";

        public static final String COLUMN_WORD_NAME  = "name";

        public static final String COLUMN_WORD_DEFINITION = "word_definition";

        public static final String COLUMN_WORD_PUBLISH_DATE = "word_publish_date";

        public static String getWordCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORD_NAME + " TEXT, " +
                    COLUMN_WORD_DEFINITION + " TEXT, " +
                    COLUMN_WORD_PUBLISH_DATE + " TEXT" + ");";
        }

        public static String getWordDeleteQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }


    }
}
