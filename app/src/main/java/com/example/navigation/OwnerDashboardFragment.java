package com.example.navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class OwnerDashboardFragment extends Fragment {

    Button add_member_button, add_category_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_owner_dashboard, container, false);

//************************************************************************************************************************************************8
        add_category_button = view.findViewById(R.id.add_category_btn);
        add_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
                startActivity(intent);
            }
        });
//***************************************************************************************************************************************************
        add_member_button = view.findViewById(R.id.add_btn);
        add_member_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMembersActivity.class);
                startActivity(intent);
            }
        });

//***************************************************************************************************************************************************





 return view;
    }

}