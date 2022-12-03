package com.example.completeandroiduserkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_up extends AppCompatActivity {
    EditText mail,pass,conpass;
    CheckBox show;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);


        mail=(EditText)findViewById(R.id.e);
        pass=(EditText)findViewById(R.id.p1);
        conpass=(EditText)findViewById(R.id.p2);
        show=(CheckBox)findViewById(R.id.show);


        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    conpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    conpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();

    }

    public void login(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    public void register(View view) {
        final String email=mail.getText().toString().trim();
        final String password=pass.getText().toString().trim();
        String cpassword=conpass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(Sign_up.this, "please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(Sign_up.this, "please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(cpassword)){
            Toast.makeText(Sign_up.this, "please enter confirm password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6){
            Toast.makeText(Sign_up.this, "password too short", Toast.LENGTH_SHORT).show();
        }
        if(password.equals(cpassword)){

            firebaseAuth.fetchSignInMethodsForEmail(mail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                            boolean check = !task.getResult().getSignInMethods().isEmpty();
                            if (check) {
                                Toast.makeText(Sign_up.this, "Email already exits", Toast.LENGTH_SHORT).show();
                            } else {
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(Sign_up.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                    String currentUserID=firebaseAuth.getCurrentUser().getUid();
                                                    rootRef.child("Budget").child("Users").child(currentUserID).setValue("");

                                                    rootRef.child("Dove Chat").child("Users").child(currentUserID).setValue("");


                                                    Toast.makeText(Sign_up.this, "registration completed", Toast.LENGTH_SHORT).show();
                                                    store();
                                                    mail.setText("");
                                                    pass.setText("");
                                                    conpass.setText("");
                                                } else {
                                                    Toast.makeText(Sign_up.this, "authentication error", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
        else {
            Toast.makeText(Sign_up.this, "password not match", Toast.LENGTH_SHORT).show();
        }
    }
    private void store(){
        SharedPreferences preferences=getSharedPreferences("com.example.completeandroiduserkit",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Loginstatus","on");
        editor.apply();
    }
}
