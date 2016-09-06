package com.example.salah.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.widget.Toast;
import java.util.List;
import java.util.Locale;

public class MainNearMapActivity extends ActionBarActivity implements LocationListener {

    private boolean getService = false;		//是否已開啟定位服務
    private static final String MAP_URL = "file:///android_asset/googleMap.html";
    private WebView googleMap;
    private Location mostRecentLocation;
    boolean webviewReady = false;

    final GPS_CLASS gps = new GPS_CLASS();

    LocationManager mLocationManager;
    LocationManager netLocationManager;

    TextView gps_area_TextView;
    Button gpsrefreshButton;
    String returnAddress="";

    String star_location[]={"23.711791, 120.541169","23.692236, 120.534944","23.701313, 120.536683","23.696304, 120.532317","23.698228, 120.534411","23.696952, 120.535987"};
    String star_location_C[]={"斗六火車站","雲林科技大學","柚子藝術館","斗六人文公園","斗六人文夜市","雲林縣政府警察局"};
    String restaurant_location[]={"23.697429, 120.537644","23.699479, 120.537115","23.702522, 120.536882","23.701963, 120.534157","23.693356,120.529658"};
    //String restaurant_location_C[]={"23.697429, 120.537644","23.699479, 120.537115","23.702522, 120.536882","23.701963, 120.534157","23.693356,120.529658"};
    String restaurant_location_C[]={"麻辣風暴鴛鴦火鍋","花鳥山日式創意料理","千葉火鍋","活力永早餐店","常璟紙火鍋"};
    String landscape_location[]={"23.729990, 120.569599","23.597752, 120.591728","23.616460, 120.578039","23.659431, 120.540873","23.601259, 120.665798"};
    String landscape_location_C[]={"雅聞峇里海岸觀光工廠","華山咖啡休閒區","劍湖山世界","綠色隧道景觀公園","草嶺清水溪"};
    //------------附近景點------------
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_maps);

        mLocationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        netLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gps.GPS_init(mLocationManager, netLocationManager);
        gps.locate();
        Toast.makeText(getApplicationContext(), gps.condition, Toast.LENGTH_SHORT).show();

        gpsrefreshButton = (Button) findViewById(R.id.button1); // refresh
        gps_area_TextView = (TextView) findViewById(R.id.textView1);//gps位置
        googleMap = (WebView) findViewById(R.id.webView1);
        googleMap.getSettings().setJavaScriptEnabled(true);
        googleMap.loadUrl(MAP_URL);
        googleMap.addJavascriptInterface(new JavaScriptInterface(), "android");

        gpsrefreshButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gps.locate();
                final String clear1 = "javascript:clearOverlays()";
                //   final String show="javascript:showOverlays()";
                final String del1 = "javascript:deleteOverlays()";
                googleMap.loadUrl(clear1);
                //  webView.loadUrl(show);
                googleMap.loadUrl(del1);
                locationServiceInitial();
                gps_area_TextView.setText("目前位置：\n　" + returnAddress);
                String centerURL = "javascript:centerAt(" +
                        gps.getlat() + "," +
                        gps.getlong() + ")";
                googleMap.loadUrl(centerURL);
                //顯示地圖上的標記
                for (int x = 0; x < star_location.length; x++) {
                    String mark1URL = "javascript:mark1(" + star_location[x] + "," + x + ")";
                    googleMap.loadUrl(mark1URL);
                }
                for (int x = 0; x < restaurant_location.length; x++) {
                    String mark2URL = "javascript:mark2(" + restaurant_location[x] + "," + x + ")";
                    googleMap.loadUrl(mark2URL);
                }
                for (int x = 0; x < landscape_location.length; x++) {
                    String mark3URL = "javascript:mark3(" + landscape_location[x] + "," + x + ")";
                    googleMap.loadUrl(mark3URL);
                }
            }
        });

        googleMap.getSettings().setJavaScriptEnabled(true);
        googleMap.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webviewReady = true;//webview已載入完畢
                gps.locate();
                locationServiceInitial();
                gps_area_TextView.setText("目前位置：\n　" + returnAddress );
                String centerURL = "javascript:centerAt(" +
                        gps.getlat() + "," +
                        gps.getlong() + ")";
                googleMap.loadUrl(centerURL);
                for (int x = 0; x < star_location.length; x++) {
                    String mark1URL = "javascript:mark1(" + star_location[x] + "," + x +")";
                    googleMap.loadUrl(mark1URL);
                }
                for (int x = 0; x < restaurant_location.length; x++) {
                    String mark2URL = "javascript:mark2(" + restaurant_location[x] + "," + x + ")";
                    googleMap.loadUrl(mark2URL);
                }
                for (int x = 0; x < landscape_location.length; x++) {
                    String mark3URL = "javascript:mark3(" + landscape_location[x] + "," + x + ")";
                    googleMap.loadUrl(mark3URL);
                }
            }
        });
        googleMap.loadUrl(MAP_URL);
    }//------------附近景點------------

    private class JavaScriptInterface {
        public double getLatitude(){
            return mostRecentLocation.getLatitude();
        }
        public double getLongitude(){
            return mostRecentLocation.getLongitude();
        }
    }

    private void testLocationProvider() {
        //取得系統定位服務
        LocationManager status = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            getService = true;	//確認開啟定位服務
            locationServiceInitial();
        } else {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
        }
    }

    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;	//最佳資訊提供者
    private void locationServiceInitial() {
        lms = (LocationManager) getSystemService(LOCATION_SERVICE);	//取得系統定位服務
        Criteria criteria = new Criteria();	//資訊提供者選取標準
        bestProvider = lms.getBestProvider(criteria, true);	//選擇精準度最高的提供者
        Location location = lms.getLastKnownLocation(bestProvider);
        getLocation(location);
    }

    private void getLocation(Location location) {	//將定位資訊顯示在畫面中
        if (location != null) {
            Double longitude = location.getLongitude();    //取得經度
            Double latitude = location.getLatitude();    //取得緯度
            try {
                    //建立Geocoder物件: Android 8 以上模疑器測式會失敗
                    Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE); 	//地區:台灣
                    //自經緯度取得地址
                    List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);
                    returnAddress = lstAddress.get(0).getAddressLine(0);
                //lstAddress.get(0).getAddressLine(0); 完整地址
                // lstAddress.get(0).getCountryName();   台灣省
                // lstAddress.get(0).getAdminArea();   台北市
                // lstAddress.get(0).getLocality();   中正區
                // lstAddress.get(0).getThoroughfare();   信陽街(包含路巷弄)
                // lstAddress.get(0).getFeatureName();  會得到33(號)
                // lstAddress.get(0).getPostalCode();   會得到100(郵遞區號)
                }
            catch(Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "無法定位座標，請開啟網路或GPS", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(getService) {
            lms.requestLocationUpdates(bestProvider, 1000, 1, this);
            //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if(getService) {
            lms.removeUpdates(this);	//離開頁面時停止更新
        }
    }

    @Override
    protected void onRestart() {	//從其它頁面跳回時
        // TODO Auto-generated method stub
        super.onRestart();
        testLocationProvider();
    }

    @Override
    public void onLocationChanged(Location location) {	//當地點改變時
        // TODO Auto-generated method stub
        getLocation(location);
    }

    @Override
    public void onProviderDisabled(String arg0) {	//當GPS或網路定位功能關閉時
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String arg0) {	//當GPS或網路定位功能開啟
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {	//定位狀態改變
        // TODO Auto-generated method stub
    }
    // 攔截返回鍵
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(MainNearMapActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    //  攔截返回鍵
}
