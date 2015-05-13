package com.tyelford.cardkeeper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tyelford.cardkeeper.data.Card;
import com.tyelford.cardkeeper.data.CardDBHelper;

import java.util.ArrayList;


public class PersonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);


        setupMenu();
        //Something along the lines of
        //1. Get a Card[] with all the cards for unique givers
        Card[] cards = getUniqueCardGivers();
        //2. foreach the Card[]
        //3. Call drawRow for each unique card and add three pictures only
        //4. draw will have to be modified to take a Card object

//        drawRow();
//        drawRow();
//        drawRow();
//        drawRow();

        //Lock the screen in portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_person, menu);



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

    //On hardware back button always goto home screen
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getApplicationContext(), Splash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void setupMenu(){
        CardDBHelper readCard = new CardDBHelper(this);
        Card card = readCard.getCard(2);

        Toast.makeText(this, card.getCardID(), Toast.LENGTH_SHORT).show();
    }

    //Method to get the unique card givers
    private Card[] getUniqueCardGivers(){
        //Do Database Query here
        CardDBHelper readCard = new CardDBHelper(this);
        ArrayList<Card> cards = readCard.getUniqueCardGivers();


        return null;
    }

    //Method used to create the next item in the veritcal layout
    private void drawRow(Card card){
        //Get the Main Vertical Layout
        LinearLayout mainVert = (LinearLayout)findViewById(R.id.mainVertLayout);

        //Create a new horizontal Layout
        LinearLayout mainHor = new LinearLayout(this);
        mainHor.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(0, toDp(8), 0, toDp(8));

        //Add the Text Field
        TextView tv = new TextView(this);
        tv.setText(card.getCardGiver());
        tv.setLayoutParams(new TableLayout.LayoutParams(
                0,
                TableLayout.LayoutParams.MATCH_PARENT,
                0.6F));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        //Add the TextView to the Horizontal Layout
        mainHor.addView(tv);

        //LinearLayout to hold images
        LinearLayout imgHor = new LinearLayout(this);
        imgHor.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams imgLayoutParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        imgLayoutParams.weight = 0.4F;

        //Add the Images to the Horizontal Layout
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.logo1);
        img.setLayoutParams(new ViewGroup.LayoutParams(toDp(30), toDp(30)));


        ImageView img2 = new ImageView(this);
        img2.setImageResource(R.drawable.logo1);
        img2.setLayoutParams(new ViewGroup.LayoutParams(toDp(30), toDp(30)));

        //Add images to Layout
        imgHor.addView(img);
        imgHor.addView(img2);
        mainHor.addView(imgHor);

        //Add vertical divider
        View div = new View(this);
        div.setBackgroundColor(0xFFC2BEBF);
        div.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                toDp(1)
        ));

        //Add the horizontal LinearLayout to the Main Layout
        mainVert.addView(mainHor, layoutParams);
        mainVert.addView(div);


    }


    //Method to convert a dp value into pixels
    private int toDp(int px){
        return (int)  (px * getResources().getDisplayMetrics().density);
    }
}
