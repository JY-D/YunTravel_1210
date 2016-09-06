package com.example.salah.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
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
import org.json.JSONArray;
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
 * Created by user on 2015/8/13.
 */
public class SpotDetailsActivity_edit extends Activity implements  View.OnClickListener {

    private ArrayAdapter<String> typeAdapter=null;
    private EditText Title;
    private EditText coords;
    private EditText Addr;
    private EditText Phone;
    private EditText contents;
    private RatingBar Like;
    Spinner Type;
    private Context context;

    private int SpotID;

    String xy_location,Type_context="";
    float like_num;
    private ProgressDialog pDialog;
    private Intent it;

    private ImageView picture;
    private static String baseUrlForImage;
    private Button changeImageButton;
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
        setContentView(R.layout.activity_spot_details_edit);

        context = this;
        typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.class_list));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//預設文字

        addListenerOnRatingBar();
        Type = (Spinner)findViewById(R.id.type_select);
        Type.setAdapter(typeAdapter);
        Type.setOnItemSelectedListener(spnRegionOnItemSelected);

        this.Title = (EditText) this.findViewById(R.id.title);
        this.coords = (EditText) this.findViewById(R.id.coordinate);
        this.contents = (EditText) this.findViewById(R.id.content);
        this.Addr = (EditText) this.findViewById(R.id.addr);
        this.Phone = (EditText) this.findViewById(R.id.phone);
        this.picture = (ImageView) this.findViewById(R.id.pic);
        readData();
        this.SpotID = getIntent().getIntExtra("SpotID",-1);// get Spot ID

        readData();
        if (userData.getString(login_per, "").equals("salah"))
            baseUrlForImage = ApiConnector.ip+"img/";
        else if (userData.getString(login_per, "").equals("董哥"))
            baseUrlForImage = ApiConnector.ip_d+"img/";
        else if (userData.getString(login_per, "").equals("睿恩"))
            baseUrlForImage = ApiConnector.ip_r+"img/";

        if(this.SpotID > 0)
        {
            // we have spot ID passed correctly
            new GetSpotDetails().execute(new ApiConnector());
        }


        storeButton = (Button) this.findViewById(R.id.store_button);
        storeButton.setOnClickListener(this);


        this.changeImageButton = (Button) this.findViewById(R.id.changeImage);
        this.changeImageButton.setOnClickListener(this);


    }

    public void onClick(View v){
        switch(v.getId())
        {
            case R.id.changeImage:
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
                break;
            }

            case R.id.store_button:
            {
                final  List<NameValuePair> d_params = new ArrayList<NameValuePair>();

                d_params.add(new BasicNameValuePair("id", String.valueOf(SpotID)));
                try {
                    d_params.add(new BasicNameValuePair("title", URLEncoder.encode(Title.getText().toString(), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    d_params.add(new BasicNameValuePair("coords", URLEncoder.encode(coords.getText().toString(), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
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
                    d_params.add(new BasicNameValuePair("phone", URLEncoder.encode(Phone.getText().toString(), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    d_params.add(new BasicNameValuePair("contents", URLEncoder.encode(contents.getText().toString(), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    d_params.add(new BasicNameValuePair("type", URLEncoder.encode(Type_context, "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    d_params.add(new BasicNameValuePair("like", URLEncoder.encode(String.valueOf(like_num), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), d_params.toString(),Toast.LENGTH_LONG);

                new AsyncTask<ApiConnector,Long, Boolean >() {

                    @Override
                    protected Boolean doInBackground(ApiConnector... apiConnectors) {
                        return apiConnectors[0].changeSpotDetailToServer(d_params,userData.getString(login_per, ""));
                    }

                    protected void onPreExecute() {
                        super.onPreExecute();
                        pDialog = new ProgressDialog(SpotDetailsActivity_edit.this);
                        pDialog.setMessage("正在變更中...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }

                    protected void onPostExecute(Boolean result) {
                        if(result)
                            pDialog.dismiss();
                        else
                        {
                            Toast.makeText(getApplicationContext(), "變更失敗，請檢察網路連結",Toast.LENGTH_LONG);
                            pDialog.dismiss();
                        }
                    }

                }.execute(new ApiConnector());

                Intent intent = new Intent();
                intent.setClass(SpotDetailsActivity_edit.this, MainActivity.class);
                startActivity(intent);
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
        params.add(new BasicNameValuePair("SpotID", String.valueOf(SpotID)));

        new AsyncTask<ApiConnector, Long, Boolean>() {
            @Override
            protected Boolean doInBackground(ApiConnector... apiConnectors) {
                return apiConnectors[0].uploadImageToserver(params,userData.getString(login_per, ""));
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


    private class GetSpotDetails extends AsyncTask<ApiConnector, Long, JSONArray>
    {

        /*public GetSpotDetails(Intent _it){
            it=_it;
        }*/

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SpotDetailsActivity_edit.this);
            pDialog.setMessage("載入資料...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            return params[0].GetSpotDetails(SpotID,userData.getString(login_per, ""));
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            //JSONObject spot = null;

            try {
                JSONObject spot = jsonArray.getJSONObject(0);

                idOfSpot = spot.getString("id");
                Title.setText(spot.getString("title"));
                coords.setText(spot.getString("coords"));
                contents.setText(spot.getString("contents"));
                Addr.setText(spot.getString("addr"));
                Phone.setText(spot.getString("phone"));
                xy_location = spot.getString("coords");

                String urlForImage = baseUrlForImage + spot.getString("pic");

                new DownloadImageTask(picture).execute(urlForImage);

                //String urlForImage = baseUrlForImage + spot.getString("imageName");
                //new DownloadImageTask(picture).execute(urlForImage);

            } catch (Exception e) {
                e.printStackTrace();
            }

            /*if (spot != null) {

            }*/

            pDialog.dismiss();
            //startActivity(it);


        }
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
            intent.setClass(SpotDetailsActivity_edit.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    //  攔截返回鍵
}
