package com.example.salah.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by salah on 2015/9/11.
 */

public class Spot_ListMenu extends ActionBarActivity {

    private ListView listView;
    private String[] list = {"A","B","C","D","E"},newlist = {"","",""};
    private ArrayAdapter<String> listAdapter;
    TextView Schedule_name;
    Button Next;
    String name,search_list,new_spot;
    int i=0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_listmenu);
        //景點清單表,現在還缺載入專案編輯頁面,選擇清單頁面
        Bundle bundle =this.getIntent().getExtras();
        name = bundle.getString("Add_new_schedule");
        search_list = bundle.getString("search_list");
        Next =(Button)findViewById(R.id.addsNewbutton_next);
        Schedule_name = (TextView)findViewById(R.id.textView_spotList);
        Schedule_name.setText("選擇景點加入專案");
        listView = (ListView)findViewById(R.id.listView);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new_spot = list[position];
                Toast.makeText(getApplicationContext(), "你選擇的是\"" + new_spot + "\"景點",
                        Toast.LENGTH_SHORT).show();
                newlist[i]=list[position];
                i++;
                if(i>2)
                    i=0;
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Spot_ListMenu.this, Dayschedule_finish.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray("new_schedule_list",  newlist );
                bundle.putString("new_schedule_name",  name );
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}
