package com.tyelford.cardkeeper;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class CardActivity extends Activity {

    private String cardGiver;
    private int cardFrontImgRes;
    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        //Lock the screen in portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Set Backup button on Action Bar
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        //Get the information from the Intent
        cardGiver = intent.getStringExtra("cardGiver");
        cardFrontImgRes = intent.getIntExtra("cardFrontImgRes", 0);
        int cardInsideLeftImgRes = intent.getIntExtra("cardInsideLeftImgRes", 0);
        int cardInsideRightImgRes = intent.getIntExtra("cardInsideRightImgRes", 0);
        int cardBackImgRes = intent.getIntExtra("cardBackImgRes", 0);
        String cardComments = intent.getStringExtra("cardComments");
        String incPresent = intent.getStringExtra("incPresent");

        //Add the TextView for who the card is from
        TextView cardGiverTextView = (TextView) findViewById(R.id.cardGiverTextView);
        cardGiverTextView.setText(cardGiver);

        //Add the front of the card image
        ImageView image = (ImageView)findViewById(R.id.imgFront);
        image.setImageResource(cardFrontImgRes);

        //Add the inside left image
        image = (ImageView)findViewById(R.id.imgInsideLeft);
        image.setImageResource(cardInsideLeftImgRes);

        //Add the inside right image
        image = (ImageView)findViewById(R.id.imgInsideRight);
        image.setImageResource(cardInsideRightImgRes);

        //Add the back image
        image = (ImageView)findViewById(R.id.imgBack);
        image.setImageResource(cardBackImgRes);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch(item.getItemId()){
            case android.R.id.home:
                finish();
        }

//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //Open the full screen card
    public void openFullScreen(View view){
        Intent intent = new Intent(this, FullScreenCard.class);
        intent.putExtra("cardImageRes", cardFrontImgRes);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //Return from the full screen card
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            //Get some databack if needed

        }
    }
}
