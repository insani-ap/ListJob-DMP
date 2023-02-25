package com.insani.listjobtest.util.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class NetworkHelper {
    private NetworkHelper() {
        throw new IllegalStateException("NetworkService class");
    }

    public static boolean getConnectivityStatus(Context mContext) {
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    result = true;
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                    result = true;
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN))
                    result = true;
            }
        }

        return result;
    }
}
