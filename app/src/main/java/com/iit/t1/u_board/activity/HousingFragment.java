package com.iit.t1.u_board.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.ListViewCompat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.share.widget.ShareDialog;
import com.iit.t1.u_board.R;
import com.iit.t1.u_board.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class HousingFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int SHARE_ID = Menu.FIRST + 2;
    private static final int UPDATE_ID = Menu.FIRST + 3;
    private static int refreshing;
    ShareDialog shareDialog;
    public  static String[] arrayOfHousing;
    public static  String[] arrayOfHousingTitle;
    public static String[] arrayofContactno;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<UboardNotices> returnValues = new ArrayList<UboardNotices>();
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayList<String>listItems1=new ArrayList<>();
    ArrayList<String> dummylist=new ArrayList<String>();
    ArrayList<String> contactlist=new ArrayList<>();
    ArrayList<String> passlistItems=new ArrayList<String>();
    ListViewCompat listview;
    public HousingFragment() {
        // Required empty public constructor
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/






    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
        setHasOptionsMenu(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(new int[]{R.color.Blue_700});
            housingNotices(0);

    }

    @Override
    public void onRefresh() {
        System.out.println("Refreshing");
        GetNoticeAsyncTask task = new GetNoticeAsyncTask();
        task.getCategory("Housing");
        try {
            returnValues = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for (UboardNotices x : returnValues) {
            // System.out.println(x.getCategory().toString());
            if (x.getCategory().toString().equalsIgnoreCase("Housing")) {
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
                    contactlist.removeAll(contactlist);
                    dummylist.removeAll(dummylist);
                    refreshing=1;
                    housingNotices(refreshing);
                }

            }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, R.string.menu_share);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="Share")
        {
                System.out.println("Sharing");
                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); //Need to change the build to API 19
                String smsText="Hey checkout the exciting app !";
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, smsText);

                if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
                {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                startActivity(sendIntent);

        }
        else {
            return false;
        }
        System.out.println("Returning");
        return true;

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void housingNotices( int refresh)
    {

        GetNoticeAsyncTask task = new GetNoticeAsyncTask();
        task.getCategory("Housing");
        try {
            returnValues = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for (UboardNotices x : returnValues) {
            // System.out.println(x.getCategory().toString());
            if (x.getCategory().toString().equalsIgnoreCase("Housing")) {
                //System.out.println("Inside If");
                listItems.add(x.getNoticeTitle());
                dummylist.add(x.getDescription());
                contactlist.add(x.getContacNo());
            } else {
                continue;
            }
        }
        arrayOfHousing = dummylist.toArray(new String[dummylist.size()]);
        arrayOfHousingTitle = listItems.toArray(new String[listItems.size()]);
        arrayofContactno=contactlist.toArray(new String[contactlist.size()]);
        ListAdapter listNotices = new ListAdapter(getActivity(), R.layout.custom_adapter_view, listItems, UboardNotices.noticepics);
        setListAdapter(listNotices);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getListView().setItemChecked(position, true);
                Fragment fragment = new ViewNoticeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragment = ViewNoticeFragment.newInstance(position);
                ViewNoticeFragment.findCategory(2);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                MainActivity ob = new MainActivity();
                ob.imageButton = (ImageButton) getActivity().findViewById(R.id.fab_add);
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
}

