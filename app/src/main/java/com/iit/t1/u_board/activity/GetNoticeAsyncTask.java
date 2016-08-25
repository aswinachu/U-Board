package com.iit.t1.u_board.activity;
import android.os.AsyncTask;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
/**
 * Created by Nivash on 10/30/2015.
 */
public class GetNoticeAsyncTask extends AsyncTask <UboardNotices, Void, ArrayList<UboardNotices>> {
    static BasicDBObject user = null;
    static String OriginalObject = "";
    static String server_output = null;
    static String temp_output = null;
    static String categoryType;
    public void getCategory(String category)
    {
        categoryType=category;
    }

    @Override
    protected ArrayList<UboardNotices> doInBackground(UboardNotices... arg0) {

        ArrayList<UboardNotices> mycontacts = new ArrayList<UboardNotices>();
        try
        {
            QueryBuilderSubmitNotice qb = new QueryBuilderSubmitNotice();
            URL url = new URL(qb.buildContactsSaveURL());
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
              //  Log.d("GetNotice",server_output);
            }

// create a basic db list

            String mongoarray = "{ artificial_basicdb_list: "+server_output+"}";
            Object o = com.mongodb.util.JSON.parse(mongoarray);
            //Log.d("GetNotice",mongoarray);
            DBObject dbObj = (DBObject) o;
            BasicDBList contacts = (BasicDBList) dbObj.get("artificial_basicdb_list");
            //Log.d("GetNotice","Getinng list");
            for (Object obj : contacts) {
                //Log.d("Inside Loop", "Inside Loop");
                DBObject userObj = (DBObject) obj;
                //Log.d("Inside Loop", "Inside Loop--1");
                UboardNotices temp = new UboardNotices();
                temp.setId(userObj.get("_id").toString());
                //Log.d("Inside Loop", "Inside Loop--2");
                DBObject newobj= (DBObject) userObj.get("document");
                temp.setnoticeTitle(newobj.get("noticetitle").toString());
                //Log.d("Inside Loop", "Inside Loop--3");
                temp.setDescription(newobj.get("description").toString());
                //Log.d("Inside Loop", "Inside Loop--4");
                temp.setCategory(newobj.get("category").toString());
                //Log.d("GetNoticeBrd",temp.toString());
                temp.setContactNo(newobj.get("contactno").toString());
                if(temp.getCategory().toString().equalsIgnoreCase(categoryType)) {
                    mycontacts.add(temp);
                }
            }

        }catch (Exception e) {
            e.getMessage();
        }

        return mycontacts;
    }
}



