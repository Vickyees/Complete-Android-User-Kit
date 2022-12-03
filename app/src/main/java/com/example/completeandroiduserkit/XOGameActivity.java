package com.example.completeandroiduserkit;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class XOGameActivity extends AppCompatActivity {

    TextView p1,p2,ps1,ps2,bonus,target,turn,a1,a2,a3,a4,a5,a6,a7,a8,a9;
    String a;
    String b1,b2,b3,b4,b5,b6,b7,b8,b9;
    private static int count =9;
    public static int player1score=0;
    public static int player2score=0;
    public static int bonuss=0;
    int tt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_x_o_game);
        getSupportActionBar().hide();

        p1=(TextView)findViewById(R.id.p1);
        p2=(TextView)findViewById(R.id.p2);
        ps1=(TextView)findViewById(R.id.ps1);
        ps2=(TextView)findViewById(R.id.ps2);

        bonus=(TextView)findViewById(R.id.bonus);
        target=(TextView)findViewById(R.id.target);
        turn=(TextView)findViewById(R.id.turn);

        a1=(TextView)findViewById(R.id.a1);
        a2=(TextView)findViewById(R.id.a2);
        a3=(TextView)findViewById(R.id.a3);
        a4=(TextView)findViewById(R.id.a4);
        a5=(TextView)findViewById(R.id.a5);
        a6=(TextView)findViewById(R.id.a6);
        a7=(TextView)findViewById(R.id.a7);
        a8=(TextView)findViewById(R.id.a8);
        a9=(TextView)findViewById(R.id.a9);

        count=9;
        player1score=0;
        player2score=0;
        bonuss=0;

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            p1.setText(bundle.getString("p1"));
            p2.setText(bundle.getString("p2"));
            target.setText(bundle.getString("t"));
            turn.setText(bundle.getString("p1")+"'s  turn");
        }

        tt=Integer.parseInt(target.getText().toString());
        tuns();


    }

    public void a1(View view) {
        if (a1.getText().toString().equals("")){
            if (count<6){

                if (count%2==0){
                    a1.setText("o");
                    check();
                }
                else {
                    a1.setText("x");
                    check();
                }


            }

            else {
                if (count%2==0){
                    a1.setText("o");
                    count=count-1;
                    tuns();
                }
                else {
                    a1.setText("x");
                    count=count-1;
                    tuns();
                }
            }
        }
        else {
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Already Played");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }


    }
    public void a2(View view) {
        if (a2.getText().toString().equals("")){
            if (count<6){

                if (count%2==0){
                    a2.setText("o");
                    check();
                }
                else {
                    a2.setText("x");
                    check();
                }


            }
            else {
                if (count%2==0){
                    a2.setText("o");
                    count=count-1;
                    tuns();
                }
                else {
                    a2.setText("x");
                    count=count-1;
                    tuns();
                }
            }
        }
        else {
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Already Played");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }
    public void a3(View view) {
        if (a3.getText().toString().equals("")){
            if (count<6){

                if (count%2==0){
                    a3.setText("o");
                    check();
                }
                else {
                    a3.setText("x");
                    check();
                }


            }
            else {
                if (count%2==0){
                    a3.setText("o");
                    count=count-1;
                    tuns();
                }
                else {
                    a3.setText("x");
                    count=count-1;
                    tuns();
                }
            }
        }
        else {
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Already Played");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }


    }
    public void a4(View view) {
        if (a4.getText().toString().equals("")){
            if (count<6){

                if (count%2==0){
                    a4.setText("o");
                    check();
                }
                else {
                    a4.setText("x");
                    check();
                }


            }
            else {
                if (count%2==0){
                    a4.setText("o");
                    count=count-1;
                    tuns();
                }
                else {
                    a4.setText("x");
                    count=count-1;
                    tuns();
                }
            }
        }
        else {
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Already Played");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }
    public void a5(View view) {
        if (a5.getText().toString().equals("")){
            if (count<6){

                if (count%2==0){
                    a5.setText("o");
                    check();
                }
                else {
                    a5.setText("x");
                    check();
                }


            }
            else {
                if (count%2==0){
                    a5.setText("o");
                    count=count-1;
                    tuns();
                }
                else {
                    a5.setText("x");
                    count=count-1;
                    tuns();
                }
            }
        }
        else {
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Already Played");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }
    public void a6(View view) {
        if (a6.getText().toString().equals("")){
            if (count<6){

                if (count%2==0){
                    a6.setText("o");
                    check();
                }
                else {
                    a6.setText("x");
                    check();
                }


            }
            else {
                if (count%2==0){
                    a6.setText("o");
                    count=count-1;
                    tuns();
                }
                else {
                    a6.setText("x");
                    count=count-1;
                    tuns();
                }
            }
        }
        else {
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Already Played");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }
    public void a7(View view) {
        if (a7.getText().toString().equals("")){
            if (count<6){

                if (count%2==0){
                    a7.setText("o");
                    check();
                }
                else {
                    a7.setText("x");
                    check();
                }


            }
            else {
                if (count%2==0){
                    a7.setText("o");
                    count=count-1;
                    tuns();
                }
                else {
                    a7.setText("x");
                    count=count-1;
                    tuns();
                }
            }
        }
        else {
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Already Played");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }
    public void a8(View view) {
        if (a8.getText().toString().equals("")){
            if (count<6){

                if (count%2==0){
                    a8.setText("o");
                    check();
                }
                else {
                    a8.setText("x");
                    check();
                }


            }
            else {
                if (count%2==0){
                    a8.setText("o");
                    count=count-1;
                    tuns();
                }
                else {
                    a8.setText("x");
                    count=count-1;
                    tuns();
                }
            }
        }
        else {
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Already Played");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }
    public void a9(View view) {
        if (a9.getText().toString().equals("")){
            if (count<6){

                if (count%2==0){
                    a9.setText("o");
                    check();
                }
                else {
                    a9.setText("x");
                    check();
                }


            }
            else {
                if (count%2==0){
                    a9.setText("o");
                    count=count-1;
                    tuns();
                }
                else {
                    a9.setText("x");
                    count=count-1;
                    tuns();
                }
            }
        }
        else {
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Already Played");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }


    public void button(){
        b1=a1.getText().toString().toLowerCase();
        b2=a2.getText().toString().toLowerCase();
        b3=a3.getText().toString().toLowerCase();
        b4=a4.getText().toString().toLowerCase();
        b5=a5.getText().toString().toLowerCase();
        b6=a6.getText().toString().toLowerCase();
        b7=a7.getText().toString().toLowerCase();
        b8=a8.getText().toString().toLowerCase();
        b9=a9.getText().toString().toLowerCase();
    }


    public void check(){
        button();
        if (count%2==0){

            if((b1.equals("o")&&b2.equals("o")&&b3.equals("o"))||(b4.equals("o")&&b5.equals("o")&&b6.equals("o"))||(b7.equals("o")&&b8.equals("o")&&b9.equals("o") )||(b1.equals("o")&&b4.equals("o")&&b7.equals("o"))||(b2.equals("o")&&b5.equals("o")&&b8.equals("o"))||(b3.equals("o")&&b6.equals("o")&&b9.equals("o"))|| (b1.equals("o")&&b5.equals("o")&&b9.equals("o"))||(b3.equals("o")&&b5.equals("o")&&b7.equals("o"))){


                player2score=player2score+1;
                player1score=player1score-1;
                if (bonuss>0){

                    player2score =player2score+bonuss;
                    bonuss=0;
                    bonus.setText(String.valueOf(bonuss));
                    ps2.setText("Score  : "+String.valueOf(player2score));
                }
                else {
                    ps2.setText("Score   : "+String.valueOf(player2score));
                }
                ps1.setText("Score   : "+String.valueOf(player1score));


                if (tt<=player2score){
                    final AlertDialog.Builder alert =new AlertDialog.Builder(XOGameActivity.this);
                    View mView =getLayoutInflater().inflate(R.layout.win,null);

                    final Button ok =(Button)mView.findViewById(R.id.winbutton);
                    final TextView name=(TextView)mView.findViewById(R.id.winname);

                    alert.setView(mView);
                    final AlertDialog alertDialog=alert.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);

                    name.setText(p2.getText().toString()+"  won");

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            count=9;
                            bonuss=0;
                            startActivity(new Intent(XOGameActivity.this,MainActivity.class));
                            finish();
                        }
                    });

                    alertDialog.show();
                }

                else {

                    empty();
                    count = 9;
                    tuns();
                }



            }

            else {

                String c=String.valueOf(count);
                if (c.equals("1")){
                    LayoutInflater inflater=getLayoutInflater();
                    View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
                    TextView toastText=layout.findViewById(R.id.text);
                    toastText.setText("Bonus Point");
                    Toast toast=new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM,0,0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();


                    bonuss=bonuss+1;
                    bonus.setText(String.valueOf(bonuss));
                    empty();
                    count = 9;
                    tuns();

                }
                else {
                    count = count - 1;
                    tuns();
                }

            }

        }
        else {
            if((b1.equals("x")&&b2.equals("x")&&b3.equals("x"))||(b4.equals("x")&&b5.equals("x")&&b6.equals("x"))||(b7.equals("x")&&b8.equals("x")&&b9.equals("x") )||(b1.equals("x")&&b4.equals("x")&&b7.equals("x"))||(b2.equals("x")&&b5.equals("x")&&b8.equals("x"))||(b3.equals("x")&&b6.equals("x")&&b9.equals("x"))|| (b1.equals("x")&&b5.equals("x")&&b9.equals("x"))||(b3.equals("x")&&b5.equals("x")&&b7.equals("x"))){

                player1score=player1score+1;
                player2score=player2score-1;

                if (bonuss>0){

                    player1score =player1score+bonuss;
                    bonuss=0;
                    bonus.setText(String.valueOf(bonuss));
                    ps1.setText("Score  : "+String.valueOf(player1score));
                }
                else {
                    ps1.setText("Score   : "+String.valueOf(player1score));
                }
                ps2.setText("Score   : "+String.valueOf(player2score));
                if (tt<=player1score){

                    final AlertDialog.Builder alert =new AlertDialog.Builder(XOGameActivity.this);
                    View mView =getLayoutInflater().inflate(R.layout.win,null);

                    final Button ok =(Button)mView.findViewById(R.id.winbutton);
                    final TextView name=(TextView)mView.findViewById(R.id.winname);

                    alert.setView(mView);
                    final AlertDialog alertDialog=alert.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);

                    name.setText(p1.getText().toString()+"  won");

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            count=9;
                            bonuss=0;
                            startActivity(new Intent(XOGameActivity.this,MainActivity.class));
                            finish();
                        }
                    });
                    alertDialog.show();
                }

                else {
                    empty();
                    count = 9;
                    tuns();
                }

            }


            else {

                String c=String.valueOf(count);
                if (c.equals("1")){
                    LayoutInflater inflater=getLayoutInflater();
                    View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
                    TextView toastText=layout.findViewById(R.id.text);
                    toastText.setText("Bonus Point");
                    Toast toast=new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM,0,0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();


                    bonuss=bonuss+1;
                    bonus.setText(String.valueOf(bonuss));
                    count = 9;
                    tuns();
                    empty();

                }
                else {
                    count = count - 1;
                    tuns();
                }

            }

        }
    }



    public void empty(){
        a1.setText("");
        a2.setText("");
        a3.setText("");
        a3.setText("");
        a4.setText("");
        a5.setText("");
        a6.setText("");
        a7.setText("");
        a8.setText("");
        a9.setText("");

    }

    public void tuns(){
        if (count%2==0){
            turn.setText(p2.getText().toString()+"'s turn");
        }
        else {
            turn.setText(p1.getText().toString()+"'s turn");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        empty();
        count=0;
        player1score=0;
        player2score=0;
        bonuss=0;
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alert =new AlertDialog.Builder(XOGameActivity.this);
        View mView =getLayoutInflater().inflate(R.layout.leave,null);

        final Button yes =(Button)mView.findViewById(R.id.yes);
        final Button no =(Button)mView.findViewById(R.id.no);

        alert.setView(mView);
        final AlertDialog alertDialog=alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=9;
                bonuss=0;
                startActivity(new Intent(XOGameActivity.this,XOMainActivty.class));
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

}
