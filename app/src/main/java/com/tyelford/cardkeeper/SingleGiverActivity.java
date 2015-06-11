package com.tyelford.cardkeeper;

import android.app.Activity;
import android.content.Intent;
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


public class SingleGiverActivity extends Activity {

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

        //Get the screen size in pixels
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenW = size.x;
        screenH = size.y;

        //Make the tile size
        tileX = screenW / 3;
        tileY = screenH / 3;

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
        //Draw the cards on the screen
        drawCards();
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
    private void drawCards(){
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
        ImageView bg = new ImageView(this);
        bg.setLayoutParams(new ViewGroup.LayoutParams(tileX, tileY));
        newHorLayout.addView(bg);
        loadPic(new String("path"), bg);
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
        
    }
}
