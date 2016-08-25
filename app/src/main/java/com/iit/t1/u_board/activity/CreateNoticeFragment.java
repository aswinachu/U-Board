package com.iit.t1.u_board.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.res.Configuration;
import com.iit.t1.u_board.R;

import java.net.UnknownHostException;

public class CreateNoticeFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
    EditText editText_NoticeTitle;
    EditText editText_Description;
    Spinner spinner_category;
    EditText editText_ContactNo;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String sales="Sales";
    public CreateNoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe);
        //mSwipeRefreshLayout.setOnRefreshListener(this);
        //mSwipeRefreshLayout.setRefreshing(false);


    }
    public void SubmitNotice(View v) throws UnknownHostException {

        //Intent myIntent = new Intent(CreateNoticeFragment.this, SalesFragment.class);// have to change from one fragment to another
        //CreateNoticeFragment.this.startActivity(myIntent);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_notice, container, false);

        editText_NoticeTitle = (EditText) rootView.findViewById(R.id.editTextNoticetitle);
        editText_Description = (EditText) rootView.findViewById(R.id.editTextDescription);
        spinner_category=(Spinner)rootView.findViewById(R.id.spinnercategory);
        editText_ContactNo=(EditText) rootView.findViewById(R.id.editTextNoticecontact);
        // Inflate the layout for this fragment
        Button submitButton= (Button)rootView.findViewById(R.id.buttoncreatenotice);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_NoticeTitle.getText().toString().equals("")) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Invalid Input");
                    alert.setMessage("Title field is empty. Please enter title !");
                    alert.setIcon(R.drawable.ic_alert1);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.create();
                    alert.show();
                } else if (editText_ContactNo.getText().toString().equals("")) {

                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Invalid Input");
                    alert.setMessage("Contact No field is empty. Please enter contact no !");
                    alert.setIcon(R.drawable.ic_alert1);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.create();
                    alert.show();

                }else if (editText_Description.getText().toString().equals("")) {

                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Invalid Input");
                    alert.setMessage("Description field is empty. Please enter Description !");
                    alert.setIcon(R.drawable.ic_alert1);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.create();
                    alert.show();


                }else if (editText_NoticeTitle.getText().toString().equals("") && editText_Description.getText().toString().equals("")) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Invalid Input");
                    alert.setMessage("Please enter title and description");
                    alert.setIcon(R.drawable.ic_alert1);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.create();
                    alert.show();

                } else {
                    UboardNotices uboardNotices = new UboardNotices();
                    uboardNotices.noticeTitle = editText_NoticeTitle.getText().toString();
                    uboardNotices.description = editText_Description.getText().toString();
                    uboardNotices.category = spinner_category.getSelectedItem().toString();
                    uboardNotices.contactNo=editText_ContactNo.getText().toString();

                    SubmitNoticeAsyncTask tsk = new SubmitNoticeAsyncTask();
                    tsk.execute(uboardNotices);
                    if (uboardNotices.category.equalsIgnoreCase("Sales")) {
                        Fragment fragment = new SalesFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity())
                                .setActionBar("Sales");

                        Toast.makeText(getActivity(), "Your post regarding sale posted ! ", Toast.LENGTH_SHORT).show();
                        showNotification(uboardNotices.noticeTitle,"New Sale");

                    } else if (uboardNotices.category.equalsIgnoreCase("Housing")) {
                        Fragment fragment = new HousingFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity())
                                .setActionBar("Housing");
                        Toast.makeText(getActivity(), "Your post regarding Housing posted ! ", Toast.LENGTH_SHORT).show();
                        showNotification(uboardNotices.noticeTitle,"New Housing");

                    } else if (uboardNotices.category.equalsIgnoreCase("Jobs and Internships")) {
                        Fragment fragment = new JobsAndOpportunitiesFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity())
                                .setActionBar("Jobs and Opportunities");
                        Toast.makeText(getActivity(), "Your post regarding Jobs posted ! ", Toast.LENGTH_SHORT).show();
                        showNotification(uboardNotices.noticeTitle,"New Job");

                    } else if (uboardNotices.category.equalsIgnoreCase("Events")) {
                        Fragment fragment = new EventsFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity())
                                .setActionBar("Events");
                        Toast.makeText(getActivity(), "Your post regarding upcoming Events posted ! ", Toast.LENGTH_SHORT).show();
                        showNotification(uboardNotices.noticeTitle," New Event");
                    }
                }
            }


        });

        return rootView;
    }


    public void showNotification(String title, String category){

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // intent triggered, you can add other intent for other actions

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this.getActivity())
                .setContentTitle(category)
                .setContentText(title)
                .setSmallIcon(R.drawable.applogo,20)

                .setSound(soundUri)
                .build();

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);
        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, mNotification);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());
    }
    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        View subview = inflater.inflate(R.layout.fragment_new_notice, viewGroup);

        // Find your buttons in subview, set up onclicks, set up callbacks to your parent fragment or activity here.

        // You can create ViewHolder or separate method for that.
        // example of accessing views: TextView textViewExample = (TextView) view.findViewById(R.id.text_view_example);
        // textViewExample.setText("example");
    }
}
