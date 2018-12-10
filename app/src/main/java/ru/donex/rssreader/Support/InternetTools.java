package ru.donex.rssreader.Support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetTools {
    private Context thisContext;
    private String CLASS_NAME = "InternetTools";

    public InternetTools(Context context){
        thisContext = context;
    }

    // проверка подключения к сети
    public boolean isInternetAvailable() {

        try {
            ConnectivityManager cm =
                    (ConnectivityManager) thisContext
                            .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork.isConnectedOrConnecting();
        } catch (Exception otherException){

            Log.e(CLASS_NAME,"exception: " + otherException);
            return false;
        }
    }


}
