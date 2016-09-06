package com.example.salah.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;


public class MainAttractionsActivity extends ActionBarActivity {

    private ArrayAdapter<String> cityAdapter=null;
    private ArrayAdapter<String> regionAdapter=null;
    Spinner mSpnregion1,mSpnregion2;
    CheckBox mChk_view,mChk_fun,mChk_art,mChk_foods,mChk_restaurant,mChk_veg,mChk_nm,mChk_shop,mChk_park,mChk_happy,mChk_hala,mChk_rest;
    EditText et_attractions_name;
    Button viewBtnOK;
    String attractions_class="",attractions_city,attractions_region,attractions_name;
    private Context context;

    //-----------景點查詢------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_attractions);

        context = this;
        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.region_list_Taiwan));
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//預設文字
        regionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.region_list));
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//預設文字

        et_attractions_name = (EditText)findViewById(R.id.editText_attractions);
        mSpnregion1 = (Spinner)findViewById(R.id.spinner);
        mSpnregion1.setAdapter(cityAdapter);
        mSpnregion1.setOnItemSelectedListener(spnRegionOnItemSelected1);
        mSpnregion2 = (Spinner)findViewById(R.id.spinner2);
        mSpnregion2.setAdapter(regionAdapter);
        mSpnregion2.setOnItemSelectedListener(spnRegionOnItemSelected2);
        mChk_fun = (CheckBox)findViewById(R.id.checkBox1_fun);
        mChk_view = (CheckBox)findViewById(R.id.checkBox2_view);
        mChk_art = (CheckBox)findViewById(R.id.checkBox3_art);
        mChk_foods = (CheckBox)findViewById(R.id.checkBox4_foods);
        mChk_restaurant = (CheckBox)findViewById(R.id.checkBox5_restaurant);
        mChk_veg = (CheckBox)findViewById(R.id.checkBox6_vegetarian);
        mChk_nm = (CheckBox)findViewById(R.id.checkBox7_nightmarket);
        mChk_shop = (CheckBox)findViewById(R.id.checkBox8_shopping);
        mChk_park = (CheckBox)findViewById(R.id.checkBox9_park);
        mChk_happy = (CheckBox)findViewById(R.id.checkBox10_happy);
        mChk_hala = (CheckBox)findViewById(R.id.checkBox11_hala);
        mChk_rest = (CheckBox)findViewById(R.id.checkBox12_rest);
        viewBtnOK = (Button)findViewById(R.id.button_viewOK);

        viewBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attractions_class = "";
                if (mChk_fun.isChecked()) {
                    attractions_class += mChk_fun.getText();
                }
                if (mChk_view.isChecked()){
                    attractions_class += mChk_view.getText();
                }
                if(mChk_art.isChecked()) {
                    attractions_class += mChk_art.getText();
                }
                if(mChk_happy.isChecked()) {
                    attractions_class += mChk_happy.getText() ;
                }
                if(mChk_foods.isChecked()) {
                    attractions_class += mChk_foods.getText();
                }
                if(mChk_restaurant.isChecked()) {
                    attractions_class += mChk_restaurant.getText();
                }
                if(mChk_veg.isChecked()) {
                    attractions_class += mChk_veg.getText();
                }
                if(mChk_hala.isChecked()) {
                    attractions_class += mChk_hala.getText();
                }
                if(mChk_nm.isChecked()) {
                    attractions_class += mChk_nm.getText();
                }
                if(mChk_shop.isChecked()) {
                    attractions_class += mChk_shop.getText();
                }
                if(mChk_park.isChecked()) {
                    attractions_class += mChk_park.getText();
                }
                if(mChk_rest.isChecked()) {
                    attractions_class += mChk_rest.getText();
                }
                attractions_name = et_attractions_name.getText().toString();
                if("請選擇".equals(attractions_city)) {
                    attractions_city = "";
                    attractions_region = "";
                }
                else if("請選擇".equals(attractions_region)) {
                    attractions_region = "";
                }
                if( "".equals(attractions_name) && attractions_city == "" && attractions_class == ""){
                    new AlertDialog.Builder(MainAttractionsActivity.this)
                            .setTitle("確認視窗")
                            .setMessage("\"您沒有選取的任何項目\",\n請重新確認查詢資料?")
                            .setIcon(R.mipmap.logo)
                            .setPositiveButton("關閉",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub
                                        }
                                    }).show();
                }
                else {
                    if("".equals(attractions_class)) {
                        attractions_class = "";
                    }
                    new AlertDialog.Builder(MainAttractionsActivity.this)
                            .setTitle("確認視窗")
                            .setMessage("這些是您選取的項目嗎?\n"+attractions_name+"\n地區：" + attractions_city + attractions_region + "\n\n類別：" + attractions_class)
                            .setIcon(R.mipmap.logo)
                            .setPositiveButton("確定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent = new Intent();
                                            intent.setClass(MainAttractionsActivity.this, DataShowActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name",attractions_name);
                                            bundle.putString("region",attractions_city + attractions_region);
                                            bundle.putString("class",attractions_class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub
                                            attractions_class="";
                                        }
                                    }).show();
                }
            }
        });

    }//-----------景點查詢------------

    //------------景點查詢------------下拉式選單------------
    public AdapterView.OnItemSelectedListener spnRegionOnItemSelected1 = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View v,int position, long id) {
            int pos = mSpnregion1.getSelectedItemPosition();
            String[][] type ={getResources().getStringArray(R.array.region_list),
                    getResources().getStringArray(R.array.region_list_Keelung),
                    getResources().getStringArray(R.array.region_list_Taipei),
                    getResources().getStringArray(R.array.region_list_NewTaipei),
                    getResources().getStringArray(R.array.region_list_Taoyuan),
                    getResources().getStringArray(R.array.region_list_Hsinchu),
                    getResources().getStringArray(R.array.region_list_Hsinchu_C),
                    getResources().getStringArray(R.array.region_list_Miaoli),
                    getResources().getStringArray(R.array.region_list_Taichung),
                    getResources().getStringArray(R.array.region_list_Changhua),
                    getResources().getStringArray(R.array.region_list_Nantou),
                    getResources().getStringArray(R.array.region_list_yunlin),
                    getResources().getStringArray(R.array.region_list_Chiayi),
                    getResources().getStringArray(R.array.region_list_Chiayi_C),
                    getResources().getStringArray(R.array.region_list_Tainan),
                    getResources().getStringArray(R.array.region_list_Kaohsiung),
                    getResources().getStringArray(R.array.region_list_Pingtung),
                    getResources().getStringArray(R.array.region_list_Yilan),
                    getResources().getStringArray(R.array.region_list_Hualien),
                    getResources().getStringArray(R.array.region_list_Taitung),
                    getResources().getStringArray(R.array.region_list_Penghu),
                    getResources().getStringArray(R.array.region_list_Kinmen),
                    getResources().getStringArray(R.array.region_list_Lienchiang)};
            regionAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, type[pos]);
            regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnregion2.setAdapter(regionAdapter);
            attractions_city = parent.getSelectedItem().toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }};
    public AdapterView.OnItemSelectedListener spnRegionOnItemSelected2 = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View v,int position, long id) {
            attractions_region = parent.getSelectedItem().toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        } };
//------------景點查詢------------下拉式選單------------
// 攔截返回鍵
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub

    if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
        Intent intent = new Intent();
        intent.setClass(MainAttractionsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    return true;
}
    //  攔截返回鍵
}

