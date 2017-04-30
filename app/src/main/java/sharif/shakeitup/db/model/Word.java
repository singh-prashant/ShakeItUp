package sharif.shakeitup.db.model;

import android.content.ContentValues;

/**
 * Created by Sharif-PC on 4/28/2017.
 */

public class Word {

    private int id;
    private String word;
    private String definition;
    private String publishDate;


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


    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(WordContract.WordEntry._ID, getId());
        contentValues.put(WordContract.WordEntry.COLUMN_WORD_NAME, getWord());
        contentValues.put(WordContract.WordEntry.COLUMN_WORD_DEFINITION, getDefinition());
        contentValues.put(WordContract.WordEntry.COLUMN_WORD_PUBLISH_DATE, getPublishDate());
        return contentValues;
    }

}
