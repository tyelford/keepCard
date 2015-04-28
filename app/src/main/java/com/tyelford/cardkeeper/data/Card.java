package com.tyelford.cardkeeper.data;

/**
 * Created by elfordty on 24/04/2015.
 * This class is used to define a single card
 */
public class Card {
    public String cardGiver;
    public int cardFrontImgRes;
    public int cardInsideLeftImgRes;
    public int cardInsideRightImgRes;
    public int cardBackImgRes;
    public String cardComments;
    public String incPresent;

    //Constructor to create a card
    public Card(String cardGiver, int cardFrontImgRes, int cardInsideLeftImgRes, int cardInsideRightImgRes, int cardBackImgRes, String cardComments, String incPresent){
        this.cardGiver = cardGiver;
        this.cardFrontImgRes = cardFrontImgRes;
        this.cardInsideLeftImgRes = cardInsideLeftImgRes;
        this.cardInsideRightImgRes = cardInsideRightImgRes;
        this.cardBackImgRes = cardBackImgRes;
        this.cardComments = cardComments;
        this.incPresent = incPresent;
    }

    //ToString Mehtod used to display the card
    @Override
    public String toString() {
        return cardGiver;
    }
}
