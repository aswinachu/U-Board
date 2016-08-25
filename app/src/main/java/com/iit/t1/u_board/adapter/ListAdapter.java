package com.iit.t1.u_board.adapter;

/**
 * Created by Nivash on 11/5/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.iit.t1.u_board.R;

import java.util.ArrayList;


public class ListAdapter extends ArrayAdapter<String> {
    Context mContext;
    int resource;
    ArrayList<String> data = null;
    int pics[] = null;

    public ListAdapter(Context context, int resource,ArrayList<String> data, int []pics) {
        super(context, resource,data);
        this.resource = resource;
        this.mContext = context;
        this.data = data;
        this.pics = pics;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        // object item based on the position
        String objectItem = data.get(position);
        int pic = pics[0];

        // get the TextView and then set the text (item name) and tag (item ID) values

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image1);
        imageView.setBackgroundResource(pic);

        TextView textViewItem = (TextView) convertView.findViewById(R.id.noticetitle);
        textViewItem.setText(objectItem);
        textViewItem.setTag(objectItem);

        return convertView;

    }
}

