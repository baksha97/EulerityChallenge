package baksha97.com.euleritychallenge.ui.list;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import baksha97.com.euleritychallenge.R;
import baksha97.com.euleritychallenge.data.model.ImageItem;
import baksha97.com.euleritychallenge.data.network.VolleySingleton;
import baksha97.com.euleritychallenge.ui.edit.EditImageActivity;
import baksha97.com.euleritychallenge.utility.Constants;

public class ListActivity extends AppCompatActivity implements ImageItemAdapter.onItemClickListener {
    private static final String LOG_TAG = ListActivity.class.getSimpleName();

    public static final String EXTRA_IMAGE_CHOSEN_URL_KEY = "image_url";

    private RecyclerView recyclerView;
    private ImageItemAdapter adapter;

    private List<ImageItem> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //An empty adapter has not been set for the recycler view in the onCreate, only after HTTP Request
        //We can expect "E/RecyclerView: No adapter attached; skipping layout" in Logcat
        setupRecyclerView();
    }

    private void setupRecyclerView() {

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrieveDataFromGet();

    }

    private void retrieveDataFromGet() {
        String url = Constants.NetworkConstants.IMAGE_GET_REQUEST_URL;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {

                JSONArray jsonArray = response;
                list = new ArrayList<>();

                Gson gson = new Gson();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    ImageItem newItem = gson.fromJson(item.toString(), ImageItem.class);
                    list.add(newItem);
                }

                adapter = new ImageItemAdapter(list, ListActivity.this);
                adapter.setOnItemClickListener(ListActivity.this);
                recyclerView.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> error.printStackTrace());

        VolleySingleton.getsInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent editIntent = new Intent(this, EditImageActivity.class);
        ImageItem clickedItem = list.get(position);
        editIntent.putExtra(EXTRA_IMAGE_CHOSEN_URL_KEY, clickedItem.getUrl());

        startActivity(editIntent);
    }
}
