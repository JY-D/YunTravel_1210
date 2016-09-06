package com.example.salah.myapplication;

import java.util.Date;
import java.util.Locale;

/**
 * Created by 2wsx1000g on 2015/8/17.
 */
public class Item implements java.io.Serializable{

    // 編號、日期時間、顏色、標題、內容、檔案名稱、經緯度、修改、已選擇
    private long id;
    private long datetime;
    private String title;
    private String content;
    private String fileName;
    private Colors color;
    private double latitude;
    private double longitude;
    private long lastModify;
    private boolean selected;
    private long alarmDatetime;
    private String tid;
    private long startTime;
    private long endTime;
    public Item() {
        title = "";
        content = "";

        color = Colors.WHITE;

    }

    public Item(long id,String tid, long datetime,  String title,
                String content,
                long lastModify,String fileName,long startTime,long endTime,Colors color) {
        this.id = id;
        this.tid = tid;
        this.datetime = datetime;
        this.title = title;
        this.content = content;
        this.color = color;
        this.lastModify = lastModify;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTId() {
        return tid;
    }

    public void setTId(String tid) {
        this.tid = tid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDatetime() {
        return datetime;
    }

    // 裝置區域的日期時間
    public String getLocaleDatetime() {
        return String.format(Locale.getDefault(), "%tF  %<tR", new Date(datetime));
    }

    // 裝置區域的日期
    public String getLocaleDate() {
        return String.format(Locale.getDefault(), "%tF", new Date(datetime));
    }

    // 裝置區域的時間
    public String getLocaleTime() {
        return String.format(Locale.getDefault(), "%tR", new Date(datetime));
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public long getStarttime() {
        return startTime;
    }

    public void setStarttime(long starttime) {
        this.startTime = starttime;
    }

    public long getEndtime() {
        return endTime;
    }

    public void setEndtime(long endtime) {
        this.endTime = endtime;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getAlarmDatetime() {
        return alarmDatetime;
    }

    public void setAlarmDatetime(long alarmDatetime) {
        this.alarmDatetime = alarmDatetime;
    }
}
