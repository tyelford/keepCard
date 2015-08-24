package com.tyelford.cardkeeper.data;

import com.tyelford.cardkeeper.AddCardActivity;

/**
 * Created by elfordty on 24/04/2015.
 * This class is used to define a single card
 */
public class Card {
    private String cardID;
    private String cardGiver;
    private String cardFrontImg;
    private String cardInLeftImg;
    private String cardInRightImg;
    private String presentImg;
    private String presentComments;
    private String cardComments;
    private String occasion;
    private String addGivers;

    //Deprecated
    private int cardFrontImgRes;  //Change all photos to String for a path to the file stored on disk
    private int cardInsideLeftImgRes;
    private int cardInsideRightImgRes;
    private int cardBackImgRes;
    private String incPresent;

    //Default constructor
    public Card(){
    }

    //Full constructor
    public Card(String cardID, String cardGiver, String cardFrontImg, String cardInLeftImg, String cardInRightImg, String presentImg, String presentComments, String occasion, String cardComments, String addGivers){
        this.cardGiver = cardGiver;
        this.cardFrontImg = cardFrontImg;
        this.cardInLeftImg = cardInLeftImg;
        this.cardInRightImg = cardInRightImg;
        this.cardComments = cardComments;
        this.presentImg = presentImg;
        this.occasion = occasion;
        this.presentComments = presentComments;
        this.addGivers = addGivers;
        this.cardID = cardID;
    }

    //ToString Mehtod used to display the card
    @Override
    public String toString() {
        return cardGiver;
    }


/*GETTER AND SETTER METHODS*/
public String getCardID() {
    return cardID;
}

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getCardGiver() {
        return cardGiver;
    }

    public void setCardGiver(String cardGiver) {
        this.cardGiver = cardGiver;
    }

    public String getCardFrontImg() {
        return cardFrontImg;
    }

    public void setCardFrontImg(String cardFrontImg) {
        this.cardFrontImg = cardFrontImg;
    }

    public String getCardInLeftImg() {
        return cardInLeftImg;
    }

    public void setCardInLeftImg(String cardInLeftImg) {
        this.cardInLeftImg = cardInLeftImg;
    }

    public String getCardInRightImg() {
        return cardInRightImg;
    }

    public void setCardInRightImg(String cardInRightImg) {
        this.cardInRightImg = cardInRightImg;
    }

    public String getPresentImg() {
        return presentImg;
    }

    public void setPresentImg(String presentImg) {
        this.presentImg = presentImg;
    }

    public String getPresentComments() {
        return presentComments;
    }

    public void setPresentComments(String presentComments) {
        this.presentComments = presentComments;
    }

    public String getCardComments() {
        return cardComments;
    }

    public void setCardComments(String cardComments) {
        this.cardComments = cardComments;
    }

    public String getAddGivers() {
        return addGivers;
    }

    public void setAddGivers(String addGivers) {
        this.addGivers = addGivers;
    }

    public String getOccasion(){
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }




    /*DEPRECATED METHODS*/

    //Constructor to create a card
    @Deprecated
    public Card(String cardGiver, int cardFrontImgRes, int cardInsideLeftImgRes, int cardInsideRightImgRes, int cardBackImgRes, String cardComments, String incPresent){
        this.cardGiver = cardGiver;
        this.cardFrontImgRes = cardFrontImgRes;
        this.cardInsideLeftImgRes = cardInsideLeftImgRes;
        this.cardInsideRightImgRes = cardInsideRightImgRes;
        this.cardBackImgRes = cardBackImgRes;
        this.cardComments = cardComments;
        this.incPresent = incPresent;
    }

}
