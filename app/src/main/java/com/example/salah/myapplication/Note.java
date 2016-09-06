package com.example.salah.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;

import static android.widget.PopupMenu.OnMenuItemClickListener;

/**
 * Created by 2wsx1000g on 2015/8/17.
 */
public class Note extends Activity{
    private EditText title_text, content_text,spinner_text;
    private Item itemm;
    private ImageView picture,ppp;
    private String fileName;
    private ImageButton option;
    private static final int START_CAMERA = 3;
    private static final int START_DELETE = 2;
    private static final int START_ALARM = 0;
    private static final int START_COLOR = 1;
    private ItemAdapter itemAdapter;
    private DisplayMetrics mPhone;
    private ArrayAdapter<String> listAdapter;
    //private ArrayAdapter<String> timeAdapter;
    private Spinner kind = null;
    private Spinner name = null;
    private String itemname[] = null;
    Integer ccc;
    LinearLayout up_;
    Spinner start_time,end_time;
    Button color_select,save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_note);
        itemm = new Item();
        processViews();
        setapinner();
        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);


        Intent intent = getIntent();
        // 讀取Action名稱
        String action = intent.getAction();


        // 如果是修改記事

            // 接收與設定記事標題
            itemm = (Item) intent.getExtras().getSerializable(
                    ".Item");
            title_text.setText(itemm.getTitle());
            content_text.setText(itemm.getContent());
            up_.setBackgroundColor(itemm.getColor().parseColor());
            color_select.setBackgroundColor(itemm.getColor().parseColor());
        option.setBackgroundColor(itemm.getColor().parseColor());
        save.setBackgroundColor(itemm.getColor().parseColor());
        start_time.setSelection((int) (itemm.getStarttime()));
        end_time.setSelection((int) (itemm.getEndtime()));

//----------------右上角更多選項
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupmenu = new PopupMenu(Note.this, option);
                popupmenu.inflate(R.menu.popup_menu); //

                popupmenu.setOnMenuItemClickListener(new OnMenuItemClickListener() { // 設定popupmenu項目點擊傾聽者.

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {  //
                        switch (item.getItemId()) { // 取得被點擊的項目id.


                            case R.id.alar: // 藍色項目被點擊,文字設定為藍色.
                                processSetAlarm();
                                break;
                            case R.id.camar:
                                Intent takeIntent;
                                takeIntent = new Intent();
                                takeIntent.setType("image/*");
                                takeIntent.setAction(Intent.ACTION_GET_CONTENT);

                                startActivityForResult(Intent.createChooser(takeIntent, "來源選擇"), START_CAMERA);
                                break;
                            default:
                                break;
                        }

                        return true;
                    }

                });
                popupmenu.show();
            }
        });

        //選項--------------------------
    }





    private void setapinner() {
        ArrayAdapter timeAdapter = ArrayAdapter.createFromResource(this, R.array.time_range,android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter timeAdapter2 = ArrayAdapter.createFromResource(this, R.array.time_range2,android.R.layout.simple_spinner_item);
        timeAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        start_time.setAdapter(timeAdapter);
        start_time.setOnItemSelectedListener(spinner);
        end_time.setAdapter(timeAdapter2);
        end_time.setOnItemSelectedListener(spinner2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (( requestCode == START_CAMERA ) && data != null)
        {
            //取得照片路徑uri
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();

            try
            {
                //讀取照片，型態為Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //filename
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream );
                byte bytes[] = stream.toByteArray();
                String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
                fileName = base64;

                //判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
                if(bitmap.getWidth()>bitmap.getHeight())ScalePic(bitmap,
                        mPhone.heightPixels,0);
                else ScalePic(bitmap,mPhone.widthPixels,1);
            }
            catch (FileNotFoundException e)
            {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case START_CAMERA:
                    // 設定照片檔案名稱
                    itemm.setFileName(fileName);
                    break;
                case START_DELETE:
                    break;
                case START_ALARM:
                    break;
                // 設定顏色
                case START_COLOR:
                    int colorId = data.getIntExtra(
                            "colorId", Colors.WHITE.parseColor());
                    itemm.setColor(getColors(colorId));
                    color_select.setBackgroundColor(colorId);
                    option.setBackgroundColor(itemm.getColor().parseColor());
                    up_.setBackgroundColor(colorId);
                    save.setBackgroundColor(itemm.getColor().parseColor());
                    break;
            }
        }
    }
    private void ScalePic(Bitmap bitmap,int phone,int a)
    {
        //縮放比例預設為1
        float mScale = 1 ;

        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
       //if(bitmap.getWidth() > phone )

            //判斷縮放比例
            mScale = (float)phone/(float)bitmap.getWidth();

            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);


            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    mMat,
                    false);
            picture.setImageBitmap(mScaleBitmap);

       // else picture.setImageBitmap(bitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果有檔案名稱
        if (itemm.getFileName() != null && itemm.getFileName().length() > 0) {
            // 照片檔案物件
           // File file = configFileName("P", ".jpg");

            // 如果照片檔案存在
            byte bytes[] = Base64.decode(itemm.getFileName(), Base64.DEFAULT);

            //用BitmapFactory生成bitmap
            Bitmap bmplist = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // 顯示照片元件
                picture.setVisibility(View.VISIBLE);
                // 設定照片
               // FileUtil.fileToImageView(file.getAbsolutePath(), picture);
            picture.setImageBitmap(bmplist);

        }
    }

       public Spinner.OnItemSelectedListener spinner = new Spinner.OnItemSelectedListener(){


           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             //  item = new Item();
        itemm.setStarttime(position);
               if(itemm.getEndtime()<=position-1){
               itemm.setEndtime(position);
               end_time.setSelection((int) (itemm.getEndtime()));
               }
             //  Toast.makeText(Note.this, "您選擇"+parent.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
               itemm.setStarttime(itemm.getStarttime());
           }
       };
    public Spinner.OnItemSelectedListener spinner2 = new Spinner.OnItemSelectedListener(){


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            itemm.setEndtime(position);
            if(itemm.getStarttime()>=position+1){
                itemm.setStarttime(position);
                start_time.setSelection((int) (itemm.getStarttime()));

            }
            //  Toast.makeText(Note.this, "您選擇"+parent.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            itemm.setEndtime(itemm.getEndtime());
        }
    };


    public void clickPicture(View view) {
        Intent intent = new Intent(this, PictureActivity.class);
       /* ppp = (ImageView)findViewById(R.id.picture_view);
        if(item.getFileName()!=null&& item.getFileName().length() > 0){
       //setbitmap(item.getFileName());
            byte bytes[] = Base64.decode(item.getFileName(), Base64.DEFAULT);

            //用BitmapFactory生成bitmap
            Bitmap bmplist = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ppp.setImageBitmap(bmplist);
        }
        else{
            //setbitmap(fileName);
            byte bytes[] = Base64.decode(fileName, Base64.DEFAULT);

            //用BitmapFactory生成bitmap
            Bitmap bmplist = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ppp.setImageBitmap(bmplist);

        }*/





        // 設定圖片檔案名稱
        if(itemm.getFileName()!=null&& itemm.getFileName().length() > 0) {
            intent.putExtra("pictureName", itemm.getFileName());
        }
        else{
            intent.putExtra("pictureName", fileName);
        }
        // 如果裝置的版本是LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, picture, "picture");
            startActivity(intent, options.toBundle());
        }
        else {
            startActivity(intent);
        }
    }

    private void setbitmap(String filenam) {


    }

    public static Colors getColors(int color) {

        Colors result = Colors.WHITE;

        if (color == Colors.BLUE.parseColor()) {
            result = Colors.BLUE;
        }
        else if (color == Colors.PURPLE.parseColor()) {
            result = Colors.PURPLE;
        }
        else if (color == Colors.GREEN.parseColor()) {
            result = Colors.GREEN;
        }
        else if (color == Colors.ORANGE.parseColor()) {
            result = Colors.ORANGE;
        }
        else if (color == Colors.RED.parseColor()) {
            result = Colors.RED;
        }

        return result;

    }
    private void processViews() {
        title_text = (EditText) findViewById(R.id.title_text);
        content_text = (EditText) findViewById(R.id.content_text);
        picture = (ImageView) findViewById(R.id.picture);
        start_time = (Spinner)findViewById(R.id.start_time);
option = (ImageButton)findViewById(R.id.option);
        save=(Button)findViewById(R.id.ok_item);
        end_time = (Spinner)findViewById(R.id.end_time);
        color_select = (Button)findViewById(R.id.select_color);
        up_ = (LinearLayout)findViewById(R.id.test);
    }

    // 點擊確定與取消按鈕都會呼叫這個方法
    public void onSubmit(View view) {
        // 確定按鈕
        if (view.getId() == R.id.ok_item) {
            // 讀取使用者輸入的標題與內容
            String titleText = title_text.getText().toString();
            String contentText = content_text.getText().toString();

// 設定記事物件的標題與內容
            itemm.setTitle(titleText);
            itemm.setContent(contentText);

            // 如果是修改記事
            if (getIntent().getAction().equals(
                    ".EDIT_ITEM")) {
                itemm.setLastModify(new Date().getTime());
            }
            // 新增記事
            else {
                itemm.setDatetime(new Date().getTime());
            }
            // 取得回傳資料用的Intent物件
            Intent result = getIntent();
            // 設定everything
            result.putExtra(".Item", itemm);


            // 設定回應結果為確定
            setResult(Activity.RESULT_OK, result);
        }

        // 結束
        finish();
    }
  /*  private File configFileName(String prefix, String extension) {
        // 如果記事資料已經有檔案名稱
        if (item.getFileName() != null && item.getFileName().length() > 0) {
            fileName = item.getFileName();
        }
        // 產生檔案名稱
        else {
            fileName = FileUtil.getUniqueFileName();
        }

        return new File(FileUtil.getExternalStorageDir(FileUtil.APP_DIR),
                prefix + fileName + extension);
    }*/

    //按鈕
  public void call_button() {


  }

    // 以後需要擴充的功能

    public void clickFunction(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.option:
                call_button();
                break;
            //case R.id.set_delete:
            case R.id.camar:
                /*
                // 啟動相機元件用的Intent物件
                Intent intentCamera =
                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // 照片檔案名稱
                File pictureFile = configFileName("P", ".jpg");
                Uri uri = Uri.fromFile(pictureFile);
                // 設定檔案名稱
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 啟動相機元件
                startActivityForResult(intentCamera, START_CAMERA);*/


                break;
            //  break;
           // case R.id.set_alarm:

               // break;
            case R.id.select_color:
                // 啟動設定顏色的Activity元件
                startActivityForResult(
                        new Intent(this, ColorActivity.class), START_COLOR);
                break;
        }

    }
    private void processSetAlarm() {
        Calendar calendar = Calendar.getInstance();

        if (itemm.getAlarmDatetime() != 0) {
            // 設定為已經儲存的提醒日期時間
            calendar.setTimeInMillis(itemm.getAlarmDatetime());
        }

        // 讀取年、月、日、時、分
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        // 儲存設定的提醒日期時間
        final Calendar alarm = Calendar.getInstance();

        // 設定提醒時間
        TimePickerDialog.OnTimeSetListener timeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {
                        alarm.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        alarm.set(Calendar.MINUTE, minute);
                        alarm.set(Calendar.SECOND,0);
                        itemm.setAlarmDatetime(alarm.getTimeInMillis());
                    }
                };

        // 選擇時間對話框
        final TimePickerDialog tpd = new TimePickerDialog(
                this, timeSetListener, hour, minute, true);

        // 設定提醒日期
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year,
                                          int monthOfYear,
                                          int dayOfMonth) {
                        alarm.set(Calendar.YEAR, year);
                        alarm.set(Calendar.MONTH, monthOfYear);
                        alarm.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // 繼續選擇提醒時間
                        tpd.show();
                    }
                };

        // 建立與顯示選擇日期對話框
        final DatePickerDialog dpd = new DatePickerDialog(
                this, dateSetListener, year, month, day);
        dpd.show();
    }

}
