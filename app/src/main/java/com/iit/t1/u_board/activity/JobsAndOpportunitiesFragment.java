package com.iit.t1.u_board.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ListViewCompat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.iit.t1.u_board.R;
import com.iit.t1.u_board.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by CREATIVITY on 11/4/2015.
 */
public class JobsAndOpportunitiesFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int SHARE_ID = Menu.FIRST + 2;
    private static final int UPDATE_ID = Menu.FIRST + 3;
    private static int refreshing;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String>listItems1=new ArrayList<>();
    public  static String[] arrayOfJobs;
    public static  String[] arrayOfJobsTitle;
    public  static String[] arrayofJobContact;
    ArrayList<String> dummylist=new ArrayList<String>();
    ArrayList<UboardNotices> returnValues = new ArrayList<UboardNotices>();
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayList<String> jobcontactItems=new ArrayList<String>();
    ListViewCompat listview;

    public JobsAndOpportunitiesFragment() {
        // Required empty public constructor
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_jobs_and_opportunities, container, false);


            // Inflate the layout for this fragment
            return rootView;
        }*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(new int[]{R.color.Blue_700});
        jobNotices(0);


    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,UPDATE_ID, 0, R.string.menu_update);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        menu.add(0, SHARE_ID, 0, R.string.menu_share);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                return true;
            case UPDATE_ID:
                return true;
            case SHARE_ID:
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onRefresh() {
        System.out.println("Refreshing");
        GetNoticeAsyncTask task = new GetNoticeAsyncTask();
        task.getCategory("Jobs and Internships");
        try {
            returnValues = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for (UboardNotices x : returnValues) {
            // System.out.println(x.getCategory().toString());
            if (x.getCategory().toString().equalsIgnoreCase("Jobs and Internships")) {
                //System.out.println("Inside If");
                listItems1.add(x.getNoticeTitle());
            }
        }
                if(listItems1.equals(listItems)) {
                    Toast.makeText(getActivity(), "No New Notices", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    listItems1.removeAll(listItems1);
                }
                else
                {
                    listItems.removeAll(listItems);
                    jobcontactItems.removeAll(jobcontactItems);
                    dummylist.removeAll(dummylist);
                    refreshing=1;
                    jobNotices(refreshing);
                }

            }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void jobNotices(int refresh)
    {
        GetNoticeAsyncTask task = new GetNoticeAsyncTask();
        task.getCategory("Jobs and Internships");
        try {
            returnValues = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(UboardNotices x: returnValues) {
            //System.out.println(x.getCategory().toString());
            if (x.getCategory().toString().equalsIgnoreCase("Jobs and Internships")) {
                //System.out.println("Inside If");
                listItems.add(x.getNoticeTitle());
                dummylist.add(x.getDescription());
                jobcontactItems.add(x.getContacNo());
            }else
            {
                continue;
            }
        }
        arrayOfJobs = dummylist.toArray(new String[dummylist.size()]);
        arrayOfJobsTitle=listItems.toArray(new String[listItems.size()]);
        arrayofJobContact=jobcontactItems.toArray(new String[jobcontactItems.size()]);
        ListAdapter listNotices= new ListAdapter(getActivity(), R.layout.custom_adapter_view,listItems, UboardNotices.noticepics);
        setListAdapter(listNotices);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getListView().setItemChecked(position, true);
                Fragment fragment = new ViewNoticeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragment = ViewNoticeFragment.newInstance(position);
                ViewNoticeFragment.findCategory(4);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                MainActivity ob =new MainActivity();
                ob.imageButton=(ImageButton)getActivity().findViewById(R.id.fab_add);
                ob.imageButton.setVisibility(View.INVISIBLE);
                fragmentTransaction.commit();
            }
        });
        if(refresh==1) {
            refreshing=0;
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "New Notices Updated", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}

