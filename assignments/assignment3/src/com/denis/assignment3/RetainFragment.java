package com.denis.assignment3;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

/**
 * Created by denis on 4/20/15.
 */
public class RetainFragment extends Fragment {

    private static final String TAG = RetainFragment.class.getSimpleName();
    private ICallback callback;
    private DownloadAsyncTask downloadAsyncTask;
    private FilterAsyncTask filterAsyncTask;

    public interface ICallback {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute(Bitmap bitmap);

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
        filterAsyncTask = new FilterAsyncTask();
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
                callback.onPostExecute(bitmap);
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
            Uri uriToFile = Utils.downloadImage(getActivity(), params[0]);
            String pathToFile = "file://" + uriToFile.toString();
            Bitmap bitmap = null;
            try {
               bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(pathToFile));
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }

            //publishProgress(bitmap.si);
            return bitmap;
        }
    }

    private class FilterAsyncTask extends AsyncTask<Uri, Integer, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(callback != null) {
                callback.onPostExecute(bitmap);
            }
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {
            Log.d(TAG, "filtering started");
            Uri uriToFile = Utils.downloadImage(getActivity(), params[0]);
            String pathToFile = "file://" + uriToFile.toString();
            Uri uri = Utils.grayScaleFilter(getActivity(), Uri.parse(pathToFile));
            pathToFile = "file://" + uri.toString();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(pathToFile));
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
            return bitmap;
        }

    }

    public void startAsyncDownload(Uri uri){
        downloadAsyncTask.execute(uri);
    }

    public void startAsyncFilter(Uri uri) {
        filterAsyncTask.execute(uri);
    }
}
