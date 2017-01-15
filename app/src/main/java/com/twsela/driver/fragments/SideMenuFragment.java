package com.twsela.driver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twsela.driver.R;


/**
 * Created by Shamyyoun on 5/28/16.
 */
public class SideMenuFragment extends ParentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_side_menu, container, false);
        return rootView;
    }
}
