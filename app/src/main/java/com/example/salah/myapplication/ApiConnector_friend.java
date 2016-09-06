package com.example.salah.myapplication;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by EB209 on 2015/11/25.
 */
public class ApiConnector_friend {

    public static String ip = "http://140.125.45.241/tutorial/"; // 實驗室
    public static String ip_d = "http://140.125.45.241/tutorial_don/"; // 實驗室
    public static String ip_r = "http://140.125.45.241/tutorial_ran/"; // 實驗室
    //public static String ip = "http://192.168.1.101/"; // 家裡

    public JSONArray GetAllCustomers(String login_per)
    {
        // URL for getting all customers
        String url=ip;
        if (login_per.equals("salah"))
            url = ip+"getAllCustomers.php/";
        else if (login_per.equals("董哥"))
            url = ip_d+"getAllCustomers.php/";
        else if (login_per.equals("睿恩"))
            url = ip_r+"getAllCustomers.php/";

        // Get HttpResponse Object from url.
        // Get HttpEntity from Http Response Object

        HttpEntity httpEntity = null;

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();



        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Here



        } catch (IOException e) {
            e.printStackTrace();
        }


        // Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;

        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity, HTTP.UTF_8);

                Log.e("Entity Response  : ", entityResponse);

                jsonArray = new JSONArray(entityResponse);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;


    }
    public JSONArray GetSpotDetails(int SpotID,String login_per) {
        // URL for getting all customers

        String url=ip;
        if (login_per.equals("salah"))
            url = ip+"getSpotDetails.php?SpotID="+SpotID;
        else if (login_per.equals("董哥"))
            url = ip_d+"getSpotDetails.php?SpotID="+SpotID;
        else if (login_per.equals("睿恩"))
            url = ip_r+"getSpotDetails.php?SpotID="+SpotID;

        // Get HttpResponse Object from url.
        // Get HttpEntity from Http Response Object

        HttpEntity httpEntity = null;

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();



        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Here



        } catch (IOException e) {
            e.printStackTrace();
        }


        // Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;

        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity, HTTP.UTF_8);

                Log.e("Entity Response  : ", entityResponse);

                jsonArray = new JSONArray(entityResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;

    }

    public JSONArray GetSearchDetails(String SpotTitle, String SpotRegion, String SpotType,String login_per) {
        // URL for getting all customers
        String url=ip;
        if (login_per.equals("salah"))
            url = ip+"getSearchDetails.php?SpotTitle="+SpotTitle+"&SpotRegion="+SpotRegion+"&SpotType="+SpotType;
        else if (login_per.equals("董哥"))
            url = ip_d+"getSearchDetails.php?SpotTitle="+SpotTitle+"&SpotRegion="+SpotRegion+"&SpotType="+SpotType;
        else if (login_per.equals("睿恩"))
            url = ip_r+"getSearchDetails.php?SpotTitle="+SpotTitle+"&SpotRegion="+SpotRegion+"&SpotType="+SpotType;

        // Get HttpResponse Object from url.
        // Get HttpEntity from Http Response Object

        HttpEntity httpEntity = null;

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();



        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Here



        } catch (IOException e) {
            e.printStackTrace();
        }


        // Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;

        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity, HTTP.UTF_8);

                Log.e("Entity Response  : ", entityResponse);

                jsonArray = new JSONArray(entityResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;

    }

    public Boolean uploadImageToserver(List<NameValuePair> params,String login_per) {
        // URL for getting all customers
        String url=ip;
        if (login_per.equals("salah"))
            url = ip+"uploadImage.php";
        else if (login_per.equals("董哥"))
            url = ip_d+"uploadImage.php";
        else if (login_per.equals("睿恩"))
            url = ip_r+"uploadImage.php";

        HttpEntity httpEntity = null;

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            httpEntity = httpResponse.getEntity();
            String entityResponse = EntityUtils.toString(httpEntity);

            Log.e("Entity Response  : ", entityResponse);
            return true;

        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Her


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject GetCurrentMaxId(String login_per)
    {
        //JSONObject Try
        // URL for getting Current Max Id
        String url=ip;
        if (login_per.equals("salah"))
            url = ip+"getCurrentMaxId.php/";
        else if (login_per.equals("董哥"))
            url = ip_d+"getCurrentMaxId.php/";
        else if (login_per.equals("睿恩"))
            url = ip_r+"getCurrentMaxId.php/";

        // Get HttpResponse Object from url.
        // Get HttpEntity from Http Response Object

        HttpEntity httpEntity = null;

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();

        } catch (ClientProtocolException e) {
            // Signals error in http protocol
            e.printStackTrace();
            //Log Errors Here
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert HttpEntity into JSON Object
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;

        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity, HTTP.UTF_8);

                Log.e("Entity Response  : ", entityResponse);

                //jsonArray = new JSONArray(entityResponse);
                jsonObject = new JSONObject(entityResponse);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //return jsonArray;
        return jsonObject;

    }

    public Boolean addSpotDetailToServer(List<NameValuePair> params,String login_per) {

        String url=ip;
        if (login_per.equals("salah"))
            url = ip+"addSpotDetails.php";
        else if (login_per.equals("董哥"))
            url = ip_d+"addSpotDetails.php";
        else if (login_per.equals("睿恩"))
            url = ip_r+"addSpotDetails.php";
        // URL for getting all customers

        HttpEntity httpEntity = null;

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            httpEntity = httpResponse.getEntity();
            String entityResponse = EntityUtils.toString(httpEntity);

            Log.e("Entity Response  : ", entityResponse);
            return true;

        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Her


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean changeSpotDetailToServer(List<NameValuePair> params,String login_per) {

        String url=ip;
        if (login_per.equals("salah"))
            url = ip+"changeSpotDetails.php";
        else if (login_per.equals("董哥"))
            url = ip_d+"changeSpotDetails.php";
        else if (login_per.equals("睿恩"))
            url = ip_r+"changeSpotDetails.php";
        // URL for getting all customers

        HttpEntity httpEntity = null;

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            httpEntity = httpResponse.getEntity();
            String entityResponse = EntityUtils.toString(httpEntity);

            Log.e("Entity Response  : ", entityResponse);
            return true;

        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Her


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean deleteSpot(int SId,String login_per) {

        // URL for getting all customers
        String url=ip;
        if (login_per.equals("salah"))
            url = ip+"deleteSpot.php?id=" + SId;
        else if (login_per.equals("董哥"))
            url = ip_d+"deleteSpot.php?id=" + SId;
        else if (login_per.equals("睿恩"))
            url = ip_r+"deleteSpot.php?id=" + SId;

        HttpEntity httpEntity = null;

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();
            String entityResponse = EntityUtils.toString(httpEntity);

            Log.e("Entity Response  : ", entityResponse);
            return true;

        } catch (ClientProtocolException e) {
            // Signals error in http protocol
            e.printStackTrace();
            //Log Errors Her
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean addSpotToList(List<NameValuePair> params) {

        // URL for getting all customers
        String url = ip+"addSpotToList.php";

        HttpEntity httpEntity = null;

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            httpEntity = httpResponse.getEntity();
            String entityResponse = EntityUtils.toString(httpEntity);

            Log.e("Entity Response  : ", entityResponse);
            return true;

        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Her


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
