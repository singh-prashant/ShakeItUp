package sharif.shakeitup.sync;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import sharif.shakeitup.db.model.WordContract;

/**
 * Created by Sharif-PC on 4/29/2017.
 */

public class SyncUtil {

    private static boolean sInitialized;


    public static void initialize(final Context context){


        if (sInitialized)return;


        sInitialized = true;


        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {

                Cursor cursor = context.getContentResolver().query(WordContract.WordEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

               // if (cursor == null || cursor.getCount() == 0){
                    startImmediateSync(context);
               // }

                cursor.close();

                return null;
            }
        }.execute();


    }

    private static void startImmediateSync(Context context) {
        TodayWordSyncIntentService.startTodaysWordSyncService(context);
    }

}
