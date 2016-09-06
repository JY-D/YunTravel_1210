package com.example.salah.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 資料功能類別
public class ItemDAO {
    // 表格名稱
    public static final String TABLE_NAME = "item";
    public static final String KEY_ID = "_id";
    public static final String DATETIME_COLUMN = "datetime";
    public static final String TITLE_COLUMN = "title";
    public static final String CONTENT_COLUMN = "content";
    public static final String COLOR_COLUMN = "color";
    public static final String LASTMODIFY_COLUMN = "lastmodify";
    public static final String PITCURE_FILE = "filename";
    public static final String DATE = "date";
    public static final String START_COLUMN = "starttime";
    public static final String END_COLUMN = "endtime";
    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DATE + " TEXT NOT NULL, " +
                    DATETIME_COLUMN + " INTEGER NOT NULL, " +
                    TITLE_COLUMN + " TEXT NOT NULL, " +
                    CONTENT_COLUMN + " TEXT NOT NULL, " +
                    LASTMODIFY_COLUMN + " INTEGER," +
                    PITCURE_FILE + " TEXT , " +
                    START_COLUMN + " INTEGER NOT NULL, " +
                    END_COLUMN + " INTEGER NOT NULL, " +
                    COLOR_COLUMN + " INTEGER NOT NULL )";


    private SQLiteDatabase db;
    public ItemDAO(Context context) {
        db = MyDBHelper.getDatabase(context);
    }

    public void close() {
        db.close();
    }

    public Item insert(Item item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(DATE,item.getTId());
        cv.put(DATETIME_COLUMN, item.getDatetime());
        cv.put(TITLE_COLUMN, item.getTitle());
        cv.put(CONTENT_COLUMN, item.getContent());
        cv.put(LASTMODIFY_COLUMN, item.getLastModify());
        cv.put(PITCURE_FILE,item.getFileName());
        cv.put(START_COLUMN,item.getStarttime());
        cv.put(END_COLUMN,item.getEndtime());
        cv.put(COLOR_COLUMN, item.getColor().parseColor());
        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);


        item.setId(id);
        return item;
    }

    // 修改參數指定的物件
    public boolean update(Item item) {
int count =0 ;
        ContentValues cv = new ContentValues();
        //cv.put(KEY_ID,item.getId());
        cv.put(DATETIME_COLUMN, item.getDatetime());
        cv.put(TITLE_COLUMN, item.getTitle());
        cv.put(CONTENT_COLUMN, item.getContent());
        cv.put(LASTMODIFY_COLUMN, item.getLastModify());
        cv.put(PITCURE_FILE, item.getFileName());
        cv.put(START_COLUMN, item.getStarttime());
        cv.put(END_COLUMN, item.getEndtime());
        cv.put(COLOR_COLUMN, item.getColor().parseColor());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = DATE + "=" + item.getTId() + "AND" + START_COLUMN + "=" + item.getStarttime();

        long bug,position=   (item.getStarttime())-(item.getId()%24)+1;

        // 執行修改資料並回傳修改的資料數量是否成功
        count =(int)( item.getEndtime() - item.getStarttime() );
        int fuck = (int)(position+item.getId());
        String  bb ;
            for(;count>=0;count--){
                bb = String.valueOf(fuck);
               // System.out.println("test "+bb);
                db.update(TABLE_NAME, cv, " _id=?  ", new String[]{bb });
                fuck++;



            }
Item a=get(fuck-1),b=get(fuck);
if(a.getEndtime()+1>b.getStarttime()){
    System.out.println("a "+a.getEndtime());
    System.out.println("ab "+b.getStarttime());
    set(b);
}

            return true;
    }
//修改後刪除
    public boolean del(Item item){
        ContentValues cv = new ContentValues();
        cv.put(DATETIME_COLUMN, item.getDatetime());
        cv.put(TITLE_COLUMN, "");
        cv.put(CONTENT_COLUMN, "");
        cv.put(LASTMODIFY_COLUMN, 0);
        cv.put(PITCURE_FILE, "");
        cv.put(START_COLUMN, (item.getId()-1)%24);
        cv.put(END_COLUMN, (item.getId()-1)%24);
        cv.put(COLOR_COLUMN, Colors.WHITE.parseColor());
        System.out.println("qwert " + item.getId());
        String where=KEY_ID+"="+item.getId();

        // 刪除指定編號資料並回傳刪除是否成功
        return db.update(TABLE_NAME, cv, where,  null) > 0;



    }
    // 刪除參數指定編號的資料
    public boolean delete(String id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = DATE + "=?" ;
        String[] whereValue={id};
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where, whereValue) > 0;
    }

    // 讀取所有記事資料
    public List<Item> getAll() {
        List<Item> result = new ArrayList<>();

        //哪張表、哪幾欄、條件式、條件式的篩選值、groupBy、having與 orderBy
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }
    //1
    public List<Item> getSome(String tid) {
        List<Item> result = new ArrayList<>();

        //哪張表、哪幾欄、條件式、條件式的篩選值、groupBy、having與 orderBy
        Cursor cursor = db.query(
                TABLE_NAME, null, "date=?", new String[]{tid}, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }
    //2
    public ArrayList<String> getList() {
        ArrayList<String> result = new ArrayList<>();
        String [] iid  =new String[100];
        for(int i=0;i<100;i++){
        iid[i]=String.valueOf(i*24+1);

        }
        //String[] iid={"1","25","49","73","97","121","145","169","193","217","241"};
        String selection =
                KEY_ID
                        + " IN ("
                        + makePlaceholdersForSelection(iid.length)
                        + ")";
        //哪張表、哪幾欄、條件式、條件式的篩選值、groupBy、having與 orderBy
        Cursor cursor = db.query(
                TABLE_NAME, new String[]{DATE},selection ,iid, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getA(cursor));
        }

        cursor.close();
        return result;
    }
    //copy
    private String makePlaceholdersForSelection(int length) {
        if (length < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(length * 2 - 1);
            sb.append("?");
            for (int i = 1; i < length; i++) {

                sb.append(",?");
            }
            return sb.toString();
        }
    }

    public void  set(Item item){

        Item wen=null;
        Cursor result = db.query(
                TABLE_NAME, null, "date=? and starttime=?", new String []{item.getTId(),String.valueOf(item.getStarttime())}, null,  null, null);
while(result.moveToNext()){
    wen=getRecord(result);
  //  System.out.println("qwer "+result.getLong(0));
    del(wen);
}



    }
    // 取得指定編號的資料物件
    public Item get(long id) {
        // 準備回傳結果用的物件
        Item item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        //哪張表、哪幾欄、條件式、條件式的篩選值、groupBy、having與 orderBy
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null,  null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }
    public  String getA(Cursor cursor){
        String aa = new String();

        aa=cursor.getString(0);


        return aa;
    }
    //starttime
    public Integer getB(Item item){
        long cc = item.getStarttime();

        int aa = (int)cc;


        return aa;
    }
    // 把Cursor目前的資料包裝為物件
    public Item getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Item result = new Item();
        result.setId(cursor.getLong(0));
        result.setTId(cursor.getString(1));
        result.setDatetime(cursor.getLong(2));
        result.setTitle(cursor.getString(3));
        result.setContent(cursor.getString(4));
        result.setLastModify(cursor.getLong(5));
        result.setFileName(cursor.getString(6));
        result.setStarttime(cursor.getLong(7));
        result.setEndtime(cursor.getLong(8));
        result.setColor(Note.getColors(cursor.getInt(9)));
        // 回傳結果
        return result;
    }





    // 取得資料數量
    public int getCount( String ddate) {
        int result = 0;

        String where  = DATE + "=" + ddate;
        Cursor cursor = db.query(
                TABLE_NAME, null, "date=?", new String[]{ddate}, null, null, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }
    public String[][] getupload(String [][]data){


        return data;
    }
    public String [][]upload(String tid){
        String data[][]=new String[5][24],temp[][];
        Item item =null;
int haha=0;
        Cursor cursor = db.query(TABLE_NAME,null,"date=?",new String[]{tid},null,null,null,null);
        while(cursor.moveToNext()) {
            item = getRecord(cursor);
            // System.out.println("qwer "+cursor.getLong(0));
            if (!item.getTitle().equals("")) {
                if (haha == 0) {
                    data[0][0] = item.getTitle();
                    data[1][0] = item.getContent();
                    data[2][0] = String.valueOf(item.getStarttime());
                    data[3][0] = String.valueOf(item.getEndtime()+1);
                    data[4][0] = String.valueOf(item.getColor().parseColor());
                    haha++;
                } else {
                    for (int i = 0; i < 24; i++) {
                        if (data[2][i]==String.valueOf(item.getStarttime())) {
                            break;
                        } else if (i == 23) {
                            data[0][haha] = item.getTitle();
                            data[1][haha] = item.getContent();
                            data[2][haha] = String.valueOf(item.getStarttime());
                            data[3][haha] = String.valueOf(item.getEndtime() + 1);
                            data[4][haha] = String.valueOf(item.getColor().parseColor());
                            haha++;
                        }
                    }

                }

            }
        }
        temp=new String[5][haha];
        for(int i=0;i<haha;i++) {
            for(int j=0;j<5;j++){
                temp[j][i]=data[j][i];
                //System.out.println("wwww "+data[j][i]);
            }
        }

        return temp;
    }
public void download(String name,String title[],String content[],String start[],String end[],int i,String pic[]){
int count=0;
  /*  for(int a=0;a<5;a++){
        System.out.println("tthe "+start[a]);
        System.out.println("tthee "+title[a]);

    }*/

    //System.out.println("tthe "+start[0]);

for(int j=0;j<Integer.valueOf(start[0]);j++){
        Item item = new Item(0,name, new Date().getTime(), "","", 0,"",j,j, Colors.WHITE);
        insert(item);
   // System.out.println("fir " + j);
count++;
}
    for(int m=0;m<i;) {
        if(Integer.valueOf(start[m])>count){
            Item item = new Item(0, name, new Date().getTime(), "", "", 0, "",count , count, Colors.WHITE);
            insert(item);
            count++;
        }
        else {
            for (int k = Integer.valueOf(start[m]); k < Integer.valueOf(end[m]); k++) {
                Item item = new Item(0, name, new Date().getTime(), title[m], content[m], 0, "", Integer.valueOf(start[m]), Integer.valueOf(end[m]) - 1,Note.getColors(Integer.valueOf(pic[m])) );
                insert(item);
               // System.out.println("sec " + m);
                count++;
            }
            m++;
        }
    }
    for(int n=Integer.valueOf(end[i-1]);n<24;n++){
        Item item = new Item(0, name, new Date().getTime(), "", "", 0, "", n, n-1, Colors.WHITE);
        insert(item);


    }

}
    // 建立範例資料
    public void sample(String cc) {
        for (int i=0;i<24;i++) {

            Item item = new Item(0,cc, new Date().getTime(), "","", 0,"",i,i, Colors.WHITE);


            insert(item);
        }
    }

}