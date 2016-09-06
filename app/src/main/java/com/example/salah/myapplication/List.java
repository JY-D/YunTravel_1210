package com.example.salah.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by 2wsx1000g on 2015/8/26.
 */
public class List extends Activity {
    private ItemDAO itemDAO;
    private ListView time_list;
    private java.util.List<Item> items;
    private ItemAdapter itemAdapter;
    private Item itemm;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_list);
        time_list = (ListView) findViewById(R.id.time_list);
       // itemm=new Item();

        Intent intent = getIntent();
        itemm = (Item) intent.getExtras().getSerializable(
                ".Item");

        control();
        itemDAO = new ItemDAO(getApplicationContext());
        if (itemDAO.getCount(itemm.getTId()) == 0) {
            itemDAO.sample(itemm.getTId());
        }
        items = itemDAO.getSome(itemm.getTId());
        itemAdapter = new ItemAdapter(this, R.layout.single_item, items);
        time_list.setAdapter(itemAdapter);
    }
    private void control() {

        AdapterView.OnItemClickListener itemlistener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 讀取選擇的記事物件
                itemm = new Item();
              //  itemm.setStarttime(position);
                Item item = itemAdapter.getItem(position);




                    Intent intent = new Intent(
                            ".EDIT_ITEM");

                    // 設定記事編號與記事物件
                    intent.putExtra("position", position);
                    intent.putExtra(".Item", item);

                    startActivityForResult(intent, 1);
                }

        };
        time_list.setOnItemClickListener(itemlistener);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 如果被啟動的Activity元件傳回確定的結果
        if (resultCode == Activity.RESULT_OK) {
            // 讀取記事物件
            Item item = (Item) data.getExtras().getSerializable(
                    ".Item");

            boolean updateAlarm = false;

            if (requestCode == 0) {
                // 設定記事物件的編號與日期時間
                item = itemDAO.insert(item);

                items.add(item);
                itemAdapter.notifyDataSetChanged();


            }
            // 如果是修改記事
            else if (requestCode == 1) {
                // 讀取記事編號
                int position = data.getIntExtra("position", -1);

                if (position != -1) {
                    // 讀取原來的提醒設定
                    Item ori = itemDAO.get(item.getId());
                    // 判斷是否需要設定提醒
                    updateAlarm = (item.getAlarmDatetime() != ori.getAlarmDatetime());
                    // 設定修改的記事物件
                    itemDAO.update(item);
                   /* int list_c = (int)(item.getEndtime()-item.getStarttime());
                    for(int i=0;i<=list_c;i++) {
                        items.set(itemDAO.getB(item) + i, item);
                    }*/
                    //items = itemDAO.getSome(itemm.getTId());
                    items = itemDAO.getSome(item.getTId());
                    itemAdapter = new ItemAdapter(this, R.layout.single_item, items);
                    time_list.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();

                }
            }
            if (item.getAlarmDatetime() != 0 && updateAlarm) {
                Intent intent = new Intent(this, AlarmReceiver.class);
                intent.putExtra("id", item.getId());

                PendingIntent pi = PendingIntent.getBroadcast(
                        this, (int) item.getId(),
                        intent, PendingIntent.FLAG_ONE_SHOT);

                AlarmManager am = (AlarmManager)
                        getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, item.getAlarmDatetime(), pi);
            }
        }
    }
}
