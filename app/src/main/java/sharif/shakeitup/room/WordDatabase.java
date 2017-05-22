package sharif.shakeitup.room;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import sharif.shakeitup.db.model.*;

/**
 * Created by Sharif-PC on 5/22/2017.
 */

public abstract class WordDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "word-db";

    public abstract WordDao wordDao();
}
