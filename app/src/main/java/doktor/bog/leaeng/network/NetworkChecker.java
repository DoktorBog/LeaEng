package doktor.bog.leaeng.network;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkChecker {
    private final String LOG = getClass().getSimpleName();

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

}
