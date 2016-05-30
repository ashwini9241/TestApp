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
import java.util.HashMap;

public class MyActivity extends ListActivity {

    public static final String TAG = "MyActivity";
    //url to get sample JSON data
    private static String urlStr = "https://gist.githubusercontent.com/maclir/f715d78b49c3b4b3b77f/raw/8854ab2fe4cbe2a5919cea97d71b714ae5a4838d/items.json";
    // JSON node names
    private static final String TITLE = "title";
    private static final String IMAGE_URL = "image";
    private static final String DESCRIPTION = "description";

    JSONArray sampleData = null;

    private ProgressDialog pDialog;
    ListView mListView;
    ArrayList<HashMap<String, String>> itemList;

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


            itemList = new ArrayList<HashMap<String, String>>();

            mListView = getListView();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String title = ((TextView) view.findViewById(R.id.tv_title)).getText().toString();
                    String detail = ((TextView) view.findViewById(R.id.tv_detail)).getText().toString();
                    HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);

                    String imageUrl = map.get(IMAGE_URL);

                    Intent in = new Intent(getApplicationContext(), ItemDetailActivity.class);
                    in.putExtra(TITLE, title);
                    in.putExtra(DESCRIPTION, detail);
                    in.putExtra(IMAGE_URL, imageUrl);

                    startActivity(in);
                }
            });

            new DownloadTask().execute(urlStr);
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

    /** A method to download json data from url */
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
                data = downloadUrl(params[0]);
                JSONArray jsonArray = new JSONArray(data);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString(TITLE);
                    String detail = jsonObject.getString(DESCRIPTION);
                    String imageUrl = jsonObject.getString(IMAGE_URL);

                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put(TITLE, title);
                    item.put(DESCRIPTION, detail);
                    item.put(IMAGE_URL, imageUrl);
                    itemList.add(item);
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

            ListAdapter adapter = new SimpleAdapter(MyActivity.this, itemList,
                    R.layout.lv_item, new String[] {TITLE, DESCRIPTION, IMAGE_URL},
                        new int[]{R.id.tv_title, R.id.tv_detail});
            setListAdapter(adapter);


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}
