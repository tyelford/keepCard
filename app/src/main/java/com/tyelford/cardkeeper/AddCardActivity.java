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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tyelford.cardkeeper.data.Card;
import com.tyelford.cardkeeper.data.CardDBHelper;

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

    //Path of the three photos
    String frontPhotoPath;
    String inLeftPhotoPath;
    String inRightPhotoPath;
    String presentPhotoPath;

    //Card to be Saved
    Card newCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        //Lock the screen in portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        newCard.setCardGiver(((TextView) findViewById(R.id.insertGiver)).getText().toString());
        newCard.setCardFrontImg(frontPhotoPath);
        newCard.setCardInLeftImg(inLeftPhotoPath);
        newCard.setCardInRightImg(inRightPhotoPath);
        newCard.setPresentImg(presentPhotoPath);
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


    //Method to respond to a present being added
//    public void presentAdded(View view){
//
//        //Is the view now checked?
//        boolean checked = ((CheckBox) view).isChecked();
//
//        //Check which checkbox was clicked
//        switch (view.getId()){
//            case R.id.addPresentCheckBox:
//                if(checked)
//                    //Add some code for adding a present
//                    Toast.makeText(this, "Checkbox ticked", Toast.LENGTH_SHORT).show();
//                else
//                    //Add some code to remove a present
//                    Toast.makeText(this, "Checkbox unticked", Toast.LENGTH_SHORT).show();
//                break;
//            //Add more checkboxes here if needed
//        }
//    }
}
