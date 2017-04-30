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

import sharif.shakeitup.db.model.Word;

/**
 * Created by Sharif-PC on 4/30/2017.
 */

public class TextBox extends AppCompatEditText implements TextWatcher {

    private static final String TAG = "TextBox";
    private static final String IDENTITY_POLL = "us-west-2:0c57a6ca-b882-4ca7-ab96-d2cac360a80c";
    private static final Regions REGION = Regions.US_WEST_2;
    private CognitoSyncManager mCognitoSyncManager;
    private Word mWord;
    private OnWordMatchListener mOnWordMatchListener;

    public TextBox(Context context) {
        super(context);
        initialize();
        initTextChangeListener();
    }

    public TextBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
        initTextChangeListener();
    }

    public TextBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
        initTextChangeListener();
    }

    private void initialize() {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext().getApplicationContext(),    /* get the context for the application */
                IDENTITY_POLL,    /* Identity Pool ID */
                REGION           /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );

        mCognitoSyncManager = new CognitoSyncManager(
                getContext().getApplicationContext(),
                REGION,
                credentialsProvider);
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


    /***
     WordApplication application = (WordApplication) getApplicationContext();
     Dataset dataset = application.getClient().openOrCreateDataset("datasetname");
     String value = dataset.get("myKey");
     Log.d(TAG, "onCreate: value: " + value);
     dataset.put("myKey", "my value");
     dataset.synchronize(new Dataset.SyncCallback() {
    @Override
    public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
    Log.d(TAG, "onSuccess: ");
    }

    @Override
    public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
    Log.d(TAG, "onConflict: ");
    return false;
    }

    @Override
    public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
    Log.d(TAG, "onDatasetDeleted: ");
    return false;
    }

    @Override
    public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
    Log.d(TAG, "onDatasetsMerged: ");
    return false;
    }

    @Override
    public void onFailure(DataStorageException dse) {
    Log.d(TAG, "onFailure: " + dse);
    }
    });*/

    public interface OnWordMatchListener{
        void onWordMatch(boolean isMatched, String message);
    }

}
