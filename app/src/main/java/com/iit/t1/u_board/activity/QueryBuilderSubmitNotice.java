package com.iit.t1.u_board.activity;

import android.util.Log;

/**
 * Created by Nivash on 11/5/2015.
 */
public class QueryBuilderSubmitNotice {
    /**
     * Specify your database name here
     * @return
     */
    public String getDatabaseName() {
        return "uboard";
    }

    /**
     * Specify your MongoLab API here
     * @return
     */
    public String getApiKey() {
        return "F7wyuSMnGD23BOG52pLnTPbfB3GJR-Ap";
    }

    /**
     * This constructs the URL that allows you to manage your database,
     * collections and documents
     * @return
     */
    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }

    /**
     * Completes the formating of your URL and adds your API key at the end
     * @return
     */
    public String docApiKeyUrl()
    {
        return "?apiKey="+getApiKey();
    }

    /**
     * Returns the usercredentials collection
     * @return
     */
    public String collectionRequest()
    {
        return "notices";
    }

    /**
     * Builds a complete URL using the methods specified above
     * @return
     */
    public String buildContactsSaveURL()
    {
        //Log.d("QueryBuilderSbmtNotices", "Returning the user");
        return getBaseUrl()+collectionRequest()+docApiKeyUrl();
    }

    /**
     * Formats the contact details for MongoHQ Posting
     * @param uboardNotices: Details of the Uboardusers
     * @return
     */
    public String createnotices(UboardNotices uboardNotices)
    {
        Log.d("QuerySubmitNotice","createnotices");
        return String
                .format("{\"document\"  : {\"noticetitle\": \"%s\", "
                                + "\"category\": \"%s\", \"description\": \"%s\", "
                                + "\"contactno\": \"%s\"}, \"owner\" : \"%s\"}",
                        uboardNotices.noticeTitle,uboardNotices.category, uboardNotices.description,uboardNotices.contactNo,uboardNotices.owner);
    }
}
