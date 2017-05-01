package sharif.shakeitup.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import sharif.shakeitup.R;
import sharif.shakeitup.db.model.Word;

/**
 * Created by Sharif-PC on 4/29/2017.
 */

public class Util {

    public static final int INDEX_WORD_ID = 0;
    public static final int INDEX_WORD_NAME = 1;
    public static final int INDEX_WORD_DEFINITION = 2;
    public static final int INDEX_WORD_PUBLISH_DATE = 3;
    public static final int INDEX_RESPONSE_DATA = 4;
    public static final String QUERY_DATE_FORMAT = "yyyy-MM-dd";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(Util.QUERY_DATE_FORMAT);


    public static void showWordDefinitionDialog(Context context, String wordDefinition){
        new AlertDialog.Builder(context)
                .setTitle(R.string.word_definition)
                .setMessage(wordDefinition)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}}).show();
    }

    public static Word getWordFromCursor(Cursor cursor) {
        cursor.moveToFirst();
        Word word = new Word();
        word.setId(cursor.getInt(INDEX_WORD_ID));
        word.setWord(cursor.getString(INDEX_WORD_NAME));
        word.setDefinition(cursor.getString(INDEX_WORD_DEFINITION));
        word.setPublishDate(cursor.getString(INDEX_WORD_PUBLISH_DATE));
        word.setResponseData(cursor.getString(INDEX_RESPONSE_DATA));
        return word;
    }

    public static String getPersonName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String personName = sharedPreferences.getString(context.getString(R.string.pref_name_key),
                context.getString(R.string.pref_default_display_name));
        return personName;
    }

    /**
     * Return tru if the network is available or about to available.
     * @param context Context used to get the connectivity manager.
     * @return
     */
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }


    public static String getToday(){
        Calendar calendar = Calendar.getInstance();
        return SIMPLE_DATE_FORMAT.format(calendar.getTime());
    }

}
