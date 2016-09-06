package com.example.salah.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015/8/21.
 */
public class AddSpotActivity extends ActionBarActivity implements LocationListener, View.OnClickListener {

    final GPS_CLASS gps = new GPS_CLASS();

    private ArrayAdapter<String> typeAdapter = null;
    private EditText Title;
    private EditText coords;
    private Spinner  Type;
    private EditText Addr;
    private EditText Phone;
    private RatingBar Like;

    private ProgressDialog pDialog;

    private String SpotID;

    private boolean getService = false;		//是否已開啟定位服務
    private Location mostRecentLocation;

    String xy_location,Type_context="",friend;
    float like_num;
    LocationManager mLocationManager;
    LocationManager netLocationManager;

    private ImageView picture;
    private static String baseUrlForImage;
    private Button selectImageButton;
    private Button cancelButton;
    private Button storeButton;
    private static final int SELECT_PICTURE = 1;
    private String idOfSpot;

    private SharedPreferences userData;
    private static final String data = "DATA";
    private static final String nameField = "";
    private static final String passwordField = "";
    private static final String login_per = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addspot);

        typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.class_list));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//預設文字

        Type = (Spinner)findViewById(R.id.add_type_select);
        Type.setAdapter(typeAdapter);
        Type.setOnItemSelectedListener(spnRegionOnItemSelected);

        this.Title = (EditText) this.findViewById(R.id.add_title);
        this.coords = (EditText) this.findViewById(R.id.add_coordinate);
        this.Addr = (EditText) this.findViewById(R.id.add_addr);
        this.Phone = (EditText) this.findViewById(R.id.add_phone);
        this.picture = (ImageView) this.findViewById(R.id.pic);

        //this.googleMap = (WebView) this.findViewById(R.id.googlemapOfSpot);

        readData();
        if (userData.getString(login_per, "").equals("salah"))
            baseUrlForImage = ApiConnector.ip+"img/";
        else if (userData.getString(login_per, "").equals("董哥"))
            baseUrlForImage = ApiConnector.ip_d+"img/";
        else if (userData.getString(login_per, "").equals("睿恩"))
            baseUrlForImage = ApiConnector.ip_r+"img/";
        friend=userData.getString(login_per, "");
        new getCurrentMAXId().execute(new ApiConnector());


        mLocationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        netLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gps.GPS_init(mLocationManager, netLocationManager);
        gps.locate();

        selectImageButton = (Button) this.findViewById(R.id.selectImage_Button);
        selectImageButton.setOnClickListener(this);

        storeButton = (Button) this.findViewById(R.id.store_button);
        storeButton.setOnClickListener(this);

        cancelButton = (Button) this.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

        coords.setText("正在擷取您的位置...");
        coords.setText(gps.getlat() + ", " +gps.getlong());
        addListenerOnRatingBar();
    }

    public void onClick(View v){
        switch(v.getId())
        {
            case R.id.selectImage_Button:
            {
                while((SpotID == null) || (Integer.parseInt(SpotID) < 1))
                {
                    Toast.makeText(getApplicationContext(),"網路連線失敗，重新連線",Toast.LENGTH_LONG);
                    new getCurrentMAXId().execute(new ApiConnector());
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
                break;
            }

            case R.id.store_button:
            {
                final  List<NameValuePair> d_params = new ArrayList<NameValuePair>();
                d_params.add(new BasicNameValuePair("id", SpotID));
                d_params.add(new BasicNameValuePair("pic", idOfSpot+".jpg"));
                try {
                    d_params.add(new BasicNameValuePair("title", URLEncoder.encode(Title.getText().toString(),  "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                d_params.add(new BasicNameValuePair("coords", gps.getlat() + ", " +gps.getlong()));
                try {
                    d_params.add(new BasicNameValuePair("addr", URLEncoder.encode(Addr.getText().toString(), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    d_params.add(new BasicNameValuePair("area", URLEncoder.encode(Addr.getText().toString(), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    d_params.add(new BasicNameValuePair("type", URLEncoder.encode(Type_context, "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    d_params.add(new BasicNameValuePair("phone", URLEncoder.encode(Phone.getText().toString(), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    d_params.add(new BasicNameValuePair("like", URLEncoder.encode(String.valueOf(like_num), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, d_params.toString(),Toast.LENGTH_LONG).show();

                new AsyncTask<ApiConnector,Long, Boolean >() {

                    @Override
                    protected Boolean doInBackground(ApiConnector... apiConnectors) {
                        return apiConnectors[0].addSpotDetailToServer(d_params,friend);
                    }

                    protected void onPreExecute() {
                        super.onPreExecute();
                        pDialog = new ProgressDialog(AddSpotActivity.this);
                        pDialog.setMessage("正在儲存中...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }

                    protected void onPostExecute() {
                        pDialog.dismiss();
                    }

                }.execute(new ApiConnector());
                pDialog.dismiss();

                Intent intent = new Intent();
                intent.setClass(AddSpotActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
                break;
            }

            case R.id.cancel_button:
            {
                this.finish();
                break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (Build.VERSION.SDK_INT < 19) {
                    String selectedImagePath = getPath(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    SetImage(bitmap);
                } else {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        SetImage(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void SetImage(Bitmap image) {
        this.picture.setImageBitmap(image);
        // upload
        String imageData = encodeTobase64(image);
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", imageData));
        params.add(new BasicNameValuePair("SpotID", SpotID));

        new AsyncTask<ApiConnector, Long, Boolean>() {
            @Override
            protected Boolean doInBackground(ApiConnector... apiConnectors) {
                return apiConnectors[0].uploadImageToserver(params,friend);
            }
        }.execute(new ApiConnector());
    }

    public static String encodeTobase64(Bitmap image) {
        System.gc();

        if (image == null)return null;

        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT); // min minSdkVersion 8

        return imageEncoded;
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    private class getCurrentMAXId extends AsyncTask<ApiConnector, Long, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(ApiConnector... params) {
            return params[0].GetCurrentMaxId(friend);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddSpotActivity.this);
            pDialog.setMessage("讀取中...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
               //JSONObject try
            try {
                //if(jsonArray == null)
                if(jsonObject == null)
                {
                    Title.setText("無法連線至資料庫");
                    pDialog.dismiss();
                }
                else {
                    //Math.max.apply(Math,jsonArray.map(function(o){return o.y;}))
                    //SpotID = jsonArray.getString("id");
                    //JSONObject max_id = jsonArray.getJSONObject(0);
                    JSONObject max_id = jsonObject;
                    SpotID = String.valueOf(Integer.parseInt(max_id.getString("MAX(id)")) + 1);
                    idOfSpot = String.valueOf(Integer.parseInt(max_id.getString("MAX(id)")) + 1);
                    //Title.setText(SpotID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();
        }
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

    public void addListenerOnRatingBar() {

        Like = (RatingBar) findViewById(R.id.like_Bar);


        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        Like.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                like_num = rating;

            }
        });
    }

    public AdapterView.OnItemSelectedListener spnRegionOnItemSelected = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View v,int position, long id) {
            Type_context = parent.getSelectedItem().toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        } };

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
            intent.setClass(AddSpotActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    //  攔截返回鍵
}