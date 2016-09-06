package com.example.salah.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class MainFoodsActivity extends ActionBarActivity{

    private ArrayAdapter<String> cityAdapter=null;
    private ArrayAdapter<String> regionAdapter=null;
    Spinner mSpnregion1,mSpnregion2;
    CheckBox mChk_ch,mChk_tw,mChk_jp,mChk_ep,mChk_sa,mChk_big,mChk_break,mChk_tea,mChk_v,mChk_small,mChk_fire,mChk_hala;
    EditText et_foods_name;
    Button foodsBtnOK;
    String foods_class,foods_city,foods_region,foods_name;
    private Context context;
    //-----------美食查詢------------
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_foods);

        context = this;
        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.region_list_Taiwan));
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//預設文字
        regionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.region_list));
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//預設文字

        et_foods_name =(EditText)findViewById(R.id.editText_foods) ;
        mSpnregion1 = (Spinner)findViewById(R.id.spinner_1);
        mSpnregion1.setAdapter(cityAdapter);
        mSpnregion1.setOnItemSelectedListener(spnRegionOnItemSelected1);
        mSpnregion2 = (Spinner)findViewById(R.id.spinner_2);
        mSpnregion2.setAdapter(regionAdapter);
        mSpnregion2.setOnItemSelectedListener(spnRegionOnItemSelected2);
        mChk_ch = (CheckBox)findViewById(R.id.checkBox1_ch);
        mChk_tw = (CheckBox)findViewById(R.id.checkBox2_tw);
        mChk_jp = (CheckBox)findViewById(R.id.checkBox3_jp);
        mChk_ep = (CheckBox)findViewById(R.id.checkBox4_ep);
        mChk_sa = (CheckBox)findViewById(R.id.checkBox5_sa);
        mChk_big = (CheckBox)findViewById(R.id.checkBox6_big);
        mChk_break = (CheckBox)findViewById(R.id.checkBox7_break);
        mChk_tea = (CheckBox)findViewById(R.id.checkBox8_tea);
        mChk_v = (CheckBox)findViewById(R.id.checkBox9_v);
        mChk_small = (CheckBox)findViewById(R.id.checkBox10_nm);
        mChk_fire = (CheckBox)findViewById(R.id.checkBox11_fire);
        mChk_hala = (CheckBox)findViewById(R.id.checkBox12_hala);
        foodsBtnOK = (Button)findViewById(R.id.button_foodsOK);

        foodsBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foods_class = "";
                if(mChk_ch.isChecked())
                    foods_class += mChk_ch.getText() + "、";
                if(mChk_tw.isChecked())
                    foods_class += mChk_tw.getText() + "、";
                if(mChk_jp.isChecked())
                    foods_class += mChk_jp.getText() + "、";
                if(mChk_small.isChecked())
                    foods_class += mChk_small.getText() + "、";
                if(mChk_ep.isChecked())
                    foods_class += mChk_ep.getText() + "、";
                if(mChk_sa.isChecked())
                    foods_class += mChk_sa.getText() + "、";
                if(mChk_big.isChecked())
                    foods_class +=mChk_big.getText() +  "、";
                if(mChk_break.isChecked())
                    foods_class += mChk_break.getText() + "、";
                if(mChk_tea.isChecked())
                    foods_class += mChk_tea.getText() + "、";
                if(mChk_v.isChecked())
                    foods_class += mChk_v.getText() + "、";
                if(mChk_hala.isChecked())
                    foods_class += mChk_hala.getText() + "、";
                if(mChk_fire.isChecked())
                    foods_class += mChk_fire.getText() + "、";
                foods_name = et_foods_name.getText().toString();
                if("請選擇".equals(foods_city)) {
                    foods_city = "";
                    foods_region = "無";
                }
                else if("請選擇".equals(foods_region)) {
                    foods_region = "";
                }
                if("".equals(foods_name) && foods_city == "" && foods_class == ""){
                    new AlertDialog.Builder(MainFoodsActivity.this)
                            .setTitle("確認視窗")
                            .setMessage("\"您沒有選取的任何項目\",\n請重新確認查詢資料?")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("是",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub
                                        }
                                    })
                            .setNegativeButton("否",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub
                                        }
                                    }).show();
                }
                else {
                    if("".equals(foods_class)) {
                        foods_class = "無";
                    }
                    new AlertDialog.Builder(MainFoodsActivity.this)
                            .setTitle("確認視窗")
                            .setMessage("這些是您選取的項目嗎?\n"+foods_name+"\n地區：" + foods_city + foods_region + "\n\n類別：" + foods_class)
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("確定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent = new Intent();
                                            intent.setClass(MainFoodsActivity.this, DataShowActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name",foods_name);
                                            bundle.putString("region",foods_city+foods_region);
                                            bundle.putString("class",foods_class);
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
                                        }
                                    }).show();
                }

            }
        });
    }//-----------美食查詢------------

    //------------美食查詢------------下拉式選單------------
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
            foods_city = parent.getSelectedItem().toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }};
    public AdapterView.OnItemSelectedListener spnRegionOnItemSelected2 = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View v,int position, long id) {
            foods_region = parent.getSelectedItem().toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        } };
    //------------美食查詢------------下拉式選單------------

}
