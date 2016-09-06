package com.example.salah.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import android.app.Activity;
import android.app.ProgressDialog;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EB209 on 2015/11/26.
 */
public class SpotDetailsActivity_friend extends Activity {
    private TextView Title;
    private TextView coords;
    private TextView Addr;
    private TextView Phone;
    RatingBar like_avg;
    TextView Type;
    String xy_location,friend,spot_name,spot_phone,spot_addr;
    double like;

    private int SpotID;

    private ProgressDialog pDialog;
    private Intent it;

    private ImageView picture;
    private static String baseUrlForImage;
    private Button mapButton;
    private Button editButton;
    private static final int SELECT_PICTURE = 1;
    private String idOfSpot;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle =this.getIntent().getExtras();
        friend = bundle.getString("friend");
        setContentView(R.layout.activity_spot_details_friend);

        if ( friend.equals("salah"))
            baseUrlForImage = ApiConnector_friend.ip+"img/";
        else if ( friend.equals("董哥"))
            baseUrlForImage = ApiConnector_friend.ip_d+"img/";
        else if ( friend.equals("睿恩"))
            baseUrlForImage = ApiConnector_friend.ip_r+"img/";

        Type = (TextView)findViewById(R.id.textView_type);
        Phone = (TextView)findViewById(R.id.textView_phone);
        like_avg = (RatingBar)findViewById(R.id.like_Bar);
        this.Title = (TextView) this.findViewById(R.id.textView_title);
        this.coords = (TextView) this.findViewById(R.id.textView_introduction);
        this.Addr = (TextView) this.findViewById(R.id.textView_address);
        this.picture = (ImageView) this.findViewById(R.id.imageView);
        mapButton = (Button)findViewById(R.id.button_map);
        editButton = (Button)findViewById(R.id.button_addtomy);


        this.SpotID = getIntent().getIntExtra("SpotID",-1);// get Spot ID
        if(this.SpotID > 0)
        {
            // we have spot ID passed correctly
            new GetSpotDetails().execute(new ApiConnector_friend());
        }


        editButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent showDetails = new Intent(getApplicationContext(), AddSpotActivity_FriendspotsToMyspots.class);
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
                intent.setClass(SpotDetailsActivity_friend.this, SpotDetailsActivity_map.class);
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
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", imageData));
        params.add(new BasicNameValuePair("SpotID", String.valueOf(SpotID)));

        new AsyncTask<ApiConnector_friend, Long, Boolean>() {
            @Override
            protected Boolean doInBackground(ApiConnector_friend... apiConnectors) {
                return apiConnectors[0].uploadImageToserver(params,friend);
            }
        }.execute(new ApiConnector_friend());
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


    private class GetSpotDetails extends AsyncTask<ApiConnector_friend, Long, JSONArray>
    {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SpotDetailsActivity_friend.this);
            pDialog.setMessage("載入資料...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(ApiConnector_friend... params) {
            return params[0].GetSpotDetails(SpotID,friend);
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
                coords.setText("　　"+spot.getString("contents"));
                Type.setText("類型："+spot.getString("type"));
                Addr.setText("地址："+spot.getString("addr"));
                Phone.setText("連絡電話："+spot.getString("phone"));
                xy_location = spot.getString("coords");
                like = spot.getDouble("like");
                like_avg.setRating((float) like);

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
