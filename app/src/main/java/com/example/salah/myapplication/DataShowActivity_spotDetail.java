package com.example.salah.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
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
import java.util.*;

/**
 * Created by EB209 on 2015/12/10.
 */
public class DataShowActivity_spotDetail extends Activity {



    private TextView Title;
    private TextView introduction;
    private TextView Addr;
    private TextView Phone;
    RatingBar like_avg;
    TextView Type;
    String xy_location,spot_name,spot_phone,spot_addr,friend;
    Double like;

    private int SpotID;

    private ProgressDialog pDialog;
    private Intent it;

    private ImageView picture;
    private static final String baseUrlForImage = ApiConnector_allspots.ip+"img_public/";
    private Button addList;
    private Button mapButton;
    private Button editButton;
    private static final int SELECT_PICTURE = 1;
    private String idOfSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_public_details);

        Type = (TextView)findViewById(R.id.textView_type);
        Phone = (TextView)findViewById(R.id.textView_phone);
        like_avg = (RatingBar)findViewById(R.id.like_Bar);
        this.Title = (TextView) this.findViewById(R.id.textView_title);
        this.introduction = (TextView) this.findViewById(R.id.textView_introduction);
        this.Addr = (TextView) this.findViewById(R.id.textView_address);
        this.picture = (ImageView) this.findViewById(R.id.imageView);
        addList = (Button)findViewById(R.id.button_addlist);
        mapButton = (Button)findViewById(R.id.button_map);
        editButton = (Button)findViewById(R.id.button_edit);

        this.SpotID = getIntent().getIntExtra("SpotID_public",-1);// get Spot ID
        if(this.SpotID > 0)
        {
            // we have spot ID passed correctly
            new GetSpotDetails().execute(new ApiConnector_allspots());
        }

        addList.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final java.util.List<NameValuePair> d_params = new ArrayList<NameValuePair>();
                d_params.add(new BasicNameValuePair("id", String.valueOf(SpotID)));
                try {
                    d_params.add(new BasicNameValuePair("list", URLEncoder.encode("大眾清單1", "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new AsyncTask<ApiConnector_allspots,Long, Boolean >() {
                    @Override
                    protected Boolean doInBackground(ApiConnector_allspots... apiConnectors) {
                        return apiConnectors[0].addSpotToList(d_params);
                    }
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pDialog = new ProgressDialog(DataShowActivity_spotDetail.this);
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
                            Toast.makeText(getApplicationContext(), "變更失敗，請檢察網路連結", Toast.LENGTH_LONG);
                            pDialog.dismiss();
                        }
                    }
                }.execute(new ApiConnector_allspots());
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "加入清單成功！",
                        Toast.LENGTH_SHORT).show();
            }
        });

        editButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent showDetails = new Intent(getApplicationContext(), AddSpotActivity_AllspotsToMyspots.class);
                Bundle bundle = new Bundle();
                bundle.putString("Coords", xy_location );
                bundle.putString("Title", spot_name );
                bundle.putString("Addr", spot_addr );
                bundle.putString("Phone", spot_phone );
                showDetails.putExtras(bundle);
                startActivity(showDetails);
                finish();
            }
        });

        mapButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(DataShowActivity_spotDetail.this, SpotDetailsActivity_map.class);
                Bundle bundle = new Bundle();
                bundle.putString("location", xy_location );
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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
        final java.util.List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", imageData));
        params.add(new BasicNameValuePair("SpotID_public", String.valueOf(SpotID)));

        new AsyncTask<ApiConnector_allspots, Long, Boolean>() {
            @Override
            protected Boolean doInBackground(ApiConnector_allspots... apiConnectors_allspots) {
                return apiConnectors_allspots[0].uploadImageToserver(params);
            }
        }.execute(new ApiConnector_allspots());
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


    private class GetSpotDetails extends AsyncTask<ApiConnector_allspots, Long, JSONArray>
    {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DataShowActivity_spotDetail.this);
            pDialog.setMessage("載入資料...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(ApiConnector_allspots... params) {
            return params[0].GetSpotDetails(SpotID);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            try {
                JSONObject spot = jsonArray.getJSONObject(0);
                spot_name=spot.getString("title");
                spot_phone=spot.getString("phone");
                spot_addr=spot.getString("addr");
                idOfSpot = spot.getString("id");
                Title.setText(spot.getString("title"));
                introduction.setText("　　"+spot.getString("contents"));
                Type.setText("類型："+spot.getString("type"));
                Addr.setText("地址："+spot.getString("addr"));
                Phone.setText("連絡電話："+spot.getString("phone"));
                xy_location = spot.getString("coords");
                like = spot.getDouble("like");
                like_avg.setRating(like.floatValue());

                String urlForImage = baseUrlForImage + spot.getString("pic");
                new DownloadImageTask(picture).execute(urlForImage);

                //String urlForImage = baseUrlForImage + spot.getString("imageName");
                //new DownloadImageTask(picture).execute(urlForImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
            pDialog.dismiss();

        }
    }

}
