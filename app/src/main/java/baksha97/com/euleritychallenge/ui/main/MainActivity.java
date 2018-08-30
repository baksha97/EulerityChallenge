package baksha97.com.euleritychallenge.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import baksha97.com.euleritychallenge.R;
import baksha97.com.euleritychallenge.ui.list.ListActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

    }

    private void initViews() {
        textView = findViewById(R.id.textView);
        textView.setOnClickListener(this);
        textView.setTextSize(45);

        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        button.setTextColor(ContextCompat.getColor(this, R.color.white));
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void goToListActivity() {
        startActivity(new Intent(MainActivity.this, ListActivity.class));
    }

    @Override
    public void onClick(View v) {
        Log.d(LOG_TAG, "View has been touched");
        switch (v.getId()) {
            case R.id.button:
                Log.d(LOG_TAG, "MAIN -> LIST");
                goToListActivity();
            default:
                System.out.println();
        }
    }
}
