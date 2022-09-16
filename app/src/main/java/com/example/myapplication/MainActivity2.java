package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adepter.DistrictViewAdepter;
import com.example.myapplication.utils.Districtpojo;
import com.example.myapplication.utils.WebServiceHandler;
import com.example.myapplication.utils.WebServiceListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity2 extends AppCompatActivity {
    private Button getBtn;
    private TextView result;
    private OkHttpClient client;
    private ArrayList<Districtpojo> arraydistrict = new ArrayList<>();
    RecyclerView recyclerView;
    DistrictViewAdepter districtadepter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.rv_districtlist);

        result = (TextView) findViewById(R.id.result);
        getBtn = (Button) findViewById(R.id.getBtn);

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebservice();
            }
        });
        client = new OkHttpClient();
    }
    private void getWebservice() {
        WebServiceHandler webServiceHandler = new WebServiceHandler(MainActivity2.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) throws JSONException {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONArray jsonArray = jsonObject1.getJSONArray("Data");
                for(int i = 0; i<jsonArray.length();i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    String District = jsonObject2.getString("District");
                    String districtid = jsonObject2.getString("ID");
                    Districtpojo districtpojo = new Districtpojo();
                    districtpojo.setDistrictname(District);
                    districtpojo.setDistrictID(districtid);
                    arraydistrict.add(districtpojo);
                    // Log.e("respss",District);

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setHasFixedSize(true);
                        StaggeredGridLayoutManager gridLayoutManager =
                                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        districtadepter = new DistrictViewAdepter(getApplicationContext(),arraydistrict);
                        recyclerView.setAdapter(districtadepter);
                        districtadepter.myClickListeners = new DistrictViewAdepter.MyClickListeners() {
                            @Override
                            public void onItemClick(int position) {
                                Toast.makeText(getApplicationContext(),arraydistrict.get(position).getDistrictname(),Toast.LENGTH_LONG).show();
                            }
                        };
                    }
                });
               // result.setText(response);


            }

            @Override
            public void onFailer(String failure) throws JSONException {

            }
        };
        webServiceHandler.getMasterdata("56");
    }

}