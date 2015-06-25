package com.tyelford.cardkeeper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.tyelford.cardkeeper.data.CardDBHelper;


public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Lock the screen in portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        //Do a database test
       // CardDBHelper dbHelper = new CardDBHelper(getContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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

    public void loadPersons(View view){
        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);
    }

    public void loadNewCard(View view){
        Intent intent = new Intent(this, AddCardActivity.class);
        startActivity(intent);
    }

    public void loadNewOccasion(View view){
        Intent intent = new Intent(this, AddOccasionActivity.class);
        intent.putExtra("FromWhere", 1);
        startActivity(intent);
    }

    public void loadOccasions(View view){
        Intent intent = new Intent(this, OccasionsActivity.class);
        startActivity(intent);
    }

    public void deleteDatabase(View view){
//        CardDBHelper deleteDB = new CardDBHelper(this);
//        deleteDB.deleteDB();
//        Toast.makeText(this, "You have deleted all database records", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, CardTest.class);
        startActivity(intent);
    }

}
