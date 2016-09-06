package com.example.salah.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

/**
 * Created by EB209 on 2015/11/24.
 */
public class Friends_list extends ActionBarActivity {

    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    public String[] list = {"睿恩","董哥"} ;
    private SharedPreferences userData;
    private static final String data = "DATA";
    private static final String nameField = "";
    private static final String passwordField = "";
    private static final String login_per = "";
    String name;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firends_list);

        listView = (ListView)findViewById(R.id.friends_list);

        readData();
        name=userData.getString(login_per, "");
        switch (name){
            case "salah":
                list = new String[]{"睿恩","董哥"};
                setListAdapter(list);
                break;
            case "睿恩":
                list = new String[]{"salah","董哥"};
                setListAdapter(list);
                break;
            case "董哥":
                list = new String[]{"salah","睿恩"};
                setListAdapter(list);
                break;
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "你選擇的是" + list[position] ,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(Friends_list.this, Friends_list_Details.class);
                Bundle bundle = new Bundle();
                bundle.putString("friend",list[position]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void readData(){
        userData = getSharedPreferences(data,0);
        userData.getString(login_per, "");
        userData.getString(nameField, "");
        userData.getString(passwordField, "");
    }

    public void setListAdapter(String[] jsonArray)                                   //以下database list class
    {
        this.list = jsonArray;
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        this.listView.setAdapter(listAdapter);
    }

    // 攔截返回鍵
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(Friends_list.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    //  攔截返回鍵
}
