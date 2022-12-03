package com.example.completeandroiduserkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText mail, pswd;
    private FirebaseAuth firebaseAuth;
    String email,password;
    CheckBox show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mail = (EditText) findViewById(R.id.mail);
        pswd = (EditText) findViewById(R.id.pwd);
        show=(CheckBox)findViewById(R.id.show);


        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    pswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();



    }

    public void login(View view) {
        email = mail.getText().toString().trim();
        password = pswd.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Login.this, "please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(Login.this, "password too short", Toast.LENGTH_SHORT).show();
        }

        else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                store();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                mail.setText("");
                                pswd.setText("");


                            } else {
                                Toast.makeText(Login.this, "user or password incorrect", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    }


    public void signup(View view) {
        startActivity(new Intent(getApplicationContext(),Sign_up.class));
        finish();
    }

    public void forget(View view) {
        showrecoverPasswordDialog();
    }


    private void showrecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        builder.setMessage("Mail will be sent to your Email to recover your password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailET = new EditText(this);
        emailET.setHint("Email");
        emailET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailET);
        linearLayout.setPadding(10, 10, 10, 10);
        emailET.setMinEms(16);
        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = emailET.getText().toString().trim();
                beginRecover(email);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        builder.create().show();

    }

    private void beginRecover(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Email Sent", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Login.this, "Sent Failed..", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void store(){
        SharedPreferences preferences=getSharedPreferences("com.example.completeandroiduserkit",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Loginstatus","on");
        editor.apply();
    }




}
