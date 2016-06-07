package com.example.monalisa.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by monalisa on 27/05/16.
 * list item will be opened in detail in this activity,
 * image will be downloaded and shown.
 */
public class ItemDetailActivity extends Activity{
    public static final String TAG = "ItemDetailActivity";
    // JSON node names
    private ProgressDialog simpleWaitDialog;
    private ImageView ivImage;
    private boolean isDownloadFailed = false;
    private String imageUrl = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.itemdetail);

        Intent in = getIntent();
        // get values from intent, and then start AsyncTask to download image and update iv_image
        String title = in.getStringExtra(Constant.TITLE);
        String detail = in.getStringExtra(Constant.DESCRIPTION);
        imageUrl = in.getStringExtra(Constant.IMAGE_URL);

        Log.d(TAG, "onCreate: imageUrl:" + imageUrl);

        // Displaying all values on the screen
        TextView tvTitle = (TextView) findViewById(R.id.actv_title);
        TextView tvDesc = (TextView) findViewById(R.id.actv_detail);
        ivImage = (ImageView) findViewById(R.id.iv_image);

        tvTitle.setText(title);
        tvDesc.setText(detail);

        final Callback callback = new Callback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Image downloaded successfully");
                isDownloadFailed = false;
            }

            @Override
            public void onError() {
                isDownloadFailed = true;
            }
        };

        // new ImageDownloader().execute(imageUrl);
        Picasso.with(this).load(imageUrl).
                placeholder(R.drawable.ic_wait).
                error(R.drawable.ic_download).
                into(ivImage, callback);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDownloadFailed){
                    Picasso.with(ItemDetailActivity.this).load(imageUrl).
                            placeholder(R.drawable.ic_wait).
                            error(R.drawable.ic_download).
                            into(ivImage, callback);
                }
            }
        });

    }

}
