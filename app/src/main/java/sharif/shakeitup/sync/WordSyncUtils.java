package sharif.shakeitup.sync;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import sharif.shakeitup.db.model.WordContract;

/**
 * Created by Sharif-PC on 4/29/2017.
 */

public class WordSyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;

    private static final String WORD_SYNC_TAG = "word-sync";

    /**
     * Schedules a repeating sync of Today word data using FirebaseJobDispatcher.
     *
     * @param context Context used to create the GooglePlayDriver that powers the
     *                FirebaseJobDispatcher
     */
    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        /* Create the Job to periodically sync word */
        Job syncTodayWordJob = dispatcher.newJobBuilder()

                .setService(TodayWordFirebaseJobService.class)
                /* Set the UNIQUE tag used to identify this Job */
                .setTag(WORD_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(syncTodayWordJob);
    }


    public static void initialize(final Context context) {


        if (sInitialized) return;


        sInitialized = true;
                /*
         * This method call triggers our app to create its task to synchronize word data
         * periodically.
         */
        scheduleFirebaseJobDispatcherSync(context);


        new AsyncTask<Void, Void, Void>() {

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
