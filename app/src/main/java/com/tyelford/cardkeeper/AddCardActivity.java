package com.tyelford.cardkeeper;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tyelford.cardkeeper.data.Card;
import com.tyelford.cardkeeper.data.CardDBHelper;
import com.tyelford.cardkeeper.data.NoGiversException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddCardActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE_FRONT = 1;
    static final int REQUEST_IMAGE_CAPTURE_LEFT = 2;
    static final int REQUEST_IMAGE_CAPTURE_RIGHT = 3;
    static final int REQUEST_IMGAE_CAPTURE_PRESENT = 4;
    static final int REQUEST_NEW_OCCASION = 5;

    //Path of the three photos
    String frontPhotoPath;
    String inLeftPhotoPath;
    String inRightPhotoPath;
    String presentPhotoPath;

    //Info from previous Cards
    //String[] previousGivers;
    String[] previousOccasions;

    //Card to be Saved
    Card newCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        //Lock the screen in portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String[] previousGivers = null;
        //Get the givers and store them for now
        try{
            previousGivers = getPreviousGivers();
        }catch(NoGiversException e){
            //This is ok, just means there is no previous givers
            previousGivers = new String[]{};
        }

        //Populate the AutoCompleteTextView for givers
        fillPreviousGivers(previousGivers);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_card, menu);
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

    //Get a list of unique previous card givers
    private String[] getPreviousGivers() throws NoGiversException{
        CardDBHelper readCard = new CardDBHelper(this);
        return readCard.getUniqueCardGivers();
    }

    //Populate the previous givers in the AutoCompleteTextView
    private void fillPreviousGivers(String[] previousGivers){
        if(previousGivers.length == 0)
            return;
        AutoCompleteTextView actv = (AutoCompleteTextView)findViewById(R.id.insertGiverAutoCompleteTextView);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, previousGivers);
        actv.setAdapter(ad);

    }

    //Take a picture of the front of the card
    public void takeImageFront(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){

            File photoFile = null;
            try{
                photoFile = createImageFile("front");
            }catch(IOException ex){
                Toast.makeText(this, "Error Saving Image", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

            //Continue if the file was created successfully
            if(photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_FRONT);
            }
        }
    }

    //Take a picture of the inside left of the card
    public void takeImageLeft(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){

            File photoFile = null;
            try{
                photoFile = createImageFile("inLeft");
            }catch(IOException ex){
                Toast.makeText(this, "Error Saving Image", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

            //Continue if the file was created successfully
            if(photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_LEFT);
            }
        }
    }

    //Take a picture of the inside right of the card
    public void takeImageRight(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){

            File photoFile = null;
            try{
                photoFile = createImageFile("inRight");
            }catch(IOException ex){
                Toast.makeText(this, "Error Saving Image", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

            //Continue if the file was created successfully
            if(photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_RIGHT);
            }
        }
    }

    //Take a picture of the present
    public void takeImagePresent(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){

            File photoFile = null;
            try{
                photoFile = createImageFile("present");
            }catch(IOException ex){
                Toast.makeText(this, "Error Saving Image", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

            //Continue if the file was created successfully
            if(photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMGAE_CAPTURE_PRESENT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Place Thumbnail for front of card
        if(requestCode == REQUEST_IMAGE_CAPTURE_FRONT && resultCode == RESULT_OK){
            loadPic(frontPhotoPath, (ImageView)findViewById(R.id.imgViewFront));
        }

        //Place Thumbnail for inside left of card
        if(requestCode == REQUEST_IMAGE_CAPTURE_LEFT && resultCode == RESULT_OK){
            loadPic(inLeftPhotoPath, (ImageView)findViewById(R.id.imgViewLeft));
        }

        //Place Thumbnail for inside right of card
        if(requestCode == REQUEST_IMAGE_CAPTURE_RIGHT && resultCode == RESULT_OK){
            loadPic(inRightPhotoPath, (ImageView)findViewById(R.id.imgViewRight));
        }

        //Place Thumbnail of the present
        if(requestCode == REQUEST_IMGAE_CAPTURE_PRESENT && resultCode == RESULT_OK){
            loadPic(presentPhotoPath, (ImageView)findViewById(R.id.imgViewPresent));
        }

        //Finish from AddOccasionActivity
        if(requestCode == REQUEST_NEW_OCCASION && resultCode == RESULT_OK){
            String newOcc = data.getStringExtra("newOcc");
            AutoCompleteTextView occ = (AutoCompleteTextView)findViewById(R.id.insertOccasion);
            occ.setText(newOcc);
        }
    }

    //Method used to create a collision proof image file name
    private File createImageFile(String whichImage) throws IOException{
        //Create the image file
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File image = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), imageFileName);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //Create the cardbox folder if it does not exist
        File cardboxFolder = new File(storageDir + "/CardBox");
        if(!cardboxFolder.exists()){
            cardboxFolder.mkdir();
        }

        File image = File.createTempFile(imageFileName, ".jpg", cardboxFolder);

        //Save a file: path for use with ACTION_VIEW intents
        if(whichImage.equals("front"))
            frontPhotoPath = image.getAbsolutePath();
        if(whichImage.equals("inLeft"))
            inLeftPhotoPath = image.getAbsolutePath();
        if(whichImage.equals("inRight"))
            inRightPhotoPath = image.getAbsolutePath();
        if(whichImage.equals("present"))
            presentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Load the picture back from the file
    private void loadPic(String photoPath, ImageView mImageView){
        //Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

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

    public void saveCard(View view){
        //Save the Card
        newCard = new Card();

        newCard.setCardGiver(((AutoCompleteTextView) findViewById(R.id.insertGiverAutoCompleteTextView)).getText().toString());
        newCard.setCardFrontImg(frontPhotoPath);
        newCard.setCardInLeftImg(inLeftPhotoPath);
        newCard.setCardInRightImg(inRightPhotoPath);
        newCard.setPresentImg(presentPhotoPath);
        newCard.setOccasion(((AutoCompleteTextView) findViewById(R.id.insertOccasion)).getText().toString());
        newCard.setCardComments(((TextView) findViewById(R.id.insertComments)).getText().toString());
        newCard.setAddGivers(((TextView) findViewById(R.id.insertAddGivers)).getText().toString());

        //Save the Card to the database
        CardDBHelper writeCardDB = new CardDBHelper(this);
        writeCardDB.insertCard(newCard);

        Toast.makeText(this, "Card Saved!", Toast.LENGTH_LONG).show();

        //Goto the Person Activity
        //Clear the activity stack
        Intent intent = new Intent(getApplicationContext(), PersonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    //Create a new Occasion from this Activity
    public void newOccFromCard(View view){
        //Grab all the typed info and pass it to the new Occasion Activity
        //So when it return they don't have to retype
        Intent intent = new Intent(this, AddOccasionActivity.class);
        intent.putExtra("FromWhere", 2);
        intent.putExtra("cardGiver", ((AutoCompleteTextView) findViewById(R.id.insertGiverAutoCompleteTextView)).getText().toString());
        intent.putExtra("cardFrontImg", frontPhotoPath);
        intent.putExtra("cardInLeftImg", inLeftPhotoPath);
        intent.putExtra("cardInRightImg", inRightPhotoPath);
        intent.putExtra("cardPresentImg", presentPhotoPath);
        intent.putExtra("cardComments", ((TextView) findViewById(R.id.insertComments)).getText().toString());
        intent.putExtra("cardAddGivers", ((TextView) findViewById(R.id.insertAddGivers)).getText().toString());

        startActivityForResult(intent, REQUEST_NEW_OCCASION);
    }
}
