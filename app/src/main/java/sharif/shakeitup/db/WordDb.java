package sharif.shakeitup.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by User on 6/3/2017.
 */

@Database(entities = {sharif.shakeitup.db.entity.Word.class}, version = 1, exportSchema = false)
public abstract class WordDb extends RoomDatabase {

    private static WordDb INSTANCE;

    public abstract WordDao wordDao();


    public static synchronized WordDb getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WordDb.class, "word_db").build();
        }

        return INSTANCE;
    }


    public static void destroyInstance(){
        INSTANCE = null;
    }

}
