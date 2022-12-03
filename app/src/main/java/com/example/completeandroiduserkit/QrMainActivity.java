package com.example.completeandroiduserkit;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class QrMainActivity extends AppCompatActivity {

    public static TextView resultTextView;
    ImageView scan_btn;
    ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_main);
        setTitle("QR / Bar Code  Scanner");

        resultTextView = (TextView)findViewById(R.id.result_text);
        scan_btn = (ImageView) findViewById(R.id.btn_scan);

        clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionListener permissionListener=new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        startActivity(new Intent(getApplicationContext(),ScanCodeActivity.class));
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {

                        Toast.makeText(QrMainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                };

                TedPermission.with(QrMainActivity.this)
                        .setPermissionListener(permissionListener)
                        .setPermissions(Manifest.permission.CAMERA)
                        .check();

            }
        });
    }

    public void copyToClipBoard(View view) {

        String a=resultTextView.getText().toString().trim();

        if (!a.isEmpty()) {
            ClipData clipData = ClipData.newPlainText("text",a);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
        }

    }
}
