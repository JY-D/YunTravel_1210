package com.example.salah.myapplication;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;*/

public class NewsList extends BaseAdapter {
    private JSONArray dataArray;
    private Activity activity;

   // private static final String baseUrlForImage = ApiConnector_allspots.ip + "img_public/"; //Url for Img folder

    private static LayoutInflater inflater = null;

    public NewsList(JSONArray jsonArray, Activity a)
    {
        this.dataArray = jsonArray;
        this.activity = a;

        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.dataArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // set up convert view if it is null
        final ListCell cell;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.get_all_news_list_cell, null);
            cell = new ListCell();

            cell.FullName = (TextView) convertView.findViewById(R.id.spot_title);
            //cell.coords =  (TextView) convertView.findViewById(R.id.spot_coords);
            cell.address = (TextView) convertView.findViewById(R.id.spot_addr);

            cell.type = (ImageView) convertView.findViewById(R.id.spot_type);
            cell.select = (ImageView) convertView.findViewById(R.id.select);

            convertView.setTag(cell);
        }
        else
        {
            cell = (ListCell) convertView.getTag();
        }

        // change the data of cell
        try{
            JSONObject jsonObject = this.dataArray.getJSONObject(position);
            cell.FullName.setText(jsonObject.getString("title"));
            //cell.coords.setText(jsonObject.getString("coords"));
            cell.address.setText(jsonObject.getString("addr"));

            String type = jsonObject.getString("type");                                     //用型別

            if(type.equals("觀光"))
            {
                cell.type.setImageResource(R.drawable.icon_1_landscape);
            }
            else if(type.equals("購物"))
            {
                cell.type.setImageResource(R.drawable.icon_8_shopping);
            }
            else if(type.equals("餐廳"))
            {
                cell.type.setImageResource(R.drawable.icon_7_restaurant);
            }
            else if(type.equals("風景"))
            {
                cell.type.setImageResource(R.drawable.icon_2_scenic);
            }
            else if(type.equals("人文"))
            {
                cell.type.setImageResource(R.drawable.icon_3_humanities);
            }
            else if(type.equals("娛樂"))
            {
                cell.type.setImageResource(R.drawable.icon_4_fun);
            }
            else if(type.equals("運動"))
            {
                cell.type.setImageResource(R.drawable.icon_9_sport);
            }
            else if(type.equals("美食"))
            {
                cell.type.setImageResource(R.drawable.icon_6_food);
            }
            else if(type.equals("住宿"))
            {
                cell.type.setImageResource(R.drawable.icon_5_rest);
            }
            else
            {
                cell.type.setImageResource(R.drawable.logo);
            }


          /*  String nameOfImage = jsonObject.getString("pic");
            String urlForImageInServer = baseUrlForImage + nameOfImage;

            new AsyncTask<String, Void, Bitmap>()
            {
                @Override
                protected Bitmap doInBackground(String... params) {
                    //download Img
                    String url = params[0];
                    Bitmap icon = null;

                    try {
                        InputStream in = new URL(url).openStream();
                        icon = BitmapFactory.decodeStream(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return icon;
                }

               @Override
                protected void onPostExecute(Bitmap result) {
                    cell.type.setImageBitmap(result);
                }
            }.execute(urlForImageInServer);*/  // Lsit 圖片抓取暫時取消功能

        }catch (JSONException e)
        {
            e.printStackTrace();
        }


        return convertView;
    }

    public void removeItemAt(int position) {
        if(this.dataArray.length() > 0) {
            dataArray.remove(position);
            this.notifyDataSetChanged();
        }
    }

    private class ListCell
    {
        private TextView FullName;
        private TextView coords;
        private TextView address;

        private ImageView select;
        private ImageView type;
    }
}
