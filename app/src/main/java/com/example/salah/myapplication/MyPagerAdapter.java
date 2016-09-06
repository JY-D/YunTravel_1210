package com.example.salah.myapplication;

/**
 * Created by salah on 2015/6/17.
 */
import java.util.List;

import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class MyPagerAdapter extends PagerAdapter {
    private List<View> mListViews;

    public MyPagerAdapter() {
        super();
    }

    public MyPagerAdapter(List<View> mListViews) {
        super();
        this.mListViews = mListViews;

    }

    @Override
    public void destroyItem(View collection, int position, Object arg2) {
        Log.d("k", "destroyItem");
        ((ViewPager) collection).removeView(mListViews.get(position));
    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub
        Log.d("k", "finishUpdate");
    }

    @Override
    public int getCount() {
        Log.d("k", "getCount");
        return mListViews.size();
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        Log.d("k", "instantiateItem");
        ((ViewPager) collection).addView(mListViews.get(position),0);
        return mListViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        Log.d("k", "isViewFromObject");
        return arg0==(arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub
        Log.d("k", "restoreState");
    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        Log.d("k", "saveState");
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
        // TODO Auto-generated method stub
        Log.d("k", "startUpdate");
    }

}