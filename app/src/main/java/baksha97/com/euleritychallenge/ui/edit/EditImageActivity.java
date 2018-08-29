package baksha97.com.euleritychallenge.ui.edit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import baksha97.com.euleritychallenge.imaging.EditingToolsAdapter;
import baksha97.com.euleritychallenge.imaging.FilterCycler;
import baksha97.com.euleritychallenge.imaging.ToolType;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.ViewType;

import static baksha97.com.euleritychallenge.ui.list.ListActivity.EXTRA_IMAGE_CHOSEN;

public class EditImageActivity extends AppCompatActivity implements OnPhotoEditorListener,
        View.OnClickListener, EditingToolsAdapter.OnItemSelected {

    private static final String LOG_TAG = EditImageActivity.class.getSimpleName();
    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private RecyclerView mRvTools;

    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        initViews();

        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                .build(); // build photo editor sdk
        mPhotoEditor.setOnPhotoEditorListener(this);

        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_IMAGE_CHOSEN);
        Glide.with(this).load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(mPhotoEditorView.getSource());
    }

    private void initViews() {
        ImageView imgUndo;
        ImageView imgRedo;
        FloatingActionButton uploadFab;

        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mRvTools = findViewById(R.id.rvConstraintTools);

        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

        uploadFab = findViewById(R.id.uploadFab);
        uploadFab.setOnClickListener(this);
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
            case R.id.uploadFab:
                showCompletionDialog();
                break;
        }
    }

    private void showCompletionDialog() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    displayImage();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you are ready to upload??").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void displayImage() {

        SaveSettings saveSettings = new SaveSettings.Builder()
                .setClearViewsEnabled(true)
                .setTransparencyEnabled(true)
                .build();

        mPhotoEditor.saveAsBitmap(new OnSaveBitmap() {
            @Override
            public void onBitmapReady(Bitmap saveBitmap) {
                mPhotoEditorView.getSource().setImageBitmap(saveBitmap);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
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
                break;
        }
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

