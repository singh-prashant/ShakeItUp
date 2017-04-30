package sharif.shakeitup.sync;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sharif.shakeitup.api.ApiConfig;
import sharif.shakeitup.db.model.Word;
import sharif.shakeitup.db.model.WordContract;
import sharif.shakeitup.util.JsonParseUtil;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class TodayWordSyncIntentService extends IntentService {

    private static final String TAG = "TodayWordSyncIntentServ";

    private static final String ACTION_TODAYS_WORD = "sharif.shakeitup.sync.action.todays.word";

    public TodayWordSyncIntentService() {
        super("TodayWordSyncIntentService");
    }

    /**
     * Starts this service to perform action startTodaysWordSyncService. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startTodaysWordSyncService(Context context) {
        Intent intent = new Intent(context, TodayWordSyncIntentService.class);
        intent.setAction(ACTION_TODAYS_WORD);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_TODAYS_WORD.equals(action)) {
                WordSyncTask.syncTodayWord(this);
            }
        }
    }
}
