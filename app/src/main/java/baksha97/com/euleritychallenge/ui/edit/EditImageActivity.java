package baksha97.com.euleritychallenge.ui.edit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import baksha97.com.euleritychallenge.R;
import baksha97.com.euleritychallenge.data.network.ImageUploader;
import baksha97.com.euleritychallenge.imaging.EditingToolsAdapter;
import baksha97.com.euleritychallenge.imaging.FilterCycler;
import baksha97.com.euleritychallenge.imaging.ToolType;
import baksha97.com.euleritychallenge.utility.Constants;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.ViewType;

import static baksha97.com.euleritychallenge.ui.list.ListActivity.EXTRA_IMAGE_CHOSEN;
import static baksha97.com.euleritychallenge.utility.Constants.Codes.READ_WRITE_STORAGE_PERMISSION_CODE;

/*
    Library used to edit the photos is located here: https://github.com/burhanrashid52/PhotoEditor
 */
public class EditImageActivity extends AppCompatActivity implements OnPhotoEditorListener,
        View.OnClickListener, EditingToolsAdapter.OnItemSelected {

    private static final String LOG_TAG = EditImageActivity.class.getSimpleName();
    private static String ORIGINAL_IMAGE_URL;
    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private RecyclerView mRvTools;

    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        initViews();

        //Optional LayoutManager that can be used to reorganize the recycled views.
        //LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //mRvTools.setLayoutManager(llmTools);

        mRvTools.setAdapter(mEditingToolsAdapter);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                .build(); // build photo editor sdk
        mPhotoEditor.setOnPhotoEditorListener(this);

        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_IMAGE_CHOSEN);
        ORIGINAL_IMAGE_URL = url;
        Glide.with(this).load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(mPhotoEditorView.getSource());
    }


    private void initViews() {
        ImageView imgUndo;
        ImageView imgRedo;

        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mRvTools = findViewById(R.id.rvConstraintTools);

        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;
            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;
        }
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case DRAW:
                Log.d(LOG_TAG, "SELECTED: DRAW");
                mPhotoEditor.setBrushDrawingMode(true);
                break;
            case TEXT:
                Log.d(LOG_TAG, "SELECTED: TEXT");
                askForText();
                break;
            case ERASER:
                Log.d(LOG_TAG, "SELECTED: ERASER");
                mPhotoEditor.brushEraser();
                break;
            case FILTER:
                Log.d(LOG_TAG, "SELECTED: FILTER");
                mPhotoEditor.setFilterEffect(FilterCycler.getsInstance().nextFilter());
                Toast.makeText(this, "Filters stack upon load! Stack your edits!", Toast.LENGTH_LONG).show();
                break;
            case UPLOAD:
                Log.d(LOG_TAG, "SELECTED: SAVE");
                promptWithLatestPhoto();
        }
    }

    public boolean requestPermission(String permission) {
        boolean isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (!isGranted) {
            Log.d(LOG_TAG, "Asking permission for Read Write");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    READ_WRITE_STORAGE_PERMISSION_CODE);
        }
        return isGranted;
    }

    private AlertDialog.Builder photoAlert(Bitmap fileImageBit) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Your image so far:");
        alert.setMessage("Are you sure you'd like to upload this image?...");
        // Set an EditText view to get user input
        final ImageView imageView = new ImageView(this);
        alert.setView(imageView);
        imageView.setImageBitmap(fileImageBit);
        alert.setPositiveButton("Go", (dialog, whichButton) -> {
            Log.d(LOG_TAG, "Latest image selected for upload: ");
            uploadLatestImage();
            return;
        });

        return alert;
    }

    //TODO: FIX - Quickly tapping for prompt will cause multiple popups to occur.
    private synchronized void promptWithLatestPhoto() {
        saveImageToFile(() -> {
            //Put image into view upon saving ...
            Bitmap fileImageBit = BitmapFactory.decodeFile(Constants.PathConstants.getDesignatedEditedFilePath(this));
            photoAlert(fileImageBit).show();

        });
    }


    //TODO: Add into AsyncTask
    @SuppressLint("MissingPermission")
    private void saveImageToFile(ImageUploader.OnImageSaveComplete action) {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            String filePath = Constants.PathConstants.getDesignatedEditedFilePath(this);

            mPhotoEditor.saveAsFile(filePath, new PhotoEditor.OnSaveListener() {
                @Override
                public void onSuccess(@NonNull String imagePath) {
                    action.onSave();
                }

                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e(LOG_TAG, "save file error");
                }
            });
        }
    }

    private void uploadLatestImage() {
        ImageUploader.getInstance().uploadLatestSavedImage(this, ORIGINAL_IMAGE_URL);
    }


    private void askForText() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Input text:");
        alert.setMessage("Your text will be entered into the photo...");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Go", (dialog, whichButton) -> {
            String value = input.getText().toString();
            Log.d(LOG_TAG, "Value Entered : " + value);
            mPhotoEditor.addText(value, R.color.black);
            return;
        });
        alert.show();
    }


    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to exit without saving image ?");
        builder.setPositiveButton("Yes", (dialog, which) -> finish());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if (!mPhotoEditor.isCacheEmpty()) {
            showExitDialog();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(LOG_TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        Log.d(LOG_TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(LOG_TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(LOG_TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(LOG_TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }


}

