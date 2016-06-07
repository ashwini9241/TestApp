package com.example.monalisa.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyActivity extends ListActivity {

    public static final String TAG = "MyActivity";

    JSONArray sampleData = null;

    private ProgressDialog pDialog;
    ListView mListView;
    ArrayList<DummyData> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        // check if not online
        if(! isOnline()){

            try {
                final AlertDialog alertDialog = new AlertDialog.Builder(MyActivity.this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });

                alertDialog.show();
            }
            catch(Exception e)
            {
                Log.d(TAG, "Show Dialog: "+e.getMessage());
            }
        } else {


            itemList = new ArrayList<DummyData>();
            mListView = getListView();

            // Download and parse JSON data using given URL asynchronously.
            new DownloadTask().execute(Constant.DataURL);
        }
    }

    private boolean isOnline(){
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }

    /** method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb  = new StringBuilder();

            String line;
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d(TAG,"Exception while downloading url" + e.toString());
        }finally{
            if(iStream != null)
                iStream.close();
        }

        return data;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String data = null;
            try{
                // download JSON data from given url
                data = downloadUrl(params[0]);

                // parse JSON array and create list<DummyData>
                JSONArray jsonArray = new JSONArray(data);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString(Constant.TITLE);
                    String detail = jsonObject.getString(Constant.DESCRIPTION);
                    String imageUrl = jsonObject.getString(Constant.IMAGE_URL);

                    itemList.add(new DummyData(title, detail, imageUrl));
                }
            }catch(Exception e){
                android.util.Log.d(TAG, "Background Task" + e.toString());
            }
            Log.d(TAG, "doInBackground: list size =" + itemList.size());
            return data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            // changed to customAdapter, added image view to list item.
            // handling loading of imageview from customAdapter.

            final CustomListAdapter adapter = new CustomListAdapter(MyActivity.this, itemList);
            mListView.setAdapter(adapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    DummyData data = adapter.getItem(position);
                    Intent in = new Intent(getApplicationContext(), ItemDetailActivity.class);
                    in.putExtra(Constant.TITLE, data.getTitle());
                    in.putExtra(Constant.DESCRIPTION, data.getDescription());
                    in.putExtra(Constant.IMAGE_URL, data.getImageUrl());

                    startActivity(in);
                }
            });


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}
