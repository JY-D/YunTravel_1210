package com.example.salah.myapplication;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DataShowActivity extends ActionBarActivity {

    TextView tvtest,tvtest2,tvtest3,resault;

    private ListView getSearchTitlesList;
    private JSONArray jsonArray;

    private ProgressDialog pDialog;

    String f_name ;
    String f_region ;
    String f_class ;
    String u_name;
    String u_region ;
    String u_class ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_datashow);

        Bundle bundle =this.getIntent().getExtras();

        ProgressDialog pDialog;

        f_name = bundle.getString("name");
        f_region = bundle.getString("region");
        f_class = bundle.getString("class");

        getSearchTitlesList = (ListView) findViewById(R.id.getSearchTitlesList);

        tvtest = (TextView)findViewById(R.id.textView18);
        tvtest2 = (TextView)findViewById(R.id.textView20);
        tvtest3 = (TextView)findViewById(R.id.textView22);
        resault = (TextView)findViewById(R.id.textView16);

        tvtest.setText(f_name);
        tvtest2.setText(f_region);
        tvtest3.setText(f_class);

        new GetAllSpotsTask_allspots().execute(new ApiConnector_allspots());                         //以下執行讀取資料庫
        this.getSearchTitlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {        //點取資料庫LIST
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try
                {
                    JSONObject spotClicked = jsonArray.getJSONObject(position);

                    Intent showDetails = new Intent(getApplicationContext(),DataShowActivity_spotDetail.class);
                    showDetails.putExtra("SpotID_public", spotClicked.getInt("id"));

                    startActivity(showDetails);
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    public void setListAdapter(JSONArray jsonArray)                                   //以下database list class
    {
        this.jsonArray = jsonArray;
        this.getSearchTitlesList.setAdapter(new GetAllSpotsListAdapter(jsonArray, this));
    }

    private class GetAllSpotsTask_allspots extends AsyncTask<ApiConnector_allspots, Long, JSONArray>
    {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DataShowActivity.this);
            pDialog.setMessage("載入資料...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(ApiConnector_allspots... params) {
            try {
                u_name = URLEncoder.encode(f_name, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                u_region = URLEncoder.encode(f_region, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                u_class = URLEncoder.encode(f_class, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return params[0].GetSearchDetails(u_name, u_region, u_class);

        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            //setTextToTextView(jsonArray);
            if(jsonArray == null)
            {
                resault.setText("查無資料");
                tvtest.setText(f_name);
                tvtest2.setText(f_region);
                tvtest3.setText(f_class);
            }
            else {
                tvtest.setText(f_name);
                tvtest2.setText(f_region);
                tvtest3.setText(f_class);
                setListAdapter(jsonArray);
            }

            pDialog.dismiss();
        }
    }
}
