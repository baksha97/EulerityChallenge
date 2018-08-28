package baksha97.com.euleritychallenge.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import baksha97.com.euleritychallenge.R;
import baksha97.com.euleritychallenge.ui.edit.EditImageActivity;
import baksha97.com.euleritychallenge.ui.list.ListActivity;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        move();
    }

    private void move() {
        startActivity(new Intent(MainActivity.this, ListActivity.class));
    }
}
