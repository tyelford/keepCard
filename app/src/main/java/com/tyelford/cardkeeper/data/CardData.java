package com.tyelford.cardkeeper.data;

import com.tyelford.cardkeeper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elfordty on 24/04/2015.
 * Class used to display all cards in list format
 */
public class CardData {

    private List<Card> Cards = new ArrayList<Card>();

    //Receive the CardData needed
    public List<Card> getCards(){
        return Cards;
    }

    public CardData(){
        //Add info to the Card List here - should come from external source
        addItem(new Card("Tyson Elford", R.drawable.front, R.drawable.insideleft, R.drawable.insideright, R.drawable.back, "This is an awesome Card", "Massage"));
        addItem(new Card("Jade Elford", 1, 1, 1, 1, "This is an awesome Card", "Massage"));
        addItem(new Card("Mom", 1, 1, 1, 1, "This is an awesome Card", "Massage"));
        addItem(new Card("Grandpa", 1, 1, 1, 1, "This is an awesome Card", "Massage"));
        addItem(new Card("Eddy Elford", 1, 1, 1, 1, "This is an awesome Card", "Massage"));
    }

    private void addItem(Card item){
        Cards.add(item);
    }
}
