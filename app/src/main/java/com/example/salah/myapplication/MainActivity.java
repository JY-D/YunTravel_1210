package com.example.salah.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity{

    ViewPager mViewPager;
    ArrayList<View> viewList;

    //以下資料庫宣告區 記得import
    private GetAllSpotsListAdapter spotItemAdapter;
    private NewsList spotItemAdapter_news;
    protected static final int Menu_Delete = 2;
    protected static final int Menu_Add = Menu.FIRST;
    protected static final int MENU_LIST1 = 3;
    protected static final int MENU_LIST2 = 4;
    public static final int numberOfUserList = 3;
    private ListView getAllTitlesList;
    private Button addSpot;
    private JSONArray jsonArray;
    protected JSONObject spotClicked_d;
    protected JSONObject spotClicked;
    protected JSONObject spotClicked_allspots;
    protected int dId = 0;
    private ProgressDialog pDialog;
    private boolean lock = true;

    //以上資料庫宣告區

    private SharedPreferences userData;
    private static final String data = "DATA";
    private static final String nameField = "123";
    private static final String passwordField = "456";
    private static final String login_per ="";
    boolean login = false;
    private String Schedule_nameID,ScheduleID;
    String login_name="";


    private ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    private Item itemm;
    private ListView item_list;
    private ItemAdapter itemAdapter;
    private List<Item> items;

    // 選單項目物件
    private MenuItem add_item,delete_item,revert_item,friends,logout;
    // 已選擇項目數量
    private int selectedCount = 0;

    TextView  tv2, tv4,textView_hot_news;
    EditText editText_usernam,editText_password;
    ImageView weather;
    Button mainSchedule;
    ListView getAllNewsList;
    SatelliteMenu menu;
    private ItemDAO itemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        // ------------帳號登入頁面------------
        editText_usernam = (EditText) findViewById(R.id.editText1);
        editText_password = (EditText) findViewById(R.id.editText2);
        Button mButton = (Button) findViewById(R.id.button1);
        Button m2Button = (Button) findViewById(R.id.button2);

        editText_usernam.setText(nameField);
        editText_password.setText(passwordField);
        readData();
        if("123".equals(userData.getString(nameField, "")) && "456".equals(userData.getString(passwordField, "")) )  {
            login = true;
            login_name = userData.getString(login_per, "");
            jumpTogamecontext_main();
        }
        if("159".equals(userData.getString(nameField, "")) && "753".equals(userData.getString(passwordField, "")) )  {
            login = true;
            login_name = userData.getString(login_per, "");
            jumpTogamecontext_main();
        }
        if("852".equals(userData.getString(nameField, "")) && "789".equals(userData.getString(passwordField, "")) )  {
            login = true;
            login_name = userData.getString(login_per, "");
            jumpTogamecontext_main();
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                if("123".equals(userData.getString(nameField, "")) && "456".equals(userData.getString(passwordField, "")) ) {
                    jumpTogamecontext_main();
                    login = true;
                    add_item.setVisible(selectedCount == 0 && login==true);
                    friends.setVisible( login == true);
                    logout.setVisible(login == true);
                    login_name="salah";
                    saveData();
                    Toast.makeText(getApplicationContext(), "salah,登入成功!",
                            Toast.LENGTH_SHORT).show();
                }
                else if("159".equals(userData.getString(nameField, "")) && "753".equals(userData.getString(passwordField, "")) ) {
                    jumpTogamecontext_main();
                    login = true;
                    add_item.setVisible(selectedCount == 0 && login==true);
                    friends.setVisible( login == true);
                    logout.setVisible( login == true);
                    login_name="睿恩";
                    saveData();
                    Toast.makeText(getApplicationContext(), "睿恩,登入成功!",
                            Toast.LENGTH_SHORT).show();
                }
                else if("852".equals(userData.getString(nameField, "")) && "789".equals(userData.getString(passwordField, "")) ) {
                    jumpTogamecontext_main();
                    login = true;
                    add_item.setVisible(selectedCount == 0 && login==true);
                    friends.setVisible( login == true);
                    logout.setVisible( login == true);
                    login_name= "董哥";
                    saveData();
                    Toast.makeText(getApplicationContext(), "董哥,登入成功!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    editText_usernam.setBackgroundColor(Color.parseColor("#ffff5e60"));
                    editText_password.setBackgroundColor(Color.parseColor("#ffff5e60"));
                    Toast.makeText(getApplicationContext(), "帳號或密碼錯誤",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        m2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_usernam.setText("");
                editText_password.setText("");
                editText_usernam.setBackgroundColor(Color.WHITE);
                editText_password.setBackgroundColor(Color.WHITE);
            }
        });


    }
    public void readData(){
        userData = getSharedPreferences(data,0);
        userData.getString(login_per, "");
        userData.getString(nameField, "");
        userData.getString(passwordField, "");
    }
    public void saveData(){
        userData = getSharedPreferences(data,0);
        userData.edit()
                .putString(login_per, login_name)
                .putString(nameField, editText_usernam.getText().toString())
                .putString(passwordField, editText_password.getText().toString())
                .commit();
    }
    // ------------帳號登入頁面------------
    public void jumpTogamecontext_main() {
        //------------主要滑動頁面------------
        setContentView(R.layout.activity_main);
        getLayoutInflater();
        LayoutInflater mInflater = LayoutInflater.from(this);

        View v1 = mInflater.inflate(R.layout.fragment1_main, null);
        View v2 = mInflater.inflate(R.layout.fragment2_main, null);
        View v3 = mInflater.inflate(R.layout.fragment3_main, null);

        viewList = new ArrayList<View>();
        viewList.add(v1);
        viewList.add(v2);
        viewList.add(v3);
//v1
        tv2 = (TextView) v1.findViewById(R.id.textView2);
        tv4 = (TextView) v1.findViewById(R.id.lblOutput);
        getAllTitlesList = (ListView) v1.findViewById(R.id.getAllTitlesList);
        addSpot = (Button) v1.findViewById(R.id.addSpot);
//v2
        weather = (ImageView) v2.findViewById(R.id.weatherPIC);
        getAllNewsList = (ListView) v2.findViewById(R.id.getAllNewsList);
        menu = (SatelliteMenu) v2.findViewById(R.id.menu);
        textView_hot_news =(TextView) v2.findViewById(R.id.textView_hot_news);
//v3
        item_list = (ListView) v3.findViewById(R.id.item_list);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(viewList));
        mViewPager.setCurrentItem(1);
//------------炫炮按鈕------------
        List<SatelliteMenuItem> menuitems = new ArrayList<SatelliteMenuItem>();
        menuitems.add(new SatelliteMenuItem(1, R.drawable.high_t));
        menuitems.add(new SatelliteMenuItem(2, R.drawable.trans));
        menuitems.add(new SatelliteMenuItem(3, R.drawable.day));
        menuitems.add(new SatelliteMenuItem(4, R.drawable.nearmap));
        menuitems.add(new SatelliteMenuItem(5, R.drawable.search));
//        items.add(new SatelliteMenuItem(5, R.drawable.sat_item));
        menu.addItems(menuitems);

//------------炫炮按鈕------------
        //筆記LIST

        itemDAO = new ItemDAO(getApplicationContext());
        list=itemDAO.getList();
        int layoutId = android.R.layout.simple_list_item_1;
        listAdapter = new ArrayAdapter<String>(this, layoutId, list);
        item_list.setAdapter(listAdapter);
        items = itemDAO.getAll();

        clickact();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //
        for(int i = 0; i < numberOfUserList ; i++)
        {
            int add = 3+i;
        }
        //


        //以下執行讀取資料庫 記得import

        new GetAllCustomerTask().execute(new ApiConnector());
        new GetAllCustomerTask_allspots().execute(new ApiConnector_allspots());

        this.getAllTitlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {        //點取資料庫LIST
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spotClicked = jsonArray.getJSONObject(position);

                    Intent showDetails = new Intent(getApplicationContext(), SpotDetailsActivity.class);
                    showDetails.putExtra("SpotID", spotClicked.getInt("id"));
                    startActivity(showDetails);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), "SpotId: " + spotClicked + " click",Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "SpotId: " + position + " click",Toast.LENGTH_LONG).show();
            }
        });


        this.getAllNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {        //點取資料庫LIST
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spotClicked_allspots = jsonArray.getJSONObject(position);

                    Intent showDetails = new Intent(getApplicationContext(), SpotDetailsActivity_public.class);
                    showDetails.putExtra("SpotID_public", spotClicked_allspots.getInt("id"));
                    startActivity(showDetails);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), "SpotId: " + spotClicked + " click",Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "SpotId: " + position + " click",Toast.LENGTH_LONG).show();
            }
        });


        addSpot.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddSpotActivity.class);
                startActivity(intent);
            }
        });

        //以上執行讀取資料庫

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainWeather.class);
                startActivity(intent);
            }
        });
//------------炫炮按鈕------------
        menu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {
            public void eventOccured(int id) {
                Log.i("sat", "Clicked on " + id);
                switch (id) {
                    case 1: {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, TrainSearch.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("train","http://m.thsrc.com.tw/tw/TimeTable/SearchResult");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, TrainSearch.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("train", "http://twtraffic.tra.gov.tw/twrail/mobile/home.aspx");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    }
                    case 3:{
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, Dayschedule.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case 4:{
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, MainNearMapActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case 5:{
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, MainAttractionsActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    default:
                        break;
                }
            }
        });
//------------炫炮按鈕------------
        //以下Context Menu
        registerForContextMenu(getAllTitlesList);
        registerForContextMenu(getAllNewsList);
    }//------------主要滑動頁面------------



    //以下Context Menu
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.getAllTitlesList) {
            // ListView getAllTitlesList = (ListView) v;
            //AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //Activity obj = (Activity) getAllTitlesList.getItemAtPosition(acmi.position);


            SubMenu sub1 = menu.addSubMenu(Menu.NONE, Menu_Add, 0, "新增至清單");

            for(int i = 1; i <= numberOfUserList ; i++)
                sub1.add(Menu.NONE, numberOfUserList + i, i, "自訂清單"+i); // "清單"+i+"(自己Bundle)" 要抓使用者清單名稱

            menu.add(1,Menu_Delete,0,"刪除景點");
        }
        if (v.getId() == R.id.getAllNewsList) {

            SubMenu sub1 = menu.addSubMenu(Menu.NONE, Menu_Add, 0, "新增至清單");

            for(int i = 1; i <= numberOfUserList ; i++)
                sub1.add(Menu.NONE, numberOfUserList+3+ i, i, "大眾清單"+i); // "清單"+i+"(自己Bundle)" 要抓使用者清單名稱
        }
    }


    public boolean onContextItemSelected (MenuItem itm)
    {

        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo =(AdapterView.AdapterContextMenuInfo)itm.getMenuInfo();
        //输出position
        //Toast.makeText(this,String.valueOf(menuInfo.position), Toast.LENGTH_LONG).show();

        //onOptionsItemSelected(itm);

        while(lock) {
            try {
                spotClicked_d = this.jsonArray.getJSONObject(menuInfo.position);
                dId = spotClicked_d.getInt("id");
                lock = false;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(itm.getItemId() > 3 && itm.getItemId() < 7) {
            Toast.makeText(this, "do add spot to " + itm.toString(), Toast.LENGTH_LONG).show();
            add_spot_to_list(dId, itm);
            lock = true;
        }
        else if(itm.getItemId() > 7 && itm.getItemId() < 10) {
            Toast.makeText(this, "do add spot to " + itm.toString(), Toast.LENGTH_LONG).show();
            add_spot_to_list2(dId, itm);
            lock = true;
        }
        else if (itm.getItemId() == Menu_Delete)
        {
            //Toast.makeText(this, "SpotId: " + dId + " delete", Toast.LENGTH_LONG).show();
            new AsyncTask<ApiConnector, Long, Boolean>() {
                @Override
                protected Boolean doInBackground(ApiConnector... apiConnectors) {
                    return apiConnectors[0].deleteSpot(dId,userData.getString(login_per, ""));
                }
            }.execute(new ApiConnector());

            spotItemAdapter.removeItemAt(menuInfo.position);
            lock = true;
        }
        return super.onContextItemSelected(itm);
    }
    //以上Context Menu

    //記是點及
    private void clickact() {
        AdapterView.OnItemClickListener timelistener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemm = new Item();
                String ti=list.get(position);
                itemm.setTId(ti);
                Intent intent = new Intent(                             );
                intent.putExtra("position", position);
                intent.putExtra(".Item", itemm);
                intent.setClass(MainActivity.this, com.example.salah.myapplication.List.class);
                startActivity(intent);
            }
        };
        item_list.setOnItemClickListener(timelistener);

        /////待修
        AdapterView.OnItemLongClickListener itemlonglistener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String[] dinner = {"刪除","上傳","取消"};
                itemm = new Item();
                String ti=list.get(position);
                itemm.setTId(ti);

                AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
                dialog_list.setTitle("選項");
                dialog_list.setItems(dinner, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {

                            case 0:
                                AlertDialog.Builder c1 = new AlertDialog.Builder(MainActivity.this);
                                c1.setTitle("刪除");
                                c1.setMessage("您要刪除這個行程嗎?");
                                c1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        itemDAO.delete(itemm.getTId());
                                        listAdapter.remove(itemm.getTId());

                                    }
                                });
                                c1.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                    }

                                });
                                c1.show();
                                break;
                            case 1:
                                AlertDialog.Builder c3 = new AlertDialog.Builder(MainActivity.this);
                                c3.setTitle("上傳");
                                c3.setMessage("您要上傳這個行程嗎?");
                                c3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        new getScheduleNameMAXId().execute(new ApiConnector());
                                        final  List<NameValuePair> d_params = new ArrayList<NameValuePair>();
                                        d_params.add(new BasicNameValuePair("id", Schedule_nameID));
                                        try {
                                            d_params.add(new BasicNameValuePair("name", URLEncoder.encode(itemm.getTId(),  "utf-8")));
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            d_params.add(new BasicNameValuePair("area", URLEncoder.encode("",  "utf-8")));
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        new AsyncTask<ApiConnector,Long, Boolean >() {
                                            @Override
                                            protected Boolean doInBackground(ApiConnector... apiConnectors) {
                                                return apiConnectors[0].addScheduleNameToServer(d_params, userData.getString(login_per, ""));
                                            }
                                        }.execute(new ApiConnector());


                                        String [][] data_S=itemDAO.upload(itemm.getTId());
                                        int test=data_S[0].length;
                                        new getScheduleMAXId().execute(new ApiConnector());
                                                for(int i=0;i<test;i++) {
                                                   // System.out.println("qqqq "+data_S[i][j]);
                                                    final List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                    params.add(new BasicNameValuePair("id", ScheduleID+1));
                                                    try {
                                                        params.add(new BasicNameValuePair("name", URLEncoder.encode(itemm.getTId(), "utf-8")));
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        params.add(new BasicNameValuePair("title", URLEncoder.encode(data_S[0][i], "utf-8")));
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        params.add(new BasicNameValuePair("start", URLEncoder.encode(data_S[2][i], "utf-8")));
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        params.add(new BasicNameValuePair("end", URLEncoder.encode(data_S[3][i], "utf-8")));
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        params.add(new BasicNameValuePair("contents", URLEncoder.encode(data_S[1][i], "utf-8")));
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        params.add(new BasicNameValuePair("pic", URLEncoder.encode(data_S[4][i], "utf-8")));
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    new AsyncTask<ApiConnector, Long, Boolean>() {
                                                        @Override
                                                        protected Boolean doInBackground(ApiConnector... apiConnectors) {
                                                            return apiConnectors[0].addScheduleToServer(params, userData.getString(login_per, ""));
                                                        }
                                                    }.execute(new ApiConnector());
                                                }
                                    }
                                });
                                c3.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                    }

                                });
                                c3.show();

                                break;
                            case 2:
                                AlertDialog.Builder c2 = new AlertDialog.Builder(MainActivity.this);
                                c2.setTitle("取消");
                                c2.setMessage("別再按到了");
                                c2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });
                                c2.show();
                                break;

                        }//which
                    }//onclick
                });
                dialog_list.show();



                // 讀取選擇的記事物件
                //Item item = items.get(position);
                // 處理是否顯示已選擇項目
                // 重新設定記事項目
                //itemAdapter.set(position, item);
                return true;
            }
        };
        item_list.setOnItemLongClickListener(itemlonglistener);
    }

    // 處理是否顯示已選擇項目
    private void processMenu(Item item) {
        // 如果需要設定記事項目
        if (item != null) {
            // 設定已勾選的狀態
            item.setSelected(!item.isSelected());

            // 計算已勾選數量
            if (item.isSelected()) {
                selectedCount++;
            }
            else {
                selectedCount--;
            }
        }

        // 根據選擇的狀況，設定是否顯示選單項目
        add_item.setVisible(selectedCount == 0 && login==true);
        friends.setVisible(login == true);
        logout.setVisible(login == true);
        revert_item.setVisible(selectedCount > 0);
        delete_item.setVisible(selectedCount > 0);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        // 取得選單項目物件
        add_item = menu.findItem(R.id.add_item);
        revert_item = menu.findItem(R.id.revert_item);
        delete_item = menu.findItem(R.id.delete_item);
        friends = menu.findItem(R.id.friends);
        logout = menu.findItem(R.id.logout);
        // 設定選單項目
        processMenu(null);
        return true;
    }
    //新增行成
    public void Menuclick(final MenuItem item){
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.add_item:
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View v = inflater.inflate(R.layout.single_new, null);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("請輸入行程名稱")
                        .setView(v)
                        .setIcon(R.mipmap.logo)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) (v.findViewById(R.id.list_new));
                                String titleText = editText.getText().toString();
                                itemm = new Item();
                                if(titleText.equals("")|| titleText.substring(0).equals(" ")) {
                                    AlertDialog.Builder space = new AlertDialog.Builder(MainActivity.this);
                                    space.setTitle("Error");
                                    space.setMessage("行程名稱無法是空白");
                                    space.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            // TODO Auto-generated method stub
                                            Toast.makeText(MainActivity.this, "行程名稱無效", Toast.LENGTH_SHORT).show();
                                        }

                                    });
                                    space.show();

                                }
                                else if(itemDAO.getCount(titleText)!=0){
                                    AlertDialog.Builder same = new AlertDialog.Builder(MainActivity.this);
                                    same.setTitle("Error");
                                    same.setMessage("您已經有相同名稱的行程");
                                    same.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            // TODO Auto-generated method stub
                                            Toast.makeText(MainActivity.this, "您已經有相同名稱的行程", Toast.LENGTH_SHORT).show();
                                        }

                                    });
                                    same.show();

                                }
                                else {

                                    list.add(titleText);
                                    listAdapter.notifyDataSetChanged();
                                    itemm.setTId(titleText);
                                    if (itemDAO.getCount(itemm.getTId()) == 0) {
                                        itemDAO.sample(itemm.getTId());
                                    }
                                }
                            }
                        }).show();

                break;
        /*    case R.id.revert_item:
                for (int i = 0; i < itemAdapter.getCount(); i++) {
                    Item ri = itemAdapter.getItem(i);
                    if (ri.isSelected()) {
                        ri.setSelected(false);
                        itemAdapter.set(i, ri);
                    }
                }
                selectedCount = 0;
                processMenu(null);
                break;*/
            case R.id.delete_item:
                // 沒有選擇
                if (selectedCount == 0) {
                    break;
                }
                // 建立與顯示詢問是否刪除的對話框
                AlertDialog.Builder d = new AlertDialog.Builder(this);
                String message = getString(R.string.delete_item);
                d.setTitle(R.string.delete)
                        .setMessage(String.format(message, selectedCount));
                d.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 刪除所有已勾選的項目
                                int index = itemAdapter.getCount() - 1;

                                while (index > -1) {
                                    Item item = itemAdapter.get(index);

                                    if (item.isSelected()) {
                                        itemAdapter.remove(item);
                                        itemDAO.delete(item.getTId());
                                    }
                                    index--;
                                }
                                // 通知資料改變
                                itemAdapter.notifyDataSetChanged();
                                selectedCount = 0;
                                processMenu(null);
                            }
                        });
                d.setNegativeButton(android.R.string.no, null);
                d.show();
                break;
            case R.id.friends:{
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Friends_list.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.logout: {
                login = false;
                userData = getSharedPreferences(data,0);
                userData.edit()
                        .putString(login_per, "")
                        .putString(nameField,"" )
                        .putString(passwordField,"")
                        .commit();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    }

        /*@Override
    protected void onStop() {
        super.onStop();
        db.close(); // 關閉資料庫
    }*/

    //資料庫
 /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_insert) { // 新增
            db.execSQL("INSERT INTO " + DATABASE_TABLE + " (" +
                    "title, longitude, latitude) VALUES ('" + et1.getText().toString() +
                    "', " + et2.getText().toString() + ", " + et3.getText().toString() + " )");
            tv4.setText("新增記錄成功...");
            return true;
        }
        if (id == R.id.action_update) {  // 更新
            db.execSQL("UPDATE " + DATABASE_TABLE + " SET longitude =" +
                    et2.getText().toString() + ",  latitude =" +  et3.getText().toString() + " WHERE title='" +
                    et1.getText().toString() + "'");
            tv4.setText("更新記錄成功...");
            return true;
        }
        if (id == R.id.action_delete) {  // 刪除
            db.execSQL("DELETE FROM " + DATABASE_TABLE + " WHERE title='" +
                    et1.getText().toString() + "'");
           tv4.setText("刪除記錄成功...");
            return true;
        }
        if (id == R.id.action_queryAll) {  // 顯示全部記錄
            String[] colNames;
            String str = "";
            Cursor c = db.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
            colNames = c.getColumnNames();
            // 顯示欄位名稱
            for (int i = 0; i < colNames.length; i++)
                str += colNames[i] + "\t\t";
            str += "\n";
            c.moveToFirst();  // 第1筆
            // 顯示欄位值
            for (int i = 0; i < c.getCount(); i++) {
                str += c.getString(0) + "\t\t";
                str += c.getString(1) + "\t\t";
                str += c.getString(2) + "\t\t";
                str += c.getString(3) + "\n";
                c.moveToNext();  // 下一筆
            }
           tv4.setText(str.toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
    //資料庫


    public void setListAdapter(JSONArray jsonArray)                                   //以下database list class
    {
        this.jsonArray = jsonArray;
        spotItemAdapter = new GetAllSpotsListAdapter (jsonArray, this);
        this.getAllTitlesList.setAdapter(spotItemAdapter);
    }

    public void setListAdapter2(JSONArray jsonArray)                                   //以下database list class
    {
        this.jsonArray = jsonArray;
        spotItemAdapter_news = new NewsList (jsonArray, this);
        this.getAllNewsList.setAdapter(spotItemAdapter_news);
    }

    private class GetAllCustomerTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            do {
                login_name = userData.getString(login_per, "");
            }while(login_name=="");
            return params[0].GetAllCustomers(login_name);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            //setTextToTextView(jsonArray);
            if(jsonArray == null)
            {
                tv4.setText/*("Can not connect to DataBase, Please check your network.");*/(login_name);
            }
            else {
                tv4.setText("");
                setListAdapter(jsonArray);
            }
        }
    }

    private class GetAllCustomerTask_allspots extends AsyncTask<ApiConnector_allspots, Long, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector_allspots... params_allspots) {
            return params_allspots[0].GetAllCustomers_allspots();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            //setTextToTextView(jsonArray);
            if(jsonArray == null)
            {
                textView_hot_news.setText("Can not connect to DataBase!");
            }
            else {
                setListAdapter2(jsonArray);
            }
        }
    }

    private class getScheduleMAXId extends AsyncTask<ApiConnector, Long, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(ApiConnector... S_params) {
            return S_params[0].GetScheduleMaxId(userData.getString(login_per, ""));
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //JSONObject try
            try {
                //if(jsonArray == null)
                if(jsonObject == null)
                {
                    pDialog.dismiss();
                }
                else {
                    //Math.max.apply(Math,jsonArray.map(function(o){return o.y;}))
                    //SpotID = jsonArray.getString("id");
                    //JSONObject max_id = jsonArray.getJSONObject(0);
                    JSONObject max_id = jsonObject;
                    ScheduleID = String.valueOf(Integer.parseInt(max_id.getString("MAX(id)")) + 1);
                    //Title.setText(SpotID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class getScheduleNameMAXId extends AsyncTask<ApiConnector, Long, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(ApiConnector... params) {
            return params[0].GetScheduleNameMaxId(userData.getString(login_per, ""));
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
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
                    pDialog.dismiss();
                }
                else {
                    //Math.max.apply(Math,jsonArray.map(function(o){return o.y;}))
                    //SpotID = jsonArray.getString("id");
                    //JSONObject max_id = jsonArray.getJSONObject(0);
                    JSONObject max_id = jsonObject;
                    Schedule_nameID = String.valueOf(Integer.parseInt(max_id.getString("MAX(id)")) + 1);
                    //Title.setText(SpotID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();
        }
    }

    public void add_spot_to_list(int id , MenuItem itm)
    {
        final  List<NameValuePair> l_params = new ArrayList<NameValuePair>();

        l_params.add(new BasicNameValuePair("id", String.valueOf(id)));
        try {
            l_params.add(new BasicNameValuePair("list", URLEncoder.encode(itm.getTitle().toString(), "utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new AsyncTask<ApiConnector,Long, Boolean >() {

            @Override
            protected Boolean doInBackground(ApiConnector... apiConnectors) {
                return apiConnectors[0].addSpotToList(l_params,userData.getString(login_per, ""));
            }

            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(MainActivity.this);
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
    }

    public void add_spot_to_list2(int id , MenuItem itm)
    {
        final  List<NameValuePair> l_params = new ArrayList<NameValuePair>();

        l_params.add(new BasicNameValuePair("id", String.valueOf(id)));
        try {
            l_params.add(new BasicNameValuePair("list", URLEncoder.encode(itm.getTitle().toString(), "utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new AsyncTask<ApiConnector_allspots,Long, Boolean >() {

            @Override
            protected Boolean doInBackground(ApiConnector_allspots... apiConnectors) {
                return apiConnectors[0].addSpotToList(l_params);
            }

            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(MainActivity.this);
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

        }.execute(new ApiConnector_allspots());
    }
    //以上database list class

    // 攔截返回鍵
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setIcon(R.mipmap.logo)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
        }
        return true;
    }
    //  攔截返回鍵
}
