package baksha97.com.euleritychallenge.data.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.io.File;
import java.util.List;

import baksha97.com.euleritychallenge.utility.Constants;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.SaveSettings;

public class ImageUploader {
    private static final String LOG_TAG = ImageUploader.class.getSimpleName();

    private static final ImageUploader sInstance = new ImageUploader();

    private ImageUploader() {
    }

    public static ImageUploader getInstance() {
        return sInstance;
    }

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

    @SuppressLint("MissingPermission")
    public void uploadSelectedImage(Context context, PhotoEditor photoEditor, String originalImageUrl) {

        String appId = Constants.NetworkConstants.APP_ID;
        //TODO: Implement Async Tasks
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Thread thread = new Thread(() -> {
            String filePath = Constants.PathConstants.getDesignatedEditedFilePath(context);
            String charset = Constants.NetworkConstants.CHARSET;
            SaveSettings saveSettings = new SaveSettings.Builder()
                    .setClearViewsEnabled(true)
                    .setTransparencyEnabled(true)
                    .build();

            getUploadUrl(context, requestUrl -> {
                try {
                    photoEditor.saveAsFile(filePath, saveSettings, new PhotoEditor.OnSaveListener() {
                        @Override
                        public void onSuccess(@NonNull String imagePath) {
                            System.out.println(imagePath);

                            try {
                                MultipartUtility multipart = new MultipartUtility(requestUrl, charset);
                                File file = new File(imagePath);
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
                        }

                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e(LOG_TAG, "save file error");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        thread.start();
    }


    public interface OnUrlResponse {
        void onResponse(String requestUrl);
    }


}
