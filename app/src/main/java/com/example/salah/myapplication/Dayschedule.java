package com.example.salah.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by salah on 2015/9/10.
 */
public class Dayschedule extends ActionBarActivity {

    private SharedPreferences userData;
    private static final String data = "DATA";
    private static final String nameField = "123";
    private static final String passwordField = "456";
    private static final String login_per ="";
    String login_name="";

    private GetAllScheludeListAdapter schelrdeItemAdapter;
    protected JSONObject scheduleClicked;
    private JSONArray jsonArray;
    private ListView listView;
    Button addNews;
    String new_schedule,new_schedule_name,name="";
    DatePickerDialog.OnDateSetListener dPL;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dayschedule);

        addNews =(Button)findViewById(R.id.addsNewbutton);
        listView = (ListView)findViewById(R.id.listView);
        readData();
        new GetAllScheduleTask().execute(new ApiConnector());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                try {
                    scheduleClicked = jsonArray.getJSONObject(position);
                    new_schedule = scheduleClicked.getString("name");
                    Intent intent = new Intent();
                    intent.setClass(Dayschedule.this, Dayschedule_Details.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("new_schedule", new_schedule);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        addNews.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(Dayschedule.this);
                v = inflater.inflate(R.layout.main_dayschedule_addnewname, null);
                final View V = v;
                new AlertDialog.Builder(Dayschedule.this)
                        .setTitle("創立新專案")
                        .setMessage("請輸入你的專案名稱：")
                        .setIcon(R.mipmap.logo)
                        .setView(v)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) (V.findViewById(R.id.edittext));
                                name = editText.getText().toString();
                                new AlertDialog.Builder(Dayschedule.this)
                                        .setTitle("選擇清單")
                                        .setMessage("請選擇安排行程用景點清單")
                                        .setIcon(R.mipmap.logo)
                                        .setPositiveButton("大眾清單", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setClass(Dayschedule.this, Spot_ListMenu.class);
                                                Bundle bundle = new Bundle();
                                                if(name.equals(""))
                                                    bundle.putString("Add_new_schedule", "新的專案唷");
                                                else
                                                    bundle.putString("Add_new_schedule",  name);
                                                bundle.putString("search_list", "大眾清單");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();

                                            }
                                        })
                                        .setNegativeButton("自訂清單",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO Auto-generated method stub
                                                Intent intent = new Intent();
                                                intent.setClass(Dayschedule.this, Spot_ListMenu.class);
                                                Bundle bundle = new Bundle();
                                                if(name.equals(""))
                                                    bundle.putString("Add_new_schedule", "新的專案唷");
                                                else
                                                    bundle.putString("Add_new_schedule",  name);
                                                bundle.putString("search_list", "自訂清單");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                        .show();
            }
        });
    }

    private class GetAllScheduleTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            do {
                login_name = userData.getString(login_per, "");
            }while(login_name=="");
            return params[0].GetAllSchedule(login_name);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            //setTextToTextView(jsonArray);
            if(jsonArray == null)
            {
                test : System.out.println("test "+login_name);
            }
            else {
                setListAdapter(jsonArray);
            }
        }
    }

    public void setListAdapter(JSONArray jsonArray)                                   //以下database list class
    {
        this.jsonArray = jsonArray;
        schelrdeItemAdapter = new GetAllScheludeListAdapter (jsonArray, this);
        this.listView.setAdapter(schelrdeItemAdapter);
    }


    public void readData(){
        userData = getSharedPreferences(data,0);
        userData.getString(login_per, "");
        userData.getString(nameField, "");
        userData.getString(passwordField, "");
    }

    // 攔截返回鍵
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(Dayschedule.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    //  攔截返回鍵

}

