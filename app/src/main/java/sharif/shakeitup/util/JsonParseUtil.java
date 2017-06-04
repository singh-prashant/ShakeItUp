package sharif.shakeitup.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sharif-PC on 4/29/2017.
 */

public class JsonParseUtil {
    public static final String ID = "id";
    public static final String WORD = "word";
    public static final String PUBLISH_DATE = "publishDate";
    public static final String DEFINITIONS = "definitions";
    public static final String DEFINITIONS_TEXT = "text";

    public static sharif.shakeitup.db.entity.Word getWordFromJson(String responseData) throws JSONException {

        sharif.shakeitup.db.entity.Word word = null;

        if (TextUtils.isEmpty(responseData))return word;

        word = new sharif.shakeitup.db.entity.Word();

        JSONObject wordJson = new JSONObject(responseData);
        final int id = wordJson.getInt(ID);
        final String wordName = wordJson.getString(WORD);
        final String publishDateFullFormat = wordJson.getString(PUBLISH_DATE);
        final String publishDate = publishDateFullFormat.substring(0, publishDateFullFormat.indexOf('T'));
        word.setId(id);
        word.setWord(wordName);
        word.setPublishDate(publishDate);
        word.setResponseData(responseData);
        JSONArray definitionsArray = wordJson.getJSONArray(DEFINITIONS);
        final int size = definitionsArray.length();
        for (int jIndex = 0; jIndex < size; jIndex++){
            JSONObject definitionObject = definitionsArray.getJSONObject(jIndex);
            String definitionText = definitionObject.getString(DEFINITIONS_TEXT);
            word.setDefinition(definitionText);
        }

        return word;
    }
}
