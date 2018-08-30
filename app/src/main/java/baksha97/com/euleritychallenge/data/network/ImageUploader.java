package baksha97.com.euleritychallenge.data.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.io.File;
import java.util.List;

import baksha97.com.euleritychallenge.utility.Constants;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.SaveSettings;


/*
    We will try to clean up the EditImageActivity with this Singleton
    This Singleton will contain the code for uploading the latest image that was saved in our Activity.
    The latest image is always stored in one location as denoted by the {@link Constants.java}
 */

public class ImageUploader {
    private static final String LOG_TAG = ImageUploader.class.getSimpleName();

    private static final ImageUploader sInstance = new ImageUploader();

    /**
     * This constructor privately initializes for a single instance.
     */
    private ImageUploader() {
    }

    public static ImageUploader getInstance() {
        return sInstance;
    }


    //Simply retrieve an available upload URL
    private void getUploadUrl(Context context, OnUrlResponse aResponse) {
        String getRequestUrl = Constants.NetworkConstants.UPLOAD_IMAGE_GET_REQUEST_URL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestUrl, null, response -> {
            try {
                String url = response.getString("url");
                System.out.println(url);
                aResponse.onResponse(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> error.printStackTrace());
        VolleySingleton.getsInstance(context).addToRequestQueue(request);
    }


    //We can supress this warning because the Activity will ask for permission prior to this call.
    @SuppressLint("MissingPermission")
    public void uploadLatestSavedImage(Context context, String originalImageUrl) {

        String appId = Constants.NetworkConstants.APP_ID;

        //TODO: Implement Async/Rx Tasks to improve reliability and adhere to Android coding conventions.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Attempt to run upload off of the main thread
        Thread thread = new Thread(() -> {
            String filePath = Constants.PathConstants.getDesignatedEditedFilePath(context);
            String charset = Constants.NetworkConstants.CHARSET;
            SaveSettings saveSettings = new SaveSettings.Builder()
                    .setClearViewsEnabled(true)
                    .setTransparencyEnabled(true)
                    .build();

            ImageUploader.this.getUploadUrl(context, requestUrl -> {

                try {
                    //Utility class posted online
                    MultipartUtility multipart = new MultipartUtility(requestUrl, charset);
                    File file = new File(filePath);
                    file.createNewFile();

                    multipart.addFormField("appid", appId);
                    multipart.addFormField("original", originalImageUrl);
                    multipart.addFilePart("file", file);

                    List<String> response = multipart.finish();

                    System.out.println("SERVER REPLIED:");

                    for (String line : response) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        });

        thread.start();
    }

    /**
     * Below are a set of interfaces that will be used for adding runnable method on completion of a task.
     * It may be best suited to use a higher level library such as RxJava to more appropriately access API endpoints.
     */
    public interface OnUrlResponse {
        void onResponse(String requestUrl);
    }


    public interface OnImageSaveComplete {
        void onSave();
    }

}

