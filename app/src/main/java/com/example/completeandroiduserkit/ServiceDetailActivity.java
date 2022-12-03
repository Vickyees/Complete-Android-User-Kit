package com.example.completeandroiduserkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ServiceDetailActivity extends AppCompatActivity {

    TextView dName,dService,dLocation,dExprience,dAge,dPhone,dAddress,dDescription,dGender,dUid,Smile;
    ImageView dpropic,like;
    String key="";
    String imageurl="",checkuid;
    LinearLayout linearLayout,addfeedbacklayout;
    EditText text_feedback;
    FirebaseUser user1;
    String cuid;
    private BottomSheetDialog bottomSheetDialog;
    Button del;
    String a1,a2,a3,a4,a5,a6,a7,a8,a9,a10;
    private ValueEventListener valueEventListener;
    public static String verifysmile="";
    DatabaseReference reff;
    public static Double countsmile;
    public  static String t="0";
    RecyclerView fbRecyclerview;
    List<Feedback> myFeedbackList;
    DatabaseReference databaseReference;
    MyAdapter myAdapter;
    private AdView MadView;
    private InterstitialAd mInterstitialAd;
    private UnifiedNativeAd nativeAd;
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-7762392222724955/2786222234";
    DatabaseReference rootRef,demoRef;
    private Button refresh;
    private CheckBox startVideoAdsMuted;
    private TextView videoStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        setTitle("Profile Details");
        MobileAds.initialize(this,"ca-app-pub-7762392222724955~2438737873") ;
        MadView = findViewById(R.id.SeradView);
        AdRequest adRequest = new AdRequest.Builder().build();
        MadView.loadAd(adRequest);
        refresh = findViewById(R.id.btn_refresh);
        startVideoAdsMuted = findViewById(R.id.cb_start_muted);
        videoStatus = findViewById(R.id.tv_video_status);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View unusedView) {
                refreshAd();
            }
        });
        refreshAd();
        dName=(TextView)findViewById(R.id.dName);
        dService=(TextView)findViewById(R.id.dService);
        dLocation=(TextView)findViewById(R.id.dLocation);
        dPhone=(TextView)findViewById(R.id.dPhone);
        dExprience=(TextView)findViewById(R.id.dExprience);
        dAge=(TextView)findViewById(R.id.dAge);
        dAddress=(TextView)findViewById(R.id.dAddress);
        dDescription=(TextView)findViewById(R.id.dDescription);
        dGender=(TextView)findViewById(R.id.dGender);
        dUid=(TextView)findViewById(R.id.dUid);
        text_feedback=(EditText)findViewById(R.id.txt_feedback);
        Smile=(TextView)findViewById(R.id.cSmile);
        del=(Button)findViewById(R.id.delbtn);
       like=(ImageView)findViewById(R.id.like);
        dpropic=(ImageView)findViewById(R.id.dDp);
        user1 = FirebaseAuth.getInstance().getCurrentUser();
        cuid = user1.getUid();

            Bundle mbundle=getIntent().getExtras();

            if (mbundle!=null){

                key=mbundle.getString("keyValue");
                imageurl=mbundle.getString("Image");
                dName.setText(mbundle.getString("Name"));
                dService.setText(mbundle.getString("Ser"));
                dLocation.setText(mbundle.getString("Location"));
                dPhone.setText(mbundle.getString("Phone"));
                dExprience.setText(mbundle.getString("Exp"));
                dAge.setText(mbundle.getString("Age"));
                dAddress.setText(mbundle.getString("Add"));
                dDescription.setText(mbundle.getString("Desc"));
                dGender.setText(mbundle.getString("Gender"));
                dUid.setText(mbundle.getString("uid"));
                a10=(mbundle.getString("uid"));
                a7=(mbundle.getString("Gender"));
                if (imageurl.equals("eee")){
                    dpropic.setImageResource(R.drawable.eee);
                }
                else {
                    Glide.with(this)
                            .load(mbundle.getString("Image"))
                            .into(dpropic);
                }
            }


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reflike = database.getReference("Smiles");

        reflike.child(cuid).orderByChild("smile").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    verifysmile="s";

                }
                else {
                    verifysmile="n";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ServiceDetailActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });


        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("CountSmile");

        demoRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    t= dataSnapshot.getValue(String.class);
                    try {
                        countsmile=Double.parseDouble(t);
                        if (countsmile>1000){
                            Double a=countsmile/1000;
                            int i = Integer.valueOf(a.intValue());
                            Smile.setText(String.valueOf(i)+" k");
                        }
                        else {
                            Smile.setText(t);
                        }
                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + nfe);
                    }
                }
                else {
                   Smile.setText("0");
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ServiceDetailActivity.this, "Check Your internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        checkuid=dUid.getText().toString();
            linearLayout=findViewById(R.id.UDbtn);
        addfeedbacklayout=findViewById(R.id.addfeedbacklayout);

        if (cuid.equals(checkuid)){
            linearLayout.setVisibility(View.VISIBLE);
            addfeedbacklayout.setVisibility(View.GONE);
        }



        fbRecyclerview=(RecyclerView)findViewById(R.id.fbrecyclerview);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(ServiceDetailActivity.this,1);
        fbRecyclerview.setLayoutManager(gridLayoutManager);

        myFeedbackList=new ArrayList<>();

        myAdapter=new MyAdapter(ServiceDetailActivity.this,myFeedbackList);
        fbRecyclerview.setAdapter(myAdapter);
        preparedAD();

    }


    public void preparedAD(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7762392222724955/5387021762");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void delete(View view) {

        if (imageurl.equals("eee")){
            bottomSheetDialog=new BottomSheetDialog(ServiceDetailActivity.this);
            bottomSheetDialog.setContentView(R.layout.dialog_layout);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            Button yes=bottomSheetDialog.findViewById(R.id.yes);
            Button No=bottomSheetDialog.findViewById(R.id.no);
            TextView t=bottomSheetDialog.findViewById(R.id.confirm);
            t.setText("Are You Sure Want To DELETE ?");

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }

                    final DatabaseReference referencecount= FirebaseDatabase.getInstance().getReference("CountSmile").child(key);
                    referencecount.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Services").child(dLocation.getText().toString());

                            reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    final DatabaseReference referencecount= FirebaseDatabase.getInstance().getReference("Feedback").child(key);
                                    referencecount.removeValue();
                                    Toast.makeText(ServiceDetailActivity.this, "Profile Deleted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ServiceDetailActivity.this, UZMainActivity.class));
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ServiceDetailActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ServiceDetailActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });

            No.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.show();
        }

        else {
            bottomSheetDialog = new BottomSheetDialog(ServiceDetailActivity.this);
            bottomSheetDialog.setContentView(R.layout.dialog_layout);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            Button yes = bottomSheetDialog.findViewById(R.id.yes);
            Button No = bottomSheetDialog.findViewById(R.id.no);
            TextView t = bottomSheetDialog.findViewById(R.id.confirm);
            t.setText("Are You Sure Want To DELETE ?");

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Services").child(dLocation.getText().toString());
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    StorageReference storageReference = storage.getReferenceFromUrl(imageurl);

                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    final DatabaseReference referencecount= FirebaseDatabase.getInstance().getReference("CountSmile").child(key);
                                    referencecount.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            final DatabaseReference referencecount= FirebaseDatabase.getInstance().getReference("Feedback").child(key);
                                            referencecount.removeValue();
                                            Toast.makeText(ServiceDetailActivity.this, "Profile Deleted", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ServiceDetailActivity.this, UZMainActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ServiceDetailActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ServiceDetailActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ServiceDetailActivity.this, "check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            No.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.show();
        }

    }
    public void updateservice(View view) {
        startActivity(new Intent(ServiceDetailActivity.this, updateServive.class)
        .putExtra("Name",dName.getText().toString())
                .putExtra("Service",dService.getText().toString())
                        .putExtra("Location",dLocation.getText().toString())
                        .putExtra("Phone",dPhone.getText().toString())
                        .putExtra("Expr",dExprience.getText().toString())
                        .putExtra("Age",dAge.getText().toString())
                        .putExtra("Address",dAddress.getText().toString())
                        .putExtra("Desc",dDescription.getText().toString())
                        .putExtra("Gender",dGender.getText().toString())
                        .putExtra("Uid",dUid.getText().toString())
                        .putExtra("Image",imageurl)
                .putExtra("Key",key)
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.msave:
                bottomSheetDialog = new BottomSheetDialog(ServiceDetailActivity.this);
                bottomSheetDialog.setContentView(R.layout.dialog_layout);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                Button yes = bottomSheetDialog.findViewById(R.id.yes);
                Button No = bottomSheetDialog.findViewById(R.id.no);
                TextView t = bottomSheetDialog.findViewById(R.id.confirm);
                t.setText("Are You Sure Want To SAVE ?");


                ImageView ynicon = bottomSheetDialog.findViewById(R.id.ynicon);
                ynicon.setImageResource(R.drawable.ic_save_24dp);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        uploadService();

                    }
                });

                No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();

                break;
        }
        return true;
    }
    public void uploadService(){
       a1= dName.getText().toString().trim().toUpperCase();
                a2=dService.getText().toString().trim().toUpperCase();
                a3=dLocation.getText().toString().trim().toUpperCase();
                a4=dPhone.getText().toString().trim();
                a5=dExprience.getText().toString().trim();
                a6=dAge.getText().toString().trim();
                a8=dAddress.getText().toString().trim();
                a9=dDescription.getText().toString().trim();



        ServiceData serviceData = new ServiceData(
                a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,imageurl
        );


        FirebaseDatabase.getInstance().getReference("save").child(cuid)
                .child(key).setValue(serviceData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(ServiceDetailActivity.this, "Profile Added to save Page", Toast.LENGTH_SHORT).show();


                }
                else {
                    Toast.makeText(ServiceDetailActivity.this, "Profile not added", Toast.LENGTH_SHORT).show();
                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ServiceDetailActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addFeedback(View view) {


        if (text_feedback.getText().toString().equals("")){
            Toast.makeText(this, "Enter Feedback", Toast.LENGTH_SHORT).show();
        }
        else
            {
            Feedback feedback = new Feedback(
                    text_feedback.getText().toString().trim()
            );


            String myCurrentDateTime = DateFormat.getDateTimeInstance()
                    .format(Calendar.getInstance().getTime());

            FirebaseDatabase.getInstance().getReference("Feedback").child(key)
                    .child(myCurrentDateTime+cuid).setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(ServiceDetailActivity.this, "Feedback Added", Toast.LENGTH_SHORT).show();
                        text_feedback.setText("");

                    }
                    else {
                        Toast.makeText(ServiceDetailActivity.this, "Feedback Not Added", Toast.LENGTH_SHORT).show();
                    }



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ServiceDetailActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void showfeedback(View view) {

        databaseReference= FirebaseDatabase.getInstance().getReference("Feedback").child(key);

        valueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myFeedbackList.clear();

                if (!dataSnapshot.exists()){
                    Toast.makeText(ServiceDetailActivity.this, "No Feedback Found", Toast.LENGTH_SHORT).show();
                }
                else {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        Feedback feedback = itemSnapshot.getValue(Feedback.class);
                        myFeedbackList.add(feedback);
                    }
                    myAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ServiceDetailActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void addsmile(View view) {

        if (verifysmile.equals("s")){
            Toast.makeText(this, "Already Smiled", Toast.LENGTH_SHORT).show();
        }

        else {
            Double aa = countsmile + 1.0;
            int ab = Integer.valueOf(aa.intValue());
            String ba=String.valueOf(ab);
            Smile.setText(ba);

            FirebaseDatabase.getInstance().getReference("CountSmile").child(key).setValue(ba).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        like();
                        verifysmile="s";
                    }
                    else {
                        Toast.makeText(ServiceDetailActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ServiceDetailActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    public void like(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("Smiles").child(cuid).child(myCurrentDateTime).child("smile").setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ServiceDetailActivity.this, "Smiled", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ServiceDetailActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ServiceDetailActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            videoStatus.setText(String.format(Locale.getDefault(),
                    "Video status: Ad contains a %.2f:1 video asset.",
                    vc.getAspectRatio()));

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    refresh.setEnabled(true);
                    videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
            videoStatus.setText("Video status: Ad does not contain a video asset.");
            refresh.setEnabled(true);
        }
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private void refreshAd() {
        refresh.setEnabled(false);

        AdLoader.Builder builder = new AdLoader.Builder(this, ADMOB_AD_UNIT_ID);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                FrameLayout frameLayout =
                        findViewById(R.id.fl_adplaceholder);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                        .inflate(R.layout.ad_unified, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(startVideoAdsMuted.isChecked())
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                refresh.setEnabled(true);
              //  Toast.makeText(ServiceDetailActivity.this, "Failed to load native ad: "+ errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

        videoStatus.setText("");
    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }
}
