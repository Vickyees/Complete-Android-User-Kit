package com.example.completeandroiduserkit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DictionaryMainActivity extends AppCompatActivity {
    String url;

    private TextView showDef;
    private EditText enterWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_main);
        setTitle("Dictionary");

        enterWord=findViewById(R.id.e1);
        showDef=findViewById(R.id.t1);
    }

    public void meaning(View view) {

        final String l="en-gb";
        final String w=enterWord.getText().toString();
        final String f="definitions";
        final String sm="false";
        final String word_id=w.toLowerCase();

        if (word_id.equals("")){
            Toast.makeText(this, "Please enter a word", Toast.LENGTH_SHORT).show();
        }

        else {

            showDef.setText("");
            String a = "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + l + "/" + word_id + "?" + "fields=" + f + "&strictMatch=" + sm;

            DictionaryRequest dr = new DictionaryRequest(this, showDef);
            url = a;
            dr.execute(url);
            Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
        }
    }
}
