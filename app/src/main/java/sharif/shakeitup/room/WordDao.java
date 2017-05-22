package sharif.shakeitup.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Query;

import sharif.shakeitup.db.model.*;

/**
 * Created by Sharif-PC on 5/22/2017.
 */

public interface WordDao {

    @Query("SELECT * FROM Word where publishDate = :date")
    LiveData<sharif.shakeitup.db.model.Word> getWordByDate(String date);

}
