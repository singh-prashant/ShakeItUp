package sharif.shakeitup.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import sharif.shakeitup.db.WordDb;
import sharif.shakeitup.db.entity.Word;
import sharif.shakeitup.sync.WordSyncUtils;
import sharif.shakeitup.util.Util;

/**
 * Created by User on 6/3/2017.
 */

public class WordViewModel extends AndroidViewModel{

    private static final String TAG = "WordViewModel";

    public final LiveData<Word> word;

    public WordViewModel(Application application) {
        super(application);
        WordSyncUtils.initialize(application);

        word = WordDb.getInstance(application).wordDao().getWordByDate(Util.getToday());

    }

}
