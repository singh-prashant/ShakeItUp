package sharif.shakeitup.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sharif.shakeitup.db.model.Word;

/**
 * Created by Sharif-PC on 4/29/2017.
 */

public class JsonParseUtil {
    public static final String ID = "id";
    public static final String WORD = "word";
    public static final String PUBLISH_DATE = "publishDate";
    public static final String DEFINITIONS = "definitions";
    public static final String DEFINITIONS_TEXT = "text";

    public static Word getWordFromJson(String responseData) throws JSONException {

        Word word = null;

        if (TextUtils.isEmpty(responseData))return word;

        word = new Word();

        JSONObject wordJson = new JSONObject(responseData);
        final int id = wordJson.getInt(ID);
        final String wordName = wordJson.getString(WORD);
        word.setId(id);
        word.setWord(wordName);

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
