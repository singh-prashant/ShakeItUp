package sharif.shakeitup;

import android.app.Application;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.regions.Regions;

/**
 * Created by Sharif-PC on 5/1/2017.
 */

public class WordApplication extends Application{

    private static final String IDENTITY_POLL = "us-west-2:0c57a6ca-b882-4ca7-ab96-d2cac360a80c";
    private static final Regions REGION = Regions.US_WEST_2;
    private CognitoSyncManager mCognitoSyncManager;

    @Override
    public void onCreate() {
        super.onCreate();
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),    /* get the context for the application */
                IDENTITY_POLL,    /* Identity Pool ID */
                REGION           /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );

        mCognitoSyncManager = new CognitoSyncManager(
                getApplicationContext(),
                REGION,
                credentialsProvider);
    }

    public CognitoSyncManager getCognitoSyncManager() {
        return mCognitoSyncManager;
    }


}
