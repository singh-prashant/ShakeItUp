package sharif.shakeitup.ui.fragment;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;

import java.util.List;

import sharif.shakeitup.R;
import sharif.shakeitup.WordApplication;
import sharif.shakeitup.helper.ShakeDetector;
import sharif.shakeitup.db.entity.Word;
import sharif.shakeitup.sync.WordSyncUtils;
import sharif.shakeitup.ui.view.TextBox;
import sharif.shakeitup.ui.viewmodel.WordViewModel;
import sharif.shakeitup.util.Util;

import static android.view.View.GONE;


public class MainActivityFragment extends Fragment implements ShakeDetector.Listener, TextBox.OnWordMatchListener {

    private static final String TAG = "MainActivityFragment";

    public static final String DATA_SET_NAME = "todays_word";
    public static final String PERSON_NAME = "name";
    private static final String MESSAGE = "message";
    private static final String RESPONSE_DATA = "wotd";

    public static final int TIME_FIVE_SECONDS = 5000;
    public static final int TODAY_WORD_TASK_LOADER_ID = 101;
    private TextBox mTextBoxMessage;
    private Button mTodayButton;
    private ProgressBar mProgress;
    private TextView mTvTodayWord, mTvEmptyView;
    private Word mWord;
    private MenuItem mSend;

    private WordViewModel mWordViewModel;

    // This class provide response call back from amazon aws server.
    private Dataset.SyncCallback mDataSyncCallback = new Dataset.SyncCallback() {
        @Override
        public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
           getActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Toast.makeText(getContext(), R.string.message_sent_successful, Toast.LENGTH_SHORT).show();
                   mTextBoxMessage.setText("");
               }
           });
        }

        @Override
        public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
            return false;
        }

        @Override
        public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
            return false;
        }

        @Override
        public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
            return false;
        }

        @Override
        public void onFailure(DataStorageException dse) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSend.setVisible(true);
                    Toast.makeText(getContext(), R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public MainActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * */
    public static MainActivityFragment newInstance() {
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mSend = menu.findItem(R.id.action_send);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_send){
            mSend.setVisible(false);
            pushMessageToServer();
        }

        return super.onOptionsItemSelected(item);
    }

    private void pushMessageToServer() {
        WordApplication application = (WordApplication) getActivity().getApplicationContext();
        Dataset dataset = application.getCognitoSyncManager().openOrCreateDataset(DATA_SET_NAME);
        dataset.put(PERSON_NAME, Util.getPersonName(getContext()));
        dataset.put(MESSAGE, mTextBoxMessage.getText().toString());
        dataset.put(RESPONSE_DATA, mWord.getResponseData());
        dataset.synchronize(mDataSyncCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
        showLoading();
    }

    private void showLoading() {
        mProgress.setVisibility(View.VISIBLE);
        mTvTodayWord.setVisibility(View.INVISIBLE);
        mTextBoxMessage.setVisibility(View.INVISIBLE);
        mTodayButton.setVisibility(GONE);
        mTvEmptyView.setVisibility(GONE);
    }

    private void initListener() {
        // Todays button click listener.
        mTodayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWord == null){
                    Toast.makeText(getContext(), R.string.no_word_found, Toast.LENGTH_SHORT).show();
                    return;
                }

                Util.showWordDefinitionDialog(getContext(), mWord.getDefinition());
            }
        });

        mTextBoxMessage.setOnWordMatchListener(this);
    }

    private void initView(View view) {
        mTextBoxMessage = (TextBox) view.findViewById(R.id.text_box_message);
        mTodayButton = (Button) view.findViewById(R.id.btn_today_word);
        mProgress = (ProgressBar) view.findViewById(R.id.progress_bar);
        mTvTodayWord = (TextView) view.findViewById(R.id.tv_today_word);
        mTvEmptyView = (TextView) view.findViewById(R.id.tv_empty_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initShakeDetector();

        mWordViewModel = ViewModelProviders.of(getActivity()).get(WordViewModel.class);
        mWordViewModel.word.observe((LifecycleOwner) getActivity(), new Observer<Word>() {
            @Override
            public void onChanged(@Nullable Word word) {
                if (word == null){
                    showEmptyView();
                }else {
                    showTodayWord(word);
                }
            }
        });
    }

    private void initShakeDetector() {
        SensorManager mSensorManager;
        mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(mSensorManager);
    }


    @Override
    public void hearShake() {
        showBlueBendMessageForFiveSeconds();
    }

    private void showBlueBendMessageForFiveSeconds() {
        mTodayButton.setVisibility(View.VISIBLE);

        mTodayButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTodayButton.setVisibility(View.GONE);
            }
        }, TIME_FIVE_SECONDS);
    }

    private void showEmptyView() {
        if (!Util.isNetworkAvailable(getActivity())){
            mProgress.setVisibility(GONE);
            mTvEmptyView.setVisibility(View.VISIBLE);
        }
    }

    private void showTodayWord(Word word) {
        mProgress.setVisibility(GONE);
        mTvTodayWord.setVisibility(View.VISIBLE);
        mTextBoxMessage.setVisibility(View.VISIBLE);
        //mWord = Util.getWordFromCursor(data);
        mTextBoxMessage.setWord(word);
        mTvTodayWord.append(" " + word.getWord());
    }

    @Override
    public void onWordMatch(boolean isMatched, String message) {
        mSend.setVisible(isMatched);
    }
}
