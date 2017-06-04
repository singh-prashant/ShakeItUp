package sharif.shakeitup.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import sharif.shakeitup.db.entity.Word;

/**
 * Created by Sharif-PC on 5/22/2017.
 */
@Dao
public interface WordDao {

    @Query("SELECT * FROM words where publishDate = :date")
    LiveData<Word> getWordByDate(String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Word word);

}
