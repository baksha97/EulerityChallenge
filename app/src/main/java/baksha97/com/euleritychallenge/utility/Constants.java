package baksha97.com.euleritychallenge.utility;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Constants {

    public static class Codes {
        public static final int READ_WRITE_STORAGE_PERMISSION_CODE = 52;
    }

    public static class PathConstants {
        public static String getDesignatedEditedFilePath(Context context) {
            String path = context.getFilesDir().getPath().toString() + "/image.png";

            String sd_path = Environment.getExternalStorageDirectory()
                    + File.separator + ""
                    + "image" + ".png";
            String sd_randomPath = Environment.getExternalStorageDirectory() + File.separator + "" + System.currentTimeMillis() + ".png";

            return sd_path;
        }
    }

    public static class NetworkConstants {
        public static final String APP_ID = "baksha97@gmail.com";

        public static final String BASE_API_URL = "https://eulerity-hackathon.appspot.com";

        public static final String IMAGE_GET_REQUEST_URL = BASE_API_URL + "/image";
        public static final String UPLOAD_IMAGE_GET_REQUEST_URL = BASE_API_URL + "/upload";

        public static final String CHARSET = "UTF-8";
    }

}
