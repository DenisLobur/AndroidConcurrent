package com.denis.assignment3;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends Activity implements RetainFragment.ICallback, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String RETAIN_FRAGMENT = "retain_fragment";
    private ProgressBar progressBar;
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
        fragment = (RetainFragment)fm.findFragmentByTag(RETAIN_FRAGMENT);

        if (fragment == null) {
            fragment = new RetainFragment();
            fm.beginTransaction().add(fragment, RETAIN_FRAGMENT).commit();
        }

        progressBar = (ProgressBar)findViewById(R.id.progress);
        enterUrlEditText = (EditText)findViewById(R.id.enter_url);
        imageHolder = (ImageView)findViewById(R.id.image_holder);
        imageHolder.setScaleType(ImageView.ScaleType.CENTER_CROP);
        download = (Button)findViewById(R.id.download_btn);
        download.setOnClickListener(this);
        cancel = (Button)findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(this);
        filter = (Button)findViewById(R.id.filter_btn);
        filter.setOnClickListener(this);

        presenter = new Presenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_btn:
                Log.d(TAG, "Download click");
                    Uri uri = presenter.extractUrl(enterUrlEditText.getText().toString());
                    fragment.startAsyncDownload(uri);
                break;
            case R.id.cancel_btn:
                Log.d(TAG, "Cancel click");

                break;
            case R.id.filter_btn:
                Log.d(TAG, "Filter click");
                Uri uri2 = presenter.extractUrl(enterUrlEditText.getText().toString());
                fragment.startAsyncFilter(uri2);

                break;
        }
    }

    @Override
    public void onPreExecute() {
        Log.d(TAG, "onPreExecute");
    }

    @Override
    public void onProgressUpdate(int percent) {
        Log.d(TAG, "onProgressUpdate " + percent + "%");
        progressBar.setProgress(percent);
    }

    @Override
    public void onCancelled() {
        Log.d(TAG, "onCancelled");
    }

    @Override
    public void onPostExecute(Bitmap bitmap) {
        Log.d(TAG, "onPostExecute");
        imageHolder.setImageBitmap(bitmap);
    }


}
