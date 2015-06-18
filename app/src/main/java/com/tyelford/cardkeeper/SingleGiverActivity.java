package com.tyelford.cardkeeper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tyelford.cardkeeper.data.Card;
import com.tyelford.cardkeeper.data.CardDBHelper;

import java.io.File;
import java.util.LinkedList;


public class SingleGiverActivity extends Activity {

    private static final int SCREEN_SCALER = 3;

    //Screen Dimensions
    int screenW;
    int screenH;
    //Card Tile size
    int tileX;
    int tileY;
    //This Giver
    String dasGiver;
    //This Givers Cards
    Card[] dasGiverCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_giver);

        //Lock the screen in portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Get the screen size in pixels
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenW = size.x;
        screenH = size.y;

        //Make the tile size
        tileX = screenW / SCREEN_SCALER;
        tileY = screenH / SCREEN_SCALER;

        //Get the clicked Giver from the previous activity
        dasGiver = getIntent().getExtras().getString("clickedGiver");

        //Set the title for this activity
        setTitle("Cards From " + dasGiver);

        //Load up the info from the previous giver
        loadGiverData();

        //Check for cards
        if(dasGiverCards.length == 0){
            drawNothing();
        }

        //Move the Array to a LinkedList to make it easier
        //to work with
        LinkedList<Card> theseCards = new LinkedList<Card>();
        for(int i = 0; i < dasGiverCards.length; i++){
            theseCards.addLast(dasGiverCards[i]);
        }

        //Use LinkedList as a queue, popping 3 off at a time
        //to add to the horizontal layout of the next method
        while(theseCards.size() > 0){

            //Check for if there is 2 cards left
            if(theseCards.size() == 2){
                Card[] threeCards = new Card[2];
                threeCards[0] = theseCards.removeFirst();
                threeCards[1] = theseCards.removeFirst();

                drawCards(threeCards);
                break;
            }

            //Check for if there is 2 cards left
            if(theseCards.size() == 1){
                Card[] threeCards = new Card[1];
                threeCards[0] = theseCards.removeFirst();

                drawCards(threeCards);
                break;
            }

            Card[] threeCards = new Card[3];
            threeCards[0] = theseCards.removeFirst();
            threeCards[1] = theseCards.removeFirst();
            threeCards[2] = theseCards.removeFirst();

            drawCards(threeCards);
        }


//        for(int i = 0; i < dasGiverCards.length; i+=3) {
//            //Create an array of length 3 and send cards 3 at a time
//            //to preserve the row look
//            Card[] threeCards = new Card[3];
//            if(dasGiverCards.length >= 3) {
//                threeCards[i] = dasGiverCards[i];
//                threeCards[i + 1] = dasGiverCards[i + 1];
//                threeCards[i + 2] = dasGiverCards[i + 2];
//            }
//
//            //Draw the cards on the screen
//            drawCards(threeCards);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_giver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Load up an Array of Cards with data from the DB
    private void loadGiverData(){
        //Do a database query and return an array of Card Objects from this giver
        CardDBHelper readCard = new CardDBHelper(this);
        dasGiverCards = readCard.getCardsFromGiver(dasGiver);
    }

    //Draw the cards to the screen
    private void drawCards(Card cards[]){
        //Get the main vertical layout
        LinearLayout mainVertLayout = (LinearLayout)findViewById(R.id.mainVertThisGiver);

        //Create a new horizontal layout
        LinearLayout newHorLayout = new LinearLayout(this);
        newHorLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //Load up the front of the card as the 'backgroud' of the tile
        for(int i = 0; i < cards.length; i++){
            //Check that we have a front image of the card first
            String frontPath = cards[i].getCardFrontImg();
            if(frontPath == "" || frontPath == null){
                ImageView ivFiller = new ImageView(this);
                ivFiller.setImageResource(R.drawable.greybox);
                ivFiller.setLayoutParams(new ViewGroup.LayoutParams(
                        tileX, tileY
                ));
                newHorLayout.addView(ivFiller);
            }
            else {
                File file = new File(cards[i].getCardFrontImg());
                ImageView bg = new ImageView(this);
                bg.setLayoutParams(new ViewGroup.LayoutParams(tileX, tileY));
                newHorLayout.addView(bg);
                loadPic(file.getAbsolutePath(), bg);
            }
        }


        //Add the horizonal layout to the main vertical layout
        mainVertLayout.addView(newHorLayout, layoutParams);
    }

    //No Cards
    private void drawNothing(){
        TextView tv = new TextView(this);
        tv.setText("There is no cards to show yet");
        tv.setLayoutParams(new TableLayout.LayoutParams(
                0,
                TableLayout.LayoutParams.MATCH_PARENT));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);

        //Add the textview to the screen
        LinearLayout ll = (LinearLayout)findViewById(R.id.mainVertThisGiver);
        ll.addView(tv);
    }

    //Helper method to load a picture from the file
    private void loadPic(String photoPath, ImageView mImageView){

        //Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / tileX, photoH / tileY);

        //Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }
}
