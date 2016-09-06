package com.example.salah.myapplication;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.location.Criteria;
import android.widget.Toast;


public class SpotDetailsActivity_map extends ActionBarActivity implements LocationListener {

    private boolean getService = false;		//是否已開啟定位服務
    private static final String MAP_URL = "file:///android_asset/googleMap_Detail_RoadMap.html";
    private WebView googleMap;
    private Location mostRecentLocation;
    boolean webviewReady = false;
    String xy_location;

    final GPS_CLASS gps = new GPS_CLASS();

    LocationManager mLocationManager;
    LocationManager netLocationManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle =this.getIntent().getExtras();
        xy_location = bundle.getString("location");
        setContentView(R.layout.activity_spot_details_maps);

        mLocationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        netLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gps.GPS_init(mLocationManager, netLocationManager);
        gps.locate();
        Toast.makeText(getApplicationContext(), gps.condition, Toast.LENGTH_SHORT).show();

        googleMap = (WebView) findViewById(R.id.webView);
        googleMap.getSettings().setJavaScriptEnabled(true);
        googleMap.loadUrl(MAP_URL);
        googleMap.addJavascriptInterface(new JavaScriptInterface(), "android");

        googleMap.getSettings().setJavaScriptEnabled(true);
        googleMap.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url){
                webviewReady = true;//webview已載入完畢

                String routeURL = "javascript:RoutePlanning(" +
                        gps.getlat() + "," +
                        gps.getlong() + "," +  xy_location + ")";
                googleMap.loadUrl(routeURL);
            }
        });
        googleMap.loadUrl(MAP_URL);
    }

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

}
