package com.tyelford.cardkeeper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tyelford.cardkeeper.data.CardDBContract.CardTable;
import com.tyelford.cardkeeper.data.CardDBContract.OccasionTable;

import java.util.ArrayList;

/**
 * Created by elfordty on 1/05/2015.
 */
public class CardDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "CardDB.db";

    public CardDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        //Create the Card table
        db.execSQL(SQL_CREATE_CARD_ENTRIES);
        //Create the Occasion table
        db.execSQL(SQL_CREATE_OCCASION_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Upgrade the Card Table
        db.execSQL(SQL_DELETE_CARD_ENTRIES);
        //Upgrade the Occasions Table
        db.execSQL(SQL_DELETE_OCCASION_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    //Method to return unique card givers
    public String[] getUniqueCardGivers() throws NoGiversException{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(SQL_SELECT_DISTINCT_GIVERS, null);

        ArrayList<String> thisUniqueGivers = new ArrayList<String>();

        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do{
                thisUniqueGivers.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }

        String[] s = new String[thisUniqueGivers.size()];

        if (s.length == 0)
            throw new NoGiversException("NoGiversAreInTheDatabase");

        s = thisUniqueGivers.toArray(s);
        return s;
    }

    //Method to return all occasions
    public String[] getAllOccasions() throws NoOccasionException{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_OCCASIONS, null);

        ArrayList<String> thisOccasions = new ArrayList<String>();

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do{
                thisOccasions.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }

        String[] s = new String[thisOccasions.size()];

        if(s.length == 0)
            throw new NoOccasionException("NoOccasionsInTheDatabase");

        s = thisOccasions.toArray(s);
        return s;

    }

    //Method to return three photos for a given giver - Path on Disk only
    public String[] getThreePhotosPerGiver(String giver){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(SQL_SELECT_FRONT_PHOTO_OF_GIVER, new String[]{giver});

        ArrayList<String> photos = new ArrayList<String>();
        int counter = 0;
        if(cursor != null){
            cursor.moveToFirst();
            do{
                String thisPhoto = cursor.getString(0);
                if(thisPhoto == null || thisPhoto.equals("")) {
                    counter++;
                    continue;
                }
                photos.add(thisPhoto);
                counter++;
                if(counter == 3){
                    break;
                }
            }while(cursor.moveToNext());
        }else
            return new String[]{};

        String[] s = new String[photos.size()];
        s = photos.toArray(s);
        return s;
    }

    public Card getCard(int id){
        //Get a reference to the readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        //build query
        //Cursor cursor = db.query(CardTable.TABLE_NAME, CardTable.COLUMNS, "id = ?", new String[] {String.valueOf(id)}, null, null, null, null);
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_CARD_COLUMNS + " where _id = ? ", new String[]{Integer.toString(id)});
        if(cursor != null){
            cursor.moveToFirst();
        }

        //Load up the card into a Card Object
        Card card = new Card();
        card.setCardID(cursor.getString(0));
        card.setCardGiver(cursor.getString(1));
        card.setCardFrontImg(cursor.getString(2));
        card.setCardInLeftImg(cursor.getString(3));
        card.setCardInRightImg(cursor.getString(4));
        card.setPresentImg(cursor.getString(5));
        card.setPresentComments(cursor.getString(6));
        card.setOccasion(cursor.getString(7));
        card.setAddGivers(cursor.getString(8));

        return card;

    }

    public void insertCard(Card card){
        //Get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();

        //create the content values to add
        ContentValues values = new ContentValues();
        values.put(CardTable.COLUMN_NAME_GIVER, card.getCardGiver());
        values.put(CardTable.COLUMN_NAME_P_FRONT, card.getCardFrontImg());
        values.put(CardTable.COLUMN_NAME_P_IN_LEFT, card.getCardInLeftImg());
        values.put(CardTable.COLUMN_NAME_P_IN_RIGHT, card.getCardInRightImg());
        values.put(CardTable.COLUMN_NAME_P_PRES, card.getPresentImg());
        values.put(CardTable.COLUMN_NAME_C_PRES, "");
        values.put(CardTable.COLUMN_NAME_OCC_ID, card.getOccasion());
        values.put(CardTable.COLUMN_NAME_ADD_GIVERS, card.getAddGivers());

        db.insert(CardTable.TABLE_NAME, null, values);
        db.close();
    }

    public void insertOccasion(Occasion occ){
        //Get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();

        //Create the content values to add
        ContentValues values = new ContentValues();
        values.put(CardDBContract.OccasionTable.COLUMN_NAME_OCCASION, occ.getOccName());
        values.put(CardDBContract.OccasionTable.COLUMN_NAME_DATE, occ.getOccDate());
        values.put(CardDBContract.OccasionTable.COLUMN_NAME_LOCATION, occ.getOccLocation());
        values.put(CardDBContract.OccasionTable.COLUMN_NAME_NOTES, occ.getOccNotes());

        db.insert(CardDBContract.OccasionTable.TABLE_NAME, null, values);
        db.close();
    }


    //Method to delete the entire database
    public void deleteDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ALL_CARD_ROWS);
    }


    //Some helpers to build the database
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    /******************************/
    //CARD TABLE STRINGS//

    private static final String SQL_CREATE_CARD_ENTRIES =
            "CREATE TABLE " + CardTable.TABLE_NAME + " (" +
            CardTable._ID + " INTEGER PRIMARY KEY," +
            CardTable.COLUMN_NAME_GIVER + TEXT_TYPE + COMMA_SEP +
            CardTable.COLUMN_NAME_P_FRONT  + TEXT_TYPE + COMMA_SEP +
            CardTable.COLUMN_NAME_P_IN_LEFT + TEXT_TYPE + COMMA_SEP +
            CardTable.COLUMN_NAME_P_IN_RIGHT + TEXT_TYPE + COMMA_SEP +
            CardTable.COLUMN_NAME_P_PRES + TEXT_TYPE + COMMA_SEP +
            CardTable.COLUMN_NAME_C_PRES + TEXT_TYPE + COMMA_SEP +
            CardTable.COLUMN_NAME_OCC_ID + TEXT_TYPE + COMMA_SEP +
            CardTable.COLUMN_NAME_ADD_GIVERS + TEXT_TYPE +
            " )";

    private static final String SQL_SELECT_ALL_CARD_COLUMNS =
            "SELECT " +
            CardTable._ID + COMMA_SEP +
            CardTable.COLUMN_NAME_GIVER + COMMA_SEP +
            CardTable.COLUMN_NAME_P_FRONT  + COMMA_SEP +
            CardTable.COLUMN_NAME_P_IN_LEFT + COMMA_SEP +
            CardTable.COLUMN_NAME_P_IN_RIGHT + COMMA_SEP +
            CardTable.COLUMN_NAME_P_PRES + COMMA_SEP +
            CardTable.COLUMN_NAME_C_PRES + COMMA_SEP +
            CardTable.COLUMN_NAME_OCC_ID + COMMA_SEP +
            CardTable.COLUMN_NAME_ADD_GIVERS +
            " FROM " + CardTable.TABLE_NAME;

    private static final String SQL_SELECT_DISTINCT_GIVERS =
            "SELECT DISTINCT " +
            CardTable.COLUMN_NAME_GIVER +
            " FROM " + CardTable.TABLE_NAME;

    private static final String SQL_SELECT_FRONT_PHOTO_OF_GIVER =
            "SELECT " +
             CardTable.COLUMN_NAME_P_FRONT +
             " FROM " + CardTable.TABLE_NAME +
             " WHERE " + CardTable.COLUMN_NAME_GIVER +
             " = ?" +
             " ORDER BY " + CardTable._ID + " DESC";

    private static final String SQL_DELETE_CARD_ENTRIES =
            "DROP TABLE IF EXISTS " + CardTable.TABLE_NAME;

    private static final String SQL_DELETE_ALL_CARD_ROWS =
            "DELETE FROM " + CardTable.TABLE_NAME;

    /****************************************/
    //OCCASION TABLE STRINGS//

    //private static final String TEXT_TYPE = " TEXT";
    //private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_OCCASION_ENTRIES =
            "CREATE TABLE " + OccasionTable.TABLE_NAME + " (" +
                    OccasionTable._ID + " INTEGER PRIMARY KEY," +
                    OccasionTable.COLUMN_NAME_OCCASION + TEXT_TYPE + COMMA_SEP +
                    OccasionTable.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    OccasionTable.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    OccasionTable.COLUMN_NAME_NOTES + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_OCCASION_ENTRIES =
            "DROP TABLE IF EXISTS " + CardDBContract.OccasionTable.TABLE_NAME;

    private static final String SQL_SELECT_ALL_OCCASIONS =
            "SELECT " +
                    OccasionTable._ID + COMMA_SEP +
                    OccasionTable.COLUMN_NAME_OCCASION + COMMA_SEP +
                    OccasionTable.COLUMN_NAME_DATE +
                    " FROM " + OccasionTable.TABLE_NAME +
                    " ORDER BY " + OccasionTable.COLUMN_NAME_DATE + " DESC";

}
