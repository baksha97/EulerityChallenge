package baksha97.com.euleritychallenge.ui.edit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import baksha97.com.euleritychallenge.R;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

import static baksha97.com.euleritychallenge.ui.list.ListActivity.EXTRA_IMAGE_CHOSEN_URL_KEY;

public class EditImageActivity extends AppCompatActivity {

    private PhotoEditorView mPhotoEditorView;
    private PhotoEditor mPhotoEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        setup();
    }

    private void setup() {
        setupIntent();
        setupEditor();
    }

    private void setupIntent() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_IMAGE_CHOSEN_URL_KEY);
        Glide.with(this).load(url)
                .apply(new RequestOptions()
                        .centerInside()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(mPhotoEditorView.getSource());
        System.out.println(url);
    }

    private void setupEditor() {
        mPhotoEditorView = findViewById(R.id.photoEditorView);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .build();

        mPhotoEditor.addText("hel9333333lo", 1);
    }
}

