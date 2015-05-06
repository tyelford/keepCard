package com.tyelford.cardkeeper.data;

import android.provider.BaseColumns;

/**
 * Created by elfordty on 1/05/2015.
 */
public class CardDBContract{

    public CardDBContract(){}

    //Inner class that defines the table content
    public static abstract class CardTable implements BaseColumns{
        public static final String TABLE_NAME = "CardTable";

        //Create the columns for the table
        public static final String COLUMN_NAME_CARD_ID = "card_id";
        public static final String COLUMN_NAME_GIVER = "giver";
        public static final String COLUMN_NAME_P_FRONT = "p_front";
        public static final String COLUMN_NAME_P_IN_LEFT = "p_in_left";
        public static final String COLUMN_NAME_P_IN_RIGHT = "p_in_right";
        public static final String COLUMN_NAME_P_PRES = "p_pres";
        public static final String COLUMN_NAME_C_PRES = "c_pres";
        public static final String COLUMN_NAME_OCC_ID = "occ_id";
        public static final String COLUMN_NAME_ADD_GIVERS = "add_givers";
        public static final String[] COLUMNS = buildColumnArray();

        private static String[] buildColumnArray(){
            String[] columnArray = {COLUMN_NAME_CARD_ID, COLUMN_NAME_GIVER, COLUMN_NAME_P_FRONT,
                    COLUMN_NAME_P_IN_LEFT, COLUMN_NAME_P_IN_RIGHT, COLUMN_NAME_P_PRES, COLUMN_NAME_C_PRES,
                    COLUMN_NAME_OCC_ID, COLUMN_NAME_ADD_GIVERS};
            return columnArray;
        }
    }
}
