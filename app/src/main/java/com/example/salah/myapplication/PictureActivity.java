package com.example.salah.myapplication;

/**
 * Created by 2wsx1000g on 2015/10/7.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class PictureActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picture);

        // 取得照片元件
        ImageView picture_view = (ImageView) findViewById(R.id.picture_view);

        // 讀取照片檔案名稱
        Intent intent = getIntent();
        String pictureName = intent.getStringExtra("pictureName");

        if (pictureName != null) {
            // 設定照片元件
            byte bytes[] = Base64.decode(pictureName, Base64.DEFAULT);

            //用BitmapFactory生成bitmap
            Bitmap bmplist = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            picture_view.setImageBitmap(bmplist);
        }
    }

    public void clickPicture(View view) {
        // 如果裝置的版本是LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        else {
            finish();
        }
    }

}