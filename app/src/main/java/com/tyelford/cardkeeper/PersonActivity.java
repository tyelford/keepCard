package com.tyelford.cardkeeper;

/*
This class is used to display a list of the unique card givers
It will show each card giver in a list form and also show
the most recent three front of card pictures in a small bitmap

Author: Tyson Elford
 */


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tyelford.cardkeeper.data.Card;
import com.tyelford.cardkeeper.data.CardDBHelper;
import com.tyelford.cardkeeper.data.NoGiversException;

import java.io.File;


public class PersonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        //Lock the screen in portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //1. Get the names of all the unique givers
        String[] uniqueGivers;
        try {
            uniqueGivers = getUniqueCardGivers();

            //Loop through the unique givers and draw them to the screen
            for(int i = 0; i < uniqueGivers.length; i++) {
                //Find the first three photos for a giver
                String[] photos = getThreePhotosPerGiver(uniqueGivers[i]);
                //Draw the row
                drawRow(uniqueGivers[i], photos);
            }

        }catch(NoGiversException e){
            e.printStackTrace();
            //Print the No Cards Yet Message
            noGiversMessage();
        }
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
    private String[] getUniqueCardGivers() throws NoGiversException{
        //Do Database Query here
        CardDBHelper readCard = new CardDBHelper(this);
        return readCard.getUniqueCardGivers();
    }

    //Get three photos for a given giver - will just return the path on disk
    public String[] getThreePhotosPerGiver(String giver){
        //Call the database method
        CardDBHelper readCard = new CardDBHelper(this);
        return readCard.getThreePhotosPerGiver(giver);
    }


    //Method used to create the next item in the veritcal layout
    private void drawRow(String thisGiver, String[] photos){
        //Get the Main Vertical Layout
        LinearLayout mainVert = (LinearLayout)findViewById(R.id.mainVertLayout);

        //Create a new horizontal Layout
        LinearLayout mainHor = new LinearLayout(this);
        mainHor.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

//        layoutParams.setMargins(0, toDp(8), 0, 0);
        mainHor.setPadding(0, toDp(8), 0, toDp(8));

        //Add Click Listener to Horizonal Linear
        mainHor.setClickable(true);
        int thisId = thisGiver.hashCode();
        mainHor.setId(thisId);
//        mainHor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(PersonActivity.this, "I was clicked", Toast.LENGTH_LONG).show();
//            }
//        });
        mainHor.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int bgColour = view.getDrawingCacheBackgroundColor();
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    LinearLayout ll = (LinearLayout) view;
                    ll.setBackgroundColor(0xFFC2BEBF);
                    return true;
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    LinearLayout ll = (LinearLayout) view;
                    ll.setBackgroundColor(bgColour);
                    TextView textV = (TextView)ll.getChildAt(0);
//                    Toast.makeText(PersonActivity.this, textV.getText(), Toast.LENGTH_LONG).show();
                    loadGiverActivity(textV.getText().toString());
                    return true;
                }
                return false;
            }
        });

        //Add the Text Field
        TextView tv = new TextView(this);
        tv.setText(thisGiver);
        tv.setLayoutParams(new TableLayout.LayoutParams(
                0,
                TableLayout.LayoutParams.MATCH_PARENT,
                0.6F));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        //Add the TextView to the Horizontal Layout
        mainHor.addView(tv);


        //CHeck if there is photos
        if(photos.length != 0){
            //LinearLayout to hold images
            LinearLayout imgHor = new LinearLayout(this);
            imgHor.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams imgLayoutParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            imgLayoutParams.weight = 0.4F;

            //Add upto three images
            for(int i = 0; i < photos.length; i++){
                File file = new File(photos[i]);
                if(file.exists()){
                    ImageView img = new ImageView(this);
                    img.setLayoutParams(new ViewGroup.LayoutParams(toDp(30), toDp(30)));
                    imgHor.addView(img);
                    loadPic(file.getAbsolutePath(), img);
                }
            }
            mainHor.addView(imgHor);
        }


//        //Add the Images to the Horizontal Layout
//        ImageView img = new ImageView(this);
//        img.setImageResource(R.drawable.logo1);
//        img.setLayoutParams(new ViewGroup.LayoutParams(toDp(30), toDp(30)));
//
//
//        ImageView img2 = new ImageView(this);
//        img2.setImageResource(R.drawable.logo1);
//        img2.setLayoutParams(new ViewGroup.LayoutParams(toDp(30), toDp(30)));
//
//        //Add images to Layout
//        imgHor.addView(img);
//        imgHor.addView(img2);


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

    //Load the picture back from the file
    private void loadPic(String photoPath, ImageView mImageView){
        //Get the dimensions of the View
        int targetW = toDp(30);
        int targetH = toDp(30);

        //Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        //Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    private void noGiversMessage(){
        TextView tv = new TextView(this);
        tv.setText("There is no cards to show yet");
        tv.setLayoutParams(new TableLayout.LayoutParams(
                0,
                TableLayout.LayoutParams.MATCH_PARENT));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);

        //Add the textview to the screen
        LinearLayout ll = (LinearLayout)findViewById(R.id.mainVertLayout);
        ll.addView(tv);
    }

    private void loadGiverActivity(String dasGiver){
        //Load up the next activity to display all the cards from a single giver

    }
}
