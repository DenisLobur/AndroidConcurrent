package com.denis.assignment3;

import android.content.Context;
import android.net.Uri;

/**
 * Created by denis on 4/21/15.
 */
public class Presenter {

    private Context mContext;
    private Uri mDefaultUrl = Uri.parse("http://www.dre.vanderbilt.edu/~schmidt/robot.png");

    public Presenter(Context context) {
        mContext = context;
    }

    public Uri extractUrl(String extractedUrl) {
        Uri url = null;

        // Get the text the user typed in the edit text (if anything).
        url = Uri.parse(extractedUrl);

        // If the user didn't provide a URL then use the default.
        String uri = url.toString();
        if (uri == null || uri.equals(""))
            url = mDefaultUrl;

        // Do a sanity check to ensure the URL is valid, popping up a
        // toast if the URL is invalid.
        // @@ TODO -- you fill in here, replacing "true" with the
        // proper code.
        if ((url.toString().startsWith("http://") || url.toString().startsWith("https://")) && url.toString().endsWith(".png"))
            return url;
        else {
            Utils.showToast(mContext, "Invalid URL");
            return null;
        }
    }
}
