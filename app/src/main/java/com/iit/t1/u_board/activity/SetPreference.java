package com.iit.t1.u_board.activity;

/**
 * Created by Nivash on 12/9/2015.
 */

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.iit.t1.u_board.R;

public class SetPreference extends PreferenceActivity {


    public SetPreference() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub


        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.checkboxpref);



        //setHasOptionsMenu(true);


    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        return view;
    }*/


}