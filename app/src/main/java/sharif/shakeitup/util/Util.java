package sharif.shakeitup.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

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
        cursor.close();
        return word;
    }

    public static String getPersonName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String personName = sharedPreferences.getString(context.getString(R.string.pref_name_key),
                context.getString(R.string.pref_default_display_name));
        return personName;
    }

}
