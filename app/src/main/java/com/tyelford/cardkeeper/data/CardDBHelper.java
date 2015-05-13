package com.tyelford.cardkeeper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tyelford.cardkeeper.data.CardDBContract.CardTable;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by elfordty on 1/05/2015.
 */
public class CardDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CardDB.db";

    public CardDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    //Method for PersonActivity List Screen
    public ArrayList<Card> getUniqueCardGivers(){
        SQLiteDatabase db = this.getReadableDatabase();

        //Plan
        //1.Get all Cards
        //2.Loop through the cards one by one
        //3.Create a list of the Givers
        //4.Add only uniqe givers to the list - no doubles
        //5.Get the first 3 card objects for each unique giver

        //Cursor cursor db.rawQuery(SQL_SELECT_ALL_COLUMNS + " where ")
        return null;
    }

    public Card getCard(int id){
        //Get a reference to the readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        //build query
        //Cursor cursor = db.query(CardTable.TABLE_NAME, CardTable.COLUMNS, "id = ?", new String[] {String.valueOf(id)}, null, null, null, null);
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_COLUMNS + " where _id = ? ", new String[]{Integer.toString(id)});
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
        card.setOccasion(new Occasion());
        card.setAddGivers(cursor.getString(8));

        return card;

    }

    public void insertCard(Card card){
        //Get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();

        //Increment to the next available card

        //create the content values to add
        ContentValues values = new ContentValues();
        values.put(CardTable.COLUMN_NAME_GIVER, card.getCardGiver());
        values.put(CardTable.COLUMN_NAME_P_FRONT, card.getCardFrontImg());
        values.put(CardTable.COLUMN_NAME_P_IN_LEFT, card.getCardInLeftImg());
        values.put(CardTable.COLUMN_NAME_P_IN_RIGHT, card.getCardInRightImg());
        values.put(CardTable.COLUMN_NAME_P_PRES, card.getPresentImg());
        values.put(CardTable.COLUMN_NAME_C_PRES, "");
        values.put(CardTable.COLUMN_NAME_OCC_ID, "New Occasion");
        values.put(CardTable.COLUMN_NAME_ADD_GIVERS, card.getAddGivers());

        db.insert(CardTable.TABLE_NAME, null, values);

        db.close();

    }



    //Some helpers to build the database
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
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

    private static final String SQL_SELECT_ALL_COLUMNS =
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

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CardTable.TABLE_NAME;
}
