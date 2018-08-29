package baksha97.com.euleritychallenge.data.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*
    Ensures that only one instance is created with this singleton.
    This allows for ONE RequestQueue for the entire lifecycle of the application.
 */

public class VolleySingleton {
    private static VolleySingleton sInstance;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized VolleySingleton getsInstance(Context context) {
        if (sInstance == null) sInstance = new VolleySingleton(context);

        return sInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    private RequestQueue getRequestQueue() {
        return requestQueue;
    }


}
