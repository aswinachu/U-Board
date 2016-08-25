package com.iit.t1.u_board.activity;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
/**
 * Created by Nivash on 11/5/2015.
 */

public class RegisterAsyncTask extends AsyncTask<Uboardusers, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Uboardusers... arg0) {
            try
            {
                Uboardusers uboardusers = arg0[0];

                QueryBuilder qb = new QueryBuilder();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(qb.buildContactsSaveURL());

                StringEntity params =new StringEntity(qb.createuser(uboardusers));
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);

                if(response.getStatusLine().getStatusCode()<205)
                {
                    Log.d("RegisterAsyncTask", "Added the record");
                    return true;
                }
                else
                {
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
