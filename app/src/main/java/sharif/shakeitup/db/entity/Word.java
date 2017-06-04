package sharif.shakeitup.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Sharif-PC on 5/22/2017.
 */

@Entity(tableName = "words")
public class Word {

    @PrimaryKey
    private int id;
    private String word;
    private String definition;
    private String publishDate;
    private String responseData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }


    @Override
    public String toString() {
        return "id: " + id + " name: " + word;
    }
}
