//package baksha97.com.euleritychallenge.data.network;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.android.volley.Request;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.google.gson.Gson;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//import baksha97.com.euleritychallenge.data.model.ImageItem;
//
//public class NetworkManager {
//
//
//    private static final String LOG_TAG = NetworkManager.class.getSimpleName();
//
//    private static final String IMAGE_GET_REQUEST_URL = "https://eulerity-hackathon.appspot.com/image";
//
//    private static final NetworkManager sInstance = new NetworkManager();
//
//    private NetworkManager() {
//    }
//
//    public static NetworkManager getInstance() {
//        return sInstance;
//    }
//
//
////    public ArrayList<ImageItem> getImageItemRequest(){
////        ArrayList<ImageItem> list = new ArrayList<>();
////
////        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, IMAGE_GET_REQUEST_URL, null, response -> {
////            try {
////
////                JSONArray jsonArray = response;//.getJSONArray(requestItemArray);
////
////                Gson gson = new Gson();
////
////                for (int i = 0; i < jsonArray.length(); i++) {
////                    JSONObject item = jsonArray.getJSONObject(i);
////
////                    ImageItem newItem = gson.fromJson(item.toString(), ImageItem.class);
////                    list.add(newItem);
////                }
////
////                Log.d(LOG_TAG, "");
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////        }, error -> error.printStackTrace());
////
////        VolleySingleton.getsInstan.addToRequestQueue(request);
////        Log.d(LOG_TAG, "");
////    }
//
//}
