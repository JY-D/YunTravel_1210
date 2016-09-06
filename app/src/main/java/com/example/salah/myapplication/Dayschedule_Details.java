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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by EB209 on 2015/12/6.
 */
public class Dayschedule_Details extends ActionBarActivity {
    private SharedPreferences userData;
    private static final String data = "DATA";
    private static final String nameField = "123";
    private static final String passwordField = "456";
    private static final String login_per ="";
    String login_name="";
    private ItemDAO itemDAO;
    private GetAllScheludeListAdapter_Details schelrdeItemAdapter;
    private JSONArray jsonArray;
    protected JSONObject scheduleClicked;
    private ListView listView;
    Button btn_ok;
    String new_schedule_name,name="",title[]=new String[24],content[]=new String[24],start[]=new String[24],end[]=new String[24],pic[]=new String[24];
    int i=0;
    DatePickerDialog.OnDateSetListener dPL;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle =this.getIntent().getExtras();
        name = bundle.getString("new_schedule");

        setContentView(R.layout.main_dayschedule_details);

        btn_ok =(Button)findViewById(R.id.button_check);
        listView = (ListView)findViewById(R.id.listView_sDetails);
        readData();
        new GetAllSchedule_DetailsTask().execute(new ApiConnector());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    scheduleClicked = jsonArray.getJSONObject(position);
                    title[i]= scheduleClicked.getString("title");
                    content[i]= scheduleClicked.getString("contents");
                    start[i]= scheduleClicked.getString("start");
                    end[i]= scheduleClicked.getString("end");
                    pic[i]= scheduleClicked.getString("pic");
                    //new_schedule = scheduleClicked.getString("title");
                   } catch (JSONException e) {
                    e.printStackTrace();
                }
                test:System.out.println("test " +  end[i]);
                i++;
            }
        });

        dPL=new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view ,int year,int  monthOfYear,int dayOfMonth){
                new_schedule_name = name + "(" + year + "-" + (monthOfYear+1) + "-" +dayOfMonth + ")";

                new AlertDialog.Builder(Dayschedule_Details.this)
                        .setTitle("確認新增行程")
                        .setMessage(new_schedule_name + " 將會加入您的行程中。")
                        .setIcon(R.mipmap.logo)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int j=0;j<i;j++){
                                 test:System.out.println("test " + title[j]);}
                                itemDAO=new ItemDAO(getApplicationContext());
                                itemDAO.download(new_schedule_name,title,content,start,end,i,pic);
                                Toast.makeText(Dayschedule_Details.this, "已經成功加入行程", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                        .show();
            }
        };

        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(0);
            }
        });

    }

    private class GetAllSchedule_DetailsTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            do {
                login_name = userData.getString(login_per, "");
            }while(login_name=="");
            return params[0].SearchAllSchedule(login_name, name);
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
        this.schelrdeItemAdapter = new GetAllScheludeListAdapter_Details (jsonArray, this);
        this.listView.setAdapter(schelrdeItemAdapter);
    }

    public void readData(){
        userData = getSharedPreferences(data,0);
        userData.getString(login_per, "");
        userData.getString(nameField, "");
        userData.getString(passwordField, "");
    }

    protected Dialog onCreateDialog(int id){
        Calendar c = Calendar.getInstance();
        switch (id){
            case 0:
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int date = c.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(this,dPL,year,month,date);
        }
        return null;
    }
}
