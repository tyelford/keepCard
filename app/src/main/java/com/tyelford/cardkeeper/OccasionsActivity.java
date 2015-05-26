package com.tyelford.cardkeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tyelford.cardkeeper.data.CardDBHelper;
import com.tyelford.cardkeeper.data.NoOccasionException;


public class OccasionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occasions);

        //1.Get the occaions
        String[] allOccasions;
        try{
            allOccasions = getAllOccasions();

            //Loop through the occasions and draw them to the screen
            for(int i = 0; i < allOccasions.length; i++){
                //Check for nullability
                if(allOccasions[i] == null){
                    continue;
                }

                drawRow(allOccasions[i]);
            }
        }catch(NoOccasionException e){
            e.printStackTrace();
            //Print the No Occasoins Yet Message
            noOccasionsMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_occasions, menu);
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


    private String[] getAllOccasions() throws NoOccasionException{
        //Do Database Query
        CardDBHelper readCard = new CardDBHelper(this);
        return readCard.getAllOccasions();

    }

    private void drawRow(String thisOcc){
        //Get the Main Vertial Layout
        LinearLayout mainVert = (LinearLayout)findViewById(R.id.mainVertLayoutOcc);

        //Create a new Horizontal Layout
        LinearLayout mainHor = new LinearLayout(this);
        mainHor.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        mainHor.setPadding(0, toDp(8), 0, toDp(8));

        //Add a click listener to the Horizonal Layout
        mainHor.setClickable(true);
        mainHor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int bgColour = view.getDrawingCacheBackgroundColor();
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    LinearLayout ll = (LinearLayout) view;
                    ll.setBackgroundColor(0xFFC2BEBF);
                    return true;
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    LinearLayout ll = (LinearLayout) view;
                    ll.setBackgroundColor(bgColour);
                    TextView tv = (TextView)ll.getChildAt(0);
                    loadOccasionActivity(tv.getText().toString());
                    return true;
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_CANCEL){
                    LinearLayout ll = (LinearLayout) view;
                    ll.setBackgroundColor(bgColour);
                }
                return false;
            }
        });

        //Add the Text Field
        TextView tv = new TextView(this);
        tv.setText(thisOcc);
        tv.setLayoutParams(new TableLayout.LayoutParams(
                0,
                TableLayout.LayoutParams.MATCH_PARENT,
                0.6F
        ));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        //Add the TextView to the Horizonal Layout
        mainHor.addView(tv);

        //Possibly add a photo in here
        //TODO

        //Add a vertical divider
        View div = new View(this);
        div.setBackgroundColor(0xFFC2BEBF);
        div.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                toDp(1)
        ));

        //Add the horizontal view to the main layout
        mainVert.addView(mainHor, layoutParams);
        mainVert.addView(div);
    }

    private void noOccasionsMessage(){
        TextView tv = new TextView(this);
        tv.setText("There is no occasions to show yet");
        tv.setLayoutParams(new TableLayout.LayoutParams(
                0,
                TableLayout.LayoutParams.MATCH_PARENT));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);

        //Add the textview to the screen
        LinearLayout ll = (LinearLayout)findViewById(R.id.mainVertLayoutOcc);
        ll.addView(tv);
    }

    private void loadOccasionActivity(String dasOccasion){
        //Load up the next activity to dispaly all the cards from this Occ
        //TODO
    }

    //Method to convert a dp value into pixels
    private int toDp(int px){
        return (int)  (px * getResources().getDisplayMetrics().density);
    }
}
