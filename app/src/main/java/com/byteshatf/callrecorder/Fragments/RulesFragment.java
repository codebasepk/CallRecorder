package com.byteshatf.callrecorder.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.byteshatf.callrecorder.AddRuleActivity;
import com.byteshatf.callrecorder.Helpers;
import com.byteshatf.callrecorder.R;

public class RulesFragment extends android.support.v4.app.Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View baseView;
    private Button mButton;
    private CheckBox mCheckBox;
    private Helpers mHelpers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.rules_fragment, container, false);
        mHelpers = new Helpers(getActivity());
        mButton = (Button) baseView.findViewById(R.id.button);
        mCheckBox = (CheckBox) baseView.findViewById(R.id.recording_enable);
        mCheckBox.setOnCheckedChangeListener(this);
        mCheckBox.setChecked(mHelpers.isSpecialRecordingEnabled());
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.recording_enable:
                mHelpers.specialRecordingEnabled(isChecked);
        }
    }
}
