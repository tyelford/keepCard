package com.tyelford.cardkeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.tyelford.cardkeeper.data.CardDBHelper;
import com.tyelford.cardkeeper.data.Occasion;


public class AddOccasionActivity extends Activity {

    //Check if this came from add Card Page
    boolean fromAddCardPage = false;
    //Values from Add Card Page
    String cardGiver = "";
    String cardFrontImg = "";
    String cardInLeftImg = "";
    String cardInRightImg = "";
    String cardPresentImg = "";
    String cardComments = "";
    String cardAddGivers = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_occasion);

        Intent intent = getIntent();
        int fromWhere = intent.getIntExtra("FromWhere", 1);
        if(fromWhere == 2){
            //This means it came from the Add Card Page
            fromAddCardPage = true;
            cardGiver = intent.getStringExtra("cardGiver");
            cardFrontImg = intent.getStringExtra("cardFrontImg");
            cardInLeftImg = intent.getStringExtra("cardInLeftImg");
            cardInRightImg = intent.getStringExtra("cardInRightImg");
            cardPresentImg = intent.getStringExtra("cardPresentImg");
            cardComments = intent.getStringExtra("cardComments");
            cardAddGivers = intent.getStringExtra("cardAddGivers");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_occasion, menu);
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

    public void saveOccasion(View view){
        //Save the occasion
        Occasion occ = new Occasion();

        String thisOccName = ((TextView) findViewById(R.id.insertOccasionName)).getText().toString();

        occ.setOccName(thisOccName);
        occ.setOccLocation(((TextView) findViewById(R.id.insertOccasionLocation)).getText().toString());
        occ.setOccNotes(((TextView)findViewById(R.id.insertOccasionNotes)).getText().toString());
        //Formate the date
        DatePicker dp = (DatePicker)findViewById(R.id.occasionDatePicker);
        String day = Integer.toString(dp.getDayOfMonth());
        String month = Integer.toString(dp.getMonth());
        String year = Integer.toString(dp.getYear());
        String date = year + month + day;
        //Set the date to the obj
        occ.setOccDate(date);

        //Save the Occasion to the database
        CardDBHelper writeOccDB = new CardDBHelper(this);
        writeOccDB.insertOccasion(occ);

        Toast.makeText(this, "Occasion Saved!", Toast.LENGTH_LONG).show();

        //Check if we came from the Add new card page
        if(fromAddCardPage){
            Intent intent = new Intent();
            intent.putExtra("newOcc", thisOccName);
            setResult(RESULT_OK, intent);
            finish();
        }else {
            //Go to the Occasions Activity
            //Clear the Activity Stack
            Intent intent = new Intent(getApplicationContext(), OccasionsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
