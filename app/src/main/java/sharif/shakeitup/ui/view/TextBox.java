package sharif.shakeitup.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import sharif.shakeitup.db.model.Word;

/**
 * Created by Sharif-PC on 4/30/2017.
 */

public class TextBox extends AppCompatEditText implements TextWatcher {

    private static final String TAG = "TextBox";

    private Word mWord;

    private final String testWord = "Sharif";
    //https://docs.aws.amazon.com/mobile/sdkforandroid/developerguide/setup.html
    //https://github.com/aws/aws-sdk-android
    //https://www.youtube.com/watch?v=arZ2zj_F4LA

    public TextBox(Context context) {
        super(context);
        initTextChangeListener();
    }

    public TextBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTextChangeListener();
    }

    public TextBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTextChangeListener();
    }

    private void initTextChangeListener() {
        addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "onTextChanged: " + s.toString());

        if (isWordFoundInTextBox(s)){
            // Word is found so take some action.
        }
    }

    private boolean isWordFoundInTextBox(CharSequence s) {
        return s.toString().toLowerCase().indexOf(testWord.toLowerCase()) != -1;
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
