package com.denis.assignment3;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by denis on 4/20/15.
 */
public class RetainFragment extends Fragment {

    private static final String TAG = RetainFragment.class.getSimpleName();
    private ICallback callback;
    private DownloadAsyncTask downloadAsyncTask;

    public interface ICallback {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach");
        callback = (ICallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        callback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate " + this.hashCode());
        setRetainInstance(true);

        downloadAsyncTask = new DownloadAsyncTask();
//        downloadAsyncTask.execute()
    }

    private class DownloadAsyncTask extends AsyncTask<Uri, Integer, Bitmap>{
        @Override
        protected void onPreExecute() {
            if(callback != null){
                callback.onPreExecute();
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(callback != null) {
                callback.onPostExecute();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (callback != null) {
                callback.onProgressUpdate(values[0]);
            }
        }

        @Override
        protected void onCancelled() {
            if (callback != null) {
                callback.onCancelled();
            }
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {
            Log.d(TAG, "download started");
            return null;
        }
    }
}
