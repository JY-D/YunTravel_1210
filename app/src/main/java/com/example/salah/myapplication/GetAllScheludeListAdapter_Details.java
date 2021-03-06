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

/**
 * Created by EB209 on 2015/12/6.
 */
public class GetAllScheludeListAdapter_Details extends BaseAdapter {

    private JSONArray dataArray;
    private Activity activity;
    private static LayoutInflater inflater = null;

    public GetAllScheludeListAdapter_Details(JSONArray jsonArray, Activity a)
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
            convertView = inflater.inflate(R.layout.get_schelude_details_list_cell, null);
            cell = new ListCell();
            cell.title = (TextView) convertView.findViewById(R.id.schelude_title);
            cell.content = (TextView) convertView.findViewById(R.id.schelude_content);
            cell.start = (TextView) convertView.findViewById(R.id.schelude_start);
            cell.end = (TextView) convertView.findViewById(R.id.schelude_end);
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
            cell.title.setText(jsonObject.getString("title"));
            cell.content.setText(jsonObject.getString("contents"));
            cell.start.setText(jsonObject.getString("start"));
            cell.end.setText(jsonObject.getString("end"));
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ListCell
    {
        private TextView title;
        private TextView content;
        private TextView start;
        private TextView end;
        private ImageView select;
    }
}
