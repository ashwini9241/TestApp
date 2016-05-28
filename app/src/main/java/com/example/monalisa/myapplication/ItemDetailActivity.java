package com.example.monalisa.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by monalisa on 27/05/16.
 */
public class ItemDetailActivity extends Activity{
    public static final String TAG = "ItemDetailActivity";
    // JSON node names
    private static final String TITLE = "title";
    private static final String IMAGE_URL = "image";
    private static final String DESCRIPTION = "description";
    private ProgressDialog simpleWaitDialog;
    private ImageView ivImage;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.itemdetail);

        Intent in = getIntent();
        //TODO: get values from intent, and then start AsyncTask to download image and update iv_image
        // Get JSON values from previous intent
        String title = in.getStringExtra(TITLE);
        String detail = in.getStringExtra(DESCRIPTION);
        String imageUrl = in.getStringExtra(IMAGE_URL);

        Log.d(TAG, "onCreate: imageUrl:" + imageUrl);

        // Displaying all values on the screen
        TextView tvTitle = (TextView) findViewById(R.id.actv_title);
        TextView tvDesc = (TextView) findViewById(R.id.actv_detail);
        ivImage = (ImageView) findViewById(R.id.iv_image);

        tvTitle.setText(title);
        tvDesc.setText(detail);

    }

    private class ImageDownloader extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... param) {
            // TODO Auto-generated method stub
            //return downloadBitmap(param[0]);
            return null;
        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute Called");
            simpleWaitDialog = ProgressDialog.show(ItemDetailActivity.this,
                    "Wait", "Downloading Image");

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Log.i("Async-Example", "onPostExecute Called");
            ivImage.setImageBitmap(result);
            simpleWaitDialog.dismiss();

        }


    }
}
