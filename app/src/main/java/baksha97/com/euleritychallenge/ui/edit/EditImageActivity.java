package baksha97.com.euleritychallenge.ui.edit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import baksha97.com.euleritychallenge.R;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class EditImageActivity extends AppCompatActivity {

    private PhotoEditorView mPhotoEditorView;
    private PhotoEditor mPhotoEditor;

    private RecyclerView mRvTools, mRvFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        mPhotoEditorView = findViewById(R.id.photoEditorView);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .build();

        mPhotoEditor.addText("hel9333333lo", 1);

        //  mRvTools = findViewById(R.id.rvConstraintTools);
    }
}

