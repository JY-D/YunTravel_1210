

package com.example.salah.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by salah on 2015/9/16.
 */
public class Dayschedule_finish extends ActionBarActivity {

    private ListView listView;
    private String[] list;
    private ArrayAdapter<String> listAdapter;
    TextView Schedule_name;
    Button Finish;
    String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dayschedule_finish);
        Bundle bundle =this.getIntent().getExtras();
        name = bundle.getString("new_schedule_name");
        list = bundle.getStringArray("new_schedule_list");
        Finish =(Button)findViewById(R.id.addsNewbutton_finish);
        Schedule_name = (TextView)findViewById(R.id.textView_finish_List);
        Schedule_name.setText(name);
        listView = (ListView)findViewById(R.id.listView_finish);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(listAdapter);

        Finish.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Dayschedule_finish.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray("new_schedule_list",  list );
                bundle.putString("new_schedule_name",  name );
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}
