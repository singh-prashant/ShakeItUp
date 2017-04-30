package sharif.shakeitup.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import java.util.List;

import sharif.shakeitup.R;
import sharif.shakeitup.WordApplication;

public class ScrollCheck extends AppCompatActivity {

    private static final String TAG = "ScrollCheck";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_check);

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
        });

    }
}
