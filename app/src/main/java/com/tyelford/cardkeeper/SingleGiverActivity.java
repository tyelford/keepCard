package com.tyelford.cardkeeper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

    private static final int SCREEN_SCALER = 2;

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
    //Card counter side counter, left or right side
    boolean drawLeftSide = true;

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
            return;
        }


        for(int i = 0; i < dasGiverCards.length; i++) {
            //Draw a material card
            drawMaterialCard(dasGiverCards[i]);
        }

//        //Move the Array to a LinkedList to make it easier
//        //to work with
//        LinkedList<Card> theseCards = new LinkedList<Card>();
//        for(int i = 0; i < dasGiverCards.length; i++){
//            theseCards.addLast(dasGiverCards[i]);
//        }

//        //Use LinkedList as a queue, popping 3 off at a time
//        //to add to the horizontal layout of the next method
//        while(theseCards.size() > 0){
//
//            //Check for if there is 2 cards left
//            if(theseCards.size() == 2){
//                Card[] threeCards = new Card[2];
//                threeCards[0] = theseCards.removeFirst();
//                threeCards[1] = theseCards.removeFirst();
//
//                drawCards(threeCards);
//                break;
//            }
//
//            //Check for if there is 2 cards left
//            if(theseCards.size() == 1){
//                Card[] threeCards = new Card[1];
//                threeCards[0] = theseCards.removeFirst();
//
//                drawCards(threeCards);
//                break;
//            }
//
//            Card[] threeCards = new Card[3];
//            threeCards[0] = theseCards.removeFirst();
//            threeCards[1] = theseCards.removeFirst();
//            threeCards[2] = theseCards.removeFirst();
//
//            drawCards(threeCards);
//        }


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

    //Draw material cards to the screen
    private void drawMaterialCard(Card thisCard){
        //Get the side to draw on
        LinearLayout ll;
        if(drawLeftSide){
            ll = (LinearLayout)findViewById(R.id.llVertLeft);
        }else{
            ll = (LinearLayout)findViewById(R.id.llVertRight);
        }

        //Standard Height of the CardViews
        int standHeight = toDp(150);
        //Add some extra height if the 'notes' section is really long
        int extraHeight = 0;

        //Create a LinearLayout for the TextViews
        //Have to create this first to get the 'added' height of the CardView
        LinearLayout textLL = new LinearLayout(this);
        LinearLayout.LayoutParams textLLParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        textLL.setPadding(0, toDp(75), 0, 0);
        textLL.setOrientation(LinearLayout.VERTICAL);
        textLL.setLayoutParams(textLLParams);

        //Add the Occasion Text
        TextView occText = new TextView(this);
        occText.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        ));
        occText.setPadding(toDp(16), 0, 0, 0);
        occText.setTextSize(20F);
        occText.setText(thisCard.getOccasion());
        textLL.addView(occText);

        //Add the notes that are part of the card
        String notesText = thisCard.getCardComments();
        if(notesText == null || notesText.equals("")){
            //No Notes to add and can be skipped
            extraHeight = 0;
        }else{
            //Find any extra height for the card view
            char[] notesChar = notesText.toCharArray();

            if(notesChar.length > 35 && notesChar.length <= 70){
                extraHeight += toDp(45);
            }
            if(notesChar.length > 70){
                extraHeight += toDp(45);
                notesText.substring(0, 67);
                notesText += "...";
            }

            TextView notesTv = new TextView(this);
            notesTv.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            ));
            notesTv.setPadding(toDp(16), 0, 0, 0);
            notesTv.setTextSize(13F);
            notesTv.setText(notesText);
            textLL.addView(notesTv);
        }


        //Build the CardView
        CardView cv = new CardView(this);
        FrameLayout.LayoutParams cvLayoutParams = new FrameLayout.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                standHeight + extraHeight
        );
        cvLayoutParams.setMargins(0, toDp(10), 0, 0);
        cv.setLayoutParams(cvLayoutParams);

        //Add the background image to the card
        ImageView bgImg = new ImageView(this);
        bgImg.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                toDp(230),
                Gravity.CENTER
        ));
        cv.addView(bgImg);
        loadPic(thisCard.getCardFrontImg(), bgImg);

        //Add the overlay ImageView
        ImageView overlayImg = new ImageView(this);
        overlayImg.setImageResource(R.color.overlay);
        overlayImg.setLayoutParams(new FrameLayout.LayoutParams(
                toDp(175),
                standHeight + extraHeight
        ));
        overlayImg.setPadding(0, toDp(75), 0, 0);
        cv.addView(overlayImg);

        cv.addView(textLL);
        ll.addView(cv);

        //Add a spacer
        View view = new View(this);
        ViewGroup.LayoutParams vglp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                toDp(8)
        );
        view.setLayoutParams(vglp);
        ll.addView(view);



        //Change the size to draw
        if(drawLeftSide)
            drawLeftSide = false;
        else
            drawLeftSide = true;
    }

    //Draw the cards to the screen
//    private void drawCards(Card cards[]){
//        //Get the main vertical layout
//        LinearLayout mainVertLayout = (LinearLayout)findViewById(R.id.mainVertThisGiver);
//
//        //Create a new horizontal layout
//        LinearLayout newHorLayout = new LinearLayout(this);
//        newHorLayout.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//
//        //Load up the front of the card as the 'backgroud' of the tile
//        for(int i = 0; i < cards.length; i++){
//            //Check that we have a front image of the card first
//            String frontPath = cards[i].getCardFrontImg();
//            if(frontPath == "" || frontPath == null){
//                ImageView ivFiller = new ImageView(this);
//                ivFiller.setImageResource(R.drawable.greybox);
//                ivFiller.setLayoutParams(new ViewGroup.LayoutParams(
//                        tileX, tileY
//                ));
//                newHorLayout.addView(ivFiller);
//            }
//            else {
//                File file = new File(cards[i].getCardFrontImg());
//                ImageView bg = new ImageView(this);
//                bg.setLayoutParams(new ViewGroup.LayoutParams(tileX, tileY));
//                newHorLayout.addView(bg);
//                loadPic(file.getAbsolutePath(), bg);
//            }
//        }
//
//
//        //Add the horizonal layout to the main vertical layout
//        mainVertLayout.addView(newHorLayout, layoutParams);
//    }

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
        LinearLayout ll = (LinearLayout)findViewById(R.id.llVertLeft);
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

    //Method to convert a dp value into pixels
    private int toDp(int px){
        return (int)  (px * getResources().getDisplayMetrics().density);
    }

}
