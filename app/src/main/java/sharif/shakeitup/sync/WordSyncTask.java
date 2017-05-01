package sharif.shakeitup.sync;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sharif.shakeitup.api.ApiConfig;
import sharif.shakeitup.db.model.Word;
import sharif.shakeitup.db.model.WordContract;
import sharif.shakeitup.util.JsonParseUtil;
import sharif.shakeitup.util.Util;

/**
 * Created by Sharif-PC on 4/30/2017.
 */

public class WordSyncTask {

    private static final String TAG = "WordSyncTask";
    /**
     * Performs the network request for updated word, parses the JSON from that request, and
     * inserts the new word information into our ContentProvider. Will notify the user that new
     * word data has been loaded.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncTodayWord(Context context){
        // Make api call here. And insert it into our word database.
        OkHttpClient okHttpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder =  HttpUrl.parse(ApiConfig.BASE_WORD_NIK_API).newBuilder();
        final String today = Util.getToday();
        urlBuilder.addQueryParameter(ApiConfig.API_DATE_PARAM, today);
        urlBuilder.addQueryParameter(ApiConfig.API_KEY_PARAM, ApiConfig.API_KEY);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            final String responseData = response.body().string();
            Word word = JsonParseUtil.getWordFromJson(responseData);
            context.getContentResolver().insert(WordContract.WordEntry.CONTENT_URI, word.getContentValues());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
