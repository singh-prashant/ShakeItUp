package sharif.shakeitup.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import sharif.shakeitup.db.model.WordContract;

/**
 * Created by Sharif-PC on 4/28/2017.
 */

public class WordProvider extends ContentProvider {

    private static final String TAG = "WordProvider";

    public static final int WORDS = 100;

    public static final int WORD_WITH_ID = 101;

    private WordDbHelper mWordDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(WordContract.AUTHORITY, WordContract.PATH_WORD, WORDS);
        sUriMatcher.addURI(WordContract.AUTHORITY, WordContract.PATH_WORD + "/#", WORD_WITH_ID);
    }

    @Override
    public boolean onCreate() {

        mWordDbHelper = new WordDbHelper(getContext());

        return true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mWordDbHelper.getReadableDatabase();

        Cursor retCursor = null;
        Log.d(TAG, "query: uri: " + sUriMatcher.match(uri));
        switch (sUriMatcher.match(uri)){


            case WORDS:
                retCursor = db.query(WordContract.WordEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case WORD_WITH_ID:

                String id = uri.getPathSegments().get(1);

                String mSelection = "_id=?";

                String[] mSelectionArgs = new String[]{id};


                retCursor = db.query(WordContract.WordEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mWordDbHelper.getWritableDatabase();

        Uri returnUri;
        Log.d(TAG, "insert: uri: "+ uri);
        switch (sUriMatcher.match(uri)){

            case WORDS:

               // long id = db.insert(WordContract.WordEntry.TABLE_NAME, null, values);

                long id = db.insertWithOnConflict(WordContract.WordEntry.TABLE_NAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);

                if (id > 0){
                    returnUri = ContentUris.withAppendedId(WordContract.WordEntry.CONTENT_URI, id);
                }else {
                    throw new SQLiteException("Failed to insert row into " + uri);
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted;
        final SQLiteDatabase db = mWordDbHelper.getWritableDatabase();

        if (selection == null) selection = "1";

        switch (sUriMatcher.match(uri)){
            case WORDS:

                numRowsDeleted = db.delete(
                        WordContract.WordEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        int wordsUpdated;

        switch (sUriMatcher.match(uri)){
            case WORD_WITH_ID:

                String id = uri.getPathSegments().get(1);
                wordsUpdated = mWordDbHelper.getWritableDatabase().update(
                        WordContract.WordEntry.TABLE_NAME,
                        values, "_id=?", new String[]{id}

                );


                if (wordsUpdated != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                break;

            default:
                throw  new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return wordsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mWordDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){

            case WORDS:
                db.beginTransaction();

                int rowsInserted = 0;

                try{
                    for (ContentValues value : values){
                        long _id = db.insert(WordContract.WordEntry.TABLE_NAME, null, value);
                        if (_id != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();

                }finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
