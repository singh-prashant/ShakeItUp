package sharif.shakeitup.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.regions.Regions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sharif.shakeitup.db.entity.Word;

/**
 * Created by Sharif-PC on 4/30/2017.
 */

public class TextBox extends AppCompatEditText implements TextWatcher {

    private static final String TAG = "TextBox";
    private Word mWord;
    private OnWordMatchListener mOnWordMatchListener;

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

    public void setWord(Word word) {
        this.mWord = word;
    }

    public void setOnWordMatchListener(OnWordMatchListener onWordMatchListener) {
        this.mOnWordMatchListener = onWordMatchListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}


    @Override
    public void afterTextChanged(Editable s) {

        if (mWord == null){
            return;
        }

        if (mOnWordMatchListener != null){
            mOnWordMatchListener.onWordMatch(isWordFoundInTextBox(s), s.toString());
        }
    }

    private boolean isWordFoundInTextBox(CharSequence s) {
        final String pattern = "\\b"+mWord.getWord().toLowerCase()+"\\b";
        Pattern p= Pattern.compile(pattern);
        Matcher m=p.matcher(s.toString().toLowerCase());
        return m.find();
    }

    public interface OnWordMatchListener{
        void onWordMatch(boolean isMatched, String message);
    }

}
