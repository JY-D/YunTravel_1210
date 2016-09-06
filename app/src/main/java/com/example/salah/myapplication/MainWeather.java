package com.example.salah.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MainWeather  extends ActionBarActivity{


    Map<String, String> descript = new HashMap<String, String>();
    TextView tv,tv1,tv2,tv_,tv_1,tv_2;
    Service_Activity showinfo = new Service_Activity();
    String Temperature="",Conditions="",Rainfall="",N_Temperature="",N_Conditions="",N_Rainfall="";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_weather);
        tv = (TextView)findViewById(R.id.text_T);
        tv1 = (TextView)findViewById(R.id.text_C);
        tv2 = (TextView)findViewById(R.id.text_RF);
        tv_ = (TextView)findViewById(R.id.text_NT);
        tv_1 = (TextView)findViewById(R.id.text_NC);
        tv_2 = (TextView)findViewById(R.id.text_NRF);

        try {
            // TODO Auto-generated method stub
            new Thread(runnable).start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            try {
                descript = showinfo.Showinfo();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //輸出訊息
            Temperature = descript.get("Temperature");
            Conditions = descript.get("Conditions");
            Rainfall = descript.get("Rainfall");
            N_Temperature=descript.get("N_Temperature");
            N_Conditions=descript.get("Conditions");
            N_Rainfall=descript.get("N_Rainfall");
            tv.setText(Temperature+"°C");
            tv1.setText(Conditions);
            tv2.setText(Rainfall + "%");
            tv_.setText(N_Temperature+"°C");
            tv_1.setText(N_Conditions);
            tv_2.setText(N_Rainfall + "%");
        }
    };
}
