package com.example.completeandroiduserkit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TwitterMainActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBarweb;
    ProgressDialog progressDialog;
    private String webUrl="https://twitter.com/login?lang=en/";
    RelativeLayout relativeLayout;
    Button btnno;
    private Context mContext;


    private static final int REQUEST_STORAGE = 1;
    private static final int REQUEST_LOCATION = 2;
    public ValueCallback<Uri> mUploadMessage;
    public static final int FILECHOOSER_RESULTCODE = 5173;
    ValueCallback<Uri[]> mUMA;
    ValueCallback<Uri> mUM;
    String mCM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_twitter_main);

        webView = (WebView) findViewById(R.id.mywebview);
        progressBarweb = (ProgressBar) findViewById(R.id.progressbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait ...");
        btnno = (Button) findViewById(R.id.btnnoconnection);
        relativeLayout = (RelativeLayout) findViewById(R.id.reletivelayout);

        if (savedInstanceState!=null){
            webView.restoreState(savedInstanceState);
        }

        else {


            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setDomStorageEnabled(true);


            checkConnection();

        }



        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);

        webView.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath()+"/cache");
        webView.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath()+"/database");


        webView.requestFocus();
        webView.getSettings().setSavePassword(true);


        checkConnection();

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String s, final String s1, final String s2, final String s3, long l) {

                Dexter.withActivity(TwitterMainActivity.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {


                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s));
                                request.setMimeType(s3);
                                String cookies = CookieManager.getInstance().getCookie(s);
                                request.addRequestHeader("cookie", cookies);
                                request.addRequestHeader("User-Agent", s1);
                                request.setDescription("Downloading File.....");
                                request.setTitle(URLUtil.guessFileName(s, s2, s3));
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(
                                        Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                                s, s2, s3));
                                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);
                                Toast.makeText(TwitterMainActivity.this, "Downloading File..", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {


                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });


        webView.setWebChromeClient(new WebChromeClient() {


            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                this.openFileChooser(uploadMsg, "*/*");
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                this.openFileChooser(uploadMsg, acceptType, null);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                TwitterMainActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            public boolean onShowFileChooser(

                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {

                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(TwitterMainActivity.this.getPackageManager()) != null) {
                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Toast.makeText(TwitterMainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                return true;
            }




            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarweb.setVisibility(View.VISIBLE);
                progressBarweb.setProgress(newProgress);
                setTitle("Loading...");
                progressDialog.show();
                if (newProgress == 100) {
                    progressBarweb.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                    progressDialog.dismiss();
                }

                super.onProgressChanged(view, newProgress);
            }
        });

        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
//Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FILECHOOSER_RESULTCODE) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
//Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FILECHOOSER_RESULTCODE) {

                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    public class Callback extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), "Failed loading! try reloading", Toast.LENGTH_SHORT).show();
            webView.loadUrl("file:///android_asset/help.html");
        }
    }

    // Create an image file
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {

                            Intent intent=new Intent(TwitterMainActivity.this,MainActivity.class);
                            startActivity(intent);
            finish();
                        }
    }



    public void checkConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if(wifi.isConnected()){
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }

        else if(mobile.isConnected()){
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);

        }
        else {
            webView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.nav_previous:
                onBackPressed();
                break;

            case R.id.nav_next:
                if(webView.canGoForward()){
                    webView.goForward();
                }
                break;

            case R.id.nav_reload:
                checkConnection();
                break;

            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you Sure Want to Remove URL ? ")
                        .setNegativeButton("Cancel",null)
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences preferences =getSharedPreferences("URL",0);
                                SharedPreferences.Editor editor=preferences.edit();
                                String a="0";
                                editor.putString("url",a);
                                editor.apply();
                                Intent intent =new Intent(getApplicationContext(),UrlActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }).show();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

}
