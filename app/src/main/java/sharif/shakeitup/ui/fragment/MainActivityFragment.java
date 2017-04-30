package sharif.shakeitup.ui.fragment;

import android.database.Cursor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import sharif.shakeitup.R;
import sharif.shakeitup.db.model.Word;
import sharif.shakeitup.db.model.WordContract;
import sharif.shakeitup.helper.ShakeDetector;
import sharif.shakeitup.sync.SyncUtil;
import sharif.shakeitup.util.Util;

import static android.view.View.GONE;


public class MainActivityFragment extends Fragment implements ShakeDetector.Listener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivityFragment";

    public static final int TIME_FIVE_SECONDS = 5000;

    public static final int TODAY_WORD_TASK_LOADER_ID = 101;

    private Button mTodayButton;
    private TextView mTvEmptyWord;
    private ProgressBar mProgress;
    private Word mWord;

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
        SyncUtil.initialize(getContext());
        startTodayWordTaskLoader();
        initShakeDetector();
    }

    private void initShakeDetector() {
        SensorManager mSensorManager;
        mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(mSensorManager);
    }

    private void startTodayWordTaskLoader() {
        getActivity().getSupportLoaderManager().initLoader(TODAY_WORD_TASK_LOADER_ID, null, this);
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
    }

    private void initView(View view) {
        mTodayButton = (Button) view.findViewById(R.id.btn_today_word);
        mTvEmptyWord = (TextView) view.findViewById(R.id.tv_empty_word);
        mProgress = (ProgressBar) view.findViewById(R.id.progress_bar);
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
                mTodayButton.setVisibility(GONE);
            }
        }, TIME_FIVE_SECONDS);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                WordContract.WordEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProgress.setVisibility(GONE);
        if (data == null || data.getCount() == 0){
            showEmptyView(true);
            return;
        }
        showEmptyView(false);
        mWord = Util.getWordFromCursor(data);

    }

    private void showEmptyView(boolean isVisible) {
        mTvEmptyWord.setVisibility(isVisible ? View.VISIBLE : GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
