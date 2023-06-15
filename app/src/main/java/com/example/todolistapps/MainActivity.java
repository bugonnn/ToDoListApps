package com.example.todolistapps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<ToDoItem> todoList = new ArrayList<>();
    private RecyclerView rv;
    private ToDoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rvToDoList);

        todoAdapter = new ToDoAdapter(MainActivity.this.todoList);
        fetchData();
        rv.setAdapter(todoAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchData() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if (isConnected) {
            // Show loading state
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Sedang Mengambil Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Fetch data in the background thread
                    try {
                        //Simulate delay, so it will show loading state
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Update UI after data is loaded
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            String url = "https://mgm.ub.ac.id/todo.php"; // Data to fetch

                            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Try to parse JSON
                                            try {
                                                JSONArray jsonArray = new JSONArray(response);
                                                Log.d("json", jsonArray.toString());
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                    int id = jsonObject.getInt("id");
                                                    String what = jsonObject.getString("what");
                                                    String time = jsonObject.getString("time");
                                                    ToDoItem item = new ToDoItem(id, what, time);
                                                    todoList.add(item);
                                                }

                                                // Update RecyclerView
                                                todoAdapter.notifyDataSetChanged();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }
                                    });

                            queue.add(stringRequest);
                        }
                    });
                }
            }).start();
        } else {
            // Show AlertDialog for no internet connection
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Tidak Ada Koneksi Internet")
                    .setMessage("Tidak ada koneksi internet. Silakan periksa koneksi internet Anda!")
                    .setCancelable(false)
                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fetchData();
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }
}