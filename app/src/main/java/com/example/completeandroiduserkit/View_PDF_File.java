package com.example.completeandroiduserkit;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class View_PDF_File extends AppCompatActivity {

    ListView myPDFListview;
    FirebaseUser user1;
    String uid;
    DatabaseReference databaseReference;
    List<Uploadpdf1>uploadPDFs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__pdf__file);
        setTitle("PDF VIEW");


        myPDFListview=(ListView)findViewById(R.id.myListView);

        uploadPDFs=new ArrayList<>();

        ViewAllFiles();

        myPDFListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uploadpdf1 uploadpdf1=uploadPDFs.get(i);
                Intent intent=new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadpdf1.getUrl()));
                startActivity(intent);
            }
        });
    }



    private void ViewAllFiles() {
        user1= FirebaseAuth.getInstance().getCurrentUser();
        uid=user1.getUid();
    databaseReference= FirebaseDatabase.getInstance().getReference("PDF").child(uid);
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                Uploadpdf1 uploadpdf1 = postSnapshot.getValue(Uploadpdf1.class);
                uploadPDFs.add(uploadpdf1);

            }
            String[] uploads = new String[uploadPDFs.size()];
            for (int i=0;i<uploads.length;i++){
                uploads[i]=uploadPDFs.get(i).getName();
            }

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads){
                @Override
                public View getView(int position,  View convertView, ViewGroup parent) {
                    View view =super.getView(position, convertView, parent);

                    TextView myText = (TextView)view.findViewById(android.R.id.text1);
                    myText.setTextColor(Color.BLACK);


                    return view;
                }
            };

            myPDFListview.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }
}
