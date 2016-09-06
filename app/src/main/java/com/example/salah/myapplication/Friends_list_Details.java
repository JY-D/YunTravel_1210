package com.example.salah.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EB209 on 2015/11/24.
 */
public class Friends_list_Details extends ActionBarActivity {

    private GetAllSpotsListAdapter spotItemAdapter;
    ListView getAllTitlesList;
    private JSONArray jsonArray;
    protected JSONObject spotClicked_d;
    protected JSONObject spotClicked;
    protected JSONObject spotClicked_allspots;
    String friend;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle =this.getIntent().getExtras();
        friend = bundle.getString("friend");
        setContentView(R.layout.firends_list);

        getAllTitlesList = (ListView) findViewById(R.id.friends_list);

        new GetAllCustomerTask().execute(new ApiConnector_friend());


        this.getAllTitlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {        //點取資料庫LIST
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spotClicked = jsonArray.getJSONObject(position);

                    Intent showDetails = new Intent(getApplicationContext(), SpotDetailsActivity_friend.class);
                    showDetails.putExtra("SpotID", spotClicked.getInt("id"));
                    Bundle bundle = new Bundle();
                    bundle.putString("friend", friend);
                    showDetails.putExtras(bundle);
                    startActivity(showDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), "SpotId: " + spotClicked + " click",Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "SpotId: " + position + " click",Toast.LENGTH_LONG).show();
            }
        });

    }

    private class GetAllCustomerTask extends AsyncTask<ApiConnector_friend, Long, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector_friend... params) {
            return params[0].GetAllCustomers(friend);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            //setTextToTextView(jsonArray);
            if(jsonArray == null)
            {

            }
            else {
                setListAdapter(jsonArray);
            }
        }
    }

    public void setListAdapter(JSONArray jsonArray)                                   //以下database list class
    {
        this.jsonArray = jsonArray;
        spotItemAdapter = new GetAllSpotsListAdapter (jsonArray, this);
        this.getAllTitlesList.setAdapter(spotItemAdapter);
    }
}
