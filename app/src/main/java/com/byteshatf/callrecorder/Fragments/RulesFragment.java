package com.byteshatf.callrecorder.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.byteshatf.callrecorder.AddRuleActivity;
import com.byteshatf.callrecorder.R;

public class RulesFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private View baseView;
    private Button mButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.rules_fragment, container, false);
        mButton = (Button) baseView.findViewById(R.id.button);
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
}
