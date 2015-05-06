package com.tyelford.cardkeeper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tyelford.cardkeeper.data.CardDBContract.CardTable;

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

    public void getCard(int id){
        //Get a reference to the readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        //build query
        Cursor cursor = db.query(CardTable.TABLE_NAME, CardTable.COLUMNS, "id = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Card card = new Card();

    }

    public void insertCard(){
        //Get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();

        //create the content values to add
        ContentValues values = new ContentValues();
        values.put(CardTable.COLUMN_NAME_CARD_ID, "1");
        values.put(CardTable.COLUMN_NAME_GIVER, "Database Giver");
        values.put(CardTable.COLUMN_NAME_P_FRONT, "");
        values.put(CardTable.COLUMN_NAME_P_IN_LEFT, "");
        values.put(CardTable.COLUMN_NAME_P_IN_RIGHT, "");
        values.put(CardTable.COLUMN_NAME_P_PRES, "");
        values.put(CardTable.COLUMN_NAME_C_PRES, "");
        values.put(CardTable.COLUMN_NAME_OCC_ID, "Birthday");
        values.put(CardTable.COLUMN_NAME_ADD_GIVERS, "");

        db.insert(CardTable.TABLE_NAME, null, values);

        db.close();

    }



    //Some helpers to build the database
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CardTable.TABLE_NAME + " (" +
                    CardTable._ID + " INTEGER PRIMARY KEY," +
                    CardTable.COLUMN_NAME_CARD_ID + TEXT_TYPE + COMMA_SEP +
                    CardTable.COLUMN_NAME_GIVER + TEXT_TYPE + COMMA_SEP +
                    CardTable.COLUMN_NAME_P_FRONT  + TEXT_TYPE + COMMA_SEP +
                    CardTable.COLUMN_NAME_P_IN_LEFT + TEXT_TYPE + COMMA_SEP +
                    CardTable.COLUMN_NAME_P_IN_RIGHT + TEXT_TYPE + COMMA_SEP +
                    CardTable.COLUMN_NAME_P_PRES + TEXT_TYPE + COMMA_SEP +
                    CardTable.COLUMN_NAME_C_PRES + TEXT_TYPE + COMMA_SEP +
                    CardTable.COLUMN_NAME_OCC_ID + TEXT_TYPE + COMMA_SEP +
                    CardTable.COLUMN_NAME_ADD_GIVERS + TEXT_TYPE + COMMA_SEP +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CardTable.TABLE_NAME;
}
