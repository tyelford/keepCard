package com.tyelford.cardkeeper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tyelford.cardkeeper.data.Card;
import com.tyelford.cardkeeper.data.CardDBHelper;


public class SingleGiverActivity extends Activity {

    //This Giver
    String dasGiver;
    //This Givers Cards
    Card[] dasGiverCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_giver);

        //Get the clicked Giver from the previous activity
        dasGiver = getIntent().getExtras().getString("clickedGiver");

        //Set the title for this activity
        setTitle("Cards From " + dasGiver);

        //Load up the info from the previous giver
        loadGiverData();
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

    private void loadGiverData(){
        //Do a database query and return an array of Card Objects from this giver
        CardDBHelper readCard = new CardDBHelper(this);
        dasGiverCards = readCard.getCardsFromGiver(dasGiver);
    }
}
