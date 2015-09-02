package com.byteshatf.callrecorder.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.byteshatf.callrecorder.AddRuleActivity;
import com.byteshatf.callrecorder.Helpers;
import com.byteshatf.callrecorder.R;

public class RulesFragment extends android.support.v4.app.Fragment implements View.OnClickListener,
        Spinner.OnItemSelectedListener {

    private View baseView;
    private Button mButton;
    private Spinner mSpinner;
    private Helpers mHelpers;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.rules_fragment, container, false);
        mHelpers = new Helpers(getActivity());
        mButton = (Button) baseView.findViewById(R.id.button);
        mSpinner = (Spinner) baseView.findViewById(R.id.main_spinner);
        mSpinner.setOnItemSelectedListener(this);
        String[] mainSpinner = {"All Incoming Calls", "All Outgoing Calls", "All Incoming/outgoing",
                "Unknown Calls Only","Selected Only"};
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                mainSpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setSelection(mHelpers.getSpinnerValue("key"));
        System.out.println(mHelpers.getSpinnerValue("key"));
        mButton.setOnClickListener(this);
        return baseView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startActivity(new Intent(getActivity(), AddRuleActivity.class));
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mHelpers.saveSpinnerState("key" ,parent.getSelectedItemPosition());
        System.out.println(parent.getSelectedItemPosition());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
