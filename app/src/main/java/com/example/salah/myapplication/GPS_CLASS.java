package com.example.salah.myapplication;

import java.math.BigDecimal;
import java.net.NetPermission;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.location.Criteria;
//--GPS_NETWORK
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.widget.Button;
import android.widget.TextView;

public class GPS_CLASS {

    String condition = null;
    String latString = null;
    String longsString = null;
    double lat1,long1;
    Location location,location2,location3;
    private LocationManager mLocationManager;
    private LocationManager netLocationManager;

    public String getlat() {



        BigDecimal bd=new BigDecimal(lat1);
        bd=bd.setScale(6, BigDecimal.ROUND_HALF_UP); //小數點後6位
        latString=String.valueOf(bd);

        return latString;
    }

    public String getlong() {

        BigDecimal db=new BigDecimal(long1);
        db=db.setScale(6, BigDecimal.ROUND_HALF_UP); //小數點後6位
        longsString=String.valueOf(db);

        return longsString;
    }


    public void GPS_init(LocationManager init1, LocationManager init2) {

        mLocationManager = init1;
        netLocationManager = init2;
    }

    public void locate() {
        // String a,b,c;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        StringBuilder builder = new StringBuilder("可用的providers");

        // GPS模組 緯度 經度 高度 方向 速度
        LocationListener ll = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub

            }
        };

        // GPS_mode
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            String provider = mLocationManager.GPS_PROVIDER;

            mLocationManager.requestLocationUpdates(provider, 5000, 10, ll); // (povider,時間(ms),距離變化(米),lisenter)

            location = mLocationManager.getLastKnownLocation(provider);
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                condition = "GPS:SUSSCESS";
                lat1 = lat;
                long1 =lng;
            } else {
                condition = "未能成功獲得GPS位置資訊請稍後在試";

                netLocationManager.requestLocationUpdates(
                        netLocationManager.NETWORK_PROVIDER, 3000, 3, ll); // (povider,時間(ms),距離變化(米),lisenter)

                location3 = netLocationManager
                        .getLastKnownLocation(netLocationManager.NETWORK_PROVIDER);

                if (location3 != null) {
                    double lat_network = location3.getLatitude();
                    double lng_network = location3.getLongitude();
                    condition = "WIFI: enable_use";
                    lat1 = lat_network;
                    long1 =lng_network;
                } else {
                    condition = "GPS功能故障或未開啟";
                }
            }

        }
        // 偵測他網路能不能抓到提供器
        // internet_mode
        else if (netLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            String network = netLocationManager.NETWORK_PROVIDER;

            netLocationManager.requestLocationUpdates(network, 3000, 3, ll); // (povider,時間(ms),距離變化(米),lisenter)

            location2 = netLocationManager
                    .getLastKnownLocation(network);

            if (location2 != null) {
                double lat_network = location2.getLatitude();
                double lng_network = location2.getLongitude();
                condition = "WIFI: enable_use";
                lat1 = lat_network;
                long1 =lng_network;
            } else {
                condition = "未能成功獲得網路位置資訊請開網路";
            }

        } else {
            condition = "GPS功能故障";
        }

    }
}
