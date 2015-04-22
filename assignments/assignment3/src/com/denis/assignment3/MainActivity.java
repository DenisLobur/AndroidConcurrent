package com.denis.assignment3;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements RetainFragment.ICallback, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String RETAIN_FRAGMENT = "retain_fragment";
    private EditText enterUrlEditText;
    private ImageView imageHolder;
    private RetainFragment fragment;
    private Button download;
    private Button cancel;
    private Button filter;
    private Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate " + this.hashCode());
        setContentView(R.layout.main);

        FragmentManager fm = getFragmentManager();
        fragment = (RetainFragment) fm.findFragmentByTag(RETAIN_FRAGMENT);

        if (fragment == null) {
            fragment = new RetainFragment();
            fm.beginTransaction().add(fragment, RETAIN_FRAGMENT).commit();
        }

        enterUrlEditText = (EditText) findViewById(R.id.enter_url);
        imageHolder = (ImageView) findViewById(R.id.image_holder);
        imageHolder.setScaleType(ImageView.ScaleType.CENTER_CROP);
        download = (Button) findViewById(R.id.download_btn);
        download.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(this);
        filter = (Button) findViewById(R.id.filter_btn);
        filter.setOnClickListener(this);

        presenter = new Presenter(this);
        enableUI(true);
        cancel.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_btn:
                Log.d(TAG, "Download click");
                if(isInternetOn()) {
                    download.setEnabled(false);
                    Uri uri = presenter.extractUrl(enterUrlEditText.getText().toString());
                    fragment.startAsyncDownload(uri);
                } else {
                    Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel_btn:
                Log.d(TAG, "Cancel click");
                enterUrlEditText.setText("");
                imageHolder.setImageDrawable(null);
                v.setEnabled(false);
                break;
            case R.id.filter_btn:
                Log.d(TAG, "Filter click");
                if (isInternetOn()) {
                    filter.setEnabled(false);
                    Uri uri2 = presenter.extractUrl(enterUrlEditText.getText().toString());
                    fragment.startAsyncFilter(uri2);
                } else {
                    Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onPreExecute() {
        Log.d(TAG, "onPreExecute");
        enableUI(false);
        enableButtons(false, download, cancel, filter);
    }

    @Override
    public void onProgressUpdate(int percent) {
        Log.d(TAG, "onProgressUpdate " + percent + "%");
    }

    @Override
    public void onCancelled() {
        Log.d(TAG, "onCancelled");
    }

    @Override
    public void onPostExecute(Bitmap bitmap) {
        Log.d(TAG, "onPostExecute");
        imageHolder.setImageBitmap(bitmap);
        enableUI(true);
        enableButtons(true, download, cancel, filter);
    }

    public void enableUI(boolean enabled) {
        View loadingLayout = findViewById(R.id.ui_disabler);
        if (loadingLayout != null) {
            int visibility = enabled ? View.GONE : View.VISIBLE;
            loadingLayout.setVisibility(visibility);
        }
    }

    private void enableButtons(boolean isEnable, Button... buttons) {
        for (Button button : buttons) {
            button.setEnabled(isEnable);
        }
    }

    private boolean isInternetOn() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING ||
                cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ||
                cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTING) {
            return true;
        } else {
            return false;
        }
    }

}
