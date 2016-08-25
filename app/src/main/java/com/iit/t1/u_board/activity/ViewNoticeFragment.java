package com.iit.t1.u_board.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iit.t1.u_board.R;

import java.util.ArrayList;

/**
 * Created by Nivash on 11/25/2015.
 */
public class ViewNoticeFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {
    ArrayList<UboardNotices> returnValues = new ArrayList<UboardNotices>();
    ImageView  imageview;
    TextView textview;
    TextView textview1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    TextView contactnotextarea;
    private static  int categoryIndex=0;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(false);
        View scroller = inflater.inflate(R.layout.fragment_viewnotice, container , false);
        imageview=(ImageView) scroller.findViewById(R.id.imageView3);
        imageview.setImageResource(R.drawable.viewnoticeimage);
        textview1=(TextView)scroller.findViewById(R.id.textView11);
        textview=(TextView)scroller.findViewById(R.id.textView9);
        contactnotextarea=(TextView)scroller.findViewById(R.id.contactnotextarea);
        contactnotextarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact=contactnotextarea.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+contact));
                startActivity(callIntent);
            }
        });
        if(categoryIndex==1) {
            textview.setText(com.iit.t1.u_board.activity.SalesFragment.arrayOfSales[getShownIndex()]);
            textview1.setText(com.iit.t1.u_board.activity.SalesFragment.arrayOfSalesTitle[getShownIndex()]);
            contactnotextarea.setText(SalesFragment.arrayofsaleContactno[getShownIndex()]);
            MainActivity obj=new MainActivity();

        }else if(categoryIndex==2)
        {

            textview.setText(com.iit.t1.u_board.activity.HousingFragment.arrayOfHousing[getShownIndex()]);
            textview1.setText(com.iit.t1.u_board.activity.HousingFragment.arrayOfHousingTitle[getShownIndex()]);
            contactnotextarea.setText(HousingFragment.arrayofContactno[getShownIndex()]);
        }else if(categoryIndex==3)
        {

            textview.setText(com.iit.t1.u_board.activity.EventsFragment.arrayOfEvents[getShownIndex()]);
            textview1.setText(com.iit.t1.u_board.activity.EventsFragment.arrayOfEventsTitle[getShownIndex()]);
            contactnotextarea.setText(EventsFragment.arrayOfEventContact[getShownIndex()]);
        } else if(categoryIndex==4)
        {

            textview.setText(JobsAndOpportunitiesFragment.arrayOfJobs[getShownIndex()]);
            textview1.setText(JobsAndOpportunitiesFragment.arrayOfJobsTitle[getShownIndex()]);
            contactnotextarea.setText(JobsAndOpportunitiesFragment.arrayofJobContact[getShownIndex()]);
        }
        return scroller;



    }
    public static void findCategory(int category)
    {
    categoryIndex=category;
    }
    public int getShownIndex() {


        return getArguments().getInt("index", 0);
    }

    public static ViewNoticeFragment newInstance(int index) {
        ViewNoticeFragment f = new ViewNoticeFragment();


        Bundle args = new Bundle();
        args.putInt("index", index);


        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}

