package com.example.completeandroiduserkit;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DictionaryRequest extends AsyncTask<String,Integer,String> {


    Context context;
    TextView showDef;

    DictionaryRequest(Context context, TextView tv){
        this.context=context;
        showDef=tv;
    }

    @Override
    protected String doInBackground(String... params) {
        final String app_id="b0d75c01";
        final String app_key="b4c9785647f293f503eb45df46298c9d";

        try{
            URL url=new URL(params [0]);
            HttpsURLConnection urlConnection=(HttpsURLConnection)url.openConnection();

            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);
            BufferedReader reader =new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder=new StringBuilder();

            String line=null;
            while ((line=reader.readLine())!=null){
                stringBuilder.append(line+"\n");
            }
            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        String def;



        try {
            JSONObject js = new JSONObject(s);
            JSONArray results = js.getJSONArray("results");

            for (int i = 0; i <= results.length(); i++) {

                JSONObject lEntries = results.getJSONObject(i);
                JSONArray laArray = lEntries.getJSONArray("lexicalEntries");

                JSONObject entries = laArray.getJSONObject(i);
                JSONArray e = entries.getJSONArray("entries");

                JSONObject jsonObject = e.getJSONObject(i);
                JSONArray senseArray = jsonObject.getJSONArray("senses");

                JSONObject de = senseArray.getJSONObject(i);
                JSONArray d = de.getJSONArray("definitions");

                def = d.getString(i);

                if (def.equals("")){
                    Toast.makeText(context, "Word not Found", Toast.LENGTH_SHORT).show();
                    showDef.setText("");
                }
                else {
                    showDef.setText(def);
                }


            }
        }
       catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("Result of Dictionary","onPostExecute"+s);
    }
}
