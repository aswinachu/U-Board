package com.iit.t1.u_board.activity;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
/**
 * Created by Nivash on 11/5/2015.
 */
public class SubmitNoticeAsyncTask extends AsyncTask<UboardNotices, Void, Boolean> {
    @Override
    protected Boolean doInBackground(UboardNotices... arg0) {
        try
        {
            UboardNotices uboardNotices = arg0[0];

            QueryBuilderSubmitNotice qb = new QueryBuilderSubmitNotice();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(qb.buildContactsSaveURL());

            StringEntity params =new StringEntity(qb.createnotices(uboardNotices));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            if(response.getStatusLine().getStatusCode()<205)
            {
                //Log.d("SubmitNoticeAsyncTask", "Added the record");
                return true;
            }
            else
            {
                //Log.d("SubmitNoticeAsyncTask", "Failed to add record");
                return false;
            }
        } catch (Exception e) {
            //e.getCause();
            String val = e.getMessage();
            String val2 = val;
            return false;
        }
    }
}

