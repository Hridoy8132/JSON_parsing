package com.hridoy.a267androidjsonparsingfromdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText edName, edPhone, edAddress, edEmail;
    Button buttonInsert;
    ListView listView;
    HashMap<String,String> hashMap;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edphone);
        edAddress = findViewById(R.id.edAddress);
        edEmail = findViewById(R.id.edEmail);
        buttonInsert = findViewById(R.id.buttonInsert);
        listView = findViewById(R.id.listView);
        loadData();





        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String phone = edPhone.getText().toString();
                String address = edAddress.getText().toString();
                String email = edEmail.getText().toString();
                String url = "https://hridoy5765bd.000webhostapp.com/apps/link.php?n=" + name + "&p="
                        + phone + "&a=" + address + "&e=" + email;


              progressBar.setVisibility(View.GONE);
              StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                  @Override
                  public void onResponse(String response) {
                      progressBar.setVisibility(View.GONE);
                      new AlertDialog.Builder(MainActivity.this)
                              .setTitle("Server Response")
                              .setMessage(response)
                              .show();
                      loadData();

                  }
              }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                      Log.d("serverRes",error.toString());

                  }

            });


                 RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);


            }
        });
    }



    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View myView = layoutInflater.inflate(R.layout.item,null);
            TextView tvId = myView.findViewById(R.id.tvId);
            TextView tvName = myView.findViewById(R.id.tvName);
            TextView tvPhone = myView.findViewById(R.id.tvPhone);
            TextView tvAddress = myView.findViewById(R.id.tvAddress);
            TextView tvEmail = myView.findViewById(R.id.tvEmail);

            hashMap = arrayList.get(position);
            String id = hashMap.get("Id");
            String name = hashMap.get("Name");
            String phone = hashMap.get("Phone");
            String address = hashMap.get("Address");
            String email = hashMap.get("Email");

            tvId.setText(id);
            tvName.setText(name);
            tvAddress.setText(address);
            tvPhone.setText(phone);
            tvEmail.setText(email);




            return myView;
        }
    }

    private void loadData(){

        arrayList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                "https://hridoy5765bd.000webhostapp.com/apps/stepwiseshowalldb.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String id = jsonObject.getString("Id");
                        String name = jsonObject.getString("Name");
                        String phone = jsonObject.getString("Phone");
                        String address = jsonObject.getString("Address");
                        String email = jsonObject.getString("Email");

                        hashMap = new HashMap<>();
                        hashMap.put("Id", id);
                        hashMap.put("Name", name);
                        hashMap.put("Phone", phone);
                        hashMap.put("Address", address);
                        hashMap.put("Email", email);
                        arrayList.add(hashMap);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if (arrayList.size() > 0) {
                    MyAdapter myadapter = new MyAdapter();
                    listView.setAdapter(myadapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);


    }
}