package com.iit.t1.u_board.activity;

import com.iit.t1.u_board.R;

/**
 * Created by Nivash on 10/30/2015.
 */
    public class UboardNotices {
    public String noticeTitle;
    public String category;
    public String getFullname;// getfullname from uboardusers
    public String description;
    public String date;
    public String id;
    public String document;
    public String contactNo;
    public String owner;
    public static int[] noticepics = {R.drawable.ic_launcher_negative};
    // send current sys date

    public void setId(String id) {
        this.id = id;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setCategory(String category) {
        this.category = category;
        //System.out.println(this.category);
    }

    public void setDescription(String description) {
        this.description = description;
        //System.out.println(this.description);
    }

    public void setnoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
        //System.out.println(this.noticeTitle);
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
    return id;
    }

    public String getDescription() {
        return description;
    }
    public String getContacNo()
    {
        return contactNo;
    }
    public String getOwner(){return owner;}
    public void setOwner(String owner){this.owner=owner;}
    public void setContactNo(String contactNo)
    {
        this.contactNo=contactNo;
    }
}
