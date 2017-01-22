package com.example.studerande.upg31;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView thePhotoTakenImageView;
    private String mImageFileLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button pic_button = (Button) findViewById(R.id.take_picture_button);
        thePhotoTakenImageView = (ImageView) findViewById(R.id.PhotoTakenImageView);

        // Set image
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String imgPref = settings.getString("image_data", "");
        Bitmap imageB;
        if(imgPref != "")
        {
            imageB = decodeToBase64(imgPref);
            thePhotoTakenImageView.setImageBitmap(imageB);
        }

        // Exit button
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });


      /*
      commentated out because right now  program runs takePhoto() from the code generated through xml file
      pic_button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                pic_button.setText("lol");
                takePhoto(v);
            }
        });
        */
    }
    // Made by http://stackoverflow.com/questions/31826008/how-to-save-images-to-imageview-using-shared-preferences
    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void takePhoto(View v)
    {
        Intent takePictureApplicationIntent = new Intent();
        takePictureApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

       /*
        File photoFile = null;
        try {
            photoFile = createImageFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        */
       // takePictureApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(takePictureApplicationIntent, REQUEST_IMAGE_CAPTURE);

    }
    public void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            // small toast message
            Toast.makeText(this, "Picture taken successfully", Toast.LENGTH_SHORT).show();
            // get the photo we took
             Bundle extras = data.getExtras();
             Bitmap photoCapturedBitMap = (Bitmap) extras.get("data");
             thePhotoTakenImageView.setImageBitmap(photoCapturedBitMap);
             // Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
             // thePhotoTakenImageView.setImageBitmap(photoCapturedBitmap);

            SharedPreferences myPrefrence = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = myPrefrence.edit();
            editor.putString("image_data", encodeToBase64(photoCapturedBitMap));
            editor.commit();
        }
    }
    /*File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStorageDirectory();

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;
    }*/

}